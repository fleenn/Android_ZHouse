package com.zfb.house.emchat.temp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.zfb.house.R;

/**
 * Class Function Instructions:实现Fragment绑定
 *
 * @Version 1.0.0
 * @Author Silence
 */
public abstract class BaseFragmentsActivity extends FragmentActivity {

    private int statusBarHeight = 0;

    /**
     * 每个activity添加的Fragment
     */
    public BaseActivityFragment baseFragment = null;

    protected abstract BaseActivityFragment createFragment(Bundle arg0);


    /**
     * baseFragment调用该方法请求数据操作
     *
     * @param actionId 事件ID
     * @param params   请求携带参数
     */
    public abstract void executeAction(int actionId, Object params);


    //该方法用于创建不同的Fragment对象

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            statusBarHeight = ScreenUtil.getStatusBarHeight(this);
//            if (statusBarHeight > 0) {
//                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//设置了通知栏透明
//            }
//        }

        setContentView(R.layout.activity_base);
//        findViewById(R.id.tab_main_status_bar).setPadding(0, statusBarHeight, 0, 0);

        /* 获取管理器*/
        FragmentManager fm = getSupportFragmentManager();
        baseFragment = (BaseActivityFragment) fm.findFragmentById(R.id.fragmentContainer); //R.id.fragmentContainer就是容器

        if (baseFragment == null) {
            //在activity中添加对应的Fragment的过程
            baseFragment = createFragment(getIntent().getExtras());
            fm.beginTransaction()     //具体的操作类似数据库的事务处理过程
                    .add(R.id.fragmentContainer, baseFragment)
                    .commit();
        }
    }


}
