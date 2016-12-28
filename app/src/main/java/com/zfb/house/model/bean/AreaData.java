package com.zfb.house.model.bean;

import java.io.Serializable;

/**
 * 片区实体
 * Created by Administrator on 2016/5/19.
 */
public class AreaData implements Serializable {
    public String id;//片区ID
    public String pageM;
    public Boolean isNewRecord;
    public String remarks;
    public String parentIds;
    public String name;//片区名
    public int sort;
    public String code;
    public String type;
    public String areaPname;
    public String parentId;
    public String x;
    public String y;

    public boolean isSelect;//判断是否选中

    //格式不要改变
    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + id + '\"' +
                ", \"pageM\":\"" + pageM + '\"' +
                ", \"isNewRecord\":\"" + isNewRecord + '\"' +
                ", \"remarks\":\"" + remarks + '\"' +
                ", \"parentIds\":\"" + parentIds + '\"' +
                ", \"name\":\"" + name + '\"' +
                ", \"sort\":\"" + sort + '\"' +
                ", \"code\":\"" + code + '\"' +
                ", \"type\":\"" + type + '\"' +
                ", \"areaPname\":\"" + areaPname + '\"' +
                ", \"parentId\":\"" + parentId + '\"' +
                ", \"x\":\"" + x + '\"' +
                ", \"y\":\"" + y + '\"' +
                ", \"isSelect\":\"" + isSelect + '\"' +
                '}';
    }

    public void setSelect(Boolean select) {
        isSelect = select;
    }
}
