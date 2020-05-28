package com.jiachang.tv_launcher.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.anim.ActivityAnimationHelper;
import com.jiachang.tv_launcher.utils.HttpUtils;
import com.jiachang.tv_launcher.utils.ViewUtils;
import com.zhy.autolayout.AutoLinearLayout;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

import static com.jiachang.tv_launcher.utils.Constants.MAC;
import static com.jiachang.tv_launcher.utils.Constants.hostUrl;
import static com.jiachang.tv_launcher.utils.Constants.needImage;
import static com.jiachang.tv_launcher.utils.Constants.needName;

/**
 * @author Mickey.Ma
 * @date 2020-04-09
 * @description
 */
public class DiningActivity extends Activity {
    private Context mContext;
    private String TAG = "DiningActivity.class";
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
        float scale = isFocus ? 1.05f : 1.0f;
        ViewCompat.animate(view).scaleX(scale).scaleY(scale).translationZ(1).setInterpolator(new AccelerateInterpolator()).setDuration(200);
        /*if (isFocus){
            Drawable drawable = ContextCompat.getDrawable(mContext,R.drawable.item_background);
            view.setBackground(drawable);
        }else {
            Drawable drawable = ContextCompat.getDrawable(mContext,R.color.transparent);
            view.setBackground(drawable);
        }*/
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomMenu();
        setContentView(R.layout.dining_activity_layout);
        ButterKnife.bind(this);

        mContext = DiningActivity.this;
        initView();
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomMenu() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            View v = this.getWindow().getDecorView();
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


        ViewGroup.LayoutParams dC = diningCake.getLayoutParams();
        dC.width = width / 21 * 5;
        dC.height = height / 21 * 11;

        ViewGroup.LayoutParams dD = diningDrink.getLayoutParams();
        dD.width = width / 21 * 5;
        dD.height = height / 21 * 11;


        ViewGroup.LayoutParams dF = diningFruit.getLayoutParams();
        dF.width = width / 29 * 6;
        dF.height = height / 3;

        ViewGroup.LayoutParams dI = diningIceCream.getLayoutParams();
        dI.width = width / 29 * 6;
        dI.height = height / 3;

        ViewGroup.LayoutParams dW = diningWelcome.getLayoutParams();
        dW.width = width / 2;
        dW.height = height / 3;
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
                    String url2 = hostUrl + "/api/hic/deliveryType/serDetail";
                    Map<String, Object> map = new HashMap<>();
                    map.put("cuid", MAC);
                    map.put("deliveryType", id);
                    String request = HttpUtils.mPost(url2, map);
                    Log.e("","request="+request);
                    JSONObject json = JSONObject.parseObject(request);
                    Log.d("request", request);
                    int code = json.getIntValue("code");
                    if (code == 0) {
                        Log.d(TAG, "请求成功");
                        Intent intent = new Intent();
                        JSONArray array = json.getJSONArray("serviceDetails");
                        intent.putExtra("foodlist",array);
                        intent.setClass(mContext, FoodListActivity.class);
                        startActivity(intent);
                    } else {
                        Log.d(TAG, "请求失败");
                        Looper.prepare();
                        Toast.makeText(DiningActivity.this,"抱歉，酒店暂不提供该服务",Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
