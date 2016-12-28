package com.zfb.house.ui;

import android.content.Intent;
import android.view.View;

import com.lemon.LemonFragment;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.zfb.house.R;

import de.greenrobot.event.EventBus;

/**
 * 提示游客登录页面
 * Created by Administrator on 2016/6/2.
 */
@Layout(id = R.layout.fragment_mine_default)
public class MineDefaultFragment extends LemonFragment {

    @Override
    protected void initView() {
        setCenterText(R.string.title_mine);
        mImgTitleLt.setVisibility(View.GONE);
        EventBus.getDefault().register(this);
    }

    @OnClick(id = R.id.btn_mine_register)
    public void toRegister() {//注册
        startActivity(new Intent(getActivity(), RegisterActivity.class));
    }

    @OnClick(id = R.id.btn_mine_login)
    public void toLogin() {//登录
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

}
