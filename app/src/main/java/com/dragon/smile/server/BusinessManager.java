package com.dragon.smile.server;

import android.content.Context;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviPara;
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
    private LatLng mUserLocation = null;
    private PoiDataCallback mDataCallBack = null;
    private boolean mStarted = false;

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
        if (mStarted) {
            LogUtils.d(TAG, "BusinessManager had started");
        }
        mStarted = true;
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
                    mUserLocation = (LatLng) object;
                    mPoiWrapper.setLocation(mUserLocation, location);
                    if (mDataCallBack == null) {
                        mDataCallBack = new PoiDataCallback() {
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
                        };
                    }
                    mPoiWrapper.addDataCallback(mDataCallBack);
                    mPoiWrapper.start();
                }
            }
        });
        LocationService.getInstance(mContext).start();
    }

    public void stop() {
        if (!mStarted) {
            LogUtils.d(TAG, "BusinessManager had stopped");
            return;
        }

        if (mPoiWrapper != null) {
            mPoiWrapper.removeDataCallback(mDataCallBack);
            mDataCallBack = null;
            mPoiWrapper.stop();
        }
        mStarted = false;
    }

    public void registerCallback(UiCallback callback) {
        mCallback = callback;
    }

    public void navigate(double destLatitude, double destLongitude) {
        LatLng src = new LatLng(mUserLocation.latitude, mUserLocation.longitude);
        LatLng dest = new LatLng(destLatitude, destLongitude);

        LogUtils.d(TAG, "dest = " + destLatitude + "," + destLongitude);

        // 构建 导航参数
        NaviPara para = new NaviPara();
        para.startPoint = src;
        para.startName = "从这里开始";
        para.endPoint = dest;
        para.endName = "到这里结束";

        try {
            BaiduMapNavigation.openBaiduMapNavi(para, mContext);
            LogUtils.d(TAG, "begin to navigate");
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            LogUtils.d(TAG, "exception occur");
            //TODO
        }


    }


}
