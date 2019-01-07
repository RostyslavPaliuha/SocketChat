package com.rostyslav.home.socketChat.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ConsoleDemoChatClient {
    Socket socket;
    Scanner keyboardIn;
    String userName = null;
    BufferedReader in;
    PrintWriter out;

    public ConsoleDemoChatClient() throws IOException {
        socket = new Socket("0.0.0.0", 9999);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream());
        out.println(userName);
        out.flush();
        new WriterThread(keyboardIn, out).start();
        new ReaderThread(in).start();

    }

    public class WriterThread extends Thread {

        Scanner in;
        PrintWriter out;

        public WriterThread(Scanner in, PrintWriter out) {
            this.in = in;
            this.out = out;
        }

        @Override
        public void run() {

            String msg = "message:";
            while (true) {
                String message = msg.concat(in.nextLine());
                out.println(message);
                out.flush();
            }
        }
    }


    public class ReaderThread extends Thread {

        private BufferedReader in;

        public ReaderThread(BufferedReader in) {
            this.in = in;
        }

        @Override
        public void run() {
            String msg;
            try {
                while (true) {
                    msg = in.readLine();
                    System.out.println(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws IOException {
        new ConsoleDemoChatClient();
    }
}
