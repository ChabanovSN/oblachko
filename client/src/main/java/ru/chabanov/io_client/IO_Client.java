package ru.chabanov.io_client;
import ru.chabanov.PlainText;
import ru.chabanov.SerializationText;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class IO_Client {
    private String host = "127.0.0.1";
    private int port = 8080;
    Socket socket = null;
    ObjectOutputStream out = null;
    ObjectInputStream in = null;

    public IO_Client() {
        System.out.println("Connecting to host " + host + " on port " + port + ".");
        try {


            socket = new Socket(host, port);
            out = new ObjectOutputStream (socket.getOutputStream());
            in = new  ObjectInputStream (socket.getInputStream());



            //      PlainText newPlainText=(PlainText) SerializationText.deSerialization(in) ;
            //      Converter.convertionClassToFile(newPlainText);
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + host);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Unable to get streams from server");
            System.exit(1);
        }
    }
     public void ascceptCommand(PlainText commands){
         System.out.println("In IO_CLIEN "+commands.getCommands());
         try {
             SerializationText.serialization(out, commands);
         } catch (IOException e) {
             e.printStackTrace();
         }
     }
    public void sendIO_Client(List<PlainText> list) {
        try {

            System.out.println("send files");

            if(list !=null) {
                for (PlainText plainText : list){
                    System.out.println("In IO_CLIEN "+plainText.toString());
                   SerializationText.serialization(out, plainText);

                }

            }
//            Socket socket = null;
//            ObjectOutputStream out = null;
//            ObjectInputStream in = null;



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


//            out.close();
//            in.close();
//         //   stdIn.close();
//           socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
