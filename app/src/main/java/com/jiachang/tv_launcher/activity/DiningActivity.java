package com.jiachang.tv_launcher.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.anim.ActivityAnimationHelper;
import com.jiachang.tv_launcher.utils.ViewUtils;
import com.zhy.autolayout.AutoLinearLayout;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

/**
 * @author Mickey.Ma
 * @date 2020-04-09
 * @description
 */
public class DiningActivity extends Activity {
    private Context mContext;

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

    @OnFocusChange({R.id.dining_main_all, R.id.dining_cake_all, R.id.dining_drink_all, R.id.dining_fruit_all, R.id.dining_iceCream_all})
    public void onViewFocusChange(View view, boolean isfocus){
        float scale = isfocus ? 1.05f : 1.0f;
        view.animate().scaleX(scale).scaleY(scale).setInterpolator(new AccelerateInterpolator()).setDuration(200);
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
    protected void hideBottomMenu() {
        //隐藏虚拟按键，并且全屏
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

    private void initView(){
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        // 屏幕宽度（像素）
        int width = metric.widthPixels;
        // 屏幕高度（像素）
        int height = metric.heightPixels;
        ViewGroup.LayoutParams dM =diningMain.getLayoutParams();
        dM.width = width/23*10;
        dM.height = height/21*11;


        ViewGroup.LayoutParams dC =diningCake.getLayoutParams();
        dC.width = width/21*5;
        dC.height = height/21*11;

        ViewGroup.LayoutParams dD =diningDrink.getLayoutParams();
        dD.width = width/21*5;
        dD.height = height/21*11;


        ViewGroup.LayoutParams dF =diningFruit.getLayoutParams();
        dF.width = width/29*6;
        dF.height = height/3;

        ViewGroup.LayoutParams dI =diningIceCream.getLayoutParams();
        dI.width = width/29*6;
        dI.height = height/3;

        ViewGroup.LayoutParams dW =diningWelcome.getLayoutParams();
        dW.width = width/2;
        dW.height = height/3;
    }

    @OnClick({R.id.dining_main_all, R.id.dining_cake_all, R.id.dining_drink_all, R.id.dining_fruit_all, R.id.dining_iceCream_all})
    public void onclick(View v){
        Intent intent = new Intent();
        intent.setClass(mContext, FoodListActivity.class);
        ActivityAnimationHelper.startActivity(mContext, intent, v);
    }
}
