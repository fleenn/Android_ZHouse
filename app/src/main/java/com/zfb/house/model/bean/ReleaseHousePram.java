package com.zfb.house.model.bean;

import com.lemon.model.BaseParam;

import java.io.Serializable;

/**
 * 发布房源的实体
 * Created by linwenbing on 16/6/16.
 */
public class ReleaseHousePram extends BaseParam implements Serializable {

    /**
     * id : string
     * title : string
     * resourceNum : string
     * floor : string
     * direction : string
     * layout : string
     * address : string
     * decorationLevel : 0
     * category : 0
     * officeId : string
     * regionName : string
     * officeType : 0
     * officeLevel : 0
     * propertyCost : 0
     * propertyCompany : string
     * shopCategory : 0
     * shopType : 0
     * shopState : 0
     * shopTag : string
     * age : string
     * area : 0
     * areaEnabled : 0
     * provides : string
     * traffic : string
     * around : string
     * contacts : string
     * contactSex : 0
     * contactWay : string
     * remark : string
     * display : 0
     * syncState : 0
     * userType : 0
     * communtityName : string
     * resourceType : 0
     * serverAreaId : string
     * serviceDistrict : string
     * serviceVillage : string
     * propertyRightTypeName : string
     * shopCategoryName : string
     * shopStateName : string
     * propertyRightType : 0
     * validateCode : string
     * sellTag : string
     * unitPrice : 0
     * wishPrice : 0
     * releaseType : 0
     * flatShareType : string
     * photos : string
     * release : true
     * cutEnabled : true
     * property : true
     * transfer : true
     */
    private String id;//房源ID
    private String title;
    private String resourceNum;
    private String floor;
    private String direction;
    private String layout;
    private String address;
    private int decorationLevel;
    private int category;
    private String officeId;
    private String regionName;
    private int officeType;
    private int officeLevel;
    private int propertyCost;
    private String propertyCompany;
    private int shopCategory;
    private int shopType;
    private int shopState;
    private String shopTag;
    private String age;
    private String area;
    private int areaEnabled;
    private String provides;
    private String traffic;
    private String around;
    private String contacts;
    private int contactSex;
    private String contactWay;
    private String remark;
    private int display;
    private int syncState;
    private int userType;
    private String communtityName;
    private int resourceType;
    private String serverAreaId;
    private String serviceDistrict;
    private String serviceVillage;
    private String propertyRightTypeName;
    private String shopCategoryName;
    private String shopStateName;
    private String propertyRightType;
    private String validateCode;
    private String sellTag;//售房标签
    private int unitPrice;
    private int wishPrice;//售价
    private int releaseType = 0;
    private String flatShareType;
    private String photos;
    private boolean release;
    private String cutEnabled;
    private boolean property;
    private boolean transfer;
    private String lift = "0";//有无电梯  0无 1有
    private String liftType;//几梯几户
    private String propertyRight;//产权性质
    private String rentalWay;//出租方式
    private String rentalWayName;//出租支付方式
    private String paymentTypeName;//支付方式
    private String photo;

    public String getPhoto() {
        return photo;
    }

    public String getPaymentTypeName() {
        return paymentTypeName;
    }

