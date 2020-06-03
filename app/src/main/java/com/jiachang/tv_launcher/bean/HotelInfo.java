package com.jiachang.tv_launcher.bean;

import java.util.List;

/**
 * @author Mickey.Ma
 * @date 2020-05-16
 * @description
 */
public class HotelInfo {
    private int code;
    private HotelDbBean hotelDb;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public HotelDbBean getHotelDb() {
        return hotelDb;
    }

    public void setHotelDb(HotelDbBean hotelDb) {
        this.hotelDb = hotelDb;
    }

    public static class HotelDbBean {

        private int id;
        private String hotelName;
        private String hotelEngName;
        private String address;
        private String hotelIntrodu;
        private String telephone;
        private String fax;
        private String postCode;
        private String hotelStar;
        private String praciceDate;
        private String fitmentDate;
        private String city;
        private String area;
        private String business;
        private String location;
        private String distance;
        private String checkInTime;
        private String checkOutTime;
        private String files;
        private String appearancePicUrl;
        private String hotelFacilitys;
        private String hotelPolicys;
        private String hotelArounds;
        private String hotelLabel;
        private String score;
        private String userId;
        private String price;
        private String logo;
        private String roomCount;
        private String wifi;
        private List<ServiceConfsBean> serviceConfs;
        private List<HotelFacilitiesBean> hotelFacilities;

        public String getHotelName() {
            return hotelName;
        }

        public void setHotelName(String hotelName) {
            this.hotelName = hotelName;
        }

        public String getHotelIntrodu() {
            return hotelIntrodu;
        }

        public void setHotelIntrodu(String hotelIntrodu) {
            this.hotelIntrodu = hotelIntrodu;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getBusiness() {
            return business;
        }

        public void setBusiness(String business) {
            this.business = business;
        }

        public String getAppearancePicUrl() {
            return appearancePicUrl;
        }

        public void setAppearancePicUrl(String appearancePicUrl) {
            this.appearancePicUrl = appearancePicUrl;
        }

        public String getHotelFacilitys() {
            return hotelFacilitys;
        }

        public void setHotelFacilitys(String hotelFacilitys) {
            this.hotelFacilitys = hotelFacilitys;
        }

        public String getHotelPolicys() {
            return hotelPolicys;
        }

        public void setHotelPolicys(String hotelPolicys) {
            this.hotelPolicys = hotelPolicys;
        }

        public String getHotelArounds() {
            return hotelArounds;
        }

        public void setHotelArounds(String hotelArounds) {
            this.hotelArounds = hotelArounds;
        }

        public String getWifi() {
            return wifi;
        }

        public void setWifi(String wifi) {
            this.wifi = wifi;
        }

        public List<ServiceConfsBean> getServiceConfs() {
            return serviceConfs;
        }

        public void setServiceConfs(List<ServiceConfsBean> serviceConfs) {
            this.serviceConfs = serviceConfs;
        }

        public List<HotelFacilitiesBean> getHotelFacilities() {
            return hotelFacilities;
        }

        public void setHotelFacilities(List<HotelFacilitiesBean> hotelFacilities) {
            this.hotelFacilities = hotelFacilities;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public static class ServiceConfsBean {
            private int id;
            private int hotelId;
            private int serviceTypeId;
            private ServiceTypeBean serviceType;
            private String interfaceUrl;
            private int status;
            private long waiterStartTime;
            private long waiterEndTime;
            private int gmtCreate;
            private int gmtModified;
            private String deliveryType;
            private List<ServiceDetailsBean> serviceDetails;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public ServiceTypeBean getServiceType() {
                return serviceType;
            }

            public void setServiceType(ServiceTypeBean serviceType) {
                this.serviceType = serviceType;
            }

            public List<ServiceDetailsBean> getServiceDetails() {
                return serviceDetails;
            }

            public void setServiceDetails(List<ServiceDetailsBean> serviceDetails) {
                this.serviceDetails = serviceDetails;
            }

            public int getServiceTypeId() {
                return serviceTypeId;
            }

            public void setServiceTypeId(int serviceTypeId) {
                this.serviceTypeId = serviceTypeId;
            }

            public long getWaiterStartTime() {
                return waiterStartTime;
            }

            public void setWaiterStartTime(long waiterStartTime) {
                this.waiterStartTime = waiterStartTime;
            }

            public long getWaiterEndTime() {
                return waiterEndTime;
            }

            public void setWaiterEndTime(long waiterEndTime) {
                this.waiterEndTime = waiterEndTime;
            }


            public static class ServiceTypeBean {
                private int id;

                private String serviceTypeName;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getServiceTypeName() {
                    return serviceTypeName;
                }

                public void setServiceTypeName(String serviceTypeName) {
                    this.serviceTypeName = serviceTypeName;
                }
            }

            public static class ServiceDetailsBean {
                private int id;
                private int serConfId;
                private String needName;
                private String needImage;
                private int totalAmount;
                private float price;
                private String classify;
                private int needConfirm;
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

        public static class HotelFacilitiesBean {
            private int id;
            private String name;
            private String location;
            private String time;
            private int type;
            private HotelFacilitiesTypeBean hotelFacilitiesType;
            private int breakfast;
            private int hotelId;
            private String charges;
            private String description;
            private String image;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public HotelFacilitiesTypeBean getHotelFacilitiesType() {
                return hotelFacilitiesType;
            }

            public void setHotelFacilitiesType(HotelFacilitiesTypeBean hotelFacilitiesType) {
                this.hotelFacilitiesType = hotelFacilitiesType;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public static class HotelFacilitiesTypeBean {
                private int id;
                private String name;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
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
