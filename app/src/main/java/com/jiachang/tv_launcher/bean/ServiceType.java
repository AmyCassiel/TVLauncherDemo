package com.jiachang.tv_launcher.bean;

import android.graphics.Bitmap;

/**
 * @author Mickey.Ma
 * @date 2020-05-18
 * @description
 */
public class ServiceType {
    private String name;
    private String supplyTime;
    private Bitmap image;

    public ServiceType(String name,String supplyTime, Bitmap image){
        this.name = name;
        this.supplyTime = supplyTime;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getSupplyTime(){
        return supplyTime;
    }

    public Bitmap getImageId() {
        return image;
    }
}
