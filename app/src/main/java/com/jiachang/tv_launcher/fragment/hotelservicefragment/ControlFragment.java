package com.jiachang.tv_launcher.fragment.hotelservicefragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.activity.HotelServiceActivity;
import com.jiachang.tv_launcher.adapter.SControlScenesAdapter;
import com.jiachang.tv_launcher.adapter.SControlDevicesAdapter;
import com.jiachang.tv_launcher.bean.ControlDevicesBean;
import com.jiachang.tv_launcher.bean.ControlScenesBean;
import com.jiachang.tv_launcher.utils.HttpUtils;
import com.jiachang.tv_launcher.utils.LogUtils;
import com.zhy.autolayout.AutoLinearLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
 * @description  客控界面，显示数据
 */
public class ControlFragment extends Fragment {
    private Unbinder mUnbinder;
    private HotelServiceActivity mActivity;
    private SControlDevicesAdapter sControlDevicesAdapter;
    private SControlScenesAdapter sControlScenesAdapter;
    private List<ControlDevicesBean> controlDevicesBeanList = new ArrayList<>();
    private List<ControlScenesBean> controlScenesBeanList = new ArrayList<>();

    @BindView(R.id.contentrecyclerview)
    RecyclerView introduceControl;
    @BindView(R.id.contentrecyclerview1)
    RecyclerView introduceControl1;
    @BindView(R.id.myroom0)
    LinearLayout myRoom0;
    @BindView(R.id.myroom1)
    LinearLayout myRoom1;
    @BindView(R.id.myroom2)
    LinearLayout myRoom2;
    @BindView(R.id.intro_control)
    LinearLayout introControl;
    @BindView(R.id.introduce_control)
    AutoLinearLayout introduceControlJIA;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.service_intro_control_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mActivity = (HotelServiceActivity) getContext();

        sControlScenesAdapter = new SControlScenesAdapter(mActivity,controlScenesBeanList);
        StaggeredGridLayoutManager layoutManager1 = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        introduceControl1.setLayoutManager(layoutManager1);
        introduceControl1.setAdapter(sControlScenesAdapter);

        sControlDevicesAdapter = new SControlDevicesAdapter(mActivity,controlDevicesBeanList);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL);
        introduceControl.setLayoutManager(layoutManager);
        introduceControl.setAdapter(sControlDevicesAdapter);
        /*Bundle bundle = this.getArguments();
        if (bundle != null){
            String isFirst = bundle.getString("isFirst");
            if (isFirst != null){
                introduceControl.requestFocus();
                introduceControl1.requestFocus();
            }
        }*/

//        getData();
        introControl.setVisibility(View.GONE);
        introduceControlJIA.setVisibility(View.VISIBLE);
        return view;
    }

    private void getData() {
        String url = "http://182.61.44.102:8181/App/b/home/status.php?hictoken=410108959-1560850901-1558662097-7c7bdcf7ae-715e15bde1";
        Map map = new LinkedHashMap();
        map.put("rs", "getDevListJson");
        try {
            String request = HttpUtils.mPost(url, map);
            LogUtils.d("ControlFragment.95", "request = " + request);
            if (!request.isEmpty()) {
                if (!request.contains("logout")){
                    introControl.setVisibility(View.VISIBLE);
                    introduceControlJIA.setVisibility(View.GONE);
                    myRoom0.setVisibility(View.VISIBLE);
                    myRoom1.setVisibility(View.VISIBLE);
                    myRoom2.setVisibility(View.VISIBLE);
                    Map responseMap = (Map) JSONObject.parse(request, Feature.OrderedField);
                    Map pageMap = (Map) responseMap.get("page");
                    Set set = pageMap.keySet();
                    Iterator iterator = set.iterator();
                    while (iterator.hasNext()) {
                        String key = iterator.next().toString();
                        Map map1 = (Map) pageMap.get(key);
                        Map attrMap = (Map) map1.get("attr");
                        String type = attrMap.get("SYSNAME").toString();
                        if (type.equals("cl1")) {
                            String name = attrMap.get("NAME").toString();
                            int value = (int) map1.get("value");
                            if (value != 0) {
                                ControlDevicesBean controltype = new ControlDevicesBean(name, "状态：打开", R.mipmap.cl_1);
                                controlDevicesBeanList.add(controltype);
                            } else {
                                ControlDevicesBean controltype = new ControlDevicesBean(name, "状态：关闭", R.mipmap.cl);
                                controlDevicesBeanList.add(controltype);
                            }
                        }
                        if (type.equals("kg")) {
                            String name = attrMap.get("NAME").toString();
                            int value = (int) map1.get("value");
                            if (value != 0) {
                                ControlDevicesBean controltype = new ControlDevicesBean(name, "状态：打开", R.mipmap.light_1);
                                controlDevicesBeanList.add(controltype);
                            } else {
                                ControlDevicesBean controltype = new ControlDevicesBean(name, "状态：关闭", R.mipmap.light);
                                controlDevicesBeanList.add(controltype);
                            }
                        }
                        if (type.equals("color")) {
                            String name = attrMap.get("NAME").toString();
                            Map value = (Map) map1.get("value");
                            int m = (int) value.get("m");
                            if (m != -1) {
                                ControlDevicesBean controltype = new ControlDevicesBean(name, "状态：打开", R.mipmap.light_1);
                                controlDevicesBeanList.add(controltype);
                            } else {
                                ControlDevicesBean controltype = new ControlDevicesBean(name, "状态：关闭", R.mipmap.light);
                                controlDevicesBeanList.add(controltype);
                            }
                        }
                        if (type.equals("qj")) {
                            String name = attrMap.get("NAME").toString();
                            Map value = (Map) map1.get("value");
                            boolean ISGROUP = (boolean) value.get("ISGROUP");
                            if (ISGROUP) {
                                ControlScenesBean controltype1 = new ControlScenesBean(name, R.mipmap.qj);
                                controlScenesBeanList.add(controltype1);
                            } else {
                                ControlScenesBean controltype1 = new ControlScenesBean(name, R.mipmap.qj);
                                controlScenesBeanList.add(controltype1);
                            }
                        }
                        if (type.equals("kt")) {
                            String name = attrMap.get("NAME").toString();
                            Map value = (Map) map1.get("value");
                            String open = value.get("open").toString();
                            if (open.equals("0")) {
                                ControlDevicesBean controltype = new ControlDevicesBean(name, "状态：打开", R.mipmap.kt_1);
                                controlDevicesBeanList.add(controltype);
                            } else {
                                ControlDevicesBean controltype = new ControlDevicesBean(name, "状态：关闭", R.mipmap.kt);
                                controlDevicesBeanList.add(controltype);
                            }
                        }
                    }
                }else {
                    Toast.makeText(getContext(),"您的网关离线了，请检查网关！",Toast.LENGTH_LONG).show();
                    myRoom0.setVisibility(View.GONE);
                    myRoom1.setVisibility(View.GONE);
                    myRoom2.setVisibility(View.GONE);
                    introControl.setVisibility(View.GONE);
                    introduceControlJIA.setVisibility(View.VISIBLE);
                }
            }else {
                myRoom0.setVisibility(View.GONE);
                myRoom1.setVisibility(View.GONE);
                myRoom2.setVisibility(View.GONE);
                introControl.setVisibility(View.GONE);
                introduceControlJIA.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(),"抱歉，酒店暂时不提供该服务",Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        sControlDevicesAdapter.setDataList(controlDevicesBeanList);
        sControlScenesAdapter.setDataList(controlScenesBeanList);
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
