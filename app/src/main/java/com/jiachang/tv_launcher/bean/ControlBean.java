package com.jiachang.tv_launcher.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mickey.Ma
 * @date 2020-07-15
 * @description
 */
public class ControlBean {
    private String usertype;
    private addrBean addr;
    private mapBean map;
    private offlineBean offline;
    private pageBean page;
    private layoutBean layout;
    private tqBean tq;
    private List<favoriteBean> favorite;
    private List<placelistBean> placelist;
    private List<alarmBean> alarm;
    private List<jrBean> jr;

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public addrBean getAddr() {
        return addr;
    }

    public void setAddr(addrBean addr) {
        this.addr = addr;
    }

    public mapBean getMap() {
        return map;
    }

    public void setMap(mapBean map) {
        this.map = map;
    }

    public offlineBean getOffline() {
        return offline;
    }

    public void setOffline(offlineBean offline) {
        this.offline = offline;
    }

    public pageBean getPage() {
        return page;
    }

    public void setPage(pageBean page) {
        this.page = page;
    }

    public layoutBean getLayout() {
        return layout;
    }

    public void setLayout(layoutBean layout) {
        this.layout = layout;
    }

    public tqBean getTq() {
        return tq;
    }

    public void setTq(tqBean tq) {
        this.tq = tq;
    }

    public List<favoriteBean> getFavorite() {
        return favorite;
    }

    public void setFavorite(List<favoriteBean> favorite) {
        this.favorite = favorite;
    }

    public List<placelistBean> getPlacelist() {
        return placelist;
    }

    public void setPlacelist(List<placelistBean> placelist) {
        this.placelist = placelist;
    }

    public List<alarmBean> getAlarm() {
        return alarm;
    }

    public void setAlarm(List<alarmBean> alarm) {
        this.alarm = alarm;
    }

    public List<jrBean> getJr() {
        return jr;
    }

    public void setJr(List<jrBean> jr) {
        this.jr = jr;
    }

    private class addrBean {
    }

    private class mapBean {
    }

    private class offlineBean {
    }

    private class pageBean {
        private numberBean number;

        private class numberBean {
            /**
             * "2":{"type":"switch","attr":{
             * "ID":"2",
             * "DEVID":"1",
             * "NAME":"家人",
             * "SYSNAME":"jr",
             * "ICON":"jr",
             * "YYBM":null,
             * "INUSE":"1",
             * "CANDEL":"0",
             * "ISR":"1",
             * "ISS":"0",
             * "ISC":"0",
             * "ATTRINT":null
             * },
             * "value":0,
             * "other":[
             * "没人",
             * "有人"
             * ],
             * "detail":false,
             * "phydev":"0"
             * }
             */
            private String type;
            private attrBean attr;
            private Object value;
            private Object other;
            private boolean detail;
            private String phydev;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public attrBean getAttr() {
                return attr;
            }

            public void setAttr(attrBean attr) {
                this.attr = attr;
            }

            public Object getValue() {
                return value;
            }

            public void setValue(Object value) {
                this.value = value;
            }



            public boolean isDetail() {
                return detail;
            }

            public void setDetail(boolean detail) {
                this.detail = detail;
            }

            public String getPhydev() {
                return phydev;
            }

            public void setPhydev(String phydev) {
                this.phydev = phydev;
            }


            private class attrBean {
                /**{
                 * "ID":"2",
                 * "DEVID":"1",
                 * "NAME":"家人",
                 * "SYSNAME":"jr",
                 * "ICON":"jr",
                 * "YYBM":null,
                 * "INUSE":"1",
                 * "CANDEL":"0",
                 * "ISR":"1",
                 * "ISS":"0",
                 * "ISC":"0",
                 * "ATTRINT":null
                 * }
                 * */
                private String ID;
                private String DEVID;
                private String NAME;
                private String SYSNAME;
                private String ICON;
                private String YYBM;
                private String INUSE;
                private String CANDEL;
                private String ISR;
                private String ISS;
                private String ISC;
                private String ATTRINT;

                public String getID() {
                    return ID;
                }

                public void setID(String ID) {
                    this.ID = ID;
                }

                public String getNAME() {
                    return NAME;
                }

                public void setNAME(String NAME) {
                    this.NAME = NAME;
                }

                public String getSYSNAME() {
                    return SYSNAME;
                }

                public void setSYSNAME(String SYSNAME) {
                    this.SYSNAME = SYSNAME;
                }
            }

        }
    }

    private class layoutBean {
    }

    private class tqBean {
    }

    private class favoriteBean {
    }

    private class placelistBean {
    }

    private class alarmBean {
    }

    private class jrBean {
    }
}
