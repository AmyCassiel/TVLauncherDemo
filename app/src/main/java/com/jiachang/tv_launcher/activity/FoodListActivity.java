package com.jiachang.tv_launcher.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.adapter.FoodAdapter;
import com.jiachang.tv_launcher.bean.Food;
import com.jiachang.tv_launcher.utils.ImageUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Mickey.Ma
 * @date 2020-03-26
 * @description 食物列表界面
 */
public class FoodListActivity extends Activity implements FoodAdapter.onItemClickListener {
    private List<Food> foodList = new ArrayList<>();
    private String foodName;
    private String foodPrice;
    private String foodPath;
    private ImageView imageView;
    private ArrayList arrayList = new ArrayList();
    private Context context = this;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private long EndTime;
    private long StartTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomMenu();
        setContentView(R.layout.dining_foodlist_layout);
        ButterKnife.bind(this);

        arrayList = getIntent().getStringArrayListExtra("foodlist");

        initFood();

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        FoodAdapter adapter = new FoodAdapter(this, foodList);
        recyclerView.setLayoutManager(layoutManager);
        adapter.setItemClickListener(this);//给适配器添加点击事件，回调处理
        recyclerView.setAdapter(adapter);
        recyclerView.setOnScrollListener(new OnPopRecyclerViewScrollListener());
    }

    @Override
    public void onItemClick(int position, View v, Food food) {
        Intent intent = new Intent();
        intent.setClass(this, FoodDetailsActivity.class);
        intent.putExtra("image", food.getImageId());
        intent.putExtra("name", food.getName());
        intent.putExtra("price", food.getPrice());
        intent.putExtra("ingredient", food.getSupplyTime());
        imageView = v.findViewById(R.id.food_image);

        ActivityOptions options = ActivityOptions.makeScaleUpAnimation(
                imageView, 0, 0, //拉伸开始的坐标
                imageView.getMeasuredWidth(), imageView.getMeasuredHeight()); // 初始的宽高
        startActivity(intent, options.toBundle());
    }

    private void initFood() {
        try {
            Iterator<Map> iterator = arrayList.iterator();
            while (iterator.hasNext()) {
                Map string = (Map) iterator.next();
                JSONObject dataJson = new JSONObject(string);
                for (int i = 0; i < arrayList.size(); i++) {
                    foodName = dataJson.getString("needName");
                    foodPath = dataJson.getString("needImage");
                    foodPrice = dataJson.getString("price");
                    StartTime = dataJson.getLong("supplyStartTime");
                    EndTime = dataJson.getLong("supplyEndTime");
                    Log.d("FoodListActivity","supplyStartTime ="+StartTime+", supplyEndTime ="+EndTime);
                }
                Bitmap bitmap = ImageUtil.returnBitmap(foodPath);
                CharSequence sTimeStr = DateFormat.format("HH:mm", StartTime);
                CharSequence eTimeStr = DateFormat.format("HH:mm", EndTime);
                Log.d("","sTimeStr = "+sTimeStr);
                long sysTime = System.currentTimeMillis();
                CharSequence nowTimeStr = DateFormat.format("HH:mm", sysTime);
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                    long start = simpleDateFormat.parse((String) sTimeStr).getTime();
                    long end = simpleDateFormat.parse((String) eTimeStr).getTime();
                    long now = simpleDateFormat.parse((String) nowTimeStr).getTime();
                    if (start <= now && end >= now) { //使用时间戳做判断
                        Food duck = new Food(foodName, "¥" + foodPrice,
                                "\n供应时间段：" + sTimeStr + "-" + eTimeStr, bitmap);
                        foodList.add(duck);
                    }else {
                        Toast.makeText(context,"抱歉，当前时间不在部分食物的供应时间段内",Toast.LENGTH_LONG).show();
                    }
                } catch (ParseException p) {
                    p.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void hideBottomMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //在recyclerView的move事件情况下，拦截调，只让它响应五向键和左右箭头移动
        return ev.getAction() == MotionEvent.ACTION_MOVE || super.dispatchTouchEvent(ev);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
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
