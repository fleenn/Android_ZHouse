package com.zfb.house.jpush;

import android.content.Context;

import com.lemon.event.StartJpushEvent;
import com.lemon.event.StopJpushEvent;
import com.lemon.init.AbstractInitializer;
import com.lemon.util.EventUtil;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.model.bean.UserBean;

import cn.jpush.android.api.JPushInterface;

/**
 * 项目名称:  [CarMonitor]
 * 包:        [com.lemon.init]
 * 类描述:    [执行配置好的初始化方法]
 * 创建人:    [xflu]
 * 创建时间:  [2015/12/18 14:15]
 * 修改人:    [xflu]
 * 修改时间:  [2015/12/18 14:15]
 * 修改备注:  [说明本次修改内容]
 * 版本:      [v1.0]
 */
public class JpushInitializer extends AbstractInitializer {
    public Context mContext;

    @Override
    public Object initialize(Object... objects) throws Exception {
        EventUtil.register(this);
        startJpush();
        return null;
    }

    public void onEventAsync(StopJpushEvent event) {
        JPushInterface.setAlias(mContext, "", null);
        JPushInterface.init(mContext);
    }

    public void onEventAsync(StartJpushEvent event) {
        startJpush();
    }

    private void startJpush() {
        if (!SettingUtils.get(mContext, "ispush", true)) {
            return;
        }
        if (ParamUtils.isNull(UserBean.getInstance(mContext))) {
            return;
        }
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        //约定注册的value。 看是userid还是手机号
        String value = UserBean.getInstance(mContext).phone;
        JPushInterface.setAlias(mContext, value, null);
        if(JPushInterface.isPushStopped(mContext)){
            JPushInterface.resumePush(mContext);
        }
        JPushInterface.init(mContext);            // 初始化 JPush
    }
}
