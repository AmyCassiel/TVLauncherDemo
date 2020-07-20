package com.jiachang.tv_launcher.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.bean.ControlScenesBean;
import com.jiachang.tv_launcher.bean.FacilityGoodsBean;
import com.jiachang.tv_launcher.utils.ViewUtils;

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
public class SControlScenesAdapter extends RecyclerView.Adapter<SControlScenesAdapter.ViewHolder>{
    private List<ControlScenesBean> mServiceGoods;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.service_control_image1)
        ImageView serviceImage;
        @BindView(R.id.service_control_name1)
        TextView serviceName;
        @BindView(R.id.service_control_type_card1)
        RelativeLayout serviceTypeCard;

        ViewHolder (View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public SControlScenesAdapter(List<ControlScenesBean> serviceTypeList){
        this.mServiceGoods = serviceTypeList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_recyview_goodsitem1,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        initView(holder);
        holder.serviceTypeCard.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                ViewUtils.focusStatus(v);
            } else {
                ViewUtils.normalStatus(v);
            }
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
        ControlScenesBean serviceType = mServiceGoods.get(position);
        holder.serviceImage.setImageResource(serviceType.getImage());
        holder.serviceName.setText(serviceType.getName());
    }

    @Override
    public int getItemCount() {
        return mServiceGoods.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setDataList( List<ControlScenesBean> serviceTypeList){
        serviceTypeList.clear();
        this.mServiceGoods = serviceTypeList;
        notifyDataSetChanged();
    }
    public interface onItemClickListener{
        void onItemClick(int position, View v, FacilityGoodsBean serviceType);

    }
}
