package com.jiachang.tv_launcher;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.backup.BackupManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static com.jiachang.tv_launcher.utils.HttpUtils.netWorkCheck;

/**
 * @author Mickey.Ma
 * @date 2020-03-24
 * @description
 */
public class SampleApplication extends TinkerApplication {

    public static Map dataMap;

    public SampleApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.jiachang.tv_launcher.SampleApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        
        //设置酒店信息缓存
        dataMap = new HashMap<String, Object>();
        
        //修改系统时区
//        ((AlarmManager)getSystemService(Context.ALARM_SERVICE)).setTimeZone("Asia/Shanghai");
        AlarmManager mAlarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.setTimeZone("Asia/Shanghai");
        
        //修改系统默认语言
        /*String langCode = "zh-CN";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            changeLocale(langCode);
        }*/

        String language = "zh";
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = getResources().getConfiguration();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        config.locale = Locale.SIMPLIFIED_CHINESE;
        getResources().updateConfiguration(config,metrics);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void changeLocale(String langCode) {
        int size = getResources().getConfiguration().getLocales().size();
        for(int i=0; i<size; i++){
            Locale locale = getResources().getConfiguration().getLocales().get(i);
            if (!langCode.equals(locale.getLanguage())){
                updateLanguage(locale);
                break;
            }
        }
    }
    public static void updateLanguage(Locale locale){
        //修改系统默认语言
        try {
            Class iActivityManager = Class.forName("android.app.IActivityManager");
            Class activityManagerNative = Class.forName("android.app.ActivityManagerNative");
            Method getDefault = activityManagerNative.getDeclaredMethod("getDefault");
            Object objIActMag = getDefault.invoke(activityManagerNative);
            Method getConfiguration = iActivityManager.getDeclaredMethod("getConfiguration");
            Configuration config = (Configuration) getConfiguration.invoke(objIActMag);
            config.locale = locale;
            Class clzConfig = Class.forName("android.content.res.Configuration");
            java.lang.reflect.Field userSetLocale = clzConfig.getField("userSetLocale");
            userSetLocale.set(config, true);
            // 此处需要声明权限:android.permission.CHANGE_CONFIGURATION
            // 会重新调用 onCreate();
            Class[] clzParams = {Configuration.class};
            Method updateConfiguration = iActivityManager.getDeclaredMethod("updateConfiguration", clzParams);
            Log.d("SampleApplication","updateConfiguration:"+updateConfiguration);
            updateConfiguration.invoke(objIActMag, config);
            BackupManager.dataChanged("com.android.providers.settings");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
