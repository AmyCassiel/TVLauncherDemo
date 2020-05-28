package com.jiachang.tv_launcher.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiachang.tv_launcher.R;

import java.util.ArrayList;

import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author Mickey.Ma
 * @date 2020-03-26
 * @description
 */
public class FoodDetailsActivity extends Activity implements View.OnClickListener {
    private ArrayList arrayList = new ArrayList();
    @BindView(R.id.food_img)
    ImageView foodImg;
//    @BindView(R.id.foodName)
//    TextView foodName;
//    @BindView(R.id.foodPrice)
//    TextView foodPrice;
//    @BindView(R.id.foodMaterial)
    TextView foodMaterial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomMenu();
        setContentView(R.layout.dining_fooddetail_activity);
        ButterKnife.bind(this);

        arrayList = getIntent().getStringArrayListExtra("deliveryType");

//        foodImg.setOnClickListener(this);
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
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    public void onClick(View v) {
        //兼容5.0以下的手机，运行不会出错，但是只有在5.0以上的手机才能看到共享元素回退的效果
        ActivityCompat.finishAfterTransition(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
