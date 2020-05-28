package com.jiachang.tv_launcher.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jiachang.tv_launcher.BuildConfig;
import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.utils.IPUtils;
import com.jiachang.tv_launcher.utils.QRCodeUtil;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

import static com.jiachang.tv_launcher.utils.Constants.MAC;

/**
 * @author Mickey.Ma
 * @date 2020-03-31
 * @description
 */
public class SettingActivity extends Activity {
    @BindView(R.id.et_account)
    EditText ed_account;
    @BindView(R.id.et_pwd)
    EditText ed_pwd;
    @BindView(R.id.tv_confirm)
    Button tv_confirm;
    @BindView(R.id.tv_cancle)
    Button tv_cancle;
    @BindView(R.id.set_setting)
    Button set_setting;
    @BindView(R.id.id)
    AutoLinearLayout id;
    @BindView(R.id.login)
    TextView login;
    @BindView(R.id.pwd)
    AutoLinearLayout pwd;
    @BindView(R.id.btn)
    AutoRelativeLayout btn;
    @BindView(R.id.img)
    ImageView img;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomMenu();
        setContentView(R.layout.service_setting_activity);
        ButterKnife.bind(this);
        getMac();

    }

    /**隐藏虚拟按键，并且全屏*/
    protected void hideBottomMenu() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * 获取mac地址（适配所有Android版本）
     * @return
     */
    private String getMac(){
        String mac = IPUtils.getLocalEthernetMacAddress();
        Log.d("mac",MAC);
        img.setImageBitmap(QRCodeUtil.createQRCode(MAC));
        return MAC;
    }

    @OnFocusChange({R.id.et_account,R.id.et_pwd,R.id.tv_confirm,R.id.tv_cancle,R.id.set_setting})
    public void onViewFocusChange(View view, boolean isfocus){
    }
    @OnClick({R.id.tv_confirm,R.id.set_setting, R.id.tv_cancle})
    void setOnViewClick(View v){
        switch (v.getId()){
            case R.id.set_setting:
                img.setVisibility(View.GONE);
                set_setting.setVisibility(View.GONE);
                login.setVisibility(View.VISIBLE);
                id.setVisibility(View.VISIBLE);
                pwd.setVisibility(View.VISIBLE);
                btn.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_confirm:
                String username = ed_account.getText().toString();
                String mypwd = ed_pwd.getText().toString();
                if (username.equals("admin") && mypwd.equals("jiachang888")) {
                    /*Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    startActivity(intent);*/
                    PackageManager packageManager = getPackageManager();
                    String packageName = "com.android.tv.settings";//要打开应用的包名,以微信为例
                    Intent launchIntentForPackage = packageManager.getLaunchIntentForPackage(packageName);
                    if (launchIntentForPackage != null)
                        startActivity(launchIntentForPackage);
                    else
                        Toast.makeText(this, "未安装该应用", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(SettingActivity.this,"用户名或密码错误",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.tv_cancle:
                ed_account.setText("");
                ed_pwd.setText("");
                img.setVisibility(View.VISIBLE);
                set_setting.setVisibility(View.VISIBLE);
                login.setVisibility(View.GONE);
                id.setVisibility(View.GONE);
                pwd.setVisibility(View.GONE);
                btn.setVisibility(View.GONE);
                break;
            default:
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
