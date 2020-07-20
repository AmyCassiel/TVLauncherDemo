package com.jiachang.tv_launcher.bean;

import android.graphics.Bitmap;

/**
 * @author Mickey.Ma
 * @date 2020-03-26
 * @description  FoodListActivity中RecyclerView的Item布局实体类
 */
public class FoodListBean {
    private int id;
    private final String name;
    private final String price;
    private String supplyTime;
    private final Bitmap image;
    private int count;

    public FoodListBean(int id, String name, String price, Bitmap image, int count) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.supplyTime = supplyTime;
        this.image = image;
        this.count = count;
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

    public String getPrice() {
        return price;
    }

    public String getSupplyTime() {
        return supplyTime;
    }

    public Bitmap getImageId() {
        return image;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
