package com.freeman.flurryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alberthe on 2014/12/18.
 */
public class AppListAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    ArrayList<FlurryApplication> mData = new ArrayList<FlurryApplication>();

    public AppListAdapter(Context c){
        mInflater = LayoutInflater.from(c);
    }

    public void setData(ArrayList<FlurryApplication> list){
        mData.clear();
        mData.addAll(list);

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_app_list,parent,false);
            holder = new ViewHolder();
            holder.mNameText = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickCallBack != null){
                    itemClickCallBack.onItemClick(position);
                }
            }
        });

        holder.mNameText.setText(mData.get(position).name);

        return convertView;
    }

    private class ViewHolder{
        public TextView mNameText;
    }

    public interface ItemClickCallBack{
        public void onItemClick(int position);
    }

    private ItemClickCallBack itemClickCallBack = null;

    public void setItemClickCallBack(ItemClickCallBack callBack){
        itemClickCallBack = callBack;
    }
}
