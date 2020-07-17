package com.jiachang.tv_launcher.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.adapter.FoodAdapter;
import com.jiachang.tv_launcher.bean.FoodListBean;
import com.jiachang.tv_launcher.bean.FoodIntentBean;
import com.jiachang.tv_launcher.utils.ImageUtil;
import com.jiachang.tv_launcher.utils.ViewUtils;

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
import butterknife.OnFocusChange;

/**
 * @author Mickey.Ma
 * @date 2020-03-26
 * @description 食物列表界面
 */
public class DiningFoodListActivity extends Activity implements FoodAdapter.onItemClickListener {
    private String TAG = "FoodListActivity";
    public static List<FoodListBean> foodList = new ArrayList<>();
    public static List<FoodIntentBean> foodItems = new ArrayList<FoodIntentBean>();
    private String foodName;
    private String foodPrice;
    private String foodImg;
    private Bitmap bitmap;
    private ArrayList arrayList = new ArrayList();
    // 贝塞尔曲线中间过程点坐标
    private float[] mCurrentPosition = new float[2];
    private Context context = this;

    private int foodItemId;
    private int amount;
    private int allCount;
    private int foodId;
    private long EndTime;
    private long StartTime;

    @BindView(R.id.snack_layout)
    LinearLayout snackLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.shopping_car)
    ImageView shoppingCar;
    @BindView(R.id.shoppingcar_count_layout)
    RelativeLayout scAmountLayout;
    @BindView(R.id.tv_shopping_cart_count)
    TextView mTvShoppingCartCount;

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

        shoppingCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent();
                    intent.setClass(context, DiningFoodCarActivity.class);
                    ActivityOptions options = ActivityOptions.makeScaleUpAnimation(
                            shoppingCar, 0, 0, //拉伸开始的坐标
                            shoppingCar.getMeasuredWidth(), shoppingCar.getMeasuredHeight()); // 初始的宽高
                    startActivity(intent, options.toBundle());
                }catch (Exception e){
                    Log.e(TAG,"报错："+e.getMessage());
                }

            }
        });
    }

    @OnFocusChange(R.id.shopping_car)
    void OnFocus(View view, boolean isFocus) {
        ViewUtils.sView(view, isFocus);
    }

    @Override
    public void onItemClick(int position, View v, FoodListBean food) {
        foodItemId = food.getId();
        foodName = food.getName();
        foodPrice = food.getPrice();
        food.setCount(food.getCount() + 1);
        amount = food.getCount();
        FoodIntentBean foodItem = new FoodIntentBean(foodItemId, foodName,  foodPrice, bitmap,  amount);
        foodItems.add(foodItem);

        allCount++;
        if (allCount > 0) {
            scAmountLayout.setVisibility(View.VISIBLE);
        } else {
            scAmountLayout.setVisibility(View.GONE);
        }
        mTvShoppingCartCount.setText(String.valueOf(allCount));

        addGoods2CartAnim(v);
    }

    private void calculatePrice(List<FoodIntentBean> foodList) {
        double totalPrice = 0;
        int totalNum = 0;
        for (int i = 0; i < foodList.size(); i++) {//循环的商家
            FoodIntentBean foodBean = foodList.get(i);
            //计算价格
//            totalPrice = totalPrice + foodBean.getAmount() * foodBean.getFoodPrice();
            totalNum += foodBean.getFoodCount();//计数

        }
    }

    /**
     * 贝塞尔曲线动画
     *
     * @param view
     */
    public void addGoods2CartAnim(View view) {
        final ImageView goods = new ImageView(context);
        goods.setImageResource(R.mipmap.service_add);
        //service_add的布局参数
        int size = ViewUtils.dp2px(24);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(size, size);
        goods.setLayoutParams(lp);
        //mSnackLayout是整个页面布局id
//        snackLayout.addView(goods);
        // 控制点的位置
        int[] recyclerLocation = new int[2];
        snackLayout.getLocationInWindow(recyclerLocation);
        // 加入点的位置起始点
        int[] startLocation = new int[2];
        view.getLocationInWindow(startLocation);
        // 购物车的位置终点
        int[] endLocation = new int[2];
        //shopping_car是动画运行轨迹最终结束的地方  我这里用的是购物车的图标控件
        shoppingCar.getLocationInWindow(endLocation);
        // TODO: 考虑到状态栏的问题，不然会往下偏移状态栏的高度
        int startX = startLocation[0] - recyclerLocation[0];
        int startY = startLocation[1] - recyclerLocation[1];
        // TODO:  和上面一样
        int endX = endLocation[0] - recyclerLocation[0];
        int endY = endLocation[1] - recyclerLocation[1];
        // 开始绘制贝塞尔曲线
        Path path = new Path();
        // 移动到起始点位置(即贝塞尔曲线的起点)
        path.moveTo(startX, startY);
        // 使用二阶贝塞尔曲线：注意第一个起始坐标越大，贝塞尔曲线的横向距离就会越大，一般按照下面的式子取即可
        path.quadTo((startX + endX) / 2, startY, endX, endY);
        // mPathMeasure用来计算贝塞尔曲线的曲线长度和贝塞尔曲线中间插值的坐标，如果是true，path会形成一个闭环
        final PathMeasure pathMeasure = new PathMeasure(path, false);
        // 属性动画实现（从0到贝塞尔曲线的长度之间进行插值计算，获取中间过程的距离值）
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, pathMeasure.getLength());
        // 计算距离
        int tempX = Math.abs(startX - endX);
        int tempY = Math.abs(startY - endY);
        // 根据距离计算时间
        int time = (int) (0.3 * Math.sqrt((tempX * tempX) + tempY * tempY));
        valueAnimator.setDuration(time);
        valueAnimator.start();
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 当插值计算进行时，获取中间的每个值，
                // 这里这个值是中间过程中的曲线长度（下面根据这个值来得出中间点的坐标值）
                float value = (Float) animation.getAnimatedValue();
                // 获取当前点坐标封装到mCurrentPosition
                // boolean getPosTan(float distance, float[] pos, float[] tan) ：
                // 传入一个距离distance(0<=distance<=getLength())，然后会计算当前距离的坐标点和切线，pos会自动填充上坐标，这个方法很重要。
                // mCurrentPosition此时就是中间距离点的坐标值
                pathMeasure.getPosTan(value, mCurrentPosition, null);
                // 移动的商品图片（动画图片）的坐标设置为该中间点的坐标
                goods.setTranslationX(mCurrentPosition[0]);
                goods.setTranslationY(mCurrentPosition[1]);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 移除图片
                recyclerView.removeView(goods);
                // 购物车数量增加
                mTvShoppingCartCount.setText(String.valueOf(allCount));
            }
        });
    }

    private void initFood() {
        try {
            Iterator<Map> iterator = arrayList.iterator();
            while (iterator.hasNext()) {
                Map string = (Map) iterator.next();
                JSONObject dataJson = new JSONObject(string);
                for (int i = 0; i < arrayList.size(); i++) {
                    foodId = dataJson.getInt("id");
                    foodName = dataJson.getString("needName");
                    foodImg = dataJson.getString("needImage");
                    foodPrice = dataJson.getString("price");
                    StartTime = dataJson.getLong("supplyStartTime");
                    EndTime = dataJson.getLong("supplyEndTime");
                }
                bitmap = ImageUtil.returnBitmap(foodImg);
                CharSequence sTimeStr = DateFormat.format("HH:mm", StartTime);
                CharSequence eTimeStr = DateFormat.format("HH:mm", EndTime);
                long sysTime = System.currentTimeMillis();
                CharSequence nowTimeStr = DateFormat.format("HH:mm", sysTime);
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                    long start = simpleDateFormat.parse((String) sTimeStr).getTime();
                    long end = simpleDateFormat.parse((String) eTimeStr).getTime();
                    long now = simpleDateFormat.parse((String) nowTimeStr).getTime();
                    if (start <= now && end >= now) { //使用时间戳做判断
                        FoodListBean duck = new FoodListBean(foodId, foodName, "¥" + foodPrice,
                                bitmap, amount);
                        foodList.add(duck);
                    } else {
                        Toast.makeText(context, "抱歉，当前时间不在部分食物的供应时间段内", Toast.LENGTH_LONG).show();
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
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
        foodList.clear();
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
