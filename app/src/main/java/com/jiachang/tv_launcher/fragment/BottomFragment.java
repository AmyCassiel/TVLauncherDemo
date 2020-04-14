package com.jiachang.tv_launcher.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiachang.tv_launcher.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Mickey.Ma
 * @date 2020-03-26
 * @description
 */
public class BottomFragment extends Fragment {
    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View bottomlayout = inflater.inflate(R.layout.main_bottom_bar,container,false);
        mUnbinder = ButterKnife.bind(this,bottomlayout);
        return bottomlayout;
    }
}
