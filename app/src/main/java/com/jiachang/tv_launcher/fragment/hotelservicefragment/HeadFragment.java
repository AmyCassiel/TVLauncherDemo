package com.jiachang.tv_launcher.fragment.hotelservicefragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.activity.HotelServiceActivity;
import com.jiachang.tv_launcher.adapter.HorizontalListViewAdapter;
import com.jiachang.tv_launcher.adapter.ServiceTypeAdapter;
import com.jiachang.tv_launcher.bean.Food;
import com.jiachang.tv_launcher.bean.ServiceType;
import com.jiachang.tv_launcher.utils.ImageUtil;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import static com.jiachang.tv_launcher.utils.Constants.end2;
import static com.jiachang.tv_launcher.utils.Constants.end5;
import static com.jiachang.tv_launcher.utils.Constants.end6;
import static com.jiachang.tv_launcher.utils.Constants.end7;
import static com.jiachang.tv_launcher.utils.Constants.start2;
import static com.jiachang.tv_launcher.utils.Constants.start5;
import static com.jiachang.tv_launcher.utils.Constants.start6;
import static com.jiachang.tv_launcher.utils.Constants.start7;

/**
 * @author Mickey.Ma
 * @date 2020-05-14
 * @description
 */
public class HeadFragment extends Fragment {
    private List<ServiceType> service = new ArrayList<>();
    private String tag = "HeadFragment";
    private Unbinder mUnbinder;
    private HorizontalListViewAdapter hListViewAdapter;
    protected HotelServiceActivity mActivity;
    private ServiceTypeAdapter adapter;

    @BindView(R.id.recycler_goods)
    RelativeLayout recyclerGoods;
    @BindView(R.id.horizon_listview)
    ListView horizontalListView;
    @BindView(R.id.content_recycler_view)
    RecyclerView contentReVi;
    @BindView(R.id.time_show)
    LinearLayout timeShow;
    @BindView(R.id.time_text)
    TextView textView;
    private String[] titles,  sFsNames0, sFsTimes0;
    private String[] detailsNames0, detailsNames2,detailsNames3;
    private String[] servicePath0, servicePath2,servicePath3;
    private Bitmap bitmap0, bitmap1;
    private String name0, name1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    ServiceType duck = new ServiceType(name0, "供应时间：9:00-10:00", bitmap0);
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

        //获取宿主Activity
        if (mActivity != null) {
            ListView li = mActivity.findViewById(R.id.select_listview);
            li.getItemsCanFocus();
            li.getId();
            li.getNextFocusRightId();
            li.setNextFocusRightId(R.id.horizon_listview);
        }

