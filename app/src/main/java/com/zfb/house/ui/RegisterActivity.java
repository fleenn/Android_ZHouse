package com.zfb.house.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.model.StatusCode;
import com.lemon.util.ParamUtils;
import com.lemon.util.SmsUtil;
import com.zfb.house.R;
import com.zfb.house.model.param.CheckPhoneParam;
import com.zfb.house.model.param.RegisterParam;
import com.zfb.house.model.result.CheckPhoneResult;
import com.zfb.house.model.result.RegisterResult;

/**
 * 注册页面
 * Created by Administrator on 2016/4/15.
 */
@Layout(id = R.layout.activity_register)
public class RegisterActivity extends LemonActivity {
    private static final String TAG = "RegisterActivity";

    //    发送验证码按钮
    @FieldView(id = R.id.btn_register_verification)
    private Button btnRegisterVerification;
    //    注册按钮
    @FieldView(id = R.id.btn_register)
    private Button btnRegister;
    //    手机号码输入框
    @FieldView(id = R.id.edt_phone_number)
    private EditText edtPhoneNumber;
    //    验证码输入框
    @FieldView(id = R.id.edt_register_verification)
    private EditText edtRegisterVerification;
    //    密码输入框
    @FieldView(id = R.id.edt_register_password)
    private EditText edtRegisterPassword;
    //    邀请码
    @FieldView(id = R.id.edt_register_invite)
    private EditText edtRegisterInvite;
    //    错误信息提示
    @FieldView(id = R.id.llayout_error_register)
    private LinearLayout llayoutErrorRegister;
    //    错误内容
    @FieldView(id = R.id.tv_tips)
    private TextView tvTips;
    //    短信倒计时
    public int i = 60;
    //    角色
    private String type;

    @Override
    protected void initView() {
        llayoutErrorRegister.setVisibility(View.GONE);
        type = getIntentExtraStr("userType");
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.btn_back)
    public void toBack() {
        finish();
    }

    /**
     * 服务协议及隐私政策
     */
    @OnClick(id = R.id.txt_register_agreement)
    public void toAgreement() {
        startActivity(new Intent(mContext, AgreementActivity.class));
    }

    /**
     * 发送短信验证码
     */
    @OnClick(id = R.id.btn_register_verification)
    public void toSendSms() {
        llayoutErrorRegister.setVisibility(View.GONE);
        hideKeyboard();
        String phone = edtPhoneNumber.getText().toString();
        if (!ParamUtils.isPhoneNumberValid(phone)) {
            llayoutErrorRegister.setVisibility(View.VISIBLE);
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
            btnRegisterVerification.setText(i + "s后 (重发)");
        } else if (msg.what == -8) {
            Resources resources = getResources();
            int colorUnlock = resources.getColor(R.color.show_color);
            btnRegisterVerification.setText("重发");
            btnRegisterVerification.setClickable(true);
            btnRegisterVerification.setBackgroundResource(R.drawable.bg_btn_sms);
            btnRegisterVerification.setTextColor(colorUnlock);
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
     * 注册
     */
    @OnClick(id = R.id.btn_register)
    public void toRegister() {
        llayoutErrorRegister.setVisibility(View.GONE);

        String phone = edtPhoneNumber.getText().toString();
        String smsCode = edtRegisterVerification.getText().toString();
        String password = edtRegisterPassword.getText().toString();
        String inviteCode = edtRegisterInvite.getText().toString();

        if (!ParamUtils.isPwdValid(password)) {
            llayoutErrorRegister.setVisibility(View.VISIBLE);
            tvTips.setText(R.string.hint_new_password_format);
            return;
        }
        RegisterParam registerParam = new RegisterParam();
        registerParam.setPhone(phone);
        registerParam.setSmscode(smsCode);
        registerParam.setPassword(password);
        if (!ParamUtils.isEmpty(inviteCode)) {
            registerParam.setInviteCode(inviteCode);
        }
        registerParam.setType(type);
        registerParam.setShowDialog(true);
        apiManager.regist(registerParam);
    }

    /**
     * 判断手机号是否注册
     *
     * @param result
     */
    public void onEventMainThread(CheckPhoneResult result) {
        if (!((CheckPhoneParam) result.getParam()).getTag().equals(TAG)) {
            return;
        }
        if (result.getResultCode().equals(StatusCode.DATA_DUPLICATED.getCode())) {
            llayoutErrorRegister.setVisibility(View.VISIBLE);
            tvTips.setText(R.string.hint_input_register_error);
        } else {
            String phone = edtPhoneNumber.getText().toString();
            boolean isSuccess = SmsUtil.sendSms(phone);
            if (!isSuccess) {
                return;
            }
            Resources resources = getResources();
            btnRegisterVerification.setBackground(resources.getDrawable(R.drawable.bg_btn_sms_lock));
            btnRegisterVerification.setTextColor(resources.getColor(R.color.btn_sms_lock));
            btnRegisterVerification.setClickable(false);
            btnRegisterVerification.setText(i + "s后 (重发)");
            SmsCountThread smsCountThread = new SmsCountThread();
            smsCountThread.start();
        }
    }

    /**
     * 注册
     *
     * @param result
     */
    public void onEventMainThread(RegisterResult result) {
        if (!result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            llayoutErrorRegister.setVisibility(View.VISIBLE);
            tvTips.setText(R.string.hint_input_sms_error);
            return;
        }
        //弹出自定义toast——注册成功
        lemonMessage.sendMessage("注册成功","0");
        finish();
    }

}