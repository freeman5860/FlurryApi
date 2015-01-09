package com.freeman.flurryapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.freeman.flurryapp.R;
import com.freeman.flurryapp.entry.FlurryDayValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alberthe on 2014/12/15.
 */
public class DayValueListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<FlurryDayValue> mData = new ArrayList<>();

    public DayValueListAdapter(Context c){
        mInflater = LayoutInflater.from(c);
    }

    public void setData(List<FlurryDayValue> list){
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
            convertView = mInflater.inflate(R.layout.item_day_value_list,parent,false);
            holder = new ViewHolder();
            holder.mValueText = (TextView) convertView.findViewById(R.id.tv_value);
            holder.mDateText = (TextView) convertView.findViewById(R.id.tv_date);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mValueText.setText(mData.get(position).value);
        holder.mDateText.setText(mData.get(position).date);

        return convertView;
    }

    private class ViewHolder{
        public TextView mValueText;
        public TextView mDateText;
    }
}
