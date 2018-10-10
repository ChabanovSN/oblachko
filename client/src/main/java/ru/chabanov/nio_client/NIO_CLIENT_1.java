package ru.chabanov.nio_client;

import ru.chabanov.Converter;
import ru.chabanov.PlainText;
import ru.chabanov.SerializationText;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

public class NIO_CLIENT_1 {

    private  boolean isConnected = false;
    private SocketChannel socketChannel;
    public static void main(String[] args) {
             new NIO_CLIENT_1().sendObject();
    }

    public SocketChannel createChannel(){
        try {
            SocketChannel socketChannel = SocketChannel.open();

            SocketAddress address = new InetSocketAddress("localhost",8080);
            socketChannel.connect(address);
            System.out.println("Client is working in server.."+socketChannel.getRemoteAddress());
            return socketChannel;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public void sendObject(){

        while (!isConnected){
            socketChannel = createChannel();
            isConnected=true;
            try {
                ObjectOutputStream  out = new ObjectOutputStream(socketChannel.socket().getOutputStream());
                String fileName = "clientFolder/123.text";
                PlainText plainText= Converter.convertionFileToClass(fileName);
                SerializationText.serialization(out,plainText);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
