package com.zfb.house.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Matrix;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lemon.LemonFragment;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.event.MomentsTitlePhotoEvent;
import com.lemon.event.NotifyEvent;
import com.lemon.event.RefreshEvent;
import com.lemon.util.ParamUtils;
import com.lemon.util.ScreenUtil;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.MomentsPagerAdapter;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.model.param.ReadMsgParam;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.badgeview.BGABadgeTextView;
import cn.jpush.android.api.JPushInterface;

/**
 * 房友圈
 * Created by Administrator on 2016/4/24.
 */
@Layout(id = R.layout.fragment_moments)
public class MomentsFragment extends LemonFragment {

    public static final int REQUEST_SEND_MOMENTS = 1;
    public static final int REQUEST_LOCATE = 2;
    public static final long RANGE = 5000;

    //    三个房友圈fragment容器
    @FieldView(id = R.id.pager_moments_schema)
    private ViewPager pagerMomentsSchema;
    //    tab页下方指示器
    @FieldView(id = R.id.img_cursor)
    private ImageView imgCursor;
    @FieldView(id = R.id.rlayout_moments_relevant)
    private RelativeLayout rlayoutMomentsRelevant;
    //    附近tab
    @FieldView(id = R.id.tv_neighbor)
    private TextView tvNeighbor;
    //    好友tab
    @FieldView(id = R.id.tv_friends)
    private TextView tvFriends;
    //    与我相关tab
    @FieldView(id = R.id.tv_relevant)
    private TextView tvRelevant;
    //    与我相关提醒
    @FieldView(id = R.id.bga_tab_moments)
    private BGABadgeTextView bgaTabMoments;

    private List<Fragment> mFrangments;
    //    指示器偏移量
    private int offset = 0;
    //    当前pager索引
    private int currIndex = 0;
    private RelevantMomentsFragment relevantMomentsFragment;
    private FriendsMomentsFragment friendsMomentsFragment;
    private NeighborMomentsFragment neighborMomentsFragment;

    /**
     * 跳转到我的房友圈界面
     */
    @OnClick(id = R.id.img_title_lt, anonymonus = false)
    public void toMyMoments() {
        startActivity(new Intent(getActivity(), MyMomentsActivity.class));
    }

    /**
     * 登录
     */
    @OnClick(id = R.id.tv_title_lt, anonymonus = false)
    public void toLogin() {
        startActivity(new Intent(getActivity(), MyMomentsActivity.class));
    }

    /**
     * 跳转到发布房友圈界面
     */
    @OnClick(id = R.id.rlayout_title_rt_img, anonymonus = false)
    public void toSendMoments() {
        Intent intent = new Intent(getActivity(), SendMomentsActivity.class);
        startActivityForResult(intent, REQUEST_SEND_MOMENTS);
    }

