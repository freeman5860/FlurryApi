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
    boolean showChecked = false;

    public AppListAdapter(Context c, boolean s){
        mInflater = LayoutInflater.from(c);
        showChecked = s;
    }

    public void setData(ArrayList<FlurryApplication> list){
        mData.clear();
        mData.addAll(list);

        notifyDataSetChanged();
    }

    public void setItemVisible(int position,boolean visible){
        mData.get(position).visible = visible;

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
            holder.mPlatformText = (TextView) convertView.findViewById(R.id.tv_platform);
            holder.mVisibleText = (TextView) convertView.findViewById(R.id.tv_visible);
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
        holder.mPlatformText.setText(mData.get(position).platform);

        if (showChecked) {
            if (mData.get(position).visible) {
                holder.mVisibleText.setText("show");
            } else {
                holder.mVisibleText.setText("hide");
            }
            holder.mVisibleText.setVisibility(View.VISIBLE);
        }else{
            holder.mVisibleText.setVisibility(View.GONE);
        }

        return convertView;
    }

    private class ViewHolder{
        public TextView mNameText;
        public TextView mPlatformText;
        public TextView mVisibleText;
    }

    public interface ItemClickCallBack{
        public void onItemClick(int position);
    }

    private ItemClickCallBack itemClickCallBack = null;

    public void setItemClickCallBack(ItemClickCallBack callBack){
        itemClickCallBack = callBack;
    }
}
