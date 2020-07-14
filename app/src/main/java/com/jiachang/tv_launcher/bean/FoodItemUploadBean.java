package com.jiachang.tv_launcher.bean;

/**
 * @author Mickey.Ma
 * @date 2020-07-02
 * @description  FoodItem提交订单 的实体类
 */
public class FoodItemUploadBean {
    private int hotelId;
    private String roomNumber;
    private int serDetailId;
    private int amount;
    private int serviceTypeId;

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getSerDetailId() {
        return serDetailId;
    }

    public void setSerDetailId(int serDetailId) {
        this.serDetailId = serDetailId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(int serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }
}
