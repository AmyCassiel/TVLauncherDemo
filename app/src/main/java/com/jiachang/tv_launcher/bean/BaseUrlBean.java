package com.jiachang.tv_launcher.bean;

import androidx.annotation.Keep;

@Keep
public class BaseUrlBean {
    private String a;
    private String b;
    private String c;
    private String s;
    private String fromdev;
    private boolean on;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getFromdev() {
        return fromdev;
    }

    public void setFromdev(String fromdev) {
        this.fromdev = fromdev;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }
}
