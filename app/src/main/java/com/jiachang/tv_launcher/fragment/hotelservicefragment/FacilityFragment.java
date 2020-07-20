package com.jiachang.tv_launcher.fragment.hotelservicefragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
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
import com.jiachang.tv_launcher.bean.HotelInfoBean;
import com.jiachang.tv_launcher.utils.Constant;
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
    @BindView(R.id.fcontent_recycler_view)
    RecyclerView fconRV;
    @BindView(R.id.intro_facility)
    LinearLayout introlFacility;
    @BindView(R.id.introduce_facility)
    AutoLinearLayout introduceFacility;

    private final List<FacilityGoodsBean> service = new ArrayList<>();
    private Unbinder mUnbinder;
    private HotelServiceActivity mActivity;
    private SFacTypeAdapter adapter;
    private ArrayList<String> sFsNames = new ArrayList<>();
    private ArrayList<String> sFsImgs = new ArrayList<>();
    private ArrayList<String> sFsTimes = new ArrayList<>();
    private ArrayList<String> sFsLocals = new ArrayList<>();
    private List<HotelInfoBean.HotelDbBean.HotelFacilitiesBean> hotelFacilities = Constant.hotelFacilities;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

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
        Log.i(tag,"onActivityCreated");
        //获取宿主Activity
        if (mActivity != null) {
            ListView listView = mActivity.findViewById(R.id.select_listview);
            listView.getItemsCanFocus();
            listView.getId();
            listView.setNextFocusRightId(R.id.fcontent_recycler_view);
        }
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String isFirst = bundle.getString("isFirst");
            if (isFirst != null) {
                fconRV.requestFocus();
            }
        }

        if (!hotelFacilities.isEmpty()) {
            int fsize = hotelFacilities.size();
            for (int j = 0; j < fsize; j++) {
                Constant.sFacilitiesName = hotelFacilities.get(j).getName();
                Constant.sFacilitiesImg = hotelFacilities.get(j).getImage();
                Constant.sFacilitiesTime = hotelFacilities.get(j).getTime();
                Constant.sFacilitiesLocation = hotelFacilities.get(j).getLocation();
                sFsNames.add(Constant.sFacilitiesName);
                sFsImgs.add(Constant.sFacilitiesImg);
                sFsTimes.add(Constant.sFacilitiesTime);
                sFsLocals.add(Constant.sFacilitiesLocation);
            }
        }

        if (sFsNames != null && !sFsNames.isEmpty()) {
            initGood(sFsImgs, sFsNames, sFsTimes, sFsLocals);
        } else {
            introlFacility.setVisibility(View.GONE);
            introduceFacility.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        adapter = new SFacTypeAdapter(service);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        fconRV.setLayoutManager(layoutManager);
        fconRV.setAdapter(adapter);
        fconRV.setOnScrollListener(new OnPopRecyclerViewScrollListener());
    }

    private void initGood(ArrayList<String> detailsImage, ArrayList<String> detailsNames, ArrayList<String> detailsTime, ArrayList<String> detailsLocal) {
        adapter.setDataList(service);
        for (int i = 0; i < detailsNames.size(); i++) {
            String path = detailsImage.get(i);
            Bitmap bitmap = ImageUtil.returnBitmap(path);
            String name = detailsNames.get(i);
            String supplyTime = detailsTime.get(i);
            String local = detailsLocal.get(i);
            if (!name.isEmpty()) {
                introlFacility.setVisibility(View.VISIBLE);
                introduceFacility.setVisibility(View.GONE);
                FacilityGoodsBean duck = new FacilityGoodsBean(name, local, "开放时间：" + supplyTime, bitmap);
                service.add(duck);
            } else {
                introlFacility.setVisibility(View.GONE);
                introduceFacility.setVisibility(View.VISIBLE);
                Toast.makeText(mActivity, "酒店暂时不提供该服务", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        adapter.setDataList(service);
        Log.i(tag,"onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        service.clear();
        hotelFacilities.clear();
        Log.i(tag,"onDestroy");
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
