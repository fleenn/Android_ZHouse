package com.zfb.house.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.zfb.house.R;
import com.zfb.house.adapter.MyPagerAdapter;
import com.zfb.house.component.CirclePageIndicator;
import com.zfb.house.component.FadePageTransformer;

import java.util.List;
import java.util.Vector;

public abstract class IntroActivity extends FragmentActivity {
    private MyPagerAdapter mPagerAdapter;
    private ViewPager pager;
    private List<Fragment> fragments = new Vector<>();
    private List<ImageView> dots;
    private int slidesNumber;
    private Vibrator mVibrator;
    private boolean isVibrateOn = false;
    private int vibrateIntensity = 20;
    private boolean showSkip = true;

    private static final int FIRST_PAGE_NUM = 0;

    @Override
    final protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置窗体为全屏
        if (getSharedPreferences("SP_ShareData",Context.MODE_PRIVATE).getBoolean("appStart",false)){
            onSkipPressed();
        } else {
            getSharedPreferences("SP_ShareData",Context.MODE_PRIVATE).edit().putBoolean("appStart", true).commit();
        }

        setContentView(R.layout.intro_layout);

        mVibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);

        init(savedInstanceState);

//        slidesNumber = fragments.size();

        mPagerAdapter = new MyPagerAdapter(super.getSupportFragmentManager(), fragments);
        pager = (ViewPager) findViewById(R.id.view_pager);
        pager.setAdapter(this.mPagerAdapter);

        /**
         *  ViewPager.setOnPageChangeListener is now deprecated. Use addOnPageChangeListener() instead of it.
         */
        CirclePageIndicator mIndicator = (CirclePageIndicator) findViewById(R.id.indicatorCircle);
        mIndicator.setViewPager(pager);
    }

    public void addSlide(@NonNull Fragment fragment, @NonNull Context context) {
        fragments.add(Fragment.instantiate(context, fragment.getClass().getName()));
//        mPagerAdapter.notifyDataSetChanged();
    }

    @NonNull
    public List<Fragment> getSlides() {
        return mPagerAdapter.getFragments();
    }

    public void setVibrate(boolean vibrate) {
        this.isVibrateOn = vibrate;
    }

    public void setVibrateIntensity(int intensity) {
        this.vibrateIntensity = intensity;
    }

    public void setFadeAnimation() {
        pager.setPageTransformer(true, new FadePageTransformer());
    }

    public void setCustomTransformer(@Nullable ViewPager.PageTransformer transformer) {
        pager.setPageTransformer(true, transformer);
    }

    public void setOffScreenPageLimit(int limit) {
        pager.setOffscreenPageLimit(limit);
    }

    public abstract void init(@Nullable Bundle savedInstanceState);

    public abstract void onSkipPressed();

    public abstract void onDonePressed();
}