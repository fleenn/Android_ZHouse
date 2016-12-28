package com.zfb.house.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/5/30.
 */
public class UserEntrustPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;
//    private List<String> mTitles;

    public UserEntrustPagerAdapter(FragmentManager fm, List<Fragment> mFragments) {
        super(fm);
        this.mFragments = mFragments;
//        this.mTitles = mTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        return mTitles.get(position);
//    }
}
