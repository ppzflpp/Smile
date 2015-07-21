package com.dragon.smile;

/**
 * Created by Administrator on 2015/3/20 0020.
 */


import android.support.v4.app.Fragment;

import com.dragon.smile.fragment.AboutFragment;
import com.dragon.smile.fragment.HomePageFragment;
import com.dragon.smile.fragment.NearSearchFragment;
import com.dragon.smile.fragment.UserInfoFragment;

public class FragmentManager {

    public static Fragment getFragment(int place) {
        Fragment fragment = null;
        switch (place) {
            case BottomBarMenu.HOME_PAGE:
                fragment = HomePageFragment.newInstance(null, null);
                break;
            case BottomBarMenu.USER_INFO:
                fragment = UserInfoFragment.newInstance(null, null);
                break;
            case BottomBarMenu.NEAR_SEARCH:
                fragment = NearSearchFragment.newInstance(null, null);
                break;
            case BottomBarMenu.MORE:
                fragment = AboutFragment.newInstance(null, null);
                break;
            default:
                break;
        }
        return fragment;
    }

}
