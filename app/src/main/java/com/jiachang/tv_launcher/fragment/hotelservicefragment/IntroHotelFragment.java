package com.jiachang.tv_launcher.fragment.hotelservicefragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
<<<<<<< HEAD
=======
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
>>>>>>> 3f9ea9146edea3d1a681c668f38913b73a6b461e
import android.widget.TextView;

import com.jiachang.tv_launcher.R;
import com.zhy.autolayout.AutoLinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

<<<<<<< HEAD
import static com.jiachang.tv_launcher.utils.Constant.breakfastTime;
import static com.jiachang.tv_launcher.utils.Constant.business;
import static com.jiachang.tv_launcher.utils.Constant.hotelIntroduction;
import static com.jiachang.tv_launcher.utils.Constant.hotelName;
import static com.jiachang.tv_launcher.utils.Constant.tel;
import static com.jiachang.tv_launcher.utils.Constant.wifiName;
=======
import static com.jiachang.tv_launcher.utils.Constants.breakfastTime;
import static com.jiachang.tv_launcher.utils.Constants.business;
import static com.jiachang.tv_launcher.utils.Constants.hotelIntroduction;
import static com.jiachang.tv_launcher.utils.Constants.hotelName;
import static com.jiachang.tv_launcher.utils.Constants.hotelPolicys;
import static com.jiachang.tv_launcher.utils.Constants.tel;
import static com.jiachang.tv_launcher.utils.Constants.usageMonitoring;
import static com.jiachang.tv_launcher.utils.Constants.userNeeds;
import static com.jiachang.tv_launcher.utils.Constants.wifiName;
>>>>>>> 3f9ea9146edea3d1a681c668f38913b73a6b461e
import static com.jiachang.tv_launcher.utils.HttpUtils.netWorkCheck;

/**
 * @author Mickey.Ma
 * @date 2020-05-18
 * @description
 */
public class IntroHotelFragment extends Fragment {
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
        iH.width = width / 3*2;
        iH.height = height;

        getData();

        return introHotel;
    }

    private void getData() {
        if (netWorkCheck(mActivity)) {
            //网络已连接
            if (hotelName != null && !hotelName.isEmpty()) {
                introHotelTxt.setText("\u3000\u3000" + hotelIntroduction + "\n\u3000\u3000周围商圈有：" + business  + "\n\u3000\u3000早餐供应时间：" + breakfastTime
                        + "\n\u3000\u3000" + wifiName + "\n\u3000\u3000前台电话：" + tel);
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
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
