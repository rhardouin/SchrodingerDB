package main.java.schrodingerdb.transport;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.IOException;
import java.net.SocketException;

public class Transport {
    private Socket socket;

    public Transport(Socket socket) {
        this.socket = socket;
    }

    public void handle() {
        System.out.println("New client connected");

        try(BufferedReader input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));PrintWriter output = new PrintWriter(this.socket.getOutputStream(), true);) {
            String receivedText;
            while ((receivedText = input.readLine()) != null) {
                if (receivedText.startsWith("PING")) {
                    String[] parts = receivedText.split(" ", 2);
                    if (parts.length > 1) {
                        output.println("+"+parts[1]);
                    } else {
                        socket.getOutputStream().write("+PONG\r\n".getBytes());
                    }
                } else if ("EXIT".equalsIgnoreCase(receivedText)) {
                    break;
                } else {
                    output.println("Error: Invalid request");
                }
            }
        } catch (SocketException e) {
            System.out.println("Connection was reset by client");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}