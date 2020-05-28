package com.jiachang.tv_launcher.bean;

import android.graphics.Bitmap;

/**
 * @author Mickey.Ma
 * @date 2020-03-26
 * @description
 */
public class Food {
        private String name;
        private String price;
        private String supplyTime;
        private Bitmap image;

        public Food(String name,String price,String supplyTime, Bitmap image){
            this.name = name;
            this.price = price;
            this.supplyTime = supplyTime;
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public String getPrice(){
            return price;
        }

        public String getSupplyTime(){
            return supplyTime;
        }

        public Bitmap getImageId() {
            return image;
        }
}
