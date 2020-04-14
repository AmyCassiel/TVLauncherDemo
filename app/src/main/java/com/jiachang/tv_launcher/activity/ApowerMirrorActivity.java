package com.jiachang.tv_launcher.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.fragment.ApowerMirrorAndroidDLNAFragment;
import com.jiachang.tv_launcher.fragment.ApowerMirrorAndroidMiracastFragment;
import com.jiachang.tv_launcher.fragment.ApowerMirrorIOSFragment;
import com.jiachang.tv_launcher.utils.ViewUtils;
import com.zhy.autolayout.AutoLinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import github.chenupt.multiplemodel.viewpager.PagerModelManager;

/**
 * @author Mickey.Ma
 * @date 2020-04-03
 * @description
 */
public class ApowerMirrorActivity extends FragmentActivity {
    @BindView(R.id.apowermirror_ios)
    FrameLayout aMiOS;
    @BindView(R.id.apowermirror_android)
    FrameLayout aMa;
    @BindView(R.id.apower_mirror_android)
    LinearLayout aMA;
    @BindView(R.id.apower_mirror_android_miracast)
    FrameLayout aMAM;
    @BindView(R.id.apower_mirror_android_dlna)
    FrameLayout aMAD;
    @BindView(R.id.ios)
    FrameLayout ios;
    @BindView(R.id.android_miracast)
    FrameLayout aM;
    @BindView(R.id.android_dlna)
    FrameLayout aD;

    private FragmentManager fM;
    private FragmentTransaction fT;
    private ApowerMirrorIOSFragment aIOSfragment;
    private ApowerMirrorAndroidMiracastFragment amamf;
    private ApowerMirrorAndroidDLNAFragment amadf;
    private Fragment currentFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomMenu();
        setContentView(R.layout.apower_mirror_main_activity);

        ButterKnife.bind(this);

        fM = getSupportFragmentManager();
        fT = fM.beginTransaction();
        aIOSfragment=new ApowerMirrorIOSFragment();
        amamf = new ApowerMirrorAndroidMiracastFragment();
        amadf = new ApowerMirrorAndroidDLNAFragment();
        fT.replace(R.id.ios,aIOSfragment);
        fT.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fT.hide(amamf).hide(amadf);
        fT.commitAllowingStateLoss();
        currentFragment = aIOSfragment;
    }

    @OnFocusChange({R.id.apowermirror_ios, R.id.apowermirror_android, R.id.apower_mirror_android_miracast,
            R.id.apower_mirror_android_dlna})
    void onViewFocusChange(View v,boolean isFocus){

        if (isFocus){
            switch (v.getId()){
                case R.id.apowermirror_ios:
                    aMA.setVisibility(View.GONE);
                    fM = getSupportFragmentManager();
                    fT = fM.beginTransaction();
                    //如果之前没有添加过
                    if (!aIOSfragment.isAdded()) {
                        fT.hide(currentFragment).hide(amamf).hide(amadf)
                                .add(R.id.ios, aIOSfragment);
                    } else {
                        fT.hide(currentFragment).hide(amamf).hide(amadf)
                                .show(aIOSfragment);
                    }
                    fT.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fT.commitAllowingStateLoss();
                    break;
                case R.id.apowermirror_android:
                    aMA.setVisibility(View.VISIBLE);
                    break;
                case R.id.apower_mirror_android_miracast:
                    showFragment(amamf);
                    fM = getSupportFragmentManager();
                    fT = fM.beginTransaction();
                    //如果之前没有添加过
                    if (!aIOSfragment.isAdded()) {
                        fT.hide(currentFragment).hide(amadf).hide(amamf)
                                .add(R.id.android_miracast, amamf);
                    } else {
                        fT.hide(currentFragment).hide(amadf).hide(amamf)
                                .show(amamf);
                    }
                    fT.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fT.commitAllowingStateLoss();
                    break;
                case R.id.apower_mirror_android_dlna:
                    showFragment(amadf);
                    fM = getSupportFragmentManager();
                    fT = fM.beginTransaction();
                    //如果之前没有添加过
                    if (!aIOSfragment.isAdded()) {
                        fT.hide(currentFragment).hide(amamf).hide(amadf)
                                .add(R.id.android_dlna, amadf);
                    } else {
                        fT.hide(currentFragment).hide(amadf).hide(amamf)
                                .show(amadf);
                    }
                    fT.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fT.commitAllowingStateLoss();
                    break;
                default:
            }
        }
    }

    private void showFragment(Fragment fg) {
        fM = getSupportFragmentManager();
        fT = fM.beginTransaction();
        //如果之前没有添加过
        if (!fg.isAdded()) {
            fT.hide(currentFragment)
                    .add(R.id.ios, fg);
        } else {
            fT.hide(currentFragment)
                    .show(fg);
        }
        fT.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fT.commitAllowingStateLoss();
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
}
