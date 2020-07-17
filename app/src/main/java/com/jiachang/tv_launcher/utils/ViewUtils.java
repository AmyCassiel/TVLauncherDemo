package com.jiachang.tv_launcher.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import com.jiachang.tv_launcher.activity.MainActivity;

import androidx.core.view.ViewCompat;

/**
 * @author Mickey.Ma
 * @date 2020-03-28
 * @description
 */
public abstract class ViewUtils {
    /**
     * 动态放大/缩小
     *
     * @param view
     * @param hasFocus
     */
    public static void sView(View view, boolean hasFocus) {
        float scale = hasFocus ? 1.05f : 1.0f;
        view.animate().scaleX(scale).scaleY(scale).setInterpolator(new AccelerateInterpolator()).setDuration(200);
    }

    /**
     * item获得焦点时调用
     *
     * @param itemView view
     */
    public static void focusStatus(View itemView) {
        if (itemView == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            //抬高Z轴
            ViewCompat.animate(itemView).scaleX(1.05f).scaleY(1.05f).translationZ(1).start();
        } else {
            ViewCompat.animate(itemView).scaleX(1.10f).scaleY(1.10f).start();
            ViewGroup parent = (ViewGroup) itemView.getParent();
            parent.requestLayout();
            parent.invalidate();
        }
        onItemFocus(itemView);
    }

    /**
     * 当item获得焦点时处理
     *
     * @param itemView itemView
     */
    protected static void onItemFocus(View itemView) {
    }

    /**
     * item失去焦点时
     *
     * @param itemView item对应的View
     */
    public static void normalStatus(View itemView) {
        if (itemView == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            ViewCompat.animate(itemView).scaleX(1.0f).scaleY(1.0f).translationZ(0).start();
        } else {
            ViewCompat.animate(itemView).scaleX(1.0f).scaleY(1.0f).start();
            ViewGroup parent = (ViewGroup) itemView.getParent();
            parent.requestLayout();
            parent.invalidate();
        }
        onItemGetNormal(itemView);
    }

    /**
     * 当条目失去焦点时调用
     *
     * @param itemView 条目对应的View
     */
    protected static void onItemGetNormal(View itemView) {
    }

    public static int tvLauncherLScale(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;         // 屏幕宽度（像素）
        int height = metric.heightPixels;       // 屏幕高度（像素）
        float density = metric.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = metric.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）

        String info = "机顶盒型号: " + android.os.Build.MODEL
                + ",\nSDK版本:" + android.os.Build.VERSION.SDK
                + ",\n系统版本:" + android.os.Build.VERSION.RELEASE
                + "\n屏幕宽度（像素）: " + width
                + "\n屏幕高度（像素）: " + height
                + "\n屏幕密度:  " + density
                + "\n屏幕密度DPI: " + densityDpi;
        Log.i("ViewUtils", info);

        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)
        return metric.widthPixels;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(float dpValue) {
        return (int) (0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
    }
}
