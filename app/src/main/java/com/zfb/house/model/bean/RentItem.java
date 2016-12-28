package com.zfb.house.model.bean;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * 项目名称:  [zfbandroid]
 * 包:        [com.zfb.house.model.bean]
 * 类描述:    [类描述]
 * 创建人:    [XiaoFeng]
 * 创建时间:  [2016/6/5 23:12]
 * 修改人:    [XiaoFeng]
 * 修改时间:  [2016/6/5 23:12]
 * 修改备注:  [说明本次修改内容]
 * 版本:      [v1.0]
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RentItem implements Serializable {
    private String totalRental;
    private String officeId;
    private String remark;
    private String serverAreaId;
    private String communtityName;//小区
    private String pop;
    private String shopTag;
    private String paymentTypeName;
    private String contactSex;
    private String userType;//用户类型
    private String contacts;
    private String rentalWay;
    private String dbName;
    private String shopCategoryName;
    private String acceptDate;
    private String userId;
    private String age;//建筑年代
    private String contactStar;
    private boolean isNewRecord;
    private String userName;//用户名
    private String decorationLevel;
    private String updateBy;
    private String browseQuantity;
    private int limit;
    private String propertyRightTypeName;//产权性质
    private String propertyRightType;//产权性质
    private String regionName;
    private String shopState;
    private String display;
    private String property;
    private String rental;//租金
    private String officeLevel;
    private String officeLevelName;//级别
    private String category;
    private String contactPhoto;
    private String traffic;
    private String decorationLevelName;//装修
    private String floor;//楼层
    private String officeType;
    private String officeTypeName;//类型
    private String lift;//有无电梯
    private String collectionNum;
    private String cutEnabled;
    private String serverAreaName;//片区
    private String flatShareType;
    private String release;
    private String serviceDistrict;
    private String sellTag;//售房标签
    private String sellTagName;//售房标签
    private String contactWay;
    private String direction;
    private String shopStateName;//状态
    private String userPhoto;
    private String excellent;
    private String rentRequire;
    private String rentType;
    private String rentTypeName;//出租方式
    private String createBy;
    private String rentalWayName;//支付方式
    private String shopTagName;
    private String id;
    private String title;//标题
    private String syncState;
    private String area;//面积
    private String officeName;//区域，比如：思明区
    private String synchronizedId;
    private String layout;//户型
    private String upDown;
    private String serviceVillage;
    private String createDate;
    private String areaEnabled;
    private String rentalType;
    private String communtityNameName;
    private String resourceNum;
    private String paymentType;
    private String shopTypeName;
    private String releaseFlag;
    private String provides;
    private String shopCategory;
    private String photo;//照片
    private String updateDate;
    private String lastUpdateTime;//时间
    private String propertyCompany;//物业公司
    private String resourceType;
    private String resourceTypeName;//房源类型：1.住宅、2.别墅、3.写字楼、4.商铺
    private String around;
    private String shopType;
    private String address;//房源地址
    private String liftType;//几梯几户
    private String y;
    private String brokers;
    private String x;
    private String providesName;//配套设施
    private String propertyCost;//是否包含物业费
    private String isCutEnabled;
    private String directionName;//朝向
    private String sid;//关系ID
    public boolean isEdit;

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

    public String getOfficeLevelName() {
        return officeLevelName;
    }

    public void setOfficeLevelName(String officeLevelName) {
        this.officeLevelName = officeLevelName;
    }

    public String getSellTag() {
        return sellTag;
    }

    public void setSellTag(String sellTag) {
        this.sellTag = sellTag;
    }

    public boolean isNewRecord() {
        return isNewRecord;
    }

    public void setNewRecord(boolean newRecord) {
        isNewRecord = newRecord;
    }

    public String getRentTypeName() {
        return rentTypeName;
    }

    public void setRentTypeName(String rentTypeName) {
        this.rentTypeName = rentTypeName;
    }

    public String getDirectionName() {
        return directionName;
    }

    public void setDirectionName(String directionName) {
        this.directionName = directionName;
    }

    public String getIsCutEnabled() {
        return isCutEnabled;
    }

    public void setIsCutEnabled(String isCutEnabled) {
        this.isCutEnabled = isCutEnabled;
    }

    public void setTotalRental(String totalRental) {
        this.totalRental = totalRental;
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

    public void setCommuntityName(String communtityName) {
        this.communtityName = communtityName;
    }

    public void setPop(String pop) {
        this.pop = pop;
    }

    public void setShopTag(String shopTag) {
        this.shopTag = shopTag;
    }

    public void setPaymentTypeName(String paymentTypeName) {
        this.paymentTypeName = paymentTypeName;
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

    public void setRentalWay(String rentalWay) {
        this.rentalWay = rentalWay;
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

    public void setContactStar(String contactStar) {
        this.contactStar = contactStar;
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

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public void setBrowseQuantity(String browseQuantity) {
        this.browseQuantity = browseQuantity;
    }

    public void setLimit(int limit) {
        this.limit = limit;
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

    public void setRental(String rental) {
        this.rental = rental;
    }

    public void setOfficeLevel(String officeLevel) {
        this.officeLevel = officeLevel;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setContactPhoto(String contactPhoto) {
        this.contactPhoto = contactPhoto;
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

    public void setLift(String lift) {
        this.lift = lift;
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

    public void setSellTagName(String sellTagName) {
        this.sellTagName = sellTagName;
    }

    public void setContactWay(String contactWay) {
        this.contactWay = contactWay;
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

    public void setRentRequire(String rentRequire) {
        this.rentRequire = rentRequire;
    }

    public void setRentType(String rentType) {
        this.rentType = rentType;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public void setRentalWayName(String rentalWayName) {
        this.rentalWayName = rentalWayName;
    }

    public void setShopTagName(String shopTagName) {
        this.shopTagName = shopTagName;
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

    public void setUpDown(String upDown) {
        this.upDown = upDown;
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

    public void setRentalType(String rentalType) {
        this.rentalType = rentalType;
    }

    public void setCommuntityNameName(String communtityNameName) {
        this.communtityNameName = communtityNameName;
    }

    public void setResourceNum(String resourceNum) {
        this.resourceNum = resourceNum;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public void setShopTypeName(String shopTypeName) {
        this.shopTypeName = shopTypeName;
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

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public void setPropertyCompany(String propertyCompany) {
        this.propertyCompany = propertyCompany;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public void setAround(String around) {
        this.around = around;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLiftType(String liftType) {
        this.liftType = liftType;
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

    public String getTotalRental() {
        return totalRental;
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

    public String getCommuntityName() {
        return communtityName;
    }

    public String getPop() {
        return pop;
    }

    public String getShopTag() {
        return shopTag;
    }

    public String getPaymentTypeName() {
        return paymentTypeName;
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

    public String getRentalWay() {
        return rentalWay;
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

    public String getContactStar() {
        return contactStar;
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

    public String getUpdateBy() {
        return updateBy;
    }

    public String getBrowseQuantity() {
        return browseQuantity;
    }

    public int getLimit() {
        return limit;
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

    public String getRental() {
        return rental;
    }

    public String getOfficeLevel() {
        return officeLevel;
    }

    public String getCategory() {
        return category;
    }

    public String getContactPhoto() {
        return contactPhoto;
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

    public String getLift() {
        return lift;
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

    public String getSellTagName() {
        return sellTagName;
    }

    public String getContactWay() {
        return contactWay;
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

    public String getRentRequire() {
        return rentRequire;
    }

    public String getRentType() {
        return rentType;
    }

    public String getCreateBy() {
        return createBy;
    }

    public String getRentalWayName() {
        return rentalWayName;
    }

    public String getShopTagName() {
        return shopTagName;
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

    public String getUpDown() {
        return upDown;
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

    public String getRentalType() {
        return rentalType;
    }

    public String getCommuntityNameName() {
        return communtityNameName;
    }

    public String getResourceNum() {
        return resourceNum;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public String getShopTypeName() {
        return shopTypeName;
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

    public String getPhoto() {
        return photo;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public String getPropertyCompany() {
        return propertyCompany;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getAround() {
        return around;
    }

    public String getShopType() {
        return shopType;
    }

    public String getAddress() {
        return address;
    }

    public String getLiftType() {
        return liftType;
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

    public String getProvidesName() {
        return providesName;
    }

    public void setProvidesName(String providesName) {
        this.providesName = providesName;
    }

    public String getPropertyRightType() {
        return propertyRightType;
    }

    public void setPropertyRightType(String propertyRightType) {
        this.propertyRightType = propertyRightType;
    }
}
