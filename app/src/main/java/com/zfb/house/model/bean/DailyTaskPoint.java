package com.zfb.house.model.bean;

/**
 * Created by Snekey on 2016/8/15.
 */
public class DailyTaskPoint {
    private int totalPoint;//总积分
    private int getPoint = 0;//本次操作改变的积分数

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
}
