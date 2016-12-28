package com.zfb.house.ui;

import com.lemon.LemonActivity;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.zfb.house.R;

/**
 * 我的 -> 设置 -> 关于我们
 * Created by Administrator on 2016/5/23.
 */
@Layout(id = R.layout.activity_about_us)
public class AboutUsActivity extends LemonActivity {

    @Override
    protected void initView() {
        setCenterText(R.string.title_to_about);
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        finish();
    }

}
