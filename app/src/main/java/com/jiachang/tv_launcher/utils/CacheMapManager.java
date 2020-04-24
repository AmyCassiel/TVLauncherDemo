package com.jiachang.tv_launcher.utils;

import android.content.Context;
import android.util.Log;

import com.jiachang.tv_launcher.SampleApplication;

import java.util.HashMap;

/**
 * @author Mickey.Ma
 * @date 2020-04-21
 * @description
 */
public class CacheMapManager {
    //put 缓存
    public synchronized static void putCache(String key, Object object) {
        if (SampleApplication.dataMap == null) {
            SampleApplication.dataMap = new HashMap<String, Object>();
        }
        SampleApplication.dataMap.put(key, object);
    }

    //get 缓存
    public static Object getCache(Context context, String key) {
        if (SampleApplication.dataMap == null) {
            SampleApplication.dataMap = new HashMap<String, Object>();
        }
        Object obj = SampleApplication.dataMap.get(key);
        if (obj == null) {
//            Util.toRestart(context);
            Log.e("aaa", "缓存数据丢失，重启程序包");
        }
        return obj;
    }

    //是否可以为空
    public static Object getCache(Context context, String key, boolean canEmpty) {
        if (SampleApplication.dataMap == null) {
            SampleApplication.dataMap = new HashMap<String, Object>();
        }
        Object obj = SampleApplication.dataMap.get(key);
        if (!canEmpty && obj == null) {
//            Util.toRestart(context);
            Log.e("aaa", "缓存数据丢失，重启程序包");
        }
        return obj;
    }

    public static void putCach(String presentCachinfo, Object info) {
    }
}
