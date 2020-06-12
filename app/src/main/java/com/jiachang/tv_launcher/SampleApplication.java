package com.jiachang.tv_launcher;

import android.app.AlarmManager;
import android.app.Application;
<<<<<<< HEAD
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import com.tencent.bugly.Bugly;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

=======
import android.app.backup.BackupManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

import com.tencent.bugly.Bugly;
import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;
>>>>>>> 3f9ea9146edea3d1a681c668f38913b73a6b461e

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.RequiresApi;

import static com.tencent.bugly.Bugly.applicationContext;

/**
 * @author Mickey.Ma
 * @date 2020-03-24
 * @description
 */
public class SampleApplication extends Application {
<<<<<<< HEAD

    public static Map dataMap;

    @Override
    public void onCreate() {
        super.onCreate();

        Bugly.init(getApplicationContext(), "7ced2eccf9", false);

        //设置酒店信息缓存
        dataMap = new HashMap<String, Object>();
        
        //修改系统时区
        AlarmManager mAlarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.setTimeZone("Asia/Shanghai");
        
        //修改系统默认语言
        String language = "zh";
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = getResources().getConfiguration();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        config.locale = Locale.SIMPLIFIED_CHINESE;
        getResources().updateConfiguration(config,metrics);
    }
=======

    public static Map dataMap;

    @Override
    public void onCreate() {
        super.onCreate();
>>>>>>> 3f9ea9146edea3d1a681c668f38913b73a6b461e

        Bugly.init(getApplicationContext(), "7ced2eccf9", false);

        //设置酒店信息缓存
        dataMap = new HashMap<String, Object>();
        
        //修改系统时区
        AlarmManager mAlarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.setTimeZone("Asia/Shanghai");
        
        //修改系统默认语言
        String language = "zh";
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = getResources().getConfiguration();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        config.locale = Locale.SIMPLIFIED_CHINESE;
        getResources().updateConfiguration(config,metrics);
    }
}
