/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.jiachang.tv_launcher.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.bean.HotelInfo;
import com.jiachang.tv_launcher.bean.HotelInfo.HotelDbBean;
import com.jiachang.tv_launcher.bean.HotelInfo.HotelDbBean.ServiceConfsBean;
import com.jiachang.tv_launcher.bean.HotelInfo.HotelDbBean.ServiceConfsBean.ServiceDetailsBean;
import com.jiachang.tv_launcher.fragment.mainfragment.BottomFragment;
import com.jiachang.tv_launcher.fragment.mainfragment.MenuFragment;
import com.jiachang.tv_launcher.fragment.mainfragment.TopbarFragment;
import com.jiachang.tv_launcher.utils.HttpUtils;
import com.jiachang.tv_launcher.utils.IPUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.ButterKnife;

import static com.jiachang.tv_launcher.utils.Constants.MAC;
import static com.jiachang.tv_launcher.utils.Constants.breakfastTime;
import static com.jiachang.tv_launcher.utils.Constants.business;
import static com.jiachang.tv_launcher.utils.Constants.end2;
import static com.jiachang.tv_launcher.utils.Constants.end5;
import static com.jiachang.tv_launcher.utils.Constants.end6;
import static com.jiachang.tv_launcher.utils.Constants.end7;
import static com.jiachang.tv_launcher.utils.Constants.hostUrl;
import static com.jiachang.tv_launcher.utils.Constants.hotelIntroduction;
import static com.jiachang.tv_launcher.utils.Constants.hotelName;
import static com.jiachang.tv_launcher.utils.Constants.hotelPolicys;
import static com.jiachang.tv_launcher.utils.Constants.img;
import static com.jiachang.tv_launcher.utils.Constants.localhostUrl;
import static com.jiachang.tv_launcher.utils.Constants.sDetailsImage;
import static com.jiachang.tv_launcher.utils.Constants.sDetailsName;
import static com.jiachang.tv_launcher.utils.Constants.sFacilitiesName;
import static com.jiachang.tv_launcher.utils.Constants.sFacilitiesTime;
import static com.jiachang.tv_launcher.utils.Constants.sTypeId;
import static com.jiachang.tv_launcher.utils.Constants.sTypeName;
import static com.jiachang.tv_launcher.utils.Constants.start2;
import static com.jiachang.tv_launcher.utils.Constants.start5;
import static com.jiachang.tv_launcher.utils.Constants.start6;
import static com.jiachang.tv_launcher.utils.Constants.start7;
import static com.jiachang.tv_launcher.utils.Constants.tel;
import static com.jiachang.tv_launcher.utils.Constants.wifi;

/**
 * @author Mickey.Ma
 * @date 2020-03-25
 * @description
 */
public class MainActivity extends FragmentActivity {

    private String TAG = "MainActivity";
    private Context mContext;
    private MenuFragment mF;
    private String[] sTypeNames1;
    private String[] sDetailsNames1,sDetailsNames3,sDetailsNames4;
    private String[] sDetailsImage1,sDetailsImage3,sDetailsImage4;
    private String[] sFsNames0;
    private String[] sFsTimes0;

    private ArrayList<String> sTypeNames = new ArrayList<>();
    private ArrayList<String> sFsNames = new ArrayList<>();
    private ArrayList<String> sFsTimes = new ArrayList<>();

