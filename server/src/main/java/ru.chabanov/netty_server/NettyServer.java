package ru.chabanov.netty_server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class NettyServer {

    private static final int MAX_OBJ_SIZE = 1024 * 1024 * 100; // 10 mb
    private  int PORT_NUMBER;
    private   String PATH_TO_MAIN_FOLDER;

    public NettyServer( String PATH_TO_MAIN_FOLDER,int PORT_NUMBER) {
        this.PORT_NUMBER = PORT_NUMBER;
        this.PATH_TO_MAIN_FOLDER = PATH_TO_MAIN_FOLDER;
        try {
            run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void run() throws Exception {
        EventLoopGroup mainGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(mainGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(
                                    new CheckingServer(),
                                    new ObjectDecoder(MAX_OBJ_SIZE, ClassResolvers.cacheDisabled(null)),
                                    new ObjectEncoder(),
                                    new CloudServerHandler(PATH_TO_MAIN_FOLDER)
                            );
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                 //   .option(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            System.out.println("Netty server working on port "+PORT_NUMBER);

            ChannelFuture future = b.bind(PORT_NUMBER).sync();
            future.channel().closeFuture().sync();
        } finally {
            mainGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

//    public static void main(String[] args) throws Exception {
//        new NettyServer().run();
//    }
}