    public void setPaymentTypeName(String paymentTypeName) {
        this.paymentTypeName = paymentTypeName;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    private String paymentType;//支付方式


    public String getCommuntityNameName() {
        return communtityNameName;
    }

    public void setCommuntityNameName(String communtityNameName) {
        this.communtityNameName = communtityNameName;
    }

    private String communtityNameName;

    private int rental;

    public int getRental() {
        return rental;
    }

    public void setRental(int rental) {
        this.rental = rental;
    }


    public int getRentalType() {
        return rentalType;
    }

    public void setRentalType(int rentalType) {
        this.rentalType = rentalType;
    }

    private int rentalType;

    public String getRentType() {
        return rentType;
    }

    public void setRentType(String rentType) {
        this.rentType = rentType;
    }

    private String rentType;

    public String getLift() {
        return lift;
    }

    public void setLift(String lift) {
        this.lift = lift;
    }

    public String getLiftType() {
        return liftType;
    }

    public void setLiftType(String liftType) {
        this.liftType = liftType;
    }

    public String getPropertyRight() {
        return propertyRight;
    }

    public void setPropertyRight(String propertyRight) {
        this.propertyRight = propertyRight;
    }

    public String getRentalWay() {
        return rentalWay;
    }

    public void setRentalWay(String rentalWay) {
        this.rentalWay = rentalWay;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getResourceNum() {
        return resourceNum;
    }

    public void setResourceNum(String resourceNum) {
        this.resourceNum = resourceNum;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDecorationLevel() {
        return decorationLevel;
    }

    public void setDecorationLevel(int decorationLevel) {
        this.decorationLevel = decorationLevel;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public int getOfficeType() {
        return officeType;
    }

    public void setOfficeType(int officeType) {
        this.officeType = officeType;
    }

    public int getOfficeLevel() {
        return officeLevel;
    }

    public void setOfficeLevel(int officeLevel) {
        this.officeLevel = officeLevel;
    }

    public int getPropertyCost() {
        return propertyCost;
    }

    public void setPropertyCost(int propertyCost) {
        this.propertyCost = propertyCost;
    }

    public String getPropertyCompany() {
        return propertyCompany;
    }

    public void setPropertyCompany(String propertyCompany) {
        this.propertyCompany = propertyCompany;
    }

    public int getShopCategory() {
        return shopCategory;
    }

    public void setShopCategory(int shopCategory) {
        this.shopCategory = shopCategory;
    }

    public int getShopType() {
        return shopType;
    }

    public void setShopType(int shopType) {
        this.shopType = shopType;
    }

    public int getShopState() {
        return shopState;
    }

    public void setShopState(int shopState) {
        this.shopState = shopState;
    }

    public String getShopTag() {
        return shopTag;
    }

    public void setShopTag(String shopTag) {
        this.shopTag = shopTag;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getAreaEnabled() {
        return areaEnabled;
    }

    public void setAreaEnabled(int areaEnabled) {
        this.areaEnabled = areaEnabled;
    }

    public String getProvides() {
        return provides;
    }

    public void setProvides(String provides) {
        this.provides = provides;
    }

    public String getTraffic() {
        return traffic;
    }

    public void setTraffic(String traffic) {
        this.traffic = traffic;
    }

    public String getAround() {
        return around;
    }

    public void setAround(String around) {
        this.around = around;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public int getContactSex() {
        return contactSex;
    }

    public void setContactSex(int contactSex) {
        this.contactSex = contactSex;
    }

    public String getContactWay() {
        return contactWay;
    }

    public void setContactWay(String contactWay) {
        this.contactWay = contactWay;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getDisplay() {
        return display;
    }

    public void setDisplay(int display) {
        this.display = display;
    }

    public int getSyncState() {
        return syncState;
    }

    public void setSyncState(int syncState) {
        this.syncState = syncState;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getCommuntityName() {
        return communtityName;
    }

    public void setCommuntityName(String communtityName) {
        this.communtityName = communtityName;
    }

    public int getResourceType() {
        return resourceType;
    }

    public void setResourceType(int resourceType) {
        this.resourceType = resourceType;
    }

    public String getServerAreaId() {
        return serverAreaId;
    }

    public void setServerAreaId(String serverAreaId) {
        this.serverAreaId = serverAreaId;
    }

    public String getServiceDistrict() {
        return serviceDistrict;
    }

    public void setServiceDistrict(String serviceDistrict) {
        this.serviceDistrict = serviceDistrict;
    }

    public String getServiceVillage() {
        return serviceVillage;
    }

    public void setServiceVillage(String serviceVillage) {
        this.serviceVillage = serviceVillage;
    }

    public String getPropertyRightTypeName() {
        return propertyRightTypeName;
    }

    public void setPropertyRightTypeName(String propertyRightTypeName) {
        this.propertyRightTypeName = propertyRightTypeName;
    }

    public String getShopCategoryName() {
        return shopCategoryName;
    }

    public void setShopCategoryName(String shopCategoryName) {
        this.shopCategoryName = shopCategoryName;
    }

    public String getShopStateName() {
        return shopStateName;
    }

    public void setShopStateName(String shopStateName) {
        this.shopStateName = shopStateName;
    }

    public String getPropertyRightType() {
        return propertyRightType;
    }

    public void setPropertyRightType(String propertyRightType) {
        this.propertyRightType = propertyRightType;
    }

    public String getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }

    public String getSellTag() {
        return sellTag;
    }

    public void setSellTag(String sellTag) {
        this.sellTag = sellTag;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getWishPrice() {
        return wishPrice;
    }

    public void setWishPrice(int wishPrice) {
        this.wishPrice = wishPrice;
    }

    public int getReleaseType() {
        return 0;
    }

    public void setReleaseType(int releaseType) {
        this.releaseType = releaseType;
    }

    public String getFlatShareType() {
        return flatShareType;
    }

    public void setFlatShareType(String flatShareType) {
        this.flatShareType = flatShareType;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public boolean isRelease() {
        return release;
    }

    public void setRelease(boolean release) {
        this.release = release;
    }

    public String getCutEnabled() {
        return cutEnabled;
    }

    public void setCutEnabled(String cutEnabled) {
        this.cutEnabled = cutEnabled;
    }

    public boolean isProperty() {
        return property;
    }

    public void setProperty(boolean property) {
        this.property = property;
    }

    public boolean isTransfer() {
        return transfer;
    }

    public void setTransfer(boolean transfer) {
        this.transfer = transfer;
    }

    public String getRentalWayName() {
        return rentalWayName;
    }

    public void setRentalWayName(String rentalWayName) {
        this.rentalWayName = rentalWayName;
    }
}
