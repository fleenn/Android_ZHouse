package com.zfb.house.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.config.Config;
import com.lemon.event.UpdateUserInfoEvent;
import com.lemon.model.StatusCode;
import com.lemon.util.GlideUtil;
import com.lemon.util.ParamUtils;
import com.lemon.util.ScreenUtil;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.BrokerPersonalAdapter;
import com.zfb.house.component.CircleImageView;
import com.zfb.house.component.RatingBar;
import com.zfb.house.emchat.ChatActivity;
import com.zfb.house.emchat.CommonUtils;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.model.param.DeleteFriendParam;
import com.zfb.house.model.param.UserPersonalParam;
import com.zfb.house.model.result.DeleteFriendResult;
import com.zfb.house.model.result.UserPersonalResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 经纪人：个人资料
 * Created by HourGlassRemember on 2016/8/25.
 */
@Layout(id = R.layout.activity_broker_personal)
public class BrokerPersonalActivity extends LemonActivity {

    private static final String TAG = "BrokerPersonalActivity";
    //编辑好友备注的请求码
    private final int REQUEST_MODIFY_REMARK = 0x1;

    //好友 -> 更多
    @FieldView(id = R.id.img_broker_more)
    private ImageView imgMore;
    //头像
    @FieldView(id = R.id.img_broker_avatar)
    private CircleImageView imgAvatar;
    //备注
    @FieldView(id = R.id.txt_broker_remark)
    private TextView txtRemark;
    //用户类型
    @FieldView(id = R.id.img_broker_type)
    private ImageView imgType;
    //性别
    @FieldView(id = R.id.img_broker_sex)
    private ImageView imgSex;
    //星级
    @FieldView(id = R.id.rating_bar_broker)
    private RatingBar rbStar;
    //Ta 的房友圈 or 我的房友圈
    @FieldView(id = R.id.txt_broker_moment)
    private TextView txtMoments;
    //Ta 的店铺 or 我的店铺
    @FieldView(id = R.id.txt_broker_shop)
    private TextView txtShop;
    //发消息 or 我的个人中心
    @FieldView(id = R.id.txt_broker_chat)
    private TextView txtChat;

    //服务信息文字
    @FieldView(id = R.id.txt_broker_service)
    private TextView txtService;
    //基本信息文字
    @FieldView(id = R.id.txt_broker_basic)
    private TextView txtBasic;
    @FieldView(id = R.id.vp_broker_personal)
    private ViewPager vpBroker;
    @FieldView(id = R.id.img_broker_cursor)
    private ImageView imgBrokerCursor;
    // 指示器偏移量
    private int offset = 0;
    //当前pager索引
    private int currIndex = 0;
    //经纪人个人资料——服务信息
    private BrokerServiceFragment serviceFragment;
    //经纪人个人资料——基本信息
    private BrokerBasicFragment basicFragment;

    //右上角弹出框
    private PopupWindow mPopWindow;
    //底部弹出框
    private PopupWindow deletePopWindow;
    //标识是否为好友经纪人
    private Boolean isBrokerFriend;
    //传过来的经纪人ID
    private String brokerId;
    private String token;
    //好友备注
    private String remark;
    //remarek pinyin
    private String pinyin;
    //是否删除好友
    private boolean isDelete;
    //当前item在列表中的位置
    private int position;
    //目标用户头像，带往聊天页面
    private String mPhoto;
    //是否是自己的详情页
    private boolean isMine;
    //经纪人实体
    private UserBean item;

