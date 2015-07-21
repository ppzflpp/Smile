package com.dragon.smile.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.dragon.smile.R;
import com.dragon.smile.UiCallback;
import com.dragon.smile.data.BusinessData;
import com.dragon.smile.server.BusinessManager;
import com.dragon.smile.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;


public class NearSearchFragment extends Fragment implements UiCallback {

    private final static String TAG = "POIFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final static int MSG_UPDATE_LOCATION = 0;
    private final static int MSG_UPDATE_POI_RESULT = 1;
    private static NearSearchFragment sInstance;
    private boolean mAttached = false;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private NearSearchAdapter mAdapter = null;
    private List<BusinessData> mDataList;
    private ListView mNearSearchListView;
    private TextView mLocationView = null;
    private String mUserLocation = null;
    private ArrayList<BusinessData> mBusinessDataList = null;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_UPDATE_LOCATION:
                    if (mAttached)
                        mLocationView.setText(getString(R.string.location_tip) + mUserLocation);
                    break;
                case MSG_UPDATE_POI_RESULT:
                    setDataList(mBusinessDataList);
                    break;

            }
        }

    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NearSearchFragment() {
    }

    // TODO: Rename and change types of parameters
    public static NearSearchFragment newInstance(String param1, String param2) {
        if (sInstance != null)
            return sInstance;

        NearSearchFragment fragment = new NearSearchFragment();
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

        // TODO: Change Adapter to display your content
        mAdapter = new NearSearchAdapter(getActivity());

        BusinessManager.getInstance().init(getActivity().getApplicationContext());
        BusinessManager.getInstance().registerCallback(this);
        BusinessManager.getInstance().start();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mUserLocation != null)
            mLocationView.setText(getString(R.string.location_tip) + mUserLocation);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View rootView = null;
        rootView = inflater.inflate(R.layout.near_search_fragment, null);
        mNearSearchListView = (ListView) rootView.findViewById(R.id.near_search_list_view);
        mNearSearchListView.setAdapter(mAdapter);

        mLocationView = (TextView) rootView.findViewById(R.id.user_location);
        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mAttached = true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mAttached = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusinessManager.getInstance().stop();
    }


    private void setDataList(List<BusinessData> dataList) {
        for (BusinessData data : dataList) {
            LogUtils.d(TAG, "data = " + data.address);
        }
        mDataList = dataList;
        mAdapter.setDataList(mDataList);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onUIRefresh(int type, Object data) {
        switch (type) {
            case UI_TYPE_LOCATION_STRING:
                mUserLocation = (String) data;
                mHandler.sendEmptyMessage(MSG_UPDATE_LOCATION);
                break;
            case UI_TYPE_LOCATION_POI_RESULT:
                mBusinessDataList = (ArrayList<BusinessData>) data;
                mHandler.sendEmptyMessage(MSG_UPDATE_POI_RESULT);
                break;
        }
    }
}
