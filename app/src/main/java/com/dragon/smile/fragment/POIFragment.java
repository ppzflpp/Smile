package com.dragon.smile.fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.dragon.smile.data.BusinessData;
import com.dragon.smile.utils.LogUtils;

import java.util.List;


public class POIFragment extends ListFragment {

    private final static String TAG = "POIFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private POIAdapter mAdapter = null;
    private List<BusinessData> mDataList;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public POIFragment() {
    }

    // TODO: Rename and change types of parameters
    public static POIFragment newInstance(String param1, String param2) {
        POIFragment fragment = new POIFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
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
        mAdapter = new POIAdapter(getActivity());
        setListAdapter(mAdapter);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

    }
    public void setDataList(List<BusinessData> dataList) {
        for (BusinessData data : dataList) {
            LogUtils.d(TAG, "data = " + data.address);
        }
        mDataList = dataList;
        mAdapter.setDataList(mDataList);
        mAdapter.notifyDataSetChanged();
    }


}
