package com.cardgameserver.netty;

import com.cardgameserver.entity.User;
import com.cardgameserver.proto.MessagePOJO;
import com.cardgameserver.service.UserService;
import com.cardgameserver.utils.MD5Util;
import com.cardgameserver.utils.SpringUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
public class MyServerHandlerInfo extends SimpleChannelInboundHandler<MessagePOJO.Message> {



    private static UserService userService;
    static {
        System.out.println("准备获取 userService");
        userService= SpringUtil.getBean(UserService.class);
        System.out.println("获取userService");
    }

    private ChannelHandlerContext ctx;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessagePOJO.Message message) throws Exception {
        this.ctx=ctx;
        int id1=message.getId1();
        int id2=message.getId2();
        String context=message.getContext();
        if(id1==1||id1==2||id1==3||id1==4){
            switch (id1){
                case 1:
                    log.info("客户端发来注册请求");
                    apply(context);
                    break;








            }

        }
        else{

            ctx.fireChannelRead(message);

        }
    }

    private void apply(String context){

        System.out.println(context);
        String[]data=context.split(",");
        Long id=Long.valueOf(data[0]);
        String nickName=data[1];
        String passwd=data[2];
        System.out.println(id);
        System.out.println(nickName);
        System.out.println(passwd);
        String password = MD5Util.inputPassToFromPass(passwd);

        User user=new User(id,nickName,password);
        int ans = userService.insert(user);

        if(ans==1){
            log.info("成功");
        }
        else{
            log.info("失败");
        }


    }

}
