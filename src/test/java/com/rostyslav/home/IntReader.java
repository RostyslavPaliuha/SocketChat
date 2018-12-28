package com.rostyslav.home;

import java.io.DataInputStream;
import java.io.IOException;

public class IntReader implements Runnable {
    DataInputStream in;

    public IntReader(DataInputStream in) {
        this.in = in;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println(in.readInt());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
