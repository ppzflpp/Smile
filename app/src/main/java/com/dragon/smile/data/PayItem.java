package com.dragon.smile.data;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/4/26 0026.
 */
public class PayItem extends Item implements Serializable {
    public String id;
    public String userName;

    public static PayItem createPayItem(ServiceItem item, String id, String userName) {
        PayItem payItem = new PayItem();
        payItem.serviceName = item.serviceName;
        payItem.serviceInfo = item.serviceInfo;
        payItem.servicePrice = item.servicePrice;
        payItem.id = id;
        payItem.userName = userName;
        return payItem;
    }
}
