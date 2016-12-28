package com.zfb.house.model.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/9.
 */
public class GoodsData {
    private Integer point;//积分数量
    private boolean canDone;//是否为可签到，true为可签到，false为不可签到即已经签到了
    private Integer continueSign;
    private GoodsList items;
    private List<Banner> banner;

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public boolean isCanDone() {
        return canDone;
    }

    public void setCanDone(boolean canDone) {
        this.canDone = canDone;
    }

    public Integer getContinueSign() {
        return continueSign;
    }

    public void setContinueSign(Integer continueSign) {
        this.continueSign = continueSign;
    }

    public GoodsList getItems() {
        return items;
    }

    public void setItems(GoodsList items) {
        this.items = items;
    }

    public List<Banner> getBanner() {
        return banner;
    }

    public void setBanner(List<Banner> banner) {
        this.banner = banner;
    }
}
