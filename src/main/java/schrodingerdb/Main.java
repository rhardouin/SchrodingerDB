package schrodingerdb;

import java.net.ServerSocket;
import java.net.Socket;
import main.java.schrodingerdb.transport.*;
public class Main {
    public static void main(String[] args) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(6379)) {
            System.out.println("Server is listening on port 6379");

            while (true) {
                Socket socket = serverSocket.accept();
                Transport transport = new Transport(socket);
                Thread clientThread = new Thread(() -> {
                    try {
                        transport.handle();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                clientThread.start();
            }
        }
    }
}