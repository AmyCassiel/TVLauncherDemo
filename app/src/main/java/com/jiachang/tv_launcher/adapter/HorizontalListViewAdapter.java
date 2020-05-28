package com.jiachang.tv_launcher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jiachang.tv_launcher.R;

/**
 * @author Mickey.Ma
 * @date 2020-05-16
 * @description
 */
public class HorizontalListViewAdapter extends BaseAdapter {
    private String[] mTitles;
    private Context mContext;
    private LayoutInflater mInflater;
    private int selectIndex = -1;

    public HorizontalListViewAdapter(Context activity, String[] titles) {
        this.mContext = activity;
        this.mTitles = titles;
        mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mTitles!=null?mTitles.length:0;
    }

    @Override
    public String getItem(int position) {
        return mTitles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.horizontal_list_item, null);
            holder.mTitle=(TextView)convertView.findViewById(R.id.hotel_head_item);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        holder.mTitle.setText(mTitles[position]);
        return convertView;

    }

    private static class ViewHolder {
        private TextView mTitle ;
    }


    public int getCountForSection(int section) {
        return section;
    }


    public void setSelectIndex(int i){
        selectIndex = i;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onKey(int keykode, View view, int position);
    }
}
