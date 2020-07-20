package com.jiachang.tv_launcher.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.utils.HttpUtils;
import com.jiachang.tv_launcher.utils.IPUtils;
import com.jiachang.tv_launcher.utils.LogUtils;
import com.jiachang.tv_launcher.utils.ViewUtils;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

import static com.jiachang.tv_launcher.utils.Constant.hostUrl;

/**
 * @author Mickey.Ma
 * @date 2020-04-09
 * @description 餐品分类主界面
 */
public class DiningActivity extends Activity {
    private Context mContext;
    private final String TAG = "DiningActivity.class";
    private float price;

    @BindView(R.id.dining_main)
    ImageView diningMain;
    @BindView(R.id.dining_cake)
    ImageView diningCake;
    @BindView(R.id.dining_drink)
    ImageView diningDrink;
    @BindView(R.id.dining_fruit)
    ImageView diningFruit;
    @BindView(R.id.dining_iceCream)
    ImageView diningIceCream;
    @BindView(R.id.dining_welcome)
    ImageView diningWelcome;

    @OnFocusChange({R.id.dining_main_all, R.id.dining_cake_all, R.id.dining_drink_all, R.id.dining_fruit_all, R.id.dining_iceCream_all, R.id.dining_welcome_all})
    public void onViewFocusChange(View view, boolean isFocus) {
        ViewUtils.sView(view,isFocus);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomMenu();
        setContentView(R.layout.dining_activity_layout);
        ButterKnife.bind(this);
        mContext = DiningActivity.this;

    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
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
     * 根据屏幕的宽和高设置控件的大小
     */
    private void initView() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        // 屏幕宽度（像素）
        int width = metric.widthPixels;
        // 屏幕高度（像素）
        int height = metric.heightPixels;
        ViewGroup.LayoutParams dM = diningMain.getLayoutParams();
        dM.width = width / 23 * 10;
        dM.height = height / 21 * 11;
        diningMain.setLayoutParams(dM);
        ViewGroup.LayoutParams dC = diningCake.getLayoutParams();
        dC.width = width / 21 * 5;
        dC.height = height / 21 * 11;
        diningCake.setLayoutParams(dC);
        ViewGroup.LayoutParams dD = diningDrink.getLayoutParams();
        dD.width = width / 21 * 5;
        dD.height = height / 21 * 11;
        diningDrink.setLayoutParams(dD);

        ViewGroup.LayoutParams dF = diningFruit.getLayoutParams();
        dF.width = width / 29 * 6;
        dF.height = height / 3;
        diningFruit.setLayoutParams(dF);
        ViewGroup.LayoutParams dI = diningIceCream.getLayoutParams();
        dI.width = width / 29 * 6;
        dI.height = height / 3;
        diningIceCream.setLayoutParams(dI);
        ViewGroup.LayoutParams dW = diningWelcome.getLayoutParams();
        dW.width = width / 2;
        dW.height = height / 3;
        diningWelcome.setLayoutParams(dW);
    }

    @OnClick({R.id.dining_main_all, R.id.dining_cake_all, R.id.dining_drink_all, R.id.dining_fruit_all, R.id.dining_iceCream_all, R.id.dining_welcome_all})
    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.dining_main_all:
                getDiningType(1);
                break;
            case R.id.dining_cake_all:
                getDiningType(2);
                break;
            case R.id.dining_drink_all:
                getDiningType(3);
                break;
            case R.id.dining_fruit_all:
                getDiningType(4);
                break;
            case R.id.dining_iceCream_all:
                getDiningType(5);
                break;
            case R.id.dining_welcome_all:
                getDiningType(6);
                break;
            default:
        }
    }

    public void getDiningType(int id) {
        new Thread() {
            @Override
            public void run() {
                try {
                    String url2 = hostUrl + "/reservation/api/hic/deliveryType/serDetail";
                    Map<String, Object> map = new HashMap<>();
                    map.put("cuid", IPUtils.getLocalEthernetMacAddress());
                    map.put("deliveryType", id);
                    String request = HttpUtils.mPost(url2, map);
                    LogUtils.d("","request="+request);
                    JSONObject json = JSONObject.parseObject(request);
                    LogUtils.d("request", request);
                    int code = json.getIntValue("code");
                    if (code == 0) {
                        LogUtils.w(TAG, "请求成功");
                        Intent intent = new Intent();
                        JSONArray array = json.getJSONArray("serviceDetails");
                        intent.putExtra("foodlist",array);
                        intent.setClass(mContext, DiningFoodListActivity.class);
                        startActivity(intent);
                    } else {
                        Log.d(TAG, "请求失败");
                        Looper.prepare();
                        Toast.makeText(DiningActivity.this,"抱歉，酒店暂不提供该服务",Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
