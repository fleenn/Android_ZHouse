package com.zfb.house.ui;

import com.lemon.LemonActivity;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.zfb.house.R;

/**
 * 注册-> 用户服务协议
 * Created by Administrator on 2016/7/11.
 */
@Layout(id = R.layout.activity_agreement)
public class AgreementActivity extends LemonActivity {

    @Override
    protected void initView() {
        setCenterText(R.string.title_to_agreement);
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        finish();
    }

}
