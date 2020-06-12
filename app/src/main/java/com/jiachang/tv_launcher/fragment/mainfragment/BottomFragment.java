package com.jiachang.tv_launcher.fragment.mainfragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.activity.MainActivity;

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
 * @description
 */
public class BottomFragment extends Fragment {
    private Unbinder mUnbinder;
    private MainActivity activity;

    @BindView(R.id.wifi_id)
    TextView wifiId;
   /* @BindView(R.id.wifi_password)
    TextView wifiPassword;*/
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
        String breakTime = activity.getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS)
                .getString("breakfastTime", "");
        sFacilityLocation = activity.getApplicationContext().getSharedPreferences("hotel", Context.MODE_MULTI_PROCESS)
                .getString("local", "");
        if (!wifiID.isEmpty() && !breakTime.isEmpty() && !sFacilityLocation.isEmpty()){
            wifiId.setText(wifiID);
            breakfastTime.setText("早餐供应时间：" +breakTime+"("+sFacilityLocation+")");
        }else {
            wifiId.setText("");
            breakfastTime.setText("");
        }
        return bottomlayout;
    }
}
