package ru.chabanov.nio_client;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import ru.chabanov.Client_communication;
import ru.chabanov.Converter;
import ru.chabanov.PlainText;
import ru.chabanov.SerializationText;

import java.io.*;
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

   private List<PlainText> list;

    public NIO_CLIENT_1( String host, int port) {
        this.host = host;
        this.port = port;
        try {
           socketChannel = SocketChannel.open();
            SocketAddress address = new InetSocketAddress(host,port);
            socketChannel.connect(address);

            byte check = 13;
            out = new DataOutputStream(socketChannel.socket().getOutputStream());
            out.write(check);
            out.flush();
            in = new DataInputStream(socketChannel.socket().getInputStream());


            byte checkBack = (byte) in.read();
            System.out.println(checkBack+"checkBack");
            if(check ==checkBack) {
                out = new ObjectEncoderOutputStream(socketChannel.socket().getOutputStream());
                in = new ObjectDecoderInputStream(socketChannel.socket().getInputStream());

            }else{
                out = new ObjectOutputStream(socketChannel.socket().getOutputStream());
                in = new ObjectInputStream(socketChannel.socket().getInputStream());
            }


            System.out.println("Клиент NIO соединился с сервером "+host+ " на порте "+port+ ".");
        }
//        catch (ClassNotFoundException e){
//            System.out.println("ClassNotFoundException in NIO CLIENT");
//            e.printStackTrace();
//        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void  sendObgect(List<PlainText> list) {
        try {
            System.out.println("sendObgect");
            for(PlainText pr : list) {
                if(pr.getCommands() !=null)
                System.out.println(" on nio client "+pr.getCommands());
            }
            SerializationText.serialization(out, list);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public List<PlainText> receiveObject() {

        try {
            list = (List<PlainText>) SerializationText.deSerialization(in);

            for(PlainText pt : list){

                System.out.println("receiveObject on NIO client"+ pt + " comm "+pt.getCommands());
            }

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

    @Override
    public void discard()  {
        try {
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
