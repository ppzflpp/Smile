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
import com.dragon.smile.fragment.dummy.MainPageContent;

/**
 * Created by Administrator on 2015/3/22 0022.
 */
public class MainPageAdapter extends BaseAdapter {

    private Context mContext = null;

    public MainPageAdapter(Activity activity) {
        mContext = activity.getApplicationContext();
    }

    @Override
    public int getCount() {
        return MainPageContent.ITEMS.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.main_page_item, null);
            viewHolder = new ViewHolder();
            viewHolder.iconView = (ImageView) convertView.findViewById(R.id.main_page_item_icon);
            viewHolder.titleView = (TextView) convertView.findViewById(R.id.main_page_item_title);
            viewHolder.contentView = (TextView) convertView.findViewById(R.id.main_page_item_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.iconView.setImageResource(MainPageContent.ITEMS.get(position).iconId);
        viewHolder.titleView.setText(MainPageContent.ITEMS.get(position).titleId);
        viewHolder.contentView.setText(MainPageContent.ITEMS.get(position).contentId);

        return convertView;
    }


    class ViewHolder {
        ImageView iconView;
        TextView titleView;
        TextView contentView;
    }

}
