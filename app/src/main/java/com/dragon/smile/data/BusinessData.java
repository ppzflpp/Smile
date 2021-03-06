package com.dragon.smile.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/3/30 0030.
 */
public class BusinessData implements Serializable {

    public String id;
    public String name;
    public String address;
    public String url;
    public String phone;
    public int distance;//M
    public String comment_technology = "0";
    public String comment_service = "0";
    public String comment_environment = "0";
    public int volume;
    public double longitude;
    public double latitude;
    public List<ServiceItem> serviceItems = new ArrayList<>();

}
