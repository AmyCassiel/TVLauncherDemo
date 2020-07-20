package com.jiachang.tv_launcher.utils;

import android.util.Log;

import com.jiachang.tv_launcher.interfaces.RetrofitInterface;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiRetrofit {
    /*
     * 初始化retrofit
     * */
    public static RetrofitInterface initRetrofit(String baseUrl){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message -> Log.i("okHttp",message));
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);//Level中还有其他等级
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)//添加拦截者
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())//声明兼容Gson
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//声明兼容rxJava
                .client(client)//载入拦截器
                .build();
        //创建Retrofit接口对象
        return retrofit.create(RetrofitInterface.class);
    }
}
