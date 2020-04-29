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

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.alibaba.fastjson.JSONObject;
import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.fragment.BottomFragment;
import com.jiachang.tv_launcher.fragment.MenuFragment;
import com.jiachang.tv_launcher.fragment.TopbarFragment;
import com.jiachang.tv_launcher.utils.HttpUtils;
import com.jiachang.tv_launcher.utils.IPUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.ButterKnife;

import static com.jiachang.tv_launcher.utils.Constants.MAC;
import static com.jiachang.tv_launcher.utils.Constants.breakfastTime;
import static com.jiachang.tv_launcher.utils.Constants.dinnerTime;
import static com.jiachang.tv_launcher.utils.Constants.hotelIntroduction;
import static com.jiachang.tv_launcher.utils.Constants.hotelName;
import static com.jiachang.tv_launcher.utils.Constants.img;
import static com.jiachang.tv_launcher.utils.Constants.lunchTime;
import static com.jiachang.tv_launcher.utils.Constants.tel;
import static com.jiachang.tv_launcher.utils.Constants.usageMonitoring;
import static com.jiachang.tv_launcher.utils.Constants.userNeeds;
import static com.jiachang.tv_launcher.utils.Constants.wifiName;
import static com.jiachang.tv_launcher.utils.Constants.wifiPassword;

/**
 * @author Mickey.Ma
 * @date 2020-03-25
 * @description
 */
public class MainActivity extends FragmentActivity {

    private String TAG = "MainActivity";
    private Context mContext;
    private MenuFragment mF;
    private String[] permission = {Manifest.permission.CHANGE_CONFIGURATION};
    private static AudioTrack at;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ButterKnife.bind(this);
        hideBottomMenu();
        setContentView(R.layout.main_activity);

        mContext = this;
        //初始化视图
        initView();

        //获取系统声音
        int bufferSizeInBytes = AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        at = new AudioTrack(AudioManager.STREAM_VOICE_CALL, 44100, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSizeInBytes, AudioTrack.MODE_STREAM);
        //设置当前左右声道音量大小
        at.setStereoVolume(0.7f, 0.7f);
        at.play();
        mAudioManager.setMode(AudioManager.MODE_NORMAL);

        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
        Log.d("VIOCE_CALL", "current : " + current);


        //获取酒店介绍相关数据并缓存
        getData();
    }

    private void initView() {
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
                //打开晓听服务和投屏服务
                Intent intent = new Intent();
                ComponentName comp = new ComponentName("com.aispeech.tvui", "com.aispeech.tvui.service.TelenetService");
                intent.setComponent(comp);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startService(intent);
                //打开晓听服务和投屏服务
                Intent inten = new Intent();
                ComponentName com = new ComponentName("com.ionitech.airscreen", "com.ionitech.airscreen.service.MyFirebaseInstanceIDService");
                inten.setComponent(com);
                inten.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startService(inten);
            }
        }.start();
    }

    private void getData() {
        new Thread() {
            @Override
            public void run() {
                String mac = IPUtils.getLocalEthernetMacAddress();
                MAC = mac;
                Log.d("HotelServiceActivity", "MAC" + MAC);
                String url = "http://192.168.0.243:8081/find/hotel/info";
                Map map = new LinkedHashMap();
                map.put("mac", mac);
                try {
                    String req = HttpUtils.mPost(url, map);
                    Log.d(TAG, "req = " + req);
                    if (!req.isEmpty()) {
                        JSONObject json = JSONObject.parseObject(req);
                        String msg = json.getString("msg");
                        if (msg.contains("success")) {
                            Log.d(TAG, "状态:成功");
                            JSONObject dataBean = json.getJSONObject("data");
                            hotelName = dataBean.getString("hotelName");
                            hotelIntroduction = dataBean.getString("hotelIntroduction");
                            usageMonitoring = dataBean.getString("usageMonitoring");
                            userNeeds = dataBean.getString("userNeeds");
                            wifiName = dataBean.getString("wifiName");
                            wifiPassword = dataBean.getString("wifiPassword");
                            breakfastTime = dataBean.getString("breakfastTime");
                            lunchTime = dataBean.getString("lunchTime");
                            dinnerTime = dataBean.getString("dinnerTime");
                            tel = dataBean.getString("phone");
                            img = dataBean.getString("image");
                            if (hotelName != null && !hotelName.isEmpty()) {
                                getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS).edit()
                                        .putString("hotelName", hotelName).putString("hotelIntroduction", hotelIntroduction)
                                        .putString("usageMonitoring", usageMonitoring).putString("userNeeds", userNeeds)
                                        .putString("wifiName", wifiName).putString("wifiPassword", wifiPassword)
                                        .putString("breakfastTime", breakfastTime).putString("lunchTime", lunchTime)
                                        .putString("dinnerTime", dinnerTime).putString("phone", tel)
                                        .putString("image", img)
                                        .apply();
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


    class AudioTrackThread extends Thread {
        @Override
        public void run() {
            byte[] out_bytes = new byte[44100];
            /*InputStream is = getResources().openRawResource(R.raw.start);
            int length ;
            try{
                at.play();
            }catch (IllegalStateException e)
            {
                e.printStackTrace();
            }
            try {
                while((length = is.read(out_bytes))!=-1){
                    //System.out.println(length);
                    at.write(out_bytes, 0, length);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(at.getPlayState()== AudioTrack.PLAYSTATE_PLAYING){
                try{
                    at.stop();
                }catch (IllegalStateException e)
                {
                    e.printStackTrace();
                }
                at.release();
//                am.setMode(AudioManager.MODE_NORMAL);
            }*/
        }
    }
}

