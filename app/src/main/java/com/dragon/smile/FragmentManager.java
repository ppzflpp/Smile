package com.dragon.smile;

/**
 * Created by Administrator on 2015/3/20 0020.
 */

import android.support.v4.app.Fragment;

public class FragmentManager {

    public static Fragment getFragment(int place) {
        Fragment fragment = null;
        switch (place) {
            case BottomBarMenu.HOME_PAGE:
                break;
            default:
                break;
        }
        return fragment;
    }

}
