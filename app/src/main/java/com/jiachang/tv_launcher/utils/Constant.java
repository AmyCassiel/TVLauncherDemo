package com.jiachang.tv_launcher.utils;

import android.os.Environment;

import com.jiachang.tv_launcher.bean.HotelInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mickey.Ma
 * @date 2020-04-21
 * @description  软件总体所需的参数
 */
public class Constant {
    public static final String hostUrl = "https://jczh.jiachang8.com";
    public static String localhostUrl = "http://192.168.0.243:8383";
    public static String MAC = "";
    public static final String API_KEY = "l7in2sysjxkwjolf";
    public static String hotelName = "";
    public static String hotelIntroduction = "";
    public static String usageMonitoring = "";
    public static String userNeeds = "";
    public static String wifiName = "";
    public static String wifiPassword = "";
    public static String breakfastTime = "";
    public static String sFacilityLocation = "";
    public static String tel = "";
    public static String img = "";
    public static String hotelPolicys = "";
    public static String business = "";
    public static String roomNum="";
    public static int hotelId = 0;

    public static String sTypeName = "";
    public static int sTypeId = 0;
    public static String sDetailsName = "";
    public static String sDetailsImage ="";
    public static float foodPrice = 0;
    public static String sFacilitiesName = "";
    public static String sFacilitiesImg = "";
    public static String sFacilitiesTime = "";
    public static String sFacilitiesLocation = "";
    public static long start2 = 0;
    public static long end2 = 0;
    public static long start5 = 0;
    public static long end5 = 0;
    public static long start6 = 0;
    public static long end6 = 0;
    public static long start7 = 0;
    public static long end7 = 0;
    public static List<HotelInfoBean.HotelDbBean.ServiceConfsBean> serviceConfs = null;
    public static List<HotelInfoBean.HotelDbBean.HotelFacilitiesBean> hotelFacilities = null;

    public static final String CASHUPLOAD = hostUrl+"/reservation/notify/logUpload";

    public static final String LOG_PATH_SDCARD_DIR = Environment.getExternalStorageDirectory().getPath() + "/tv_launcher/log";
    public static final String LOG_FILE_Name = "log.txt";
    public static final String LOG_FILE_PATH = LOG_PATH_SDCARD_DIR +"/"+LOG_FILE_Name;
}
