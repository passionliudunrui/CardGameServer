package com.cardgameserver.netty;

import com.cardgameserver.thread.CustomTheadPoolExecutor;
import com.cardgameserver.thread.ManageThread;
import com.cardgameserver.utils.SpringUtil;
import com.cardgameserver.vo.UserVo;
import com.cardgameserver.zset.SkipList;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;


@Service
public class MyServer {

    public static SkipList skipList;
    public static ConcurrentHashMap<UserVo, Channel>players;
    public static LinkedBlockingQueue<UserVo> waitQueue;
    public static ManageThread manageThread;
    public static ThreadPoolExecutor pool;

    static {
        skipList= new SkipList();
        players=new ConcurrentHashMap<>();
        waitQueue=new LinkedBlockingQueue<>();
        manageThread=new ManageThread();
        pool= CustomTheadPoolExecutor.getPool();
        try {
            skipList.insert(12.0,12L);
            skipList.insert(134.9,1234L);
            skipList.insert(12.9,1233L);
            skipList.insert(3434.9,1823L);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    private MyServerInitializer myServerInitializer;

    private final int PORT = 8888;


    EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    EventLoopGroup workerGroup = new NioEventLoopGroup(1);

    public void start() throws InterruptedException{
        new Thread(manageThread).start();
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