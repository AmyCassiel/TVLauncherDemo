package com.jiachang.tv_launcher.bean;

import android.graphics.Bitmap;

/**
 * @author Mickey.Ma
 * @date 2020-05-18
 * @description
 */
public class NeedServiceType {
    private String name;
    private String supplyTime;
    private String explain;
    private Bitmap image;

    public NeedServiceType(Bitmap image,String name, String supplyTime, String explain){
        this.image = image;
        this.name = name;
        this.supplyTime = supplyTime;
        this.explain = explain;
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

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }
}
