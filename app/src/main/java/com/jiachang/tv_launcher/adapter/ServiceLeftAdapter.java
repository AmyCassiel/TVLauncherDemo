package com.jiachang.tv_launcher.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jiachang.tv_launcher.R;

import androidx.annotation.NonNull;

/**
 * @author Mickey.Ma
 * @date 2020-05-21
 * @description
 */
public class ServiceLeftAdapter extends ArrayAdapter {
    private final String[] data;
    private final Context mContext;
    private final int res;

    public ServiceLeftAdapter(@NonNull Context context, int resource,String[] data) {
        super(context, resource);
        this.mContext = context;
        this.res = resource;
        this.data = data;
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

        Typeface face = Typeface.createFromAsset(mContext.getAssets(),"fonts/sim.ttf");
        holder.mText.setTypeface(face);
        holder.mText.setText(data[position]);
        return convertView;
    }

    
    public static class ViewHolder{

        final TextView mText;

        ViewHolder(View itemView) {
            mText= itemView.findViewById(R.id.text1);
        }
    }
}
