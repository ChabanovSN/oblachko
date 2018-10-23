package ru.chabanov.netty_server;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import ru.chabanov.Converter;
import ru.chabanov.PlainText;

import java.util.ArrayList;
import java.util.List;

public class CloudServerHandler extends ChannelInboundHandlerAdapter {
    private   String PATH_TO_MAIN_FOLDER;
    private static int step=0;
    public CloudServerHandler(  String PATH_TO_MAIN_FOLDER) {
        this.PATH_TO_MAIN_FOLDER = PATH_TO_MAIN_FOLDER;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected..." + ctx.channel().remoteAddress());

         }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
++step;

        System.out.println("WORKING step "+step);
        List<PlainText> list = null;
        try {
            if (msg != null){
                try {
                     list = (List<PlainText>) msg;
                                       for (PlainText pt : list) {
                       // if (pt.getCommands() != null)
                            System.out.println(" On server " + pt.getCommands() + " name " + pt);
                    }
                }catch (ClassCastException e){
                    System.out.println("ClassCastException" +msg.toString());
                }

              //  ReferenceCountUtil.release(msg);
                if(list !=null) {
                   new Converter().doingCommands(list, PATH_TO_MAIN_FOLDER, null, ctx);

                }
            } else {
                System.out.printf("Server received wrong object!");
                return;
            }
        } finally {
          ReferenceCountUtil.release(msg);

        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(" ошибка на сервере");


       // cause.printStackTrace();
        //ctx.flush();
     //   ctx.close();
    }
}
