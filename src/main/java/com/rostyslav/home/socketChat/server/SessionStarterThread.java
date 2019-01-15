package com.rostyslav.home.socketChat.server;

import java.io.*;
import java.net.Socket;


class SessionStarterThread implements Runnable {

    private Socket socket;
    private File clients;
    private volatile boolean chooseMaked = false;
    private DataInputStream in;
    private DataOutputStream out;

    public SessionStarterThread(Socket socket, File clients) {
        this.socket = socket;
        this.clients = clients;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            socket.setKeepAlive(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try (BufferedReader fileReader = new BufferedReader(new FileReader(clients));
             BufferedWriter fileWriter = new BufferedWriter(new FileWriter(clients, true))) {
            while (!chooseMaked) {
                String data = in.readUTF();
                if (data.startsWith("login:")) {
                    String nameAndPassword = data.replace("login:", "").replace(" ", "");
                    String[] credArr = nameAndPassword.split(",");
                    boolean accessGaranted = fileReader.lines()
                            .anyMatch(s -> s.contains(credArr[0]) && s.contains(credArr[1]));
                    if (accessGaranted) {
                        System.out.println("accepted new client " + credArr[0]);
                        ClientSessionThread clientSessionThread = new ClientSessionThread(socket);
                        new Thread(clientSessionThread).start();
                        SocketChatServer.getConnectedClients().put(credArr[0], clientSessionThread);
                        String onlineList = String.join(",", SocketChatServer.getConnectedClients().keySet());
                        SocketChatServer.getConnectedClients().forEach((s, serverMessageSenderThread) -> serverMessageSenderThread.send("online:" + onlineList));
                        out.writeUTF(credArr[0]);
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