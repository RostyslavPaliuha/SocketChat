package com.rostyslav.home;

import java.io.IOException;
import java.io.ObjectInputStream;

public class ObjectReader implements Runnable {
    ObjectInputStream in;

    public ObjectReader(ObjectInputStream in) {
        this.in = in;
    }

    @Override
    public void run() {
        while (true) {
           try {
               Entity e=(Entity) in.readObject();
               System.out.println(e.getMsg());
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        }
    }
}
