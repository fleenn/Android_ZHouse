package com.zfb.house.ui;

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
import com.zfb.house.model.param.FindPwdParam;
import com.zfb.house.model.result.CheckPhoneResult;
import com.zfb.house.model.result.FindPwdResult;
import com.zfb.house.util.ToolUtil;

/**
 * 忘记密码
 * Created by Snekey on 2016/4/18.
 */
@Layout(id = R.layout.activity_find_pwd)
public class FindPwdActivity extends LemonActivity {
    private static final String TAG = "FindPwdActivity";

    //    电话号码输入框
    @FieldView(id = R.id.edt_forget_phone)
    private EditText edtForgetPhone;
    //    发送短信按钮
    @FieldView(id = R.id.btn_forget_verification)
    private Button btnForgetVerification;
    //    短信验证码输入框
    @FieldView(id = R.id.edt_forget_verification)
    private EditText edtForgetVerification;
    //    新密码输入框
    @FieldView(id = R.id.edt_forget_password)
    private EditText edtForgetPassword;
    //    错误提示
    @FieldView(id = R.id.llayout_error_find)
    private LinearLayout llayoutErrorFind;
    //    错误内容
    @FieldView(id = R.id.tv_tips)
    private TextView tvTips;
    //    倒计时
    public int i = 60;

    /**
     * 返回
     */
    @OnClick(id = R.id.btn_back)
    public void toBack() {
        finish();
    }

    //    发送验证码
    @OnClick(id = R.id.btn_forget_verification)
    public void toSendSms() {
        llayoutErrorFind.setVisibility(View.GONE);

        String phone = edtForgetPhone.getText().toString();
        if (!ParamUtils.isPhoneNumberValid(phone)) {
            llayoutErrorFind.setVisibility(View.VISIBLE);
            tvTips.setText(R.string.hint_input_phone_error);
            return;
        }
        CheckPhoneParam checkPhoneParam = new CheckPhoneParam();
        checkPhoneParam.setPhone(phone);
        checkPhoneParam.setTag(TAG);
        apiManager.registedCheck(checkPhoneParam);
    }

    //    更新UI计时器
    @Override
    public void notificationMessage(Message msg) {
        if (msg.what == -9) {
            btnForgetVerification.setText(i + "s后 (重发)");
        } else if (msg.what == -8) {
            Resources resources = getResources();
            int colorUnlock = resources.getColor(R.color.show_color);
            btnForgetVerification.setText("重发");
            btnForgetVerification.setClickable(true);
            btnForgetVerification.setBackgroundResource(R.drawable.bg_btn_sms);
            btnForgetVerification.setTextColor(colorUnlock);
            i = 60;
        }
    }

    //    计时器线程
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
     * 提交
     */
    @OnClick(id = R.id.btn_submit)
    public void toSubmit() {
        llayoutErrorFind.setVisibility(View.GONE);

        String phone = edtForgetPhone.getText().toString();
        String smsCode = edtForgetVerification.getText().toString();
        String newPwd = edtForgetPassword.getText().toString();

        if (!ParamUtils.isPwdValid(newPwd)) {
            llayoutErrorFind.setVisibility(View.VISIBLE);
            tvTips.setText(R.string.hint_new_password_format);
            return;
        }
        FindPwdParam findPwdParam = new FindPwdParam();
        findPwdParam.setPhone(phone);
        findPwdParam.setSmscode(smsCode);
        findPwdParam.setNewPass(newPwd);
        apiManager.forgetPass(findPwdParam);
    }

    public void onEventMainThread(CheckPhoneResult result) {
        if (!((CheckPhoneParam) result.getParam()).getTag().equals(TAG)) {
            return;
        }
        if (!result.getResultCode().equals(StatusCode.DATA_DUPLICATED.getCode())) {
            llayoutErrorFind.setVisibility(View.VISIBLE);
            tvTips.setText(R.string.hint_input_unregister_error);
            return;
        }
        String phone = edtForgetPhone.getText().toString();
        boolean isSuccess = SmsUtil.sendSms(phone);
        if (!isSuccess) {
            return;
        }
        Resources resources = getResources();
        int colorLocked = resources.getColor(R.color.btn_sms_lock);
        btnForgetVerification.setBackgroundResource(R.drawable.bg_btn_sms_lock);
        btnForgetVerification.setTextColor(colorLocked);
        btnForgetVerification.setClickable(false);
        btnForgetVerification.setText(i + "s后 (重发)");
        SmsCountThread smsCountThread = new SmsCountThread();
        smsCountThread.start();
    }

    /**
     * 注册结果
     *
     * @param result
     */
    public void onEventMainThread(FindPwdResult result) {
        //弹出自定义toast——修改成功
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())){
            lemonMessage.sendMessage("修改成功","0");
            finish();
        }
    }
}
