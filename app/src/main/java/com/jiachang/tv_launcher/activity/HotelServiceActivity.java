package com.jiachang.tv_launcher.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.utils.ViewUtils;
import com.zhy.autolayout.AutoLinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnFocusChange;

/**
 * @author Mickey.Ma
 * @date 2020-03-28
 * @description
 */
public class HotelServiceActivity extends FragmentActivity {
    @BindView(R.id.setting)
    ImageView setting;
    @BindView(R.id.introduce_hotel)
    AutoLinearLayout introduceHotel;
    @BindView(R.id.introduce_function)
    AutoLinearLayout introduceFunction;
    @BindView(R.id.introduce_weixin)
    AutoLinearLayout introduceWeixin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomMenu();
        setContentView(R.layout.service_activity);

        ButterKnife.bind(this);

        initView();
    }

    private void initView(){
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        // 屏幕宽度（像素）
        int width = metric.widthPixels;
        // 屏幕高度（像素）
        int height = AutoLinearLayout.LayoutParams.WRAP_CONTENT;
        ViewGroup.LayoutParams iH =introduceHotel.getLayoutParams();
        iH.width = width/2;
        iH.height = height;

        ViewGroup.LayoutParams iF =introduceFunction.getLayoutParams();
        iF.width = width/2;
        iF.height = height;

        ViewGroup.LayoutParams iW =introduceWeixin.getLayoutParams();
        iW.width = width/2;
        iW.height = height;
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideBottomMenu();
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

    @OnFocusChange({R.id.intro_hotel,R.id.intro_need,R.id.intro_control,R.id.intro_service,R.id.service_request})
    public void onViewFocusChange(View view, boolean isfocus){
        switch (view.getId()){
            case R.id.intro_hotel:
                introduceHotel.setVisibility(View.VISIBLE);
                introduceWeixin.setVisibility(View.GONE);
                introduceFunction.setVisibility(View.GONE);
                break;
            case R.id.intro_need:
                introduceWeixin.setVisibility(View.VISIBLE);
                introduceHotel.setVisibility(View.GONE);
                introduceFunction.setVisibility(View.GONE);
                break;
            case R.id.intro_control:
                introduceFunction.setVisibility(View.VISIBLE);
                introduceHotel.setVisibility(View.GONE);
                introduceWeixin.setVisibility(View.GONE);
                break;
            case R.id.intro_service:
                introduceHotel.setVisibility(View.VISIBLE);
                introduceWeixin.setVisibility(View.GONE);
                introduceFunction.setVisibility(View.GONE);
                break;
            case R.id.service_request:
                introduceWeixin.setVisibility(View.VISIBLE);
                introduceHotel.setVisibility(View.GONE);
                introduceFunction.setVisibility(View.GONE);
                break;
            default:
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_MENU: //菜单键

                        Intent in = new Intent(HotelServiceActivity.this, SettingActivity.class);
                        startActivity(in);

                break;
            default:
        }

        return super.onKeyDown(keyCode, event);
    }
}
