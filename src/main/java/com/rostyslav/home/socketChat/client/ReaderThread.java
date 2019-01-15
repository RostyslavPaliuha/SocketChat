package com.rostyslav.home.socketChat.client;

import javafx.scene.control.TextArea;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

public class ReaderThread implements Runnable {
    private Socket socket;
    private TextArea generalMessageWindow;
    private TextArea usersOnlineStatus;

    public ReaderThread(Socket socket, TextArea generalMessageWindow, TextArea usersOnlineStatus) {
        this.socket = socket;
        this.generalMessageWindow = generalMessageWindow;
        this.usersOnlineStatus = usersOnlineStatus;
    }

    @Override
    public void run() {
        try (DataInputStream in = new DataInputStream(socket.getInputStream())) {
            while (!socket.isClosed()) {
                String inputString="";
                try {
                  inputString = in.readUTF();
                } catch (EOFException e) {

                }
                if (inputString.startsWith("message:")) {
                    String message = inputString.replace("message:", "");
                    generalMessageWindow.appendText(message + "\n");
                } else if (inputString.startsWith("online:")) {
                    String list = inputString.replace("online:", "");
                    String[] arr = list.split(",");
                    usersOnlineStatus.clear();
                    Arrays.stream(arr).forEach(s -> usersOnlineStatus.appendText(s + "\n"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
                    /*if (e instanceof SocketException)
                        allerDialog();
                }*/
        }
    }
}

