package com.lemon;

import android.support.multidex.MultiDexApplication;

import com.zfb.house.emchat.DemoHXSDKHelper;

/**
 * 项目名称:  [Lemon]
 * 包:        [com.lemon]
 * 类描述:    [简要描述]
 * 创建人:    [xflu]
 * 创建时间:  [2015/12/18 8:47]
 * 修改人:    [xflu]
 * 修改时间:  [2015/12/18 8:47]
 * 修改备注:  [说明本次修改内容]
 * 版本:      [v1.0]
 */
public class LemonApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
//        EMChat.getInstance().init(getApplicationContext());
//        /**
//         * debugMode == true 时为打开，sdk 会在log里输入调试信息
//         * @param debugMode
//         * 在做代码混淆的时候需要设置成false
//         */
//        EMChat.getInstance().setDebugMode(false);
        //init base module
        ApplicationEngine.start(getApplicationContext());
        //init环信
        DemoHXSDKHelper hxSDKHelper = new DemoHXSDKHelper();
        hxSDKHelper.onInit(this);//父类 onInit 里面有设置各种监听事件

    }
}
