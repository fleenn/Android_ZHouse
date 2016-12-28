package com.lemon.event;

/**
 * Created by Snekey on 2016/8/9.
 */
public class ReFreshGrabListEvent {
    private String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public ReFreshGrabListEvent(String tag) {
        this.tag = tag;
    }
}
