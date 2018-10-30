package ru.chabanov.nio_client;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import ru.chabanov.COMMAND;
import ru.chabanov.utils.Client_communication;
import ru.chabanov.PlainText;
import ru.chabanov.SerializationText;

import java.io.*;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.List;

public class NIO_CLIENT_1 implements Closeable, Client_communication {
    private String host;
    private int port;
     private SocketChannel socketChannel;
    private OutputStream out = null;
   private   InputStream in = null;
private boolean isNatty;
   private List<PlainText> list;
    private PlainText authInfo;
    public NIO_CLIENT_1( String host, int port) {
        this.host = host;
        this.port = port;
        try {

            try {
                socketChannel = SocketChannel.open();
                SocketAddress address = new InetSocketAddress(host, port + 1);
                socketChannel.connect(address);
                if (socketChannel.socket().isConnected()) {
                    out = new ObjectEncoderOutputStream(socketChannel.socket().getOutputStream());
                    in = new ObjectDecoderInputStream(socketChannel.socket().getInputStream());
                   isNatty=true;
                    System.out.println("Клиент NIO соединился с сервером Netty  host: " + host + " на порте: " + (port + 1) + ".");
                }
            } catch (ConnectException e) {
                System.out.println("Это не Нетти");
                socketChannel = SocketChannel.open();
            }
            if (!socketChannel.socket().isConnected()) {
            SocketAddress address = new InetSocketAddress(host, port);
                socketChannel.connect(address);
                if (socketChannel.socket().isConnected()) {
                    out = new ObjectOutputStream(socketChannel.socket().getOutputStream());
                    in = new ObjectInputStream(socketChannel.socket().getInputStream());
                    isNatty=false;
                    System.out.println("Клиент NIO соединился с сервером  IO or NIO host: " + host + " на порте: " + port + ".");
                }
            }


        }

        catch (Exception e) {
            System.out.println(" сервер не Нетти");
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
            if(list !=null) {
                for (int i=0; i<list.size();i++){
                    //  System.out.println(pt.getLogin()+" IOClient");
                    if(list.get(i).getCommands()== COMMAND.RESPONSE_AUTH){
                        authInfo = list.get(i);
                        list.remove(list.get(i));
                    }
                }
            }

            return list;

        } catch (Exception e) {
            System.out.println("Error in NIO Client receiveObject() ");
        }
        return null;

    }


    @Override
    public void close() throws IOException {
        out.close();
        in.close();
        socketChannel.close();
    }

    @Override
    public void discard()  {
        try {
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isNetty() {
        return isNatty;
    }

    @Override
    public PlainText receiveAuth() {
        receiveObject();
        return authInfo;
    }


}
