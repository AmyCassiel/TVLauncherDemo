package com.jiachang.tv_launcher.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * @author Mickey.Ma
 * @date 2020-07-06
 * @description
 */
public class FoodIntentBean implements Serializable {
    private Bitmap foodImage;
    private int foodId;
    private int foodCount;
    private String foodName;
    private String foodPrice;

    public FoodIntentBean(int foodId, String foodName,  String foodPrice, Bitmap foodImage,  int foodCount) {
        this.foodImage = foodImage;
        this.foodId = foodId;
        this.foodCount = foodCount;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
    }

    public int getFoodCount() {
        return foodCount;
    }

    public void setFoodCount(int foodCount) {
        this.foodCount = foodCount;
    }

    public Bitmap getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(Bitmap foodImage) {
        this.foodImage = foodImage;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }
}
