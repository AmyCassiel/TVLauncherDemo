package com.jiachang.tv_launcher.fragment.apowerfragment;

import android.content.Context;
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
 * @date 2020-04-11
 * @description
 */
public class AndroidFragment extends Fragment {
    private Unbinder mUb;
    private Context mContext;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.apower_mirror_android_fragment, container, false);
        mUb = ButterKnife.bind(this, view);
        mContext = getActivity();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
