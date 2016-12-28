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
import com.zfb.house.model.param.FeedBackParam;
import com.zfb.house.model.result.FeedBackResult;

/**
 * 我的 -> 设置 -> 意见反馈
 * Created by Administrator on 2016/5/23.
 */
@Layout(id = R.layout.activity_feedback)
public class FeedBackActivity extends LemonActivity {

    //用户反馈的内容
    @FieldView(id = R.id.edt_feedback_content)
    private EditText edtContent;

    @Override
    protected void initView() {
        setCenterText(R.string.title_to_feedback);
        setRtText(R.string.btn_submit);
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
        String content = edtContent.getText().toString();
        if (ParamUtils.isEmpty(content)) {
            lemonMessage.sendMessage("请输入您的宝贵意见");
            return;
        }
        String token = SettingUtils.get(mContext, "token", null);
        FeedBackParam feedBackParam = new FeedBackParam();
        feedBackParam.setToken(token);
        feedBackParam.setContent(content);
        apiManager.saveFeedBack(feedBackParam);
        finish();
    }

    /**
     * 反馈
     *
     * @param result
     */
    public void onEventMainThread(FeedBackResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            lemonMessage.sendMessage(R.string.toast_feedback_success);
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

}
