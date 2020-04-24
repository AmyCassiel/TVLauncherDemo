package com.jiachang.tv_launcher.bean;

/**
 * @author Mickey.Ma
 * @date 2020-04-21
 * @description
 */
public class PresentCachInfo {
    private String  msg;
    private DataBean data;
    private String code;

    public PresentCachInfo(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean{
        private String hotelName;
        private String hotelIntroduction;
        private String usageMonitoring;
        private String userNeeds;
        private String wifiName;
        private String wifiPassword;
        private String mac;

        public String getHotelName() {
            return hotelName;
        }

        public void setHotelName(String hotelName) {
            this.hotelName = hotelName;
        }

        public String getHotelIntroduction() {
            return hotelIntroduction;
        }

        public void setHotelIntroduction(String hotelIntroduction) {
            this.hotelIntroduction = hotelIntroduction;
        }

        public String getUsageMonitoring() {
            return usageMonitoring;
        }

        public void setUsageMonitoring(String usageMonitoring) {
            this.usageMonitoring = usageMonitoring;
        }

        public String getUserNeeds() {
            return userNeeds;
        }

        public void setUserNeeds(String userNeeds) {
            this.userNeeds = userNeeds;
        }

        public String getWifiName() {
            return wifiName;
        }

        public void setWifiName(String wifiName) {
            this.wifiName = wifiName;
        }

        public String getWifiPassword() {
            return wifiPassword;
        }

        public void setWifiPassword(String wifiPassword) {
            this.wifiPassword = wifiPassword;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public String getBreakfastTime() {
            return breakfastTime;
        }

        public void setBreakfastTime(String breakfastTime) {
            this.breakfastTime = breakfastTime;
        }

        public String getLunchTime() {
            return lunchTime;
        }

        public void setLunchTime(String lunchTime) {
            this.lunchTime = lunchTime;
        }

        public String getDinnerTime() {
            return dinnerTime;
        }

        public void setDinnerTime(String dinnerTime) {
            this.dinnerTime = dinnerTime;
        }

        private String breakfastTime;
        private String lunchTime;
        private String dinnerTime;

    }
}
