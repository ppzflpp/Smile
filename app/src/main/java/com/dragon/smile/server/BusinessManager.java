package com.dragon.smile.server;

import android.content.Context;

import com.baidu.mapapi.model.LatLng;
import com.dragon.smile.UiCallback;
import com.dragon.smile.compact.PoiDataCallback;
import com.dragon.smile.compact.PoiWrapper;
import com.dragon.smile.compact.baidu.BaiduPoiWrapper;
import com.dragon.smile.data.BusinessData;
import com.dragon.smile.lbs.LocationListener;
import com.dragon.smile.lbs.LocationService;
import com.dragon.smile.utils.LogUtils;

import java.util.List;

/**
 * Created by Administrator on 2015/3/30 0030.
 */
public class BusinessManager {

    private final static String TAG = "BusinessManager";

    private static BusinessManager sInstance = null;

    private List<BusinessData> mBusinessDataList;

    private PoiWrapper mPoiWrapper;
    private Context mContext = null;
    private UiCallback mCallback;

    public static BusinessManager getInstance() {
        if (sInstance == null)
            sInstance = new BusinessManager();
        return sInstance;
    }

    public void init(Context context) {
        if (mPoiWrapper == null) {
            mContext = context;
            mPoiWrapper = new BaiduPoiWrapper(context);
        }
    }

    public void start() {
        //locate
        LocationService.getInstance(mContext).addLocationListener(new LocationListener() {
            @Override
            public void onGetLocation(Object object, String location) {
                //1.notify ui to update

                if (location != null) {
                    if (mCallback != null) {
                        mCallback.onUIRefresh(UiCallback.UI_TYPE_LOCATION_STRING, location);
                    }
                }

                //2.begin to get poi data
                LogUtils.d(TAG, "object = " + object + ",location = " + location);
                if (object != null) {
                    mPoiWrapper.setLocation((LatLng) object, location);
                    mPoiWrapper.addDataCallback(new PoiDataCallback() {
                        @Override
                        public void onDataCallback(List<BusinessData> dataList) {
                            if (dataList == null)
                                return;

                            mBusinessDataList = dataList;
                            //notify ui to update
                            if (mCallback != null) {
                                mCallback.onUIRefresh(UiCallback.UI_TYPE_LOCATION_POI_RESULT, mBusinessDataList);
                            }
                        }
                    });
                    mPoiWrapper.start();
                }
            }
        });
        LocationService.getInstance(mContext).start();
    }

    public void stop() {
        //TODO
    }

    public void registerCallback(UiCallback callback) {
        mCallback = callback;
    }


}
