package ru.chabanov.nio_client;

import ru.chabanov.Client_communication;
import ru.chabanov.Converter;
import ru.chabanov.PlainText;
import ru.chabanov.SerializationText;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.List;

public class NIO_CLIENT_1 implements Closeable, Client_communication {
    private String host;
    private int port;
     private SocketChannel socketChannel;
    private  ObjectOutputStream out = null;
   private   ObjectInputStream in = null;

   private List<PlainText> list;

    public NIO_CLIENT_1( String host, int port) {
        this.host = host;
        this.port = port;
        try {
           socketChannel = SocketChannel.open();

            SocketAddress address = new InetSocketAddress(host,port);
            socketChannel.connect(address);
            out = new ObjectOutputStream(socketChannel.socket().getOutputStream());
            in = new ObjectInputStream(socketChannel.socket().getInputStream());
            System.out.println("Клиент NIO соединился с сервером "+host+ " на порте "+port+ ".");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void  sendObgect(List<PlainText> list) {
        try {
            SerializationText.serialization(out, list);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public List<PlainText> receiveObject() {

        try {
            list = (List<PlainText>) SerializationText.deSerialization(in);

                    return list;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }


    @Override
    public void close() throws IOException {
        out.close();
        in.close();
        socketChannel.close();
    }
}