    @Override
    protected void initView() {
        token = SettingUtils.get(mContext, "token", null);
        Intent intent = getIntent();
        //传过来的经纪人ID
        brokerId = intent.getStringExtra("brokerId");
        //判断是否为自己的详情页
        if (!ParamUtils.isNull(UserBean.getInstance(mContext))) {
            if (brokerId.equals(UserBean.getInstance(mContext).id)) {
                isMine = true;
            }
        }
        //设置按钮上面的文字
        Resources resources = getResources();
        if (isMine) {//我的个人中心
            txtChat.setText(R.string.label_mine_personal_center);
            txtChat.setCompoundDrawablesWithIntrinsicBounds(null, resources.getDrawable(R.drawable.personal_center), null, null);
            txtChat.setTextColor(resources.getColor(R.color.red_one));
        } else {//发消息
            txtChat.setText(R.string.label_mine_send_message);
            txtChat.setCompoundDrawablesWithIntrinsicBounds(null, resources.getDrawable(R.drawable.personal_chat), null, null);
            txtChat.setTextColor(resources.getColor(R.color.green_one));
        }
        txtShop.setText(isMine ? R.string.label_to_my_shop : R.string.label_to_he_shop);
        txtMoments.setText(isMine ? R.string.img_mine_moments : R.string.label_mine_friend_moments);
        //传过来的标识（是否为好友），如果没传的话默认不是好友
        isBrokerFriend = intent.getBooleanExtra("isBrokerFriend", false);
        //如果是好友经纪人的话，就添加备注和右上角图标
        if (isBrokerFriend) {
            remark = intent.getStringExtra("remark");
            position = intent.getIntExtra("position", 0);
            imgMore.setVisibility(View.VISIBLE);
            imgMore.setImageResource(R.drawable.personal_more);
            imgMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //显示右上角弹出框
                    showPopupWindow();
                }
            });
        }
        initCursorPos();
        vpBroker.addOnPageChangeListener(new MyPageChangeListener());
        vpBroker.setCurrentItem(0);
        vpBroker.setOffscreenPageLimit(2);
    }

    @Override
    protected void initData() {
        if (isMine) {//查看自己的个人资料
            setPersonalData(UserBean.getInstance(mContext));
        } else {//查看其他人的个人资料
            //调用“获取用户详细信息”接口
            UserPersonalParam userPersonalParam = new UserPersonalParam();
            userPersonalParam.setId(brokerId);
            userPersonalParam.setTag(TAG);
            apiManager.getUserDetail(userPersonalParam);
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
        imgBrokerCursor.setImageMatrix(matrix);// 设置动画初始位置
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
                case 0://服务信息
                    changeTitleStyle(0);
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(one, 0, 0, 0);
                    }
                    break;
                case 1://基本信息
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
            imgBrokerCursor.startAnimation(animation);
        }

        /**
         * 切换pager时隐藏软键盘
         *
         * @param state
         */
        @Override
        public void onPageScrollStateChanged(int state) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(vpBroker.getWindowToken(), 0);
        }
    }

    /**
     * 改变小标题文字的大小和颜色
     *
     * @param index
     */
    private void changeTitleStyle(int index) {
        Resources resources = getResources();
        txtService.setTextColor(index == 0 ? resources.getColor(R.color.my_orange_two) : resources.getColor(R.color.my_gray_one));
        txtBasic.setTextColor(index == 1 ? resources.getColor(R.color.my_orange_two) : resources.getColor(R.color.my_gray_one));
        txtService.setTextSize(index == 0 ? 16 : 14);
        txtBasic.setTextSize(index == 1 ? 16 : 14);
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.img_broker_back)
    public void toBack() {
        Intent intent = new Intent();
        intent.putExtra("remark", remark);
        intent.putExtra("position", position);
        intent.putExtra("pinyin", pinyin);
        intent.putExtra("isDelete", isDelete);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 处理返回键事件
     */
    @Override
    public void onBackPressed() {
        toBack();
        super.onBackPressed();
    }

    /**
     * 服务信息
     */
    @OnClick(id = R.id.txt_broker_service)
    public void toBrokerService() {
        vpBroker.setCurrentItem(0);
    }

    /**
     * 基本信息
     */
    @OnClick(id = R.id.txt_broker_basic)
    public void toBrokerBasic() {
        vpBroker.setCurrentItem(1);
    }

    /**
     * 点击头像放大
     */
    @OnClick(id = R.id.img_broker_avatar)
    public void enlargeAvatar() {
        Intent intent = new Intent(mContext, BrokerShopPhotoActivity.class);
        String photo;
        if (isMine) {
            photo = ParamUtils.isEmpty(UserBean.getInstance(mContext).photo) ? "isDefault" : "isMine";
        } else {
            photo = ParamUtils.isEmpty(item.photo) ? "isDefault" : item.photo;
        }
        intent.putExtra("photo", photo);
        startActivity(intent);
    }

    /**
     * 显示右上角弹出框
     */
    private void showPopupWindow() {
        //一个自定义的布局，作为弹出框显示的内容
        final View contentView = LayoutInflater.from(this).inflate(R.layout.popwindow_personal_isfriends, null);
        mPopWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setContentView(contentView);
        //设置背景
        mPopWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        LinearLayout llayoutRemark = (LinearLayout) contentView.findViewById(R.id.rlayout_remark);
        LinearLayout llayoutComplaint = (LinearLayout) contentView.findViewById(R.id.rlayout_complaint);
        LinearLayout llayoutDelete = (LinearLayout) contentView.findViewById(R.id.rlayout_delete);
        //“修改备注”的监听事件
        llayoutRemark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ModifyRemarkActivity.class);
                intent.putExtra("name", item.name);
                intent.putExtra("remark", remark);
                intent.putExtra("friendId", brokerId);
                intent.putExtra("userType", "1");
                startActivityForResult(intent, REQUEST_MODIFY_REMARK);
                mPopWindow.dismiss();
            }
        });
        //“投诉”的监听事件
        llayoutComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ComplaintFriendActivity.class);
                intent.putExtra("id", brokerId);
                startActivity(intent);
                mPopWindow.dismiss();
            }
        });
        //“删除”的监听事件
        llayoutDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeletePopWindow();
                mPopWindow.dismiss();
            }
        });

        contentView.setFocusableInTouchMode(true);
        contentView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_MENU && mPopWindow.isShowing()) {
                    mPopWindow.dismiss();
                    return true;
                }
                return false;
            }
        });

        //设置弹出框的显示位置
        mPopWindow.showAsDropDown(imgMore, ScreenUtil.dip2px(mContext, -73), ScreenUtil.dip2px(mContext, -17));
    }

    /**
     * 显示底部弹出框
     */
    public void showDeletePopWindow() {
        //一个自定义的布局，作为弹出框显示的内容
        final View contentView = LayoutInflater.from(this).inflate(R.layout.popupwindow_delete_friend, null);
        deletePopWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        deletePopWindow.setContentView(contentView);
        //设置背景
        deletePopWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        RelativeLayout rlayoutName = (RelativeLayout) contentView.findViewById(R.id.rlayout_friend_name);
        TextView deleteName = (TextView) contentView.findViewById(R.id.txt_friend_name);
        RelativeLayout rlayoutDelete = (RelativeLayout) contentView.findViewById(R.id.rlayout_friend_delete);
        RelativeLayout rlayoutCancel = (RelativeLayout) contentView.findViewById(R.id.rlayout_friend_cancel);
        String str = "将联系人“" + txtRemark.getText().toString() + "”删除，同时删除该联系人的聊天记录";
        deleteName.setText(str);
        if (str.length() >= 25) {
            rlayoutName.getLayoutParams().height = 107;
        }
        //删除好友的监听事件
        rlayoutDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteFriendParam deleteFriendParam = new DeleteFriendParam();
                deleteFriendParam.setToken(token);
                deleteFriendParam.setFriendId(brokerId);
                apiManager.deleteFriend(deleteFriendParam);
                //删除缓存中好友经纪人ID
                List<String> brokerFriends = (List<String>) cacheManager.getBean(Config.getValue("brokerFriends"));
                brokerFriends.remove(brokerId);

                CommonUtils.deleteFriends(brokerId);

            }
        });
        //取消删除好友的监听事件
        rlayoutCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePopWindow.dismiss();
            }
        });
        //设置弹出框的显示位置
        View rootView = LayoutInflater.from(this).inflate(R.layout.activity_broker_personal, null);
        deletePopWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 我的个人中心 or 发消息
     */
    @OnClick(id = R.id.txt_broker_chat, anonymonus = false)
    public void toPersonalCenterOrSendMessage() {
        if (isMine) {//我的个人中心
            startActivity(new Intent(mContext, BrokerDataActivity.class));
        } else if (!ParamUtils.isEmpty(token)) {//发消息
            ChatActivity.launch(mContext, 2, brokerId, txtRemark.getText().toString().trim(), mPhoto);
        }
    }

    /**
     * 我的店铺 or Ta的店铺
     */
    @OnClick(id = R.id.txt_broker_shop)
    public void toMyShopOrTaShop() {//进入我的店铺
        Intent intent = new Intent(mContext, BrokerShopActivity.class);
        if (!isMine) {//进入Ta的店铺
            intent.putExtra("id", brokerId);
            intent.putExtra("name", ParamUtils.isEmpty(remark) ? txtRemark.getText().toString() : remark);
        }
        startActivity(intent);
    }

    /**
     * 我的房友圈 or Ta的房友圈
     */
    @OnClick(id = R.id.txt_broker_moment, anonymonus = false)
    public void toMyMomentsOrTaMoments() {
        Intent intent = new Intent(mContext, MyMomentsActivity.class);//进入我的房友圈
        if (!isMine) {//进入Ta的房友圈
            intent.putExtra("isFriend", true);
            intent.putExtra("friendId", brokerId);
            intent.putExtra("friendName", ParamUtils.isEmpty(remark) ? txtRemark.getText().toString() : remark);
        }
        startActivity(intent);
    }

    /**
     * 获取经纪人个人资料
     *
     * @param result
     */
    public void onEventMainThread(UserPersonalResult result) {
        if (!((UserPersonalParam) result.getParam()).getTag().equals(TAG)) {
            return;
        }
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            item = result.getData();
            setPersonalData(item);
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

    /**
     * 设置个人资料的数据
     *
     * @param item
     */
    private void setPersonalData(UserBean item) {
        //头像
        mPhoto = item.photo;
        GlideUtil.loadCircleImage(mContext, item.photo, imgAvatar);
        //备注，如果是自己个人资料就显示昵称name，如果是别人的个人资料的话，其中好友要有备注显示备注remark没备注显示昵称name ，非好友显示昵称name
        if (!ParamUtils.isEmpty(item.name)) {
            txtRemark.setText(isMine ? item.name : isBrokerFriend ? ParamUtils.isEmpty(remark) ? item.name : remark : item.name);
        }
        //用户类型
        if (!ParamUtils.isEmpty(item.userType)) {
            imgType.setImageResource(item.userType.equals("0") ? R.drawable.user : R.drawable.broker);
        }
        //性别
        if (!ParamUtils.isNull(item.sex)) {
            imgSex.setImageResource(item.sex == 1 ? R.drawable.man : R.drawable.woman);
        }
        //星级
        rbStar.setStar(item.star);

        //设置服务信息、基本信息的数据
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", item);
        serviceFragment = new BrokerServiceFragment();
        serviceFragment.setArguments(bundle);
        bundle.putString("remark", remark);
        bundle.putBoolean("isBrokerFriend", isBrokerFriend);
        basicFragment = new BrokerBasicFragment();
        basicFragment.setArguments(bundle);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(serviceFragment);
        fragments.add(basicFragment);
        vpBroker.setAdapter(new BrokerPersonalAdapter(getSupportFragmentManager(), fragments));
    }

    /**
     * 删除经纪人好友
     *
     * @param result
     */
    public void onEventMainThread(DeleteFriendResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            isDelete = true;
            List<String> brokerFriends = (List<String>) cacheManager.getBean(Config.getValue("brokerFriends"));
            brokerFriends.remove(brokerId);
            lemonMessage.sendMessage("删除成功！");
            deletePopWindow.dismiss();
            toBack();
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

    /**
     * 更新个人信息——用户打开自己的个人资料
     *
     * @param event
     */
    public void onEventMainThread(UpdateUserInfoEvent event) {
        //头像
        if (!ParamUtils.isEmpty(event.getPhoto())) {
            Glide.with(mContext).load(event.getPhoto()).placeholder(R.drawable.default_avatar).into(imgAvatar);
        }
        //备注，如果是自己个人资料就显示昵称name，如果是别人的个人资料的话，其中好友要有备注显示备注remark没备注显示昵称name ，非好友显示昵称name
        if (!ParamUtils.isEmpty(event.getName())) {
            txtRemark.setText(event.getName());
        }
        //性别
        if (!ParamUtils.isNull(event.getSex())) {
            imgSex.setImageResource(event.getSex() == 1 ? R.drawable.man : R.drawable.woman);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_MODIFY_REMARK://修改备注
                    if (!ParamUtils.isEmpty(data.getStringExtra("remark"))) {
                        // TODO: 2016/8/26 还需优化
                        if (data.getStringExtra("remark").equals(item.name)) {//如果是去掉备注
                            remark = item.name;
                        } else {//如果是修改备注或者添加备注
                            remark = data.getStringExtra("remark");
                        }
                    }
                    txtRemark.setText(remark);
                    pinyin = data.getStringExtra("pinyin");
                    break;
            }
        }
    }

}
