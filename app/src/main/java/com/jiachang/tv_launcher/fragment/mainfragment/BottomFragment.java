package com.jiachang.tv_launcher.fragment.mainfragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.activity.MainActivity;
import com.jiachang.tv_launcher.utils.Constant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.jiachang.tv_launcher.utils.Constant.sFacilityLocation;

/**
 * @author Mickey.Ma
 * @date 2020-03-26
 * @description 底部插件，用来显示wifi密码和早餐时间
 */
public class BottomFragment extends Fragment {
    private Unbinder mUnbinder;
    private MainActivity activity;

    @BindView(R.id.wifi_id)
    TextView wifiId;
    @BindView(R.id.wifi_password)
    TextView wifiPassword;
    @BindView(R.id.breakfast_time)
    TextView breakfastTime;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View bottomlayout = inflater.inflate(R.layout.main_bottom_bar,container,false);
        mUnbinder = ButterKnife.bind(this,bottomlayout);
        activity = (MainActivity) getActivity();
        String wifiID = activity.getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS)
                .getString("wifi", "");
        String wifiPass = activity.getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS)
                .getString("wifipassword", "");
        String breakTime = activity.getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS)
                .getString("breakfastTime", "");
        sFacilityLocation = activity.getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS)
                .getString("local", "");
        Log.d(getTag(),"wifiID = "+wifiID);
        if (wifiID!=null&& Constant.wifiPassword!=null && breakTime!=null && Constant.sFacilityLocation1 != null){
            wifiId.setText("wifi："+wifiID);
            wifiPassword.setText("密码："+Constant.wifiPassword);
            breakfastTime.setText("早餐供应时间：" +breakTime+"("+Constant.sFacilityLocation1+")");
        }else if (wifiID!=null && Constant.wifiPassword!=null && breakTime==null && Constant.sFacilityLocation1 == null){
            wifiId.setText("wifi："+wifiID);
            wifiPassword.setText("密码："+Constant.wifiPassword);
            breakfastTime.setText("");
        }else {
            wifiId.setText("");
            wifiPassword.setText("");
            breakfastTime.setText("");
        }
        return bottomlayout;
    }
}
