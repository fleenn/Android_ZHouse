package com.zfb.house.model.bean;

/**
 * Created by Administrator on 2016/8/11.
 */
public class PointData {
    private int totalPoint;//总积分
    private int getPoint;//本次操作改变的积分数
    private String taskResult;

    public int getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(int totalPoint) {
        this.totalPoint = totalPoint;
    }

    public int getGetPoint() {
        return getPoint;
    }

    public void setGetPoint(int getPoint) {
        this.getPoint = getPoint;
    }

    public String getTaskResult() {
        return taskResult;
    }

    public void setTaskResult(String taskResult) {
        this.taskResult = taskResult;
    }

}
