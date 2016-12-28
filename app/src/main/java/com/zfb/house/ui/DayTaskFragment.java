package com.zfb.house.ui;

import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lemon.LemonFragment;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.model.StatusCode;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.DayTaskAdapter;
import com.zfb.house.model.bean.DayTaskData;
import com.zfb.house.model.param.DayTaskParam;
import com.zfb.house.model.result.DayTaskResult;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 积分商城 -> 任务中心 -> 每日任务
 * Created by Administrator on 2016/8/11.
 */
@Layout(id = R.layout.fragment_task_day)
public class DayTaskFragment extends LemonFragment {

    private static final String TAG = "DayTaskFragment";

    //每日任务列表
    @FieldView(id = R.id.lv_day_task)
    private ListView lvDayTask;

    @Override
    protected void initData() {
        //调用“每日任务”的接口
        DayTaskParam dayTaskParam = new DayTaskParam();
        dayTaskParam.setToken(SettingUtils.get(getActivity(), "token", null));
        dayTaskParam.setTaskType("1");
        dayTaskParam.setTag(TAG);
        apiManager.myDailyTask(dayTaskParam);
    }

    /**
     * 每日任务
     *
     * @param result
     */
    public void onEventMainThread(DayTaskResult result) {
        if (!((DayTaskParam) result.getParam()).getTag().equals(TAG)) {
            return;
        }
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<DayTaskData>>() {
            }.getType();
            String str = gson.toJson(result.getData());
            List<DayTaskData> data = gson.fromJson(str, type);
            lvDayTask.setAdapter(new DayTaskAdapter(getActivity(), data));
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

}