    private ArrayList<String> nameArrayList1 = new ArrayList<>();
    private ArrayList<String> imageArrayList1 = new ArrayList<>();
    private ArrayList<String> nameArrayList3 = new ArrayList<>();
    private ArrayList<String> imageArrayList3 = new ArrayList<>();
    private ArrayList<String> nameArrayList4 = new ArrayList<>();
    private ArrayList<String> imageArrayList4 = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null){
            savedInstanceState.remove("android:support:fragments");
        }
        super.onCreate(savedInstanceState);
        //设置常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ButterKnife.bind(this);
        //隐藏虚拟键
        hideBottomMenu();
        setContentView(R.layout.main_activity);

        mContext = this;
        //初始化视图
        initView();
        //获取酒店介绍相关数据并缓存
        getData();
    }

    private void initView() {
        //添加fragment到Activity
        FragmentManager fM = getSupportFragmentManager();
        FragmentTransaction fT = fM.beginTransaction();

        mF = new MenuFragment();
        TopbarFragment tF = new TopbarFragment();
        BottomFragment bF = new BottomFragment();

        fT.add(R.id.menu, mF);
        fT.add(R.id.top, tF);
        fT.add(R.id.bottom, bF);
        fT.commit();

        new Thread() {
            @Override
            public void run() {
                super.run();
                //打开晓听服务
                Intent intent = new Intent();
                ComponentName comp = new ComponentName("com.aispeech.tvui", "com.aispeech.tvui.service.TelenetService");
                intent.setComponent(comp);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startService(intent);
                //打开投屏服务
                Intent inten = new Intent();
                ComponentName com = new ComponentName("com.ionitech.airscreen", "com.ionitech.airscreen.service.MyFirebaseInstanceIDService");
                inten.setComponent(com);
                inten.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startService(inten);
            }
        }.start();
    }

    public void getData() {
        new Thread() {
            @Override
            public void run() {
                String mac = IPUtils.getLocalEthernetMacAddress();
                MAC = mac;
                String url = hostUrl+"/api/hic/serHotelInfo/get";
                Map map = new LinkedHashMap();
                map.put("cuid", mac);
                try {
                    String req = HttpUtils.mPost(url, map);
                    Log.d(TAG, "req = " + req);
                    if (!req.equals("")) {
                        JSONObject json = JSONObject.parseObject(req);
                        ParserConfig.getGlobalInstance().addAccept("HotelInfo");
                        HotelInfo hotelInfo=JSONObject.toJavaObject(json, HotelInfo.class);
                        int code = hotelInfo.getCode();
                        if (code == 0) {
                            hotelName = hotelInfo.getHotelDb().getHotelName();
                            hotelIntroduction = hotelInfo.getHotelDb().getHotelIntrodu();
                            hotelPolicys = hotelInfo.getHotelDb().getHotelPolicys();
                            business = hotelInfo.getHotelDb().getBusiness();
                            wifi = hotelInfo.getHotelDb().getWifi();
                            Log.e(TAG,"wifi = "+wifi);
//                            breakfastTime = hotelInfo.getHotelDb().getHotelFacilities().get(0).getTime();
                            tel = hotelInfo.getHotelDb().getTelephone();
                            String appearancePicUrl = hotelInfo.getHotelDb().getAppearancePicUrl();
                            img = appearancePicUrl.substring(0, appearancePicUrl.indexOf(","));
                            List<ServiceConfsBean> serviceConfs = hotelInfo.getHotelDb().getServiceConfs();
                            List<HotelDbBean.HotelFacilitiesBean> hotelFacilities = hotelInfo.getHotelDb().getHotelFacilities();

                            int typeSize = serviceConfs.size();
                            for (int i=0; i<serviceConfs.size();i++){
                                sTypeName = serviceConfs.get(i).getServiceType().getServiceTypeName();
                                sTypeId = serviceConfs.get(i).getServiceType().getId();
                                sTypeNames.add(sTypeName);
                                List<ServiceDetailsBean> serviceDetails = hotelInfo.getHotelDb().getServiceConfs().get(i).getServiceDetails();

                                int id = serviceConfs.get(i).getServiceType().getId();
                                //补充服务
                                if (id == 1){
                                    sTypeName = serviceConfs.get(i).getServiceType().getServiceTypeName();
                                    int size = serviceDetails.size();
                                    for (int d=0; d<size;d++){
                                        sDetailsName = serviceDetails.get(d).getNeedName();
                                        sDetailsImage = serviceDetails.get(d).getNeedImage();
                                        nameArrayList1.add(sDetailsName);
                                        imageArrayList1.add(sDetailsImage);
                                        int sDetailsId  = serviceDetails.get(d).getId();
//                                        if (sDetailsId == 16 ){
                                            long supplyStartTime = serviceDetails.get(d).getSupplyStartTime();
                                            long supplyEndTime = serviceDetails.get(d).getSupplyEndTime();
//                                        }
                                    }
                                    sDetailsNames1 = nameArrayList1.toArray(new String[size]);
                                    sDetailsImage1 = imageArrayList1.toArray(new String[size]);

                                }
                                //客房清洁
                                if (id == 2){
                                     start2 = serviceConfs.get(i).getWaiterStartTime();
                                     end2 = serviceConfs.get(i).getWaiterEndTime();
                                }
                                //送餐服务
                                if (id == 3){
                                    sTypeName = serviceConfs.get(i).getServiceType().getServiceTypeName();
                                    int size = serviceDetails.size();
                                    System.out.println(size);
                                    for (int d=0; d<size;d++){
                                        sDetailsName = serviceDetails.get(d).getNeedName();
                                        sDetailsImage = serviceDetails.get(d).getNeedImage();
                                        nameArrayList3.add(sDetailsName);
                                        imageArrayList3.add(sDetailsImage);
                                        int sDetailsId  = serviceDetails.get(d).getId();
//                                        if (sDetailsId == 16 ){
                                        long supplyStartTime = serviceDetails.get(d).getSupplyStartTime();
                                        long supplyEndTime = serviceDetails.get(d).getSupplyEndTime();
//                                        }
                                    }
                                    sDetailsNames3 = nameArrayList3.toArray(new String[size]);
                                    sDetailsImage3 = imageArrayList3.toArray(new String[size]);

                                }
                                //保障服务
                                if (id == 4){
                                    sTypeName = serviceConfs.get(i).getServiceType().getServiceTypeName();
                                    int size = serviceDetails.size();
                                    for (int d=0; d<size;d++){
                                        sDetailsName = serviceDetails.get(d).getNeedName();
                                        sDetailsImage = serviceDetails.get(d).getNeedImage();
                                        nameArrayList4.add(sDetailsName);
                                        imageArrayList4.add(sDetailsImage);
                                        int sDetailsId  = serviceDetails.get(d).getId();
//                                        if (sDetailsId == 16 ){
                                        long supplyStartTime = serviceDetails.get(d).getSupplyStartTime();
                                        long supplyEndTime = serviceDetails.get(d).getSupplyEndTime();
//                                        }
                                    }
                                    sDetailsNames4 = nameArrayList4.toArray(new String[size]);
                                    sDetailsImage4 = imageArrayList4.toArray(new String[size]);
                                }
                                //退房服务
                                if (id == 5){
                                     start5 = serviceConfs.get(i).getWaiterStartTime();
                                     end5 = serviceConfs.get(i).getWaiterEndTime();
                                }
                                //续住服务
                                if (id == 6){
                                    start6 = serviceConfs.get(i).getWaiterStartTime();
                                    end6 = serviceConfs.get(i).getWaiterEndTime();
                                }
                                //洗衣服务
                                if (id == 7){
                                     start7 = serviceConfs.get(i).getWaiterStartTime();
                                     end7 = serviceConfs.get(i).getWaiterEndTime();
                                }
                            }

                            sTypeNames1 = sTypeNames.toArray(new String[typeSize]);
                            int fsize = hotelFacilities.size();
                            for (int j=0; j<fsize;j++){
                                sFacilitiesName = hotelFacilities.get(j).getName();
                                sFacilitiesTime = hotelFacilities.get(j).getTime();
                                sFsNames.add(sFacilitiesName);
                                sFsTimes.add(sFacilitiesTime);
                            }
                            sFsNames0 = sFsNames.toArray(new String[fsize]);
                            sFsTimes0 = sFsTimes.toArray(new String[fsize]);
                            if (hotelName != null && !hotelName.isEmpty()) {
                                getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS).edit()
                                        .putString("hotelName", hotelName).putString("hotelIntroduction", hotelIntroduction)
                                        .putString("phone", tel).putString("image", img)
                                        .putString("breakfastTime",breakfastTime)
                                        .putString("hotelFacilities",hotelFacilities.toString())
                                        .putString("hotelPolicys",hotelPolicys).putString("business",business)
                                        .putLong("start2",start2).putLong("end2",end2)
                                        .putLong("start5",start5).putLong("end5",end5)
                                        .putLong("start6",start6).putLong("end6",end6)
                                        .putLong("start7",start7).putLong("end7",end7)
                                        .apply();

                                setSharedPreference("sTypeNames",sTypeNames1);
                                setSharedPreference("sDetailsNames1",sDetailsNames1);
                                setSharedPreference("sDetailsNames3",sDetailsNames3);
                                setSharedPreference("sDetailsNames4",sDetailsNames4);
                                setSharedPreference("sDetailsImage1",sDetailsImage1);
                                setSharedPreference("sDetailsImage3",sDetailsImage3);
                                setSharedPreference("sDetailsImage4",sDetailsImage4);
                                setSharedPreference("sFsNames0",sFsNames0);
                                setSharedPreference("sFsTimes0",sFsTimes0);
                            }
                        }
                    } else {
                        return;

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }
    public void setSharedPreference1(String key, List<ServiceConfsBean> values) {
        String regularEx = "#";
        String str = "";
        SharedPreferences sp = getApplicationContext().getSharedPreferences("hotel", Context.MODE_PRIVATE);
        if (values != null && values.size() > 0) {
            for (Object value : values) {
                str += value;
                str += regularEx;
            }
            SharedPreferences.Editor et = sp.edit();
            et.putString(key, str);
            et.apply();
        }
    }

    public void setSharedPreference(String key, String[] values) {
        String regularEx = "#";
        String str = "";
        SharedPreferences sp = getApplicationContext().getSharedPreferences("hotel", Context.MODE_PRIVATE);
        if (values != null && values.length > 0) {
            for (String value : values) {
                str += value;
                str += regularEx;
            }
            SharedPreferences.Editor et = sp.edit();
            et.putString(key, str);
            et.apply();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        hideBottomMenu();
    }

    public void hideBottomMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            View v = getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }

    }

}

