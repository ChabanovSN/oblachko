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


       while (true) {
           Socket client = null;
           try {
               client = serverSocket.accept();
               Socket finalClient = client;
               new Thread(new Runnable() {
                   InputStream in = new ObjectInputStream(finalClient.getInputStream());
                   OutputStream out = new ObjectOutputStream(finalClient.getOutputStream());
                   @Override
                   public void run() {
                       System.out.println("Новый клиент присоединился. Адресс:  " + finalClient.getRemoteSocketAddress());
                       try {


                           try {
                               List<PlainText> list = (List<PlainText>) SerializationText.deSerialization(in);
                               new Converter().doingCommands(list, PATH_TO_MAIN_FOLDER, out, null);

                               in.close();
                               out.close();
                               finalClient.close();
                           } catch (ClassNotFoundException e) {
                               in.close();
                               out.close();
                               finalClient.close();
                               e.printStackTrace();

                           }
                       } catch (IOException ex) {
                           System.out.println("Unable to get streams from client");

                       }
                   }

               }).start();


           } catch (IOException e) {
               System.out.println("Unable to connect from client");
           }
       }

   }
}