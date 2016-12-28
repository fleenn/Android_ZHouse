package com.zfb.house.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lemon.LemonActivity;
import com.lemon.LemonCacheManager;
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
import com.zfb.house.component.CircleImageView;
import com.zfb.house.emchat.ChatActivity;
import com.zfb.house.emchat.CommonUtils;
import com.zfb.house.model.bean.ReleasePull;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.model.param.DeleteFriendParam;
import com.zfb.house.model.param.UserPersonalParam;
import com.zfb.house.model.result.DeleteFriendResult;
import com.zfb.house.model.result.UserPersonalResult;
import com.zfb.house.util.ToolUtil;

import java.util.List;

/**
 * 用户：个人信息
 * Created by HourGlassRemember on 2016/8/24.
 */
@Layout(id = R.layout.activity_user_personal)
public class UserPersonalActivity extends LemonActivity {

    private static final String TAG = "UserPersonalActivity";
    //编辑好友备注的请求码
    private final int REQUEST_MODIFY_REMARK = 0x1;

    //好友 -> 更多
    @FieldView(id = R.id.img_user_more)
    private ImageView imgMore;
    //头像
    @FieldView(id = R.id.img_user_avatar)
    private CircleImageView imgAvatar;
    //备注
    @FieldView(id = R.id.txt_user_remark)
    private TextView txtRemark;
    //昵称，如果是好友的话并且有备注，这边就要显示昵称，其他情况下就不显示昵称
    @FieldView(id = R.id.rlayout_user_name)
    private RelativeLayout rlayoutName;
    @FieldView(id = R.id.txt_user_name)
    private TextView txtName;
    //用户类型
    @FieldView(id = R.id.img_user_type)
    private ImageView imgType;
    //性别
    @FieldView(id = R.id.img_user_sex)
    private ImageView imgSex;
    //Ta 的房友圈 or 我的房友圈
    @FieldView(id = R.id.txt_user_moment)
    private TextView txtMoments;
    //发消息 or 我的个人中心
    @FieldView(id = R.id.txt_user_chat)
    private TextView txtChat;
    //近期状态
    @FieldView(id = R.id.txt_user_statue)
    private TextView txtUserStatue;
    //联系电话
    @FieldView(id = R.id.txt_user_phonel)
    private TextView txtPhone;
    //关注片区
    @FieldView(id = R.id.txt_user_district_one)
    private TextView txtDistrictOne;
    @FieldView(id = R.id.txt_user_district_two)
    private TextView txtDistrictTwo;
    @FieldView(id = R.id.txt_user_district_three)
    private TextView txtDistrictThree;
    //关注小区
    @FieldView(id = R.id.txt_user_village_one)
    private TextView txtVillageOne;
    @FieldView(id = R.id.txt_user_village_two)
    private TextView txtVillageTwo;
    @FieldView(id = R.id.txt_user_village_three)
    private TextView txtVillageThree;

    //右上角弹出框
    private PopupWindow mPopWindow;
    //底部弹出框
    private PopupWindow deletePopWindow;
    private String token;
    //标识是否为好友用户
    private Boolean isUserFriend;
    //传过来的用户ID
    private String userId;
    //好友备注
    private String remark;
    //remarek pinyin
    private String pinyin;
    //是否删除好友
    private boolean isDelete;
    //当前item在列表中的位置
    private int position;
    //目标用户头像，带往聊天页面
    private String photo;
    private ReleasePull mReleasePull;
    //是否为自己详情页
    private boolean isMine;
    //当前用户
    private UserBean item;

