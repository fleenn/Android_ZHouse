package com.zfb.house.ui;

import android.content.Intent;

import com.lemon.LemonActivity;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.zfb.house.R;

/**
 * 选择用户类型界面
 * Created by Administrator on 2016/8/14.
 */
@Layout(id = R.layout.activity_select_usertype)
public class SelectUserTypeActivity extends LemonActivity {


    /**
     * 经纪人注册
     */
    @OnClick(id = R.id.txt_type_broker)
    public void selectBrokerRegister() {
        Intent intent = new Intent(mContext, RegisterActivity.class);
        intent.putExtra("userType", "1");
        startActivity(intent);
    }

    /**
     * 用户注册
     */
    @OnClick(id = R.id.txt_type_user)
    public void selectUserRegister() {
        Intent intent = new Intent(mContext, RegisterActivity.class);
        intent.putExtra("userType", "0");
        startActivity(intent);
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.btn_back)
    public void toBack() {
        finish();
    }

}
