package com.jiachang.tv_launcher.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.activity.foodDetailsActivity;
import com.jiachang.tv_launcher.bean.Food;
import com.jiachang.tv_launcher.utils.ViewUtils;

import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

/**
 * @author Mickey.Ma
 * @date 2020-03-26
 * @description
 */
public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {
    private List<Food> mFoodList;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.food_image)
        ImageView foodImage;
        @BindView(R.id.food_name)
        TextView foodName;
        @BindView(R.id.food)
        LinearLayout food;

        public ViewHolder (View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public  FoodAdapter (List <Food> fruitList){
        mFoodList = fruitList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        context = view.getContext();

        holder.food.setFocusable(true);

        holder.food.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ViewUtils.focusStatus(v);
                } else {
                    ViewUtils.normalStatus(v);
                }
            }
        });

        holder.food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = v.getVerticalScrollbarPosition();
                Food food = mFoodList.get(position);
                Intent intent = new Intent();
                intent.setClass(context, foodDetailsActivity.class);
                intent.putExtra("imageName", food.getName());
                intent.putExtra("image",food.getImageId());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptionsCompat  options = ActivityOptionsCompat
                            .makeSceneTransitionAnimation((Activity) context,new Pair<View,String>(holder.foodImage,"food_image"),new Pair<View,String>(holder.foodName,"name"));
                    Log.e("options = ",""+options);
                    context.startActivity(intent, options.toBundle());
                }else{
                    context.startActivity(intent);
                }
                context.startActivity(new Intent(context,foodDetailsActivity.class),
                        ActivityOptions.makeSceneTransitionAnimation((Activity) context,
                                v, "food_image").toBundle());
            }
        });

        initView(holder);

        return holder;
    }

    private void initView(ViewHolder viewHolder){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);
        // 屏幕宽度（像素）
        int width = metric.widthPixels/5;
        // 屏幕高度（像素）
        int height = metric.heightPixels/3;
        ViewGroup.LayoutParams layoutParams =viewHolder.food.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Food food = mFoodList.get(position);
        holder.foodImage.setImageResource(food.getImageId());
        holder.foodName.setText(food.getName());
    }

    @Override
    public int getItemCount(){
        return mFoodList.size();
    }

    @Override
    public long getItemId(int i){
        return i;
    }
}
