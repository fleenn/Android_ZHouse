package com.zfb.house.ui;

import android.content.Intent;
import android.text.InputFilter;
import android.widget.EditText;

import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.model.StatusCode;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.model.param.RemarkParam;
import com.zfb.house.model.result.RemarkResult;
import com.zfb.house.util.TextWatcherUtil;

/**
 * 好友个人资料->编辑备注
 * Created by Administrator on 2016/6/8.
 */
@Layout(id = R.layout.activity_modify_remark)
public class ModifyRemarkActivity extends LemonActivity {

    private static final String TAG = "ModifyRemarkActivity";
    //昵称
    @FieldView(id = R.id.edt_modify_remark)
    private EditText edtRemark;
    private String userType, name, remark, pinyin;

    @Override
    protected void initView() {
        setCenterText(R.string.title_friend_remark);
        setRtText(R.string.complete);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        //初始化输入框的值
        edtRemark.setText(intent.getStringExtra("remark"));
        //获得焦点，让光标停在句末
        edtRemark.requestFocus();
        //经纪人：限制四个中文，用户：限制六个中文
        userType = intent.getStringExtra("userType");
        edtRemark.setHint(userType.equals("1") ? R.string.hint_limit_broker : R.string.hint_limit_user);
        edtRemark.addTextChangedListener(userType.equals("1") ? new TextWatcherUtil(edtRemark, 8).getTextWatcher() : new TextWatcherUtil(edtRemark, 12).getTextWatcher());
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        finish();
    }

    /**
     * 完成
     */
    @OnClick(id = R.id.tv_title_rt)
    public void toComplete() {
        //获取编辑框的值
        remark = edtRemark.getText().toString();
        String token = SettingUtils.get(mContext, "token", null);
        RemarkParam remarkParam = new RemarkParam();
        remarkParam.setToken(token);
        remarkParam.setFriendId(getIntent().getStringExtra("friendId"));
        remark = ParamUtils.isEmpty(remark) ? name : remark;
        remarkParam.setRemark(remark);
        apiManager.modifyRemark(remarkParam);
    }

    /**
     * 修改备注
     *
     * @param result
     */
    public void onEventMainThread(RemarkResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            pinyin = result.getData();
            Intent intent = getIntent();
            intent.putExtra("remark", remark);
            intent.putExtra("pinyin", pinyin);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

}
