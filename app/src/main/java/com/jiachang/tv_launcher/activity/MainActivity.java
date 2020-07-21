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
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
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
import com.jiachang.tv_launcher.utils.DialogUtil;
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
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Mickey.Ma
 * @date 2020-03-25
 * @description 主界面
 */
public class MainActivity extends FragmentActivity {
    private final String TAG = "MainActivity";
    public static String mac;
    private String bg,bg1;
    public static int hotelId;
    private Drawable drawable;
    public Context context;
    private static final String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    if (drawable != null){
                        constraintLayout.setBackground(drawable);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @BindView(R.id.main_activity)
    RelativeLayout constraintLayout;

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
        context = MainActivity.this;
        //产品授权
        ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 10001);
        //初始化视图
        initView();

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //获取酒店介绍相关数据并缓存
        getData();
    }

    private void initView() {
        //添加fragment到Activity
        FragmentManager fM = getSupportFragmentManager();
        FragmentTransaction fT = fM.beginTransaction();

        MenuFragment mF = new MenuFragment();
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
        DialogUtil.start(context,"正在加载酒店数据...");
        new Thread() {
            @Override
            public void run() {
                if (CommonUtil.isNetworkConnected(MainActivity.this)) {
                    String url = Constant.hostUrl + "/reservation/api/hic/serHotelInfo/get";
                    mac = IPUtils.getLocalEthernetMacAddress();
                    Constant.MAC = mac;
                    Map map = new LinkedHashMap();
                    map.put("cuid", mac);
                    try {
                        String req = HttpUtils.mPost(url, map);
                        LogUtils.d(TAG + ".202", "req = " + req);
                        if (!req.equals("") && !req.isEmpty()) {
                            DialogUtil.finish();
                            JSONObject json = JSONObject.parseObject(req);
                            ParserConfig.getGlobalInstance().addAccept("HotelInfo");
                            HotelInfoBean hotelInfo = JSONObject.toJavaObject(json, HotelInfoBean.class);
                            if (hotelInfo.getCode() == 0) {
                                Constant.hotelName = hotelInfo.getHotelDb().getHotelName();
                                LogUtils.d(TAG + ".210", "hotelName = " + Constant.hotelName);
                                Constant.hotelIntroduction = hotelInfo.getHotelDb().getHotelIntrodu();
                                Constant.hotelPolicys = hotelInfo.getHotelDb().getHotelPolicys();
                                Constant.business = hotelInfo.getHotelDb().getBusiness();
                                Constant.wifiName = hotelInfo.getHotelDb().getWifi();
                                Constant.wifiPassword = hotelInfo.getHotelDb().getPassword();
                                LogUtils.d(TAG+".216","Constant.wifiPassword = "+Constant.wifiPassword);
                                Constant.tel = hotelInfo.getHotelDb().getTelephone();
                                Constant.img = hotelInfo.getHotelDb().getLogo();
                                String bgurl = hotelInfo.getHotelDb().getAppearancePicUrl();
                                bg1 = bgurl.substring(0, bgurl.indexOf(","));
                                bg = hotelInfo.getHotelDb().getHotelPromote().getMainImage();
                                LogUtils.d(TAG+".216","MainActivityBG = "+bg);
                                if (!bg.isEmpty()){
                                    drawable = ImageUtil.ImageOperations(context,bg,"bg.png");
                                    Message message = new Message();
                                    message.what = 0;
                                    message.obj = drawable;
                                    handler.sendMessage(message);
                                }else {
                                    drawable = ImageUtil.ImageOperations(context,bg1,"bg.png");
                                    Message message = new Message();
                                    message.what = 0;
                                    message.obj = drawable;
                                    handler.sendMessage(message);
                                }
                                Constant.serviceConfs = hotelInfo.getHotelDb().getServiceConfs();

                                Constant.hotelFacilities = hotelInfo.getHotelDb().getHotelFacilities();
                                if (!Constant.hotelFacilities.isEmpty()) {
                                    int fsize = Constant.hotelFacilities.size();
                                    for (int j = 0; j < fsize; j++) {
                                        String tyna = Constant.hotelFacilities.get(j).getHotelFacilitiesType().getName();
                                        if (tyna.equals("早餐") && !tyna.isEmpty()) {
                                            Constant.breakfastTime = Constant.hotelFacilities.get(j).getTime();
                                            Constant.sFacilityLocation = Constant.hotelFacilities.get(j).getLocation();
                                        }
                                    }
                                }
                                if (Constant.hotelName != null && !Constant.hotelName.isEmpty()) {
                                    getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS).edit()
                                            .putString("hotelName", Constant.hotelName).putString("hotelIntroduction", Constant.hotelIntroduction)
                                            .putString("phone", Constant.tel).putString("wifi", Constant.wifiName)
                                            .putString("wifipassword",Constant.wifiPassword).putString("image", Constant.img)
                                            .putString("breakfastTime", Constant.breakfastTime).putString("local", Constant.sFacilityLocation).putString("business", Constant.business)
                                            .apply();
                                }
                            } else {
                                DialogUtil.finish();
                                LogUtils.e(TAG, "获取酒店信息失败");
                                Looper.prepare();
                                Toast.makeText(context, "获取酒店信息失败", Toast.LENGTH_LONG).show();
                                Looper.loop();
                            }
                            if(hotelInfo.getErrno() == 404){
                                DialogUtil.finish();
                                Looper.prepare();
                                Toast.makeText(context, hotelInfo.getErrmsg(), Toast.LENGTH_LONG).show();
                                Looper.loop();
                            }
                        } else {
                            DialogUtil.finish();
                            LogUtils.e(TAG, "请求数据失败，request = " + req);
                            Looper.prepare();
                            Toast.makeText(context, "请求数据失败,请刷新后重试", Toast.LENGTH_LONG).show();
                            Looper.loop();
                        }
                    } catch (Exception e) {
                        DialogUtil.finish();
                        e.printStackTrace();
                    }
                } else {
                    DialogUtil.finish();
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
        StringBuilder str = new StringBuilder();
        SharedPreferences sp = getApplicationContext().getSharedPreferences("hotel", Context.MODE_PRIVATE);
        if (values != null && values.length > 0) {
            for (String value : values) {
                str.append(value);
                str.append(regularEx);
            }
            SharedPreferences.Editor et = sp.edit();
            et.putString(key, str.toString());
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

