package com.zfb.house.ui;

import android.content.Context;
import android.content.Intent;
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
import com.zfb.house.adapter.CashingCenterAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 积分商城 -> 兑换中心
 * Created by HourGlassRemember on 2016/8/5.
 */
@Layout(id = R.layout.activity_cashing_center)
public class CashingCenterActivity extends LemonActivity {

    @FieldView(id = R.id.vp_cashing)
    private ViewPager vpCashing;
    @FieldView(id = R.id.img_cashing_cursor)
    private ImageView imgCashingCursor;
    //实物兑换文字
    @FieldView(id = R.id.txt_entity)
    private TextView tvEntity;
    //特权兑换文字
    @FieldView(id = R.id.txt_privilege)
    private TextView tvPrivilege;
    // 指示器偏移量
    private int offset = 0;
    //当前pager索引
    private int currIndex = 0;
    //兑换中心——实物兑换
    private EntityCashingFragment entityFragment;
    //兑换中心——特权兑换
    private PrivilegeCashingFragment privilegeFragment;

    @Override
    protected void initView() {
        setCenterText(R.string.title_cashing_center);
        setRtImg(R.drawable.shopping_cart);

        entityFragment = new EntityCashingFragment();
        privilegeFragment = new PrivilegeCashingFragment();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(entityFragment);
        fragments.add(privilegeFragment);
        initCursorPos();
        vpCashing.setAdapter(new CashingCenterAdapter(getSupportFragmentManager(), fragments));
        vpCashing.addOnPageChangeListener(new MyPageChangeListener());
        vpCashing.setCurrentItem(0);
        vpCashing.setOffscreenPageLimit(2);
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
        imgCashingCursor.setImageMatrix(matrix);// 设置动画初始位置
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
                case 0://实物兑换
                    changeTitleStyle(0);
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(one, 0, 0, 0);
                    }
                    break;
                case 1://特权兑换
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
            imgCashingCursor.startAnimation(animation);
        }

        /**
         * 切换pager时隐藏软键盘
         *
         * @param state
         */
        @Override
        public void onPageScrollStateChanged(int state) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(vpCashing.getWindowToken(), 0);
        }
    }

    /**
     * 改变小标题文字的大小和颜色
     *
     * @param index
     */
    private void changeTitleStyle(int index) {
        Resources resources = getResources();
        tvEntity.setTextColor(index == 0 ? resources.getColor(R.color.my_orange_two) : resources.getColor(R.color.my_gray_one));
        tvPrivilege.setTextColor(index == 1 ? resources.getColor(R.color.my_orange_two) : resources.getColor(R.color.my_gray_one));
        tvEntity.setTextSize(index == 0 ? 16 : 14);
        tvPrivilege.setTextSize(index == 1 ? 16 : 14);
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        finish();
    }

    /**
     * 我的订单
     */
    @OnClick(id = R.id.img_title_rt)
    public void toMyOrder() {
        startActivity(new Intent(mContext, MyOrderActivity.class));
    }

    /**
     * 实物兑换
     */
    @OnClick(id = R.id.txt_entity)
    public void toEntityCashing() {
        vpCashing.setCurrentItem(0);
    }

    /**
     * 特权兑换
     */
    @OnClick(id = R.id.txt_privilege)
    public void toPrivilegeCashing() {
        vpCashing.setCurrentItem(1);
    }

}
