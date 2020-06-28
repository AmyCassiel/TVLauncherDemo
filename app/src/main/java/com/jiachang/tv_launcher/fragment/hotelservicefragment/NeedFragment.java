package com.jiachang.tv_launcher.fragment.hotelservicefragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.activity.HotelServiceActivity;
import com.jiachang.tv_launcher.adapter.HorizontalListViewAdapter;
import com.jiachang.tv_launcher.adapter.ServiceNeedTypeAdapter;
import com.jiachang.tv_launcher.bean.NeedServiceType;
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

import static com.jiachang.tv_launcher.utils.Constant.end2;
import static com.jiachang.tv_launcher.utils.Constant.end5;
import static com.jiachang.tv_launcher.utils.Constant.end6;
import static com.jiachang.tv_launcher.utils.Constant.end7;
import static com.jiachang.tv_launcher.utils.Constant.endTime1;
import static com.jiachang.tv_launcher.utils.Constant.endTime3;
import static com.jiachang.tv_launcher.utils.Constant.endTime4;
import static com.jiachang.tv_launcher.utils.Constant.start2;
import static com.jiachang.tv_launcher.utils.Constant.start5;
import static com.jiachang.tv_launcher.utils.Constant.start6;
import static com.jiachang.tv_launcher.utils.Constant.start7;
import static com.jiachang.tv_launcher.utils.Constant.startTime1;
import static com.jiachang.tv_launcher.utils.Constant.startTime3;
import static com.jiachang.tv_launcher.utils.Constant.startTime4;

/**
 * @author Mickey.Ma
 * @date 2020-05-14
 * @description
 */
public class NeedFragment extends Fragment {
    private List<NeedServiceType> service = new ArrayList<>();
    private String tag = "NeedFragment";
    private Unbinder mUnbinder;
    private HorizontalListViewAdapter hListViewAdapter;
    protected HotelServiceActivity mActivity;
    private ServiceNeedTypeAdapter adapter;
    private String[] titles, sFsNames0, sFsTimes0;
    private String[] detailsNames0, detailsNames2, detailsNames3;
    private String[] servicePath0, servicePath2, servicePath3;
    private Bitmap bitmap0, bitmap1;
    private String name0, name1;
    private ListView li;
    private int scrollPosition = -1;

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
    @BindView(R.id.t_txt)
    TextView Txt;
    @BindView(R.id.intro_need)
    LinearLayout introControl;
    @BindView(R.id.introduce_need)
    AutoLinearLayout introduceControl;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    NeedServiceType needST = new NeedServiceType(bitmap0, name0, "供应时间：9:00-10:00", "客需语句：我要" + name0);
                    service.add(needST);
                    break;
                default:
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.service_intro_need_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mActivity = (HotelServiceActivity) getContext();

        //获取宿主Activity
        if (mActivity != null) {
            li = mActivity.findViewById(R.id.select_listview);
            li.getItemsCanFocus();
            li.getId();
            li.setNextFocusRightId(R.id.horizon_listview);
        }

        Bundle bundle = this.getArguments();//得到从Activity传来的数据
        if (bundle != null) {
            titles = getSharedPreference("sTypeNames");
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

            startTime1 = bundle.getStringArray("startTime1");
            endTime1 = bundle.getStringArray("endTime1");
            startTime3 = bundle.getStringArray("startTime3");
            endTime3 = bundle.getStringArray("endTime3");
            startTime4 = bundle.getStringArray("startTime4");
            endTime4 = bundle.getStringArray("endTime4");


            String isFirst = bundle.getString("isFirst");
            if (isFirst != null) {
                horizontalListView.requestFocus();
            }
            introControl.setVisibility(View.VISIBLE);
            introduceControl.setVisibility(View.GONE);
            initUI(titles);
        }else {
            introControl.setVisibility(View.GONE);
            introduceControl.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        horizontalListView.getItemsCanFocus();
        horizontalListView.getNextFocusRightId();

    }

