package ru.chabanov.utils;

import ru.chabanov.io_client.IO_Client;
import ru.chabanov.netty_client.NettyClient;
import ru.chabanov.nio_client.NIO_CLIENT_1;

public class Chose_option_client {
    private int number_of_option;
    private String host = "127.0.0.1";
    private int port = 8088;
    private boolean isNatty;

    public boolean isNatty() {
        return isNatty;
    }

    public Chose_option_client(int number_of_option) {
        this.number_of_option = number_of_option;
    }

    public Client_communication client(){
         if(number_of_option == 1){
             IO_Client client = new IO_Client(host,port);
             isNatty=client.isNetty();
             return client;
         }

        if(number_of_option==2) {
        NIO_CLIENT_1 client   = new NIO_CLIENT_1(host, port);
            isNatty=client.isNetty();
            return client;
        }
        if(number_of_option == 3){
     NettyClient client   =  new NettyClient(host,port);
            isNatty=client.isNetty();
            return client;
        }

        return null;

    }
}
