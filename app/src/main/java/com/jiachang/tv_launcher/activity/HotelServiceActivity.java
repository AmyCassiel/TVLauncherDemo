package com.jiachang.tv_launcher.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.bean.PresentCachInfo;
import com.jiachang.tv_launcher.utils.CacheMapManager;
import com.jiachang.tv_launcher.utils.Constants;
import com.jiachang.tv_launcher.utils.HttpUtils;
import com.jiachang.tv_launcher.utils.IPUtils;
import com.jiachang.tv_launcher.utils.ViewUtils;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.LinkedHashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnFocusChange;
import okhttp3.Cache;

import static com.jiachang.tv_launcher.utils.Constants.MAC;
import static com.jiachang.tv_launcher.utils.Constants.breakfastTime;
import static com.jiachang.tv_launcher.utils.Constants.dinnerTime;
import static com.jiachang.tv_launcher.utils.Constants.hotelIntroduction;
import static com.jiachang.tv_launcher.utils.Constants.hotelName;
import static com.jiachang.tv_launcher.utils.Constants.lunchTime;
import static com.jiachang.tv_launcher.utils.Constants.tel;
import static com.jiachang.tv_launcher.utils.Constants.usageMonitoring;
import static com.jiachang.tv_launcher.utils.Constants.userNeeds;
import static com.jiachang.tv_launcher.utils.Constants.wifiName;
import static com.jiachang.tv_launcher.utils.Constants.wifiPassword;
import static com.jiachang.tv_launcher.utils.HttpUtils.netWorkCheck;

/**
 * @author Mickey.Ma
 * @date 2020-03-28
 * @description
 */
public class HotelServiceActivity extends FragmentActivity {
    private static final String TAG = "HotelServiceActivity";
    private Context context;

    @BindView(R.id.introduce_hotel)
    AutoLinearLayout introduceHotel;
    @BindView(R.id.introduce_control)
    AutoLinearLayout introduceControl;
    @BindView(R.id.introduce_need)
    AutoLinearLayout introduceNeed;
    @BindView(R.id.introduce_wifi)
    AutoLinearLayout introduceWifi;
    @BindView(R.id.introduce_request)
    AutoLinearLayout introduceRequest;

    @BindView(R.id.intro_hotel)
    TextView introhotel;
    @BindView(R.id.intro_control)
    TextView introcontrol;
    @BindView(R.id.intro_need)
    TextView introneed;
    @BindView(R.id.intro_wifi)
    TextView introwifi;
    @BindView(R.id.intro_request)
    TextView servicerequest;

    @BindView(R.id.introduce_hotel_txt)
    TextView introHotelTxt;
    @BindView(R.id.introduce_control_txt)
    TextView introControlTxt;
    @BindView(R.id.introduce_need_txt)
    TextView introNeedTxt;
    @BindView(R.id.introce_wifi)
    TextView introceWifi;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomMenu();
        setContentView(R.layout.service_activity);

        ButterKnife.bind(this);
        context = this;