    @Override
    protected void initView() {
        token = SettingUtils.get(mContext, "token", null);
        Intent intent = getIntent();
        //传过来的用户ID
        userId = intent.getStringExtra("userId");
        //判断是否为自己的详情页
        if (!ParamUtils.isNull(UserBean.getInstance(mContext))) {
            if (userId.equals(UserBean.getInstance(mContext).id)) {
                isMine = true;
            }
        }
        //设置按钮上面的文字
        Resources resources = getResources();
        txtMoments.setText(isMine ? R.string.img_mine_moments : R.string.label_mine_friend_moments);
        if (isMine) {//我的个人中心
            txtChat.setText(R.string.label_mine_personal_center);
            txtChat.setCompoundDrawablesWithIntrinsicBounds(null, resources.getDrawable(R.drawable.personal_center), null, null);
            txtChat.setTextColor(resources.getColor(R.color.red_one));
        } else {//发消息
            txtChat.setText(R.string.label_mine_send_message);
            txtChat.setCompoundDrawablesWithIntrinsicBounds(null, resources.getDrawable(R.drawable.personal_chat), null, null);
            txtChat.setTextColor(resources.getColor(R.color.green_one));
        }
        //传过来的标识（是否为好友），如果没传的话默认不是好友
        isUserFriend = intent.getBooleanExtra("isUserFriend", false);
        //如果是好友用户的话，就添加备注和右上角图标
        if (isUserFriend) {
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
    }

    @Override
    protected void initData() {
        mReleasePull = new LemonCacheManager().getBean(ReleasePull.class);
        if (isMine) {//查看自己的个人资料
            setPersonalData(UserBean.getInstance(mContext));
        } else {//查看其他人的个人资料
            //调用“获取用户详细信息”接口
            UserPersonalParam userPersonalParam = new UserPersonalParam();
            userPersonalParam.setId(userId);
            userPersonalParam.setTag(TAG);
            apiManager.getUserDetail(userPersonalParam);
        }
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.img_user_back)
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
     * 返回键按钮事件处理
     */
    @Override
    public void onBackPressed() {
        toBack();
        super.onBackPressed();
    }

    /**
     * 点击头像放大
     */
    @OnClick(id = R.id.img_user_avatar)
    public void enlargeAvatar() {
        Intent intent = new Intent(mContext, BrokerShopPhotoActivity.class);
        if (isMine) {
            photo = ParamUtils.isEmpty(UserBean.getInstance(mContext).photo) ? "isDefault" : "isMine";
        } else {
            photo = ParamUtils.isEmpty(item.photo) ? "isDefault" : item.photo;
        }
        intent.putExtra("photo", photo);
        startActivity(intent);
    }

    /**
     * 我的房友圈 or Ta的房友圈
     */
    @OnClick(id = R.id.txt_user_moment, anonymonus = false)
    public void toMyMomentsOrTaMoments() {
        Intent intent = new Intent(mContext, MyMomentsActivity.class);//进入我的房友圈
        if (!isMine) {//进入Ta的房友圈
            intent.putExtra("isFriend", true);
            intent.putExtra("friendId", userId);
            intent.putExtra("friendName", txtRemark.getText().toString().trim());
        }
        startActivity(intent);
    }

    /**
     * 我的个人中心 or 发消息
     */
    @OnClick(id = R.id.txt_user_chat, anonymonus = false)
    public void toPersonalCenterOrSendMessage() {
        if (isMine) {//我的个人中心
            startActivity(new Intent(mContext, UserDataActivity.class));
        } else if (!ParamUtils.isEmpty(token)) {//发消息
            ChatActivity.launch(mContext, 1, userId, txtRemark.getText().toString().trim(), photo);
        }
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
                intent.putExtra("name", txtName.getText().toString());
                intent.putExtra("remark", remark);
                intent.putExtra("friendId", userId);
                intent.putExtra("userType", "0");
                startActivityForResult(intent, REQUEST_MODIFY_REMARK);
                mPopWindow.dismiss();
            }
        });
        //“投诉”的监听事件
        llayoutComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ComplaintFriendActivity.class);
                intent.putExtra("id", userId);
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
        //一个自定义的布局，作为弹出框显示的内0容
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
                deleteFriendParam.setFriendId(userId);
                apiManager.deleteFriend(deleteFriendParam);
                //删除缓存中好友用户ID
                List<String> userFriends = (List<String>) cacheManager.getBean(Config.getValue("userFriends"));
                userFriends.remove(userId);
                CommonUtils.deleteFriends(userId);
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
        View rootView = LayoutInflater.from(this).inflate(R.layout.activity_user_personal, null);
        deletePopWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 获取用户个人资料
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
        photo = item.photo;
        GlideUtil.loadCircleImage(mContext, item.photo, imgAvatar);
        //备注，如果是自己个人资料就显示昵称name，如果是别人的个人资料的话，其中好友要有备注显示备注remark没备注显示昵称name ，非好友显示昵称name
        if (!ParamUtils.isEmpty(item.name)) {
            txtRemark.setText(isMine ? item.name : isUserFriend ? ParamUtils.isEmpty(remark) ? item.name : remark : item.name);
        }
        //昵称，如果是好友的话并且有备注，这边就要显示昵称，其他情况下就不显示昵称
        if (isUserFriend && !ParamUtils.isEmpty(remark) && !remark.equals(item.name)) {
            rlayoutName.setVisibility(View.VISIBLE);
            if (!ParamUtils.isEmpty(item.name)) {
                txtName.setText(item.name);
            }
        } else {
            rlayoutName.setVisibility(View.GONE);
        }
        //用户类型
        if (!ParamUtils.isEmpty(item.userType)) {
            imgType.setImageResource(item.userType.equals("0") ? R.drawable.user : R.drawable.broker);
        }
        //性别
        if (!ParamUtils.isNull(item.sex)) {
            imgSex.setImageResource(item.sex == 1 ? R.drawable.man : R.drawable.woman);
        }
        //近期状态
        if (isMine) {//用户查看自己的近期状态
            String recentStatus = UserBean.getInstance(mContext).recentStatus;
            txtUserStatue.setText(Character.isDigit(recentStatus.charAt(0)) ? ToolUtil.convertValueToLabel(recentStatus, mReleasePull.getRECENT_STATUS()) : recentStatus);
        } else if (!ParamUtils.isEmpty(item.recentStatus) && !ParamUtils.isNull(mReleasePull)) {//用户查看非自己的近期状态
            txtUserStatue.setText(ToolUtil.convertValueToLabel(item.recentStatus, mReleasePull.getRECENT_STATUS()));
        }
        //联系电话，注：经纪人的号码对所有人都公开，用户的号码只有是好友的才能看到，包括好友用户和好友经纪人
        if (!ParamUtils.isNull(UserBean.getInstance(mContext))) {
            txtPhone.setText(isUserFriend || userId.equals(UserBean.getInstance(mContext).id) ? item.phone : "加为好友后可见");
        } else {
            txtPhone.setText("登录后可见");
        }
        //关注片区
        setDistrictData(ParamUtils.isEmpty(item.serviceDistrictName) ? "" : item.serviceDistrictName);
        //关注小区
        setVillageData(ParamUtils.isEmpty(item.serviceVillageName) ? "" : item.serviceVillageName);
    }

    /**
     * 显示用户关注的片区
     *
     * @param serviceDistrictIds
     */
    private void setDistrictData(String serviceDistrictIds) {
        if (!serviceDistrictIds.isEmpty()) {
            String[] district = serviceDistrictIds.split(",");
            switch (district.length) {
                case 3:
                    if (!"null".equals(district[2])) {
                        txtDistrictThree.setText(district[2]);
                        txtDistrictThree.setVisibility(View.VISIBLE);
                    }
                case 2:
                    if (!"null".equals(district[1])) {
                        txtDistrictTwo.setText(district[1]);
                        txtDistrictTwo.setVisibility(View.VISIBLE);
                    }
                case 1:
                    if (!"null".equals(district[0])) {
                        txtDistrictOne.setText(district[0]);
                        txtDistrictOne.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    }

    /**
     * 显示用户关注的小区
     *
     * @param serviceVillageIds
     */
    private void setVillageData(String serviceVillageIds) {
        if (!serviceVillageIds.isEmpty()) {
            String[] village = serviceVillageIds.split(",");
            switch (village.length) {
                case 3:
                    if (!"null".equals(village[2])) {
                        txtVillageThree.setText(village[2]);
                        txtVillageThree.setVisibility(View.VISIBLE);
                    }
                case 2:
                    if (!"null".equals(village[1])) {
                        txtVillageTwo.setText(village[1]);
                        txtVillageTwo.setVisibility(View.VISIBLE);
                    }
                case 1:
                    if (!"null".equals(village[0])) {
                        txtVillageOne.setText(village[0]);
                        txtVillageOne.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    }

    /**
     * 删除用户好友
     *
     * @param result
     */
    public void onEventMainThread(DeleteFriendResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            isDelete = true;
            lemonMessage.sendMessage("删除成功！");
            List<String> userFriends = (List<String>) cacheManager.getBean(Config.getValue("userFriends"));
            userFriends.remove(userId);
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
                case REQUEST_MODIFY_REMARK://修改备注——用户打开好友的个人资料
                    if (!ParamUtils.isEmpty(data.getStringExtra("remark"))) {
                        if (data.getStringExtra("remark").equals(txtName.getText().toString())) {//如果是去掉备注
                            remark = txtName.getText().toString();
                            rlayoutName.setVisibility(View.GONE);
                        } else {//如果是修改备注或者添加备注
                            remark = data.getStringExtra("remark");
                            txtName.setText(item.name);
                            rlayoutName.setVisibility(View.VISIBLE);
                        }
                    }
                    txtRemark.setText(remark);
                    pinyin = data.getStringExtra("pinyin");
                    break;
            }
        }
    }

}
