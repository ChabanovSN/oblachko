package ru.chabanov.netty_server;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import ru.chabanov.COMMAND;
import ru.chabanov.Converter;
import ru.chabanov.PlainText;
import ru.chabanov.utils.AuthService;

import java.util.ArrayList;
import java.util.List;

public class CloudServerHandler extends ChannelInboundHandlerAdapter {
    private   String PATH_TO_MAIN_FOLDER;
    private  AuthService authService;
   public  CloudServerHandler(String PATH_TO_MAIN_FOLDER, AuthService authService) {
        this.PATH_TO_MAIN_FOLDER = PATH_TO_MAIN_FOLDER;
        this.authService = authService;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected..." + ctx.channel().remoteAddress());

         }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        List<PlainText> list = null;
        try {
            if (msg != null) {

                list = (List<PlainText>) msg;

                if (list != null) {
                    for (int i =0; i<list.size();i++) {
                        if (list.get(i).getCommands() == COMMAND.CHECK_AUTH) {
                            if (list.get(i).getLogin() != null || list.get(i).getPassword() != null) {
                                String serverLogin = authService.getLoginByLoginAndPass(list.get(i).getLogin(), list.get(i).getPassword());
                                list.get(i).setCommands(COMMAND.RESPONSE_AUTH);

                                if (list.get(i).getLogin().equals(serverLogin)) {
                                    list.get(i).setAuth(true);

                                }else {
                                    list.get(i).setLogin("Неверный логин или пароль");
                                    list.get(i).setAuth(false);

                                }

                                new Converter().doingCommands(list, PATH_TO_MAIN_FOLDER, null, ctx);
                            }


                        }
                        else
                        if(list.get(i).getCommands()==COMMAND.CLIENT_IS_AUTH) {

                            new Converter().doingCommands(list, PATH_TO_MAIN_FOLDER+"\\" +(list.get(i).getLogin())+"\\", null, ctx);
                        }
                    }
                }
                //  ReferenceCountUtil.release(msg);
//                if(list !=null) {
//                   new Converter().doingCommands(list, PATH_TO_MAIN_FOLDER, null, ctx);
//
//                }
//            } else {
//                System.out.printf("Server received wrong object!");
//                return;
//            }
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
