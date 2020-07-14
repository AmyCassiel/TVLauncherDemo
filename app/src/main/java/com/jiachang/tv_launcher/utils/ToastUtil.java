package com.jiachang.tv_launcher.utils;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Mickey.Ma
 * @date 2020-07-07
 * @description 自定义吐司的实体类
 */
class ToastUtil {
    private static Toast toast;//单例的toast
    /**
     * 显示Toast
     */
    public static void showToast(final Activity activity, final String message) {
        if ("main".equals(Thread.currentThread().getName())) {
            Log.e("ToastUtil", "在主线程");
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("ToastUtil", "不在主线程");
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
