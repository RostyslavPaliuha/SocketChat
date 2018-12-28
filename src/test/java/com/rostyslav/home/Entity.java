package com.rostyslav.home;

import java.io.Serializable;

public class Entity implements Serializable {
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}