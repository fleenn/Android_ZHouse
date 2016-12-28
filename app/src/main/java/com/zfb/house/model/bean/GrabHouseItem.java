package com.zfb.house.model.bean;

import java.util.List;

/**
 * Created by Snekey on 2016/6/18.
 */
public class GrabHouseItem {


    /**
     * communtityName : null
     * userPhoto :
     * rental : 2500
     * photo : http://7xid0d.com1.z0.glb.clouddn.com/1457344400609file1?e=1457348000&amp;amp;token=4P_bABvR-QHFAEygEm3h72k8FztsB_Jri68dkVCI:xOK0lxKl5KWKrofZnviFrvhJzu0=
     * contacts : 林
     * userType : 0
     * id : 68d70eb5484c467fb87a7899a2369d7b
     * area : 250
     * layout : 3,1,2
     * userId :
     * wishPrice : 0
     * brokersList : [{"brokerPhone":"18059273917","brokerPhoto":"http://7xid0d.com1.z0.glb.clouddn.com/8bc522b42fc843638cc9e0db65bdef5dgeolo","brokerName":"王玉成","brokerId":"f861bd20cc2a4551b96b84d7a97f9452","brokerStar":null},{"brokerPhone":"18559239921","brokerPhoto":"http://7xid0d.com1.z0.glb.clouddn.com/pic477539777-537.jpg","brokerName":"智文","brokerId":"a87d96e748c64dcd907e54375e649dd5","brokerStar":null}]
     * userName :
     */

    private String communtityName;
    private String contactPhoto;
    private int rental;
    private String photo;
    private String contacts;
    private int userType;
    private String id;
    private int area;
    private String layout;
    private String officeType;
    private String shopType;
    private String contactUserId;
    private int wishPrice;
    private String userName;
    private List<?> brokersList;
    private String contactWay;
    //    控制点击抢单后按钮变灰字段
    private boolean isSuccess;

    public String getContactWay() {
        return contactWay;
    }

    public void setContactWay(String contactWay) {
        this.contactWay = contactWay;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public List<?> getBrokersList() {
        return brokersList;
    }

    public void setBrokersList(List<?> brokersList) {
        this.brokersList = brokersList;
    }

    public String getCommuntityName() {
        return communtityName;
    }

    public void setCommuntityName(String communtityName) {
        this.communtityName = communtityName;
    }


    public int getRental() {
        return rental;
    }

    public void setRental(int rental) {
        this.rental = rental;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getOfficeType() {
        return officeType;
    }

    public void setOfficeType(String officeType) {
        this.officeType = officeType;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public String getContactPhoto() {
        return contactPhoto;
    }

    public void setContactPhoto(String contactPhoto) {
        this.contactPhoto = contactPhoto;
    }

    public String getContactUserId() {
        return contactUserId;
    }

    public void setContactUserId(String contactUserId) {
        this.contactUserId = contactUserId;
    }

    public int getWishPrice() {
        return wishPrice;
    }

    public void setWishPrice(int wishPrice) {
        this.wishPrice = wishPrice;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
