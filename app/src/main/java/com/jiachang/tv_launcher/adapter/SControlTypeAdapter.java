package com.jiachang.tv_launcher.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.bean.Controltype;
import com.jiachang.tv_launcher.bean.Controltype1;
import com.jiachang.tv_launcher.bean.FacType;
import com.jiachang.tv_launcher.bean.NeedServiceType;
import com.jiachang.tv_launcher.utils.ViewUtils;

import org.jetbrains.annotations.NotNull;

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
public class SControlTypeAdapter extends RecyclerView.Adapter<SControlTypeAdapter.ViewHolder>{
    private Context context;
    private List<Controltype> mServiceGoods;
    private RecyclerView recyclerView;
    private onItemClickListener itemClickListener;//ItemView的监听器

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.service_control_image)
        ImageView serviceImage;
        @BindView(R.id.service_control_name)
        TextView serviceName;
        @BindView(R.id.service_control_state)
        TextView serviceSupplyTime;
        @BindView(R.id.service_control_type_card)
        RelativeLayout serviceTypeCard;

        ViewHolder (View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public SControlTypeAdapter(Context context, List<Controltype> serviceTypeList){
        this.context = context;
        this.mServiceGoods = serviceTypeList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_recyview_goodsitem,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        context = view.getContext();

        initView(holder);

        holder.serviceTypeCard.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ViewUtils.focusStatus(v);
                } else {
                    ViewUtils.normalStatus(v);
                }
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
        Controltype serviceType = mServiceGoods.get(position);
        holder.serviceImage.setImageResource(serviceType.getImage());
        holder.serviceName.setText(serviceType.getName());
        holder.serviceSupplyTime.setText(serviceType.getSupplyTime());
        holder.serviceSupplyTime.setTextColor(Color.GRAY);
    }

    @Override
    public int getItemCount() {
        return mServiceGoods.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setmServiceGoods(List<Controltype> mServiceGoods) {
        this.mServiceGoods = mServiceGoods;
    }

    @Override
    public void onAttachedToRecyclerView(@NotNull RecyclerView recyclerView) {
        this.recyclerView=recyclerView;

    }

    public void setDataList( List<Controltype> serviceTypeList){
        serviceTypeList.clear();
        this.mServiceGoods = serviceTypeList;
        notifyDataSetChanged();
    }

    public void setData( List<Controltype> serviceTypeList){
        this.mServiceGoods = serviceTypeList;
    }

    public interface onItemClickListener{
        void onItemClick(int position, View v, FacType serviceType);

    }
}
