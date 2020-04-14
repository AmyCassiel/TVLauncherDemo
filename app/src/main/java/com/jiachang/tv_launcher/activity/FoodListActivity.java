package com.jiachang.tv_launcher.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.adapter.FoodAdapter;
import com.jiachang.tv_launcher.bean.Food;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Mickey.Ma
 * @date 2020-03-26
 * @description
 */
public class FoodListActivity extends Activity implements FoodAdapter.onItemClickListener {
    private List<Food> foodList = new ArrayList<>();
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomMenu();
        setContentView(R.layout.dining_foodlist_layout);
        ButterKnife.bind(this);
        initFood();

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        FoodAdapter adapter = new FoodAdapter(this,foodList);
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
        intent.putExtra("text", food.getName());
        ImageView imageView = v.findViewById(R.id.food_image);
        AutoLinearLayout layout = v.findViewById(R.id.food_card);
        //android5.0以上的api有这些方法（在Android5.0以下的手机运行会报错，只有在5.0之上才能正确运行并且看到效果）
//           ActivityOptions options= ActivityOptions.makeSceneTransitionAnimation(this, Pair.create(textView,"textView"),Pair.create(textView,"textView"));
           ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(this, imageView, "image_dining");
           startActivity(intent,options.toBundle());
        //向下兼容（在Android5.0以下看不到共享元素的那种效果，但是在5.0以下的手机上能正常运行）
//        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, imageView, "food_image");
//        startActivity(intent, optionsCompat.toBundle());

    }

    private void initFood() {
        for (int i = 0; i < 20; i++) {
            Food duck = new Food("彩米握寿司    RMB:95元" +
                    "\n主要原料:大米、海苔、三文鱼、鱼子酱", R.mipmap.food);
            foodList.add(duck);
        }
    }

    protected void hideBottomMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /*@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //在recyclerView的move事件情况下，拦截调，只让它响应五向键和左右箭头移动
        Log.i("DiningActivity.this", "CustomRecycleView.dispatchTouchEvent.");
        return ev.getAction() == MotionEvent.ACTION_MOVE || super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean result = super.dispatchKeyEvent(event);
        View focusView = recyclerView.getFocusedChild();
        if (focusView != null) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        return true;
                    } else {
                        View rightView = FocusFinder.getInstance().findNextFocus(this, focusView, View.FOCUS_RIGHT);
                        Log.i("DiningActivity.this", "rightView is null:" + (rightView == null));
                        if (rightView != null) {
                            rightView.requestFocusFromTouch();
                            return true;
                        } else {
//                            this.smoothScrollBy(dx, 0);
                            return true;
                        }
                    }
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    View leftView = FocusFinder.getInstance().findNextFocus(this, focusView, View.FOCUS_LEFT);
                    Log.i("DiningActivity.this", "left is null:" + (leftView == null));
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        return true;
                    } else {
                        if (leftView != null) {
                            leftView.requestFocusFromTouch();
                            return true;
                        } else {
//                            this.smoothScrollBy(-dx, 0);
                            return true;
                        }
                    }
            }
        }
        return result;
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //popWindow
    private class OnPopRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

        }
    }
}
