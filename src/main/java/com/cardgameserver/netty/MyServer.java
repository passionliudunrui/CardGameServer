package com.cardgameserver.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MyServer {

    @Autowired
    private MyServerInitializer myServerInitializer;

    private final int PORT = 8888;


    EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    EventLoopGroup workerGroup = new NioEventLoopGroup(1);

    public void start() throws InterruptedException{

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.localAddress(PORT);
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 128);
            /*
            1.默认关闭
            2.TCP keeplive依赖操作系统实现，默认keepalive心跳时间是两个小时
            并且对keepalive修改需要系统调用，（修改配置），灵活性不足
            3.如果换成了UDP协议，南无keepalive机制失效
             */

        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        //serverBootstrap.handler(null);
        serverBootstrap.childHandler(myServerInitializer);


        ChannelFuture channelFuture = serverBootstrap.bind(PORT).sync();
        System.out.println("绑定端口成功");


        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    System.out.println(Thread.currentThread().getName());

                    System.out.println("监听端口成功");
                } else {
                    System.out.println("监听端口失败");
                }

            }
        });

        channelFuture.channel().closeFuture().sync();

    }


    public void destory(){


        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }






}