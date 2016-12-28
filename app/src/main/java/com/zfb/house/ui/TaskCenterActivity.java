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
import com.zfb.house.adapter.TaskCenterAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 积分商城 -> 任务中心
 * Created by HourGlassRemember on 2016/8/5.
 */
@Layout(id = R.layout.activity_task_center)
public class TaskCenterActivity extends LemonActivity {

    @FieldView(id = R.id.vp_task_center)
    private ViewPager vpTaskCenter;
    @FieldView(id = R.id.img_task_cursor)
    private ImageView imgTaskCursor;
    //每日任务
    @FieldView(id = R.id.txt_task_day)
    private TextView txtTaskDay;
    //每月任务
    @FieldView(id = R.id.txt_task_month)
    private TextView txtTaskMonth;
    //新手任务
    @FieldView(id = R.id.txt_task_novice)
    private TextView txtTaskNovice;
    //活动任务
    @FieldView(id = R.id.txt_task_activity)
    private TextView txtTaskActivity;

    // 指示器偏移量
    private int offset = 0;
    //当前pager索引
    private int currIndex = 0;
    //任务中心——每日任务
    private DayTaskFragment dayTaskFragment;
    //任务中心——每月任务
    private MonthTaskFragment monthTaskFragment;
    //任务中心——新手任务
    private NoviceTaskFragment noviceTaskFragment;
    //任务中心——活动任务
    private ActivityTaskFragment activityTaskFragment;

    @Override
    protected void initView() {
        setCenterText(R.string.title_task_center);

        dayTaskFragment = new DayTaskFragment();
        monthTaskFragment = new MonthTaskFragment();
        noviceTaskFragment = new NoviceTaskFragment();
        activityTaskFragment = new ActivityTaskFragment();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(dayTaskFragment);
        fragments.add(monthTaskFragment);
        fragments.add(noviceTaskFragment);
        fragments.add(activityTaskFragment);
        initCursorPos();
        TaskCenterAdapter taskCenterAdapter = new TaskCenterAdapter(getSupportFragmentManager(), fragments);
        vpTaskCenter.setAdapter(taskCenterAdapter);
        vpTaskCenter.addOnPageChangeListener(new MyPageChangeListener());
        vpTaskCenter.setCurrentItem(0);
        vpTaskCenter.setOffscreenPageLimit(4);
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
        offset = screenWidth / 4;// 计算偏移量

        Matrix matrix = new Matrix();
        matrix.postTranslate(0, 0);
        imgTaskCursor.setImageMatrix(matrix);// 设置动画初始位置
    }

    /**
     * cursor平移
     */
    public class MyPageChangeListener implements ViewPager.OnPageChangeListener {
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
                case 0://每日任务
                    changeTitleStyle(0);
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(one, 0, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, 0, 0, 0);
                    } else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, 0, 0, 0);
                    }
                    break;
                case 1://每月任务
                    changeTitleStyle(1);
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(0, one, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, one, 0, 0);
                    } else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, one, 0, 0);
                    }
                    break;
                case 2://新手任务
                    changeTitleStyle(2);
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(0, two, 0, 0);
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, two, 0, 0);
                    } else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, two, 0, 0);
                    }
                    break;
                case 3://活动任务
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
            if (null != animation) {
                animation.setFillAfter(true);// True:图片停在动画结束位置
                animation.setDuration(300);
            }
            imgTaskCursor.startAnimation(animation);
        }

        /**
         * 切换pager时隐藏软键盘
         *
         * @param state
         */
        @Override
        public void onPageScrollStateChanged(int state) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(vpTaskCenter.getWindowToken(), 0);
        }
    }

    /**
     * 改变小标题文字的大小和颜色
     *
     * @param index
     */
    private void changeTitleStyle(int index) {
        Resources resources = getResources();
        txtTaskDay.setTextColor(index == 0 ? resources.getColor(R.color.my_orange_two) : resources.getColor(R.color.my_gray_one));
        txtTaskMonth.setTextColor(index == 1 ? resources.getColor(R.color.my_orange_two) : resources.getColor(R.color.my_gray_one));
        txtTaskNovice.setTextColor(index == 2 ? resources.getColor(R.color.my_orange_two) : resources.getColor(R.color.my_gray_one));
        txtTaskActivity.setTextColor(index == 3 ? resources.getColor(R.color.my_orange_two) : resources.getColor(R.color.my_gray_one));
        txtTaskDay.setTextSize(index == 0 ? 16 : 14);
        txtTaskMonth.setTextSize(index == 1 ? 16 : 14);
        txtTaskNovice.setTextSize(index == 2 ? 16 : 14);
        txtTaskActivity.setTextSize(index == 3 ? 16 : 14);
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        finish();
    }

    /**
     * 每日任务
     */
    @OnClick(id = R.id.txt_task_day)
    public void toDayTask() {
        vpTaskCenter.setCurrentItem(0);
    }

    /**
     * 每月任务
     */
    @OnClick(id = R.id.txt_task_month)
    public void toMonthTask() {
        vpTaskCenter.setCurrentItem(1);
    }

    /**
     * 新手任务
     */
    @OnClick(id = R.id.txt_task_novice)
    public void toNoviceTask() {
        vpTaskCenter.setCurrentItem(2);
    }

    /**
     * 活动任务
     */
    @OnClick(id = R.id.txt_task_activity)
    public void toActivityTask() {
        vpTaskCenter.setCurrentItem(3);
    }

}
