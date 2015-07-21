package com.dragon.smile.fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.dragon.smile.R;

/**
 * Created by Administrator on 2015/5/14 0014.
 */
public class BottomBarMenuView extends FrameLayout {

    private BottomViewClickedInterface mCallBack = null;

    private ViewGroup mHomePageView;
    private ViewGroup mNearSearchView;
    private ViewGroup mUserInfoView;
    private ViewGroup mMyOrderView;

    public BottomBarMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_bar_menu_view, null);
        this.addView(view);

        mHomePageView = (ViewGroup) view.findViewById(R.id.home_page);
        mHomePageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.onBottomViewClicked(0);
                updateViewState(0);
            }
        });

        mNearSearchView = (ViewGroup) view.findViewById(R.id.near_search);
        mNearSearchView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.onBottomViewClicked(1);
                updateViewState(1);
            }
        });

        mUserInfoView = (ViewGroup) view.findViewById(R.id.user_info);
        mUserInfoView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.onBottomViewClicked(2);
                updateViewState(2);
            }
        });

        mMyOrderView = (ViewGroup) view.findViewById(R.id.my_order);
        mMyOrderView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.onBottomViewClicked(3);
                updateViewState(3);
            }
        });

    }

    private void updateViewStateLocked(ViewGroup viewGroup, boolean selected) {
        viewGroup.setSelected(selected);
        viewGroup.getChildAt(0).setSelected(selected);
    }

    public void updateViewState(int position) {
        switch (position) {
            case 0:
                updateViewStateLocked(mHomePageView, true);
                updateViewStateLocked(mNearSearchView, false);
                updateViewStateLocked(mUserInfoView, false);
                updateViewStateLocked(mMyOrderView, false);
                break;
            case 1:
                updateViewStateLocked(mHomePageView, false);
                updateViewStateLocked(mNearSearchView, true);
                updateViewStateLocked(mUserInfoView, false);
                updateViewStateLocked(mMyOrderView, false);
                break;
            case 2:
                updateViewStateLocked(mHomePageView, false);
                updateViewStateLocked(mNearSearchView, false);
                updateViewStateLocked(mUserInfoView, true);
                updateViewStateLocked(mMyOrderView, false);
                break;
            case 3:
                updateViewStateLocked(mHomePageView, false);
                updateViewStateLocked(mNearSearchView, false);
                updateViewStateLocked(mUserInfoView, false);
                updateViewStateLocked(mMyOrderView, true);
                break;
        }
    }

    public void setCallBack(BottomViewClickedInterface callBack) {
        mCallBack = callBack;
    }

    public interface BottomViewClickedInterface {
        public void onBottomViewClicked(int position);
    }
}
