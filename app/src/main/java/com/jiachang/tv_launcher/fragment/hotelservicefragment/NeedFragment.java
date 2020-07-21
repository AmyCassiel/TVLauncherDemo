package com.jiachang.tv_launcher.fragment.hotelservicefragment;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hjq.toast.ToastUtils;
import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.activity.HotelServiceActivity;
import com.jiachang.tv_launcher.activity.MainActivity;
import com.jiachang.tv_launcher.adapter.ServiceNeedGoodsAdapter;
import com.jiachang.tv_launcher.adapter.ServiceNeedListViewAdapter;
import com.jiachang.tv_launcher.bean.HotelInfoBean;
import com.jiachang.tv_launcher.bean.NeedGoodsBean;
import com.jiachang.tv_launcher.bean.NeedServiceBean;
import com.jiachang.tv_launcher.utils.ApiRetrofit;
import com.jiachang.tv_launcher.utils.Constant;
import com.jiachang.tv_launcher.utils.ImageUtil;
import com.zhy.autolayout.AutoLinearLayout;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * @author Mickey.Ma
 * @date 2020-05-14
 * @description
 */
public class NeedFragment extends Fragment implements ServiceNeedGoodsAdapter.OnItemSelectedListener {
    private final List<NeedServiceBean> service = new ArrayList<>();
    public static final List<NeedGoodsBean> needGoodsBeansList = new ArrayList<>();
    public static ArrayList<String> list = new ArrayList<>();
    private final String TAG = "NeedFragment";
    private Unbinder mUnbinder;
    protected HotelServiceActivity mActivity;
    private ServiceNeedGoodsAdapter needAdapter;
    private  List<HotelInfoBean.HotelDbBean.ServiceConfsBean> serviceConfs = Constant.serviceConfs;
    private ArrayList<Integer> idArrayList1 = new ArrayList<>();
    private ArrayList<Integer> idArrayList3 = new ArrayList<>();
    private ArrayList<Integer> idArrayList4 = new ArrayList<>();
    private ArrayList<Float> priceArrayList3= new ArrayList<>();
    private ArrayList<String> nameArrayList1 = new ArrayList<>();
    private ArrayList<String> nameArrayList3 = new ArrayList<>();
    private ArrayList<String> nameArrayList4 = new ArrayList<>();
    private ArrayList<String> imageArrayList1 = new ArrayList<>();
    private ArrayList<String> imageArrayList3 = new ArrayList<>();
    private ArrayList<String> imageArrayList4 = new ArrayList<>();
    private ArrayList<String> sTypeTime1 = new ArrayList<>();
    private ArrayList<String> eTypeTime1 = new ArrayList<>();
    private ArrayList<String> sTypeTime3 = new ArrayList<>();
    private ArrayList<String> eTypeTime3 = new ArrayList<>();
    private ArrayList<String> sTypeTime4 = new ArrayList<>();
    private ArrayList<String> eTypeTime4 = new ArrayList<>();
    private Bundle bundle;
    private NeedServiceBean needServiceType;
    private int scrollPosition = -1;
    private final ArrayList<String> sTypeNames = new ArrayList<>();
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
    @BindView(R.id.ok_confirm)
    Button okConfirm;
    @BindView(R.id.ok_cancel)
    Button okCancel;

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            int what = msg.what;
            switch (what) {
                case 1:
                    needAdapter = new ServiceNeedGoodsAdapter(mActivity, service);
                    needAdapter.notifyDataSetChanged();
                    contentReVi.setAdapter(needAdapter);
                    initUI(sTypeNames);
                default:
                    break;
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
            ListView listView = mActivity.findViewById(R.id.select_listview);
            listView.getItemsCanFocus();
            listView.getId();
            listView.setNextFocusRightId(R.id.horizon_listview);
        }
        contentReVi.setOnKeyListener(backlistener);
        bundle = this.getArguments();//得到从Activity传来的数据

