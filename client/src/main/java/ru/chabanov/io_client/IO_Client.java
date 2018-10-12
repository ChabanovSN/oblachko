package ru.chabanov.io_client;
import ru.chabanov.Client_communication;
import ru.chabanov.PlainText;
import ru.chabanov.SerializationText;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class IO_Client implements Closeable, Client_communication {
    private String host;
    private int port;
    Socket socket = null;
    ObjectOutputStream out = null;
    ObjectInputStream in = null;

    public IO_Client(String host,int port) {
this.host=host;
this.port=port;


        System.out.println("Клиент IO соединился с сервером "+host+ " на порте "+port+ ".");
        try {
            socket = new Socket(host, port);
            out = new ObjectOutputStream (socket.getOutputStream());
            in = new  ObjectInputStream (socket.getInputStream());
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + host);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Unable to get streams from server");
            System.exit(1);
        }
    }

    public void  sendObgect(List<PlainText> list) {
        try {
            if (list != null) SerializationText.serialization(out, list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<PlainText> receiveObject() {
        List<PlainText> list = null;
        try {
            try {
                list = (List<PlainText>) SerializationText.deSerialization(in);
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
        out.close();
        in.close();
        socket.close();
    }
}
