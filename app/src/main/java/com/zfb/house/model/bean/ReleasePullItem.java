package com.zfb.house.model.bean;

/**
 * Created by linwenbing on 16/6/14.
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
public class ReleasePullItem {
    private String remark;
    private String description;
    private String value;
    private String label;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
