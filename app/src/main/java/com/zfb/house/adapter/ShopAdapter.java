package com.zfb.house.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lemon.LemonFragment;

import java.util.List;

/**
 * 店铺管理的适配器
 */
public class ShopAdapter extends FragmentPagerAdapter {
    List<Fragment> mFragments;

    public ShopAdapter(FragmentManager fm, List<Fragment> mFragments) {
        super(fm);
        this.mFragments = mFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ((LemonFragment) mFragments.get(position)).getTitle();
    }
}
