package com.rayvision.netty.section3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;


/**
 * Created by admin on 2017/11/6.
 */
public class Server {

    private static int port = 9072;

    public static void main(String[] args) throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                     .channel(NioServerSocketChannel.class)
                     .option(ChannelOption.SO_BACKLOG, 1024)
                     .childHandler(new ChannelInitializer<SocketChannel>() {
                         protected void initChannel(SocketChannel socketChannel) throws Exception {
                             ChannelPipeline pipeline = socketChannel.pipeline();
                             pipeline.addLast(new LineBasedFrameDecoder(1024));
                             pipeline.addLast(new StringDecoder());
                             pipeline.addLast(new ServerHandler());
                         }
                     });
            ChannelFuture future = bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

