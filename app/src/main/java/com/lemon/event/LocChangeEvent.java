package com.lemon.event;

import android.nfc.Tag;

/**
 * Created by Snekey on 2016/7/19.
 */
public class LocChangeEvent {
    private String Tag;

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }
}
