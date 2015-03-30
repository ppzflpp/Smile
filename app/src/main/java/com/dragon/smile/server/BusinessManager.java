package com.dragon.smile.server;

import android.content.Context;
import android.util.SparseArray;

import com.dragon.smile.compact.PoiWrapper;
import com.dragon.smile.compact.baidu.BaiDuPoiWrapper;
import com.dragon.smile.data.BusinessData;

/**
 * Created by Administrator on 2015/3/30 0030.
 */
public class BusinessManager {

    private static BusinessManager sInstance = null;

    private SparseArray<BusinessData> mBusinessDataList;

    private PoiWrapper mPoiWrapper;
    private Context mContext = null;

    public static BusinessManager getInstance() {
        if (sInstance == null)
            sInstance = new BusinessManager();
        return sInstance;
    }

    public SparseArray<BusinessData> getBusinessDataList() {
        return mBusinessDataList;
    }

    public void init(Context context) {
        if (mPoiWrapper == null) {
            mContext = context;
            mPoiWrapper = new BaiDuPoiWrapper(context);
            mBusinessDataList = (SparseArray<BusinessData>) mPoiWrapper.getData();
        }
    }

    private SparseArray<BusinessData> loadData(boolean reload) {
        if (reload) {
            mBusinessDataList = (SparseArray<BusinessData>) mPoiWrapper.getData();
        }
        return mBusinessDataList;
    }


}
