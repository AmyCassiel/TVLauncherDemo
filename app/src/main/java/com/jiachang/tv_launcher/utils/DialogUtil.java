package com.jiachang.tv_launcher.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 *
 * dialog工具类
 */

public class DialogUtil {
    private static ProgressDialog pd;
    public static void start(Context context){
        if (pd == null){
            pd = new ProgressDialog(context);
            pd.setMessage("加载中，请稍后...");
            pd.show();
        }else {
            pd.show();
        }
    }
    public static void finish(){
        if (pd != null && pd.isShowing()){
            pd.cancel();
            pd = null;
        }
    }

    public static void start(Context context, String message){
        if (pd == null ){
            pd = new ProgressDialog(context);
            pd.setMessage(message);
            pd.show();
        }else {
            pd.setMessage(message);
            pd.show();
        }
    }
}
