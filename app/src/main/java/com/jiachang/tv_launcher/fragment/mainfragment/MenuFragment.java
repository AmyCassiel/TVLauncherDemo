package com.jiachang.tv_launcher.fragment.mainfragment;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.activity.ApowerMirrorActivity;
import com.jiachang.tv_launcher.activity.ControlActivity;
import com.jiachang.tv_launcher.activity.DiningActivity;
import com.jiachang.tv_launcher.activity.HotelServiceActivity;
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
        int height = metric.heightPixels/4;
        Log.d("MenuFragment","height:"+height);
        ViewGroup.LayoutParams layoutParams =menuItemLayout.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;

        ViewGroup.MarginLayoutParams layoutParams1 = (ViewGroup.MarginLayoutParams) menuItemLayout.getLayoutParams();
        layoutParams1.bottomMargin = 100;
        menuItemLayout.setLayoutParams(layoutParams1);

        return menuLayout;
    }

    @OnClick({R.id.about_item_1,R.id.about_item_2,R.id.about_item_3,R.id.about_item_5,
            R.id.menu_tv,R.id.menu_music,R.id.menu_apower_mirror,R.id.menu_dining,R.id.menu_service})
    void imageClick( View v) {
        switch (v.getId()){
            case R.id.menu_tv:
                intent = pm.getLaunchIntentForPackage("com.qiyivideo.sibichi");
                if (intent != null) {
                    mContext.startActivity(intent);
                } else {
                    Toast.makeText(mContext, "暂时不提供该服务！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_music:
                intent = pm.getLaunchIntentForPackage("com.netease.cloudmusic");
                if (intent != null) {
                    mContext.startActivity(intent);
                } else {
                    intent = pm.getLaunchIntentForPackage("com.tencent.qqmusictv");
                    if (intent != null) {
                        mContext.startActivity(intent);
                    } else {
                        Toast.makeText(mContext, "抱歉，您还没有安装音乐软件！", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.menu_apower_mirror:
                Intent ent = new Intent();
                ent.setClass(mContext, ControlActivity.class);
                mContext.startActivity(ent);
                break;
            case R.id.menu_dining:
                startActivity(new Intent(mContext, DiningActivity.class),
                        ActivityOptions.makeSceneTransitionAnimation((Activity) mContext,
                                v, "image_dining").toBundle());
                break;
            case R.id.menu_service:
                Intent in = new Intent();
                in.putExtra("main",true);
                in.setClass(mContext, HotelServiceActivity.class);
                mContext.startActivity(in);
                break;
            case R.id.about_item_1:
                Intent it = new Intent();
                it.setClass(mContext, HotelServiceActivity.class);
                it.putExtra("about_item_1",true);
                it.putExtras(new Bundle());
                mContext.startActivity(it);
                break;
            case R.id.about_item_2:
                Intent ti = new Intent();
                ti.setClass(mContext, HotelServiceActivity.class);
                ti.putExtra("about_item_2",true);
                ti.putExtras(new Bundle());
                mContext.startActivity(ti);
                break;
            case R.id.about_item_3:
                Intent ite = new Intent();
                Bundle bundle = new Bundle();
                ite.setClass(mContext, HotelServiceActivity.class);
                ite.putExtra("about_item_4",true);
                ite.putExtras(bundle);
                mContext.startActivity(ite);
                break;
            case R.id.about_item_5:
                Intent iten = new Intent();
                iten.setClass(mContext, HotelServiceActivity.class);
                iten.putExtra("about_item_5",true);
                iten.putExtras(new Bundle());
                mContext.startActivity(iten);
                break;
            default:
                break;
        }
    }

    @OnFocusChange({R.id.tv,R.id.menu_tv,R.id.menu_music,R.id.menu_apower_mirror,R.id.menu_dining,R.id.menu_service})
    public void onViewFocusChange(View view, boolean isFocus){
        if(isFocus){
            switch (view.getId()){
                case R.id.tv:
                    aboutItem.setVisibility(View.GONE);
                    break;
                case R.id.menu_tv:
                    aboutItem.setVisibility(View.GONE);
                    break;
                case R.id.menu_music:
                    aboutItem.setVisibility(View.GONE);
                    break;
                case R.id.menu_apower_mirror:
                    aboutItem.setVisibility(View.GONE);
                    break;
                case R.id.menu_dining:
                    aboutItem.setVisibility(View.GONE);
                    break;
                case R.id.menu_service:
                    aboutItem.setVisibility(View.VISIBLE);
                    break;
                default:
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUb.unbind();
    }
}
