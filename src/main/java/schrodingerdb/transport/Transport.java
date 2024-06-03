package main.java.schrodingerdb.transport;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class Transport {
    private Socket socket;
    private Map<String, Runnable> commandMap;
    private PrintWriter output;
    private String receivedText;

    public Transport(Socket socket) {
        this.socket = socket;
        initializeCommandMap();
    }

    private void initializeCommandMap() {
        commandMap = new HashMap<>();
        commandMap.put("PING", this::ping);
        commandMap.put("GET",this::get);
        commandMap.put("SET",this::set);
    }

    public void handle() {
        System.out.println("New client connected");

        try(BufferedReader input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));) {
            this.output = new PrintWriter(this.socket.getOutputStream(), true);
            while ((this.receivedText = input.readLine()) != null) {
                String command = receivedText.split(" ", 2)[0];
                if("EXIT".equalsIgnoreCase(command)){
                    break;
                }
                else if (commandMap.containsKey(command.toUpperCase())) {
                    commandMap.get(command.toUpperCase()).run();
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

    private void ping() {
        try {
            String[] parts = receivedText.split(" ", 2);
            if (parts.length > 1) {
                output.println("+"+parts[1]);
            } else {
                socket.getOutputStream().write("+PONG\r\n".getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Define the functions to handle each command
    private void get() {
       try{

        socket.getOutputStream().write("This is a GET\r\n".getBytes());
    } catch (IOException e) {
        e.printStackTrace();
    }
    }

    private void set() {
        try{

            socket.getOutputStream().write("This is a SET\r\n".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}