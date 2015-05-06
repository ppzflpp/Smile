package com.dragon.smile;

import android.app.Application;
import android.content.Context;

import com.bmob.BmobConfiguration;
import com.bmob.BmobPro;

import cn.bmob.v3.Bmob;

/**
 * Created by Administrator on 2015/5/6 0006.
 */
public class SmileApplication extends Application {

    public final static String APP_ID = "23bdc5c09d29d653c30fe3d5932fb525";

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        initConfig(getApplicationContext());
    }

    public void initConfig(Context context) {
        BmobConfiguration config = new BmobConfiguration.Builder(context).customExternalCacheDir("Smile").build();
        BmobPro.getInstance(context).initConfig(config);

        Bmob.initialize(getApplicationContext(), APP_ID);
    }
}
