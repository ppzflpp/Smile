package com.dragon.smile.compact;

import com.dragon.smile.data.BusinessData;
import com.dragon.smile.data.ServiceItem;
import com.dragon.smile.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by Administrator on 2015/4/26 0026.
 */
public class DataParser {
    private final static String TAG = "DataParser";

    public static void parseData(BusinessData srcData, String jsonTag) {
        LogUtils.d(TAG, "jsonTag = " + jsonTag);
        try {
            JSONTokener jsonParser = new JSONTokener(jsonTag);
            JSONObject data = (JSONObject) jsonParser.nextValue();
            srcData.url = data.getString("url");
            srcData.name = data.getString("name");
            srcData.address = data.getString("address");
            JSONArray serviceItems = data.getJSONArray("services");
            if (serviceItems != null) {
                ServiceItem item = null;
                for (int i = 0; i < serviceItems.length(); i++) {
                    JSONObject object = serviceItems.getJSONObject(i);
                    item = new ServiceItem();
                    item.serviceName = object.getString("name");
                    item.serviceInfo = object.getString("info");
                    item.servicePrice = object.getString("price");
                    srcData.serviceItems.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
