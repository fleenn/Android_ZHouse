package com.zfb.house.ui;

import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.model.bean.UploadTradingBody;
import com.zfb.house.model.param.TradingParam;

/**
 * 经纪人：成交上传
 * Created by Administrator on 2016/6/18.
 */
@Layout(id = R.layout.activity_upload)
public class UploadActivity extends LemonActivity {

    private static final String TAG = "UploadActivity";
    //成交房源地址
    @FieldView(id = R.id.edt_upload_address)
    private EditText edtAddress;
    //购房者或承租者
    @FieldView(id = R.id.group_tenant)
    private RadioGroup groupTenant;
    //购房者或承租者的联系人姓名
    @FieldView(id = R.id.edt_name_one)
    private EditText edtNameOne;
    //购房者或承租者的联系人电话
    @FieldView(id = R.id.edt_phone_one)
    private EditText edtPhoneOne;
    @FieldView(id = R.id.group_proxy)
    private RadioGroup groupProxy;
    //代理人或业主的联系人姓名
    @FieldView(id = R.id.edt_name_two)
    private EditText edtNameTwo;
    //代理人或业主的联系人电话
    @FieldView(id = R.id.edt_phone_two)
    private EditText edtPhoneTwo;

    private String address, tenantName, homeBuyerName, homeBuyerPhone, proxyName, ownerName, ownerPhone;

    @Override
    protected void initView() {
        setCenterText(R.string.title_upload);
    }

    @Override
    protected void initData() {
        groupTenant.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) findViewById(group.getCheckedRadioButtonId());
                tenantName = radioButton.getText().toString();
            }
        });
        groupProxy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) findViewById(checkedId);
                proxyName = radioButton.getText().toString();
            }
        });
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        finish();
    }

    /**
     * 提交审核
     */
    @OnClick(id = R.id.btn_submit)
    public void toSubmit() {
        address = edtAddress.getText().toString().trim();
        if (ParamUtils.isEmpty(address)) {
            lemonMessage.sendMessage("请输入成交房源地址");
            return;
        }

        if (ParamUtils.isEmpty(tenantName)) {
            lemonMessage.sendMessage("请选择购房者或承租者");
            return;
        }

        if (ParamUtils.isEmpty(proxyName)) {
            lemonMessage.sendMessage("请选择代理人或业主");
            return;
        }

        homeBuyerName = edtNameOne.getText().toString().trim();
        ownerName = edtNameTwo.getText().toString().trim();
        if (ParamUtils.isEmpty(homeBuyerName) || ParamUtils.isEmpty(ownerName)) {
            lemonMessage.sendMessage("请填写联系人姓名");
            return;
        }

        homeBuyerPhone = edtPhoneOne.getText().toString().trim();
        ownerPhone = edtPhoneTwo.getText().toString().trim();
        if (ParamUtils.isEmpty(homeBuyerPhone) || ParamUtils.isEmpty(ownerPhone)) {
            lemonMessage.sendMessage("请填写联系人电话");
            return;
        }
        if (!ParamUtils.isPhoneNumberValid(homeBuyerPhone) || !ParamUtils.isPhoneNumberValid(ownerPhone)) {
            lemonMessage.sendMessage(R.string.hint_input_phone_error);
            return;
        }

        String token = SettingUtils.get(mContext, "token", null);
        final UploadTradingBody uploadTradingBody = new UploadTradingBody();
        uploadTradingBody.address = address;
        uploadTradingBody.tenantName = tenantName;
        uploadTradingBody.homeBuyerName = homeBuyerName;
        uploadTradingBody.homeBuyerPhone = homeBuyerPhone;
        uploadTradingBody.proxyName = proxyName;
        uploadTradingBody.ownerName = ownerName;
        uploadTradingBody.ownerPhone = ownerPhone;
        TradingParam tradingParam = new TradingParam();
        tradingParam.setToken(token);
        tradingParam.setBody(new Gson().toJson(uploadTradingBody));
        apiManager.uploadTrading(tradingParam);
        lemonMessage.sendMessage("提交成功");
        finish();
    }

}
