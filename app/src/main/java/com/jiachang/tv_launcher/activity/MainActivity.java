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
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.fragment.BottomFragment;
import com.jiachang.tv_launcher.fragment.MenuFragment;
import com.jiachang.tv_launcher.fragment.TopbarFragment;
import com.jiachang.tv_launcher.utils.ViewUtils;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.ButterKnife;

import static com.jiachang.tv_launcher.utils.ViewUtils.getScaledSize;

public class MainActivity extends FragmentActivity {

    private String TAG = "key";
    private Context mContext;
    private MenuFragment mF;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ButterKnife.bind(this);
        hideBottomMenu();
        setContentView(R.layout.main_activity);

        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(3, 5,
                1, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(128));
        
        mContext = this;

        initView();
        ViewUtils.tvLauncherLScale(mContext);
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

        new Thread(){
            @Override
            public void run() {
                super.run();
                /*PackageManager packageManager = mContext.getPackageManager();
                Intent intent= packageManager.getLaunchIntentForPackage("com.aispeech.tvui.service.TelenetService");
                startService(intent);*/

                Intent intent = new Intent();
                ComponentName comp = new ComponentName("com.aispeech.tvui", "com.aispeech.tvui.service.TelenetService");
                Log.e("comp",""+comp);
                intent.setComponent(comp);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startService(intent);
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

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_DPAD_DOWN:
                LinearLayout ll0= mF.getView().findViewById(R.id.spinner_multimedia_item);
                LinearLayout ll1= mF.getView().findViewById(R.id.spinner_music_item);
                LinearLayout ll2= mF.getView().findViewById(R.id.spinner_apowermirror_item);
                LinearLayout ll3= mF.getView().findViewById(R.id.spinner_dining_item);
                LinearLayout ll4= mF.getView().findViewById(R.id.spinner_about_item);
                ll0.setVisibility(View.GONE);
                ll1.setVisibility(View.GONE);
                ll2.setVisibility(View.GONE);
                ll3.setVisibility(View.GONE);
                ll4.setVisibility(View.GONE);
                break;
            default:
        }
        return super.onKeyDown(keyCode, event);
    }*/
}

