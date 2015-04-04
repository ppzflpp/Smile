package com.dragon.smile;

/**
 * Created by Administrator on 2015/4/4 0004.
 */
public interface UiCallback {
    public final static int UI_TYPE_LOCATION_STRING = 0;
    public final static int UI_TYPE_LOCATION_POI_RESULT = 1;

    public void onUIRefresh(int type, Object data);
}
