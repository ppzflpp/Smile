package com.dragon.smile.compact;

/**
 * Created by Administrator on 2015/3/30 0030.
 */
public interface PoiWrapper {
    void setLocation(Object object, String location);

    void start();

    void stop();

    void addDataCallback(PoiDataCallback back);

    void removeDataCallback(PoiDataCallback back);
}
