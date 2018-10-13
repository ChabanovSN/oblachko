package ru.chabanov.netty_client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class CheckingClient extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf tmp = ctx.alloc().buffer(1);
        tmp.writeByte(13);
        ctx.writeAndFlush(tmp);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Проброс на client");
        ctx.fireChannelRead(msg);

//        ByteBuf tmp = ctx.alloc().buffer(1);
//        ByteBuf buf = ((ByteBuf)msg);
//        byte b=buf.getByte(0);
//
//
//        if(b==13) {
//            System.out.println(" checking client in CheckingClient class");
////            tmp.writeByte(b);
////            ctx.writeAndFlush(tmp);
//
//            ctx.fireChannelRead(msg);
//        }else {
//            System.out.println("Проброс на client");
//            ctx.fireChannelRead(msg);
//
//        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("exceptionCaught in CheckingClientNetty.class");

    }
}

