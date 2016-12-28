package com.zfb.house.ui;

import android.content.res.Resources;
import android.graphics.Matrix;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.util.ScreenUtil;
import com.zfb.house.R;
import com.zfb.house.adapter.UserEntrustPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * （用户）我的委托：呼叫委托、房源委托、聊天委托
 * Created by Administrator on 2016/5/24.
 */
@Layout(id = R.layout.activity_entrust)
public class UserEntrustActivity extends LemonActivity {

    @FieldView(id = R.id.pager_user_entrust)
    private ViewPager pagerEntrustSchema;
    @FieldView(id = R.id.img_user_entrust)
    private ImageView imgCursor;

    //右上角的“编辑”文字
    @FieldView(id = R.id.tv_title_rt)
    private TextView txtTitleRight;
    //呼叫委托
    @FieldView(id = R.id.txt_entrust_call)
    private TextView txtCall;
    //房源委托
    @FieldView(id = R.id.txt_entrust_house)
    private TextView txtHouse;
    //聊天委托
    @FieldView(id = R.id.txt_entrust_chat)
    private TextView txtChat;
    private List<Fragment> mFragments;
    private int offset = 0;
    private int currIndex = 0;
    //呼叫委托
    private UserCallEntrustFragment userCallEntrustFragment;
    //房源委托
    private UserHouseEntrustFragment userHouseEntrustFragment;
    //聊天委托
    private PurposeHouseFragment purposeHouseFragment;
    private boolean isEdit = false;

    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {//返回
        finish();
    }

    @OnClick(id = R.id.tv_title_rt)
    public void toEdit() {//编辑
        if (isEdit) {
            txtTitleRight.setText(R.string.edit);
            isEdit = false;
        } else {
            txtTitleRight.setText(R.string.complete);
            isEdit = true;
        }
        if (pagerEntrustSchema.getCurrentItem() == 0) {
            userCallEntrustFragment.editRefreshView(isEdit);
        } else if (pagerEntrustSchema.getCurrentItem() == 1) {
            userHouseEntrustFragment.editRefreshView(isEdit);
        }
    }

    @Override
    protected void initView() {
        setCenterText(R.string.img_mine_entrust);
        setRtText(R.string.edit);
        //默认呼叫委托
        txtCall.performClick();
        mFragments = new ArrayList<>();
        userCallEntrustFragment = new UserCallEntrustFragment();
        userHouseEntrustFragment = new UserHouseEntrustFragment();
        purposeHouseFragment = new PurposeHouseFragment();
        mFragments.add(userCallEntrustFragment);
        mFragments.add(userHouseEntrustFragment);
        mFragments.add(purposeHouseFragment);
        txtCall.setOnClickListener(new MyOnClickListener(0));
        txtHouse.setOnClickListener(new MyOnClickListener(1));
        txtChat.setOnClickListener(new MyOnClickListener(2));
        initCursorPos();
        UserEntrustPagerAdapter userEntrustPagerAdapter = new UserEntrustPagerAdapter(getSupportFragmentManager(), mFragments);
        pagerEntrustSchema.setAdapter(userEntrustPagerAdapter);
        pagerEntrustSchema.addOnPageChangeListener(new MyPageChangeListener());
        pagerEntrustSchema.setOffscreenPageLimit(3);
    }

    /**
     * 头标点击监听
     */
    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            pagerEntrustSchema.setCurrentItem(index);
        }
    }

    /**
     * 初始化指示器位置
     */
    private void initCursorPos() {
        if (ScreenUtil.screenWidth == 0) {
            ScreenUtil.init(mContext);
        }
        int screenWidth = ScreenUtil.screenWidth;//获取分辨率宽度
        offset = screenWidth / 3;//计算偏移量

        Matrix matrix = new Matrix();
        matrix.postTranslate(0, 0);
        imgCursor.setImageMatrix(matrix);//设置动画初始化位置
    }

    public class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        private int one = offset;//页卡1->页卡2的偏移量
        private int two = one * 2;//页卡2->页卡3的偏移量

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Animation animation = null;
            switch (position) {
                case 0://呼叫委托
                    changeTitleStyle(0);
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(one, 0, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, 0, 0, 0);
                    }
                    break;
                case 1://房源委托
                    changeTitleStyle(1);
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(0, one, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, one, 0, 0);
                    }
                    break;
                case 2://聊天委托
                    changeTitleStyle(2);
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(0, two, 0, 0);
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, two, 0, 0);
                    }
                    break;
            }
            currIndex = position;
            if (null != animation) {
                animation.setFillAfter(true);//True：图片停在动画位置
                animation.setDuration(300);
            }
            imgCursor.startAnimation(animation);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(pagerEntrustSchema.getWindowToken(), 0);

            isEdit = false;
            txtTitleRight.setText(R.string.edit);
            userHouseEntrustFragment.editRefreshView(isEdit);
            userCallEntrustFragment.editRefreshView(isEdit);
        }
    }

    /**
     * 改变小标题文字的大小和颜色
     *
     * @param index
     */
    private void changeTitleStyle(int index) {
        Resources resources = getResources();
        txtCall.setTextColor(index == 0 ? resources.getColor(R.color.my_orange_two) : resources.getColor(R.color.my_gray_one));
        txtHouse.setTextColor(index == 1 ? resources.getColor(R.color.my_orange_two) : resources.getColor(R.color.my_gray_one));
        txtChat.setTextColor(index == 2 ? resources.getColor(R.color.my_orange_two) : resources.getColor(R.color.my_gray_one));
        txtCall.setTextSize(index == 0 ? 16 : 14);
        txtHouse.setTextSize(index == 1 ? 16 : 14);
        txtChat.setTextSize(index == 2 ? 16 : 14);
    }

}