        initView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (serviceConfs!=null){
            getData();
        }else {
            introControl.setVisibility(View.GONE);
            introduceControl.setVisibility(View.VISIBLE);
            Toast.makeText(mActivity, "酒店暂时不提供该服务", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        horizontalListView.getItemsCanFocus();
        horizontalListView.getNextFocusRightId();
    }

    private void getData() {
        int typeSize = serviceConfs.size();
        for (int i = 0; i < typeSize; i++) {
            Constant.sTypeName = serviceConfs.get(i).getServiceType().getServiceTypeName();
            Constant.sTypeId = serviceConfs.get(i).getServiceType().getId();
            sTypeNames.add(Constant.sTypeName);
            List<HotelInfoBean.HotelDbBean.ServiceConfsBean.ServiceDetailsBean> serviceDetails = serviceConfs.get(i).getServiceDetails();
            int id = serviceConfs.get(i).getServiceType().getId();
            //补充服务
            if (id == 1) {
                int size = serviceDetails.size();
                idArrayList1 = new ArrayList<>();
                for (int d = 0; d < size; d++) {
                    int sDetailsId = serviceDetails.get(d).getId();
                    Constant.sDetailsName = serviceDetails.get(d).getNeedName();
                    Constant.sDetailsImage = serviceDetails.get(d).getNeedImage();
                    idArrayList1.add(sDetailsId);
                    nameArrayList1.add(Constant.sDetailsName);
                    imageArrayList1.add(Constant.sDetailsImage);
                    long supplyStartTime = serviceDetails.get(d).getSupplyStartTime();
                    long supplyEndTime = serviceDetails.get(d).getSupplyEndTime();
                    sTypeTime1.add(String.valueOf(supplyStartTime));
                    eTypeTime1.add(String.valueOf(supplyEndTime));
                }
            }
            //客房清洁
            if (id == 2) {
                Constant.start2 = serviceConfs.get(i).getWaiterStartTime();
                Constant.end2 = serviceConfs.get(i).getWaiterEndTime();
            }
            //送餐服务
            if (id == 3) {
                int size = serviceDetails.size();
                for (int d = 0; d < size; d++) {
                    int sDetailsId = serviceDetails.get(d).getId();
                    idArrayList3.add(sDetailsId);
                    Constant.sDetailsName = serviceDetails.get(d).getNeedName();
                    Constant.sDetailsImage = serviceDetails.get(d).getNeedImage();
                    Constant.foodPrice = serviceDetails.get(d).getPrice();
                    nameArrayList3.add(Constant.sDetailsName);
                    imageArrayList3.add(Constant.sDetailsImage);
                    priceArrayList3.add(Constant.foodPrice);
                    long supplyStartTime = serviceDetails.get(d).getSupplyStartTime();
                    long supplyEndTime = serviceDetails.get(d).getSupplyEndTime();
                    sTypeTime3.add(String.valueOf(supplyStartTime));
                    eTypeTime3.add(String.valueOf(supplyEndTime));
                }
            }
            //保障服务
            if (id == 4) {
                int size = serviceDetails.size();
                for (int d = 0; d < size; d++) {
                    int sDetailsId = serviceDetails.get(d).getId();
                    Constant.sDetailsName = serviceDetails.get(d).getNeedName();
                    Constant.sDetailsImage = serviceDetails.get(d).getNeedImage();
                    idArrayList4.add(sDetailsId);
                    nameArrayList4.add(Constant.sDetailsName);
                    imageArrayList4.add(Constant.sDetailsImage);
                    long supplyStartTime = serviceDetails.get(d).getSupplyStartTime();
                    long supplyEndTime = serviceDetails.get(d).getSupplyEndTime();
                    sTypeTime4.add(String.valueOf(supplyStartTime));
                    eTypeTime4.add(String.valueOf(supplyEndTime));
                }
            }
            //退房服务
            if (id == 5) {
                Constant.start5 = serviceConfs.get(i).getWaiterStartTime();
                Constant.end5 = serviceConfs.get(i).getWaiterEndTime();
            }
            //续住服务
            if (id == 6) {
                Constant.start6 = serviceConfs.get(i).getWaiterStartTime();
                Constant.end6 = serviceConfs.get(i).getWaiterEndTime();
            }
            //洗衣服务
            if (id == 7) {
                Constant.start7 = serviceConfs.get(i).getWaiterStartTime();
                Constant.end7 = serviceConfs.get(i).getWaiterEndTime();
            }
        }
        if (bundle != null) {
            String isFirst = bundle.getString("isFirst");
            if (isFirst != null) {
                horizontalListView.requestFocus();
            }
        } else {
            introControl.setVisibility(View.GONE);
            introduceControl.setVisibility(View.VISIBLE);
        }

        if (!sTypeNames.isEmpty()) {
            introControl.setVisibility(View.VISIBLE);
            introduceControl.setVisibility(View.GONE);
            initUI(sTypeNames);
        } else {
            introControl.setVisibility(View.GONE);
            introduceControl.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        okConfirm.setOnClickListener(v -> {
            initDatas();
            upload();
        });
        okCancel.setOnClickListener(v -> {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        });
    }

    //初始化数据
    private ArrayList<String> initData() {
        for (int i = 0; i < needGoodsBeansList.size(); i++) {
            NeedGoodsBean bean = needGoodsBeansList.get(i);
            String goodsName = bean.getGoodsName();
            if (bean.isExist()) {
                int amount = bean.getAmount();
                String string = goodsName + " * " + amount + "；";
                list.add(string);
            }
        }
        return list;
    }

    //初始化数据
    private List<NeedGoodsBean> initDatas() {
        for (int i = 0; i < needGoodsBeansList.size(); i++) {
            NeedGoodsBean bean = needGoodsBeansList.get(i);
            int serId = bean.getSerDetailId();
            Log.i(TAG, "serId===" + serId);
            if (bean.isExist()) {
                int amount = 0;
                amount++;
                bean.setAmount(amount);
                Log.i(TAG, "amount===" + amount);
            }

        }
        return needGoodsBeansList;
    }

    private void upload() {
        list = initData();
        if (list.size() == 0) {
            Toast.makeText(mActivity, "您未选中任何物品！", Toast.LENGTH_SHORT).show();
            return;
        }
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(LAYOUT_INFLATER_SERVICE);
        View Alertlayout = inflater.inflate(R.layout.alertdialog_listview, null);
        ListView myAlertListView = Alertlayout.findViewById(R.id.mylistview);
        myAlertListView.setFocusable(false);
        MyAlertDialogAdapter myAdapter = new MyAlertDialogAdapter(mActivity, list);
        myAlertListView.setAdapter(myAdapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, R.style.AlertDialogStyle);
        builder.setTitle("提示");
        double money = 0;
        for (int i = 0; i < needGoodsBeansList.size(); i++) {
            int typeId = needGoodsBeansList.get(i).getServiceTypeId();
            int goodsAmount = needGoodsBeansList.get(i).getAmount();
            double goodsPrice = needGoodsBeansList.get(i).getPrice();
            if (typeId == 4) {
                builder.setMessage("您确定要将以下物品损坏，并通知到前台吗？");
            } else if (typeId == 1) {
                builder.setMessage("您确定以下产品缺失，并通知前台吗？");
            } else if (typeId == 3) {
                double num = goodsAmount * goodsPrice;
                money += num;
                builder.setMessage("以下菜品共计" + money + "元，将在您房券中扣除，您确定要下单吗？");
            }
        }
        builder.setView(Alertlayout);
        builder.setCancelable(false);
        builder.setPositiveButton("确定", (dialog, which) -> {
            if (needServiceType.getAmount() == 0) {
                ToastUtils.show("操作失败，请检查物品是否为空！");
                return;
            }
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(needGoodsBeansList));
            ApiRetrofit.initRetrofit(Constant.hostUrl)
                    .sendGoods(Constant.MAC, body)
                    .subscribeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(responseBody -> {
                        try {
                            String body1 = responseBody.string();
                            JSONObject object = JSONObject.parseObject(body1);
                            int code = object.getIntValue("code");
                            if (code == 0) {
                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage(message);
                                ToastUtils.show("操作成功！");
                            } else {
                                ToastUtils.show("操作失败，请检查后重试！");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }, throwable -> {
                        ToastUtils.show("操作失败，请联系前台！");
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                        Log.e(TAG, Objects.requireNonNull(throwable.getMessage()));
                    });
        }).setNegativeButton("取消", (dialog, which) -> {
            needGoodsBeansList.clear();
            list.clear();
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.setInverseBackgroundForced(true);
        dialog.show();
    }

    private void initUI(ArrayList<String> title) {
        if (title != null) {
            ServiceNeedListViewAdapter hListViewAdapter = new ServiceNeedListViewAdapter(mActivity, title);
            horizontalListView.setAdapter(hListViewAdapter);
            needAdapter = new ServiceNeedGoodsAdapter(mActivity, service);
            needAdapter.setItemSelectedListener(this);
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
            contentReVi.setLayoutManager(layoutManager);
            contentReVi.setOnScrollListener(new OnPopRecyclerViewScrollListener());
            contentReVi.setAdapter(needAdapter);

            horizontalListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    //默认选中第一个
                    if (scrollPosition != firstVisibleItem) {
                        scrollPosition = firstVisibleItem;
                    }
                }
            });
            horizontalListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    contentReVi.setNextFocusLeftId(R.id.horizon_listview);
                    switch (sTypeNames.get(position)){
                        case "补充服务":
                            if (sTypeTime1 != null && eTypeTime1 != null) {
                                initGoods(1, idArrayList1, nameArrayList1, null, imageArrayList1, sTypeTime1, eTypeTime1);
                            } else {
                                initGoods(1, idArrayList1, nameArrayList1, null, imageArrayList1, null, null);
                            }
                            break;
                        case "客房清洁":
                            needAdapter.setDataList(service);
                            supplyTime(Constant.start2, Constant.end2);
                            Txt.setText("请对我说：我要客房清洁");
                            break;
                        case "送餐服务":
                            initGoods(3, idArrayList3, nameArrayList3, priceArrayList3,
                                    imageArrayList3, sTypeTime3, eTypeTime3);
                            break;
                        case "保障服务":
                            initGoods(4, idArrayList4, nameArrayList4, null,
                                    imageArrayList4, sTypeTime4, eTypeTime4);
                            break;
                        case "退房服务":
                            needAdapter.setDataList(service);
                            supplyTime(Constant.start5, Constant.end5);
                            Txt.setText("请对我说：我要退房");
                            break;
                        case "续住服务":
                            needAdapter.setDataList(service);
                            supplyTime(Constant.start6, Constant.end6);
                            Txt.setText("请对我说：我要续住");
                            break;
                        case "洗衣服务":
                            needAdapter.setDataList(service);
                            supplyTime(Constant.start7, Constant.end7);
                            Txt.setText("请对我说：我要洗衣服");
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else {
            introControl.setVisibility(View.GONE);
            introduceControl.setVisibility(View.VISIBLE);
            Toast.makeText(mActivity, "酒店暂时不提供该服务", Toast.LENGTH_LONG).show();
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


    private void initGoods(int typeId, ArrayList<Integer> idArrayList, ArrayList<String> detailsNames, ArrayList<Float> priceArrayList, ArrayList<String> detailsImage, ArrayList<String> startTime, ArrayList<String> endTime) {
        recyclerGoods.setVisibility(View.VISIBLE);
        timeShow.setVisibility(View.GONE);
        needAdapter.setDataList(service);
        if (idArrayList.size() <= 0) {
            introControl.setVisibility(View.GONE);
            introduceControl.setVisibility(View.VISIBLE);
        } else {
            for (int i = 0; i < idArrayList.size(); i++) {
                int goodsId = idArrayList.get(i);
                final int count = 0;
                final float price;
                if (priceArrayList != null) {
                    price = priceArrayList.get(i);
                } else {
                    price = 0;
                }
                String path = detailsImage.get(i);
                if (path != null) {
                    String name = detailsNames.get(i);
                    Bitmap bitmap = ImageUtil.returnBitmap(path);
                    if (name != null && bitmap != null && startTime != null && endTime != null) {
                        long start = Long.parseLong(startTime.get(i));
                        long end = Long.parseLong(endTime.get(i));
                        CharSequence sTimeStr = DateFormat.format("HH:mm", start);
                        CharSequence eTimeStr = DateFormat.format("HH:mm", end);
                        NeedServiceBean needST = new NeedServiceBean(typeId, goodsId, bitmap, name, "¥" + price, "供应时间：" + sTimeStr + "-" + eTimeStr, count);
                        service.add(needST);
                    } else {
                        NeedServiceBean needST = new NeedServiceBean(typeId, goodsId, bitmap, name, "¥" + price, "全天供应", count);
                        service.add(needST);
                    }
                } else {
                    recyclerGoods.setVisibility(View.GONE);
                    Toast.makeText(mActivity, "酒店暂时不提供该服务", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void OnItemSelectedListener(int position, View v, NeedServiceBean serviceType) {
        this.needServiceType = serviceType;
        serviceType.setAmount(serviceType.getAmount() + 1);
        final ServiceNeedGoodsAdapter.ViewHolder holder = new ServiceNeedGoodsAdapter.ViewHolder(v);
        holder.serviceAmount.setText("数量：x" + serviceType.getAmount());
        addList(serviceType);
    }

    private List<NeedGoodsBean> addList(NeedServiceBean serviceType) {
        List<NeedGoodsBean> needGoodsBeans = new ArrayList<>();
        int goodsId = serviceType.getId();
        int serviceTypeId = serviceType.getTypeId();
        String goodsPrice = serviceType.getPrice();
        String goodsName = serviceType.getName();
        int amount = serviceType.getAmount();
        NeedGoodsBean goods;
        if (!goodsPrice.isEmpty()) {
            String transPrice = goodsPrice.replace("¥", "");
            float price = Float.parseFloat(transPrice);
            goods = new NeedGoodsBean(MainActivity.hotelId, Constant.roomNum, goodsId, amount, serviceTypeId, price);
        } else {
            goods = new NeedGoodsBean(MainActivity.hotelId, Constant.roomNum, goodsId, amount, serviceTypeId, 0);
        }
        goods.setExist(true);
        goods.setGoodsName(goodsName);
        needGoodsBeans.add(goods);
        needGoodsBeansList.add(goods);
        return needGoodsBeans;
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
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        needGoodsBeansList.clear();
        service.clear();
        if (serviceConfs != null){
            serviceConfs.clear();
        }

    }

    static class MyAlertDialogAdapter extends BaseAdapter {
        public final ArrayList<String> stringArrayList;
        public final Context mContext;

        public MyAlertDialogAdapter(Context context, ArrayList<String> list) {
            this.mContext = context;
            this.stringArrayList = list;
        }

        @Override
        public int getCount() {
            return stringArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return stringArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Goods goods = null;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(R.layout.alertdialog_item, null);
                goods = new Goods();
                goods.goodsName = convertView.findViewById(R.id.tv_name);
                goods.goodsName.setFocusable(false);
                convertView.setFocusable(false);
                convertView.setTag(goods);
            } else {
                goods = (Goods) convertView.getTag();
            }
            goods.goodsName.setText(stringArrayList.get(position));
            return convertView;
        }

        static class Goods {
            TextView goodsName;
        }

    }

    private final View.OnKeyListener backlistener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                //判断RecyclerView是否得到焦点
                if (contentReVi.hasFocus()) {
                    okConfirm.setFocusable(true);
                    okConfirm.requestFocus();
                    /*if (service.size() > 0) {
                        //得到item的数量
                        int l = service.size() - 1;
                        //抛出控制值是因为 findViewByPosition方法判断的是还未加载到的item
                        try {
                            if (contentReVi.getLayoutManager().findViewByPosition(i).hasFocus()) {

                                return true;
                            }
                        } catch (Exception e) {
                        }
                    }*/
                }
            }
            return false;
        }
    };
}
