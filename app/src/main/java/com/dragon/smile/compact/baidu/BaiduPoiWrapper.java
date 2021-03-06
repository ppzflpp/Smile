package com.dragon.smile.compact.baidu;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.cloud.CloudListener;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.CloudPoiInfo;
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
import com.dragon.smile.compact.DataParser;
import com.dragon.smile.compact.PoiDataCallback;
import com.dragon.smile.compact.PoiWrapper;
import com.dragon.smile.data.BusinessData;
import com.dragon.smile.data.SearchConstant;
import com.dragon.smile.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/30 0030.
 */
public class BaiduPoiWrapper implements PoiWrapper {

    private final static String TAG = "BaiDuPoiWrapper";
    private final static boolean ENABLE_CLOUD_POI_SEARCH = true;
    private boolean mStarted = false;
    private LatLng mLocation = null;
    private String mLocationString = null;
    private PoiSearch mPoiSearch = null;
    private CloudManager mCloudManager = null;
    private SuggestionSearch mSuggestionSearch = null;
    private List<PoiDataCallback> mPoiDataCallbackList = new ArrayList<PoiDataCallback>();
    private List<BusinessData> mBusinessDataList = new ArrayList<>();
    private CloudListener mCloudListener = new CloudListener() {
        @Override
        public void onGetSearchResult(CloudSearchResult cloudSearchResult, int i) {
            if (cloudSearchResult != null && cloudSearchResult.poiList != null) {

                mBusinessDataList.clear();
                Log.d("TAG", "onGetSearchResult..efefewfe....i  = " + i);
                for (CloudPoiInfo info : cloudSearchResult.poiList) {
                    mBusinessDataList.add(parseData(info));

                    for (PoiDataCallback callback : mPoiDataCallbackList)
                        callback.onDataCallback(mBusinessDataList);
                }
            } else {
                LogUtils.d(TAG, "can not get data");
            }
        }

        @Override
        public void onGetDetailSearchResult(DetailSearchResult detailSearchResult, int i) {
            LogUtils.d(TAG, "cloud onGetDetailSearchResult");
        }
    };
    private OnGetPoiSearchResultListener mOnGetPoiSearchResultListener = new OnGetPoiSearchResultListener() {
        @Override
        public void onGetPoiResult(PoiResult poiResult) {
            List<PoiInfo> lists = poiResult.getAllPoi();
            if (lists == null)
                return;

            mBusinessDataList.clear();

            for (PoiInfo info : lists) {
                mBusinessDataList.add(parseData(info));
            }

            for (PoiDataCallback callback : mPoiDataCallbackList)
                callback.onDataCallback(mBusinessDataList);
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        }
    };

    public BaiduPoiWrapper(final Context context) {
        SDKInitializer.initialize(context);

        if (ENABLE_CLOUD_POI_SEARCH) {
            mCloudManager = CloudManager.getInstance();
            mCloudManager.init(mCloudListener);
        } else {
            mPoiSearch = PoiSearch.newInstance();
            mPoiSearch.setOnGetPoiSearchResultListener(mOnGetPoiSearchResultListener);
        }

    }

    private BusinessData parseData(Object object) {
        BusinessData data = new BusinessData();
        if (object instanceof PoiInfo) {
            PoiInfo info = (PoiInfo) object;

        } else if (object instanceof CloudPoiInfo) {
            CloudPoiInfo info = (CloudPoiInfo) object;
            Map<String, Object> extras = info.extras;
            data.id = String.valueOf(info.uid);
            data.latitude = info.latitude;
            data.longitude = info.longitude;
            data.distance = info.distance;
            String extra = (String) extras.get("extra");
            DataParser.parseData(data, extra);
        }
        return data;
    }

    @Override
    public void start() {
        if (mStarted) {
            LogUtils.d(TAG, "search had started");
            return;
        }

        mStarted = true;
        if (ENABLE_CLOUD_POI_SEARCH) {
            Log.d("TAG", "wrapper start");
            NearbySearchInfo info = new NearbySearchInfo();
            info.ak = "1ef16c3021f26091f465617ae4f790eb";
            info.pageSize = SearchConstant.SEARCH_PAGE_CAPACITY;
            info.radius = SearchConstant.SEARCH_RADIUS;
            info.geoTableId = 99144;
            info.location = new StringBuilder().append(mLocation.longitude).append(",").append(mLocation.latitude).toString();
            boolean result = mCloudManager.nearbySearch(info);
        } else {
            mPoiSearch.searchNearby(new PoiNearbySearchOption().keyword(SearchConstant.SEARCH_KEY_WORD)
                    .radius(SearchConstant.SEARCH_RADIUS).pageCapacity(SearchConstant.SEARCH_PAGE_CAPACITY)
                    .location(mLocation));
        }
    }

    @Override
    public void stop() {
        mStarted = false;
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
