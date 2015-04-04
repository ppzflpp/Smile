package com.dragon.smile.lbs;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.dragon.smile.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/3/22 0022.
 */
public class LocationService {

    private static final String TAG = "LocationService";

    private static LocationService sInstance = null;

    private Context mContext = null;
    private MyLocationListener mListener = new MyLocationListener();
    private String mUserLocationString = null;
    private LatLng mUserLocation = null;
    private LocationClient mLocationClient;
    private boolean mRegister = false;
    private List<LocationListener> mLocationListenerList = new ArrayList<LocationListener>();


    private LocationService(Context context) {
        mContext = context;
    }

    public static LocationService getInstance(Context context) {
        if (sInstance == null)
            sInstance = new LocationService(context);

        return sInstance;
    }

    public void start() {
        if (mLocationClient == null) {
            mLocationClient = new LocationClient(mContext);
            LocationClientOption option = new LocationClientOption();
            option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
            option.setCoorType("bd09ll");
            option.setScanSpan(5000);
            option.setIsNeedAddress(true);
            option.setNeedDeviceDirect(true);
            mLocationClient.setLocOption(option);
        }

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

    public void addLocationListener(LocationListener listener) {
        if (mLocationListenerList.contains(listener)) {
            LogUtils.w(TAG, "listener has registered");
        } else {
            mLocationListenerList.add(listener);
            listener.onGetLocation(mUserLocation, mUserLocationString);
        }
    }

    public void removeLocationListener(LocationListener listener) {
        mLocationListenerList.remove(listener);
    }

    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return;

            mUserLocationString = location.getAddrStr();
            if (mUserLocationString != null) {
                mUserLocation = new LatLng(location.getLatitude(),
                        location.getLongitude());
                for (LocationListener listener : mLocationListenerList)
                    listener.onGetLocation(mUserLocation, mUserLocationString);
                stop();
            }
        }
    }


}
