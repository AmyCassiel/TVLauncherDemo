package com.jiachang.tv_launcher.utils;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.jiachang.tv_launcher.interfaces.RetrofitInterface;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
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

    /*
     * 初始化retrofit
     * */
    public static void getRetrofit(String baseUrl){
        OkHttpClient okHttpClient = new OkHttpClient();

        // request body
        Map<String, String> foo = new HashMap<>();
        foo.put("rs", "execAttr");
        foo.put("rsargs[]", "18");
        foo.put("rsargs[m]","");

        Request request = new Request.Builder().url(baseUrl)
                .post(RequestBody.create(MediaType.get("application/json"), JSONObject.toJSONString(foo))).build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            ResponseBody body = response.body();
            if (response.isSuccessful()) {
                Log.i("success:{}", body == null ? "" : body.string());
            } else {
                Log.e("error,statusCode={},body={}", ""+response.code()+""+body == null ? "" : body.string());
            }
        }catch (IOException e){
            e.getMessage();
        }
    }
}
