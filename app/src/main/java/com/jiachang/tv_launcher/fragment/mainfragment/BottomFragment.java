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

/**
 * @author Mickey.Ma
 * @date 2020-03-26
 * @description 底部插件，用来显示wifi密码和早餐时间
 */
public class BottomFragment extends Fragment {
    private Unbinder mUnbinder;
    private MainActivity activity;

    @BindView(R.id.wifi_id)
    TextView tvName;
    @BindView(R.id.wifi_password)
    TextView tvPassword;
    @BindView(R.id.breakfast_time)
    TextView tvBreakfastTime;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View bottomlayout = inflater.inflate(R.layout.main_bottom_bar,container,false);
        mUnbinder = ButterKnife.bind(this,bottomlayout);
        activity = (MainActivity) getActivity();
        return bottomlayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Constant.wifiName = activity.getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS)
                .getString("wifi", "");
        Constant.wifiPassword = activity.getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS)
                .getString("wifipassword", "");
        Constant.breakfastTime = activity.getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS)
                .getString("breakfastTime", "");
        Constant.sFacilityLocation = activity.getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS)
                .getString("local", "");

        if (!Constant.wifiName.isEmpty()&& !Constant.wifiPassword.isEmpty()){
            tvName.setText("WIFI账号："+Constant.wifiName);
            tvPassword.setText("密码："+Constant.wifiPassword);
        }else {
            tvName.setText("");
            tvPassword.setText("");
        }
        if (!Constant.breakfastTime.isEmpty()&& !Constant.sFacilityLocation.isEmpty()){
            tvBreakfastTime.setText("早餐供应时间：" +Constant.breakfastTime+"("+Constant.sFacilityLocation+")");
        }else {
            tvBreakfastTime.setText("");
        }
    }
}
