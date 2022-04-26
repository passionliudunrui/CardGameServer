package com.cardgameserver.netty;

import com.cardgameserver.entity.Account;
import com.cardgameserver.proto.MessagePOJO;
import com.cardgameserver.service.AccountService;
import com.cardgameserver.utils.SpringUtil;
import com.cardgameserver.utils.Transfrom;
import com.cardgameserver.vo.UserVo;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
public class MyServerHandlerPlay extends SimpleChannelInboundHandler<MessagePOJO.Message> {

    private static AccountService accountService;

    private  UserVo userVo;
    private ChannelHandlerContext ctx;
    private ThreadPoolExecutor pool=MyServer.pool;

    static {
        accountService= SpringUtil.getBean(AccountService.class);
    }

    public void setUserVo(UserVo userVo){
        this.userVo=userVo;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessagePOJO.Message message) throws Exception {
        this.ctx=ctx;
        int id1=message.getId1();
        int id2=message.getId2();
        String context=message.getContext();

        if(id1==5||id1==6||id1==7){

            switch(id1){
                case 5:
                    log.info("用户请求加入游戏");
                    joinGame();
                    break;

                case 6:
                    log.info("用户正在出牌");
                    playGame(id2,context);
                    break;

                case 7:
                    log.info("用户退出游戏");

                    break;

            }

        }
        else{
            MyServerHandlerBuy nextHandler = ctx.pipeline().get(MyServerHandlerBuy.class);
            nextHandler.setUserVo(userVo);
            ctx.fireChannelRead(message);
        }

    }

    /**
     *  功能描述 ：双方打牌的业务在这里实现   6
     * @param id2   id2=0 表示当前用户不要    id=1 表示用户出牌   context表示用户出的牌
     * @param context
     */
    private void playGame(int id2, String context) {

        Channel channel=MyServer.players.get(userVo.getOpponent());

        if(id2==0){
            String str="对方不要，请你继续出牌";
            MessagePOJO.Message message1 = Transfrom.transform(6, 0,4, str);
            channel.writeAndFlush(message1);

        }
        //id2= 1
        else{
            boolean remove = userVo.getPokers().remove(context);
            String str="";
            str="对方出的牌是 "+context;  //0表示对方出完牌了  1表示对方没有出完牌
            if(!judgeProkersEmpty()){
                MessagePOJO.Message message1 = Transfrom.transform(6, 1, str);
                channel.writeAndFlush(message1);
            }
            else{
                String str1="游戏结束 恭喜你获得游戏胜利";
                String str2="游戏结束 对方获得游戏胜利  下局继续加油";

                /*
                数据库的操作  本用户增加100happybean  对方减少100happybean
                内存中要修改
                数据库也要修改
                 */
                userVo.setHappybean(userVo.getHappybean()+10);
                userVo.getOpponent().setHappybean(userVo.getOpponent().getHappybean()-10);
                Account account1=new Account(userVo.getId(),userVo.getBalance(),userVo.getHappybean());
                Account account2=new Account(userVo.getOpponent().getId(),userVo.getOpponent().getBalance(),userVo.getOpponent().getHappybean());

                /**
                 * 交给异步线程池来执行
                 */
                CompletableFuture<Boolean>cf=CompletableFuture.supplyAsync(()->{
                    boolean ans = accountService.transfer(account1, account2);
                    return ans;
                },pool);
                try {
                    CompletableFuture<String>cf2=cf.thenApplyAsync((result)->{
                        if(result==false){
                            try {
                                throw new Exception("插入数据库错误");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        return "";
                    });
                }catch (Exception e){
                    //给用户提示  服务器异常
                    e.printStackTrace();
                }


                MessagePOJO.Message message2 = Transfrom.transform(6, 2, str1);
                MessagePOJO.Message message3 = Transfrom.transform(6, 2, str2);
                ctx.writeAndFlush(message2);
                channel.writeAndFlush(message3);
            }

        }

    }


    public boolean judgeProkersEmpty(){
        if(userVo.getPokers().isEmpty()){
            return true;
        }
        return false;

    }

    private void joinGame() {
        boolean offer = MyServer.waitQueue.offer(userVo);
        System.out.println("等待队列的数量  :"+MyServer.waitQueue.size());
        MessagePOJO.Message message1;
        if(offer==true){
            message1=Transfrom.transform(5,1,"正在等待其他游戏玩家");
        }else {
            message1 = Transfrom.transform(5, 0, "匹配游戏失败");
        }
        ctx.writeAndFlush(message1);
    }

}