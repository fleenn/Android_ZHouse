package com.zfb.house.component;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 自定义ViewPager
 * Created by HourGlassRemember on 2016/9/2.
 */
public class MyViewPager extends ViewPager {

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        try {
            return super.onInterceptTouchEvent(event);
        } catch (IllegalArgumentException e) {
            // ignore it
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            return super.onTouchEvent(event);
        } catch (IllegalArgumentException e) {
            //  ignore it
        }
        return false;
    }

}
