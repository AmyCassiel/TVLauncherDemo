package com.jiachang.tv_launcher.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jiachang.tv_launcher.R;

/**
 * @author Mickey.Ma
 * @date 2020-07-07
 * @description 自定义吐司的实体类
 */
public class ToastUtil {
    /**
     * 显示Toast
     */
    public static void showToast(final Activity activity, final String message) {
        if ("main".equals(Thread.currentThread().getName())) {
            Log.e("ToastUtil", "在主线程");
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        } else {
            activity.runOnUiThread(() -> {
                Log.e("ToastUtil", "不在主线程");
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            });
        }
    }

    private Toast mToast;
    private ToastUtil(Context context, CharSequence text, int duration) {
        View v = LayoutInflater.from(context).inflate(R.layout.eplay_toast, null);
        TextView textView = (TextView) v.findViewById(R.id.toastUtil_textView);
        textView.setText(text);
        mToast = new Toast(context);
        mToast.setDuration(duration);
        mToast.setView(v);
        mToast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL,0,0);
    }

    public static ToastUtil makeText(Context context, CharSequence text, int duration) {
        return new ToastUtil(context, text, duration);
    }
    public void show() {
        if (mToast != null) {
            mToast.show();
        }
    }
}
