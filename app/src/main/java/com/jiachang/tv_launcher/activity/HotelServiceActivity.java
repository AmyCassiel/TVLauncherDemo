package com.jiachang.tv_launcher.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.adapter.HorizontalListViewAdapter;
import com.jiachang.tv_launcher.adapter.ServiceLeftAdapter;
import com.jiachang.tv_launcher.adapter.ServiceTypeAdapter;
import com.jiachang.tv_launcher.bean.ServiceType;
import com.jiachang.tv_launcher.fragment.hotelservicefragment.HeadFragment;
import com.jiachang.tv_launcher.fragment.hotelservicefragment.HotelIntroControlFragment;
import com.jiachang.tv_launcher.fragment.hotelservicefragment.HotelIntroFragment;
import com.jiachang.tv_launcher.fragment.hotelservicefragment.HotelIntroNeedFragment;
import com.jiachang.tv_launcher.fragment.hotelservicefragment.HotelIntroRequestFragment;
import com.jiachang.tv_launcher.fragment.mainfragment.TopbarFragment;
import com.jiachang.tv_launcher.utils.ImageUtil;
import com.jiachang.tv_launcher.view.HorListView;
import com.jiachang.tv_launcher.view.HorizontalListView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnFocusChange;
import butterknife.Unbinder;

