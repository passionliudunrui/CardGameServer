package com.cardgameserver.netty;

import com.cardgameserver.dao.AccountDao;
import com.cardgameserver.entity.Account;
import com.cardgameserver.entity.Order;
import com.cardgameserver.proto.MessagePOJO;
import com.cardgameserver.service.AccountService;
import com.cardgameserver.service.OrderService;
import com.cardgameserver.utils.SpringUtil;
import com.cardgameserver.utils.Transfrom;
import com.cardgameserver.vo.UserVo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.awt.font.TransformAttribute;

/**
 * 处理购买的逻辑    正常购买 20yuan  100happybean
 *                 周六晚搞活动  10yuan  100happybean
 */
@Slf4j
public class MyServerHandlerBuy extends SimpleChannelInboundHandler<MessagePOJO.Message> {

    private UserVo userVo;
    private ChannelHandlerContext ctx;
    private static OrderService orderService;
    private static AccountService accountService;

    public void setUserVo(UserVo userVo){
        this.userVo=userVo;
    }
    static {
        orderService= SpringUtil.getBean(OrderService.class);
        accountService=SpringUtil.getBean(AccountService.class);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessagePOJO.Message message) throws Exception {
        this.ctx=ctx;
        int id1=message.getId1();
        int id2=message.getId2();
        String context=message.getContext();
        //普通的购买系统
        if(id1==9){
            System.out.println("用户要买东西");
            /**    下单  减库存  付钱  比较的复杂  简化秒杀业务  不必付款直接选择购买的时候就执行秒杀的活动
             * 1.用户下单                            客户发来 9,1
             * 2.判断用户的金额是否足够                如果不足服务端返回 9,2
             * 3.数据库中 user 的balance--  happybean++   服务器返回9,1 购买完成
             */
            //判断用户的金额是否足够

            if(userVo.getBalance()<20){
                MessagePOJO.Message message1 = Transfrom.transform(9, 2, "余额不足");
                ctx.writeAndFlush(message1);
            }
            else{
                //正常购买  1号商品  普通购买  20yuan  100happybean
                Order order=new Order(userVo.getId(),1,20,100,"普通购买");
                /*
                更新内存中的数据  更新数据库的数据
                 */
                userVo.setBalance(userVo.getBalance()-20);
                userVo.setHappybean(userVo.getHappybean()+100);
                Account account=new Account(userVo.getId(), userVo.getBalance(), userVo.getHappybean());
                /*
                更新前面handler中的userVo的数据
                 */
                MyServerHandlerPlay myServerHandlerPlay = ctx.pipeline().get(MyServerHandlerPlay.class);
                MyServerHandlerInfo myServerHandlerInfo = ctx.pipeline().get(MyServerHandlerInfo.class);
                myServerHandlerPlay.setUserVo(userVo);
                myServerHandlerInfo.setUserVo(userVo);


                int ans1=accountService.update(account);
                int ans = orderService.insert(order);

                int orderId=order.getId();
                MessagePOJO.Message message1 = Transfrom.transform(9, 1, "购买成功 100happybean已经成功到账。 订单编号 "+orderId);
                ctx.writeAndFlush(message1);

            }


        }
        //秒杀系统的设计
        else{






        }

    }
}
