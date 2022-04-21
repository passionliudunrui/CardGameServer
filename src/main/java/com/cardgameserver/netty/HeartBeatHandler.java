package com.cardgameserver.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class HeartBeatHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt){


    }


}
