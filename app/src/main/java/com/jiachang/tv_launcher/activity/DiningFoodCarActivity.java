package com.jiachang.tv_launcher.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.adapter.FoodCarAdapter;
import com.jiachang.tv_launcher.bean.FoodIntentBean;
import com.jiachang.tv_launcher.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnFocusChange;


/**
 * @author Mickey.Ma
 * @date 2020-03-26
 * @description  食物详情页
 */
public class DiningFoodCarActivity extends Activity implements FoodCarAdapter.onItemClickListener {
    private String TAG = "FoodCarActivity";
    private List<FoodIntentBean> foodList = new ArrayList<FoodIntentBean>();
    @BindView(R.id.recyclerLisCar)
    RecyclerView carList;
    @BindView(R.id.gopay)
    TextView goPay;
    @BindView(R.id.tv_altogether)
    TextView tvAltogether;
    @OnFocusChange(R.id.gopay)
    void OnFocus(View view, boolean isFocus) {
        ViewUtils.sView(view, isFocus);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomMenu();
        setContentView(R.layout.dining_fooddetail_activity);
        ButterKnife.bind(this);

        //获取前一页的数据
        foodList = DiningFoodListActivity.foodItems;

        FoodCarAdapter foodCarAdapter = new FoodCarAdapter(this, foodList);
//        foodCarAdapter.setItemClickListener((FoodCarAdapter.onItemClickListener) this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        carList.setLayoutManager(layoutManager);
        carList.setAdapter(foodCarAdapter);
        carList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        getData();
    }

    private void getData() {
        Iterator<FoodIntentBean> iterator = foodList.iterator();
        while (iterator.hasNext()) {
            FoodIntentBean food = (FoodIntentBean) iterator.next();
            List<Float> pr = new ArrayList<>();
            float price = 0;
            for (int i = 0; i < foodList.size(); i++){
                String foodPrice = food.getFoodPrice();
                int count = food.getFoodCount();
                price = Float.valueOf(foodPrice) * count;
                pr.add(price);
            }
            tvAltogether.setText("总计："+price+"元");
//            tvAltogether.
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideBottomMenu();
    }

    protected void hideBottomMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    public void onItemClick(int position, View v, FoodIntentBean food) {
        if(food.getFoodCount() >0){
            food.setFoodCount(food.getFoodCount()-1);
        }
    }
}
