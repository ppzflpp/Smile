package com.dragon.smile.fragment;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dragon.smile.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/5/20 0020.
 */
public class MainPageViewPagerAdapter extends PagerAdapter {

    private List<ImageView> mImageViewList = new ArrayList<ImageView>();
    private Context mContext;

    public MainPageViewPagerAdapter(Context context) {
        mContext = context;
        ImageView imageView1 = new ImageView(context);
        imageView1.setBackgroundResource(R.drawable.ads_1);
        ImageView imageView2 = new ImageView(context);
        imageView2.setBackgroundResource(R.drawable.ads_2);
        ImageView imageView3 = new ImageView(context);
        imageView3.setBackgroundResource(R.drawable.ads_3);
        mImageViewList.add(imageView1);
        mImageViewList.add(imageView2);
        mImageViewList.add(imageView3);
    }

    @Override
    public int getCount() {
        return mImageViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup viewGroup, int position, Object object) {
        viewGroup.removeView(mImageViewList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, int position) {
        viewGroup.addView(mImageViewList.get(position));
        return mImageViewList.get(position);
    }
}
