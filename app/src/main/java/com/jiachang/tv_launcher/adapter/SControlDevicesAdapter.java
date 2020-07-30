package com.jiachang.tv_launcher.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.bean.ControlDevicesBean;
import com.jiachang.tv_launcher.bean.FacilityGoodsBean;
import com.jiachang.tv_launcher.bean.NeedServiceBean;
import com.jiachang.tv_launcher.fragment.hotelservicefragment.NeedFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Mickey.Ma
 * @date 2020-05-28
 * @description
 */
public class SControlDevicesAdapter extends RecyclerView.Adapter<SControlDevicesAdapter.ViewHolder> implements View.OnClickListener{
    private List<ControlDevicesBean> mServiceGoods;
    private onItemClickListener itemClickListener;//ItemView的监听器
    private RecyclerView recyclerView;
    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.service_control_image)
        ImageView serviceImage;
        @BindView(R.id.service_control_name)
        TextView serviceName;
        @BindView(R.id.service_control_state)
        TextView serviceControlState;
        @BindView(R.id.service_control_type_card)
        RelativeLayout serviceTypeCard;

        ViewHolder (View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
    public SControlDevicesAdapter(List<ControlDevicesBean> serviceTypeList){
        this.mServiceGoods = serviceTypeList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_recyview_goodsitem,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        initView(holder);

        holder.serviceTypeCard.setOnClickListener(v -> {
            int position = recyclerView.getChildAdapterPosition(v);
            itemClickListener.onItemClick(position,v,mServiceGoods.get(position));
        });
        return holder;
    }
    private void initView(ViewHolder viewHolder){
        ViewGroup.LayoutParams layoutParams =viewHolder.serviceTypeCard.getLayoutParams();
        layoutParams.width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ControlDevicesBean serviceType = mServiceGoods.get(position);
        holder.serviceImage.setImageResource(serviceType.getImage());
        holder.serviceName.setText(serviceType.getName());
        holder.serviceControlState.setText(serviceType.getState());
        holder.serviceControlState.setTextColor(Color.GRAY);
    }
    @Override
    public void onClick(View v) {
        if (itemClickListener != null && recyclerView != null) {
            int position = recyclerView.getChildAdapterPosition(v);
            itemClickListener.onItemClick(position, v, mServiceGoods.get(position));
        }
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
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }
    public void setDataList(List<ControlDevicesBean> serviceTypeList){
        serviceTypeList.clear();
        this.mServiceGoods = serviceTypeList;
        notifyDataSetChanged();
    }
    public void setItemSelectedListener(onItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    public interface onItemClickListener{
        void onItemClick(int position, View v, ControlDevicesBean devicesBean);
    }
}
