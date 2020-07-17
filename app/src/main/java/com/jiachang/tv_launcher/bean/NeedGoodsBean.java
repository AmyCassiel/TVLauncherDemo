package com.jiachang.tv_launcher.bean;

/**
 * @author Mickey.Ma
 * @date 2020-07-07
 * @description
 */
public class NeedGoodsBean {
    private boolean isExist;
    private int hotelId;
    private String roomNumber;
    private int serDetailId;
    private int amount;
    private int serviceTypeId;
    private String goodsName;
    private double price;
    private String goodsPrice;



    public NeedGoodsBean(int hotelId,String roomNumber,int serDetailId,int amount,int serviceTypeId,double price){
        this.hotelId = hotelId;
        this.roomNumber = roomNumber;
        this.serDetailId = serDetailId;
        this.amount = amount;
        this.serviceTypeId = serviceTypeId;
        this.price = price;
    }

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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isExist() {
        return isExist;
    }

    public void setExist(boolean exist) {
        isExist = exist;
    }
}
