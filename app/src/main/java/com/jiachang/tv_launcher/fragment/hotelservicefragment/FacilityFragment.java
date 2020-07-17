package com.jiachang.tv_launcher.fragment.hotelservicefragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.activity.HotelServiceActivity;
import com.jiachang.tv_launcher.adapter.SFacTypeAdapter;
import com.jiachang.tv_launcher.bean.FacilityGoodsBean;
import com.jiachang.tv_launcher.utils.ImageUtil;
import com.zhy.autolayout.AutoLinearLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Mickey.Ma
 * @date 2020-05-18
 * @description
 */
public class FacilityFragment extends Fragment {
    private String tag = "FacilityFragment";
    private ListView listView;
    @BindView(R.id.fcontent_recycler_view)
    RecyclerView fconRV;
    @BindView(R.id.intro_facility)
    LinearLayout introlFacility;
    @BindView(R.id.introduce_facility)
    AutoLinearLayout introduceFacility;

    private List<FacilityGoodsBean> service = new ArrayList<>();
    private Unbinder mUnbinder;
    private HotelServiceActivity mActivity;
    private SFacTypeAdapter adapter;
    private String[] sFsNames0, sFsTimes0, sFsImgs0, sFsLocals0;
    private Bitmap bitmap0;
    private String name0, local, supplyTime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.service_facility_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mActivity = (HotelServiceActivity) getContext();
        initView();
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //获取宿主Activity
        if (mActivity != null) {
            listView = mActivity.findViewById(R.id.select_listview);
            listView.getItemsCanFocus();
            listView.getId();
            listView.setNextFocusRightId(R.id.fcontent_recycler_view);
        }
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
    private void initView() {
        adapter = new SFacTypeAdapter(mActivity, service);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        fconRV.setLayoutManager(layoutManager);
        fconRV.setAdapter(adapter);
        fconRV.setOnScrollListener(new OnPopRecyclerViewScrollListener());
    }

    private void initGood(String[] detailsImage, String[] detailsNames, String[] detailsTime, String[] detailsLocal) {
        for (int i = 0; i < detailsNames.length; i++) {
            String path = detailsImage[i];
            bitmap0 = ImageUtil.returnBitmap(path);
            name0 = detailsNames[i];
            supplyTime = detailsTime[i];
            local = detailsLocal[i];
            if(!name0.isEmpty()){
                introlFacility.setVisibility(View.VISIBLE);
                introduceFacility.setVisibility(View.GONE);
                FacilityGoodsBean duck = new FacilityGoodsBean(name0, local, "开放时间：" + supplyTime, bitmap0);
                service.add(duck);
            }else {
                introlFacility.setVisibility(View.GONE);
                introduceFacility.setVisibility(View.VISIBLE);
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
