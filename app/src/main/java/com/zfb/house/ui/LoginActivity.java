package com.zfb.house.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.emchat.Constant;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.lemon.LemonActivity;
import com.lemon.LemonContext;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.event.LogOutEvent;
import com.lemon.event.StartJpushEvent;
import com.lemon.model.StatusCode;
import com.lemon.util.EventUtil;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.lemon.util.SmsUtil;
import com.zfb.house.R;
import com.zfb.house.emchat.DemoHXSDKHelper;
import com.zfb.house.emchat.HXSDKHelper;
import com.zfb.house.emchat.User;
import com.zfb.house.emchat.UserDao;
import com.zfb.house.init.FriendsListInitializer;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.model.param.CheckPhoneParam;
import com.zfb.house.model.param.LoginByPwdParam;
import com.zfb.house.model.param.LoginBySmsParam;
import com.zfb.house.model.result.CheckPhoneResult;
import com.zfb.house.model.result.LoginResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登陆
 * Created by Snekey on 2016/4/14.
 */
@Layout(id = R.layout.activity_login)
public class LoginActivity extends LemonActivity {

    private static final String TAG = "LoginActivity";

    //    普通登录tab页面
    @FieldView(id = R.id.llayout_login_left)
    private LinearLayout llayoutLeftLogin;
    //    sms登录tab页面
    @FieldView(id = R.id.llayout_login_right)
    private LinearLayout llayoutRightLogin;
    //    普通登录tab文字
    @FieldView(id = R.id.tv_normal)
    private TextView tvNormal;
    //    sms登录tab文字
    @FieldView(id = R.id.tv_sms)
    private TextView tvSms;
    //    普通登录tab按钮下划线
    @FieldView(id = R.id.img_underline_normal)
    private View imgUnderlineNormal;
    //    sms登录tab按钮下划线
    @FieldView(id = R.id.img_underline_sms)
    private View imgUnderlineSms;
    //    普通登录tab按钮
    @FieldView(id = R.id.llayout_tab_left)
    private LinearLayout llayoutTabLeft;
    //    普通登录手机号输入框
    @FieldView(id = R.id.edt_login_phone)
    private EditText edtLoginPhone;
    //    密码输入框
    @FieldView(id = R.id.edt_login_password)
    private EditText edtLoginPassword;
    //    sms登录手机号输入框
    @FieldView(id = R.id.edt_sms_phone)
    private EditText edtSmsPhone;
    //    获取验证码按钮
    @FieldView(id = R.id.btn_login_verification)
    private Button btnLoginVerification;
    //    验证码输入框
    @FieldView(id = R.id.edt_login_verification)
    private EditText edtLoginVerification;
    //    登录错误信息提示
    @FieldView(id = R.id.llayout_error_login)
    private LinearLayout llayoutErrorLogin;
    //    错误内容
    @FieldView(id = R.id.tv_tips)
    private TextView tvTips;

    //    短信倒计时
    public int i = 60;
    //    是否短信登录
    private boolean mIsSms = false;

    public boolean ismIsSms() {
        return mIsSms;
    }

    public void setmIsSms(boolean mIsSms) {
        this.mIsSms = mIsSms;
    }

    /**
     * 初始化页面，模拟点击普通登录tab按钮
     */
    @Override
    protected void initView() {
        llayoutTabLeft.performClick();
        llayoutErrorLogin.setVisibility(View.GONE);
        String userName = SettingUtils.get(mContext, Constant.LAST_USERNAME, "");
        edtLoginPhone.setText(userName);
        edtSmsPhone.setText(userName);
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.btn_back)
    public void toBack() {
        finish();
    }

    /**
     * 跳转到选择注册类型页面
     */
    @OnClick(id = R.id.btn_register)
    public void toRegister() {
        startActivity(new Intent(mContext, SelectUserTypeActivity.class));
    }

    /**
     * 跳转到找回密码页面
     */
    @OnClick(id = R.id.tv_find_pwd)
    public void toFindPwd() {
        startActivity(new Intent(mContext, FindPwdActivity.class));
    }

    /**
     * 切换至普通登录tab页
     */
    @OnClick(id = R.id.llayout_tab_left)
    public void toChangeNormal() {
        this.setmIsSms(false);
        llayoutLeftLogin.setVisibility(View.VISIBLE);
        llayoutRightLogin.setVisibility(View.GONE);
        llayoutErrorLogin.setVisibility(View.GONE);
        tvSms.setSelected(false);
        tvNormal.setSelected(true);
        imgUnderlineNormal.setSelected(true);
        imgUnderlineSms.setSelected(false);
    }

