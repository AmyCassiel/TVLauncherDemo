package com.jiachang.tv_launcher.fragment.hotelservicefragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.activity.HotelServiceActivity;
import com.jiachang.tv_launcher.adapter.SControlTypeAdapter;
import com.jiachang.tv_launcher.adapter.SControlTypeAdapter1;
import com.jiachang.tv_launcher.bean.Controltype;
import com.jiachang.tv_launcher.bean.Controltype1;
import com.jiachang.tv_launcher.utils.HttpUtils;
import com.jiachang.tv_launcher.utils.LogUtils;

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
 * @description
 */
public class ControlFragment extends Fragment {
    private Unbinder mUnbinder;
    private HotelServiceActivity mActivity;
    private SControlTypeAdapter adapter;
    private SControlTypeAdapter1 adapter1;

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

    private List<Controltype> service = new ArrayList<>();
    private List<Controltype1> service1 = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.service_intro_control_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mActivity = (HotelServiceActivity) getContext();

        adapter1 = new SControlTypeAdapter1(mActivity,service1);
        StaggeredGridLayoutManager layoutManager1 = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        introduceControl1.setLayoutManager(layoutManager1);
        introduceControl1.setAdapter(adapter1);

        adapter = new SControlTypeAdapter(mActivity,service);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(8, StaggeredGridLayoutManager.VERTICAL);
        introduceControl.setLayoutManager(layoutManager);
        introduceControl.setAdapter(adapter);
        /*Bundle bundle = this.getArguments();
        if (bundle != null){
            String isFirst = bundle.getString("isFirst");
            if (isFirst != null){
                introduceControl.requestFocus();
                introduceControl1.requestFocus();
            }
        }*/

        getData();
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
                    if (type.equals("cl1")){
                        String name = attrMap.get("NAME").toString();
                        int value =  (int)map1.get("value");
                        if (value != 0){
                            Controltype controltype = new Controltype(name,"状态：打开" ,R.mipmap.cl_1);
                            service.add(controltype);
                        }else {
                            Controltype controltype = new Controltype(name, "状态：关闭",R.mipmap.cl);
                            service.add(controltype);
                        }
                    }
                    if (type.equals("kg")){
                        String name = attrMap.get("NAME").toString();
                        int value =  (int)map1.get("value");
                        if (value != 0){
                            Controltype controltype = new Controltype(name, "状态：打开", R.mipmap.light_1);
                            service.add(controltype);
                        }else {
                            Controltype controltype = new Controltype(name, "状态：关闭", R.mipmap.light);
                            service.add(controltype);
                        }
                    }
                    if (type.equals("color")){
                        String name = attrMap.get("NAME").toString();
                        Map value = (Map) map1.get("value");
                        int m = (int) value.get("m");
                        if (m != -1){
                            Controltype controltype = new Controltype(name, "状态：打开", R.mipmap.light_1);
                            service.add(controltype);
                        }else {
                            Controltype controltype = new Controltype(name, "状态：关闭", R.mipmap.light);
                            service.add(controltype);
                        }
                    }
                    if (type.equals("qj")){
                        String name = attrMap.get("NAME").toString();
                        Map value = (Map) map1.get("value");
                        boolean ISGROUP = (boolean) value.get("ISGROUP");
                        if (ISGROUP){
                            Controltype1 controltype1 = new Controltype1(name, R.mipmap.qj);
                            service1.add(controltype1);
                        }else {
                            Controltype1 controltype1 = new Controltype1(name, R.mipmap.qj);
                            service1.add(controltype1);
                        }
                    }
                    if (type.equals("kt")){
                        String name = attrMap.get("NAME").toString();
                        Map value = (Map) map1.get("value");
                        String open =  value.get("open").toString();
                        if (open.equals("0") ){
                            Controltype controltype = new Controltype(name,"状态：打开", R.mipmap.kt_1);
                            service.add(controltype);
                        }else {
                            Controltype controltype = new Controltype(name, "状态：关闭",R.mipmap.kt);
                            service.add(controltype);
                        }
                    }
                }
            }else {
                myRoom0.setVisibility(View.GONE);
                myRoom1.setVisibility(View.GONE);
                myRoom2.setVisibility(View.GONE);
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
        adapter.setDataList(service);
        adapter1.setDataList(service1);
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
