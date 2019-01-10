package com.rostyslav.home.socketChat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

public class ClientMessageSender implements Runnable {
    private Socket clientSocket;

    public ClientMessageSender(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {

        while (clientSocket.isConnected()) {
            try (DataInputStream in = new DataInputStream(this.clientSocket.getInputStream())) {
                String content = in.readUTF();
                if (content.startsWith("message:")) {
                    Map<String, ClientMessageSender> map = SocketChatServer.getConnectedClients();
                    map.forEach((a, b) -> b.send(content));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void send(String msg) {
        try (DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream())) {
            out.writeUTF(msg);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}