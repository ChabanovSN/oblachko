package ru.chabanov.netty_server;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import ru.chabanov.Converter;
import ru.chabanov.PlainText;

import java.util.List;

public class CloudServerHandler extends ChannelInboundHandlerAdapter {
    private   String PATH_TO_MAIN_FOLDER;
    private static int step=0;
    public CloudServerHandler(  String PATH_TO_MAIN_FOLDER) {
        this.PATH_TO_MAIN_FOLDER = PATH_TO_MAIN_FOLDER;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected...");





        // Send greeting for a new connection.
        // ctx.write("Welcome to " + InetAddress.getLocalHost().getHostName() + "!\r\n");
        // ctx.write("It is " + new Date() + " now.\r\n");
        // ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
++step;
        System.out.println("WORKING step "+step);
        try {
            if (msg != null){
                List<PlainText> list = (List<PlainText>) msg;
             //   ((List<PlainText>) msg).clear();
                 for(PlainText pt : list){
                     if(pt.getCommands() !=null)
                     System.out.println(" On server "+pt.getCommands());
                 }

              //  ReferenceCountUtil.release(msg);
                Converter.doingCommands(list,PATH_TO_MAIN_FOLDER,null,ctx);
//
//                 list.clear();
//                 list.add(new PlainText("netty1.txt",null));
//                list.add(new PlainText("netty2.txt",null));
//                list.add(new PlainText("netty3.txt",null));
              //  ctx.write(msg);
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
        cause.printStackTrace();
        //ctx.flush();
     //   ctx.close();
    }
}
