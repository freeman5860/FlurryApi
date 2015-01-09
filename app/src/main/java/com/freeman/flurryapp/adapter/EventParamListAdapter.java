package com.freeman.flurryapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.freeman.flurryapp.R;
import com.freeman.flurryapp.entry.EventParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alberthe on 2014/12/15.
 */
public class EventParamListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<EventParam> mData = new ArrayList<>();

    public EventParamListAdapter(Context c){
        mInflater = LayoutInflater.from(c);
    }

    public void setData(List<EventParam> list){
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_event_param,parent,false);
            holder = new ViewHolder();
            holder.mKeyText = (TextView) convertView.findViewById(R.id.tv_key);
            holder.mValueText = (TextView) convertView.findViewById(R.id.tv_value);
            holder.mTotalText = (TextView) convertView.findViewById(R.id.tv_total);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mKeyText.setText("key: " + mData.get(position).key);
        holder.mValueText.setText("value: " + mData.get(position).name);
        holder.mTotalText.setText("total: " + mData.get(position).totalCount);

        return convertView;
    }

    private class ViewHolder{
        public TextView mKeyText;
        public TextView mValueText;
        public TextView mTotalText;
    }
}
