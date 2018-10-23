package ru.chabanov.nio_server;

import ru.chabanov.COMMAND;
import ru.chabanov.Converter;
import ru.chabanov.PlainText;
import ru.chabanov.SerializationText;

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

    public NIO_SERVER_1(String path, int port) {
        this.PATH_TO_MAIN_FOLDER=path;
        this.PORT_NUMBER = port;
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
                  new  Converter().doingCommands(list, PATH_TO_MAIN_FOLDER, out,null);
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




