package com.dragon.smile.lbs;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.model.LatLng;
import com.dragon.smile.utils.LogUtils;

/**
 * Created by Administrator on 2015/3/22 0022.
 */
public class LocationService {

    private static final String TAG = "LocationService";

    private static LocationService sInstance = null;

    private Context mContext = null;
    private MyLocationListener mListener = new MyLocationListener();
    private LocationClient mLocationClient;
    private boolean mRegister = false;
    private LocationListener mLocationListener = null;


    private LocationService(Context context) {
        mContext = context;
    }

    public static LocationService getInstance(Context context) {
        if (sInstance == null)
            sInstance = new LocationService(context);

        return sInstance;
    }

    public void start() {
        if (mLocationClient == null)
            mLocationClient = new LocationClient(mContext);

        if (!mRegister) {
            mLocationClient.registerLocationListener(mListener);
            LogUtils.d(TAG, "start");
            mLocationClient.start();
            mRegister = true;
        }
    }

    public void stop() {
        if (mRegister) {
            mLocationClient.unRegisterLocationListener(mListener);
            LogUtils.d(TAG, "stop");
            mLocationClient.stop();
            mRegister = false;
        }
    }

    public void setLocationListener(LocationListener listener) {
        mLocationListener = listener;
    }

    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return;

            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            String city = location.getAddrStr();
            LogUtils.d(TAG, "city = " + city);
            /*
            if(mLocationListener != null)
                mLocationListener.onGetLocation(ll);
                */
        }
    }


}
