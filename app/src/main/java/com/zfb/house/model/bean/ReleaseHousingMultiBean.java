package com.zfb.house.model.bean;

/**
 * Created by linwenbing on 16/6/7.
 */
public class ReleaseHousingMultiBean {
    private String title;
    private String value;
    private boolean isSelect;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }
}
