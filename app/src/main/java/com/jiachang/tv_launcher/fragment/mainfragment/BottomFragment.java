package com.jiachang.tv_launcher.fragment.mainfragment;

import android.annotation.SuppressLint;
import android.content.Context;
<<<<<<< HEAD
=======
import android.content.SharedPreferences;
import android.graphics.Typeface;
>>>>>>> 3f9ea9146edea3d1a681c668f38913b73a6b461e
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.activity.MainActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
<<<<<<< HEAD
=======
import androidx.core.content.res.ResourcesCompat;
>>>>>>> 3f9ea9146edea3d1a681c668f38913b73a6b461e
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

<<<<<<< HEAD
import static com.jiachang.tv_launcher.utils.Constant.sFacilityLocation;
=======
<<<<<<< HEAD
import static com.jiachang.tv_launcher.utils.Constants.hotelName;
=======
>>>>>>> a85785c65a3223194db5ec8ca027ea58a13eb455
import static com.jiachang.tv_launcher.utils.Constants.sFacilityLocation;
>>>>>>> 3f9ea9146edea3d1a681c668f38913b73a6b461e

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
<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> 3f9ea9146edea3d1a681c668f38913b73a6b461e
        if (!wifiID.isEmpty() && !breakTime.isEmpty() && !sFacilityLocation.isEmpty()){
            wifiId.setText(wifiID);
            breakfastTime.setText("早餐供应时间：" +breakTime+"("+sFacilityLocation+")");
        }else {
            wifiId.setText("");
            breakfastTime.setText("");
<<<<<<< HEAD
=======
=======
        if (wifiID != null && breakfastTime != null && sFacilityLocation != null){
            wifiId.setText(wifiID);
            breakfastTime.setText("早餐供应时间：" +breakTime+"("+sFacilityLocation+")");
>>>>>>> a85785c65a3223194db5ec8ca027ea58a13eb455
>>>>>>> 3f9ea9146edea3d1a681c668f38913b73a6b461e
        }
        return bottomlayout;
    }


}
