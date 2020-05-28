package com.jiachang.tv_launcher.fragment.mainfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiachang.tv_launcher.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Mickey.Ma
 * @date 2020-03-26
 * @description
 */
public class BottomFragment extends Fragment {
    private Unbinder mUnbinder;

    @BindView(R.id.wifi_id)
    TextView wifiId;
    @BindView(R.id.wifi_password)
    TextView wifiPassword;
    @BindView(R.id.breakfast_time)
    TextView breakfastTime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View bottomlayout = inflater.inflate(R.layout.main_bottom_bar,container,false);
        mUnbinder = ButterKnife.bind(this,bottomlayout);
        return bottomlayout;
    }
}
