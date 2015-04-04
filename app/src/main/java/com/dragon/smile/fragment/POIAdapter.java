package com.dragon.smile.fragment;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dragon.smile.R;
import com.dragon.smile.data.BusinessData;

import java.util.List;

/**
 * Created by Administrator on 2015/4/4 0004.
 */
public class POIAdapter extends BaseAdapter {
    private Context mContext = null;
    private List<BusinessData> mDataList = null;

    public POIAdapter(Activity activity) {
        mContext = activity.getApplicationContext();
    }


    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.poi_item, null);
            viewHolder = new ViewHolder();
            viewHolder.iconView = (ImageView) convertView.findViewById(R.id.poi_item_icon);
            viewHolder.titleView = (TextView) convertView.findViewById(R.id.poi_item_title);
            viewHolder.commentView = (TextView) convertView.findViewById(R.id.poi_item_comment);
            viewHolder.exchangeView = (TextView) convertView.findViewById(R.id.poi_item_exchange);
            viewHolder.addressView = (TextView) convertView.findViewById(R.id.poi_item_address);
            viewHolder.distanceView = (TextView) convertView.findViewById(R.id.poi_item_distance);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.iconView.setImageResource(R.mipmap.ic_launcher);
        viewHolder.titleView.setText(mDataList.get(position).name);
        viewHolder.addressView.setText(mDataList.get(position).address);

        return convertView;
    }

    public void setDataList(List<BusinessData> dataList) {
        mDataList = dataList;
    }

    class ViewHolder {
        ImageView iconView;
        TextView titleView;
        TextView commentView;
        TextView exchangeView;
        TextView addressView;
        TextView distanceView;
    }
}
