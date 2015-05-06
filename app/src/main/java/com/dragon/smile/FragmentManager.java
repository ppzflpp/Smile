package com.dragon.smile;

/**
 * Created by Administrator on 2015/3/20 0020.
 */


import android.support.v4.app.Fragment;

import com.dragon.smile.fragment.MainPageFragment;
import com.dragon.smile.fragment.UserInfoFragment;

public class FragmentManager {

    public static Fragment getFragment(int place) {
        Fragment fragment = null;
        switch (place) {
            case BottomBarMenu.HOME_PAGE:
                fragment = MainPageFragment.newInstance(null, null);
                break;
            case BottomBarMenu.USER_INFO:
                fragment = UserInfoFragment.newInstance(null, null);
            default:
                break;
        }
        return fragment;
    }

}
