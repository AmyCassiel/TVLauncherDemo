package com.jiachang.tv_launcher.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.fragment.apowerfragment.AndroidFragment;
import com.jiachang.tv_launcher.fragment.apowerfragment.IOSFragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnFocusChange;

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
    /*@BindView(R.id.scroll_view)
    ScrollView scrollView;
    @BindView(R.id.ios_img)
    ImageView iosImg;
    @BindView(R.id.apower_mirror_android)
    LinearLayout aMA;
    @BindView(R.id.apower_mirror_android_miracast)
    FrameLayout aMAM;
    @BindView(R.id.apower_mirror_android_dlna)
    FrameLayout aMAD;*/
    @BindView(R.id.ios)
    FrameLayout ios;
    @BindView(R.id.android)
    FrameLayout aD;

    private FragmentManager fM;
    private FragmentTransaction fT;
    private IOSFragment iosf;
    private AndroidFragment af;
    private Fragment currentFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomMenu();
        setContentView(R.layout.apower_mirror_main_activity);

        ButterKnife.bind(this);
        iosf=new IOSFragment();
        af = new AndroidFragment();
    }

    @OnFocusChange({R.id.apowermirror_ios, R.id.apowermirror_android})
    void onViewFocusChange(View v,boolean isFocus){
        if (isFocus){
            fM = getSupportFragmentManager();
            fT = fM.beginTransaction();
            switch (v.getId()){
                case R.id.apowermirror_ios:
                    if (!iosf.isAdded()) {
                        fT.hide(af).add(R.id.ios, iosf).show(iosf);
                    } else {
                        fT.hide(af).replace(R.id.ios,iosf).show(iosf);
                    }
                    fT.commitAllowingStateLoss();
                    break;
                case R.id.apowermirror_android:
                    if (!af.isAdded()) {
                        fT.hide(iosf).add(R.id.ios, af).show(af);
                    } else {
                        fT.hide(iosf).replace(R.id.ios,af).show(af);
                    }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
