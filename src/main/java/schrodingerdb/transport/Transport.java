package main.java.schrodingerdb.transport;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class Transport {
    private Socket socket;

    public Transport(Socket socket) {
        this.socket = socket;
    }

    public void handle() {
        System.out.println("New client connected");

        try(BufferedReader input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));PrintWriter output = new PrintWriter(this.socket.getOutputStream(), true);) {
            String receivedText = input.readLine();
            if ("PING".equalsIgnoreCase(receivedText)) {
                socket.getOutputStream().write("+PONG\r\n".getBytes());
            } else {
                output.println("Error: Invalid request");
            }
        } catch (SocketException e) {
            System.out.println("Connection was reset by client");
        } catch (Exception e) {
            e.printStackTrace();
        }
            }
}