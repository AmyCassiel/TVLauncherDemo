package com.jiachang.tv_launcher.fragment.hotelservicefragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiachang.tv_launcher.R;
import com.zhy.autolayout.AutoLinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.jiachang.tv_launcher.utils.Constants.hotelName;
import static com.jiachang.tv_launcher.utils.Constants.usageMonitoring;
import static com.jiachang.tv_launcher.utils.Constants.userNeeds;
import static com.jiachang.tv_launcher.utils.HttpUtils.netWorkCheck;

/**
 * @author Mickey.Ma
 * @date 2020-05-18
 * @description
 */
public class HotelIntroControlFragment extends Fragment {
    private Unbinder mUnbinder;
    private Activity mActivity;

    /*@BindView(R.id.introduce_control)
    AutoLinearLayout introduceControl;
    @BindView(R.id.introduce_control_txt)
    TextView introControlTxt;*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.service_intro_control_fragment,container,false);
        mUnbinder = ButterKnife.bind(this, view);
        mActivity = (Activity) getContext();

        /*DisplayMetrics metric = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        // 屏幕宽度（像素）
        int width = metric.widthPixels;
        // 屏幕高度（像素）
        int height = AutoLinearLayout.LayoutParams.WRAP_CONTENT;
        ViewGroup.LayoutParams iF = introduceControl.getLayoutParams();
        iF.width = width / 2;
        iF.height = height;

        if (netWorkCheck(mActivity)) {
            //网络已连接
            if (hotelName != null && !hotelName.isEmpty()) {
                introControlTxt.setText(usageMonitoring);
            }
        } else {
            // 网络未连接
            if (hotelName != null && !hotelName.isEmpty()) {
                introControlTxt.setText(usageMonitoring);
            }
        }*/
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
