package com.zfb.house.ui;

import android.content.Intent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lemon.LemonActivity;
import com.lemon.LemonCacheManager;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.model.StatusCode;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.model.bean.ReleasePull;
import com.zfb.house.model.param.ComplaintParam;
import com.zfb.house.model.result.ComplaintResult;
import com.zfb.house.util.ToolUtil;

/**
 * 好友个人资料 -> 投诉好友
 * Created by Administrator on 2016/6/9.
 */
@Layout(id = R.layout.activity_complaint)
public class ComplaintFriendActivity extends LemonActivity {

    private static final int REQUEST_COMPLAINT_OTHER = 0x1;

    @FieldView(id = R.id.radio_group)
    private RadioGroup radioGroup;
    //色情
    @FieldView(id = R.id.rbtn_one)
    private RadioButton rbOne;
    //欺诈
    @FieldView(id = R.id.rbtn_two)
    private RadioButton rbTwo;
    //侮辱诋毁
    @FieldView(id = R.id.rbtn_three)
    private RadioButton rbThree;
    //广告骚扰
    @FieldView(id = R.id.rbtn_four)
    private RadioButton rbFour;
    //骚扰
    @FieldView(id = R.id.rbtn_five)
    private RadioButton rbFive;
    //其他
    @FieldView(id = R.id.txt_other)
    private TextView txtOther;
    private ReleasePull mReleasePull;
    //用户ID
    private String id;
    //投诉的内容
    private String content;
    //投诉的类型
    private String type;

    @Override
    protected void initView() {
        setCenterText(R.string.complaint);
        setRtText(R.string.btn_submit);

        //获得要投诉好友的ID
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
    }

    @Override
    protected void initData() {
        LemonCacheManager cacheManager = new LemonCacheManager();
        mReleasePull = cacheManager.getBean(ReleasePull.class);
        final String[] values = ToolUtil.getReleasePullData(mReleasePull.getUSER_COMPLAIN());
        if (!ParamUtils.isEmpty(values)) {
            rbOne.setText(values[0]);
            rbTwo.setText(values[1]);
            rbThree.setText(values[2]);
            rbFour.setText(values[3]);
            rbFive.setText(values[4]);
            txtOther.setText(values[5]);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) findViewById(group.getCheckedRadioButtonId());
                content = radioButton.getText().toString();
                for (int i = 0; i < values.length; i++) {
                    if (content.equals(values[i])) {
                        type = i + 1 + "";
                    }
                }
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
     * 提交
     */
    @OnClick(id = R.id.tv_title_rt)
    public void toSubmit() {
        if (!ParamUtils.isEmpty(id) && !ParamUtils.isEmpty(content)) {
            String token = SettingUtils.get(mContext, "token", null);
            ComplaintParam complaintParam = new ComplaintParam();
            complaintParam.setToken(token);
            complaintParam.setUserId(id);
            complaintParam.setContent(content);
            complaintParam.setType(type);
            apiManager.saveComplain(complaintParam);
            finish();
        }
    }

    /**
     * 其他原因
     */
    @OnClick(id = R.id.rlayout_complaint_other)
    public void toOtherReason() {
        Intent intent = new Intent(mContext, ComplaintOtherActivity.class);
        intent.putExtra("id", id);
        startActivityForResult(intent, REQUEST_COMPLAINT_OTHER);
    }

    /**
     * 投诉
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_COMPLAINT_OTHER:
                    finish();
                    break;
            }
        }
    }

}
