package com.lemon.event;

/**
 * Created by Administrator on 2016/8/4.
 */
public class RefreshMomentsEvent {
    private String id;
    private boolean isPraise;
    private int praiseCount;
    private int collectCount;

    public int getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }

    public boolean isPraise() {
        return isPraise;
    }

    public void setPraise(boolean praise) {
        isPraise = praise;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
