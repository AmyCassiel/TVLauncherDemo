package com.jiachang.tv_launcher.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Looper;
import android.widget.Toast;

import com.jiachang.tv_launcher.utils.Constant;
import com.jiachang.tv_launcher.utils.HttpUtils;

import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
/**
 * @author Mickey.Ma
 * @date 2020-06-09
 * @description
 */
public class UploadCashService extends IntentService {
    private String TAG = "UploadCashService";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public UploadCashService(String name) {
        super(name);
    }

    public UploadCashService() {
        super("UploadCashService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {}

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    uploadFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        return super.onStartCommand(intent, flags, startId);
    }
    public void uploadFile()throws Exception{
        String cuid= URLEncoder.encode(Constant.MAC,"utf-8");
        Map<String, String> params = new HashMap<String, String>();
        Map<String, File> upfiles = new HashMap<String, File>();
        File file = new File(Constant.LOG_FILE_PATH);
        upfiles.put(Constant.MAC+".txt", file);
        params.put("cuid", cuid);
        boolean post = HttpUtils.post(Constant.CASHUPLOAD, params, upfiles);
        if (post){
            Looper.prepare();
            Toast.makeText(UploadCashService.this, "上传成功", Toast.LENGTH_LONG).show();
            Looper.loop();
            if (file.exists()){
                file.delete();
            }
        }else {
            Looper.prepare();
            Toast.makeText(UploadCashService.this, "上传失败", Toast.LENGTH_LONG).show();
            Looper.loop();
        }
    }
}
