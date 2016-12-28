package com.zfb.house.ui;

import com.lemon.LemonActivity;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.zfb.house.R;

/**
 * 积分商城 -> 游戏中心
 * Created by HourGlassRemember on 2016/8/5.
 */
@Layout(id = R.layout.activity_game_center)
public class GameCenterActivity extends LemonActivity {

    @Override
    protected void initView() {
        setCenterText(R.string.title_game_center);
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        finish();
    }




}
