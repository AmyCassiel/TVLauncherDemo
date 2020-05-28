package com.jiachang.tv_launcher.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.bean.ServiceType;
import com.jiachang.tv_launcher.utils.ViewUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Mickey.Ma
 * @date 2020-05-18
 * @description
 */
public class ServiceTypeAdapter extends RecyclerView.Adapter<ServiceTypeAdapter.ViewHolder> implements View.OnClickListener {

    private List<ServiceType> mServiceGoods;
    private Context context;
    private onItemClickListener itemClickListener;//ItemView的监听器
    private RecyclerView recyclerView;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.service_image)
        ImageView serviceImage;
        @BindView(R.id.service_name)
        TextView serviceName;
        @BindView(R.id.service_supply_time)
        TextView serviceSupplyTime;
        @BindView(R.id.service_type_card)
        RelativeLayout serviceTypeCard;

        ViewHolder (View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public  ServiceTypeAdapter (Context context,List<ServiceType> serviceTypeList){
        this.context = context;
        this.mServiceGoods = serviceTypeList;
    }

    @Override
    public void onClick(View v) {
        if (itemClickListener != null && recyclerView != null){
            //recyclerView 21以下使用,　22时作废
//             int position = recyclerView.getChildPosition(v);
            //22时用些方法替换上面的方法
            int position = recyclerView.getChildAdapterPosition(v);
            itemClickListener.onItemClick(position,v,mServiceGoods.get(position));
        }
    }
    public void setItemClickListener(onItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_recy_view_goods_item,parent,false);
        view.setOnClickListener(this);
        final ViewHolder holder = new ViewHolder(view);
        context = view.getContext();

        holder.serviceTypeCard.setFocusable(true);
        holder.serviceTypeCard.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Drawable drawable = ContextCompat.getDrawable(context,R.drawable.item_background);
                    v.setBackground(drawable);
                } else {
                    Drawable drawable = ContextCompat.getDrawable(context,R.drawable.white_radius);
                    v.setBackground(drawable);
                }
            }
        });

        initView(holder);

        return holder;
    }

    private void initView(ViewHolder viewHolder){
        ViewGroup.LayoutParams layoutParams =viewHolder.serviceTypeCard.getLayoutParams();
        layoutParams.width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;

//        onDetachedFromRecyclerView(recyclerView);
    }
    public void setDataList( List<ServiceType> mServiceGoods){
        mServiceGoods.clear();
        this.mServiceGoods = mServiceGoods;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceType serviceType = mServiceGoods.get(position);
        holder.serviceImage.setImageBitmap(serviceType.getImageId());
        holder.serviceName.setText(serviceType.getName());
        holder.serviceSupplyTime.setText(serviceType.getSupplyTime());
    }

    @Override
    public int getItemCount() {
        return mServiceGoods.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onAttachedToRecyclerView(@NotNull RecyclerView recyclerView) {
        this.recyclerView=recyclerView;

    }

    public void remove(int i){

        notifyItemRangeChanged(i, mServiceGoods.size());
    }

    public interface onItemClickListener{
        void onItemClick(int position,View v,ServiceType serviceType);

    }
}
