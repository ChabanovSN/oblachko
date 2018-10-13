package ru.chabanov;

import ru.chabanov.io_server.IO_Server;
import ru.chabanov.netty_server.NettyServer;
import ru.chabanov.nio_server.NIO_SERVER_1;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;

public class ChoseServer implements Closeable {
   private static BufferedReader reader = null;
    private static final int PORT_NUMBER = 8088;
    private static final String PATH_TO_MAIN_FOLDER = "C:\\Users\\User\\Desktop\\serverFolder\\";
    public static void main(String[] args) {
     choosingServer();

    }
    private static void choosingServer(){

    reader = new BufferedReader(new InputStreamReader(System.in));
        int yourChoise=0;
        while (true){

            System.out.println("Выберете сервер: \n1(IO server)\n2(NIO server)\n3(Netty server)\n4 Выход");
            try {
                yourChoise = Integer.parseInt(reader.readLine());
                System.out.println(yourChoise);
                if(yourChoise==1)
                {
                    new IO_Server(PATH_TO_MAIN_FOLDER, PORT_NUMBER);
                    reader.close();
                    break;

                }
                if (yourChoise == 2) {
                    new NIO_SERVER_1(PATH_TO_MAIN_FOLDER, PORT_NUMBER);
                    reader.close();
                    break;
                }
                if (yourChoise == 3)
                    new NettyServer(PATH_TO_MAIN_FOLDER, PORT_NUMBER);
                reader.close();
                if (yourChoise == 4) {
                    System.out.println("Выход.\nДо свидания.");
                    reader.close();
                    System.exit(1);
                }
                System.out.println("Вы выбрали " + yourChoise + "- это неверный выбор.");

                //   yourChoise = Integer.parseInt(reader.readLine());

            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
