package com.jiachang.tv_launcher.fragment;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.activity.ApowerMirrorActivity;
import com.jiachang.tv_launcher.activity.DiningActivity;
import com.jiachang.tv_launcher.activity.HotelServiceActivity;
import com.jiachang.tv_launcher.utils.ViewUtils;
import com.zhy.autolayout.AutoLinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    @BindView(R.id.menu_tv)
    AutoLinearLayout menuTV;
    @BindView(R.id.menu_music)
    AutoLinearLayout menuMusic;
    @BindView(R.id.lin)
    LinearLayout menuItemLayout;

    @BindView(R.id.spinner_multimedia_item)
    LinearLayout multimediaItem;
    @BindView(R.id.spinner_music_item)
    LinearLayout musicItem;
    @BindView(R.id.spinner_apowermirror_item)
    LinearLayout apowermirrorItem;
    @BindView(R.id.spinner_dining_item)
    LinearLayout diningItem;
    @BindView(R.id.spinner_about_item)
    LinearLayout aboutItem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View menuLayout = inflater.inflate(R.layout.main_menu_layout, container, false);

        mUb = ButterKnife.bind(this, menuLayout);

        mContext = getActivity();

        pm = mContext.getPackageManager();

        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        // 屏幕宽度（像素）
        int width = metric.widthPixels;
        // 屏幕高度（像素）
        int height = metric.heightPixels/3;
        Log.e("height",""+height);
        ViewGroup.LayoutParams layoutParams =menuItemLayout.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;

        ViewGroup.MarginLayoutParams layoutParams1 = (ViewGroup.MarginLayoutParams) menuItemLayout.getLayoutParams();
        layoutParams1.bottomMargin = 95;
        menuItemLayout.setLayoutParams(layoutParams1);

        initView();
        return menuLayout;
    }

    public void initView(){
//        menuTV.setFocusable(true);
//        menuTV.setFocusableInTouchMode(true);
        menuTV.setNextFocusDownId(R.id.txtV);
    }

    @OnClick({R.id.multimedia_item_1,R.id.multimedia_item_2,R.id.multimedia_item_3,R.id.music_item_1,R.id.music_item_2,R.id.music_item_3,
            R.id.apowermirror_item_1,R.id.apowermirror_item_2,R.id.apowermirror_item_3,R.id.dining_item_1,R.id.dining_item_2,R.id.dining_item_3,
            R.id.about_item_1,R.id.about_item_2,R.id.about_item_3,R.id.menu_tv,R.id.menu_music,R.id.menu_apower_mirror,R.id.menu_dining,R.id.menu_service})
    void imageClick( View v) {
        switch (v.getId()){
            case R.id.multimedia_item_1:
                intent = pm.getLaunchIntentForPackage("com.qiyivideo.sibichi");
                if (intent != null) {
                    mContext.startActivity(intent);
                } else {
                    Toast.makeText(mContext, "暂时不提供该服务！", Toast.LENGTH_SHORT).show();
                }
            case R.id.multimedia_item_2:
                intent = pm.getLaunchIntentForPackage("com.dianshijia.newlive");   //这个方法直接返回 访问特定包名下activity或service etc.的入口的intent， 省去设置componentName的参数
                if (intent != null) {
                    mContext.startActivity(intent);
                } else {
                    Toast.makeText(mContext, "你还没有安装“电视家”这个软件哦！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.music_item_1:
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
            case R.id.apowermirror_item_1:
                Intent ent = new Intent();
                ent.setClass(mContext, ApowerMirrorActivity.class);
                mContext.startActivity(ent);
                break;
            case R.id.dining_item_1:
                startActivity(new Intent(mContext, DiningActivity.class),
                        ActivityOptions.makeSceneTransitionAnimation((Activity) mContext,
                                v, "image_dining").toBundle());
                break;
            case R.id.about_item_1:
                Intent in = new Intent();
                in.setClass(mContext, HotelServiceActivity.class);
                mContext.startActivity(in);
                break;
            default:
                break;
        }
    }

    @OnFocusChange({R.id.txtV,R.id.menu_tv,R.id.menu_music,R.id.menu_apower_mirror,R.id.menu_dining,R.id.menu_service})
    public void onViewFocusChange(View view, boolean isFocus){
        if(isFocus){
            switch (view.getId()){
                case R.id.txtV:
                    multimediaItem.setVisibility(View.GONE);
                    musicItem.setVisibility(View.GONE);
                    apowermirrorItem.setVisibility(View.GONE);
                    diningItem.setVisibility(View.GONE);
                    aboutItem.setVisibility(View.GONE);
                    break;
                case R.id.menu_tv:
                    multimediaItem.setVisibility(View.VISIBLE);
                    musicItem.setVisibility(View.GONE);
                    apowermirrorItem.setVisibility(View.GONE);
                    diningItem.setVisibility(View.GONE);
                    aboutItem.setVisibility(View.GONE);
                    break;
                case R.id.menu_music:
                    multimediaItem.setVisibility(View.GONE);
                    musicItem.setVisibility(View.VISIBLE);
                    apowermirrorItem.setVisibility(View.GONE);
                    diningItem.setVisibility(View.GONE);
                    aboutItem.setVisibility(View.GONE);
                    break;
                case R.id.menu_apower_mirror:
                    multimediaItem.setVisibility(View.GONE);
                    musicItem.setVisibility(View.GONE);
                    apowermirrorItem.setVisibility(View.VISIBLE);
                    diningItem.setVisibility(View.GONE);
                    aboutItem.setVisibility(View.GONE);
                    break;
                case R.id.menu_dining:
                    multimediaItem.setVisibility(View.GONE);
                    musicItem.setVisibility(View.GONE);
                    apowermirrorItem.setVisibility(View.GONE);
                    diningItem.setVisibility(View.VISIBLE);
                    aboutItem.setVisibility(View.GONE);
                    break;
                case R.id.menu_service:
                    multimediaItem.setVisibility(View.GONE);
                    musicItem.setVisibility(View.GONE);
                    apowermirrorItem.setVisibility(View.GONE);
                    diningItem.setVisibility(View.GONE);
                    aboutItem.setVisibility(View.VISIBLE);
                    break;
                default:
            }
        }
    }

}
