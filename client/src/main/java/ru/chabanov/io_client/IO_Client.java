package ru.chabanov.io_client;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import ru.chabanov.COMMAND;
import ru.chabanov.utils.Client_communication;
import ru.chabanov.PlainText;
import ru.chabanov.SerializationText;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.List;

public class IO_Client implements Closeable, Client_communication {
    private String host;
    private int port;
    Socket socket = null;
    OutputStream oeos = null;
    InputStream  odis = null;
    private boolean isNatty;
    private PlainText authInfo;
    public IO_Client(String host, int port) {
        this.host = host;
        this.port = port;


        try {
            try {
                socket = new Socket(host, port + 1);

                oeos = new ObjectEncoderOutputStream(socket.getOutputStream());
                odis = new ObjectDecoderInputStream(socket.getInputStream());
                isNatty=true;
                System.out.println("Клиент IO соединился с сервером Netty  host: " + host + " на порте: " + (port+1) + ".");
            } catch (ConnectException e) {
                System.out.println("Это не Нетти");
            }
            if (socket == null) {
                socket = new Socket(host, port);
                oeos = new ObjectOutputStream(socket.getOutputStream());
                odis = new ObjectInputStream(socket.getInputStream());
                isNatty=false;
                System.out.println("Клиент IO соединился с сервером  IO or NIO host: " + host + " на порте: " + port + ".");
            }
        } catch (Exception e) {
            System.out.println(" Error in IO Client");
        }

    }

    public void  sendObgect(List<PlainText> list) {
        try {
            if (list != null){
               SerializationText.serialization(oeos, list);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<PlainText> receiveObject() {
        List<PlainText> list = null;

        try {
            list = (List<PlainText>) SerializationText.deSerialization(odis);
            if(list !=null) {
                for (int i=0; i<list.size();i++){
                  //  System.out.println(pt.getLogin()+" IOClient");
                    if(list.get(i).getCommands()== COMMAND.RESPONSE_AUTH){
                        authInfo = list.get(i);
                        list.remove(list.get(i));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка в receiveObject() Client IO "+ e.getMessage());

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
