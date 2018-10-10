package ru.chabanov.io_client;
import ru.chabanov.Converter;
import ru.chabanov.PlainText;
import ru.chabanov.SerializationText;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class IO_Client {
    private String host = "127.0.0.1";
    private int port = 8080;


    public void sendIO_Client(List<PlainText> list) {
        try {

            System.out.println("Connecting to host " + host + " on port " + port + ".");

            Socket socket = null;
            ObjectOutputStream out = null;
            ObjectInputStream in = null;

            try {
                String fileName = "clientFolder/123.text";
              PlainText plainText= Converter.convertionFileToClass(fileName);
                /// file in class


               socket = new Socket(host, port);
                out = new ObjectOutputStream (socket.getOutputStream());
                in = new  ObjectInputStream (socket.getInputStream());
                for(PlainText plainText1 : list)
                 SerializationText.serialization(out,plainText);


         //      PlainText newPlainText=(PlainText) SerializationText.deSerialization(in) ;
         //      Converter.convertionClassToFile(newPlainText);
            } catch (UnknownHostException e) {
                System.err.println("Unknown host: " + host);
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Unable to get streams from server");
                System.exit(1);
            }

         /// режим чата
//            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
//
//            while (true) {
//                System.out.print("client: ");
//                String userInput = stdIn.readLine();
//                /** Exit on 'q' char sent */
//                if ("q".equals(userInput)) {
//                    break;
//                }
//                out.println(userInput);
//                System.out.println("server: " + in.readLine());
//            }


            out.close();
            in.close();
         //   stdIn.close();
           socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
