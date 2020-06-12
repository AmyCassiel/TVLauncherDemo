package com.jiachang.tv_launcher.activity;

import android.app.Activity;
<<<<<<< HEAD
import android.content.Context;
import android.content.DialogInterface;
=======
>>>>>>> 3f9ea9146edea3d1a681c668f38913b73a6b461e
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
<<<<<<< HEAD
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
=======
import android.provider.Settings;
import android.util.Log;
>>>>>>> 3f9ea9146edea3d1a681c668f38913b73a6b461e
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

<<<<<<< HEAD
import com.aispeech.upload.util.LogUtil;
import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.fragment.ContentFragment;
import com.jiachang.tv_launcher.service.UploadCashService;
import com.jiachang.tv_launcher.utils.CommonUtil;
import com.jiachang.tv_launcher.utils.Constant;
import com.jiachang.tv_launcher.utils.IPUtils;
import com.jiachang.tv_launcher.utils.LogUtils;
=======
import com.jiachang.tv_launcher.BuildConfig;
import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.utils.IPUtils;
>>>>>>> 3f9ea9146edea3d1a681c668f38913b73a6b461e
import com.jiachang.tv_launcher.utils.QRCodeUtil;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

<<<<<<< HEAD
import java.io.File;

=======
>>>>>>> 3f9ea9146edea3d1a681c668f38913b73a6b461e
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

<<<<<<< HEAD
import static com.jiachang.tv_launcher.utils.Constant.MAC;
=======
import static com.jiachang.tv_launcher.utils.Constants.MAC;
>>>>>>> 3f9ea9146edea3d1a681c668f38913b73a6b461e

/**
 * @author Mickey.Ma
 * @date 2020-03-31
 * @description
 */
public class SettingActivity extends FragmentActivity {
    private static final String TAG = "SettingActivity";
    private Context context;
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
        context = this;
        ButterKnife.bind(this);
        getMac();
    }

<<<<<<< HEAD
    /**
     * 隐藏虚拟按键，并且全屏
     */
=======
    /**隐藏虚拟按键，并且全屏*/
>>>>>>> 3f9ea9146edea3d1a681c668f38913b73a6b461e
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
     *
     * @return
     */
<<<<<<< HEAD
    private String getMac() {
        LogUtils.d(TAG+".108","mac = "+ MAC);
        if (!MAC.isEmpty()){
            img.setImageBitmap(QRCodeUtil.createQRCode(MAC));
        }
=======
    private String getMac(){
        String mac = IPUtils.getLocalEthernetMacAddress();
        Log.d("mac",MAC);
        img.setImageBitmap(QRCodeUtil.createQRCode(MAC));
>>>>>>> 3f9ea9146edea3d1a681c668f38913b73a6b461e
        return MAC;
    }

    @OnFocusChange({R.id.et_account, R.id.et_pwd, R.id.tv_confirm, R.id.tv_cancle, R.id.set_setting})
    public void onViewFocusChange(View view, boolean isfocus) {
    }

    @OnClick({R.id.tv_confirm, R.id.set_setting, R.id.tv_cancle})
    void setOnViewClick(View v) {
        switch (v.getId()) {
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
<<<<<<< HEAD
                    if (launchIntentForPackage != null) {
                        startActivity(launchIntentForPackage);
                    } else {
                        Toast.makeText(this, "未安装该应用", Toast.LENGTH_SHORT).show();
                    }

=======
                    if (launchIntentForPackage != null)
                        startActivity(launchIntentForPackage);
                    else
                        Toast.makeText(this, "未安装该应用", Toast.LENGTH_SHORT).show();
>>>>>>> 3f9ea9146edea3d1a681c668f38913b73a6b461e
                    finish();
                } else {
                    Toast.makeText(SettingActivity.this, "密码错误，请重新输入", Toast.LENGTH_LONG).show();
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
<<<<<<< HEAD
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU: //菜单键
                FragmentManager fM = getSupportFragmentManager();
                ContentFragment dialogFragment = new ContentFragment();
                dialogFragment.show(fM,"");
                break;
            default:
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
=======
>>>>>>> 3f9ea9146edea3d1a681c668f38913b73a6b461e
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
