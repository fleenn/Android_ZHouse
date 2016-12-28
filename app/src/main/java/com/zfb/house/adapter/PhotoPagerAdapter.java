package com.zfb.house.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.zfb.house.ui.BrokerShopPhotoActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 店铺详情页面照片浏览适配器
 * Created by HourGlassRemember on 2016/8/31.
 */
public class PhotoPagerAdapter extends PagerAdapter {

    private static final String TAG = "PhotoPagerAdapter";

    private Context mContext;
    private String[] photoArray;
    private List<ImageView> imageViewList = new ArrayList<>();

    public PhotoPagerAdapter(Context mContext, List<ImageView> imageViewList, String[] photoArray) {
        this.mContext = mContext;
        this.imageViewList = imageViewList;
        this.photoArray = photoArray;
    }

    @Override
    public int getCount() {
        return imageViewList.size();
    }

    @Override
    public boolean isViewFromObject(View arg1, Object arg2) {
        return arg1 == arg2;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        FrameLayout frameLayout = new FrameLayout(mContext);
        final View imageView = imageViewList.get(position);
        //设置imageView的大小
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BrokerShopPhotoActivity.class);
                intent.putExtra("photoArray", photoArray);
                intent.putExtra("position", position);
                mContext.startActivity(intent);
            }
        });
        //如果imageView已经在之前添加到了一个父控件，则必须先remove，否则会抛出IllegalStateException的异常
        ViewParent viewParent = imageView.getParent();
        if (null != viewParent) {
            ((ViewGroup) viewParent).removeView(imageView);
        }
        frameLayout.addView(imageView);
        container.addView(frameLayout);
        return frameLayout;
    }

}
