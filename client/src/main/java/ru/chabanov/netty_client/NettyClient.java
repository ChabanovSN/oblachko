package ru.chabanov.netty_client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import ru.chabanov.Converter;
import ru.chabanov.PlainText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NettyClient {
    private  final  String host;
    private final int port;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() {
        EventLoopGroup group= new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap()
            .group(group)
             .channel(NioSocketChannel.class)
             .handler(new NettyClientInitializer());

            Channel channel = bootstrap.connect(host,port).sync().channel();

            String fileName = "clientFolder/123.text";
            PlainText plainText= Converter.convertionFileToClass(fileName);


              channel.write(plainText);
              channel.flush();
            // mode chat

//            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//
//            while (true){
//                channel.write((in.readLine()+"\r\n"));
//                channel.flush();
//            }
        } catch (InterruptedException e){

            e.printStackTrace();
        }
        finally {
         group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new NettyClient("localhost",8080).run();
    }
}
