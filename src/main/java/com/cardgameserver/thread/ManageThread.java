package com.cardgameserver.thread;

import com.cardgameserver.netty.MyServer;
import com.cardgameserver.proto.MessagePOJO;
import com.cardgameserver.utils.Transfrom;
import com.cardgameserver.vo.UserVo;
import io.netty.channel.Channel;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service

public class ManageThread extends Thread{

    @SneakyThrows
    @Override
    public void run(){
        System.out.println("管理线程启动 监视任务");
        while(true){
            if(judge()==true){
                UserVo use1=MyServer.waitQueue.poll();
                UserVo use2=MyServer.waitQueue.poll();
                Channel channel1=MyServer.players.get(use1.getId());
                Channel channel2=MyServer.players.get(use2.getId());

                load(use1,use2);
                MessagePOJO.Message message1 = Transfrom.transform(6, 0, 0,"匹配成功加入游戏");
                channel1.writeAndFlush(message1);
                channel2.writeAndFlush(message1);

                String context1="对手的昵称是 "+use2.getNickName()+"  你本局的牌是 "+
                        use1.getPokers().get(0)+"   "+use1.getPokers().get(1)+"   "+use1.getPokers().get(2)+
                        "请你先出牌";
                String context2="对手的昵称是 "+use1.getNickName()+"  你本局的牌是 "+
                        use2.getPokers().get(0)+"   "+use2.getPokers().get(1)+"   "+use2.getPokers().get(2)+
                        "请等待对方出牌";

                MessagePOJO.Message message2 = Transfrom.transform(6, 0,1, context1);
                MessagePOJO.Message message3 = Transfrom.transform(6, 0,2, context2);
                channel1.writeAndFlush(message2);
                channel2.writeAndFlush(message3);

            }
            else{
                Thread.sleep(20);
            }
        }

    }


    public boolean judge(){
        if(MyServer.waitQueue.size()>=2){
            return true;
        }
        return false;
    }

    public void load(UserVo user1,UserVo user2){
        user1.setOpponent(user2);
        user2.setOpponent(user1);
        user1.setFirst(true);
        user2.setFirst(false);

        ArrayList<String>pokers1=new ArrayList<>();
        ArrayList<String>pokers2=new ArrayList<>();

        pokers1.add("1");
        pokers1.add("4");
        pokers1.add("6");

        pokers2.add("4");
        pokers2.add("8");
        pokers2.add("9");

        user1.setPokers(pokers1);
        user2.setPokers(pokers2);
    }

}
