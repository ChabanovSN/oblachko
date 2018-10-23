package ru.chabanov.netty_client;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import ru.chabanov.Converter;
import ru.chabanov.PlainText;

import java.util.List;

public class CloudClientHandler extends ChannelInboundHandlerAdapter {
      private List<PlainText> list=null;
    public List<PlainText> getList() {
        return list;
    }

    public void setList(List<PlainText> list) {
        this.list = list;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Connected to server from Netty "+ctx.channel().remoteAddress());



        if(list !=null) {
            ctx.writeAndFlush(list);
            list.clear();
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {




            list = (List<PlainText>) msg;

            ctx.close();

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
