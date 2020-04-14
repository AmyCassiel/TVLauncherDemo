package com.jiachang.tv_launcher.utils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import androidx.core.view.ViewCompat;

/**
 * @author Mickey.Ma
 * @date 2020-03-28
 * @description
 */
public abstract class ViewUtils {
    /**
     * 动画 放大/缩小
     *
     * @param view
     * @param hasFocus
     */
    public static void scaleView(View view, boolean hasFocus) {
        float scale = hasFocus ? 1.3f : 1.0f;
        view.animate().scaleX(scale).scaleY(scale).setInterpolator(new AccelerateInterpolator()).setDuration(200);
    }

    public static void scalerView(View view, boolean hasFocus) {
        float scale = hasFocus ? 1.2f : 1.0f;
        view.animate().scaleX(scale).scaleY(scale).setInterpolator(new AccelerateInterpolator()).setDuration(200);
    }

    /**
     * 隐藏底边状态栏
     */
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

    /**
     * item获得焦点时调用
     *
     * @param itemView view
     */
    public static void onFocusStatus(View itemView) {
        if (itemView == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            //抬高Z轴
            ViewCompat.animate(itemView).scaleX(1.05f).scaleY(1.05f).translationZ(1).start();
        } else {
            ViewCompat.animate(itemView).scaleX(1.05f).scaleY(1.05f).start();
            ViewGroup parent = (ViewGroup) itemView.getParent();
            parent.requestLayout();
            parent.invalidate();
        }
        onItemFocus(itemView);
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
            ViewCompat.animate(itemView).scaleX(1.10f).scaleY(1.10f).translationZ(1).start();
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


    /**
     * 改变图片的颜色
     * 参考：https://www.jianshu.com/p/9cae2250d0ed
     *
     * @param view  ImageView
     * @param color 颜色格式：0xA6FFFFFF
     */
    public static void setViewColorFilter(ImageView view, int color) {
        view.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }

    // the minimum scaling factor for the web dialog (50% of screen size)
    private static final double MIN_SCALE_FACTOR = 0.5;

    /**
     * Returns a scaled size (either width or height) based on the parameters passed.
     *
     * @param screenSize     a pixel dimension of the screen (either width or height)
     * @param density        density of the screen
     * @param noPaddingSize  the size at which there's no padding for the dialog
     * @param maxPaddingSize the size at which to apply maximum padding for the dialog
     * @return a scaled size.
     */
    public static int getScaledSize(int screenSize, float density, int noPaddingSize, int maxPaddingSize) {
        int scaledSize = (int) ((float) screenSize / density);
        double scaleFactor;
        if (scaledSize <= noPaddingSize) {
            scaleFactor = 1.0;
        } else if (scaledSize >= maxPaddingSize) {
            scaleFactor = MIN_SCALE_FACTOR;
        } else {
            // between the noPadding and maxPadding widths, we take a linear reduction to go from 100%
            // of screen size down to MIN_SCALE_FACTOR
            scaleFactor = MIN_SCALE_FACTOR +
                    ((double) (maxPaddingSize - scaledSize))
                            / ((double) (maxPaddingSize - noPaddingSize))
                            * (1.0 - MIN_SCALE_FACTOR);
        }
        return (int) (screenSize * scaleFactor);
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
        Log.e("System INFO", info);

        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)
        return metric.widthPixels;
    }

}
