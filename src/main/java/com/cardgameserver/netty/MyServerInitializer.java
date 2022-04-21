package com.cardgameserver.netty;

import com.cardgameserver.proto.MessagePOJO;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class MyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline=ch.pipeline();

        /*
        多少时间没有读取到客户端，就发送一个心跳检测包给客户端检测是否连接
         */
        //netty提供的

        pipeline.addLast(new ProtobufVarint32FrameDecoder());
//        pipeline.addLast("decoder",
//                new ProtobufDecoder(Message.MyMessage.getDefaultInstance()));
        //解码的时候 要注明解码哪个数据结构
        pipeline.addLast("decoder",
                new ProtobufDecoder(MessagePOJO.Message.getDefaultInstance()));


        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        pipeline.addLast(new ProtobufEncoder());

        pipeline.addLast(new IdleStateHandler(10,0,0, TimeUnit.SECONDS));
        //pipeline.addLast(new HeartBeatHandler());
        pipeline.addLast(new MyServerHandler());

        System.out.println("绑定处理器");

        //pipeline.addLast(new MyServerIdleHandler());
    }
}
