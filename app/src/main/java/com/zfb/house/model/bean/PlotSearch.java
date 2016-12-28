package com.zfb.house.model.bean;

import java.io.Serializable;

/**
 * Created by linwenbing on 16/6/14.
 */
public class PlotSearch implements Serializable{

    /**
     * id : 05920102022
     * pageM : null
     * isNewRecord : false
     * remarks : null
     * pid : 05920102
     * name : 湖明路120-126号院
     * letter : H
     * areaName : null
     * x : 118.120654
     * y : 24.490187
     * type : 8
     */

    private String id;
    private Object pageM;
    private boolean isNewRecord;
    private Object remarks;
    private String pid;
    private String name;
    private String letter;
    private Object areaName;
    private String x;
    private String y;
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getPageM() {
        return pageM;
    }

    public void setPageM(Object pageM) {
        this.pageM = pageM;
    }

    public boolean isIsNewRecord() {
        return isNewRecord;
    }

    public void setIsNewRecord(boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
    }

    public Object getRemarks() {
        return remarks;
    }

    public void setRemarks(Object remarks) {
        this.remarks = remarks;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public Object getAreaName() {
        return areaName;
    }

    public void setAreaName(Object areaName) {
        this.areaName = areaName;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
