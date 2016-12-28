package com.zfb.house.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Matrix;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.event.ReFreshGrabListEvent;
import com.lemon.util.ScreenUtil;
import com.zfb.house.R;
import com.zfb.house.adapter.MomentsPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 一键抢单
 * Created by Administrator on 2016/4/24.
 */
@Layout(id = R.layout.activity_grab)
public class GrabActivity extends LemonActivity {

    public static final int GRAB_CUSTOMER = 0;
    public static final int GRABBED_CUSTOMER = 1;

    @FieldView(id = R.id.pager_grab_list)
    private ViewPager pagerGrabList;
    @FieldView(id = R.id.img_grab_cursor)
    private ImageView imgGrabCursor;
    //抢客户
    @FieldView(id = R.id.tv_grab_customer)
    private TextView tvGrabCustomer;
    //已抢客户
    @FieldView(id = R.id.tv_grabbed_customer)
    private TextView tvGrabbedCustomer;
    //抢房源
    @FieldView(id = R.id.tv_grab_house)
    private TextView tvGrabHouse;
    //已抢房源
    @FieldView(id = R.id.tv_grabbed_house)
    private TextView tvGrabbedHouse;

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        finish();
    }

    @OnClick(id = R.id.rlayout_title_rt_img)
    public void toLocation() {
        startActivity(new Intent(mContext, MapActivity.class));
    }

    MomentsPagerAdapter mMomentsPagerAdapter;
    GrabCustomerFragment mCustomerFragment, mGrabbedCustomerFragment;
    GrabHouseFragment mHouseFragment;
    GrabbedHouseFragment mGrabbedHouseFragment;
    List<Fragment> mFragments;
    //    指示器偏移量
    private int offset = 0;
    //    当前pager索引
    private int currIndex = 0;

    @Override
    protected void initView() {
        setCenterText(R.string.title_grab);
        setRtImg(R.drawable.near_position);
        mFragments = new ArrayList<>();
        mCustomerFragment = new GrabCustomerFragment(GRAB_CUSTOMER);
        mGrabbedCustomerFragment = new GrabCustomerFragment(GRABBED_CUSTOMER);
        mHouseFragment = new GrabHouseFragment();
        mGrabbedHouseFragment = new GrabbedHouseFragment();
        mFragments.add(mCustomerFragment);
        mFragments.add(mGrabbedCustomerFragment);
        mFragments.add(mHouseFragment);
        mFragments.add(mGrabbedHouseFragment);
        tvGrabCustomer.setOnClickListener(new MyOnClickListener(0));
        tvGrabbedCustomer.setOnClickListener(new MyOnClickListener(1));
        tvGrabHouse.setOnClickListener(new MyOnClickListener(2));
        tvGrabbedHouse.setOnClickListener(new MyOnClickListener(3));
        mMomentsPagerAdapter = new MomentsPagerAdapter(getSupportFragmentManager(), mFragments);
        pagerGrabList.setAdapter(mMomentsPagerAdapter);
        pagerGrabList.setCurrentItem(0);
        initCursorPos();
        pagerGrabList.addOnPageChangeListener(new MyPageChangeListener());
        pagerGrabList.setOffscreenPageLimit(3);
    }

    /**
     * 头标点击监听
     */
    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            if (index == 0) {
                mCustomerFragment.setRefreshing();
            } else if (index == 2) {
                mHouseFragment.setRefreshing();
            }
            pagerGrabList.setCurrentItem(index);
        }
    }

    //初始化指示器位置
    public void initCursorPos() {
        // 初始化动画
        if (ScreenUtil.screenWidth == 0) {
            ScreenUtil.init(mContext);
        }
        int screenWidth = ScreenUtil.screenWidth;// 获取分辨率宽度
        offset = screenWidth / 4;// 计算偏移量

        Matrix matrix = new Matrix();
        matrix.postTranslate(0, 0);
        imgGrabCursor.setImageMatrix(matrix);// 设置动画初始位置
    }

    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        int one = offset;// 页卡1 -> 页卡2 偏移量
        int two = one * 2;// 页卡1 -> 页卡3 偏移量
        int three = one * 3;// 页卡1 -> 页卡4 偏移量

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Animation animation = null;
            switch (position) {
                case 0:
                    changeTitleStyle(0);
//                    切换到可抢列表的时候要自动刷新
                    mCustomerFragment.setRefreshing();
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(one, 0, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, 0, 0, 0);
                    } else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, 0, 0, 0);
                    }
                    break;
                case 1:
                    changeTitleStyle(1);
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(0, one, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, one, 0, 0);
                    } else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, one, 0, 0);
                    }
                    break;
                case 2:
                    changeTitleStyle(2);
//                    切换到可抢列表的时候要自动刷新
                    mHouseFragment.setRefreshing();
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(0, two, 0, 0);
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, two, 0, 0);
                    } else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, two, 0, 0);
                    }
                    break;
                case 3:
                    changeTitleStyle(3);
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(0, three, 0, 0);
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, three, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, three, 0, 0);
                    }
                    break;
            }
            currIndex = position;
            if (animation != null) {
                animation.setFillAfter(true);// True:图片停在动画结束位置
                animation.setDuration(300);
            }
            imgGrabCursor.startAnimation(animation);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public void onEventMainThread(ReFreshGrabListEvent event) {
        if (event.getTag().equals(GrabHouseFragment.TAG)) {
            mGrabbedCustomerFragment.setRefreshing();
        } else if (event.getTag().equals(GrabHouseFragment.TAG)) {
            mGrabbedHouseFragment.setRefreshing();
        }
    }

    /**
     * 改变小标题文字的大小和颜色
     *
     * @param index
     */
    private void changeTitleStyle(int index) {
        Resources resources = getResources();
        tvGrabCustomer.setTextColor(index == 0 ? resources.getColor(R.color.my_orange_two) : resources.getColor(R.color.my_gray_one));
        tvGrabbedCustomer.setTextColor(index == 1 ? resources.getColor(R.color.my_orange_two) : resources.getColor(R.color.my_gray_one));
        tvGrabHouse.setTextColor(index == 2 ? resources.getColor(R.color.my_orange_two) : resources.getColor(R.color.my_gray_one));
        tvGrabbedHouse.setTextColor(index == 3 ? resources.getColor(R.color.my_orange_two) : resources.getColor(R.color.my_gray_one));
        tvGrabCustomer.setTextSize(index == 0 ? 16 : 14);
        tvGrabbedCustomer.setTextSize(index == 1 ? 16 : 14);
        tvGrabHouse.setTextSize(index == 2 ? 16 : 14);
        tvGrabbedHouse.setTextSize(index == 3 ? 16 : 14);
    }

}
