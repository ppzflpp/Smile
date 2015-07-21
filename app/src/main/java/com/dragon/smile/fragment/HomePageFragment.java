package com.dragon.smile.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dragon.smile.LoginActivity;
import com.dragon.smile.R;
import com.dragon.smile.RedPacketActivity;
import com.dragon.smile.SmileApplication;

public class HomePageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final static int MSG_UPDATE_ADS = 1;
    private static HomePageFragment sInstance;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ViewPager mViewPager = null;
    private int mItem = 0;
    private boolean mResume = false;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_UPDATE_ADS:
                    if (mViewPager != null) {
                        mViewPager.setCurrentItem(mItem++);
                        if (mItem >= 3)
                            mItem = 0;

                        if (mResume) {
                            mHandler.sendEmptyMessageDelayed(MSG_UPDATE_ADS, 3000);
                        }
                    }
                    break;
            }
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HomePageFragment() {
    }

    // TODO: Rename and change types of parameters
    public static HomePageFragment newInstance(String param1, String param2) {

        if (sInstance != null)
            return sInstance;

        HomePageFragment fragment = new HomePageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        sInstance = fragment;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mResume = true;
        mHandler.sendEmptyMessage(MSG_UPDATE_ADS);
    }

    @Override
    public void onPause() {
        super.onPause();
        mResume = false;
        mHandler.removeMessages(MSG_UPDATE_ADS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View rootView = null;
        rootView = inflater.inflate(R.layout.home_page, null);
        rootView.findViewById(R.id.red_packet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((SmileApplication) getActivity().getApplication()).mIsLoginIn) {
                    Intent intent = new Intent(getActivity(), RedPacketActivity.class);
                    getActivity().startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }
            }
        });

        mViewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        mViewPager.setAdapter(new MainPageViewPagerAdapter(this.getActivity()));

        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
