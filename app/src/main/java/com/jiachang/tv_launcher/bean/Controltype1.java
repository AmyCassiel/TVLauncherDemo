package com.jiachang.tv_launcher.bean;

/**
 * @author Mickey.Ma
 * @date 2020-05-29
 * @description
 */
public class Controltype1 {
    private String name;
    private int image;

    public Controltype1(String name, int image){
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
