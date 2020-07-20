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
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.adapter.ServiceLeftAdapter;
import com.jiachang.tv_launcher.bean.HotelInfoBean;
import com.jiachang.tv_launcher.fragment.hotelservicefragment.NeedFragment;
import com.jiachang.tv_launcher.fragment.hotelservicefragment.IntroHotelFragment;
import com.jiachang.tv_launcher.fragment.hotelservicefragment.FacilityFragment;
import com.jiachang.tv_launcher.fragment.hotelservicefragment.RequestFragment;
import com.jiachang.tv_launcher.utils.Constant;
import com.jiachang.tv_launcher.utils.HttpUtils;
import com.jiachang.tv_launcher.utils.IPUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Mickey.Ma
 * @date 2020-03-28
 * @description 服务主界面
 */
public class HotelServiceActivity extends FragmentActivity {
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

    private Context context;
    private final String[] mStrings = {"酒店介绍", "酒店设施", "使用客需", "服务反馈"};
    private Fragment currentFragment;
    private final IntroHotelFragment hIF = new IntroHotelFragment();
    private final NeedFragment hNF = new NeedFragment();
    private final FacilityFragment hFF = new FacilityFragment();
    private final RequestFragment hRF = new RequestFragment();
    private static final String TAG = "HotelServiceActivity";
    private final Handler mHandler = new Handler() {
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
        getWindow().setBackgroundDrawable(null);
        hideBottomMenu();
        ButterKnife.bind(this);
        context = HotelServiceActivity.this;
        /*最左侧listView*/
        ServiceLeftAdapter leftAdapter = new ServiceLeftAdapter(context, R.layout.service_activity_list_item, mStrings);
        leftListView.setAdapter(leftAdapter);
        leftListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        FragmentManager fM = getSupportFragmentManager();
        FragmentTransaction fT = fM.beginTransaction();

        /*主界面进入服务界面显示的布局*/
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
            bundle1.putString("isFirst", "true");
            hFF.setArguments(bundle1);
            currentFragment = hFF;
            if (hFF.isAdded()) {
                fT.hide(currentFragment).show(hFF);
            } else {
                fT.hide(currentFragment).replace(R.id.head_fragment, hFF).show(hFF);
            }
            fT.commitAllowingStateLoss();
            lvBackground.setVisibility(View.GONE);
        }  else if (bundle.getBoolean("about_item_4")) {
            Bundle bund = new Bundle();
            bund.putString("isFirst", "true");
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
        } else if (bundle.getBoolean("main")) {
            leftListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    itemSelected(mStrings[position]);
//                    leftAdapter.setSelectItem(position);
                    /*ListView mRight= hNF.getView().findViewById(R.id.horizon_listview);
                    mRight.setSelection(position);*/
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    /**
     * 服务界面的最左边item控制右边fragment的显示
     */
    public void itemSelected(String strings) {
        FragmentManager fM = getSupportFragmentManager();
        FragmentTransaction fT = fM.beginTransaction();
        currentFragment = hIF;
        switch (strings) {
            case "酒店介绍":
                if (hIF.isAdded()) {
                    fT.hide(currentFragment).hide(hNF).hide(hRF).hide(hFF).show(hIF);
                } else {
                    fT.hide(currentFragment).hide(hNF).hide(hRF).hide(hFF).replace(R.id.head_fragment, hIF).show(hIF);
                }
                fT.commitAllowingStateLoss();
                break;
            case "酒店设施":
                Bundle bundle1 = new Bundle();
                hFF.setArguments(bundle1);
                if (hFF.isAdded()) {
                    fT.hide(currentFragment).hide(hNF).hide(hIF).hide(hFF).show(hFF);
                } else {
                    fT.hide(currentFragment).hide(hNF).hide(hIF).hide(hFF).replace(R.id.head_fragment, hFF).show(hFF);
                }
                fT.commitAllowingStateLoss();
                break;
            case "使用客需": {
                Bundle bund = new Bundle();
                hNF.setArguments(bund);
                if (hNF.isAdded()) {
                    fT.hide(currentFragment).hide(hIF).hide(hRF).hide(hFF).show(hNF);
                } else {
                    fT.hide(currentFragment).hide(hIF).hide(hRF).hide(hFF).replace(R.id.head_fragment, hNF).show(hNF);
                }
                fT.commitAllowingStateLoss();
                break;
            }
            case "服务反馈":
                if (hRF.isAdded()) {
                    fT.hide(currentFragment).hide(hNF).hide(hIF).hide(hFF).show(hRF);
                } else {
                    fT.hide(currentFragment).hide(hNF).hide(hIF).hide(hFF).replace(R.id.head_fragment, hRF).show(hRF);
                }
                fT.commitAllowingStateLoss();
                break;
            default:
                break;
        }
    }

    /**
     * 获取时间并在头部显示
     */
    private void getTime() {
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

    /**
     * 开启时间线程，刷新时间
     */
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
        new TimeThread().start();
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomMenu() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * 读取缓存
     */
    public String[] getSharedPreference(String key) {
        String regularEx = "#";
        SharedPreferences sp = context.getSharedPreferences("hotel", Context.MODE_PRIVATE);
        String values = sp.getString(key, "");
        return values.split(regularEx);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU://设置键
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
        finish();
    }

}
