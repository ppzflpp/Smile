package com.dragon.smile.utils;

import android.content.Context;

import com.dragon.smile.data.LoginUser;

/**
 * Created by Administrator on 2015/5/31 0031.
 */
public class ApplicationUtils {
    public static void saveUserData(Context context, LoginUser user) {
        if (user != null) {
            //user.save(context);
            user.update(context);
        }
    }
}
