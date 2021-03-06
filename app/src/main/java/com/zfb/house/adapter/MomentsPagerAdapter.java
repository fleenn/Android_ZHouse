package com.zfb.house.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.lemon.LemonFragment;

import java.util.List;

/**
 * Created by Snekey on 2016/5/23.
 */
public class MomentsPagerAdapter extends FragmentPagerAdapter{
    List<Fragment> mFragments;
    public MomentsPagerAdapter(FragmentManager fm,List<Fragment> mFragments) {
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
    public CharSequence getPageTitle(int position) {
        return ((LemonFragment)mFragments.get(position)).getTitle();
    }
}
