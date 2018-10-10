package ru.chabanov.nio_server;

import ru.chabanov.Converter;
import ru.chabanov.PlainText;
import ru.chabanov.SerializationText;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NIO_SERVER_1 {
    private SocketChannel socketChannel =null;

    public static void main(String[] args) {
      new NIO_SERVER_1().recieveObject();
    }

    public void recieveObject(){
        socketChannel = createSocketChannel();

        try {
            ObjectInputStream in = new ObjectInputStream(socketChannel.socket().getInputStream());

            String fileName = "clientFolder/123.text";

            PlainText newPlainText= null;
            try {
                newPlainText = (PlainText) SerializationText.deSerialization(in);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            Converter.convertionClassToFile(newPlainText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SocketChannel createSocketChannel() {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(8080));
            socketChannel = serverSocketChannel.accept();

            System.out.println("Соединение установленно..."+socketChannel.getRemoteAddress());
            return socketChannel;
        }catch (IOException e){
            e.printStackTrace();
        }

        return  null;
    }

}
