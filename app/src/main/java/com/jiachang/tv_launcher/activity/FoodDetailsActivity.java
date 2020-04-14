package com.jiachang.tv_launcher.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiachang.tv_launcher.R;

import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author Mickey.Ma
 * @date 2020-03-26
 * @description
 */
public class FoodDetailsActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.food_img)
    ImageView foodImg;


    private int resId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomMenu();
        setContentView(R.layout.dining_fooddetail_activity);
        ButterKnife.bind(this);


        Intent intent = getIntent();
        int image = intent.getIntExtra("image", 0);
        String text = intent.getStringExtra("text");
        ImageView imageView_share = findViewById(R.id.food_img);
        TextView textView_share = findViewById(R.id.foodName);

        imageView_share.setImageResource(image);
        textView_share.setText(text);

        imageView_share.setOnClickListener(this);
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

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT: //向左键
                Log.d(TAG,"left--->");
                Toast.makeText(this,"left--->",Toast.LENGTH_LONG).show();
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:  //向右键
                Log.d(TAG,"right--->");
                Toast.makeText(this,"right--->",Toast.LENGTH_LONG).show();
                break;
            case KeyEvent.KEYCODE_DPAD_UP:   //向上键
                Log.d(TAG,"up--->");
                Toast.makeText(this,"up--->",Toast.LENGTH_LONG).show();
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:   //向下键
                *//* 实际开发中有时候会触发两次，所以要判断一下按下时触发 ，松开按键时不触发
                 *    exp:KeyEvent.ACTION_UP*//*

                if (event.getAction() == KeyEvent.ACTION_DOWN){
                    Toast.makeText(this,"enter--->",Toast.LENGTH_LONG).show();
                    Log.d(TAG,"enter--->");
                }
                break;

//            case KeyEvent.KEYCODE_ENTER:     //确定键enter
            case KeyEvent.KEYCODE_DPAD_CENTER:
                Log.d(TAG,"enter--->");
                Toast.makeText(this,"enter--->",Toast.LENGTH_LONG).show();
                break;
            case KeyEvent.KEYCODE_BACK:    //返回键
                Log.d(TAG,"back--->");
                Toast.makeText(this,"back--->",Toast.LENGTH_LONG).show();
                *//*if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (down && repeatCount == 0) {
                    } else if((event.getFlags() & KeyEvent.FLAG_LONG_PRESS) != 0) {
                        mHandler.postDelayed(MouseRunable, 100);
//                        return -1;
                    }
                }
                return true;   //这里由于break会退出，所以我们自己要处理掉 不返回上一层*//*
                break;
            case KeyEvent.KEYCODE_MENU: //菜单键
                Log.d(TAG,"setting--->");
                Toast.makeText(this,"setting--->",Toast.LENGTH_LONG).show();
                break;
            *//*case KeyEvent.KEYCODE_PAGE_DOWN:     //向上翻页键
            case KeyEvent.KEYCODE_MEDIA_NEXT:
                Log.d(TAG,"page down--->");
                Toast.makeText(mContext,"page down--->",Toast.LENGTH_LONG).show();
                break;
            case KeyEvent.KEYCODE_PAGE_UP:     //向下翻页键
            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                Log.d(TAG,"page up--->");
                Toast.makeText(mContext,"page up--->",Toast.LENGTH_LONG).show();
                break;
            case KeyEvent.KEYCODE_VOLUME_UP:   //调大声音键
                Log.d(TAG,"voice up--->");
                Toast.makeText(mContext,"voice up--->",Toast.LENGTH_LONG).show();
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN: //降低声音键
                Log.d(TAG,"voice down--->");
                Toast.makeText(mContext,"voice down--->",Toast.LENGTH_LONG).show();
                break;
            case KeyEvent.KEYCODE_VOLUME_MUTE: //禁用声音
                Log.d(TAG,"voice mute--->");
                Toast.makeText(mContext,"voice mute--->",Toast.LENGTH_LONG).show();
                break;*//*
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }*/
}
