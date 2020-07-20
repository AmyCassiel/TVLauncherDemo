package com.jiachang.tv_launcher.fragment.apowerfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiachang.tv_launcher.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author Mickey.Ma
 * @date 2020-04-11
 * @description
 */
public class AndroidFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.apower_mirror_android_fragment, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
