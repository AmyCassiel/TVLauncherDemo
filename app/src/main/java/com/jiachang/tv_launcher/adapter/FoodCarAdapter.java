package com.jiachang.tv_launcher.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.bean.FoodIntentBean;
import com.jiachang.tv_launcher.utils.ViewUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Mickey.Ma
 * @date 2020-07-06
 * @description  FoodCarActivity中RecyclerView 的Item实体类
 */
public class FoodCarAdapter extends RecyclerView.Adapter<FoodCarAdapter.ViewHolder> implements View.OnClickListener{
    private RecyclerView recyclerView;
    private final List<FoodIntentBean> foodNameList;
    private onItemClickListener itemClickListener;

    public FoodCarAdapter(List<FoodIntentBean> foodNameList) {
        this.foodNameList = foodNameList;
    }
    @Override
    public void onClick(View v) {
        if (itemClickListener != null && recyclerView != null) {
            int position = recyclerView.getChildAdapterPosition(v);
            itemClickListener.onItemClick(position, v, foodNameList.get(position));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dining_foodcar_item, parent, false);
        view.setOnClickListener(this);
        ViewHolder holder = new ViewHolder(view);
        holder.foodItemLayout.setFocusable(true);
        holder.foodItemLayout.setOnFocusChangeListener(ViewUtils::sView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.foodImg.setImageBitmap(foodNameList.get(position).getFoodImage());
        holder.foodName.setText(foodNameList.get(position).getFoodName());
        holder.foodPrice.setText(foodNameList.get(position).getFoodPrice());
        holder.foodCount.setText(foodNameList.get(position).getFoodCount()+"份");
    }

    @Override
    public int getItemCount() {
        return foodNameList.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        @BindView(R.id.food_item_layout)
        LinearLayout foodItemLayout;
        @BindView(R.id.tv_name)
        TextView foodName;
        @BindView(R.id.tv_price)
        TextView foodPrice;
        @BindView(R.id.food_img)
        ImageView foodImg;
        @BindView(R.id.tv_unit)
        TextView foodCount;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }
    public void setItemClickListener(onItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    public interface onItemClickListener {
        void onItemClick(int position, View v, FoodIntentBean foodNameList);
    }
}
