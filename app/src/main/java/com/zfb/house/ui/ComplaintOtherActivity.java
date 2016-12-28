package com.zfb.house.ui;

import android.content.Intent;
import android.widget.EditText;

import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.model.StatusCode;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.model.param.ComplaintParam;
import com.zfb.house.model.result.ComplaintResult;

/**
 * 投诉 -> 其他
 * Created by Administrator on 2016/5/23.
 */
@Layout(id = R.layout.activity_complaint_other)
public class ComplaintOtherActivity extends LemonActivity {

    //用户输入其他投诉原因的内容
    @FieldView(id = R.id.edt_complaint_other)
    private EditText edtContent;
    //用户ID
    private String id;
    //投诉的内容
    private String content;

    @Override
    protected void initView() {
        setCenterText(R.string.complaint);
        setRtText(R.string.btn_submit);

        //获得要投诉好友的ID
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        finish();
    }

    /**
     * 提交
     */
    @OnClick(id = R.id.tv_title_rt)
    public void toSubmit() {
        content = edtContent.getText().toString();
        if (ParamUtils.isEmpty(content)) {
            lemonMessage.sendMessage("请填写投诉原因");
            return;
        }
        if (!ParamUtils.isEmpty(id)) {
            String token = SettingUtils.get(mContext, "token", null);
            ComplaintParam complaintParam = new ComplaintParam();
            complaintParam.setToken(token);
            complaintParam.setUserId(id);
            complaintParam.setContent(content);
            complaintParam.setType("6");
            apiManager.saveComplain(complaintParam);
            setResult(RESULT_OK);
            finish();
        }
    }

    /**
     * 投诉->其他
     *
     * @param result
     */
    public void onEventMainThread(ComplaintResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            lemonMessage.sendMessage(R.string.toast_complaint_success);
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

}
