package com.dragon.smile.compact.baidu;

import android.content.Context;
import android.location.Location;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.cloud.CloudListener;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.CloudSearchResult;
import com.baidu.mapapi.cloud.DetailSearchResult;
import com.baidu.mapapi.cloud.NearbySearchInfo;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.dragon.smile.compact.PoiDataCallback;
import com.dragon.smile.compact.PoiWrapper;
import com.dragon.smile.data.BusinessData;
import com.dragon.smile.data.SearchConstant;
import com.dragon.smile.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/3/30 0030.
 */
public class BaiduPoiWrapper implements PoiWrapper {

    private final static String TAG = "BaiDuPoiWrapper";
    private CloudListener mCloudListener = new CloudListener() {
        @Override
        public void onGetSearchResult(CloudSearchResult cloudSearchResult, int i) {
            LogUtils.d(TAG, "cloud onGetSearchResult");
        }

        @Override
        public void onGetDetailSearchResult(DetailSearchResult detailSearchResult, int i) {
            LogUtils.d(TAG, "cloud onGetDetailSearchResult");
        }
    };
    private final static boolean ENABLE_CLOUD_POI_SEARCH = false;
    private LatLng mLocation = null;
    private String mLocationString = null;
    private PoiSearch mPoiSearch = null;
    private CloudManager mCloudManager = null;
    private SuggestionSearch mSuggestionSearch = null;
    private List<PoiDataCallback> mPoiDataCallbackList = new ArrayList<PoiDataCallback>();
    private List<BusinessData> mBusinessDataList = new ArrayList<>();
    private OnGetPoiSearchResultListener mOnGetPoiSearchResultListener = new OnGetPoiSearchResultListener() {
        @Override
        public void onGetPoiResult(PoiResult poiResult) {
            List<PoiInfo> lists = poiResult.getAllPoi();
            if (lists == null)
                return;

            for (PoiInfo info : lists) {
                BusinessData data = new BusinessData();
                data.id = info.uid;
                data.name = info.name;
                data.address = info.address;
                data.phone = info.phoneNum;
                data.latitude = info.location.latitude;
                data.longitude = info.location.longitude;
                data.distance = getDistance(mLocation.latitude, mLocation.longitude, info.location.latitude, info.location.longitude);
                LogUtils.d(TAG, "longitude = " + data.longitude + ",latitude = " + data.latitude);
                mBusinessDataList.add(data);
            }

            for (PoiDataCallback callback : mPoiDataCallbackList)
                callback.onDataCallback(mBusinessDataList);
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
            LogUtils.d(TAG, "---2---" + poiDetailResult);
        }
    };

    public BaiduPoiWrapper(final Context context) {
        SDKInitializer.initialize(context);

        if (ENABLE_CLOUD_POI_SEARCH) {
            mCloudManager = CloudManager.getInstance();
            mCloudManager.init(mCloudListener);
            LogUtils.d(TAG, "--------------------------init------------------------");
        } else {
            mPoiSearch = PoiSearch.newInstance();
            mPoiSearch.setOnGetPoiSearchResultListener(mOnGetPoiSearchResultListener);
        }

    }

    @Override
    public void start() {
        if (ENABLE_CLOUD_POI_SEARCH) {
            NearbySearchInfo info = new NearbySearchInfo();
            info.ak = "1ef16c3021f26091f465617ae4f790eb";
            info.pageSize = SearchConstant.SEARCH_PAGE_CAPACITY;
            info.radius = SearchConstant.SEARCH_RADIUS;
            info.location = new StringBuilder().append(mLocation.longitude).append(",").append(mLocation.latitude).toString();
            mCloudManager.nearbySearch(info);
            LogUtils.d(TAG, "--------------------------nearbySearch------------------------" + info.location.toString());
        } else {
            mPoiSearch.searchNearby(new PoiNearbySearchOption().keyword(SearchConstant.SEARCH_KEY_WORD)
                    .radius(SearchConstant.SEARCH_RADIUS).pageCapacity(SearchConstant.SEARCH_PAGE_CAPACITY)
                    .location(mLocation));
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void addDataCallback(PoiDataCallback callback) {
        if (mPoiDataCallbackList.contains(callback)) {
            LogUtils.w(TAG, "callback registered");
        } else {
            mPoiDataCallbackList.add(callback);
        }

    }

    @Override
    public void removeDataCallback(PoiDataCallback callback) {
        mPoiDataCallbackList.remove(callback);
    }

    @Override
    public void setLocation(Object object, String location) {
        mLocation = (LatLng) object;
        mLocationString = location;

    }

    private int getDistance(double lat1, double lon1, double lat2, double lon2) {
        float[] results = new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, results);
        return (int) results[0];
    }
}
