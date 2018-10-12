package ru.chabanov.nio_server;

import ru.chabanov.COMMAND;
import ru.chabanov.Converter;
import ru.chabanov.PlainText;
import ru.chabanov.SerializationText;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
                ObjectInputStream in = new ObjectInputStream(sChannel.socket().getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(sChannel.socket().getOutputStream());
                try {
                    List<PlainText> list = (List<PlainText>) SerializationText.deSerialization(in);
                    Converter.doingCommands(list, PATH_TO_MAIN_FOLDER, out);
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




