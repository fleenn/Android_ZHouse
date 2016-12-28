package com.zfb.house.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lemon.config.Config;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;

public class DefaultIntro extends IntroActivity {

    @Override
    public void init(Bundle savedInstanceState) {
        addSlide(new FirstSlideFragment(), getApplicationContext());
        addSlide(new SecondSlideFragment(), getApplicationContext());
        addSlide(new ThirdSlideFragment(), getApplicationContext());
        addSlide(new FouthSlideFragment(), getApplicationContext());
    }

    private void loadMainActivity() {
        Intent intent;
        intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSkipPressed() {
        loadMainActivity();
    }

    @Override
    public void onDonePressed() {
        loadMainActivity();
    }

    public void getStarted(View v) {
        loadMainActivity();
    }
}
