package com.jiachang.tv_launcher.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mickey.Ma
 * @date 2020-05-28
 * @description
 */
public class Control {
    private String usertype;
    private Object addr;
    private Object map;
    private List offline;
    private PageBean page;

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public class PageBean {
        private  String i;
        private  DatasBean dataBean;

        public String getI() {
            return i;
        }

        public void setI(String i) {
            this.i = i;
        }

        public DatasBean getDataBean() {
            return dataBean;
        }

        public void setDataBean(DatasBean dataBean) {
            this.dataBean = dataBean;
        }

        public class DatasBean {
            private String type;
            private AttrBean attrBean;
            private int value;

            public AttrBean getAttrBean() {
                return attrBean;
            }

            public void setAttrBean(AttrBean attrBean) {
                this.attrBean = attrBean;
            }

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public class AttrBean {
                private String id;
                private String name;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }
        }
    }
}
