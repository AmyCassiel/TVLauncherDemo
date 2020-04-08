package com.jiachang.tv_launcher.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.activity.ApowerMirrorActivity;
import com.jiachang.tv_launcher.activity.DiningActivity;
import com.jiachang.tv_launcher.activity.HotelServiceActivity;
import com.jiachang.tv_launcher.utils.ViewUtils;
import com.zhy.autolayout.AutoLinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.Unbinder;

/**
 * @author Mickey.Ma
 * @date 2020-03-25
 * @description
 */
public class MenuFragment extends Fragment{
    private Unbinder mUb;
    private Context mContext;
    private Intent intent;
    private PackageManager pm;

    @BindView(R.id.image_tv)
    ImageView image_tv;
    @BindView(R.id.image_music)
    ImageView image_music;
    @BindView(R.id.image_apower_mirror)
    ImageView image_apower_mirror;
    @BindView(R.id.image_dining)
    ImageView image_dining;
    @BindView(R.id.image_service)
    ImageView image_service;
    @BindView(R.id.lin)
    AutoLinearLayout menuItemLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View menuLayout = inflater.inflate(R.layout.menu_layout, container, false);

        mUb = ButterKnife.bind(this, menuLayout);

        mContext = getActivity();

        pm = mContext.getPackageManager();

        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        // 屏幕宽度（像素）
        int width = metric.widthPixels;
        // 屏幕高度（像素）
        int height = metric.heightPixels/3;
        ViewGroup.LayoutParams layoutParams =menuItemLayout.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;

        return menuLayout;
    }

    @OnClick({R.id.image_tv,R.id.image_music,R.id.image_apower_mirror,R.id.image_dining,R.id.image_service})
    void imageClick( View v) {
        switch (v.getId()){
            case R.id.image_tv:
                intent = pm.getLaunchIntentForPackage("com.dianshijia.newlive");   //这个方法直接返回 访问特定包名下activity或service etc.的入口的intent， 省去设置componentName的参数
                if (intent != null) {
                    mContext.startActivity(intent);
                } else {
                    Toast.makeText(mContext, "你还没有安装“电视家”这个软件哦！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.image_music:
                intent = pm.getLaunchIntentForPackage("com.netease.cloudmusic");
                if (intent != null) {
                    mContext.startActivity(intent);
                } else {
                    intent = pm.getLaunchIntentForPackage("com.tencent.qqmusictv");
                    if (intent != null) {
                        mContext.startActivity(intent);
                    } else {
                        intent = pm.getLaunchIntentForPackage("com.kugou.android");
                        if (intent != null) {
                            mContext.startActivity(intent);
                        } else {
                            intent = pm.getLaunchIntentForPackage("cn.kuwo.player");
                            if (intent != null) {
                                mContext.startActivity(intent);
                            } else {
                                Toast.makeText(mContext, "抱歉，您还没有安装音乐软件！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                break;
            case R.id.image_apower_mirror:
                Intent ent = new Intent();
                ent.setClass(mContext, ApowerMirrorActivity.class);
                mContext.startActivity(ent);
                break;
            case R.id.image_dining:
                Intent intent = new Intent();
                intent.setClass(mContext, DiningActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.image_service:
                Intent in = new Intent();
                in.setClass(mContext, HotelServiceActivity.class);
                mContext.startActivity(in);
                break;
            default:
                break;
        }
    }

    @OnFocusChange({R.id.image_tv,R.id.image_music,R.id.image_apower_mirror,R.id.image_dining,R.id.image_service,R.id.text_tv,
    R.id.text_music,R.id.text_apower_mirror,R.id.text_dinging,R.id.text_service})
    public void onViewFocusChange(View view, boolean isFocus){
        ViewUtils.scaleView(view, isFocus);
    }

}
