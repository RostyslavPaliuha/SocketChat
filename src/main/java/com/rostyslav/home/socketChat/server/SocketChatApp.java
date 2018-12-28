package com.rostyslav.home.socketChat.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SocketChatApp {

    private ServerSocket serverSocket;

    private static Map<String, ServerMessageSenderThread> connectedClients = new HashMap<>();

    public static Map<String, ServerMessageSenderThread> getConnectedClients() {
        return connectedClients;
    }

    public SocketChatApp() throws IOException {
        serverSocket = new ServerSocket(9999);
    }


    public void launch() throws IOException {
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String userName = in.readLine();
                System.out.println("accepted new client " + userName);
                ServerMessageSenderThread clientThread = new ServerMessageSenderThread(clientSocket);
                clientThread.start();
                connectedClients.put(userName, clientThread);
                String onlineList = String.join(",", connectedClients.keySet());
                connectedClients.forEach((s, serverMessageSenderThread) -> serverMessageSenderThread.send("online:" + onlineList));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class ServerMessageSenderThread extends Thread {
        private Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;

        private StringBuilder messagebuilder;

        public ServerMessageSenderThread(Socket clientSocket) throws IOException {
            this.clientSocket = clientSocket;
            this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            this.out = new PrintWriter(this.clientSocket.getOutputStream());
        }

        @Override
        public void run() {

            while (true) {
                try {
                    messagebuilder = new StringBuilder();
                    String content = in.readLine();
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
            out.println(msg);
            out.flush();
        }
    }

    Runnable registrationThread = () -> {

        try {
            ServerSocket registrationSocket = new ServerSocket(9998);
            DataInputStream in;
            DataOutputStream out;
            File clients = new File("clients.txt");
            FileWriter fileWriter = new FileWriter(clients);

            BufferedReader fileReader = new BufferedReader(new FileReader(clients));
            while (true) {
                Socket clientRegistrationSocket = registrationSocket.accept();
                in = new DataInputStream(clientRegistrationSocket.getInputStream());
                out = new DataOutputStream(clientRegistrationSocket.getOutputStream());
                String client = in.readUTF();
                String[] clientData = client.split(",");
                if (fileReader.lines().noneMatch(s -> s.equals(clientData[0]))) {
                    try {
                        fileWriter.append(client);
                        fileWriter.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    out.writeBoolean(true);
                    out.flush();
                } else {
                    out.writeBoolean(false);
                    out.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    };

    public static void main(String[] args) throws IOException {
        new SocketChatApp().launch();
    }
}
