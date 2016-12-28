package com.zfb.house.ui;

import android.content.Intent;
import android.os.Message;

import com.lemon.LemonActivity;
import com.lemon.annotation.Layout;
import com.zfb.house.R;

/**
 * Created by Snekey on 2016/7/15.
 */
@Layout(id = R.layout.activity_splash)
public class SplashActivity extends LemonActivity{


    @Override
    protected void initData() {
        handler.sendEmptyMessageDelayed(2,1500);
    }

    @Override
    public void notificationMessage(Message msg) {
        Intent intent = new Intent(this, DefaultIntro.class);
        startActivity(intent);
        super.finish();
    }
}
