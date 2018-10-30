package ru.chabanov.io_server;

import ru.chabanov.COMMAND;
import ru.chabanov.Converter;
import ru.chabanov.PlainText;
import ru.chabanov.SerializationText;
import ru.chabanov.utils.AuthService;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class IO_Server extends Thread {
   private  int PORT_NUMBER;
    private   String PATH_TO_MAIN_FOLDER;
    private ServerSocket serverSocket;
    private AuthService authService;

    public IO_Server(String path, int port) {
        this.PATH_TO_MAIN_FOLDER = path;
        this.PORT_NUMBER = port;
        try {
            serverSocket = new ServerSocket(PORT_NUMBER);
            authService = new AuthService();
            authService.connect();
            this.start();
            System.out.println("IO_Server start");
        } catch (Exception e) {
            System.out.println("Error in starting SERVER IO");
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


                               if (list != null) {
                                   for (int i =0; i<list.size();i++) {
                                       if (list.get(i).getCommands() == COMMAND.CHECK_AUTH) {
                                           if (list.get(i).getLogin() != null || list.get(i).getPassword() != null) {
                                            String serverLogin = authService.getLoginByLoginAndPass(list.get(i).getLogin(), list.get(i).getPassword());
                                               list.get(i).setCommands(COMMAND.RESPONSE_AUTH);

                                               if (list.get(i).getLogin().equals(serverLogin)) {
                                                   list.get(i).setAuth(true);

                                               }else {
                                                   list.get(i).setLogin("Неверный логин или пароль");
                                                   list.get(i).setAuth(false);

                                               }

                                               new Converter().doingCommands(list, PATH_TO_MAIN_FOLDER, out, null);
                                           }


                                       }
                                       else
                                           if(list.get(i).getCommands()==COMMAND.CLIENT_IS_AUTH) {

                                           new Converter().doingCommands(list, PATH_TO_MAIN_FOLDER+"\\" +(list.get(i).getLogin())+"\\", out, null);
                                       }
                                   }
                               }
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