    @Override
    protected void initView() {
        setCenterText(R.string.title_moments);
        ViewGroup.LayoutParams layoutParams = mImgTitleLt.getLayoutParams();
        int px = ScreenUtil.dip2px(getActivity(), 30);
        layoutParams.height = px;
        layoutParams.width = px;
        mImgTitleLt.setLayoutParams(layoutParams);
        if (ParamUtils.isEmpty(SettingUtils.get(getActivity(), "token", null))) {
            setLtText(R.string.title_login);
        } else {
            setLtImg(UserBean.getInstance(getContext()).photo);
        }
        setRtImg(R.drawable.friends_one_camera);
        mFrangments = new ArrayList();
        neighborMomentsFragment = new NeighborMomentsFragment();
        friendsMomentsFragment = new FriendsMomentsFragment();
        relevantMomentsFragment = new RelevantMomentsFragment();
        mFrangments.add(neighborMomentsFragment);
        mFrangments.add(friendsMomentsFragment);
        mFrangments.add(relevantMomentsFragment);
        tvNeighbor.setOnClickListener(new MyOnClickListener(0));
        tvFriends.setOnClickListener(new MyOnClickListener(1));
        rlayoutMomentsRelevant.setOnClickListener(new MyOnClickListener(2));
        initCursorPos();
        MomentsPagerAdapter momentsPagerAdapter = new MomentsPagerAdapter(getActivity().getSupportFragmentManager(), mFrangments);
        pagerMomentsSchema.setAdapter(momentsPagerAdapter);
        pagerMomentsSchema.setCurrentItem(0);
        pagerMomentsSchema.addOnPageChangeListener(new MyPageChangeListener());
        pagerMomentsSchema.setOffscreenPageLimit(3);
        if (!ParamUtils.isEmpty(SettingUtils.get(getActivity(), "token", null))) {
            int badge = SettingUtils.get(getContext(), UserBean.getInstance(getContext()).phone, 0);
            if (badge != 0) {
                bgaTabMoments.showCirclePointBadge();
            }
        }

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
            pagerMomentsSchema.setCurrentItem(index);
        }
    }

    //初始化指示器位置
    public void initCursorPos() {
        // 初始化动画
        if (ScreenUtil.screenWidth == 0) {
            ScreenUtil.init(getActivity());
        }
        int screenWidth = ScreenUtil.screenWidth;// 获取分辨率宽度
        offset = screenWidth / 3;// 计算偏移量

        Matrix matrix = new Matrix();
        matrix.postTranslate(0, 0);
        imgCursor.setImageMatrix(matrix);
        tvNeighbor.setSelected(true);// 设置动画初始位置
    }

    //    cursor平移
    public class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        int one = offset;// 页卡1 -> 页卡2 偏移量
        int two = one * 2;// 页卡1 -> 页卡3 偏移量

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    changeTitleStyle(0);
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(one, 0, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, 0, 0, 0);
                    }
                    break;
                case 1:
                    changeTitleStyle(1);
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(0, one, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, one, 0, 0);
                    }
                    break;
                case 2:
                    if (!ParamUtils.isEmpty(SettingUtils.get(getContext(), "token", ""))) {
                        readMsg();
                    }
                    changeTitleStyle(2);
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(0, two, 0, 0);
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, two, 0, 0);
                    }
                    break;
            }
            currIndex = arg0;
            if (animation != null) {
                animation.setFillAfter(true);// True:图片停在动画结束位置
                animation.setDuration(300);
            }
            imgCursor.startAnimation(animation);
        }

        //        切换pager时隐藏软键盘
        @Override
        public void onPageScrollStateChanged(int state) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(pagerMomentsSchema.getWindowToken(), 0);
        }
    }

    //    清空推送未读提示
    private void readMsg() {
        ((MainActivity) getActivity()).hideBadge();
        ReadMsgParam readMsgParam = new ReadMsgParam();
        readMsgParam.setToken(SettingUtils.get(getContext(), "token", ""));
        apiManager.readMsg(readMsgParam);
        bgaTabMoments.hiddenBadge();
        JPushInterface.clearAllNotifications(getActivity());
        SettingUtils.set(getContext(), UserBean.getInstance(getContext()).phone, 0);
    }

    /**
     * 改变小标题文字的大小和颜色
     *
     * @param index
     */
    private void changeTitleStyle(int index) {
        tvNeighbor.setSelected(index == 0);
        tvFriends.setSelected(index == 1);
        tvRelevant.setSelected(index == 2);
        Resources resources = getResources();
        tvNeighbor.setTextColor(index == 0 ? resources.getColor(R.color.my_orange_two) : resources.getColor(R.color.my_gray_one));
        tvFriends.setTextColor(index == 1 ? resources.getColor(R.color.my_orange_two) : resources.getColor(R.color.my_gray_one));
        tvRelevant.setTextColor(index == 2 ? resources.getColor(R.color.my_orange_two) : resources.getColor(R.color.my_gray_one));
        tvNeighbor.setTextSize(index == 0 ? 16 : 14);
        tvFriends.setTextSize(index == 1 ? 16 : 14);
        tvRelevant.setTextSize(index == 2 ? 16 : 14);
    }

    public void onEventMainThread(NotifyEvent event) {
        bgaTabMoments.showCirclePointBadge();
    }

    /**
     * 修改头像后会收到该事件，刷新房友圈右上角头像
     *
     * @param event
     */
    public void onEventMainThread(MomentsTitlePhotoEvent event) {
        setLtImg(UserBean.getInstance(getContext()).photo);
    }

    public void onEventMainThread(RefreshEvent event) {
        bgaTabMoments.hiddenBadge();
        setLtText(R.string.title_login);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            switch (requestCode) {
                case REQUEST_SEND_MOMENTS:
                    neighborMomentsFragment.setRefreshing();
                    break;
                default:
                    break;
            }
        }
    }

}
