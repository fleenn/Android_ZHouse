package com.zfb.house.model.bean;

/**
 * 小区实体
 * Created by Administrator on 2016/5/20.
 */
public class VillageData {
    public String id;//小区ID
    public String pageM;
    public Boolean isNewRecord;
    public String remarks;
    public String pid;
    public String name;//小区名
    public String letter;
    public String areaName;
    public String x;
    public String y;
    public String type;

    public boolean isSelect;//判断是否选中

    //格式不要改变
    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + id + '\"' +
                ", \"pageM\":\"" + pageM + '\"' +
                ", \"isNewRecord\":\"" + isNewRecord + '\"' +
                ", \"remarks\":\"" + remarks + '\"' +
                ", \"pid\":\"" + pid + '\"' +
                ", \"name\":\"" + name + '\"' +
                ", \"letter\":\"" + letter + '\"' +
                ", \"areaName\":\"" + areaName + '\"' +
                ", \"x\":\"" + x + '\"' +
                ", \"y\":\"" + y + '\"' +
                ", \"type\":\"" + type + '\"' +
                '}';
    }

    public void setSelect(Boolean select) {
        isSelect = select;
    }

}
