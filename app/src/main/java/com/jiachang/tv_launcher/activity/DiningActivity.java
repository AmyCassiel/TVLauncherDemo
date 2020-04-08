package com.jiachang.tv_launcher.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.adapter.FoodAdapter;
import com.jiachang.tv_launcher.bean.Food;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Mickey.Ma
 * @date 2020-03-26
 * @description
 */
public class DiningActivity extends Activity {
    private List<Food> foodList = new ArrayList<>();
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomMenu();
        setContentView(R.layout.dining_layout);
        ButterKnife.bind(this);
        initFood();
        GridLayoutManager layoutManager = new GridLayoutManager(this,4);
        FoodAdapter adapter = new FoodAdapter(foodList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnScrollListener(new OnPopRecyclerViewScrollListener());
    }

    private void initFood() {
        for (int i = 0; i < 20; i++) {
            Food duck = new Food("翡翠玉皮烤鸭\nRMB:95元", R.mipmap.roast_duck);
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
//        recyclerView.getAdapter();
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
