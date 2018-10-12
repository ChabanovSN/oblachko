package ru.chabanov.io_server;

import ru.chabanov.COMMAND;
import ru.chabanov.Converter;
import ru.chabanov.PlainText;
import ru.chabanov.SerializationText;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class IO_Server extends Thread {
   private  int PORT_NUMBER;
    private   String PATH_TO_MAIN_FOLDER;
    private ServerSocket serverSocket;


    public IO_Server(String path, int port) {
        this.PATH_TO_MAIN_FOLDER = path;
        this.PORT_NUMBER = port;
        try {
            serverSocket = new ServerSocket(PORT_NUMBER);
            this.start();
            System.out.println("IO_Server start");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

   public void run() {

        try {


            while (true) {
                Socket   client = serverSocket.accept();
                ObjectInputStream    in = new ObjectInputStream(client.getInputStream());
                ObjectOutputStream     out = new ObjectOutputStream(client.getOutputStream());
                System.out.println("Новый клиент присоединился. Адресс:  "+client.getRemoteSocketAddress());

                try {
                    List<PlainText> list = (List<PlainText>) SerializationText.deSerialization(in);
                    Converter.doingCommands(list, PATH_TO_MAIN_FOLDER,out);

                          in.close();
                          out.close();
                          client.close();
                } catch (ClassNotFoundException e) {
                    in.close();
                    out.close();
                    client.close();
                    e.printStackTrace();

                }
            }
        } catch (IOException ex) {
            System.out.println("Unable to get streams from client");

        }
    }


}