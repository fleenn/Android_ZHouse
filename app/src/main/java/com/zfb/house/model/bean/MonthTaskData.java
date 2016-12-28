package com.zfb.house.model.bean;

/**
 * 任务中心-> 每月任务实体
 * Created by HourGlassRemember on 2016/8/26.
 */
public class MonthTaskData {
    private String text;//任务名
    private String totalPoint;//任务所得积分
    private String totalDays;//当前完成进度
    private String singInTimes;//总进度

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(String totalPoint) {
        this.totalPoint = totalPoint;
    }

    public String getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(String totalDays) {
        this.totalDays = totalDays;
    }

    public String getSingInTimes() {
        return singInTimes;
    }

    public void setSingInTimes(String singInTimes) {
        this.singInTimes = singInTimes;
    }
}
