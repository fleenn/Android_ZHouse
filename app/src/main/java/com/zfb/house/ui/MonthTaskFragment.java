package com.zfb.house.ui;

import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lemon.LemonFragment;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.model.StatusCode;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.component.CustomerProgress;
import com.zfb.house.model.bean.MonthTaskData;
import com.zfb.house.model.param.DayTaskParam;
import com.zfb.house.model.result.DayTaskResult;

import java.lang.reflect.Type;

/**
 * 积分商城 -> 任务中心 -> 每月任务
 * Created by Administrator on 2016/8/11.
 */
@Layout(id = R.layout.fragment_task_month)
public class MonthTaskFragment extends LemonFragment {

    private static final String TAG = "MonthTaskFragment";

    //任务名
    @FieldView(id = R.id.txt_task_name)
    private TextView txtTaskName;
    //任务所得积分
    @FieldView(id = R.id.txt_task_coin)
    private TextView txtTaskCoin;
    //任务状态，包括当前完成进度、总进度
    @FieldView(id = R.id.customer_progressbar)
    private CustomerProgress customerProgress;

    @Override
    protected void initData() {
        //调用“每日任务”的接口
        DayTaskParam dayTaskParam = new DayTaskParam();
        dayTaskParam.setToken(SettingUtils.get(getActivity(), "token", null));
        dayTaskParam.setTaskType("3");
        dayTaskParam.setTag(TAG);
        apiManager.myDailyTask(dayTaskParam);
    }

    /**
     * 每月任务——目前只显示一条数据
     *
     * @param result
     */
    public void onEventMainThread(DayTaskResult result) {
        if (!((DayTaskParam) result.getParam()).getTag().equals(TAG)) {
            return;
        }
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            Gson gson = new Gson();
            Type type = new TypeToken<MonthTaskData>() {
            }.getType();
            String str = gson.toJson(result.getData());
            MonthTaskData data = gson.fromJson(str, type);
            txtTaskName.setText(data.getText());
            txtTaskCoin.setText(data.getTotalPoint());
            customerProgress.setProgressValue(Integer.valueOf(data.getSingInTimes()));
            customerProgress.setTotalValue(Integer.valueOf(data.getTotalDays()));
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

}
