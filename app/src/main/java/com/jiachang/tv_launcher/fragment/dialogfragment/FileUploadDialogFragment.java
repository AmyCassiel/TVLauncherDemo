package com.jiachang.tv_launcher.fragment.dialogfragment;

import android.app.AlertDialog;
import android.app.Dialog;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import static com.jiachang.tv_launcher.utils.Constant.MAC;

/**
 * @author Mickey.Ma
 * @date 2020-04-18
 * @description
 */
public class FileUploadDialogFragment extends DialogFragment {
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
        return builder.create();
    }
}