    private void initUI(String[] title) {
        if (!title.equals("")) {
            introControl.setVisibility(View.VISIBLE);
            introduceControl.setVisibility(View.GONE);
            horizontalListView.setVisibility(View.VISIBLE);
            contentReVi.setVisibility(View.VISIBLE);
            hListViewAdapter = new HorizontalListViewAdapter(mActivity, title);
            horizontalListView.setAdapter(hListViewAdapter);
            horizontalListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (scrollPosition != firstVisibleItem) {
//                    adapter.setSelectItem(firstVisibleItem);
//                    mLeft.setSelectionFromTop(firstVisibleItem, 40);
                        scrollPosition = firstVisibleItem;
                    }
                }
            });
            horizontalListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    adapter = new ServiceNeedTypeAdapter(mActivity, service);
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
                        if (startTime1 != null && endTime1 != null) {
                            initGoods(detailsNames0, servicePath0, startTime1, endTime1);
                        } else {
                            initGoods(detailsNames0, servicePath0, null, null);
                        }
                    } else if (titles[position].equals("客房清洁")) {
                        adapter.setDataList(service);
                        supplyTime(start2, end2);
                        Txt.setText("请对我说：我想要客房清洁");
                    } else if (title[position].equals("送餐服务")) {
                        adapter.setDataList(service);
                        recyclerGoods.setVisibility(View.VISIBLE);
                        timeShow.setVisibility(View.GONE);
                        contentReVi.setLayoutManager(layoutManager);
                        contentReVi.setAdapter(adapter);
                        contentReVi.setOnScrollListener(new OnPopRecyclerViewScrollListener());
                        initGoods(detailsNames2, servicePath2, startTime3, endTime3);
                    } else if (title[position].equals("保障服务")) {
                        adapter.setDataList(service);
                        recyclerGoods.setVisibility(View.VISIBLE);
                        timeShow.setVisibility(View.GONE);
                        contentReVi.setLayoutManager(layoutManager);
                        contentReVi.setAdapter(adapter);
                        contentReVi.setOnScrollListener(new OnPopRecyclerViewScrollListener());
                        initGoods(detailsNames3, servicePath3, startTime4, endTime4);
                    } else if (title[position].equals("退房服务")) {
                        adapter.setDataList(service);
                        supplyTime(start5, end5);
                        Txt.setText("请对我说：我想要退房服务");
                    } else if (title[position].equals("续住服务")) {
                        adapter.setDataList(service);
                        supplyTime(start6, end6);
                        Txt.setText("请对我说：我想要续住服务");
                    } else if (title[position].equals("洗衣服务")) {
                        adapter.setDataList(service);
                        supplyTime(start7, end7);
                        Txt.setText("请对我说：我要洗衣服务");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else {
            horizontalListView.setVisibility(View.GONE);
            contentReVi.setVisibility(View.GONE);
            Toast.makeText(getContext().getApplicationContext(), "酒店暂时不提供该服务", Toast.LENGTH_LONG).show();
        }
    }

    private void supplyTime(long startTime, long endTime) {
        if (startTime != 0 && endTime != 0) {
            CharSequence sTimeStr = DateFormat.format("HH:mm", startTime);
            CharSequence eTimeStr = DateFormat.format("HH:mm", endTime);
            recyclerGoods.setVisibility(View.GONE);
            timeShow.setVisibility(View.VISIBLE);
            textView.setText(sTimeStr + "-" + eTimeStr);
        } else {
            timeShow.setVisibility(View.GONE);
            Toast.makeText(mActivity.getApplicationContext(), "酒店暂时不提供该服务", Toast.LENGTH_LONG).show();
        }
    }

    public String[] getSharedPreference(String key) {
        String regularEx = "#";
        SharedPreferences sp = mActivity.getSharedPreferences("hotel", Context.MODE_PRIVATE);
        String values = sp.getString(key, "");
        return values.split(regularEx);
    }

    private void initGoods(String[] detailsNames, String[] detailsImage, String[] startTime, String[] endTime) {
        for (int i = 0; i < detailsNames.length; i++) {
            String path = detailsImage[i];
            if (path != null) {
                name1 = detailsNames[i];
                bitmap1 = ImageUtil.returnBitmap(path);
                if (name1 != null && bitmap1 != null && startTime != null && endTime != null) {
                    long start = Long.parseLong(startTime[i]);
                    long end = Long.parseLong(endTime[i]);
                    CharSequence sTimeStr = DateFormat.format("HH:mm", start);
                    CharSequence eTimeStr = DateFormat.format("HH:mm", end);
                    NeedServiceType needST = new NeedServiceType(bitmap1, name1, "供应时间：" + sTimeStr + "-" + eTimeStr, "例如：我想要" + name1);
                    service.add(needST);
                } else {
                    NeedServiceType needST = new NeedServiceType(bitmap1, name1, "全天供应", "例如：我想要" + name1);
                    service.add(needST);
                }
            } else {
                contentReVi.setVisibility(View.GONE);
                Toast.makeText(getContext().getApplicationContext(), "酒店暂时不提供该服务", Toast.LENGTH_LONG).show();
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
    }
}
