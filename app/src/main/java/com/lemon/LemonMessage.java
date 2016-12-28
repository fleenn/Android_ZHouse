package com.lemon;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lemon.annotation.Autowired;
import com.lemon.annotation.Component;
import com.lemon.annotation.InitMethod;
import com.lemon.config.Config;
import com.lemon.event.ActivityEvent;
import com.lemon.event.NetNotAvailableEvent;
import com.lemon.event.NetStartEvent;
import com.lemon.event.NetStopEvent;
import com.lemon.event.ParamErrorEvent;
import com.lemon.event.ServerErrorEvent;
import com.lemon.event.ToastEvent;
import com.lemon.event.ToastEventRes;
import com.lemon.util.EventUtil;
import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.component.LoadDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 项目名称:  [Lemon]
 * 包:        [com.lemon]
 * 类描述:    [类描述]
 * 创建人:    [XiaoFeng]
 * 创建时间:  [2015/12/28 23:10]
 * 修改人:    [XiaoFeng]
 * 修改时间:  [2015/12/28 23:10]
 * 修改备注:  [说明本次修改内容]
 * 版本:      [v1.0]
 */
@Component(name = "lemonMessage")
public class LemonMessage {

    @Autowired
    public Context mContext;
    private LoadDialog mLoadDialog;
    private Map<String, Activity> activityMap;
    private int default_dissmiss;

    @InitMethod
    public void init() {
        EventUtil.register(this);
        activityMap = new HashMap<>();
        default_dissmiss = Integer.valueOf(Config.getValue("dismiss_loading_time")) * 1000;
    }

    public void onEventMainThread(ActivityEvent event) {
        activityMap.put(event.getActivityName(), event.getActivity());
    }

    public void onEventMainThread(ServerErrorEvent event) {
        handler.sendEmptyMessage(0);
//        toast("服务器访问异常");
    }

    public void onEventMainThread(NetNotAvailableEvent event) {
        toast("网络不可用");
    }

    public void onEventMainThread(NetStartEvent event) {
        if (ParamUtils.isNull(activityMap.get(getCurrentActivityName()))) {
            return;
        }

        if (ParamUtils.isNull(mLoadDialog)) {
            mLoadDialog = new LoadDialog(activityMap.get(getCurrentActivityName()));
        }
        if (mLoadDialog != null && !mLoadDialog.isShowing()) {
            mLoadDialog.show();
        }
        mLoadDialog.setText("请稍后...");
        handler.sendEmptyMessageDelayed(1, default_dissmiss);
    }

    public void onEventMainThread(ToastEvent event) {
        switch (event.getType()){
            case "0":
                View view = LayoutInflater.from(mContext).inflate(R.layout.custom_toast, null);
                TextView txtToast = (TextView) view.findViewById(R.id.txt_custom_toast);
                txtToast.setText(event.getMessage());
                final Toast toast = new Toast(mContext);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setView(view);
                toast.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 1000);
                break;

            case "1":
                View v1 = LayoutInflater.from(mContext).inflate(R.layout.custom_toast_daily_task, null);
                TextView tv1 = (TextView) v1.findViewById(R.id.txt_custom_toast);
                tv1.setText(event.getMessage());
                final Toast toast1 = new Toast(mContext);
                toast1.setGravity(Gravity.CENTER, 0, 0);
                toast1.setView(v1);
                toast1.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast1.cancel();
                    }
                }, 1000);
                break;
            default:
                Toast.makeText(activityMap.get(getCurrentActivityName()), event.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onEventMainThread(ToastEventRes toastEventRes) {
        Toast.makeText(activityMap.get(getCurrentActivityName()), toastEventRes.getResId(), Toast.LENGTH_SHORT).show();
    }

    public void onEventMainThread(NetStopEvent event) {
        if (mLoadDialog != null && mLoadDialog.isShowing()) {
            mLoadDialog.dismiss();
        }
        mLoadDialog = null;
    }

    public void onEventMainThread(ParamErrorEvent event) {
        handler.sendEmptyMessage(0);
        toast("参数错误");
    }

    private void toast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    private String getCurrentActivityName() {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Activity.ACTIVITY_SERVICE);
        // get the info from the currently running task
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        return componentInfo.getClassName();
    }

    public void sendMessage(String message) {
        EventBus.getDefault().post(new ToastEvent(message));
    }
    public void sendMessage(String message,String type) {
        EventBus.getDefault().post(new ToastEvent(message,type));
    }

    public void sendMessage(int resId) {
        EventBus.getDefault().post(new ToastEventRes(resId));
    }

    private Handler handler = new Handler() {
        @Override

        public void handleMessage(Message msg) {
            if (!ParamUtils.isNull(mLoadDialog)) {
                mLoadDialog.dismiss();
                mLoadDialog = null;
            }
        }
    };

}