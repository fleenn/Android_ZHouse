package com.lemon;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.lemon.annotation.Autowired;
import com.lemon.annotation.Component;
import com.lemon.annotation.InitMethod;
import com.lemon.event.ActivityEvent;
import com.lemon.event.AnonLoginEvent;
import com.lemon.util.EventUtil;
import com.zfb.house.ui.LoginActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目名称:  [Lemon]
 * 包:        [com.lemon]
 * 类描述:    [类描述]
 * 创建人:    [XiaoFeng]
 * 创建时间:  [2016/2/3 23:54]
 * 修改人:    [XiaoFeng]
 * 修改时间:  [2016/2/3 23:54]
 * 修改备注:  [说明本次修改内容]
 * 版本:      [v1.0]
 */
@Component
public class LemonActivityManager {

    @Autowired
    public Context mContext;
    private Map<String, Activity> activityMap;

    @InitMethod
    public void init() {
        EventUtil.register(this);
        activityMap = new HashMap<>();
    }

    public void onEventMainThread(ActivityEvent event) {
        activityMap.put(event.getActivityName(), event.getActivity());
    }

    public Activity getCurrentActivity(){
        return activityMap.get(getCurrentActivityName());
    }
    public void removeActivity(String activityName){
        activityMap.remove(activityName);
    }

    public void removeCurrentActivity(){
        activityMap.remove(getCurrentActivityName());
    }

    private String getCurrentActivityName() {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Activity.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        return componentInfo.getClassName();
    }

    public void onEventMainThread(AnonLoginEvent event) {
        getCurrentActivity().startActivity(new Intent(getCurrentActivity(), LoginActivity.class));
    }
}