    /**
     * 切换至短信登录tab页
     */
    @OnClick(id = R.id.llayout_tab_right)
    public void toChangeSms() {
        this.setmIsSms(true);
        llayoutLeftLogin.setVisibility(View.GONE);
        llayoutRightLogin.setVisibility(View.VISIBLE);
        llayoutErrorLogin.setVisibility(View.GONE);
        tvSms.setSelected(true);
        tvNormal.setSelected(false);
        imgUnderlineNormal.setSelected(false);
        imgUnderlineSms.setSelected(true);
    }

    /**
     * 发送短信验证码
     */
    @OnClick(id = R.id.btn_login_verification)
    public void toSendSms() {
        llayoutErrorLogin.setVisibility(View.GONE);
        hideKeyboard();
        String phone = edtSmsPhone.getText().toString();
        if (!ParamUtils.isPhoneNumberValid(phone)) {
            llayoutErrorLogin.setVisibility(View.VISIBLE);
            tvTips.setText(R.string.hint_input_phone_error);
            return;
        }
        CheckPhoneParam checkPhoneParam = new CheckPhoneParam();
        checkPhoneParam.setPhone(phone);
        checkPhoneParam.setTag(TAG);
        apiManager.registedCheck(checkPhoneParam);
    }

    @Override
    public void notificationMessage(Message msg) {
        if (msg.what == -9) {
            btnLoginVerification.setText(i + "s后 (重发)");
        } else if (msg.what == -8) {
            Resources resources = getResources();
            btnLoginVerification.setText("重发");
            btnLoginVerification.setClickable(true);
            int colorUnlock = resources.getColor(R.color.show_color);
            btnLoginVerification.setTextColor(colorUnlock);
            btnLoginVerification.setBackgroundResource(R.drawable.bg_btn_sms);
            i = 60;
        }
    }

