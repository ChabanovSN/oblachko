package ru.chabanov.nio_client.dont_use;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class NIO_Client {
    private SocketChannel channel;
    private Boolean connectedToServer = false;
    private Hook hook;
    private String host = "localhost";
    private int port = 8080;

    public static void main(String[] args) {
        new NIO_Client().registerHook((String s) -> System.out.printf("CLIENT LOG: RESPONSE %s%n", s));
    }

    public interface Hook {
        void register(String s);
    }

    public void registerHook(Hook hook) {
        this.hook = hook;
    }

    public void connect(String nickname) {
        try {
            if (connectedToServer) {
                return;
            }
            channel = SocketChannel.open();
            channel.configureBlocking(false);
            if (!channel.isOpen()) {
                channel = SocketChannel.open();
            }
            channel.connect(new InetSocketAddress(host, port));
            System.out.printf("CLIENT LOG: Connecting to %s on port %d%n", host, port);
            while (!channel.finishConnect()) {
                try {
                    Thread.sleep(200);
                } catch (Exception exc) {
                    return;
                }
                System.out.print(".");
            }
            System.out.println("CLIENT LOG: Connected!");
            connectedToServer = true;

            // Create a new thread to listen to InputStream event
            new ServerListener().start();
            sendMessage(Commands.LOGIN, nickname, "");
        } catch (IOException exc) {
            System.out.printf("CLIENT LOG: ERROR -> Problem with connecting to %s, %s%n",
                    host, exc.getLocalizedMessage());
            System.exit(1);
        }
    }

    public void disconnect(String username) {
        if (!connectedToServer) {
            return;
        }
        try {
            sendMessage(Commands.LOGOUT, username, "");
            connectedToServer = false;
        } catch (Exception exc) {
            System.out.printf("CLIENT LOG: ERROR -> Problem with disconnecting %s%n", host);
            System.exit(3);
        }
    }

    public boolean sendMessage(Commands commands, String nickname, String msg) {
        if (!connectedToServer) {
            return false;
        }
        String messageToSend = commands.name() + " " + nickname + ": " + msg + '\n';
        System.out.printf("CLIENT LOG: Sending message: %s%n", messageToSend);
        ByteBuffer messageByteBuffer = ByteBuffer.wrap(messageToSend.getBytes(Charset.forName("UTF-8")));
        try {
            channel.write(messageByteBuffer);
        } catch (IOException e) {
            System.out.printf("CLIENT LOG: Error -> Problem with read or write to/from server %s%n",
                    e.getLocalizedMessage());
        }
        return true;
    }

    class ServerListener extends Thread {

        public void run() {
            try {
                ByteBuffer buffer = ByteBuffer.allocate(1024);

                while (connectedToServer) {
                    buffer.clear();
                    if (channel.read(buffer) > 0) {
                        buffer.flip();
                        String response = new String(buffer.array(), 0, buffer.limit());
                        System.out.printf("CLIENT LOG: Server response -> %s%n", response);
                        hook.register(response);
                    }
                }
                channel.close();
                channel.socket().close();
                System.out.println("CLIENT LOG: Server logout!");
            } catch (IOException e) {
                System.out.println("CLIENT LOG: " + e.getMessage());
                connectedToServer = false;
            }
        }
    }
}
