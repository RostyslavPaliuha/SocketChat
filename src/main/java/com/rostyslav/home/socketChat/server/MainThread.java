package com.rostyslav.home.socketChat.server;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Executors;

class MainThread implements Runnable {

    private Socket clientSocket;
    private File clients;
    private volatile boolean chooseMaked = false;

    public MainThread(Socket clientSocket, File clients) {
        this.clientSocket = clientSocket;
        this.clients = clients;
    }

    @Override
    public void run() {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(clients));
             BufferedWriter fileWriter = new BufferedWriter(new FileWriter(clients, true));
             DataInputStream in = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream())) {
            while (!chooseMaked) {
                String data = in.readUTF();
                if (data.startsWith("login:")) {
                    String nameAndPassword = data.replace("login:", "").replace(" ", "");
                    String[] credArr = nameAndPassword.split(",");
                    boolean accessGaranted = fileReader.lines()
                            .anyMatch(s -> s.contains(credArr[0]) && s.contains(credArr[1]));
                    if (accessGaranted) {
                        out.writeUTF(credArr[0]);
                        System.out.println("accepted new client " + credArr[0]);
                        ClientMessageSender clientThread = new ClientMessageSender(clientSocket);
                        Executors.newSingleThreadExecutor().execute(clientThread);
                        SocketChatServer.getConnectedClients().put(credArr[0], clientThread);
                        String onlineList = String.join(",", SocketChatServer.getConnectedClients().keySet());
                        SocketChatServer.getConnectedClients().forEach((s, serverMessageSenderThread) -> serverMessageSenderThread.send("online:" + onlineList));
                        chooseMaked = true;
                    }
                } else if (data.startsWith("registration:")) {
                    String clientData = data.replace("registration:", "");
                    String[] parsedClientData = clientData.split(",");
                    if (fileReader.lines().noneMatch(s -> s.equals(parsedClientData[1]))) {
                        fileWriter.append(clientData);
                        fileWriter.flush();
                        out.writeBoolean(true);
                        out.flush();
                        chooseMaked = true;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}