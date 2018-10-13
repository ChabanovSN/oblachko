package ru.chabanov.netty_client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import ru.chabanov.Client_communication;
import ru.chabanov.PlainText;

import java.util.List;

public class NettyClient implements Client_communication {
    private static final int MAX_OBJ_SIZE = 1024 * 1024 * 100; // 10 mb
    private String host;
    private int port;



 public static   List<PlainText> innnerList;
    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
        run(null);
    }
    private void  run( List<PlainText> list ) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                                 socketChannel.pipeline().addLast(  new CheckingClient(),
                                         new CheckingClient(),
                                         new ObjectEncoder(),
                                         new ObjectDecoder(MAX_OBJ_SIZE, ClassResolvers.cacheDisabled(null)),
                                         new CloudClientHandler(list));

                        }

                    }
                    );
            ChannelFuture f = null;
            try {
                System.out.println("Netty client is working host "+host+" port "+port);
                f = b.connect(host,port).sync();
                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }finally {
            workerGroup.shutdownGracefully();
        }

        }

    @Override
    public void sendObgect(List<PlainText> list) {
        run(list);

    }

    @Override
    public  List<PlainText> receiveObject() {
          run(null);
        return innnerList ;
    }

    @Override
    public void discard() {

    }
}
