package com.dragon.smile.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dragon.smile.PayActivity;
import com.dragon.smile.R;
import com.dragon.smile.data.BusinessData;
import com.dragon.smile.data.PayItem;
import com.dragon.smile.data.ServiceItem;
import com.dragon.smile.server.BusinessManager;
import com.dragon.smile.utils.LogUtils;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Administrator on 2015/4/4 0004.
 */
public class POIAdapter extends BaseAdapter {
    private static final String TAG = "POIAdapter";

    private Context mContext = null;
    private List<BusinessData> mDataList = null;

    public POIAdapter(Activity activity) {
        mContext = activity;
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
            viewHolder.servicesView = (LinearLayout) convertView.findViewById(R.id.poi_item_services);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.iconView.setImageResource(R.mipmap.ic_launcher);
        viewHolder.titleView.setText(mDataList.get(position).name);
        viewHolder.addressView.setText(mDataList.get(position).address);
        createItems(viewHolder.servicesView, mDataList.get(position).serviceItems, position);

        int distance = mDataList.get(position).distance;
        if (distance > 1000) {
            DecimalFormat df = new DecimalFormat("0.0");
            viewHolder.distanceView.setText(df.format((double) distance / 1000) + mContext.getString(R.string.km));
        } else {
            viewHolder.distanceView.setText(distance + mContext.getString(R.string.m));
        }
        viewHolder.distanceView.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.mipmap.icon_navigate), null, null, null);
        final int pos = position;
        viewHolder.distanceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusinessManager.getInstance().navigate(mDataList.get(pos).latitude, mDataList.get(pos).longitude);
            }
        });

        viewHolder.commentView.setText(mContext.getString(R.string.comment, mDataList.get(position).comment_technology, mDataList.get(position).comment_service, mDataList.get(position).comment_environment));
        viewHolder.exchangeView.setText(mContext.getString(R.string.volume, mDataList.get(position).volume));


        return convertView;
    }

    private void createItems(ViewGroup parentView, List<ServiceItem> items, int position) {
        if (items != null) {
            for (final ServiceItem item : items) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.poi_item_service_item, null);
                TextView name = (TextView) view.findViewById(R.id.poi_service_item_name);
                LogUtils.d(TAG, "name = " + item.serviceName);
                name.setText(item.serviceName);
                TextView price = (TextView) view.findViewById(R.id.poi_service_item_price);
                price.setText(item.servicePrice);
                TextView info = (TextView) view.findViewById(R.id.poi_service_item_info);
                info.setText(item.serviceInfo);
                Button pay = (Button) view.findViewById(R.id.poi_service_item_pay);
                pay.setTag(position);
                pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PayActivity.class);
                        int pos = (Integer) v.getTag();
                        PayItem payItem = PayItem.createPayItem(item, mDataList.get(pos).phone, mDataList.get(pos).name);
                        intent.putExtra("pay_item", payItem);
                        mContext.startActivity(intent);
                    }
                });
                parentView.addView(view);
            }
        }
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
        LinearLayout servicesView;
    }
}
