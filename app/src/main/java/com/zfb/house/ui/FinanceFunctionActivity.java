package com.zfb.house.ui;

import android.content.Intent;
import android.widget.ImageView;

import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.zfb.house.R;

/**
 * 售后金融 -> 功能点界面
 * Created by Administrator on 2016/7/13.
 */
@Layout(id = R.layout.activity_finance_function)
public class FinanceFunctionActivity extends LemonActivity {

    //背景图
    @FieldView(id = R.id.img_bg)
    private ImageView imgBg;
    //申请类型
    private String type;

    @Override
    protected void initView() {
        Intent intent = getIntent();
        setCenterText(intent.getStringExtra("title"));
        imgBg.setBackgroundDrawable(getResources().getDrawable(intent.getIntExtra("bg", 0)));
        type = intent.getStringExtra("type");
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        finish();
    }

    /**
     * 立即申请预约
     */
    @OnClick(id = R.id.btn_application)
    public void immediatelyApply() {
        Intent intent = new Intent(mContext, FinanceInformationActivity.class);
        intent.putExtra("type", "1");
        startActivity(intent);
    }

}
