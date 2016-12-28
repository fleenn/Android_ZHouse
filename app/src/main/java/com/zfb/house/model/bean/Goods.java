package com.zfb.house.model.bean;

import java.io.Serializable;

/**
 * 商品实体
 * Created by Administrator on 2016/8/5.
 */
public class Goods implements Serializable{
    private String id;//商品ID
    private String itemName;//商品名称
    private String itemPic;//商品照片
    private String itemPoint;//商品所需金币数
    private String itemDescription;//商品详情
    private String itemRule;//活动规则
    private String isRecommand;//是否推荐
    private String updateTime;//更新时间
    private String createTime;//创建时间
    private int totalCount;//商品在仓库中的总量
    private int changedCount;//已兑换的个数

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPic() {
        return itemPic;
    }

    public void setItemPic(String itemPic) {
        this.itemPic = itemPic;
    }

    public String getItemPoint() {
        return itemPoint;
    }

    public void setItemPoint(String itemPoint) {
        this.itemPoint = itemPoint;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemRule() {
        return itemRule;
    }

    public void setItemRule(String itemRule) {
        this.itemRule = itemRule;
    }

    public String getIsRecommand() {
        return isRecommand;
    }

    public void setIsRecommand(String isRecommand) {
        this.isRecommand = isRecommand;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getChangedCount() {
        return changedCount;
    }

    public void setChangedCount(int changedCount) {
        this.changedCount = changedCount;
    }

}
