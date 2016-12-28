package com.zfb.house.ui;

import android.widget.ImageView;

import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.model.bean.UserBean;

/**
 * 金币商城 -> 金币规则
 * Created by HourGlassRemember on 2016/8/16.
 */
@Layout(id = R.layout.activity_coin_rule)
public class CoinRuleActivity extends LemonActivity {

    //背景图
    @FieldView(id = R.id.img_bg)
    private ImageView imgBg;

    @Override
    protected void initView() {
        setCenterText(R.string.title_coin_rule);
        if (!ParamUtils.isEmpty(UserBean.getInstance(mContext).userType)) {
            imgBg.setBackgroundDrawable(UserBean.getInstance(mContext).userType.equals("0") ?
                    getResources().getDrawable(R.drawable.bg_rule_user) : getResources().getDrawable(R.drawable.bg_rule_broker));
        }
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        finish();
    }

}
