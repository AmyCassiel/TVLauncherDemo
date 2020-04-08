package com.jiachang.tv_launcher.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.utils.QRCodeUtil;
import com.jiachang.tv_launcher.utils.ViewUtils;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnFocusChange;

/**
 * @author Mickey.Ma
 * @date 2020-03-31
 * @description
 */
public class SettingActivity extends Activity {
    private static boolean ignoreCase = false;
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
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        Context context = this;
        getMac(context);
        initView();

    }

    private void initView() {

        set_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img.setVisibility(View.GONE);
                set_setting.setVisibility(View.GONE);
                login.setVisibility(View.VISIBLE);
                id.setVisibility(View.VISIBLE);
                pwd.setVisibility(View.VISIBLE);
                btn.setVisibility(View.VISIBLE);
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ed_account.getText().toString();
                String pwd = ed_pwd.getText().toString();
                if (username.equals("admin") && pwd.equals("jiachang888")) {
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SettingActivity.this,"用户名或密码错误",Toast.LENGTH_LONG).show();
                }
            }
        });
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_account.setText("");
                ed_pwd.setText("");
                img.setVisibility(View.VISIBLE);
                set_setting.setVisibility(View.VISIBLE);
                login.setVisibility(View.GONE);
                id.setVisibility(View.GONE);
                pwd.setVisibility(View.GONE);
                btn.setVisibility(View.GONE);
            }
        });
    }

    protected void hideBottomMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
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
    private String getMac(Context context){
        String mac = "" ;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mac = getMacDefault(context);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mac = getMacAddress();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mac = getMacFromHardware();
        }
        Log.e("mac = ",mac);
        img.setImageBitmap(QRCodeUtil.createQRCode(mac));
        return mac;
    }

    /**
     * Android 6.0 之前（不包括6.0）获取mac地址
     * 必须的权限 <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"></uses-permission>
     * @param context * @return
     */
    public static String getMacDefault(Context context) {
        String mac = "";
        if (context == null) {
            return mac;
        }
        WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = null;
        try {
            info = wifi.getConnectionInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (info == null) {
            return null;
        }
        mac = info.getMacAddress();
        if (!TextUtils.isEmpty(mac)) {
            mac = mac.toUpperCase(Locale.ENGLISH);
        }
        return mac;
    }

    /**
     * Android 6.0-Android 7.0 获取mac地址
     */
    public static String getMacAddress() {
        String macSerial = null;
        String str = "";

        try {
            Process pp = Runtime.getRuntime().exec("cat/sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            while (null != str) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();//去空格
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }

        return macSerial;
    }

    /**
     * Android 7.0之后获取Mac地址
     * 遍历循环所有的网络接口，找到接口是 wlan0
     * 必须的权限 <uses-permission android:name="android.permission.INTERNET"></uses-permission>
     * @return
     */
    public static String getMacFromHardware() {
        try {
            Enumeration<NetworkInterface> interfaceEnumeration = NetworkInterface.getNetworkInterfaces();
            while (interfaceEnumeration.hasMoreElements()){
                NetworkInterface networkInterface = interfaceEnumeration.nextElement();
                byte[] addr = networkInterface.getHardwareAddress();
                if (addr==null || addr.length==0){
                    continue;
                }
                StringBuilder buf = new StringBuilder();
                for (byte b: addr){
                    buf.append(String.format("%02X:",b));
                }
                if (buf.length()>0){
                    buf.deleteCharAt(buf.length()-1);

                }
                return buf.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    @OnFocusChange({R.id.et_account,R.id.et_pwd,R.id.tv_confirm,R.id.tv_cancle,R.id.set_setting})
    public void onViewFocusChange(View view, boolean isfocus){
        ViewUtils.scalerView(view, isfocus);
    }
}
