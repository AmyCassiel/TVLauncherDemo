package com.jiachang.tv_launcher.bean;

/**
 * @author Mickey.Ma
 * @date 2020-03-26
 * @description
 */
public class Food {
        private String name;
        private int imageId;

        public Food(String name, int imageId){
            this.name = name;
            this.imageId = imageId;
        }

        public String getName() {
            return name;
        }

        public int getImageId() {
            return imageId;
        }
}
