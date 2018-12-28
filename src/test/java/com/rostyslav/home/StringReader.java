package com.rostyslav.home;

import java.io.DataInputStream;
import java.io.IOException;

public class StringReader implements Runnable {
    DataInputStream in;

    public StringReader(DataInputStream dataInputStream) {
        in = dataInputStream;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println(in.readUTF());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
