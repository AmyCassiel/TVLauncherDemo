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
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

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
import com.jiachang.tv_launcher.utils.CommonUtil;
import com.jiachang.tv_launcher.utils.HttpUtils;
import com.jiachang.tv_launcher.utils.IPUtils;
import com.jiachang.tv_launcher.utils.LogUtils;
import com.jiachang.tv_launcher.utils.PackagesInstaller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.ButterKnife;

import static com.jiachang.tv_launcher.utils.Constant.MAC;
import static com.jiachang.tv_launcher.utils.Constant.breakfastTime;
import static com.jiachang.tv_launcher.utils.Constant.business;
import static com.jiachang.tv_launcher.utils.Constant.end2;
import static com.jiachang.tv_launcher.utils.Constant.end5;
import static com.jiachang.tv_launcher.utils.Constant.end6;
import static com.jiachang.tv_launcher.utils.Constant.end7;
import static com.jiachang.tv_launcher.utils.Constant.endTime1;
import static com.jiachang.tv_launcher.utils.Constant.endTime3;
import static com.jiachang.tv_launcher.utils.Constant.endTime4;
import static com.jiachang.tv_launcher.utils.Constant.hostUrl;
import static com.jiachang.tv_launcher.utils.Constant.hotelIntroduction;
import static com.jiachang.tv_launcher.utils.Constant.hotelName;
import static com.jiachang.tv_launcher.utils.Constant.hotelPolicys;
import static com.jiachang.tv_launcher.utils.Constant.img;
import static com.jiachang.tv_launcher.utils.Constant.sDetailsImage;
import static com.jiachang.tv_launcher.utils.Constant.sDetailsName;
import static com.jiachang.tv_launcher.utils.Constant.sFacilitiesImg;
import static com.jiachang.tv_launcher.utils.Constant.sFacilitiesLocation;
import static com.jiachang.tv_launcher.utils.Constant.sFacilitiesName;
import static com.jiachang.tv_launcher.utils.Constant.sFacilitiesTime;
import static com.jiachang.tv_launcher.utils.Constant.sFacilityLocation;
import static com.jiachang.tv_launcher.utils.Constant.sTypeId;
import static com.jiachang.tv_launcher.utils.Constant.sTypeName;
import static com.jiachang.tv_launcher.utils.Constant.start2;
import static com.jiachang.tv_launcher.utils.Constant.start5;
import static com.jiachang.tv_launcher.utils.Constant.start6;
import static com.jiachang.tv_launcher.utils.Constant.start7;
import static com.jiachang.tv_launcher.utils.Constant.startTime1;
import static com.jiachang.tv_launcher.utils.Constant.startTime3;
import static com.jiachang.tv_launcher.utils.Constant.startTime4;
import static com.jiachang.tv_launcher.utils.Constant.tel;
import static com.jiachang.tv_launcher.utils.Constant.wifi;

/**
 * @author Mickey.Ma
 * @date 2020-03-25
 * @description
 */
