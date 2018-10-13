package ru.chabanov.netty_server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import ru.chabanov.PlainText;

public class CheckingServer extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {




        ByteBuf tmp = ctx.alloc().buffer(1);
        ByteBuf buf = ((ByteBuf)msg);
        byte b=buf.getByte(0);


          if(b==13) {
              System.out.println(" checking server in CheckingServer class");
               tmp.writeByte(b);
               ctx.writeAndFlush(tmp);
           }else {
              System.out.println("Проброс на сервере");
              ctx.fireChannelRead(msg);

          }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("exceptionCaught in CheckingServer.class");

    }
}

