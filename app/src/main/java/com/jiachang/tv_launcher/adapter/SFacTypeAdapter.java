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
import com.jiachang.tv_launcher.bean.FacilityGoodsBean;
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
 * @date 2020-05-28
 * @description
 */
public class SFacTypeAdapter extends RecyclerView.Adapter<SFacTypeAdapter.ViewHolder> implements View.OnClickListener {
    private Context context;
    private List<FacilityGoodsBean> serviceTypeList;
    private RecyclerView recyclerView;
    private onItemClickListener itemClickListener;//ItemView的监听器

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.service_image)
        ImageView serviceImage;
        @BindView(R.id.service_name)
        TextView serviceName;
        @BindView(R.id.service_supply_time)
        TextView serviceSupplyTime;
        @BindView(R.id.service_price)
        TextView servicePrice;
        @BindView(R.id.service_type_card)
        RelativeLayout serviceTypeCard;

        ViewHolder (View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public SFacTypeAdapter(Context context, List<FacilityGoodsBean> serviceTypeList){
        this.context = context;
        this.serviceTypeList = serviceTypeList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_recyview_goods_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        context = view.getContext();

        initView(holder);

        holder.serviceTypeCard.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Drawable drawable = ContextCompat.getDrawable(context, R.drawable.item_background);
                    v.setBackground(drawable);
                } else {
                    Drawable drawable = ContextCompat.getDrawable(context, R.drawable.white_radius);
                    v.setBackground(drawable);
                }
            }
        });

        return holder;
    }

    public void setDataList( List<FacilityGoodsBean> serviceTypeList){
        serviceTypeList.clear();
        this.serviceTypeList = serviceTypeList;
        notifyDataSetChanged();
    }

    private void initView(ViewHolder viewHolder){
        ViewGroup.LayoutParams layoutParams =viewHolder.serviceTypeCard.getLayoutParams();
        layoutParams.width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FacilityGoodsBean serviceType = serviceTypeList.get(position);
        holder.serviceImage.setImageBitmap(serviceType.getImage());
        holder.serviceName.setText(serviceType.getName());
        holder.servicePrice.setText(serviceType.getLocal());
        holder.serviceSupplyTime.setText(serviceType.getSupplyTime());
    }

    @Override
    public int getItemCount() {
        return serviceTypeList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onAttachedToRecyclerView(@NotNull RecyclerView recyclerView) {
        this.recyclerView=recyclerView;

    }

    @Override
    public void onClick(View v) {
        if (itemClickListener != null && recyclerView != null){
            //recyclerView 21以下使用,　22时作废
//             int position = recyclerView.getChildPosition(v);
            //22时用些方法替换上面的方法
            int position = recyclerView.getChildAdapterPosition(v);
            itemClickListener.onItemClick(position,v,serviceTypeList.get(position));
        }
    }
    public interface onItemClickListener{
        void onItemClick(int position, View v, FacilityGoodsBean serviceType);
    }
}
