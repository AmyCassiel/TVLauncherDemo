package com.jiachang.tv_launcher.fragment.hotelservicefragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Toast;

import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.activity.HotelServiceActivity;
import com.jiachang.tv_launcher.adapter.SFacTypeAdapter;
import com.jiachang.tv_launcher.adapter.ServiceNeedTypeAdapter;
import com.jiachang.tv_launcher.bean.FacType;
import com.jiachang.tv_launcher.bean.NeedServiceType;
import com.jiachang.tv_launcher.utils.ImageUtil;
import com.jiachang.tv_launcher.utils.ViewUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnFocusChange;
import butterknife.Unbinder;

/**
 * @author Mickey.Ma
 * @date 2020-05-18
 * @description
 */
public class FacilityFragment extends Fragment {
    private String tag = "FacilityFragment";

    @BindView(R.id.fcontent_recycler_view)
    RecyclerView fconRV;

    private List<FacType> service = new ArrayList<>();
    private Unbinder mUnbinder;
    private HotelServiceActivity mActivity;
    private SFacTypeAdapter adapter;
    private String[] sFsNames0, sFsTimes0, sFsImgs0, sFsLocals0;
    private Bitmap bitmap0;
    private String name0, local, supplyTime;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    FacType duck = new FacType(name0, local, "开放时间：" + supplyTime, bitmap0);
                    service.add(duck);
                    break;
                default:
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.service_facility_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mActivity = (HotelServiceActivity) getContext();

        initView();
        return view;
    }

    private void initView() {
        adapter = new SFacTypeAdapter(mActivity, service);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        fconRV.setLayoutManager(layoutManager);
        fconRV.setAdapter(adapter);
        fconRV.setOnScrollListener(new OnPopRecyclerViewScrollListener());
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            sFsImgs0 = bundle.getStringArray("sFsImgs0");
            sFsNames0 = bundle.getStringArray("sFsNames0");
            sFsTimes0 = bundle.getStringArray("sFsTimes0");
            sFsLocals0 = bundle.getStringArray("sFsLocals0");

            String isFirst = bundle.getString("isFirst");
            if (isFirst != null) {
                fconRV.requestFocus();
            }

            initGood(sFsImgs0, sFsNames0, sFsTimes0, sFsLocals0);
        }
    }

    private void initGood(String[] detailsImage, String[] detailsNames, String[] detailsTime, String[] detailsLocal) {
        for (int i = 0; i < detailsNames.length; i++) {
            String path = detailsImage[i];
            bitmap0 = ImageUtil.returnBitmap(path);
            name0 = detailsNames[i];
            supplyTime = detailsTime[i];
            local = detailsLocal[i];
            if(!name0.isEmpty()){
                fconRV.setVisibility(View.VISIBLE);
                FacType duck = new FacType(name0, local, "开放时间：" + supplyTime, bitmap0);
                service.add(duck);
            }else {
                fconRV.setVisibility(View.GONE);
                Toast.makeText(getActivity().getApplicationContext(), "酒店暂时不提供该服务", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.setDataList(service);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    private static class OnPopRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    }
}
