package com.jiachang.tv_launcher.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;

import com.jiachang.tv_launcher.R;
import com.zhy.autolayout.AutoLinearLayout;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Mickey.Ma
 * @date 2020-04-03
 * @description
 */
public class ApowerMirrorActivity extends Activity {
    @BindView(R.id.intro_apower_mirror)
    AutoLinearLayout introduceApowerMirror;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_apower_mirror);

        ButterKnife.bind(this);

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        // 屏幕宽度（像素）
        int width = metric.widthPixels;
        // 屏幕高度（像素）
        int height = metric.heightPixels;
        ViewGroup.LayoutParams iA =introduceApowerMirror.getLayoutParams();
        iA.width = width/2;
        iA.height = ViewGroup.LayoutParams.MATCH_PARENT;

    }
}