        hotelIntroduction = getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS)
                .getString("hotelIntroduction", "");
        breakfastTime = getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS)
                .getString("breakfastTime", "");
        lunchTime = getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS)
                .getString("lunchTime", "");
        dinnerTime = getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS)
                .getString("dinnerTime", "");
        usageMonitoring = getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS)
                .getString("usageMonitoring", "");
        userNeeds = getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS)
                .getString("userNeeds", "");
        wifiName = getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS)
                .getString("wifiName", "");
        wifiPassword = getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS)
                .getString("wifiPassword", "");
        Log.d(TAG,"hotelIntroduction = "+hotelIntroduction);

        getData();
        initView();
    }

    private void getData() {
        if(netWorkCheck(context)){
            //网络已连接
            if (hotelName != null && !hotelName.isEmpty()){
                introHotelTxt.setText("\u3000\u3000"+hotelIntroduction+"\n\u3000\u3000早餐供应时间：" +breakfastTime
                        +"\n\u3000\u3000午餐供应时间："+lunchTime+"\n\u3000\u3000晚餐供应时间："+dinnerTime+"\n\u3000\u3000前台电话："+tel);
                introControlTxt.setText(usageMonitoring);
                introNeedTxt.setText(userNeeds);
                introceWifi.setText("WIFI："+wifiName+"\n"+"密码："+wifiPassword);
            }
        }else{
            // 网络未连接
            if (hotelName != null && !hotelName.isEmpty()) {
                introHotelTxt.setText("\u3000\u3000" + hotelIntroduction + "\n\u3000\u3000早餐供应时间：" + breakfastTime
                        + "\n\u3000\u3000午餐供应时间：" + lunchTime + "\n\u3000\u3000晚餐供应时间：" + dinnerTime + "\n\u3000\u3000前台电话：" + tel);
                introControlTxt.setText(usageMonitoring);
                introNeedTxt.setText(userNeeds);
                introceWifi.setText("WIFI：" + wifiName + "\n" + "密码：" + wifiPassword);
            }
        }
    }

    /**
     * 初始化视图，设置控件的长宽高
     */
    private void initView() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        // 屏幕宽度（像素）
        int width = metric.widthPixels;
        // 屏幕高度（像素）
        int height = AutoLinearLayout.LayoutParams.WRAP_CONTENT;
        ViewGroup.LayoutParams iH = introduceHotel.getLayoutParams();
        iH.width = width / 2;
        iH.height = height;

        ViewGroup.LayoutParams iF = introduceControl.getLayoutParams();
        iF.width = width / 2;
        iF.height = height;

        ViewGroup.LayoutParams iN = introduceNeed.getLayoutParams();
        iN.width = width / 2;
        iN.height = height;

        ViewGroup.LayoutParams iW = introduceRequest.getLayoutParams();
        iW.width = width / 2;
        iW.height = height;

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle.getBoolean("about_item_1")) {
            introhotel.setFocusable(true);
            introhotel.setBackgroundColor(getResources().getColor(R.color.blue));
            introduceHotel.setVisibility(View.VISIBLE);
            introduceControl.setVisibility(View.GONE);
            introduceNeed.setVisibility(View.GONE);
            introduceWifi.setVisibility(View.GONE);
            introduceRequest.setVisibility(View.GONE);
        } else if (bundle.getBoolean("about_item_2")) {
            introcontrol.setFocusable(true);
            introcontrol.setBackgroundColor(getResources().getColor(R.color.blue));
            introduceHotel.setVisibility(View.GONE);
            introduceControl.setVisibility(View.VISIBLE);
            introduceNeed.setVisibility(View.GONE);
            introduceWifi.setVisibility(View.GONE);
            introduceRequest.setVisibility(View.GONE);
        } else if (bundle.getBoolean("about_item_3")) {
            introneed.setFocusable(true);
            introneed.setBackgroundColor(getResources().getColor(R.color.blue));
            introduceHotel.setVisibility(View.GONE);
            introduceControl.setVisibility(View.GONE);
            introduceNeed.setVisibility(View.VISIBLE);
            introduceWifi.setVisibility(View.GONE);
            introduceRequest.setVisibility(View.GONE);
        } else if (bundle.getBoolean("about_item_4")) {
            introwifi.setFocusable(true);
            introwifi.setBackgroundColor(getResources().getColor(R.color.blue));
            introduceHotel.setVisibility(View.GONE);
            introduceControl.setVisibility(View.GONE);
            introduceNeed.setVisibility(View.GONE);
            introduceWifi.setVisibility(View.VISIBLE);
            introduceRequest.setVisibility(View.GONE);
        } else if (bundle.getBoolean("about_item_5")) {
            servicerequest.setFocusable(true);
            servicerequest.setBackgroundColor(getResources().getColor(R.color.blue));
            introduceHotel.setVisibility(View.GONE);
            introduceControl.setVisibility(View.GONE);
            introduceNeed.setVisibility(View.GONE);
            introduceWifi.setVisibility(View.GONE);
            introduceRequest.setVisibility(View.VISIBLE);
        } else if (bundle.getBoolean("main")) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideBottomMenu();
    }

    protected void hideBottomMenu() {
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

    /**
     * 设置焦点
     */
    @OnFocusChange({R.id.intro_hotel, R.id.intro_need, R.id.intro_control, R.id.intro_wifi, R.id.intro_request})
    public void onViewFocusChange(View view, boolean isfocus) {
        if (isfocus) {
            switch (view.getId()) {
                case R.id.intro_hotel:
                    introduceHotel.setVisibility(View.VISIBLE);
                    introduceControl.setVisibility(View.GONE);
                    introduceNeed.setVisibility(View.GONE);
                    introduceWifi.setVisibility(View.GONE);
                    introduceRequest.setVisibility(View.GONE);
                    break;
                case R.id.intro_control:
                    introduceHotel.setVisibility(View.GONE);
                    introduceControl.setVisibility(View.VISIBLE);
                    introduceNeed.setVisibility(View.GONE);
                    introduceWifi.setVisibility(View.GONE);
                    introduceRequest.setVisibility(View.GONE);
                    break;
                case R.id.intro_need:
                    introduceHotel.setVisibility(View.GONE);
                    introduceControl.setVisibility(View.GONE);
                    introduceNeed.setVisibility(View.VISIBLE);
                    introduceWifi.setVisibility(View.GONE);
                    introduceRequest.setVisibility(View.GONE);
                    break;
                case R.id.intro_wifi:
                    introduceHotel.setVisibility(View.GONE);
                    introduceControl.setVisibility(View.GONE);
                    introduceNeed.setVisibility(View.GONE);
                    introduceWifi.setVisibility(View.VISIBLE);
                    introduceRequest.setVisibility(View.GONE);
                    break;
                case R.id.intro_request:
                    introduceHotel.setVisibility(View.GONE);
                    introduceControl.setVisibility(View.GONE);
                    introduceNeed.setVisibility(View.GONE);
                    introduceWifi.setVisibility(View.GONE);
                    introduceRequest.setVisibility(View.VISIBLE);
                    break;
                default:
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU: //菜单键
                Intent in = new Intent(HotelServiceActivity.this, SettingActivity.class);
                startActivity(in);
                break;
            default:
        }

        return super.onKeyDown(keyCode, event);
    }
}
