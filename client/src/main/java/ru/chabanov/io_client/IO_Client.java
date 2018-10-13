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
           Integer i = new Integer(13);
            System.out.println(i.byteValue());
           byte check = i.byteValue();
            oeos = new DataOutputStream(socket.getOutputStream());
            oeos.write(check);
            oeos.flush();
          //  SerializationText.serialization(oeos,check);
            odis = new DataInputStream(socket.getInputStream());

            byte checkBack = (byte) odis.read();
            System.out.println(checkBack+"chackBack");
            if(check ==checkBack) {
                oeos = new ObjectEncoderOutputStream(socket.getOutputStream());
                odis = new ObjectDecoderInputStream(socket.getInputStream());
              }else {
                oeos = new ObjectOutputStream(socket.getOutputStream());
                odis = new ObjectInputStream(socket.getInputStream());
            }


        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void  sendObgect(List<PlainText> list) {


        try {
            if (list != null){
                System.out.println("sendObgect");
                for(PlainText pt : list){
                    if(pt.getCommands() !=null)
                        System.out.println(" on io client "+pt.getCommands());
                }

                SerializationText.serialization(oeos, list);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<PlainText> receiveObject() {
        List<PlainText> list = null;
        try {
            try {
                list = (List<PlainText>) SerializationText.deSerialization(odis);


                for(PlainText pt : list){
                    System.out.println("receiveObject on io client"+ pt + " comm "+pt.getCommands());
                }
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

    @Override
    public void discard() {
        try {
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
