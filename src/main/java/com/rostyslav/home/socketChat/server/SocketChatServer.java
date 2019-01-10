package com.rostyslav.home.socketChat.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketChatServer {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private File clients;
    private static Map<String, ClientMessageSender> connectedClients = new HashMap<>();


    public static Map<String, ClientMessageSender> getConnectedClients() {
        return connectedClients;
    }

    public SocketChatServer() throws IOException {
        clients = new File(System.getProperty("user.dir") + File.separator + "clients.txt");
        clients.createNewFile();
        serverSocket = new ServerSocket(9999);
    }


    public void launch() throws IOException {
        while (true) {
            clientSocket = serverSocket.accept();
            Executors.newSingleThreadExecutor().execute(new MainThread(clientSocket,clients));
        }
    }

    public static void main(String[] args) throws IOException {
        new SocketChatServer().launch();
    }
}
