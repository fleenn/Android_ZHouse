package com.lemon.event;

/**
 * Created by Snekey on 2016/8/13.
 */
public class UpdatePointEvent {
    private int point = 0;

    public int getPoint() {
        return point;
    }

    public UpdatePointEvent(int point) {
        this.point = point;
    }
}
