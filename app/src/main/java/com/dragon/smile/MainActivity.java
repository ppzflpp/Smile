package com.dragon.smile;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dragon.smile.fragment.BottomBarMenuView;
import com.dragon.smile.server.BusinessManager;


public class MainActivity extends ActionBarActivity
        implements BottomBarMenuView.BottomViewClickedInterface {

    private CharSequence mTitle;
    private BottomBarMenuView mMenuView;
    private TextView mTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        View view = LayoutInflater.from(this).inflate(R.layout.title_bar, null);
        mTitleView = (TextView) view.findViewById(R.id.action_bar_title_view);
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        lp.leftMargin = 0;
        getSupportActionBar().setCustomView(view, lp);

        if (Build.VERSION.SDK_INT >= 21) {
            this.getWindow().setStatusBarColor(getResources().getColor(R.color.title_bg_color));
        }

        mMenuView = (BottomBarMenuView) findViewById(R.id.bottom_bar_menu_view);
        mMenuView.setCallBack(this);
        mMenuView.updateViewState(0);
        onBottomViewClicked(0);
    }

    @Override
    public void onBottomViewClicked(int position) {
        updateTitle(position);
        updateBottomBarViewContent(position);
    }

    private void updateBottomBarViewContent(int position) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, com.dragon.smile.FragmentManager.getFragment(position))
                .commit();
    }

    private void updateTitle(int position) {
        switch (position) {
            case 0:
                mTitle = getString(R.string.title_home_page);
                break;
            case 1:
                mTitle = getString(R.string.title_near_search);
                break;
            case 2:
                mTitle = getString(R.string.title_user_info);
                break;
            case 3:
                mTitle = getString(R.string.title_my_order);
                break;
        }
        mTitleView.setText(mTitle);
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d("TAG", "onDestroy......123..");
        BusinessManager.getInstance().stop();
    }
}
