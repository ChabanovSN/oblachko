package ru.chabanov.io_client;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import ru.chabanov.Client_communication;
import ru.chabanov.PlainText;
import ru.chabanov.SerializationText;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class IO_Client implements Closeable, Client_communication {
    private String host;
    private int port;
    Socket socket = null;
    OutputStream oeos = null;
    InputStream  odis = null;

//    public static void main(String[] args) {
//        new IO_Client("localhost",8088);
//    }

    public IO_Client(String host,int port) {
this.host=host;
this.port=port;


        System.out.println("Клиент IO соединился с сервером "+host+ " на порте "+port+ ".");
        try {
            socket = new Socket(host, port);

            String check = "checking";
            oeos = new ObjectOutputStream(socket.getOutputStream());
            SerializationText.serialization(oeos,check);
            odis = new ObjectInputStream(socket.getInputStream());

            String ch = (String) SerializationText.deSerialization(odis);

            if(ch.equals(check)) {
                oeos = new ObjectEncoderOutputStream(socket.getOutputStream());
                odis = new ObjectDecoderInputStream(socket.getInputStream());
              }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void  sendObgect(List<PlainText> list) {

        System.out.println("send list");
        for(PlainText pt : list) System.out.println(pt + " comm "+pt.getCommands());
        try {
            if (list != null) SerializationText.serialization(oeos, list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<PlainText> receiveObject() {
        List<PlainText> list = null;
        try {
            try {
                list = (List<PlainText>) SerializationText.deSerialization(odis);

                System.out.println("send list");
                for(PlainText pt : list) System.out.println(pt + " comm "+pt.getCommands());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;

    }




    @Override
    public void close() throws IOException {
        odis.close();
      oeos.close();
        socket.close();
    }
}
