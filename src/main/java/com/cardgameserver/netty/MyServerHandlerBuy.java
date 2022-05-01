package com.cardgameserver.netty;

import com.cardgameserver.entity.Account;
import com.cardgameserver.entity.Order;
import com.cardgameserver.proto.MessagePOJO;
import com.cardgameserver.rabbitmq.MQSender;
import com.cardgameserver.service.AccountService;
import com.cardgameserver.service.OrderService;
import com.cardgameserver.utils.JsonUtils;
import com.cardgameserver.utils.SpringUtil;
import com.cardgameserver.utils.Transfrom;
import com.cardgameserver.vo.SeckillMessage;
import com.cardgameserver.vo.UserVo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

/**
 * 处理购买的逻辑    正常购买 20yuan  100happybean
 *                 周六晚搞活动  10yuan  100happybean
 *
 *                 服务端收到id1=8  也就是客户端想要秒杀了
 */

/**
 * 详细秒杀设计
 * 秒杀业务中redis存放的信息
 * 1.当前预减库存生成的userId+goodsId  订单  (order:userId:goodsId,secKillOrder)   //判断重复抢购
 * 2.JVM中的内存标记   map<long,boolean>  id表示商品id   boolean表示是否为空
 * 3.是否库存为空  (isStockEmpty:goodsId,0)
 *
 */

@Transactional
@Slf4j
public class MyServerHandlerBuy extends SimpleChannelInboundHandler<MessagePOJO.Message> {

    private UserVo userVo;
    private ChannelHandlerContext ctx;
    private static OrderService orderService;
    private static AccountService accountService;
    private static RedisTemplate redisTemplate;
    private static RedisScript redisScript;
    private static MQSender mqSender;

    private static Map<Integer,Boolean> EmptyStockMap=new HashMap<>();

    /**
     * 尝试将setUserVo改为 static
     * @param userVo
     */
    public  void setUserVo(UserVo userVo){
        this.userVo=userVo;
    }
    static {
        orderService= SpringUtil.getBean(OrderService.class);
        accountService=SpringUtil.getBean(AccountService.class);
        redisTemplate=SpringUtil.getBean("redisTemplate",RedisTemplate.class);
        redisScript=SpringUtil.getBean(RedisScript.class);
        mqSender=SpringUtil.getBean(MQSender.class);
        EmptyStockMap.put(2,false);
        redisTemplate.opsForValue().set("seckillGoods:"+2,10);

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


        /**
         * 模拟现在就能够抢购
         * 秒杀系统的设计
         */
        else if(id1==8){
//            if(!judge()){
//                //不在秒杀的时间内
//                MessagePOJO.Message message2 = Transfrom.transform(8, 1, "不在秒杀的时间内");
//                ctx.writeAndFlush(message2);
//                return;
//            }
//            else if(userVo.getBalance()<10){
//                //余额不足
//                MessagePOJO.Message message3 = Transfrom.transform(8, 1, "余额不足");
//                ctx.writeAndFlush(message3);
//                return;
//            }
//            else{
                //进行秒杀业务

                //1.通过内存标记 减少redis的访问
                if(EmptyStockMap.get(2)){
                    MessagePOJO.Message message4 = Transfrom.transform(8, 1, "商品已经售空");
                    ctx.writeAndFlush(message4);
                    return;
                }


                //1.从redis中判断是否重复抢购
                Order order = (Order) redisTemplate.opsForValue().get("order:" + userVo.getId() + ":" + 2);
                if(order!=null){
                    MessagePOJO.Message message5 = Transfrom.transform(8, 1, "你已经抢购过，每人限购一件商品");
                    ctx.writeAndFlush(message5);
                    return;
                }



                //2.从redis中判断是否还有商品 预减库存 使用LUA脚本来进行控制
                Long stock = (Long) redisTemplate.execute(redisScript, Collections.singletonList("seckillGoods:" + 2), Collections.EMPTY_LIST);
                if(stock<0){
                    EmptyStockMap.put(2,true);
                    MessagePOJO.Message message6 = Transfrom.transform(8, 1, "商品已经售空");
                    ctx.writeAndFlush(message6);
                    return;
                }


                //3.预减库存完成  发送消息给RabbitMQ  创建了SeckillMessage  useVo+goodsId
                SeckillMessage seckillMessage=new SeckillMessage(userVo,2);
                mqSender.sendSeckillMessage(JsonUtils.objectToJsonStr(seckillMessage));
                MessagePOJO.Message message7 = Transfrom.transform(8, 2, "正在排队处理");
                ctx.writeAndFlush(message7);

            }

//        }
        /**
         * 查看本周的秒杀结果
         */
        else{
            System.out.println("00000000000000000000000000000000000000");
            //1.先从数据库中查  暂时没有找到order的信息 因为没有插入
            Order order = orderService.select(userVo.getId(), 2);
            if(null!=order){
                MessagePOJO.Message message1 = Transfrom.transform(12, 1, "抢购成功");

                ctx.writeAndFlush(message1);
            }
            //2.从redis中查  看看redis中的标记是否为空
            else if(redisTemplate.hasKey("isStockEmpty:"+2)){
                MessagePOJO.Message message1 = Transfrom.transform(12, 2, "抢购失败");
                ctx.writeAndFlush(message1);
            }

            //3.返回正在排队处理中
            else{
                MessagePOJO.Message message1 = Transfrom.transform(12, 3, "正在排队处理 ");
                ctx.writeAndFlush(message1);
            }

        }

    }


    /**
     * 判断时间是否合法
     * @return
     */
    private boolean judge(){
        long time=new Date().getTime();

        //对时间进行拦截
        Calendar cld=Calendar.getInstance(Locale.CHINA);
        cld.setFirstDayOfWeek(Calendar.MONDAY);

        cld.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
        cld.set(Calendar.HOUR_OF_DAY,20);
        cld.set(Calendar.MINUTE,0);
        cld.set(Calendar.SECOND,0);

        long time3=cld.getTime().getTime();

        cld.set(Calendar.MINUTE,30);
        long time4=cld.getTime().getTime();
        if(time>=time3&&time<=time4){
            return true;
        }
        return false;

    }
}
