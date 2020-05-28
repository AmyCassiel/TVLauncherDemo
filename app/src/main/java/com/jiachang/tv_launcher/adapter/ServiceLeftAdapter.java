package com.jiachang.tv_launcher.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jiachang.tv_launcher.R;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;

/**
 * @author Mickey.Ma
 * @date 2020-05-21
 * @description
 */
public class ServiceLeftAdapter extends ArrayAdapter {
    private String[] data;
    private int selectItem=0;
    private Context mContext;
    private int res;

    public ServiceLeftAdapter(@NonNull Context context, int resource,String[] data) {
        super(context, resource);
        this.mContext = context;
        this.res = resource;
        this.data = data;
    }


    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data!=null?data.length:0;
    }

    @Override
    public String getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView=LayoutInflater.from(mContext).inflate(res, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        holder.mText.setText(data[position]);
        return convertView;
    }

    
    public static class ViewHolder{

        TextView mText;

        ViewHolder(View itemView) {
            mText= (TextView) itemView.findViewById(R.id.text1);
        }
    }
}
