package com.zfb.house.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.zfb.house.ui.PictureSlideFragment;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 图片浏览页面的适配器
 * Created by Administrator on 2016/8/31.
 */
public class PictureSlidePagerAdapter extends FragmentStatePagerAdapter {

    //图片路径集合
    private ArrayList<String> urlList = new ArrayList<>();

    public PictureSlidePagerAdapter(FragmentManager fm, String url) {
        super(fm);
        urlList.add(url);
    }

    public PictureSlidePagerAdapter(FragmentManager fm, String[] urls) {
        super(fm);
        Collections.addAll(urlList, urls);
    }

    @Override
    public Fragment getItem(int position) {
        return PictureSlideFragment.newInstance(urlList.get(position));
    }

    @Override
    public int getCount() {
        return urlList.size();
    }

}
