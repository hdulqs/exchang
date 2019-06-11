package com.batsoft.nettysocket.controller;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.stereotype.Component;


@Component
public class StartWebSocket {



   // @Bean("startSocket")
    public  String startSocket(){
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            //开启服务端
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(eventLoopGroup,workGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.childHandler(new MyWebSocketChannelHandler());
            System.out.println("服务端开启等待客户端连接..");
            Channel channel = serverBootstrap.bind(8111).sync().channel();
            channel.closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //退出程序
            eventLoopGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
        return  null;
    }


}
