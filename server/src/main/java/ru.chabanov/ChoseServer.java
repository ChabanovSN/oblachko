package ru.chabanov;

import ru.chabanov.io_server.IO_Server;
import ru.chabanov.netty_server.NettyServer;
import ru.chabanov.nio_server.NIO_SERVER_1;

import java.io.*;

public class ChoseServer implements Closeable {
   private static BufferedReader reader = null;
    private static final int PORT_NUMBER = 8088;
    private static  String PATH_TO_MAIN_FOLDER = "C:\\serverFolder\\";
    public static void main(String[] args) {
     choosingServer();

    }
    private static void choosingServer(){

    reader = new BufferedReader(new InputStreamReader(System.in));
        int yourChoise=0;
        String root=null;
        while (true){

            System.out.println("Выберете сервер: \n1(IO server)\n2(NIO server)\n3(Netty server)\n4 Выход");
            try {
                yourChoise = Integer.parseInt(reader.readLine());
                System.out.println("Выберете корневой каталог.\n По умолчанию С:\\serverFolder\\");
                root = reader.readLine();

                if(root.length()>2)PATH_TO_MAIN_FOLDER=root;
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
