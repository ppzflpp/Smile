package com.dragon.smile.compact.baidu;

import android.content.Context;
import android.util.SparseArray;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.dragon.smile.compact.PoiWrapper;
import com.dragon.smile.data.SearchConstant;
import com.dragon.smile.lbs.LocationListener;
import com.dragon.smile.lbs.LocationService;
import com.dragon.smile.utils.LogUtils;

import java.util.List;

/**
 * Created by Administrator on 2015/3/30 0030.
 */
public class BaiduPoiWrapper implements PoiWrapper {

    private final static String TAG = "BaiDuPoiWrapper";
    private OnGetPoiSearchResultListener mOnGetPoiSearchResultListener = new OnGetPoiSearchResultListener() {
        @Override
        public void onGetPoiResult(PoiResult poiResult) {
            List<PoiInfo> lists = poiResult.getAllPoi();
            LogUtils.d(TAG, "lists = " + lists);
            for (int i = 0; lists != null && i < lists.size(); i++) {
                PoiInfo info = lists.get(i);
                LogUtils.d(TAG, "" + info.toString());
            }
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
            LogUtils.d(TAG, "---2---" + poiDetailResult);
        }
    };
    private LatLng mLocation = null;
    private PoiSearch mPoiSearch = null;
    private SuggestionSearch mSuggestionSearch = null;

    public BaiduPoiWrapper(final Context context) {
        SDKInitializer.initialize(context);

        LocationService.getInstance(context).setLocationListener(new LocationListener() {
            @Override
            public void onGetLocation(Object object) {
                if (object instanceof LatLng) {
                    mLocation = (LatLng) object;
                    LocationService.getInstance(context).stop();
                    mPoiSearch.searchNearby(new PoiNearbySearchOption().keyword(SearchConstant.SEARCH_KEY_WORD)
                            .radius(SearchConstant.SEARCH_RADIUS).pageCapacity(SearchConstant.SEARCH_PAGE_CAPACITY)
                            .location(mLocation));
                }
            }
        });
        LocationService.getInstance(context).start();

        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(mOnGetPoiSearchResultListener);
    }

    @Override
    public SparseArray<?> getData() {
        return null;
    }
}
