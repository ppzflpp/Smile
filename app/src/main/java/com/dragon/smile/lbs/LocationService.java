package com.dragon.smile.lbs;

import android.content.Context;

/**
 * Created by Administrator on 2015/3/22 0022.
 */
public class LocationService {

    private static LocationService sInstance = null;

    private Context mContext = null;

    private LocationService(Context context) {
        mContext = context;
    }

    public static LocationService getInstance(Context context) {
        if (sInstance == null)
            sInstance = new LocationService(context);

        return sInstance;
    }


}
