package com.zfb.house.ui;

import android.widget.EditText;

import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.model.StatusCode;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.model.param.FinanceApplyParam;
import com.zfb.house.model.result.FinanceApplyResult;

/**
 * 售后金融 -> 立即预约 填写资料
 * Created by Administrator on 2016/7/13.
 */
@Layout(id = R.layout.activity_finance_information)
public class FinanceInformationActivity extends LemonActivity {

    //申请人姓名
    @FieldView(id = R.id.edt_application_name)
    private EditText edtName;
    //申请人手机号码
    @FieldView(id = R.id.edt_application_phone)
    private EditText edtPhone;
    //购买小区或者意向小区
    @FieldView(id = R.id.edt_intention_village)
    private EditText edtVillage;

    private String name, phone, village, type;

    @Override
    protected void initView() {
        setCenterText(R.string.title_fill_information);
    }

    @Override
    protected void initData() {
        type = getIntentExtraStr("type");
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        finish();
    }

    /**
     * 申请
     */
    @OnClick(id = R.id.btn_sure)
    public void toApply() {
        name = edtName.getText().toString().trim();
        if (ParamUtils.isEmpty(name)) {
            lemonMessage.sendMessage("请输入您的姓名");
            return;
        }

        phone = edtPhone.getText().toString().trim();
        if (ParamUtils.isEmpty(phone)) {
            lemonMessage.sendMessage("请输入您的手机号码");
            return;
        }
        if (!ParamUtils.isPhoneNumberValid(phone)) {
            lemonMessage.sendMessage(R.string.hint_input_phone_error);
            return;
        }

        village = edtVillage.getText().toString().trim();
        String token = SettingUtils.get(mContext, "token", null);
        FinanceApplyParam financeApplyParam = new FinanceApplyParam();
        if (!ParamUtils.isEmpty(token)) {
            financeApplyParam.setToken(token);
        }
        financeApplyParam.setUserName(name);
        financeApplyParam.setMobile(phone);
        if (!ParamUtils.isEmpty(village)) {
            financeApplyParam.setPurposeVillage(village);
        }
        financeApplyParam.setType(type);
        apiManager.finacialApply(financeApplyParam);
    }

    /**
     * 金融申请
     *
     * @param result
     */
    public void onEventMainThread(FinanceApplyResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            lemonMessage.sendMessage(R.string.toast_apply_success);
            finish();
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

}
