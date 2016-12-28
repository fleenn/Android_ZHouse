package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * 每日任务 or 新手任务
 * Created by Administrator on 2016/8/11.
 */
@Module(server = "zfb_server", name = "task/v2")
public class DayTaskParam extends BaseParam {
    private String token;
    private String taskType;//任务类型：1——每日任务，2——新手任务
    private String tag;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
