package com.jiachang.tv_launcher.bean;

/**
 * @author Mickey.Ma
 * @date 2020-05-29
 * @description
 */
public class ControlDevicesBean {
    private String type;
    private String name;
    private String state;
    private String devId;
    private int stateCode;
    private int image;

    public ControlDevicesBean(String type, String name, String state, int stateCode, String devId, int image){
        this.type =type;
        this.name = name;
        this.state = state;
        this.devId = devId;
        this.image = image;
        this.stateCode =stateCode;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
