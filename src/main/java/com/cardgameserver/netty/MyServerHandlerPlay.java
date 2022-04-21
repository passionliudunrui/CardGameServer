package com.cardgameserver.netty;

import com.cardgameserver.proto.MessagePOJO;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

public class MyServerHandlerPlay extends SimpleChannelInboundHandler<MessagePOJO.Message> {

//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println("接收消息");
//        MessagePOJO.Message message=(MessagePOJO.Message)msg;
//
//        System.out.println(message.getContext());
//
//        System.out.println("接收消息2");
//    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessagePOJO.Message message) throws Exception {
        System.out.println(message.getContext());

        System.out.println("接收消息2");


    }
}