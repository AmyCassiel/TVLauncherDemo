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
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.bean.HotelInfoBean;
import com.jiachang.tv_launcher.bean.HotelInfoBean.HotelDbBean;
import com.jiachang.tv_launcher.bean.HotelInfoBean.HotelDbBean.ServiceConfsBean;
import com.jiachang.tv_launcher.bean.HotelInfoBean.HotelDbBean.ServiceConfsBean.ServiceDetailsBean;
import com.jiachang.tv_launcher.fragment.mainfragment.BottomFragment;
import com.jiachang.tv_launcher.fragment.mainfragment.MenuFragment;
import com.jiachang.tv_launcher.fragment.mainfragment.TopbarFragment;
import com.jiachang.tv_launcher.utils.ApiRetrofit;
import com.jiachang.tv_launcher.utils.CommonUtil;
import com.jiachang.tv_launcher.utils.Constant;
import com.jiachang.tv_launcher.utils.HttpUtils;
import com.jiachang.tv_launcher.utils.IPUtils;
import com.jiachang.tv_launcher.utils.ImageUtil;
import com.jiachang.tv_launcher.utils.LogUtils;
import com.jiachang.tv_launcher.utils.PackagesInstaller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author Mickey.Ma
 * @date 2020-03-25
 * @description 主界面
 */
