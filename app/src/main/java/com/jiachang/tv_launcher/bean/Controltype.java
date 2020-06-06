package com.jiachang.tv_launcher.bean;

import android.graphics.Bitmap;

/**
 * @author Mickey.Ma
 * @date 2020-05-29
 * @description
 */
public class Controltype {
    private String name;
    private String supplyTime;
    private String local;
    private int image;

    public Controltype(String name,String supplyTime, int image){
        this.name = name;
        this.supplyTime = supplyTime;
        this.local = local;
        this.image = image;
    }


    public String getSupplyTime() {
        return supplyTime;
    }

    public void setSupplyTime(String supplyTime) {
        this.supplyTime = supplyTime;
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