        Bundle bundle = this.getArguments();//得到从Activity传来的数据
        if (bundle != null) {
            titles = getSharedPreference("sTypeNames");
            Log.e(tag,"titles = "+titles);
            detailsNames0 = bundle.getStringArray("detailsNames0");
            servicePath0 = bundle.getStringArray("sDetailsImage0");
            detailsNames2 = bundle.getStringArray("detailsNames2");
            servicePath2 = bundle.getStringArray("sDetailsImage2");
            detailsNames3 = bundle.getStringArray("detailsNames3");
            servicePath3 = bundle.getStringArray("sDetailsImage3");
            start2 = bundle.getLong("start2");
            start5 = bundle.getLong("start5");
            start6 = bundle.getLong("start6");
            start7 = bundle.getLong("start7");
            end2 = bundle.getLong("end2");
            end5 = bundle.getLong("end5");
            end6 = bundle.getLong("end6");
            end7 = bundle.getLong("end7");
            sFsNames0 = bundle.getStringArray("sFsNames0");
            sFsTimes0 = bundle.getStringArray("sFsTimes0");

        }
        initUI(titles);
        return view;
    }
    /**/
    private void initUI(String[] title) {
        hListViewAdapter = new HorizontalListViewAdapter(mActivity, title);
        horizontalListView.setAdapter(hListViewAdapter);
        horizontalListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adapter = new ServiceTypeAdapter(mActivity, service);
                StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
                contentReVi.setLayoutManager(layoutManager);
                contentReVi.setAdapter(adapter);
                contentReVi.setOnScrollListener(new OnPopRecyclerViewScrollListener());

                if (titles[position].equals("补充服务")) {
                    adapter.setDataList(service);
                    recyclerGoods.setVisibility(View.VISIBLE);
                    timeShow.setVisibility(View.GONE);
                    contentReVi.setLayoutManager(layoutManager);
                    contentReVi.setAdapter(adapter);
                    contentReVi.setOnScrollListener(new OnPopRecyclerViewScrollListener());
                    initGoods(detailsNames0, servicePath0);
                    Log.i("", "------------------------------------------------------------------------------------------------");
                } else if (titles[position].equals("客房清洁")) {
                    adapter.setDataList(service);
                    supplyTime(start2,end2);
                }else if (title[position].equals("送餐服务")){
                    adapter.setDataList(service);
                    recyclerGoods.setVisibility(View.VISIBLE);
                    timeShow.setVisibility(View.GONE);
                    contentReVi.setLayoutManager(layoutManager);
                    contentReVi.setAdapter(adapter);
                    contentReVi.setOnScrollListener(new OnPopRecyclerViewScrollListener());
                    initGoods(detailsNames2, servicePath2);
                }else if (title[position].equals("保障服务")){
                    adapter.setDataList(service);
                    recyclerGoods.setVisibility(View.VISIBLE);
                    timeShow.setVisibility(View.GONE);
                    contentReVi.setLayoutManager(layoutManager);
                    contentReVi.setAdapter(adapter);
                    contentReVi.setOnScrollListener(new OnPopRecyclerViewScrollListener());
                    initGoods(detailsNames3, servicePath3);
                }else if (title[position].equals("退房服务")){
                    adapter.setDataList(service);
                    supplyTime(start5,end5);
                }else if (title[position].equals("续住服务")){
                    adapter.setDataList(service);
                    supplyTime(start6,end6);
                }else if (title[position].equals("洗衣服务")){
                    adapter.setDataList(service);
                    supplyTime(start7,end7);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void supplyTime(long startTime,long endTime){
        CharSequence sTimeStr = DateFormat.format("HH:mm", startTime);
        CharSequence eTimeStr = DateFormat.format("HH:mm", endTime);
        recyclerGoods.setVisibility(View.GONE);
        timeShow.setVisibility(View.VISIBLE);
        textView.setText(sTimeStr+"-"+eTimeStr);
    }

    public String[] getSharedPreference(String key) {
        String regularEx = "#";
        SharedPreferences sp = mActivity.getSharedPreferences("hotel", Context.MODE_PRIVATE);
        String values = sp.getString(key, "");
        return values.split(regularEx);
    }

    private void initGoods(String[] detailsNames, String[] detailsImage) {
        for (int i = 0; i < detailsNames.length; i++) {
            String path = detailsImage[i];
            if (path != null){
                name1 = detailsNames[i];
                bitmap1 = ImageUtil.returnBitmap(path);
                if (name1 != null && bitmap1 != null){
                    ServiceType ducks = new ServiceType(name1, "供应时间：9:00-10:00", bitmap1);
                    service.add(ducks);
                }
            }

        }
    }

    private void initGood(String[] detailsNames, String[] detailsImage) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                for (int i = 0; i < detailsNames.length; i++) {
                    String path = detailsImage[i];
                    bitmap0 = ImageUtil.returnBitmap(path);
                    name0 = detailsNames[i];
                    Message msg = new Message();
                    msg.what = 0;
                    msg.obj = name0;
                    msg.obj = bitmap0;
                    handler.sendMessage(msg);
                }
            }
        }.start();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
//        bitmap.recycle();
    }
}
