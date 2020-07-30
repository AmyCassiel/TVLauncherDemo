package com.jiachang.tv_launcher.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.hjq.toast.ToastUtils;
import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.adapter.SControlScenesAdapter;
import com.jiachang.tv_launcher.adapter.SControlDevicesAdapter;
import com.jiachang.tv_launcher.adapter.ServiceNeedGoodsAdapter;
import com.jiachang.tv_launcher.bean.BaseUrlBean;
import com.jiachang.tv_launcher.bean.ControlDevicesBean;
import com.jiachang.tv_launcher.bean.ControlScenesBean;
import com.jiachang.tv_launcher.utils.ApiRetrofit;
import com.jiachang.tv_launcher.utils.Constant;
import com.jiachang.tv_launcher.utils.DialogUtil;
import com.jiachang.tv_launcher.utils.HttpUtils;
import com.jiachang.tv_launcher.utils.ToastUtil;
import com.zhy.autolayout.AutoLinearLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.jiachang.tv_launcher.utils.ApiRetrofit.initRetrofit;

/**
 * @author Mickey.Ma
 * @date 2020-05-18
 * @description 客控界面，显示数据
 */
public class ControlActivity extends Activity implements SControlDevicesAdapter.onItemClickListener {
    private String Tag = "ControlActivity";
    private Context mActivity;
    private SControlDevicesAdapter sControlDevicesAdapter;
    private SControlScenesAdapter sControlScenesAdapter;
    private final List<ControlDevicesBean> controlDevicesBeanList = new ArrayList<>();
    private final List<ControlScenesBean> controlScenesBeanList = new ArrayList<>();
    private String url, hicToken;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_intro_control_fragment);
        ButterKnife.bind(this);
        mActivity = ControlActivity.this;

        sControlScenesAdapter = new SControlScenesAdapter(controlScenesBeanList);
        StaggeredGridLayoutManager layoutManager1 = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        introduceControl1.setLayoutManager(layoutManager1);
        introduceControl1.setAdapter(sControlScenesAdapter);


        sControlDevicesAdapter = new SControlDevicesAdapter(controlDevicesBeanList);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL);
        introduceControl.setLayoutManager(layoutManager);
        introduceControl.setAdapter(sControlDevicesAdapter);
        sControlDevicesAdapter.setItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getToken();
    }

    private void getToken() {
        DialogUtil.start(mActivity, "正在加载房间数据...");
        initRetrofit(Constant.hostUrl).queryDevice(Constant.MAC)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBody -> {
                    try {
                        String body = responseBody.string();
                        JSONObject object = JSONObject.parseObject(body);
                        int code = object.getIntValue("code");
                        if (code == 0) {
                            introduceControlJIA.setVisibility(View.GONE);
                            url = object.getString("url");
                            hicToken = object.getString("token");
                            getBaseUrl();
                        } else {
                            DialogUtil.finish();
                            introduceControlJIA.setVisibility(View.VISIBLE);
                            ToastUtils.show("获取失败，请检查是否已绑定网关后重试！");
                        }
                    } catch (IOException e) {
                        DialogUtil.finish();
                        introduceControlJIA.setVisibility(View.VISIBLE);
                        e.printStackTrace();
                    }
                }, throwable -> {
                    DialogUtil.finish();
                    introduceControlJIA.setVisibility(View.VISIBLE);
                    Log.e(Tag, "错误：" + throwable.getMessage());
                    ToastUtils.show("获取失败，请重试");
                });
    }

    /*
     * 检测网关是否在线
     * */
    private void getBaseUrl() {
        initRetrofit("https://c.jia.mn")
                .getBaseUrl("/App/a/frame/setLogin.php?hictoken=" + hicToken, "getdturl")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseUrlBean>() {
                    @Override
                    public void call(BaseUrlBean baseUrlBean) {
                        getData(url, hicToken);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        System.out.println(throwable.getMessage());
                        DialogUtil.finish();
                        introControl.setVisibility(View.GONE);
                        introduceControlJIA.setVisibility(View.VISIBLE);
                        ToastUtil.makeText(mActivity, "网络异常", 1000).show();
                    }
                });
    }

    private void getData(String url, String hictoken) {
        sControlDevicesAdapter.setItemSelectedListener(this);
        initRetrofit("https://c.jia.mn").getAllDevice(url + "/home/status.php", hictoken, "getDevListJson")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody responseBody) {
                        DialogUtil.finish();
                        try {
                            String response = responseBody.string();
                            if (!response.isEmpty()) {
                                if (!response.contains("logout")) {
                                    introControl.setVisibility(View.VISIBLE);
                                    introduceControlJIA.setVisibility(View.GONE);
                                    Map responseMap = (Map) JSONObject.parse(response, Feature.OrderedField);
                                    Map pageMap = (Map) responseMap.get("page");
                                    Set set = pageMap.keySet();
                                    Iterator iterator = set.iterator();
                                    while (iterator.hasNext()) {
                                        String key = iterator.next().toString();
                                        Map map1 = (Map) pageMap.get(key);
                                        Map attrMap = (Map) map1.get("attr");
                                        String type = attrMap.get("SYSNAME").toString();
                                        switch (type) {
                                            case "qj":
                                                myRoom1.setVisibility(View.VISIBLE);
                                                String name = attrMap.get("NAME").toString();
                                                Map value = (Map) map1.get("value");
                                                String Isgroup = value.get("ISGROUP").toString();
                                                boolean ISGROUP = Boolean.getBoolean(Isgroup);
                                                if (ISGROUP) {
                                                    ControlScenesBean scenesBean = new ControlScenesBean(name, R.mipmap.qj);
                                                    controlScenesBeanList.add(scenesBean);
                                                } else {
                                                    ControlScenesBean controltype1 = new ControlScenesBean(name, R.mipmap.qj);
                                                    controlScenesBeanList.add(controltype1);
                                                }
                                                break;
                                            case "cl1":
                                                String nam = attrMap.get("NAME").toString();
                                                String devId = attrMap.get("ID").toString();
                                                int valu = (int) map1.get("value");
                                                if (valu != 0) {
                                                    ControlDevicesBean controltype = new ControlDevicesBean("cl1", nam, "状态：打开", valu, devId, R.mipmap.cl_1);
                                                    controlDevicesBeanList.add(controltype);
                                                } else {
                                                    ControlDevicesBean controltype = new ControlDevicesBean("cl1", nam, "状态：关闭", valu, devId, R.mipmap.cl);
                                                    controlDevicesBeanList.add(controltype);
                                                }
                                                break;
                                            case "kg":
                                                String na = attrMap.get("NAME").toString();
                                                String deviId = attrMap.get("ID").toString();
                                                int val = (int) map1.get("value");
                                                if (val != 0) {
                                                    ControlDevicesBean controltype = new ControlDevicesBean("kg", na, "状态：打开", val, deviId, R.mipmap.light_1);
                                                    controlDevicesBeanList.add(controltype);
                                                } else {
                                                    ControlDevicesBean controltype = new ControlDevicesBean("kg", na, "状态：关闭", val, deviId, R.mipmap.light);
                                                    controlDevicesBeanList.add(controltype);
                                                }
                                                break;
                                            case "color":
                                                String n = attrMap.get("NAME").toString();
                                                String devicId = attrMap.get("ID").toString();
                                                Map va = (Map) map1.get("value");
                                                int m = (int) va.get("m");
                                                if (m != -1) {
                                                    ControlDevicesBean controltype = new ControlDevicesBean("color", n, "状态：打开", 0, devicId, R.mipmap.light_1);
                                                    controlDevicesBeanList.add(controltype);
                                                } else {
                                                    ControlDevicesBean controltype = new ControlDevicesBean("color", n, "状态：关闭", -1, devicId, R.mipmap.light);
                                                    controlDevicesBeanList.add(controltype);
                                                }
                                                break;
                                            case "kt":
                                                String names = attrMap.get("NAME").toString();
                                                String deviceId = attrMap.get("ID").toString();
                                                Map values = (Map) map1.get("value");
                                                String open = values.get("open").toString();
                                                int v = Integer.parseInt(open);
                                                if (v == 0) {
                                                    ControlDevicesBean controltype = new ControlDevicesBean("kt", names, "状态：打开", v, deviceId, R.mipmap.kt_1);
                                                    controlDevicesBeanList.add(controltype);
                                                } else {
                                                    ControlDevicesBean controltype = new ControlDevicesBean("kt", names, "状态：关闭", v, deviceId, R.mipmap.kt);
                                                    controlDevicesBeanList.add(controltype);
                                                }
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                } else {
                                    Toast.makeText(mActivity, "您的网关离线了，请检查网关！", Toast.LENGTH_LONG).show();
                                    introControl.setVisibility(View.GONE);
                                    introduceControlJIA.setVisibility(View.VISIBLE);
                                }
                            } else {
                                introControl.setVisibility(View.GONE);
                                introduceControlJIA.setVisibility(View.VISIBLE);
                                Toast.makeText(mActivity, "抱歉，酒店暂时不提供该服务", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        DialogUtil.finish();
                        Log.e(Tag, "错误，" + throwable.getMessage());
                        ToastUtil.makeText(mActivity, "错误", 1000).show();
                    }
                });
    }

    private void setControlDevices(int devId, int state) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("rs", "execAttr");
                    map.put("rsargs[]", devId);
                    map.put("rsargs[m]", state);
                    System.out.println("map=" + map);
                    String controlUrl = url + "/devattr/devattr.php?hictoken=" + hicToken;
                    String responseBody = HttpUtils.mPost(controlUrl, map);
                    System.out.println(responseBody);
                    if (!responseBody.isEmpty()) {
                        Log.d("ControlActivity", responseBody);
                        boolean body = Boolean.parseBoolean(responseBody);
                        if (body) {
                            ToastUtils.show("控制成功！");
                            Log.d("ControlActivity", "___________________");
                        } else {
                            ToastUtils.show("控制失败！");
                            Log.e("ControlActivity", "控制失败");
                            Log.d("ControlActivity", "___________________");
                        }

                    } else {
                        Log.e("ControlActivity", "报错了");
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        }.start();
    }
    private void setControlLight(int devId, int state) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("rs", "execAttr");
                    map.put("rsargs[]", devId);
                    map.put("rsargs[1][m]", state);
                    System.out.println("map=" + map);
                    String controlUrl = url + "/devattr/devattr.php?hictoken=" + hicToken;
                    String responseBody = HttpUtils.mPost(controlUrl, map);
                    System.out.println(responseBody);
                    if (!responseBody.isEmpty()) {
                        Log.d("ControlActivity", responseBody);
                        boolean body = Boolean.parseBoolean(responseBody);
                        if (body) {
                            ToastUtils.show("控制成功！");
                            Log.d("ControlActivity", "___________________");
                        } else {
                            ToastUtils.show("控制失败！");
                            Log.e("ControlActivity", "控制失败");
                            Log.d("ControlActivity", "___________________");
                        }

                    } else {
                        Log.e("ControlActivity", "报错了");
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        }.start();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (controlDevicesBeanList.size()>0){
            sControlDevicesAdapter.setDataList(controlDevicesBeanList);
            sControlScenesAdapter.setDataList(controlScenesBeanList);
        }
    }

    @Override
    public void onItemClick(int position, View v, ControlDevicesBean devicesBean) {
        int stateCode = devicesBean.getStateCode();
        Log.d("ControlActivity", "stateCode = " + stateCode);
        int devId = Integer.parseInt(devicesBean.getDevId());
        Log.d("ControlActivity", "devId = " + devId);
        switch (devicesBean.getType()) {
            case "kg":
                if (stateCode == 0) {
                    setControlDevices(devId, 1);
                    Log.d("ControlActivity", "kg打开");
                    devicesBean.setState("状态：打开");
                    devicesBean.setImage(R.mipmap.light_1);
                    devicesBean.setStateCode(1);
                    sControlDevicesAdapter.notifyItemChanged(position);
                } else {
                    setControlDevices(devId, 0);
                    Log.d("ControlActivity", "kg关闭");
                    devicesBean.setState("状态：关闭");
                    devicesBean.setImage(R.mipmap.light);
                    devicesBean.setStateCode(0);
                    sControlDevicesAdapter.notifyItemChanged(position);
                }
                break;
            case "color":
                Log.d("ControlActivity", "stateCode = " + stateCode);
                if (stateCode != -1) {
                    setControlLight(devId, -1);
                    Log.d("ControlActivity", "color关闭");
                    devicesBean.setState("状态：关闭");
                    devicesBean.setImage(R.mipmap.light);
                    devicesBean.setStateCode(-1);
                    sControlDevicesAdapter.notifyItemChanged(position);
                } else {
                    setControlLight(devId, 0);
                    Log.d("ControlActivity", "color打开");
                    devicesBean.setState("状态：打开");
                    devicesBean.setImage(R.mipmap.light_1);
                    devicesBean.setStateCode(0);
                    sControlDevicesAdapter.notifyItemChanged(position);
                }
                break;
            case "cl1":
                if (stateCode == 0) {
                    setControlDevices(devId, 10);
                    Log.d("ControlActivity", "cl1打开");
                    devicesBean.setState("状态：打开");
                    devicesBean.setImage(R.mipmap.cl_1);
                    devicesBean.setStateCode(10);
                    sControlDevicesAdapter.notifyItemChanged(position);
                } else {
                    setControlDevices(devId, 0);
                    Log.d("ControlActivity", "cl1关闭");
                    devicesBean.setState("状态：关闭");
                    devicesBean.setImage(R.mipmap.cl);
                    devicesBean.setStateCode(0);
                    sControlDevicesAdapter.notifyItemChanged(position);
                }
                break;
            default:
                break;
        }


    }
}
