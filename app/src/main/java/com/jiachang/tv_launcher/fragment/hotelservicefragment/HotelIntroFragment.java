package com.jiachang.tv_launcher.fragment.hotelservicefragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jiachang.tv_launcher.R;
import com.zhy.autolayout.AutoLinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.jiachang.tv_launcher.utils.Constants.breakfastTime;
import static com.jiachang.tv_launcher.utils.Constants.business;
import static com.jiachang.tv_launcher.utils.Constants.hotelIntroduction;
import static com.jiachang.tv_launcher.utils.Constants.hotelName;
import static com.jiachang.tv_launcher.utils.Constants.hotelPolicys;
import static com.jiachang.tv_launcher.utils.Constants.tel;
import static com.jiachang.tv_launcher.utils.Constants.usageMonitoring;
import static com.jiachang.tv_launcher.utils.Constants.userNeeds;
import static com.jiachang.tv_launcher.utils.Constants.wifiName;
import static com.jiachang.tv_launcher.utils.HttpUtils.netWorkCheck;

/**
 * @author Mickey.Ma
 * @date 2020-05-18
 * @description
 */
public class HotelIntroFragment extends Fragment {
    private Unbinder mUnbinder;
    private Activity mActivity;

    @BindView(R.id.introduce_hotel)
    AutoLinearLayout introduceHotel;

    @BindView(R.id.introduce_hotel_txt)
    TextView introHotelTxt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View introHotel = inflater.inflate(R.layout.service_intro_fragment,container,false);
        mUnbinder = ButterKnife.bind(this, introHotel);
        mActivity = (Activity) getContext();

        DisplayMetrics metric = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        // 屏幕宽度（像素）
        int width = metric.widthPixels;
        // 屏幕高度（像素）
        int height = AutoLinearLayout.LayoutParams.WRAP_CONTENT;
        ViewGroup.LayoutParams iH = introduceHotel.getLayoutParams();
        iH.width = width / 5*4;
        iH.height = height;

        getData();

        return introHotel;
    }

    private void getData() {
        if (netWorkCheck(mActivity)) {
            //网络已连接
            if (hotelName != null && !hotelName.isEmpty()) {
                introHotelTxt.setText("\u3000\u3000" + hotelIntroduction + "\n\u3000\u3000周围商圈有：" + business  + "\n\u3000\u3000早餐供应时间：" + breakfastTime
                        + "\n\u3000\u3000WIFI：" + wifiName + "\n\u3000\u3000前台电话：" + tel);
            }
        } else {
            // 网络未连接
            if (hotelName != null && !hotelName.isEmpty()) {
                introHotelTxt.setText("\u3000\u3000" + hotelIntroduction + "\n\u3000\u3000周围商圈有：" + business + "\n\u3000\u3000早餐供应时间：" + breakfastTime
                        + "\n\u3000\u3000WIFI：" + wifiName + "\n\u3000\u3000前台电话：" + tel);
            }
        }

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
