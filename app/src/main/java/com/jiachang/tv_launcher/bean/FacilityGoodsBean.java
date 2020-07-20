package com.jiachang.tv_launcher.bean;

import android.graphics.Bitmap;

/**
 * @author Mickey.Ma
 * @date 2020-05-28
 * @description
 */
public class FacilityGoodsBean {
    private String name;
    private String supplyTime;
    private String local;
    private Bitmap image;

    public FacilityGoodsBean(String name, String local, String supplyTime, Bitmap image){
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

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
