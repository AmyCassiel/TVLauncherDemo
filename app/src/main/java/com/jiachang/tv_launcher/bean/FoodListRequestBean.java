package com.jiachang.tv_launcher.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Mickey.Ma
 * @date 2020-07-01
 * @description  FoodListActivity 请求获取云端数据 的解析实体类
 */
public class FoodListRequestBean {
    /**{"code":0,
     * "serviceDetails":
     * [{"id":30,"serConfId":4,"needName":"鲍鱼","needImage":"https://jczh.jiachang8.com/reservation_img/serviceCofType/33998697-f5.png","totalAmount":null,"price":199.00,"classify":"菜品","needConfirm":null,"overstay":null,"supplyStartTime":1588435200000,"supplyEndTime":1588521599000,"deliveryType":1,"allDay":false},{"id":39,"serConfId":4,"needName":"糖醋里脊","needImage":"https://jczh.jiachang8.com/reservation_img/serviceCofType/d8ba1f8f-dd.png","totalAmount":null,"price":40.00,"classify":"菜品","needConfirm":null,"overstay":null,"supplyStartTime":1588435200000,"supplyEndTime":1588521599000,"deliveryType":1,"allDay":false},{"id":40,"serConfId":4,"needName":"红烧肉","needImage":"https://jczh.jiachang8.com/reservation_img/serviceCofType/a601c029-8b.png","totalAmount":null,"price":35.00,"classify":"菜品","needConfirm":null,"overstay":null,"supplyStartTime":1588435200000,"supplyEndTime":1588521599000,"deliveryType":1,"allDay":false}]}
     * */
    private int code;
    private ArrayList<ServiceDetailBean> serviceDetails;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ArrayList<ServiceDetailBean> getServiceDetails() {
        return serviceDetails;
    }

    public void setServiceDetails(ArrayList<ServiceDetailBean> serviceDetails) {
        this.serviceDetails = serviceDetails;
    }

    public static class ServiceDetailBean implements Serializable {
        /**{"id":30,
         * "serConfId":4,
         * "needName":"鲍鱼",
         * "needImage":"https://jczh.jiachang8.com/reservation_img/serviceCofType/33998697-f5.png",
         * "totalAmount":null,
         * "price":199.00,
         * "classify":"菜品",
         * "needConfirm":null,
         * "overstay":null,
         * "supplyStartTime":1588435200000,
         * "supplyEndTime":1588521599000,
         * "deliveryType":1,
         * "allDay":false}
         * */
        private int id;
        private int serConfId;
        private String needName;
        private String needImage;
        private String totalAmount;
        private float price;
        private String classify;
        private String needConfirm;
        private String overstay;
        private long supplyStartTime;
        private long supplyEndTime;
        private int deliveryType;
        private boolean allDay;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNeedName() {
            return needName;
        }

        public void setNeedName(String needName) {
            this.needName = needName;
        }

        public String getNeedImage() {
            return needImage;
        }

        public void setNeedImage(String needImage) {
            this.needImage = needImage;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public long getSupplyStartTime() {
            return supplyStartTime;
        }

        public void setSupplyStartTime(long supplyStartTime) {
            this.supplyStartTime = supplyStartTime;
        }

        public long getSupplyEndTime() {
            return supplyEndTime;
        }

        public void setSupplyEndTime(long supplyEndTime) {
            this.supplyEndTime = supplyEndTime;
        }
    }
}
