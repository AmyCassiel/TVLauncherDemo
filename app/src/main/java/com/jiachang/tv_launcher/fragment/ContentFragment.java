package com.jiachang.tv_launcher.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
<<<<<<< HEAD
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.activity.SettingActivity;
import com.jiachang.tv_launcher.service.UploadCashService;
import com.jiachang.tv_launcher.utils.CommonUtil;
import com.jiachang.tv_launcher.utils.Constant;
import com.jiachang.tv_launcher.utils.LogUtils;

import java.io.File;
=======
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.jiachang.tv_launcher.R;
>>>>>>> 3f9ea9146edea3d1a681c668f38913b73a6b461e

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

<<<<<<< HEAD
import static com.jiachang.tv_launcher.utils.Constant.MAC;

=======
>>>>>>> 3f9ea9146edea3d1a681c668f38913b73a6b461e
/**
 * @author Mickey.Ma
 * @date 2020-04-18
 * @description
 */
public class ContentFragment extends DialogFragment {
<<<<<<< HEAD
    private SettingActivity activity;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        activity = (SettingActivity) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.service_wifipassword_layout, null);
        builder.setTitle("上传日志");
        builder.setMessage("版本号：" + CommonUtil.getVersionName(activity.getApplicationContext())).setMessage("mac地址：" + MAC);
        builder.setView(view).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //检查文件是否存在 存在就开启服务  上传到服务器
                final File logFile = new File(Constant.LOG_FILE_PATH);
                if (logFile.exists()) {
                    LogUtils.i("ContentFragment", "logFile文件存在");
                    activity.startService(new Intent(activity, UploadCashService.class));
                }
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(activity.getApplicationContext(), "您已取消日志上传", Toast.LENGTH_LONG).show();
            }
        });
=======
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.service_wifipassword_layout, null);
        builder.setView(view).setNegativeButton("确定",null);
>>>>>>> 3f9ea9146edea3d1a681c668f38913b73a6b461e
        return builder.create();
    }
}
