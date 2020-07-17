package com.jiachang.tv_launcher.fragment.hotelservicefragment;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.hjq.toast.ToastUtils;
import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.activity.HotelServiceActivity;
import com.jiachang.tv_launcher.adapter.SControlScenesAdapter;
import com.jiachang.tv_launcher.adapter.SControlDevicesAdapter;
import com.jiachang.tv_launcher.bean.ControlDevicesBean;
import com.jiachang.tv_launcher.bean.ControlScenesBean;
import com.jiachang.tv_launcher.utils.ApiRetrofit;
import com.jiachang.tv_launcher.utils.Constant;
import com.jiachang.tv_launcher.utils.HttpUtils;
import com.jiachang.tv_launcher.utils.LogUtils;
import com.zhy.autolayout.AutoLinearLayout;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
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
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author Mickey.Ma
 * @date 2020-05-18
 * @description 客控界面，显示数据
 */
public class ControlFragment extends Fragment {
    private String Tag = "ControlFragment";
    private Unbinder mUnbinder;
    private HotelServiceActivity mActivity;
    private SControlDevicesAdapter sControlDevicesAdapter;
    private SControlScenesAdapter sControlScenesAdapter;
    private List<ControlDevicesBean> controlDevicesBeanList = new ArrayList<>();
    private List<ControlScenesBean> controlScenesBeanList = new ArrayList<>();
    private ListView listView;
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

        sControlScenesAdapter = new SControlScenesAdapter(mActivity, controlScenesBeanList);
        StaggeredGridLayoutManager layoutManager1 = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        introduceControl1.setLayoutManager(layoutManager1);
        introduceControl1.setAdapter(sControlScenesAdapter);

        sControlDevicesAdapter = new SControlDevicesAdapter(mActivity, controlDevicesBeanList);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL);
        introduceControl.setLayoutManager(layoutManager);
        introduceControl.setAdapter(sControlDevicesAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mActivity != null) {
            listView = mActivity.findViewById(R.id.select_listview);
            listView.getItemsCanFocus();
            listView.getId();
            listView.setNextFocusRightId(R.id.contentrecyclerview1);
            listView.setNextFocusRightId(R.id.contentrecyclerview);
        }
        Bundle bundle = this.getArguments();
        if (bundle != null){
            String isFirst = bundle.getString("isFirst");
            if (isFirst != null){
                introduceControl.requestFocus();
                introduceControl1.requestFocus();
            }
        }
        getToken();
    }

    private void getToken() {
        ApiRetrofit.initRetrofit(Constant.hostUrl).queryDevice(Constant.MAC)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody responseBody) {
                        try {
                            String body = responseBody.string();
                            JSONObject object = JSONObject.parseObject(body);
                            int code = object.getIntValue("code");
                            if (code == 0) {
                                String url = object.getString("url");
                                String token = object.getString("token");
                                getData(url, token);
                            } else {
                                ToastUtils.show("获取token失败，请检查后重试！");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("ControlFragment", "错误：" + throwable.getMessage());
                        ToastUtils.show("获取失败，请重试");
                    }
                });
    }

    private void getData(String url, String hictoken) {
        String url1 = url + "/home/status.php?hictoken=" + hictoken;
        Map map = new LinkedHashMap();
        map.put("rs", "getDevListJson");
        try {
            String request = HttpUtils.mPost(url1, map);
            LogUtils.d("ControlFragment.95", "request = " + request);
            if (!request.isEmpty()) {
                if (!request.contains("logout")) {
                    introControl.setVisibility(View.VISIBLE);
                    introduceControlJIA.setVisibility(View.GONE);
                    Map responseMap = (Map) JSONObject.parse(request, Feature.OrderedField);
                    Map pageMap = (Map) responseMap.get("page");
                    Set set = pageMap.keySet();
                    Iterator iterator = set.iterator();
                    while (iterator.hasNext()) {
                        String key = iterator.next().toString();
                        Map map1 = (Map) pageMap.get(key);
                        Map attrMap = (Map) map1.get("attr");
                        String type = attrMap.get("SYSNAME").toString();
                        if (type.equals("qj")) {
                            myRoom1.setVisibility(View.VISIBLE);
                            String name = attrMap.get("NAME").toString();
                            Map value = (Map) map1.get("value");
                            boolean ISGROUP = (boolean) value.get("ISGROUP");
                            if (ISGROUP) {
                                ControlScenesBean scenesBean = new ControlScenesBean(name, R.mipmap.qj);
                                controlScenesBeanList.add(scenesBean);
                            } else {
                                ControlScenesBean controltype1 = new ControlScenesBean(name, R.mipmap.qj);
                                controlScenesBeanList.add(controltype1);
                            }
                        }
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
                } else {
                    Toast.makeText(getContext(), "您的网关离线了，请检查网关！", Toast.LENGTH_LONG).show();
                    introControl.setVisibility(View.GONE);
                    introduceControlJIA.setVisibility(View.VISIBLE);
                }
            } else {
                introControl.setVisibility(View.GONE);
                introduceControlJIA.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "抱歉，酒店暂时不提供该服务", Toast.LENGTH_LONG).show();
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
