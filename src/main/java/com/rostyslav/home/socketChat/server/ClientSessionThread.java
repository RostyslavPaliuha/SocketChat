package com.rostyslav.home.socketChat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

public class ClientSessionThread implements Runnable {
    private Socket socket;
    private DataInputStream inputStream;

    public ClientSessionThread(Socket socket) {
        this.socket = socket;
        try {
            inputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {
            while (!socket.isInputShutdown()) {
                String data = inputStream.readUTF();
                if (data.startsWith("message:") || data.startsWith("online:")) {
                    Map<String, ClientSessionThread> map = SocketChatServer.getConnectedClients();
                    map.forEach((a, b) -> b.send(data));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void send(String message) {
        try (DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
            out.writeUTF(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}