package com.jiachang.tv_launcher.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.adapter.ServiceLeftAdapter;
import com.jiachang.tv_launcher.fragment.hotelservicefragment.NeedFragment;
import com.jiachang.tv_launcher.fragment.hotelservicefragment.ControlFragment;
import com.jiachang.tv_launcher.fragment.hotelservicefragment.IntroHotelFragment;
import com.jiachang.tv_launcher.fragment.hotelservicefragment.FacilityFragment;
import com.jiachang.tv_launcher.fragment.hotelservicefragment.RequestFragment;
import com.jiachang.tv_launcher.utils.HttpUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.jiachang.tv_launcher.utils.Constants.breakfastTime;
import static com.jiachang.tv_launcher.utils.Constants.business;
import static com.jiachang.tv_launcher.utils.Constants.endTime1;
import static com.jiachang.tv_launcher.utils.Constants.endTime3;
import static com.jiachang.tv_launcher.utils.Constants.endTime4;
import static com.jiachang.tv_launcher.utils.Constants.hotelIntroduction;
import static com.jiachang.tv_launcher.utils.Constants.hotelPolicys;
import static com.jiachang.tv_launcher.utils.Constants.startTime1;
import static com.jiachang.tv_launcher.utils.Constants.startTime3;
import static com.jiachang.tv_launcher.utils.Constants.startTime4;
import static com.jiachang.tv_launcher.utils.Constants.usageMonitoring;
import static com.jiachang.tv_launcher.utils.Constants.userNeeds;
import static com.jiachang.tv_launcher.utils.Constants.wifiName;
import static com.jiachang.tv_launcher.utils.Constants.wifiPassword;

/**
 * @author Mickey.Ma
 * @date 2020-03-28
 * @description
 */
public class HotelServiceActivity extends FragmentActivity {
    private static final String TAG = "HotelServiceActivity";

    @BindView(R.id.listview_background)
    LinearLayout lvBackground;
    @BindView(R.id.select_listview)
    ListView leftListView;
    @BindView(R.id.head_fragment)
    FrameLayout layout;
    @BindView(R.id.time1)
    TextView time1;
    @BindView(R.id.day1)
    TextView day1;
    @BindView(R.id.weather_text)
    TextView weatherText;

    //记录滑动的ListView 滑动的位置
    private String[] sFsNames0, sFsTimes0,sFsImgs0,sFsLocals0;
    private String[] sTypeNames;
    private String[] sDetailsNames0,sDetailsNames2,sDetailsNames3;
    private String[] sDetailsImage0,sDetailsImage2,sDetailsImage3;
    private long start2,end2,start5,end5,start6,end6,start7,end7;

    private ServiceLeftAdapter leftAdapter;
    private Context context;
    private String[] mStrings = {"酒店介绍", "酒店设施", "使用客控", "使用客需", "服务反馈"};
    private Fragment currentFragment;
    private IntroHotelFragment hIF = new IntroHotelFragment();
    private NeedFragment hNF = new NeedFragment();
    private ControlFragment hCF = new ControlFragment();
    private FacilityFragment hFF = new FacilityFragment();
    private RequestFragment hRF = new RequestFragment();

