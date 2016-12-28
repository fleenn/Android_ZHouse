package com.zfb.house.ui;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.model.StatusCode;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.component.JudgeSelectView;
import com.zfb.house.component.LoadDialog;
import com.zfb.house.component.ScoreLayout_all;
import com.zfb.house.model.bean.JudgeBrokerInfo;
import com.zfb.house.model.param.JudgeBrokerParam;
import com.zfb.house.model.result.JudgeBrokerResult;

/**
 * 评价经纪人
 * Created by linwenbing on 16/6/21.
 */
@Layout(id = R.layout.activity_judge_broker)
public class JudgeBrokerActivity extends LemonActivity {

    //房屋信息真实度
    @FieldView(id = R.id.score_house_info)
    private ScoreLayout_all sl_house;
    //态度服务满意度
    @FieldView(id = R.id.score_serve)
    private ScoreLayout_all sl_serve;
    //业务水平专业度
    @FieldView(id = R.id.score_business)
    private ScoreLayout_all sl_business;
    @FieldView(id = R.id.jsv_select)
    private JudgeSelectView jsvScore;
    @FieldView(id = R.id.et_content)
    private EditText etContent;
    @FieldView(id = R.id.tv_input_number)
    private TextView tvInputNumber;

    private JudgeBrokerParam commitParam;
    private int mScore = 1;
    private String brokerId;
    private LoadDialog loadDialog;

    @Override
    protected void initView() {
        brokerId = getIntent().getStringExtra("id");
        loadDialog = new LoadDialog(this);
        setCenterText(R.string.title_broker_evaluate);
        sl_house.setTitle("房屋信息真实度：");
        sl_serve.setTitle("态度服务满意度：");
        sl_business.setTitle("业务水平专业度：");
        jsvScore.setOnSlectClick(new JudgeSelectView.OnSlectClick() {
            @Override
            public void getResult(int selectType) {
                mScore = selectType;
            }
        });

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvInputNumber.setText("您还可以输入" + (150 - s.length()) + "个字");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBask() {
        finish();
    }

    /**
     * 提交评价
     */
    @OnClick(id = R.id.btn_submit)
    public void commit() {
        loadDialog.show();

        JudgeBrokerInfo info = new JudgeBrokerInfo();
        info.setMyd(String.valueOf(mScore));
        info.setDataId(brokerId);
        info.setContent(etContent.getText().toString());
        info.setFwtdmyd(sl_house.getScoreNumber());
        info.setFwtdmyd(sl_serve.getScoreNumber());
        info.setYwspzyd(sl_business.getScoreNumber());

        commitParam = new JudgeBrokerParam();
        commitParam.setToken(SettingUtils.get(this, "token", null));
        commitParam.setBody(new Gson().toJson(commitParam));
        apiManager.saveEvaluate(commitParam);
    }

    /**
     * 评价经纪人
     *
     * @param result
     */
    public void onEventMainThread(JudgeBrokerResult result) {
        loadDialog.dismiss();
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            lemonMessage.sendMessage("评价成功");
            finish();
        } else {
            lemonMessage.sendMessage("评价失败");
        }
    }

}
