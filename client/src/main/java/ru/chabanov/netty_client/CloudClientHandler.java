package ru.chabanov.netty_client;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import ru.chabanov.Converter;
import ru.chabanov.PlainText;

import java.util.List;

public class CloudClientHandler extends ChannelInboundHandlerAdapter {
    private static int step=0;
    private List<PlainText> list;
    public CloudClientHandler(List<PlainText> list) {
       this.list =list;
    }



    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Connected to server from Netty "+ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("WTF!!!");

  /// вот здесь беда.. при условии что можно сосывывать объекты в хендлер как я с сделал в NettyClient

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
