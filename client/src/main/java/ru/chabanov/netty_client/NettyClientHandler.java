package ru.chabanov.netty_client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ru.chabanov.PlainText;


public class NettyClientHandler extends SimpleChannelInboundHandler<PlainText> {



//    @Override
//    protected void messageReceived(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
//        System.out.println(s);
//    }

//    @Override
//    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
//     //   channelHandlerContext.channel().write("1213123").sync().channel().flush();
//        System.out.println(s);
//    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, PlainText plainText) throws Exception {


        System.out.println("Sent file from client Netty");

    }
}
