package com.rostyslav.home.socketChat.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class SocketChatServer {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private File clients;
    private DataOutputStream out;
    private DataInputStream in;
    private BufferedReader fileReader;
    private BufferedWriter fileWriter;
    private static Map<String, ServerMessageSenderThread> connectedClients = new HashMap<>();

    public static Map<String, ServerMessageSenderThread> getConnectedClients() {
        return connectedClients;
    }

    public SocketChatServer() throws IOException {
        clients = new File(System.getProperty("user.dir") + File.separator + "clients.txt");
        clients.createNewFile();
        fileReader = new BufferedReader(new FileReader(clients));
        fileWriter = new BufferedWriter(new FileWriter(clients, true));
        serverSocket = new ServerSocket(9999);
    }


    public void launch() throws IOException {
        while (true) {
            clientSocket = serverSocket.accept();
            new Thread(new MainThread(clientSocket)).start();
        }
    }


    public class ServerMessageSenderThread extends Thread {
        private Socket clientSocket;
        private DataInputStream in;
        private DataOutputStream out;

        private StringBuilder messagebuilder;

        public ServerMessageSenderThread(Socket clientSocket) throws IOException {
            this.clientSocket = clientSocket;
            this.in = new DataInputStream(this.clientSocket.getInputStream());
            this.out = new DataOutputStream(this.clientSocket.getOutputStream());
        }

        @Override
        public void run() {

            while (true) {
                try {
                    messagebuilder = new StringBuilder();
                    String content = in.readUTF();
                    if (content.startsWith("message:")) {
                        messagebuilder.append(content);
                        Map<String, ServerMessageSenderThread> map = getConnectedClients();
                        map.forEach((a, b) -> b.send(messagebuilder.toString()));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void send(String msg) {
            try {
                out.writeUTF(msg);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class MainThread implements Runnable {

        private Socket clientSocket;
        private DataOutputStream out;
        private DataInputStream in;
        private BufferedWriter fileWriter;
        private BufferedReader fileReader;

        public MainThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                this.fileReader = new BufferedReader(new FileReader(clients));
                this.fileWriter = new BufferedWriter(new FileWriter(clients, true));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    in = new DataInputStream(clientSocket.getInputStream());
                    out = new DataOutputStream(clientSocket.getOutputStream());
                    String data = "";
                    try {
                        data = in.readUTF();
                    } catch (EOFException e) {


                    }
                    if (data.startsWith("login:")) {
                        String nameAndPassword = data.replace("login:", "").replace(" ", "");
                        String[] credArr = nameAndPassword.split(",");
                        boolean accessGaranted = fileReader.lines()
                                .anyMatch(s -> s.contains(credArr[0]) && s.contains(credArr[1]));
                        if (accessGaranted) {
                            out.writeUTF(credArr[0]);
                            System.out.println("accepted new client " + credArr[0]);
                            ServerMessageSenderThread clientThread = new ServerMessageSenderThread(clientSocket);
                            clientThread.start();
                            connectedClients.put(credArr[0], clientThread);
                            String onlineList = String.join(",", connectedClients.keySet());
                            connectedClients.forEach((s, serverMessageSenderThread) -> serverMessageSenderThread.send("online:" + onlineList));
                        }
                    } else if (data.startsWith("registration:")) {
                        String clientData = data.replace("registration:", "");
                        String[] parsedClientData = clientData.split(",");
                        if (fileReader.lines().noneMatch(s -> s.equals(parsedClientData[1]))) {
                            fileWriter.append(clientData);
                            fileWriter.flush();
                            out.writeBoolean(true);
                            out.flush();
                        }
                    }
                }
            } catch (IOException e) {

            } finally {
                try {
                    out.close();
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new SocketChatServer().launch();
    }
}
