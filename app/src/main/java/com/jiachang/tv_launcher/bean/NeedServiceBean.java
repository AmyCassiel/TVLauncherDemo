package com.jiachang.tv_launcher.bean;

import android.graphics.Bitmap;

/**
 * @author Mickey.Ma
 * @date 2020-05-18
 * @description
 */
public class NeedServiceBean {
    private int typeId;
    private int id;
    private String name;
    private String supplyTime;
    private String price;
    private Bitmap image;
    private int amount;

    public NeedServiceBean(int typeId, int id, Bitmap image, String name, String price, String supplyTime, int amount){
        this.typeId = typeId;
        this.id = id;
        this.image = image;
        this.name = name;
        this.supplyTime = supplyTime;
        this.price = price;
        this.amount = amount;
    }
    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
