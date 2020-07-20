package com.jiachang.tv_launcher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiachang.tv_launcher.R;
import com.jiachang.tv_launcher.bean.NeedServiceBean;
import com.jiachang.tv_launcher.fragment.hotelservicefragment.NeedFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Mickey.Ma
 * @date 2020-05-18
 * @description
 */
public class ServiceNeedGoodsAdapter extends RecyclerView.Adapter<ServiceNeedGoodsAdapter.ViewHolder> implements View.OnClickListener {
    private List<NeedServiceBean> mServiceGoods;
    private Context context;
    private OnItemSelectedListener itemClickListener;//ItemView的监听器
    private RecyclerView recyclerView;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.service_image1)
        ImageView serviceImage;
        @BindView(R.id.service_name1)
        TextView serviceName;
        @BindView(R.id.service_supply_time1)
        TextView serviceSupplyTime;
        @BindView(R.id.service_amount1)
        public TextView serviceAmount;
        @BindView(R.id.service_price1)
        TextView servicePrice;
        @BindView(R.id.service_type_card1)
        LinearLayout serviceTypeCard;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public ServiceNeedGoodsAdapter(Context context, List<NeedServiceBean> serviceTypeList) {
        this.context = context;
        this.mServiceGoods = serviceTypeList;
    }

    @Override
    public void onClick(View v) {
        if (itemClickListener != null && recyclerView != null) {
            int position = recyclerView.getChildAdapterPosition(v);
            itemClickListener.OnItemSelectedListener(position, v, mServiceGoods.get(position));
        }
    }

    //删除元素,需要告诉UI线程布局的变动
    public void remove() {
        mServiceGoods.remove(getItemCount()-1);
        notifyDataSetChanged();
    }

    public void setItemSelectedListener(OnItemSelectedListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_need_recyview_goods_item, parent, false);
        view.setOnClickListener(this);
        final ViewHolder holder = new ViewHolder(view);
        context = view.getContext();

        holder.serviceTypeCard.setFocusable(true);
        holder.serviceTypeCard.setOnClickListener(v -> {
            int position = recyclerView.getChildAdapterPosition(v);
            itemClickListener.OnItemSelectedListener(position,v,mServiceGoods.get(position));
        });
        initView(holder);
        return holder;
    }

    private void initView(ViewHolder viewHolder) {
        ViewGroup.LayoutParams layoutParams = viewHolder.serviceTypeCard.getLayoutParams();
        layoutParams.width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
    }

    public void setDataList(List<NeedServiceBean> mServiceGoods) {
        mServiceGoods.clear();
        NeedFragment.needGoodsBeansList.clear();
        NeedFragment.list.clear();
        this.mServiceGoods = mServiceGoods;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NeedServiceBean serviceType = mServiceGoods.get(position);
        holder.serviceImage.setImageBitmap(serviceType.getImageId());
        holder.serviceName.setText(serviceType.getName());
        holder.serviceSupplyTime.setText(serviceType.getSupplyTime());
        String price = serviceType.getPrice();
        if (price.equals("¥0.0")) {
            holder.servicePrice.setText("免费");
        } else {
            holder.servicePrice.setText(price);
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
    public void onAttachedToRecyclerView(@NotNull RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public void remove(int i) {
        notifyItemRangeChanged(i, mServiceGoods.size());
    }

    public interface OnItemSelectedListener {
        void OnItemSelectedListener(int position, View v, NeedServiceBean serviceType);

    }
}