    private boolean isSelect = false;
    //记录滑动的ListView 滑动的位置
    private int scrollPosition = -1;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    getTime();
                    break;
                default:
            }
        }
    };

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        /*防止fragment获取上下文报空指令异常；
         *重建时清除已经保存的fragment的状态：在恢复Fragment之前把Bundle里面的fragment状态数据给清除。*/
        if (savedInstanceState != null) {
            savedInstanceState.remove("android:support:fragments");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_activity);

        ButterKnife.bind(this);
        context = this;
        isFirstIn();

        getTime();
        new TimeThread().start();

        leftAdapter = new ServiceLeftAdapter(context, R.layout.service_activity_list_item, mStrings);
        leftListView.setAdapter(leftAdapter);
        leftListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        String weatherTxt =getApplicationContext().getSharedPreferences("hotel", Context.MODE_PRIVATE)
                .getString("weatherTxt", "");
        Log.e(TAG,"weatherTxt:"+weatherTxt);

        if (!weatherTxt.isEmpty()){
            weatherText.setText(weatherTxt);
        }

        initView();
    }

    public void isFirstIn() {
        sTypeNames = getSharedPreference("sTypeNames");
        sDetailsNames0 = getSharedPreference("sDetailsNames1");
        sDetailsNames2 = getSharedPreference("sDetailsNames3");
        sDetailsNames3 = getSharedPreference("sDetailsNames4");

        sDetailsImage0 = getSharedPreference("sDetailsImage1");
        sDetailsImage2 = getSharedPreference("sDetailsImage3");
        sDetailsImage3 = getSharedPreference("sDetailsImage4");
        sFsNames0 = getSharedPreference("sFsNames0");
        sFsTimes0 = getSharedPreference("sFsTimes0");
        sFsImgs0 = getSharedPreference("sFsImgs0");
        sFsLocals0 = getSharedPreference("sFsLocals0");

        startTime1 = getSharedPreference("startTime1");
        endTime1 = getSharedPreference("endTime1");
        startTime3 = getSharedPreference("startTime3");
        endTime3 = getSharedPreference("endTime3");
        startTime4 = getSharedPreference("startTime4");
        endTime4 = getSharedPreference("endTime4");

        start2 = getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS)
                .getLong("start2", 0);
        end2 = getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS)
                .getLong("end2", 0);

        start5 = getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS)
                .getLong("start5", 0);
        end5 = getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS)
                .getLong("end5", 0);
        start6 = getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS)
                .getLong("start6", 0);
        end6 = getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS)
                .getLong("end6", 0);
        start7 = getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS)
                .getLong("start7", 0);
        end7 = getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS)
                .getLong("end7", 0);


        hotelIntroduction = getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS)
                .getString("hotelIntroduction", "");
        breakfastTime = getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS)
                .getString("breakfastTime", "");
        business = getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS)
                .getString("business", "");
        hotelPolicys = getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS)
                .getString("hotelPolicys", "");
        usageMonitoring = getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS)
                .getString("usageMonitoring", "");
        userNeeds = getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS)
                .getString("userNeeds", "");
        wifiName = getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS)
                .getString("wifi", "");

    }

    private void initView() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        FragmentManager fM = getSupportFragmentManager();
        FragmentTransaction fT = fM.beginTransaction();

        if (bundle.getBoolean("about_item_1")) {
            currentFragment = hIF;
            if (hIF.isAdded()) {
                fT.hide(currentFragment).show(hIF);
            } else {
                fT.hide(currentFragment).replace(R.id.head_fragment, hIF).show(hIF);
            }
            fT.commitAllowingStateLoss();
            lvBackground.setVisibility(View.GONE);
        } else if (bundle.getBoolean("about_item_2")) {
            Bundle bundle1 = new Bundle();
            onBundleFac(bundle1);
            bundle1.putString("isFirst","true");
            hFF.setArguments(bundle1);
            currentFragment = hFF;
            if (hFF.isAdded()) {
                fT.hide(currentFragment).show(hFF);
            } else {
                fT.hide(currentFragment).replace(R.id.head_fragment, hFF).show(hFF);
            }
            fT.commitAllowingStateLoss();
            lvBackground.setVisibility(View.GONE);
        } else if (bundle.getBoolean("about_item_3")) {
            Bundle bundl = new Bundle();
            bundl.putString("isFirst","true");
            hFF.setArguments(bundl);
            currentFragment = hCF;
            if (hCF.isAdded()) {
                fT.hide(currentFragment).show(hCF);
            } else {
                fT.hide(currentFragment).replace(R.id.head_fragment, hCF).show(hCF);
            }
            fT.commitAllowingStateLoss();
            lvBackground.setVisibility(View.GONE);
        } else if (bundle.getBoolean("about_item_4")) {
            Bundle bund = new Bundle();
            onBundleNeed(bund);
            bund.putString("isFirst","true");
            hNF.setArguments(bund);
            currentFragment = hNF;
            if (hNF.isAdded()) {
                fT.hide(currentFragment).show(hNF);
            } else {
                fT.hide(currentFragment).replace(R.id.head_fragment, hNF).show(hNF);
            }
            fT.commitAllowingStateLoss();
            lvBackground.setVisibility(View.GONE);
        } else if (bundle.getBoolean("about_item_5")) {
            currentFragment = hRF;
            if (hRF.isAdded()) {
                fT.hide(currentFragment).show(hRF);
            } else {
                fT.hide(currentFragment).replace(R.id.head_fragment, hRF).show(hRF);
            }
            fT.commitAllowingStateLoss();
            lvBackground.setVisibility(View.GONE);
            leftListView.clearFocus();
        }else if (bundle.getBoolean("main")){
            leftListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    itemSelected(mStrings[position]);
                    leftAdapter.setSelectItem(position);
                    /*ListView mRight= hNF.getView().findViewById(R.id.horizon_listview);
                    mRight.setSelection(position);*/
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    public void itemSelected(String strings) {
        FragmentManager fM = getSupportFragmentManager();
        FragmentTransaction fT = fM.beginTransaction();
        currentFragment = hIF;
        if (strings.equals("酒店介绍")) {
            if (hIF.isAdded()) {
                fT.hide(currentFragment).hide(hNF).hide(hRF).hide(hFF).show(hIF);
            } else {
                fT.hide(currentFragment).hide(hNF).hide(hRF).hide(hFF).replace(R.id.head_fragment, hIF).show(hIF);
            }
            fT.commitAllowingStateLoss();
        } else if (strings.equals("酒店设施")) {
            Bundle bundle1 = new Bundle();
            onBundleFac(bundle1);
            hFF.setArguments(bundle1);
            if (hFF.isAdded()) {
                fT.hide(currentFragment).hide(hNF).hide(hIF).hide(hFF).show(hFF);
            } else {
                fT.hide(currentFragment).hide(hNF).hide(hIF).hide(hFF).replace(R.id.head_fragment, hFF).show(hFF);
            }
            fT.commitAllowingStateLoss();
        } else if (strings.equals("使用客控")) {
            if (hCF.isAdded()) {
                fT.hide(currentFragment).hide(hNF).hide(hRF).hide(hFF).show(hCF);
            } else {
                fT.hide(currentFragment).hide(hNF).hide(hRF).hide(hFF).replace(R.id.head_fragment, hCF).show(hCF);
            }
            fT.commitAllowingStateLoss();
        } else if (strings.equals("使用客需")) {
            Bundle bund = new Bundle();
            onBundleNeed(bund);
            hNF.setArguments(bund);
            if (hNF.isAdded()) {
                fT.hide(currentFragment).hide(hIF).hide(hRF).hide(hFF).show(hNF);
            } else {
                fT.hide(currentFragment).hide(hIF).hide(hRF).hide(hFF).replace(R.id.head_fragment, hNF).show(hNF);
            }
            fT.commitAllowingStateLoss();
        } else if (strings.equals("服务反馈")) {
            if (hRF.isAdded()) {
                fT.hide(currentFragment).hide(hNF).hide(hIF).hide(hFF).show(hRF);
            } else {
                fT.hide(currentFragment).hide(hNF).hide(hIF).hide(hFF).replace(R.id.head_fragment, hRF).show(hRF);
            }
            fT.commitAllowingStateLoss();
        }
    }

    private void getTime() {
        Calendar calendar = Calendar.getInstance();//取得当前时间的星期
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        long sysTime = System.currentTimeMillis();//获取系统时间
        if (sysTime != 0) {
            CharSequence sysTimeStr = DateFormat.format("HH:mm", sysTime);//时间显示格式
            CharSequence sysDayStr = DateFormat.format("yyyy年MM月dd日", sysTime);
            if (!sysTimeStr.equals(0) & !sysDayStr.equals(0)) {
                time1.setText(sysTimeStr);
                day1.setText(sysDayStr);
            }
        } else {
            Toast.makeText(context, "时间为空", Toast.LENGTH_LONG).show();
        }
    }

    class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(60000);
                    Message msg = new Message();
                    msg.what = 0;  //消息(一个整型值)
                    mHandler.sendMessage(msg);// 每隔1秒发送一个msg给mHandler
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideBottomMenu();
    }

    private void onBundleFac(Bundle bund){
        bund.putStringArray("sFsNames0",sFsNames0);
        bund.putStringArray("sFsTimes0",sFsTimes0);
        bund.putStringArray("sFsImgs0",sFsImgs0);
        bund.putStringArray("sFsLocals0",sFsLocals0);
    }
    private void onBundleNeed(Bundle bund){
        bund.putStringArray("title", sTypeNames);
        bund.putStringArray("detailsNames0", sDetailsNames0);
        bund.putStringArray("sDetailsImage0", sDetailsImage0);
        bund.putStringArray("detailsNames2", sDetailsNames2);
        bund.putStringArray("sDetailsImage2", sDetailsImage2);
        bund.putStringArray("detailsNames3", sDetailsNames3);
        bund.putStringArray("sDetailsImage3", sDetailsImage3);
        bund.putLong("start2",start2);
        bund.putLong("end2",end2);
        bund.putLong("start5",start5);
        bund.putLong("end5",end5);
        bund.putLong("start6",start6);
        bund.putLong("end6",end6);
        bund.putLong("start7",start7);
        bund.putLong("end7",end7);
        bund.putStringArray("sFsNames0", sFsNames0);
        bund.putStringArray("sFsTimes0", sFsTimes0);
        bund.putStringArray("startTime1",startTime1);
        bund.putStringArray("startTime1",endTime1);
        bund.putStringArray("startTime3",startTime3);
        bund.putStringArray("endTime3",endTime3);
        bund.putStringArray("startTime4",startTime4);
        bund.putStringArray("endTime4",endTime4);

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

    public String[] getSharedPreference(String key) {
        String regularEx = "#";
        SharedPreferences sp = context.getSharedPreferences("hotel", Context.MODE_PRIVATE);
        String values = sp.getString(key, "");
        return values.split(regularEx);
    }

    @Override
    protected void onStart() {
        super.onStart();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
