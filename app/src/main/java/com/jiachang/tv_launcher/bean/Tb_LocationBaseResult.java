package com.jiachang.tv_launcher.bean;

/**
 * @author Mickey.Ma
 * @date 2020-03-25
 * @description
 */
public class Tb_LocationBaseResult {
    private String address;
    private getresult content;
    private int status;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public getresult getContent() {
        return content;
    }

    public void setContent(getresult content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class getresult {
        private String address;
        private getaddressComponent address_detail;
        private getpoint point;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public getaddressComponent getAddress_detail() {
            return address_detail;
        }

        public void setAddress_detail(getaddressComponent address_detail) {
            this.address_detail = address_detail;
        }

        public getpoint getPoint() {
            return point;
        }

        public void setPoint(getpoint point) {
            this.point = point;
        }

        public class getaddressComponent {
            public String city;
            public int city_code;
            public String district;
            public String province;
            public String street;
            public String street_number;

        }

        public class getpoint {
            public String x;
            public String y;
        }
    }
}