import static android.widget.Toast.*;
import static com.jiachang.tv_launcher.utils.Constants.breakfastTime;
import static com.jiachang.tv_launcher.utils.Constants.business;
import static com.jiachang.tv_launcher.utils.Constants.hotelIntroduction;
import static com.jiachang.tv_launcher.utils.Constants.hotelPolicys;
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
    private String[] sTypeNames, sFsNames0, sFsTimes0;
    private String[] sDetailsNames0,sDetailsNames2,sDetailsNames3;
    private String[] sDetailsImage0,sDetailsImage2,sDetailsImage3;
    private long start2,end2,start5,end5,start6,end6,start7,end7;
    private ServiceLeftAdapter leftAdapter;
    private Context context;
    private String[] mStrings = {"酒店介绍", "酒店设施", "使用客控", "使用客需", "服务反馈"};
    private Fragment currentFragment;
    private HotelIntroFragment hIF = new HotelIntroFragment();
    private HeadFragment hF = new HeadFragment();
    private HotelIntroControlFragment hICF = new HotelIntroControlFragment();
    private HotelIntroNeedFragment hINF = new HotelIntroNeedFragment();
    private HotelIntroRequestFragment hIRF = new HotelIntroRequestFragment();

    private boolean isSelect = false;

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

        initView();


    }

    private void initView() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        FragmentManager fM = getSupportFragmentManager();
        FragmentTransaction fT = fM.beginTransaction();

        if (bundle.getBoolean("about_item_1")) {
            currentFragment = hIF;
            if (hIF.isAdded()) {
                fT.hide(currentFragment).hide(hF).hide(hIRF).hide(hINF).show(hIF);
            } else {
                fT.hide(currentFragment).hide(hF).hide(hIRF).hide(hINF).replace(R.id.head_fragment, hIF).show(hIF);
            }
            fT.commitAllowingStateLoss();
//            listcontrol();
        } else if (bundle.getBoolean("about_item_2")) {
            currentFragment = hINF;
            if (hINF.isAdded()) {
                fT.hide(currentFragment).hide(hF).hide(hIF).hide(hINF).show(hINF);
            } else {
                fT.hide(currentFragment).hide(hF).hide(hIF).hide(hINF).replace(R.id.head_fragment, hINF).show(hINF);
            }
            fT.commitAllowingStateLoss();
//            listcontrol();
        } else if (bundle.getBoolean("about_item_3")) {
            currentFragment = hICF;
            if (hICF.isAdded()) {
                fT.hide(currentFragment).hide(hF).hide(hIRF).hide(hINF).show(hICF);
            } else {
                fT.hide(currentFragment).hide(hF).hide(hIRF).hide(hINF).replace(R.id.head_fragment, hICF).show(hICF);
            }
            fT.commitAllowingStateLoss();
//            listcontrol();
        } else if (bundle.getBoolean("about_item_4")) {
            onBundle();
            currentFragment = hF;
            if (hF.isAdded()) {
                fT.hide(currentFragment).hide(hIF).hide(hIRF).hide(hINF).show(hF);
            } else {
                fT.hide(currentFragment).hide(hIF).hide(hIRF).hide(hINF).replace(R.id.head_fragment, hF).show(hF);
            }
            fT.commitAllowingStateLoss();
//            listcontrol();
        } else if (bundle.getBoolean("about_item_5")) {
            currentFragment = hIRF;
            if (hIRF.isAdded()) {
                fT.hide(currentFragment).hide(hF).hide(hIF).hide(hINF).show(hIRF);
            } else {
                fT.hide(currentFragment).hide(hF).hide(hIF).hide(hINF).replace(R.id.head_fragment, hIRF).show(hIRF);
            }
            fT.commitAllowingStateLoss();
            if (hIRF.isAdded() && !isSelect){
                listcontrol();
            }
        }else if (bundle.getBoolean("main")){
            listcontrol();
        }
    }
    private void getTime() {
        Calendar calendar = Calendar.getInstance();//取得当前时间的星期
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        Log.i("weekday", "" + week);
        long sysTime = System.currentTimeMillis();//获取系统时间
        if (sysTime != 0) {
            CharSequence sysTimeStr = DateFormat.format("HH:mm", sysTime);//时间显示格式
            Log.i("sysTimeStr", "" + sysTimeStr);
            CharSequence sysDayStr = DateFormat.format("yyyy年MM月dd日", sysTime);
            Log.i("sysDayStr", "" + sysDayStr);
            if (!sysTimeStr.equals(0) & !sysDayStr.equals(0)) {
                time1.setText(sysTimeStr);
                day1.setText(sysDayStr);
            }
        } else {
            Toast.makeText(context, "时间为空", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){

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

    private void listcontrol(){
        leftListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemSelected(mStrings[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void itemSelected(String strings) {
        FragmentManager fM = getSupportFragmentManager();
        FragmentTransaction fT = fM.beginTransaction();
        currentFragment = hIF;
        if (strings.equals("酒店介绍")) {
            if (hIF.isAdded()) {
                fT.hide(currentFragment).hide(hF).hide(hIRF).hide(hINF).show(hIF);
            } else {
                fT.hide(currentFragment).hide(hF).hide(hIRF).hide(hINF).replace(R.id.head_fragment, hIF).show(hIF);
            }
            fT.commitAllowingStateLoss();
        } else if (strings.equals("酒店设施")) {
            if (hINF.isAdded()) {
                fT.hide(currentFragment).hide(hF).hide(hIF).hide(hINF).show(hINF);
            } else {
                fT.hide(currentFragment).hide(hF).hide(hIF).hide(hINF).replace(R.id.head_fragment, hINF).show(hINF);
            }
            fT.commitAllowingStateLoss();
        } else if (strings.equals("使用客控")) {
            if (hICF.isAdded()) {
                fT.hide(currentFragment).hide(hF).hide(hIRF).hide(hINF).show(hICF);
            } else {
                fT.hide(currentFragment).hide(hF).hide(hIRF).hide(hINF).replace(R.id.head_fragment, hICF).show(hICF);
            }
            fT.commitAllowingStateLoss();
        } else if (strings.equals("使用客需")) {
            onBundle();
            if (hF.isAdded()) {
                fT.hide(currentFragment).hide(hIF).hide(hIRF).hide(hINF).show(hF);
            } else {
                fT.hide(currentFragment).hide(hIF).hide(hIRF).hide(hINF).replace(R.id.head_fragment, hF).show(hF);
            }
            fT.commitAllowingStateLoss();
        } else if (strings.equals("服务反馈")) {
            if (hIRF.isAdded()) {
                fT.hide(currentFragment).hide(hF).hide(hIF).hide(hINF).show(hIRF);
            } else {
                fT.hide(currentFragment).hide(hF).hide(hIF).hide(hINF).replace(R.id.head_fragment, hIRF).show(hIRF);
            }
            fT.commitAllowingStateLoss();
        }
    }

    private void onBundle(){
        Bundle bund = new Bundle();
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
        hF.setArguments(bund);  //数据传递到fragment中
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
                .getString("wifiName", "");
        wifiPassword = getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS)
                .getString("wifiPassword", "");

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



    private static class OnPopRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
