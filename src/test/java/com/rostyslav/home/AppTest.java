package com.rostyslav.home;

import org.junit.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class AppTest {

    @Test
    public void socketAndSocketChanel() throws IOException {

        Runnable server = () -> {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(10000);

                while (true) {
                    Socket serverSideClientSocket = serverSocket.accept();
                    InputStream inputStream = serverSideClientSocket.getInputStream();
                    DataInputStream dataInput = new DataInputStream(inputStream);
                    ObjectInputStream inObj = new ObjectInputStream(inputStream);

                    Thread t1 = new Thread(new StringReader(dataInput));
                    t1.start();
                    t1.join();
                    Thread t2 = new Thread(new IntReader(dataInput));
                    t2.start();
                    t2.join();
                    Thread t3 = new Thread(new ObjectReader(inObj));
                    t3.start();
                    t3.join();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        new Thread(server).start();
        Socket socket = new Socket("0.0.0.0", 10000);
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

        outputStream.writeUTF("SOCKET STRING");
        outputStream.flush();

        outputStream.writeInt(10000);
        outputStream.flush();

        Entity e = new Entity();
        e.setMsg("message");
        objectOutputStream.writeObject(e);
    }

    @Test
    public void stringTesting() {
        String msg= "message: my message.";
        String generatedMsg=msg.replace("message:", "username:");
        System.out.println(generatedMsg);
    }


}
