package ru.chabanov.nio_server;

import ru.chabanov.COMMAND;
import ru.chabanov.Converter;
import ru.chabanov.PlainText;
import ru.chabanov.SerializationText;
import ru.chabanov.utils.AuthService;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class NIO_SERVER_1 {
    private  int PORT_NUMBER;
    private  String PATH_TO_MAIN_FOLDER;
    private AuthService authService;
    public NIO_SERVER_1(String path, int port) {
        this.PATH_TO_MAIN_FOLDER=path;
        this.PORT_NUMBER = port;
        authService = new AuthService();
        authService.connect();
        start();
    }

    private void start() {
        System.out.println("NIO Server start");
        ServerSocketChannel ssChannel = null;
        try {
            ssChannel = ServerSocketChannel.open();
            ssChannel.configureBlocking(true);
            ssChannel.socket().bind(new InetSocketAddress(PORT_NUMBER));

            while (true) {
                SocketChannel sChannel = ssChannel.accept();
                InputStream in  = new ObjectInputStream(sChannel.socket().getInputStream());
                OutputStream out  = new ObjectOutputStream(sChannel.socket().getOutputStream());
                System.out.println("Новый клиент "+sChannel.getRemoteAddress());
                try {
                    List<PlainText> list = (List<PlainText>) SerializationText.deSerialization(in);


                    if (list != null) {
                        for (int i =0; i<list.size();i++) {
                            if (list.get(i).getCommands() == COMMAND.CHECK_AUTH) {
                                if (list.get(i).getLogin() != null || list.get(i).getPassword() != null) {
                                    String serverLogin = authService.getLoginByLoginAndPass(list.get(i).getLogin(), list.get(i).getPassword());
                                    list.get(i).setCommands(COMMAND.RESPONSE_AUTH);
                                    System.out.println("Nio server " + list.get(i).getCommands());
                                    if (list.get(i).getLogin().equals(serverLogin)) {
                                        list.get(i).setAuth(true);

                                    }else {
                                        list.get(i).setLogin("Неверный логин или пароль");
                                        list.get(i).setAuth(false);

                                    }

                                    new Converter().doingCommands(list, PATH_TO_MAIN_FOLDER, out, null);
                                }


                            }
                            else
                            if(list.get(i).getCommands()==COMMAND.CLIENT_IS_AUTH) {

                                new Converter().doingCommands(list, PATH_TO_MAIN_FOLDER+"\\" +(list.get(i).getLogin())+"\\", out, null);
                            }
                        }
                    }
                       in.close();
                       out.close();
                       sChannel.close();
                } catch (ClassNotFoundException e) {
                    in.close();
                    out.close();
                    sChannel.close();
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




