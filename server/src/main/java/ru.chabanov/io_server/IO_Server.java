package ru.chabanov.io_server;

import ru.chabanov.Converter;
import ru.chabanov.PlainText;
import ru.chabanov.SerializationText;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class IO_Server extends Thread {
    public static final int PORT_NUMBER = 8080;
    private static  final  String PATH_TO_MAIN_FOLDER = "C:\\Users\\User\\Desktop\\serverFolder\\";
    protected Socket socket;

    private IO_Server(Socket socket) {
        this.socket = socket;
        System.out.println("New client connected from " + socket.getInetAddress().getHostAddress());
        start();
    }

    public void run() {
        ObjectInputStream  in = null;
        ObjectOutputStream out = null;
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());


           PlainText newPlainText;
            try {

                    newPlainText = (PlainText) SerializationText.deSerialization(in);
                    System.out.println("Receive " + newPlainText.toString());
                    if(newPlainText.getNameFile() !=null || newPlainText.getContent() !=null)
                    Converter.convertionClassToFile(newPlainText, PATH_TO_MAIN_FOLDER);

                if(newPlainText.getCommands()==1)
                    Converter.doingCommands(newPlainText,PATH_TO_MAIN_FOLDER);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        //    PlainText plainText= Converter.convertionFileToClass(fileName);
        //    SerializationText.serialization(out,plainText);

            // режим чата
//            BufferedReader br = new BufferedReader(new InputStreamReader(in));
//            String request;
//            while ((request = br.readLine()) != null) {
//                System.out.println("Message received:" + request);
//                request += '\n';
//                out.write(request.getBytes());
//            }

        } catch (IOException ex) {
            System.out.println("Unable to get streams from client");
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("IO_Server Example");
        ServerSocket server = null;
        try {
            server = new ServerSocket(PORT_NUMBER);
            while (true) {
                /**
                 * create a new {@link IO_Server} object for each connection
                 * this will allow multiple client connections
                 */
                new IO_Server(server.accept());
            }
        } catch (IOException ex) {
            System.out.println("Unable to start server.");
        } finally {
            try {
                if (server != null)
                    server.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}