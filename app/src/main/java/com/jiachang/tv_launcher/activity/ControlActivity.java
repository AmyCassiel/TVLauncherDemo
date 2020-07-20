package com.jiachang.tv_launcher.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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
import com.jiachang.tv_launcher.bean.BaseUrlBean;
import com.jiachang.tv_launcher.bean.ControlDevicesBean;
import com.jiachang.tv_launcher.bean.ControlScenesBean;
import com.jiachang.tv_launcher.utils.Constant;
import com.jiachang.tv_launcher.utils.DialogUtil;
import com.jiachang.tv_launcher.utils.HttpUtils;
import com.jiachang.tv_launcher.utils.LogUtils;
import com.jiachang.tv_launcher.utils.ToastUtil;
import com.zhy.autolayout.AutoLinearLayout;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
public class ControlActivity extends Activity {
    private String Tag = "ControlActivity";
    private Context mActivity;
    private SControlDevicesAdapter sControlDevicesAdapter;
    private SControlScenesAdapter sControlScenesAdapter;
    private final List<ControlDevicesBean> controlDevicesBeanList = new ArrayList<>();
    private final List<ControlScenesBean> controlScenesBeanList = new ArrayList<>();

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        getToken();
    }

    private void getToken() {
        DialogUtil.start(mActivity,"正在加载房间数据...");
        initRetrofit(Constant.hostUrl).queryDevice(Constant.MAC)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBody -> {
                    try {
                        String body = responseBody.string();
                        JSONObject object = JSONObject.parseObject(body);
                        int code = object.getIntValue("code");
                        if (code == 0) {
                            String url = object.getString("url");
                            String token = object.getString("token");
                            getBaseUrl(token,url);
                        } else {
                            ToastUtils.show("获取token失败，请检查后重试！");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, throwable -> {
                    Log.e(Tag, "错误：" + throwable.getMessage());
                    ToastUtils.show("获取失败，请重试");
                });
    }

    /*
     * 检测网关是否在线
     * */
    private void getBaseUrl(final String token,String url) {
        initRetrofit("https://c.jia.mn")
                .getBaseUrl("/App/a/frame/setLogin.php?hictoken="+token,"getdturl")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseUrlBean>() {
                    @Override
                    public void call(BaseUrlBean baseUrlBean) {
                        getData(url, token);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        DialogUtil.finish();
                        ToastUtil.makeText(mActivity,"网络异常",1000).show();
                    }
                });
    }

    private void getData(String url, String hictoken) {
         initRetrofit("https://c.jia.mn").getAllDevice(url+"/home/status.php",hictoken,"getDevListJson")
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(new Action1<ResponseBody>() {
                     @Override
                     public void call(ResponseBody responseBody) {
                         DialogUtil.finish();
                         try {
                             String request = responseBody.string();
                             if (!request.isEmpty()){
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
                                         switch (type){
                                             case "qj":
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
                                                 break;
                                             case "cl1":
                                                 String nam = attrMap.get("NAME").toString();
                                                 int valu = (int) map1.get("value");
                                                 if (valu != 0) {
                                                     ControlDevicesBean controltype = new ControlDevicesBean(nam, "状态：打开", R.mipmap.cl_1);
                                                     controlDevicesBeanList.add(controltype);
                                                 } else {
                                                     ControlDevicesBean controltype = new ControlDevicesBean(nam, "状态：关闭", R.mipmap.cl);
                                                     controlDevicesBeanList.add(controltype);
                                                 }
                                                 break;
                                             case "kg":
                                                 String na = attrMap.get("NAME").toString();
                                                 int val = (int) map1.get("value");
                                                 if (val != 0) {
                                                     ControlDevicesBean controltype = new ControlDevicesBean(na, "状态：打开", R.mipmap.light_1);
                                                     controlDevicesBeanList.add(controltype);
                                                 } else {
                                                     ControlDevicesBean controltype = new ControlDevicesBean(na, "状态：关闭", R.mipmap.light);
                                                     controlDevicesBeanList.add(controltype);
                                                 }
                                                 break;
                                             case "color":
                                                 String n = attrMap.get("NAME").toString();
                                                 Map va = (Map) map1.get("value");
                                                 int m = (int) va.get("m");
                                                 if (m != -1) {
                                                     ControlDevicesBean controltype = new ControlDevicesBean(n, "状态：打开", R.mipmap.light_1);
                                                     controlDevicesBeanList.add(controltype);
                                                 } else {
                                                     ControlDevicesBean controltype = new ControlDevicesBean(n, "状态：关闭", R.mipmap.light);
                                                     controlDevicesBeanList.add(controltype);
                                                 }
                                                 break;
                                             case "kt":
                                                 String names = attrMap.get("NAME").toString();
                                                 Map values = (Map) map1.get("value");
                                                 String open = values.get("open").toString();
                                                 if (open.equals("0")) {
                                                     ControlDevicesBean controltype = new ControlDevicesBean(names, "状态：打开", R.mipmap.kt_1);
                                                     controlDevicesBeanList.add(controltype);
                                                 } else {
                                                     ControlDevicesBean controltype = new ControlDevicesBean(names, "状态：关闭", R.mipmap.kt);
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
                         }catch (Exception e){
                             e.printStackTrace();
                         }

                     }
                 }, new Action1<Throwable>() {
                     @Override
                     public void call(Throwable throwable) {
                         DialogUtil.finish();
                         Log.e(Tag,"错误，"+throwable.getMessage());
                         ToastUtil.makeText(mActivity,"cuowu",1000).show();
                     }
                 });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
