package com.zfb.house.model.bean;

/**
 * 任务中心-> 每日任务实体/新手任务实体
 * Created by HourGlassRemember on 2016/8/11.
 */
public class DayTaskData {
    private String id;//任务ID
    private String comment;//任务名|任务详情
    private int taskPoint;//任务所得积分
    private long doneTime;//任务状态
    private int done;
    private String userType;
    private String userId;
    private String taskType;
    private String taskName;
    private String taskSource;
    private int dayLimit;//每日次数限制
    private int monthLimit;//每月次数限制
    private String getPointDate;
    private String startDate;
    private String endDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getTaskPoint() {
        return taskPoint;
    }

    public void setTaskPoint(int taskPoint) {
        this.taskPoint = taskPoint;
    }

    public long getDoneTime() {
        return doneTime;
    }

    public void setDoneTime(long doneTime) {
        this.doneTime = doneTime;
    }

    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskSource() {
        return taskSource;
    }

    public void setTaskSource(String taskSource) {
        this.taskSource = taskSource;
    }

    public int getDayLimit() {
        return dayLimit;
    }

    public void setDayLimit(int dayLimit) {
        this.dayLimit = dayLimit;
    }

    public int getMonthLimit() {
        return monthLimit;
    }

    public void setMonthLimit(int monthLimit) {
        this.monthLimit = monthLimit;
    }

    public String getGetPointDate() {
        return getPointDate;
    }

    public void setGetPointDate(String getPointDate) {
        this.getPointDate = getPointDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
