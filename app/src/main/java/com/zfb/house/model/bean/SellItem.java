package com.zfb.house.model.bean;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * 项目名称:  [zfbandroid]
 * 包:        [com.zfb.house.model.bean]
 * 类描述:    [类描述]
 * 创建人:    [XiaoFeng]
 * 创建时间:  [2016/6/5 23:13]
 * 修改人:    [XiaoFeng]
 * 修改时间:  [2016/6/5 23:13]
 * 修改备注:  [说明本次修改内容]
 * 版本:      [v1.0]
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SellItem implements Serializable {
    private String officeId;//办公楼ID
    private String remark;//备注
    private String serverAreaId;//
    private String validityPeriod;//有效期
    private String communtityName;//小区名称
    private String unitPrice;//单价
    private String pop;//
    private String shopTag;//商铺标记
    private String contactSex;//联系人性别
    private String userType;//用户类型
    private String contacts;//联系人
    private String dbName;
    private String shopCategoryName;//商铺分类名
    private String acceptDate;//接受时间
    private String userId;//用户ID
    private String age;//用户年龄
    private boolean isNewRecord;
    private String userName;//用户名
    private String decorationLevel;//装修等级
    private String releaseType;//发布类型
    private String directionName;//朝向
    private String updateBy;//
    private String browseQuantity;//浏览数量
    private int limit;//限制
    private String transfer;//过户
    private String validateCode;//验证码
    private String propertyRightTypeName;//产权类型
    private String regionName;//地区名
    private String shopState;//商铺状态
    private String display;
    private String property;//产权

    private String officeLevel;
    private String officeLevelName;//级别
    private String category;//分类
    private String resourceType;
    private String resourceTypeName;//房源类型：1.住宅、2.别墅、3.写字楼、4.商铺
    private String traffic;//交通
    private String decorationLevelName;//装修
    private String floor;//楼层
    private String officeType;
    private String officeTypeName;//类型
    private String collectionNum;//收藏数量
    private String cutEnabled;//
    private String serverAreaName;//服务区名
    private String flatShareType;//
    private String release;
    private String serviceDistrict;//服务区
    private String contactWay;//联系方式
    private String sellTagName;//出售标签名
    private String direction;//朝向
    private String shopStateName;//商铺状态名
    private String userPhoto;//用户照片
    private String excellent;
    private String propertyRightType;
    private String createBy;
    private String id;
    private String title;
    private String syncState;
    private String area;
    private String propertyCost;
    private String officeName;
    private String synchronizedId;
    private String layout;//户型
    private String wishPrice;//售价
    private String serviceVillage;//服务片区
    private String createDate;//创建时间
    private String areaEnabled;
    private String totalPrice;//总价
    private String communtityNameName;
    private String resourceNum;//资源数量
    private String releaseFlag;//
    private String provides;
    private String shopCategory;//店铺分类
    private String sellTag;//出售标签
    private String photo;
    private String updateDate;
    private String lastUpdateTime;//时间
    private String propertyCompany;
    private String around;//附近
    private String shopType;
    private String address;
    private String y;
    private String brokers;//经纪人
    private String x;
    private String lift;
    private String liftType;
    private String isCutEnabled;
    private String shopTagName;
    private String providesName;
    private String sid;//关系ID
    public boolean isEdit;

    private String shopTypeName;
    public String getShopTypeName() {
        return shopTypeName;
    }

    public void setShopTypeName(String shopTypeName) {
        this.shopTypeName = shopTypeName;
    }


    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getOfficeTypeName() {
        return officeTypeName;
    }

    public void setOfficeTypeName(String officeTypeName) {
        this.officeTypeName = officeTypeName;
    }

    public boolean isNewRecord() {
        return isNewRecord;
    }

    public void setNewRecord(boolean newRecord) {
        isNewRecord = newRecord;
    }

    public String getOfficeLevelName() {
        return officeLevelName;
    }

    public void setOfficeLevelName(String officeLevelName) {
        this.officeLevelName = officeLevelName;
    }

    public String getUpDown() {
        return upDown;
    }

    public void setUpDown(String upDown) {
        this.upDown = upDown;
    }

    private String upDown;

    public String getProvidesName() {
        return providesName;
    }

    public void setProvidesName(String providesName) {
        this.providesName = providesName;
    }

    public String getShopTagName() {
        return shopTagName;
    }

    public void setShopTagName(String shopTagName) {
        this.shopTagName = shopTagName;
    }

    public String getIsCutEnabled() {
        return isCutEnabled;
    }

    public void setIsCutEnabled(String isCutEnabled) {
        this.isCutEnabled = isCutEnabled;
    }

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

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setServerAreaId(String serverAreaId) {
        this.serverAreaId = serverAreaId;
    }

    public void setValidityPeriod(String validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public void setCommuntityName(String communtityName) {
        this.communtityName = communtityName;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setPop(String pop) {
        this.pop = pop;
    }

    public void setShopTag(String shopTag) {
        this.shopTag = shopTag;
    }

    public void setContactSex(String contactSex) {
        this.contactSex = contactSex;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public void setShopCategoryName(String shopCategoryName) {
        this.shopCategoryName = shopCategoryName;
    }

    public void setAcceptDate(String acceptDate) {
        this.acceptDate = acceptDate;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setIsNewRecord(boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setDecorationLevel(String decorationLevel) {
        this.decorationLevel = decorationLevel;
    }

    public void setReleaseType(String releaseType) {
        this.releaseType = releaseType;
    }

    public void setDirectionName(String directionName) {
        this.directionName = directionName;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public void setBrowseQuantity(String browseQuantity) {
        this.browseQuantity = browseQuantity;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setTransfer(String transfer) {
        this.transfer = transfer;
    }

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }

    public void setPropertyRightTypeName(String propertyRightTypeName) {
        this.propertyRightTypeName = propertyRightTypeName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public void setShopState(String shopState) {
        this.shopState = shopState;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public void setOfficeLevel(String officeLevel) {
        this.officeLevel = officeLevel;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setResourceTypeName(String resourceTypeName) {
        this.resourceTypeName = resourceTypeName;
    }

    public void setTraffic(String traffic) {
        this.traffic = traffic;
    }

    public void setDecorationLevelName(String decorationLevelName) {
        this.decorationLevelName = decorationLevelName;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public void setOfficeType(String officeType) {
        this.officeType = officeType;
    }

    public void setCollectionNum(String collectionNum) {
        this.collectionNum = collectionNum;
    }

    public void setCutEnabled(String cutEnabled) {
        this.cutEnabled = cutEnabled;
    }

    public void setServerAreaName(String serverAreaName) {
        this.serverAreaName = serverAreaName;
    }

    public void setFlatShareType(String flatShareType) {
        this.flatShareType = flatShareType;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public void setServiceDistrict(String serviceDistrict) {
        this.serviceDistrict = serviceDistrict;
    }

    public void setContactWay(String contactWay) {
        this.contactWay = contactWay;
    }

    public void setSellTagName(String sellTagName) {
        this.sellTagName = sellTagName;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setShopStateName(String shopStateName) {
        this.shopStateName = shopStateName;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public void setExcellent(String excellent) {
        this.excellent = excellent;
    }

    public void setPropertyRightType(String propertyRightType) {
        this.propertyRightType = propertyRightType;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSyncState(String syncState) {
        this.syncState = syncState;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setPropertyCost(String propertyCost) {
        this.propertyCost = propertyCost;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public void setSynchronizedId(String synchronizedId) {
        this.synchronizedId = synchronizedId;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public void setWishPrice(String wishPrice) {
        this.wishPrice = wishPrice;
    }

    public void setServiceVillage(String serviceVillage) {
        this.serviceVillage = serviceVillage;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setAreaEnabled(String areaEnabled) {
        this.areaEnabled = areaEnabled;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setCommuntityNameName(String communtityNameName) {
        this.communtityNameName = communtityNameName;
    }

    public void setResourceNum(String resourceNum) {
        this.resourceNum = resourceNum;
    }

    public void setReleaseFlag(String releaseFlag) {
        this.releaseFlag = releaseFlag;
    }

    public void setProvides(String provides) {
        this.provides = provides;
    }

    public void setShopCategory(String shopCategory) {
        this.shopCategory = shopCategory;
    }

    public void setSellTag(String sellTag) {
        this.sellTag = sellTag;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public void setPropertyCompany(String propertyCompany) {
        this.propertyCompany = propertyCompany;
    }

    public void setAround(String around) {
        this.around = around;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setY(String y) {
        this.y = y;
    }

    public void setBrokers(String brokers) {
        this.brokers = brokers;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getOfficeId() {
        return officeId;
    }

    public String getRemark() {
        return remark;
    }

    public String getServerAreaId() {
        return serverAreaId;
    }

    public String getValidityPeriod() {
        return validityPeriod;
    }

    public String getCommuntityName() {
        return communtityName;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public String getPop() {
        return pop;
    }

    public String getShopTag() {
        return shopTag;
    }

    public String getContactSex() {
        return contactSex;
    }

    public String getUserType() {
        return userType;
    }

    public String getContacts() {
        return contacts;
    }

    public String getDbName() {
        return dbName;
    }

    public String getShopCategoryName() {
        return shopCategoryName;
    }

    public String getAcceptDate() {
        return acceptDate;
    }

    public String getUserId() {
        return userId;
    }

    public String getAge() {
        return age;
    }

    public boolean isIsNewRecord() {
        return isNewRecord;
    }

    public String getUserName() {
        return userName;
    }

    public String getDecorationLevel() {
        return decorationLevel;
    }

    public String getReleaseType() {
        return releaseType;
    }

    public String getDirectionName() {
        return directionName;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public String getBrowseQuantity() {
        return browseQuantity;
    }

    public int getLimit() {
        return limit;
    }

    public String getTransfer() {
        return transfer;
    }

    public String getValidateCode() {
        return validateCode;
    }

    public String getPropertyRightTypeName() {
        return propertyRightTypeName;
    }

    public String getRegionName() {
        return regionName;
    }

    public String getShopState() {
        return shopState;
    }

    public String getDisplay() {
        return display;
    }

    public String getProperty() {
        return property;
    }

    public String getOfficeLevel() {
        return officeLevel;
    }

    public String getCategory() {
        return category;
    }

    public String getResourceTypeName() {
        return resourceTypeName;
    }

    public String getTraffic() {
        return traffic;
    }

    public String getDecorationLevelName() {
        return decorationLevelName;
    }

    public String getFloor() {
        return floor;
    }

    public String getOfficeType() {
        return officeType;
    }

    public String getCollectionNum() {
        return collectionNum;
    }

    public String getCutEnabled() {
        return cutEnabled;
    }

    public String getServerAreaName() {
        return serverAreaName;
    }

    public String getFlatShareType() {
        return flatShareType;
    }

    public String getRelease() {
        return release;
    }

    public String getServiceDistrict() {
        return serviceDistrict;
    }

    public String getContactWay() {
        return contactWay;
    }

    public String getSellTagName() {
        return sellTagName;
    }

    public String getDirection() {
        return direction;
    }

    public String getShopStateName() {
        return shopStateName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public String getExcellent() {
        return excellent;
    }

    public String getPropertyRightType() {
        return propertyRightType;
    }

    public String getCreateBy() {
        return createBy;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSyncState() {
        return syncState;
    }

    public String getArea() {
        return area;
    }

    public String getPropertyCost() {
        return propertyCost;
    }

    public String getOfficeName() {
        return officeName;
    }

    public String getSynchronizedId() {
        return synchronizedId;
    }

    public String getLayout() {
        return layout;
    }

    public String getWishPrice() {
        return wishPrice;
    }

    public String getServiceVillage() {
        return serviceVillage;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getAreaEnabled() {
        return areaEnabled;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public String getCommuntityNameName() {
        return communtityNameName;
    }

    public String getResourceNum() {
        return resourceNum;
    }

    public String getReleaseFlag() {
        return releaseFlag;
    }

    public String getProvides() {
        return provides;
    }

    public String getShopCategory() {
        return shopCategory;
    }

    public String getSellTag() {
        return sellTag;
    }

    public String getPhoto() {
        return photo;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public String getPropertyCompany() {
        return propertyCompany;
    }

    public String getAround() {
        return around;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getShopType() {
        return shopType;
    }

    public String getAddress() {
        return address;
    }

    public String getY() {
        return y;
    }

    public String getBrokers() {
        return brokers;
    }

    public String getX() {
        return x;
    }


}