public class MainActivity extends FragmentActivity {
    private String TAG = "MainActivity";
    public static String mac;
    public static int hotelId;
    private Drawable drawable;
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
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    if (drawable != null) {
                        constraintLayout.setBackground(drawable);
                    }
                    break;
                default:
            }
        }
    };

    @BindView(R.id.main_activity)
    ConstraintLayout constraintLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.remove("android:support:fragments");
        }
        super.onCreate(savedInstanceState);
        //设置常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //隐藏虚拟键
        hideBottomMenu();
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        context = getApplicationContext();
        //产品授权
        ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 10001);
        //初始化视图
        initView();

    }

    @Override
    protected void onStart() {
        super.onStart();
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
    }

    /**
     * 获取酒店的详细信息
     */
    public void getData() {
        Constant.MAC = IPUtils.getLocalEthernetMacAddress();
        if (CommonUtil.isNetworkConnected(MainActivity.this)) {
            ApiRetrofit.initRetrofit(Constant.hostUrl)
                    .getHotel(Constant.MAC)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<HotelInfoBean>() {
                        @Override
                        public void call(HotelInfoBean hotelInfo) {
                            int code = hotelInfo.getCode();
                            if (code == 0) {
                                hotelId = hotelInfo.getHotelDb().getId();
                                Constant.hotelId = hotelId;
                                Constant.hotelName = hotelInfo.getHotelDb().getHotelName();
                                LogUtils.d(TAG + ".210", "hotelName = " + Constant.hotelName);
                                Constant.hotelIntroduction = hotelInfo.getHotelDb().getHotelIntrodu();
                                Constant.hotelPolicys = hotelInfo.getHotelDb().getHotelPolicys();
                                Constant.business = hotelInfo.getHotelDb().getBusiness();
                                Constant.wifiName = hotelInfo.getHotelDb().getWifi();
                                Constant.wifiPassword = hotelInfo.getHotelDb().getPassword();
                                Constant.tel = hotelInfo.getHotelDb().getTelephone();
                                Constant.img = hotelInfo.getHotelDb().getLogo();
                                String bg = hotelInfo.getHotelDb().getHotelPromote().getMainImage();
                                LogUtils.d(TAG + ".216", "MainActivityBG = " + bg);
                                if (!bg.isEmpty()) {
                                    drawable = ImageUtil.ImageOperations(context, bg, "bg.png");
                                    Message message = new Message();
                                    message.what = 0;
                                    message.obj = drawable;
                                    handler.sendMessage(message);
                                } else {
                                    String bgurl = hotelInfo.getHotelDb().getAppearancePicUrl();
                                    String bg1 = bgurl.substring(0, bgurl.indexOf(","));
                                    drawable = ImageUtil.ImageOperations(context, bg1, "bg.png");
                                    Message message = new Message();
                                    message.what = 0;
                                    message.obj = drawable;
                                    handler.sendMessage(message);
                                }

                                List<ServiceConfsBean> serviceConfs = hotelInfo.getHotelDb().getServiceConfs();
                                int typeSize = serviceConfs.size();
                                for (int i = 0; i < serviceConfs.size(); i++) {
                                    Constant.sTypeName = serviceConfs.get(i).getServiceType().getServiceTypeName();
                                    Constant.sTypeId = serviceConfs.get(i).getServiceType().getId();
                                    sTypeNames.add(Constant.sTypeName);
                                    List<ServiceDetailsBean> serviceDetails = hotelInfo.getHotelDb().getServiceConfs().get(i).getServiceDetails();
                                    int id = serviceConfs.get(i).getServiceType().getId();
                                    //补充服务
                                    if (id == 1) {
                                        Constant.sTypeName = serviceConfs.get(i).getServiceType().getServiceTypeName();
                                        int size = serviceDetails.size();
                                        Constant.idArrayList1 = new ArrayList<>();
                                        for (int d = 0; d < size; d++) {
                                            int sDetailsId = serviceDetails.get(d).getId();
                                            Constant.sDetailsName = serviceDetails.get(d).getNeedName();
                                            Constant.sDetailsImage = serviceDetails.get(d).getNeedImage();
                                            Constant.idArrayList1.add(sDetailsId);
                                            nameArrayList1.add(Constant.sDetailsName);
                                            imageArrayList1.add(Constant.sDetailsImage);
                                            long supplyStartTime = serviceDetails.get(d).getSupplyStartTime();
                                            long supplyEndTime = serviceDetails.get(d).getSupplyEndTime();
                                            sTypeTime1.add(String.valueOf(supplyStartTime));
                                            eTypeTime1.add(String.valueOf(supplyEndTime));
                                        }
                                        Constant.startTime1 = sTypeTime1.toArray(new String[size]);
                                        Constant.endTime1 = eTypeTime1.toArray(new String[size]);
                                        sDetailsNames1 = nameArrayList1.toArray(new String[size]);
                                        sDetailsImage1 = imageArrayList1.toArray(new String[size]);

                                    }
                                    //客房清洁
                                    if (id == 2) {
                                        Constant.start2 = serviceConfs.get(i).getWaiterStartTime();
                                        Constant.end2 = serviceConfs.get(i).getWaiterEndTime();
                                    }
                                    //送餐服务
                                    if (id == 3) {
                                        Constant.sTypeName = serviceConfs.get(i).getServiceType().getServiceTypeName();
                                        Constant.idArrayList3 = new ArrayList<>();
                                        Constant.priceArrayList3 = new ArrayList<>();
                                        int size = serviceDetails.size();
                                        for (int d = 0; d < size; d++) {
                                            int sDetailsId = serviceDetails.get(d).getId();
                                            Constant.idArrayList3.add(sDetailsId);
                                            Constant.sDetailsName = serviceDetails.get(d).getNeedName();
                                            Constant.sDetailsImage = serviceDetails.get(d).getNeedImage();
                                            Constant.foodPrice = serviceDetails.get(d).getPrice();
                                            nameArrayList3.add(Constant.sDetailsName);
                                            imageArrayList3.add(Constant.sDetailsImage);
                                            Constant.priceArrayList3.add(Constant.foodPrice);
                                            long supplyStartTime = serviceDetails.get(d).getSupplyStartTime();
                                            long supplyEndTime = serviceDetails.get(d).getSupplyEndTime();
                                            sTypeTime3.add(String.valueOf(supplyStartTime));
                                            eTypeTime3.add(String.valueOf(supplyEndTime));
                                        }
                                        Constant.startTime3 = sTypeTime3.toArray(new String[size]);
                                        Constant.endTime3 = eTypeTime3.toArray(new String[size]);
                                        sDetailsNames3 = nameArrayList3.toArray(new String[size]);
                                        sDetailsImage3 = imageArrayList3.toArray(new String[size]);
                                    }
                                    //保障服务
                                    if (id == 4) {
                                        Constant.sTypeName = serviceConfs.get(i).getServiceType().getServiceTypeName();
                                        Constant.idArrayList4 = new ArrayList<>();
                                        int size = serviceDetails.size();
                                        for (int d = 0; d < size; d++) {
                                            int sDetailsId = serviceDetails.get(d).getId();
                                            Constant.sDetailsName = serviceDetails.get(d).getNeedName();
                                            Constant.sDetailsImage = serviceDetails.get(d).getNeedImage();
                                            Constant.idArrayList4.add(sDetailsId);
                                            nameArrayList4.add(Constant.sDetailsName);
                                            imageArrayList4.add(Constant.sDetailsImage);
                                            long supplyStartTime = serviceDetails.get(d).getSupplyStartTime();
                                            long supplyEndTime = serviceDetails.get(d).getSupplyEndTime();
                                            sTypeTime4.add(String.valueOf(supplyStartTime));
                                            eTypeTime4.add(String.valueOf(supplyEndTime));
                                        }
                                        Constant.startTime4 = sTypeTime4.toArray(new String[size]);
                                        Constant.endTime4 = eTypeTime4.toArray(new String[size]);
                                        sDetailsNames4 = nameArrayList4.toArray(new String[size]);
                                        sDetailsImage4 = imageArrayList4.toArray(new String[size]);
                                    }
                                    //退房服务
                                    if (id == 5) {
                                        Constant.start5 = serviceConfs.get(i).getWaiterStartTime();
                                        Constant.end5 = serviceConfs.get(i).getWaiterEndTime();
                                    }
                                    //续住服务
                                    if (id == 6) {
                                        Constant.start6 = serviceConfs.get(i).getWaiterStartTime();
                                        Constant.end6 = serviceConfs.get(i).getWaiterEndTime();
                                    }
                                    //洗衣服务
                                    if (id == 7) {
                                        Constant.start7 = serviceConfs.get(i).getWaiterStartTime();
                                        Constant.end7 = serviceConfs.get(i).getWaiterEndTime();
                                    }
                                }

                                sTypeNames1 = sTypeNames.toArray(new String[typeSize]);
                                List<HotelDbBean.HotelFacilitiesBean> hotelFacilities = hotelInfo.getHotelDb().getHotelFacilities();
                                if (!hotelFacilities.isEmpty()) {
                                    int fsize = hotelFacilities.size();
                                    for (int j = 0; j < fsize; j++) {
                                        Constant.sFacilitiesName = hotelFacilities.get(j).getName();
                                        Constant.sFacilitiesImg = hotelFacilities.get(j).getImage();
                                        Constant.sFacilitiesTime = hotelFacilities.get(j).getTime();
                                        Constant.sFacilitiesLocation = hotelFacilities.get(j).getLocation();
                                        sFsNames.add(Constant.sFacilitiesName);
                                        sFsImgs.add(Constant.sFacilitiesImg);
                                        sFsTimes.add(Constant.sFacilitiesTime);
                                        sFsLocals.add(Constant.sFacilitiesLocation);

                                        String tyna = hotelFacilities.get(j).getHotelFacilitiesType().getName();
                                        if (tyna.equals("早餐") && !tyna.isEmpty()) {
                                            Constant.breakfastTime = hotelFacilities.get(j).getTime();
                                            Constant.sFacilityLocation = hotelFacilities.get(j).getLocation();
                                        }
                                    }
                                    sFsNames0 = sFsNames.toArray(new String[fsize]);
                                    sFsImgs0 = sFsImgs.toArray(new String[fsize]);
                                    sFsTimes0 = sFsTimes.toArray(new String[fsize]);
                                    sFsLocals0 = sFsLocals.toArray(new String[fsize]);
                                }
                                if (Constant.hotelName != null && !Constant.hotelName.isEmpty()) {
                                    getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS).edit()
                                            .putString("mac", Constant.MAC).putInt("hotelId", Constant.hotelId)
                                            .putString("hotelName", Constant.hotelName).putString("hotelIntroduction", Constant.hotelIntroduction)
                                            .putString("phone", Constant.tel).putString("wifi", Constant.wifiName)
                                            .putString("wifipassword", Constant.wifiPassword).putString("image", Constant.img)
                                            .putString("breakfastTime", Constant.breakfastTime).putString("local", Constant.sFacilityLocation)
                                            .putString("hotelPolicys", Constant.hotelPolicys).putString("business", Constant.business)
                                            .putLong("start2", Constant.start2).putLong("end2", Constant.end2)
                                            .putLong("start5", Constant.start5).putLong("end5", Constant.end5)
                                            .putLong("start6", Constant.start6).putLong("end6", Constant.end6)
                                            .putLong("start7", Constant.start7).putLong("end7", Constant.end7)
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
                                    setSharedPreference("startTime1", Constant.startTime1);
                                    setSharedPreference("endTime1", Constant.endTime1);
                                    setSharedPreference("startTime3", Constant.startTime3);
                                    setSharedPreference("endTime3", Constant.endTime3);
                                    setSharedPreference("startTime4", Constant.startTime4);
                                    setSharedPreference("endTime4", Constant.endTime4);
                                }
                            } else if (hotelInfo.getErrno() == 404) {
                                Looper.prepare();
                                Toast.makeText(context, hotelInfo.getErrmsg(), Toast.LENGTH_LONG).show();
                                Looper.loop();
                            } else {
                                LogUtils.e(TAG, "获取酒店信息失败");
                                Looper.prepare();
                                Toast.makeText(context, "获取酒店信息失败", Toast.LENGTH_LONG).show();
                                Looper.loop();
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Log.e("MainActivity","数据解析失败，"+throwable.getMessage());
                            Toast.makeText(context, "请求数据失败,请检查网络" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Toast.makeText(context, "网络异常,请检查网络", Toast.LENGTH_LONG).show();
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length == 0 || PackageManager.PERMISSION_GRANTED != grantResults[0]) {
            Toast.makeText(context, "您拒绝了授权，将无法读取日志!", Toast.LENGTH_LONG).show();
            LogUtils.d(TAG, "授权被拒绝！");
        } else {
            if (hasSDCard()) {
                String decVersionName = CommonUtil.getVersionName(getApplicationContext());
                LogUtils.i(TAG, "decVersionName = " + decVersionName);
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

        String packageName = "com.dianshijia.newlive";
        if (getPackageManager().getLaunchIntentForPackage(packageName) != null) {
            PackagesInstaller.uninstallSlient(packageName);
        }
    }

    public void hideBottomMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}

