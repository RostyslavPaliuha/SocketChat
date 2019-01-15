package com.rostyslav.home.socketChat.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketChatServer {

    private ServerSocket serverSocket;
    private File users;
    private static Map<String, ClientSessionThread> connectedClients = Collections.synchronizedMap(new HashMap<>());

    public SocketChatServer() throws IOException {
        users = new File(System.getProperty("user.dir") + File.separator + "users.txt");
        users.createNewFile();
        serverSocket = new ServerSocket(9999);

    }

    public void launch() throws IOException {
        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(new SessionStarterThread(socket, users)).start();
        }
    }

    public static Map<String, ClientSessionThread> getConnectedClients() {
        return connectedClients;
    }

    public static void main(String[] args) throws IOException {
        new SocketChatServer().launch();
    }
}
