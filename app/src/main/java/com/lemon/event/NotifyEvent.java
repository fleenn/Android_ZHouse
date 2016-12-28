package com.lemon.event;

/**
 * Created by Snekey on 2016/7/4.
 */
public class NotifyEvent {
    private int badge;

    public int getBadge() {
        return badge;
    }

    public NotifyEvent(int badge) {
        this.badge = badge;
    }
}
