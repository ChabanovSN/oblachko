package ru.chabanov.netty_client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import ru.chabanov.COMMAND;
import ru.chabanov.utils.Client_communication;
import ru.chabanov.PlainText;

import java.util.ArrayList;
import java.util.List;

public class NettyClient implements Client_communication {
    private static final int MAX_OBJ_SIZE = 1024 * 1024 * 100; // 10 mb
    private String host;
    private int port;
    private boolean isWritable=false;
    private PlainText authInfo;
    private  CloudClientHandler cl = new CloudClientHandler();

private    List<PlainText> innnerList = new ArrayList<>();
    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;


    }
    private void  run( ) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();


        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                                 socketChannel.pipeline().addLast(
                                         new ObjectEncoder(),
                                         new ObjectDecoder(MAX_OBJ_SIZE, ClassResolvers.cacheDisabled(null)),
                                         cl
                                 );

                        }

                    }
                    );
            ChannelFuture f = null;
            try {

                            f = b.connect(host,port+1).sync();
                            System.out.println("Netty клиент соединился с Netty сервером "+host+" port "+(port+1));



               f.channel().closeFuture().sync();
               innnerList=cl.getList();
            } catch (InterruptedException  e) {
                e.printStackTrace();
            }

        }finally {
            workerGroup.shutdownGracefully();

        }

        }

    @Override
    public void sendObgect(List<PlainText> list) {
        cl.setList(list);
        run();

    }

    @Override
    public  List<PlainText> receiveObject() {

        if(innnerList !=null) {
            for (int i=0; i<innnerList.size();i++){

                if(innnerList.get(i).getCommands()== COMMAND.RESPONSE_AUTH){
                    authInfo = innnerList.get(i);
                    innnerList.remove(innnerList.get(i));
                }
            }
        }


        return innnerList ;
    }

    @Override
    public void discard() {

    }

    @Override
    public boolean isNetty() {
        return true;
    }

    @Override
    public PlainText receiveAuth() {
        receiveObject();
        return authInfo;
    }
}