public class MainActivity extends FragmentActivity {
    private String TAG = "MainActivity";
    public static String mac;
    public Context context;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};
    private MenuFragment mF;
    private String[] sTypeNames1;
    private String[] sDetailsNames1, sDetailsNames3, sDetailsNames4;
    private String[] sDetailsImage1, sDetailsImage3, sDetailsImage4;
    private String[] sFsNames0, sFsImgs0, sFsTimes0, sFsLocals0;
    private ArrayList<String> sTypeTime1 = new ArrayList<>();
    private ArrayList<String> eTypeTime1 = new ArrayList<>();
    private ArrayList<String> sTypeTime3 = new ArrayList<>();
    private ArrayList<String> eTypeTime3 = new ArrayList<>();
    private ArrayList<String> sTypeTime4 = new ArrayList<>();
    private ArrayList<String> eTypeTime4 = new ArrayList<>();

    private ArrayList<String> sTypeNames = new ArrayList<>();
    private ArrayList<String> sFsNames = new ArrayList<>();
    private ArrayList<String> sFsImgs = new ArrayList<>();
    private ArrayList<String> sFsTimes = new ArrayList<>();
    private ArrayList<String> sFsLocals = new ArrayList<>();

    private ArrayList<String> nameArrayList1 = new ArrayList<>();
    private ArrayList<String> imageArrayList1 = new ArrayList<>();
    private ArrayList<String> nameArrayList3 = new ArrayList<>();
    private ArrayList<String> imageArrayList3 = new ArrayList<>();
    private ArrayList<String> nameArrayList4 = new ArrayList<>();
    private ArrayList<String> imageArrayList4 = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.remove("android:support:fragments");
        }
        super.onCreate(savedInstanceState);
        //设置常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ButterKnife.bind(this);
        //隐藏虚拟键
        hideBottomMenu();
        setContentView(R.layout.main_activity);
        context = getApplicationContext();
        //产品授权
        ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 10001);
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
                ComponentName comp = new ComponentName("com.aispeech.tvui",
                        "com.aispeech.tvui.service.TelenetService");
                intent.setComponent(comp);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startService(intent);
                //打开投屏服务
                Intent inten = new Intent();
                ComponentName com = new ComponentName("com.ionitech.airscreen",
                        "com.ionitech.airscreen.service.MyFirebaseInstanceIDService");
                inten.setComponent(com);
                inten.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startService(inten);
                PackagesInstaller.uninstallSlient();
            }
        }.start();
    }

    /**
     * 获取酒店的详细信息
     */
    public void getData() {
        new Thread() {
            @Override
            public void run() {
                if (CommonUtil.isNetworkConnected(MainActivity.this)) {
                    String url = hostUrl + "/api/hic/serHotelInfo/get";
                    mac = IPUtils.getLocalEthernetMacAddress();
                    MAC = mac;
                    Map map = new LinkedHashMap();
                    map.put("cuid", mac);
                    try {
                        String req = HttpUtils.mPost(url, map);
                        LogUtils.d(TAG + ".202", "req = " + req);
                        if (!req.equals("") && !req.isEmpty()) {
                            JSONObject json = JSONObject.parseObject(req);
                            ParserConfig.getGlobalInstance().addAccept("HotelInfo");
                            HotelInfo hotelInfo = JSONObject.toJavaObject(json, HotelInfo.class);
                            int code = hotelInfo.getCode();
                            if (code == 0) {
                                hotelName = hotelInfo.getHotelDb().getHotelName();
                                LogUtils.d(TAG + ".210", "hotelName = " + hotelName);
                                hotelIntroduction = hotelInfo.getHotelDb().getHotelIntrodu();
                                hotelPolicys = hotelInfo.getHotelDb().getHotelPolicys();
                                business = hotelInfo.getHotelDb().getBusiness();
                                wifi = hotelInfo.getHotelDb().getWifi();
                                tel = hotelInfo.getHotelDb().getTelephone();
                                img = hotelInfo.getHotelDb().getLogo();
                                List<ServiceConfsBean> serviceConfs = hotelInfo.getHotelDb().getServiceConfs();

                                int typeSize = serviceConfs.size();
                                for (int i = 0; i < serviceConfs.size(); i++) {
                                    sTypeName = serviceConfs.get(i).getServiceType().getServiceTypeName();
                                    sTypeId = serviceConfs.get(i).getServiceType().getId();
                                    sTypeNames.add(sTypeName);
                                    List<ServiceDetailsBean> serviceDetails = hotelInfo.getHotelDb().getServiceConfs().get(i).getServiceDetails();

                                    int id = serviceConfs.get(i).getServiceType().getId();
                                    //补充服务
                                    if (id == 1) {
                                        sTypeName = serviceConfs.get(i).getServiceType().getServiceTypeName();
                                        int size = serviceDetails.size();
                                        for (int d = 0; d < size; d++) {
                                            sDetailsName = serviceDetails.get(d).getNeedName();
                                            sDetailsImage = serviceDetails.get(d).getNeedImage();
                                            nameArrayList1.add(sDetailsName);
                                            imageArrayList1.add(sDetailsImage);
                                            long supplyStartTime = serviceDetails.get(d).getSupplyStartTime();
                                            long supplyEndTime = serviceDetails.get(d).getSupplyEndTime();
                                            sTypeTime1.add(String.valueOf(supplyStartTime));
                                            eTypeTime1.add(String.valueOf(supplyEndTime));
                                        }
                                        startTime1 = sTypeTime1.toArray(new String[size]);
                                        endTime1 = eTypeTime1.toArray(new String[size]);
                                        sDetailsNames1 = nameArrayList1.toArray(new String[size]);
                                        sDetailsImage1 = imageArrayList1.toArray(new String[size]);

                                    }
                                    //客房清洁
                                    if (id == 2) {
                                        start2 = serviceConfs.get(i).getWaiterStartTime();
                                        end2 = serviceConfs.get(i).getWaiterEndTime();
                                    }
                                    //送餐服务
                                    if (id == 3) {
                                        sTypeName = serviceConfs.get(i).getServiceType().getServiceTypeName();
                                        int size = serviceDetails.size();
                                        for (int d = 0; d < size; d++) {
                                            sDetailsName = serviceDetails.get(d).getNeedName();
                                            sDetailsImage = serviceDetails.get(d).getNeedImage();
                                            nameArrayList3.add(sDetailsName);
                                            imageArrayList3.add(sDetailsImage);
                                            long supplyStartTime = serviceDetails.get(d).getSupplyStartTime();
                                            long supplyEndTime = serviceDetails.get(d).getSupplyEndTime();

                                            sTypeTime3.add(String.valueOf(supplyStartTime));
                                            eTypeTime3.add(String.valueOf(supplyEndTime));
                                        }
                                        startTime3 = sTypeTime3.toArray(new String[size]);
                                        endTime3 = eTypeTime3.toArray(new String[size]);
                                        sDetailsNames3 = nameArrayList3.toArray(new String[size]);
                                        sDetailsImage3 = imageArrayList3.toArray(new String[size]);

                                    }
                                    //保障服务
                                    if (id == 4) {
                                        sTypeName = serviceConfs.get(i).getServiceType().getServiceTypeName();
                                        int size = serviceDetails.size();
                                        for (int d = 0; d < size; d++) {
                                            sDetailsName = serviceDetails.get(d).getNeedName();
                                            sDetailsImage = serviceDetails.get(d).getNeedImage();
                                            nameArrayList4.add(sDetailsName);
                                            imageArrayList4.add(sDetailsImage);
                                            long supplyStartTime = serviceDetails.get(d).getSupplyStartTime();
                                            long supplyEndTime = serviceDetails.get(d).getSupplyEndTime();
                                            sTypeTime4.add(String.valueOf(supplyStartTime));
                                            eTypeTime4.add(String.valueOf(supplyEndTime));
                                        }
                                        startTime4 = sTypeTime4.toArray(new String[size]);
                                        endTime4 = eTypeTime4.toArray(new String[size]);
                                        sDetailsNames4 = nameArrayList4.toArray(new String[size]);
                                        sDetailsImage4 = imageArrayList4.toArray(new String[size]);
                                    }
                                    //退房服务
                                    if (id == 5) {
                                        start5 = serviceConfs.get(i).getWaiterStartTime();
                                        end5 = serviceConfs.get(i).getWaiterEndTime();
                                    }
                                    //续住服务
                                    if (id == 6) {
                                        start6 = serviceConfs.get(i).getWaiterStartTime();
                                        end6 = serviceConfs.get(i).getWaiterEndTime();
                                    }
                                    //洗衣服务
                                    if (id == 7) {
                                        start7 = serviceConfs.get(i).getWaiterStartTime();
                                        end7 = serviceConfs.get(i).getWaiterEndTime();
                                    }
                                }

                                sTypeNames1 = sTypeNames.toArray(new String[typeSize]);
                                List<HotelDbBean.HotelFacilitiesBean> hotelFacilities = hotelInfo.getHotelDb().getHotelFacilities();
                                if (!hotelFacilities.isEmpty()) {
                                    int fsize = hotelFacilities.size();
                                    for (int j = 0; j < fsize; j++) {
                                        sFacilitiesName = hotelFacilities.get(j).getName();
                                        sFacilitiesImg = hotelFacilities.get(j).getImage();
                                        sFacilitiesTime = hotelFacilities.get(j).getTime();
                                        sFacilitiesLocation = hotelFacilities.get(j).getLocation();
                                        sFsNames.add(sFacilitiesName);
                                        sFsImgs.add(sFacilitiesImg);
                                        sFsTimes.add(sFacilitiesTime);
                                        sFsLocals.add(sFacilitiesLocation);

                                        String tyna = hotelFacilities.get(j).getHotelFacilitiesType().getName();
                                        if (tyna.equals("早餐") && !tyna.isEmpty()) {
                                            breakfastTime = hotelFacilities.get(j).getTime();
                                            sFacilityLocation = hotelFacilities.get(j).getLocation();
                                        }
                                    }
                                    sFsNames0 = sFsNames.toArray(new String[fsize]);
                                    sFsImgs0 = sFsImgs.toArray(new String[fsize]);
                                    sFsTimes0 = sFsTimes.toArray(new String[fsize]);
                                    sFsLocals0 = sFsLocals.toArray(new String[fsize]);
                                }
                                if (hotelName != null && !hotelName.isEmpty()) {
                                    getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS).edit()
                                            .putString("hotelName", hotelName).putString("hotelIntroduction", hotelIntroduction)
                                            .putString("phone", tel).putString("wifi", wifi).putString("image", img)
                                            .putString("breakfastTime", breakfastTime).putString("local", sFacilityLocation)
                                            .putString("hotelPolicys", hotelPolicys).putString("business", business)
                                            .putLong("start2", start2).putLong("end2", end2)
                                            .putLong("start5", start5).putLong("end5", end5)
                                            .putLong("start6", start6).putLong("end6", end6)
                                            .putLong("start7", start7).putLong("end7", end7)
                                            .apply();

                                    setSharedPreference("sTypeNames", sTypeNames1);
                                    setSharedPreference("sDetailsNames1", sDetailsNames1);
                                    setSharedPreference("sDetailsNames3", sDetailsNames3);
                                    setSharedPreference("sDetailsNames4", sDetailsNames4);
                                    setSharedPreference("sDetailsImage1", sDetailsImage1);
                                    setSharedPreference("sDetailsImage3", sDetailsImage3);
                                    setSharedPreference("sDetailsImage4", sDetailsImage4);
                                    setSharedPreference("sFsNames0", sFsNames0);
                                    setSharedPreference("sFsTimes0", sFsTimes0);
                                    setSharedPreference("sFsImgs0", sFsImgs0);
                                    setSharedPreference("sFsLocals0", sFsLocals0);
                                    setSharedPreference("startTime1", startTime1);
                                    setSharedPreference("endTime1", endTime1);
                                    setSharedPreference("startTime3", startTime3);
                                    setSharedPreference("endTime3", endTime3);
                                    setSharedPreference("startTime4", startTime4);
                                    setSharedPreference("endTime4", endTime4);
                                }
                            } else {
                                LogUtils.e(TAG, "获取酒店信息失败");
                                Looper.prepare();
                                Toast.makeText(context, "获取酒店信息失败", Toast.LENGTH_LONG).show();
                                Looper.loop();
                            }
                        } else {
                            LogUtils.e(TAG, "请求数据失败，request = " + req);
                            Looper.prepare();
                            Toast.makeText(context, "请求数据失败,请检查网络", Toast.LENGTH_LONG).show();
                            Looper.loop();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Looper.prepare();
                    Toast.makeText(context, "网络异常,请检查网络", Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }
        }.start();
    }

    private boolean hasSDCard() {
        // 获取外部存储的状态
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // 有SD卡
            LogUtils.d(TAG, "SD卡存在");
            return true;
        } else {
            LogUtils.d(TAG, "SD卡不存在");
            return false;
        }
    }

    //获取设备的具体信息
    private void getDeviceInfo() {
        String deviceId = CommonUtil.getDeviceId(getApplicationContext());
        LogUtils.i(TAG, "deviceID = " + deviceId);
        String decVersionName = CommonUtil.getVersionName(getApplicationContext());
        LogUtils.i(TAG, "decVersionName = " + decVersionName);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length == 0 || PackageManager.PERMISSION_GRANTED != grantResults[0]) {
            Toast.makeText(context, "您拒绝了授权，将无法读取日志!", Toast.LENGTH_LONG).show();
            LogUtils.d(TAG, "授权被拒绝！");
        } else {
            if (hasSDCard()) {
                getDeviceInfo();
            }
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

