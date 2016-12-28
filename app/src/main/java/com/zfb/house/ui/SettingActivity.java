package com.zfb.house.ui;

import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.event.NotifyEvent;
import com.lemon.event.RefreshEvent;
import com.lemon.event.StartJpushEvent;
import com.lemon.event.StopJpushEvent;
import com.lemon.util.AppUtils;
import com.lemon.util.EventUtil;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.emchat.ChatLoginUtil;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.util.DataCleanUtil;

import de.greenrobot.event.EventBus;

/**
 * 我的->设置
 * Created by Administrator on 2016/5/23.
 */
@Layout(id = R.layout.activity_setting)
public class SettingActivity extends LemonActivity {

    public static final String IS_PUSH = "ispush";//消息推送
    public static final String IS_DISTURB = "isdisturb";//消息免打扰

    //缓存大小
    @FieldView(id = R.id.txt_cache)
    private TextView txtCache;
    //消息免打扰
    @FieldView(id = R.id.btn_not_disturb)
    private Button btnOne;
    //2G/3G/4G下自动接收图片
    @FieldView(id = R.id.btn_accept_picture)
    private Button btnTwo;
    //推送通知
    @FieldView(id = R.id.btn_push_notification)
    private Button btnThree;
    //版本号
    @FieldView(id = R.id.tv_version)
    private TextView tvVersion;

    @Override
    protected void init() {
        setCenterText(R.string.title_to_setting);
        Log.i("linwb", "sss111 = " + SettingUtils.get(this, IS_PUSH, true));
        btnThree.setSelected(SettingUtils.get(this, IS_PUSH, true));
        btnOne.setSelected(SettingUtils.get(this, IS_DISTURB, true));
        tvVersion.setText(AppUtils.getVerName(mContext));
    }

    @Override
    protected void initData() {
        try {
            txtCache.setText(DataCleanUtil.getTotalCacheSize(mContext));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBask() {
        finish();
    }

    /**
     * 忘记密码
     */
    @OnClick(id = R.id.rlayout_find_pwd)
    public void toFindPwd() {
        startActivity(new Intent(mContext, FindPwdActivity.class));
    }

    /**
     * 清除缓存
     */
    @OnClick(id = R.id.rlayout_clear_cache)
    public void toClearCache() {
        lemonMessage.sendMessage("清除缓存成功","0");
        DataCleanUtil.clearAllCache(mContext);
        txtCache.setText("0.0M");
    }

    /**
     * 消息免打扰
     */
    @OnClick(id = R.id.btn_not_disturb)
    public void toNotDisturb() {
        changeButtonState(btnOne);
        ChatLoginUtil.setNoInterruptS(btnOne.isSelected());
        SettingUtils.set(this, IS_DISTURB, btnOne.isSelected());
    }

    /**
     * 2G/3G/4G下自动接收图片
     */
    @OnClick(id = R.id.btn_accept_picture)
    public void toAcceptPicture() {
        changeButtonState(btnTwo);
    }

    /**
     * 推送通知
     */
    @OnClick(id = R.id.btn_push_notification)
    public void toPushNotification() {
        changeButtonState(btnThree);
        ChatLoginUtil.setNoInterrupt(btnThree.isSelected());
        SettingUtils.set(this, IS_PUSH, btnThree.isSelected());
        if (btnThree.isSelected()) {
            EventBus.getDefault().post(new StartJpushEvent());
        } else {
            EventBus.getDefault().post(new StopJpushEvent());
        }
    }

    /**
     * 意见反馈
     */
    @OnClick(id = R.id.rlayout_feedback)
    public void toFeedback() {
        startActivity(new Intent(mContext, FeedBackActivity.class));
    }

    /**
     * 关于我们
     */
    @OnClick(id = R.id.rlayout_about_us)
    public void toAboutUs() {
        startActivity(new Intent(mContext, AboutUsActivity.class));
    }

    /**
     * 退出账号
     */
    @OnClick(id = R.id.llayout_exit)
    public void toExit() {
        EMChatManager.getInstance().logout();//此方法为同步推出
        SettingUtils.set(mContext, "token", "");
        UserBean.clearUserBean(mContext);
        Intent intent = new Intent(mContext, LoginActivity.class);
        EventBus.getDefault().post(new StopJpushEvent());
        setResult(RESULT_OK);
        EventUtil.sendEvent(new NotifyEvent(0));
        EventUtil.sendEvent(new RefreshEvent());
        startActivity(intent);
        finish();
    }

    /**
     * 改变按钮的状态
     *
     * @param button 要改变的ImageView
     */
    private void changeButtonState(Button button) {
        button.setSelected(button.isSelected() ? false : true);
    }

}
