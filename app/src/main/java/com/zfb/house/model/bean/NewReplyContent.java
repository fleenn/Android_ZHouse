package com.zfb.house.model.bean;

/**
 * Created by Snekey on 2016/5/28.
 */
public class NewReplyContent {
    private int count;
    private ReplyContent reply;
    private int totalPoint;
    private int getPoint = 0;

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ReplyContent getReply() {
        return reply;
    }

    public void setReply(ReplyContent reply) {
        this.reply = reply;
    }
}
