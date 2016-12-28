package com.zfb.house.model.bean;

/**
 * 订单实体
 * Created by Administrator on 2016/8/10.
 */
public class Order {
    private String id;//订单ID
    private String itemId;//商品ID
    private int itemNum;//商品个数
    private String itemName;//商品名称
    private String itemPic;//商品照片
    private String itemPoint;//商品所需金币数
    private String receiver;//收货人
    private String address;//收货地址
    private String mobile;//联系电话
    private String createTime;
    private String updateTime;
    private String createTimeStr;//下单时间
    private  String remarks;//备注

    /**
     * 物流状态码，有三种状态码，分别是：
     * 0——准备发货
     * 1——配送中
     * 2——已签收
     */
    private int logisticalStatus;//物流状态
    private String userId;//用户ID

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getItemNum() {
        return itemNum;
    }

    public void setItemNum(int itemNum) {
        this.itemNum = itemNum;
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

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public int getLogisticalStatus() {
        return logisticalStatus;
    }

    public void setLogisticalStatus(int logisticalStatus) {
        this.logisticalStatus = logisticalStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
