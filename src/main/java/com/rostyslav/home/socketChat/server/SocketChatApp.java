package com.rostyslav.home.socketChat.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SocketChatApp {

    private ServerSocket serverSocket;
    private final File clients = new File("clients.txt");
    private final BufferedReader fileReader = new BufferedReader(new FileReader(clients));
    private static Map<String, ServerMessageSenderThread> connectedClients = new HashMap<>();

    public static Map<String, ServerMessageSenderThread> getConnectedClients() {
        return connectedClients;
    }

    public SocketChatApp() throws IOException {
        serverSocket = new ServerSocket(9999);
        new Thread(registrationThread).start();
    }


    public void launch() throws IOException {
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String credentials = in.readLine();
                if (credentials.startsWith("credentials:")) {
                    String nameAndPassword = credentials.replace("credentials:", "");
                    String[] credArr = nameAndPassword.split(",");
                    boolean accessGaranted = fileReader.lines()
                            .anyMatch(s -> s.contains(credArr[0]) && s.contains(credArr[1]));
                    if (accessGaranted) {
                        System.out.println("accepted new client " + credArr[0]);
                        ServerMessageSenderThread clientThread = new ServerMessageSenderThread(clientSocket);
                        clientThread.start();
                        connectedClients.put(credArr[0], clientThread);
                        String onlineList = String.join(",", connectedClients.keySet());
                        connectedClients.forEach((s, serverMessageSenderThread) -> serverMessageSenderThread.send("online:" + onlineList));
                    }
                }
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

    private Runnable registrationThread = () -> {

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
                String registrationData = in.readUTF();
                if (registrationData.startsWith("registration:")) {
                   String clientData=registrationData.replace("registration:","");
                    String[] parsedClientData = clientData.split(",");
                    if (fileReader.lines().noneMatch(s -> s.equals(parsedClientData[1]))) {
                        try {
                            fileWriter.append(clientData);
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    };

    public static void main(String[] args) throws IOException {
        new SocketChatApp().launch();
    }
}
