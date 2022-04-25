package com.cardgameserver.netty;

import com.cardgameserver.proto.MessagePOJO;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MyServerHandlerBuy extends SimpleChannelInboundHandler<MessagePOJO.Message> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessagePOJO.Message message) throws Exception {
        System.out.println(message.getContext());

        System.out.println("接收消息3");
        ctx.fireChannelRead(message);
    }
}
