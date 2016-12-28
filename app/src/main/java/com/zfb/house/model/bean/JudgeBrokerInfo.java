package com.zfb.house.model.bean;

/**
 * Created by linwenbing on 16/7/26./app/evaluate/v2/saveEvaluate
 */
public class JudgeBrokerInfo {
    private String evalutateType;
    private String dataId;
    private String content;
    private int fwxxzsd;
    private int fwtdmyd;
    private int ywspzyd;
    private String myd;

    public String getEvalutateType() {
        return evalutateType;
    }

    public void setEvalutateType(String evalutateType) {
        this.evalutateType = evalutateType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public int getFwxxzsd() {
        return fwxxzsd;
    }

    public void setFwxxzsd(int fwxxzsd) {
        this.fwxxzsd = fwxxzsd;
    }

    public int getFwtdmyd() {
        return fwtdmyd;
    }

    public void setFwtdmyd(int fwtdmyd) {
        this.fwtdmyd = fwtdmyd;
    }

    public int getYwspzyd() {
        return ywspzyd;
    }

    public void setYwspzyd(int ywspzyd) {
        this.ywspzyd = ywspzyd;
    }

    public String getMyd() {
        return myd;
    }

    public void setMyd(String myd) {
        this.myd = myd;
    }
}
