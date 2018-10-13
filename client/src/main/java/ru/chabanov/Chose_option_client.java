package ru.chabanov;

import ru.chabanov.io_client.IO_Client;
import ru.chabanov.netty_client.NettyClient;
import ru.chabanov.nio_client.NIO_CLIENT_1;

public class Chose_option_client {
    private int number_of_option;
    private String host = "127.0.0.1";
    private int port = 8088;
    public Chose_option_client(int number_of_option) {
        this.number_of_option = number_of_option;
    }

    public Client_communication client(){
         if(number_of_option == 1)
            return new IO_Client(host,port);
        if(number_of_option==2)
            return new NIO_CLIENT_1(host,port);
        if(number_of_option == 3)
            return  new NettyClient(host,port);
        return null;

    }
}