    /**
     * 计时器线程
     */
    public class SmsCountThread extends Thread {
        @Override
        public void run() {
            for (; i > 0; i--) {
                handler.sendEmptyMessage(-9);
                if (i <= 0) {
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            handler.sendEmptyMessage(-8);
        }
    }

    /**
     * 登录
     */
    @OnClick(id = R.id.btn_login)
    public void toLogin() {
        llayoutErrorLogin.setVisibility(View.GONE);

        if (this.ismIsSms()) {
//          短信登录
            String phone = edtSmsPhone.getText().toString();
            String smsCode = edtLoginVerification.getText().toString();
            if (!ParamUtils.isPhoneNumberValid(phone) || ParamUtils.isEmpty(smsCode)) {
                llayoutErrorLogin.setVisibility(View.VISIBLE);
                tvTips.setText(R.string.hint_input_verify_error);
                return;
            }
            LoginBySmsParam loginBySmsParam = new LoginBySmsParam();
            loginBySmsParam.setPhone(phone);
            loginBySmsParam.setSmscode(smsCode);
            loginBySmsParam.setShowDialog(true);
            apiManager.loginBySmscode(loginBySmsParam);
        } else {
//            密码登录
            String phone = edtLoginPhone.getText().toString();
            String pwd = edtLoginPassword.getText().toString();
            if (!ParamUtils.isPhoneNumberValid(phone) || ParamUtils.isEmpty(pwd)) {
                llayoutErrorLogin.setVisibility(View.VISIBLE);
                tvTips.setText(R.string.hint_input_verify_error);
                return;
            }
            LoginByPwdParam loginByPwdParam = new LoginByPwdParam();
            loginByPwdParam.setPhone(phone);
            loginByPwdParam.setPassword(pwd);
            loginByPwdParam.setShowDialog(true);
            apiManager.loginByPass(loginByPwdParam);
        }
    }

    //    接口返回值处理
    public void onEventMainThread(LoginResult result) {
        String resultCode = result.getResultCode();
        if (!resultCode.equals(StatusCode.SUCCESS.getCode())) {
            llayoutErrorLogin.setVisibility(View.VISIBLE);
            if (resultCode.equals(StatusCode.NAME_PWD_ERROR.getCode()) || resultCode.equals(StatusCode.NAME_USER_ERROR.getCode())) {
                tvTips.setText(R.string.hint_input_verify_error);
                return;
            } else if (resultCode.equals(StatusCode.PARAM_INVALIDATE.getCode())) {
                tvTips.setText(R.string.hint_input_sms_error);
                return;
            }
        }

        String token = result.getData().getToken();
        UserBean user = result.getData().getUser();
        String id = user.id;
        String loginName = user.loginName;
        chartLogin(id, loginName, user.photo);//登录环信
        SettingUtils.set(mContext, "token", token);
        UserBean.updateUserBean(mContext, user);
        Intent intent = new Intent(this, MainActivity.class);
        FriendsListInitializer bean = LemonContext.getBean(FriendsListInitializer.class);
        bean.getFriendListByLogin(token);
        EventUtil.sendEvent(new StartJpushEvent());
        EventUtil.sendEvent(new LogOutEvent());
        startActivity(intent);
        SettingUtils.set(mContext, Constant.LAST_USERNAME, loginName);
        finish();
    }

    public void onEventMainThread(CheckPhoneResult result) {
        if (!((CheckPhoneParam) result.getParam()).getTag().equals(TAG)) {
            return;
        }
        if (result.getResultCode().equals(StatusCode.DATA_DUPLICATED.getCode())) {
            String phone = edtSmsPhone.getText().toString();
            boolean isSuccess = SmsUtil.sendSms(phone);
            if (!isSuccess) {
                return;
            }
            Resources resources = getResources();
            btnLoginVerification.setClickable(false);
            btnLoginVerification.setText(i + "s后 (重发)");
            int colorLocked = resources.getColor(R.color.btn_sms_lock);
            btnLoginVerification.setTextColor(colorLocked);
            btnLoginVerification.setBackgroundResource(R.drawable.bg_btn_sms_lock);
            SmsCountThread smsCountThread = new SmsCountThread();
            smsCountThread.start();
        } else {
            llayoutErrorLogin.setVisibility(View.VISIBLE);
            tvTips.setText(R.string.hint_input_unregister_error);
        }
    }

    /*----------------环信------------------*/
    private void chartLogin(String id, final String loginName, final String photo) {
        //环信登录回调
        EMChatManager.getInstance().login(id, loginName, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            User user = ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getUserProfileManager().getCurrentUserInfo();
                            if (user != null) {
                                ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getUserProfileManager().getCurrentUserInfo().setAvatar(photo);
                                ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getUserProfileManager().getCurrentUserInfo().setNick(loginName);
                            }
                            Log.i("linwb", "登录聊天服务器成功！");
                        } catch (Exception e) {
                            Log.i("linwb", "异常1！");
                        }

                        EMGroupManager.getInstance().loadAllGroups();
                        EMChatManager.getInstance().loadAllConversations();
                        initializeContacts();

                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {
                Log.i("linwb", "登录聊天111！");
            }

            @Override
            public void onError(int code, String message) {
                Log.i("linwb", "登录聊天服务器失败！");
            }
        });
    }

    private void initializeContacts() {
        Map<String, User> userList = new HashMap<String, User>();

        //添加 User  申请和通知
        User newFriends = new User();
        newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
        String newfriendsNick = getResources().getString(R.string.Application_and_notify);//申请和通知
        newFriends.setNick(newfriendsNick);
        newFriends.setHeader("");
        userList.put(Constant.NEW_FRIENDS_USERNAME, newFriends);

        //添加 群聊
        User groupUser = new User();
        String groupNick = getResources().getString(R.string.group_chat);
        groupUser.setUsername(Constant.GROUP_USERNAME);
        groupUser.setNick(groupNick);
        groupUser.setHeader("");
        userList.put(Constant.GROUP_USERNAME, groupUser);

        //添加聊天室
        User chatRoomItem = new User();
        String chatroom = getResources().getString(R.string.chat_room);
        chatRoomItem.setUsername(Constant.CHAT_ROOM);
        chatRoomItem.setNick(chatroom);
        chatRoomItem.setHeader("");
        userList.put(Constant.CHAT_ROOM, chatRoomItem);

        //添加 "Robot" 环信小助手
        User robotUser = new User();
        String robot = getResources().getString(R.string.robot_chat);
        robotUser.setUsername(Constant.CHAT_ROBOT);
        robotUser.setNick(robot);
        robotUser.setHeader("");
        userList.put(Constant.CHAT_ROBOT, robotUser);

        //存入内存
        ((DemoHXSDKHelper) HXSDKHelper.getInstance()).setContactList(userList);
        UserDao dao = new UserDao(LoginActivity.this);
        List<User> users = new ArrayList<User>(userList.values());
        dao.saveContactList(users);

    }

}
