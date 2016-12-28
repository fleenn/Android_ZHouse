package com.zfb.house.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Matrix;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import com.zfb.house.adapter.CollectHouseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的->收藏的房源
 * Created by Administrator on 2016/7/25.
 */
@Layout(id = R.layout.activity_collect_house)
public class CollectHousesActivity extends LemonActivity {

    @FieldView(id = R.id.vp_shop)
    private ViewPager vpCollect;
    @FieldView(id = R.id.img_cursor)
    private ImageView imgCursor;
    //出售文字
    @FieldView(id = R.id.txt_sell)
    private TextView txtSell;
    //出租文字
    @FieldView(id = R.id.txt_rent)
    private TextView txtRent;
    //收藏的房源——出售界面
    private CollectSellFragment sellFragment;
    //收藏的房源——出租界面
    private CollectRentFragment rentFragment;
    // 指示器偏移量
    private int offset = 0;
    //当前pager索引
    private int currIndex = 0;
    private boolean isEdit = false;

    @Override
    protected void initView() {
        setCenterText(R.string.img_mine_collect_houses);
        setRtText(R.string.edit);
        sellFragment = new CollectSellFragment();
        rentFragment = new CollectRentFragment();

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(sellFragment);
        fragments.add(rentFragment);
        initCursorPos();
        CollectHouseAdapter collectHouseAdapter = new CollectHouseAdapter(getSupportFragmentManager(), fragments);
        vpCollect.setAdapter(collectHouseAdapter);
        vpCollect.addOnPageChangeListener(new MyPageChangeListener());
        vpCollect.setCurrentItem(0);
        vpCollect.setOffscreenPageLimit(2);
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        finish();
    }

    /**
     * 编辑
     */
    @OnClick(id = R.id.tv_title_rt)
    public void toEdit() {
        if (isEdit) {
            mTvTitleRt.setText(R.string.edit);
            isEdit = false;
        } else {
            mTvTitleRt.setText(R.string.complete);
            isEdit = true;
        }
        if (vpCollect.getCurrentItem() == 0) {//出售
            sellFragment.editRefreshView(isEdit);
        } else if (vpCollect.getCurrentItem() == 1) {//出租
            rentFragment.editRefreshView(isEdit);
        }
    }

    /**
     * 初始化指示器位置
     */
    public void initCursorPos() {
        // 初始化动画
        if (ScreenUtil.screenWidth == 0) {
            ScreenUtil.init(mContext);
        }
        int screenWidth = ScreenUtil.screenWidth;// 获取分辨率宽度
        offset = screenWidth / 2;// 计算偏移量

        Matrix matrix = new Matrix();
        matrix.postTranslate(0, 0);
        imgCursor.setImageMatrix(matrix);// 设置动画初始位置
    }

    /**
     * cursor平移
     */
    public class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        int one = offset;// 页卡1 -> 页卡2 偏移量

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Animation animation = null;
            switch (position) {
                case 0://出售
                    changeTitleStyle(0);
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(one, 0, 0, 0);
                    }
                    break;
                case 1://出租
                    changeTitleStyle(1);
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(0, one, 0, 0);
                    }
                    break;
            }
            currIndex = position;
            if (null != animation) {
                animation.setFillAfter(true);// True:图片停在动画结束位置
                animation.setDuration(300);
            }
            imgCursor.startAnimation(animation);
        }

        /**
         * 切换pager时隐藏软键盘
         *
         * @param state
         */
        @Override
        public void onPageScrollStateChanged(int state) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(vpCollect.getWindowToken(), 0);

            isEdit = false;
            mTvTitleRt.setText(R.string.edit);
            sellFragment.editRefreshView(isEdit);
            rentFragment.editRefreshView(isEdit);
        }
    }

    /**
     * 改变小标题文字的大小和颜色
     *
     * @param index
     */
    private void changeTitleStyle(int index) {
        Resources resources = getResources();
        txtSell.setTextColor(index == 0 ? resources.getColor(R.color.my_orange_two) : resources.getColor(R.color.my_gray_one));
        txtRent.setTextColor(index == 1 ? resources.getColor(R.color.my_orange_two) : resources.getColor(R.color.my_gray_one));
        txtSell.setTextSize(index == 0 ? 16 : 14);
        txtRent.setTextSize(index == 1 ? 16 : 14);
    }

    /**
     * 出售
     */
    @OnClick(id = R.id.txt_sell)
    public void sellClick() {
        vpCollect.setCurrentItem(0);
    }

    /**
     * 出租
     */
    @OnClick(id = R.id.txt_rent)
    public void rentClick() {
        vpCollect.setCurrentItem(1);
    }

}
