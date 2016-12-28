package com.zfb.house.model.bean;

import java.util.List;

/**
 * Created by Snekey on 2016/10/20.
 */
public class UserRequirement {

    /**
     * id : 7f95115980ed4857b4170638ba6eda10
     * remarks : null
     * userId : b3c0256f2963406fac1bb1f9074771fe
     * areaId : 05920105
     * areaName : 滨北员当片区
     * villageId : 05920105002
     * villageName : 长青小区
     * roomType : 不限
     * area : 200-300m&sup2;
     * cash : 400-450万
     * description : 哦
     * brokerId : null
     * brokerLoginName : null
     * loginName : null
     * userPhoto : null
     * alise : null
     * weiPaiUserList : [{"id":"8aa35246cf8844ee8ec194a42125d729","remarks":null,"companyId":null,"companyName":"公司","areaId":"059201,059201,059201","serviceDistrict":"05920101,05920102,05920103","serviceDistrictName":null,"serviceVillage":"05920101039,05920102032,05920103001","serviceVillageName":null,"sellVolume":0,"rentVolume":0,"realDegree":"0","satisfyDegree":0,"professionalDegree":"0","office":null,"loginName":"13255907967","no":null,"name":"账号","email":null,"phone":"18350211689","mobile":"18350211689","userType":"1","loginIp":null,"loginDate":"2016-10-20 17:22:46","loginFlag":"1","photo":"http://od46im3be.bkt.clouddn.com/pic491456836-888.jpg","oldLoginName":null,"newPassword":null,"oldLoginIp":null,"oldLoginDate":"2016-10-20 17:22:46","sessionId":null,"sex":"1","birthday":null,"star":"0","alise":"账号","pop":0,"officeName":null,"sign":"测试结果就是","smrzState":null,"zzrzState":null,"lat":24.47823,"lng":118.111465,"sid":null,"sortTotal":3,"sortPrise":null,"range":null,"point":405,"recentStatus":"1","expertType":"3","store":"门店","continuitySign":1,"orderRange":null,"inviteCode":"","certifications":[],"role":null,"admin":false,"guanzu":false,"receiveOrgMsg":false,"roleNames":"","receiveBrokerMsg":false}]
     */

    private String id;
    private Object remarks;
    private String userId;
    private String areaId;
    private String areaName;
    private String villageId;
    private String villageName;
    private String roomType;
    private String area;
    private String cash;
    private String description;
    private String applyTime;
    private Object brokerId;
    private Object brokerLoginName;
    private Object loginName;
    private Object userPhoto;
    private Object alise;

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    /**
     * id : 8aa35246cf8844ee8ec194a42125d729
     * remarks : null
     * companyId : null
     * companyName : 公司
     * areaId : 059201,059201,059201
     * serviceDistrict : 05920101,05920102,05920103
     * serviceDistrictName : null
     * serviceVillage : 05920101039,05920102032,05920103001
     * serviceVillageName : null
     * sellVolume : 0
     * rentVolume : 0
     * realDegree : 0
     * satisfyDegree : 0
     * professionalDegree : 0
     * office : null
     * loginName : 13255907967
     * no : null
     * name : 账号
     * email : null
     * phone : 18350211689
     * mobile : 18350211689
     * userType : 1
     * loginIp : null
     * loginDate : 2016-10-20 17:22:46
     * loginFlag : 1
     * photo : http://od46im3be.bkt.clouddn.com/pic491456836-888.jpg
     * oldLoginName : null
     * newPassword : null
     * oldLoginIp : null
     * oldLoginDate : 2016-10-20 17:22:46
     * sessionId : null
     * sex : 1
     * birthday : null
     * star : 0
     * alise : 账号
     * pop : 0
     * officeName : null
     * sign : 测试结果就是
     * smrzState : null
     * zzrzState : null
     * lat : 24.47823
     * lng : 118.111465
     * sid : null
     * sortTotal : 3
     * sortPrise : null
     * range : null
     * point : 405
     * recentStatus : 1
     * expertType : 3
     * store : 门店
     * continuitySign : 1
     * orderRange : null
     * inviteCode :
     * certifications : []
     * role : null
     * admin : false
     * guanzu : false
     * receiveOrgMsg : false
     * roleNames :
     * receiveBrokerMsg : false
     */


    private List<WeiPaiUserListBean> weiPaiUserList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getRemarks() {
        return remarks;
    }

    public void setRemarks(Object remarks) {
        this.remarks = remarks;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getVillageId() {
        return villageId;
    }

    public void setVillageId(String villageId) {
        this.villageId = villageId;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getBrokerId() {
        return brokerId;
    }

    public void setBrokerId(Object brokerId) {
        this.brokerId = brokerId;
    }

    public Object getBrokerLoginName() {
        return brokerLoginName;
    }

    public void setBrokerLoginName(Object brokerLoginName) {
        this.brokerLoginName = brokerLoginName;
    }

    public Object getLoginName() {
        return loginName;
    }

    public void setLoginName(Object loginName) {
        this.loginName = loginName;
    }

    public Object getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(Object userPhoto) {
        this.userPhoto = userPhoto;
    }

    public Object getAlise() {
        return alise;
    }

    public void setAlise(Object alise) {
        this.alise = alise;
    }

    public List<WeiPaiUserListBean> getWeiPaiUserList() {
        return weiPaiUserList;
    }

    public void setWeiPaiUserList(List<WeiPaiUserListBean> weiPaiUserList) {
        this.weiPaiUserList = weiPaiUserList;
    }

    public static class WeiPaiUserListBean {
        private String id;
        private Object remarks;
        private Object companyId;
        private String companyName;
        private String areaId;
        private String serviceDistrict;
        private Object serviceDistrictName;
        private String serviceVillage;
        private Object serviceVillageName;
        private int sellVolume;
        private int rentVolume;
        private String realDegree;
        private int satisfyDegree;
        private String professionalDegree;
        private Object office;
        private String loginName;
        private Object no;
        private String name;
        private Object email;
        private String phone;
        private String mobile;
        private String userType;
        private Object loginIp;
        private String loginDate;
        private String loginFlag;
        private String photo;
        private Object oldLoginName;
        private Object newPassword;
        private Object oldLoginIp;
        private String oldLoginDate;
        private Object sessionId;
        private String sex;
        private Object birthday;
        private String star;
        private String alise;
        private int pop;
        private Object officeName;
        private String sign;
        private Object smrzState;
        private Object zzrzState;
        private double lat;
        private double lng;
        private Object sid;
        private int sortTotal;
        private Object sortPrise;
        private Object range;
        private int point;
        private String recentStatus;
        private String expertType;
        private String store;
        private int continuitySign;
        private Object orderRange;
        private String inviteCode;
        private Object role;
        private boolean admin;
        private boolean guanzu;
        private boolean receiveOrgMsg;
        private String roleNames;
        private boolean receiveBrokerMsg;
        private List<?> certifications;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Object getRemarks() {
            return remarks;
        }

        public void setRemarks(Object remarks) {
            this.remarks = remarks;
        }

        public Object getCompanyId() {
            return companyId;
        }

        public void setCompanyId(Object companyId) {
            this.companyId = companyId;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getAreaId() {
            return areaId;
        }

        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }

        public String getServiceDistrict() {
            return serviceDistrict;
        }

        public void setServiceDistrict(String serviceDistrict) {
            this.serviceDistrict = serviceDistrict;
        }

        public Object getServiceDistrictName() {
            return serviceDistrictName;
        }

        public void setServiceDistrictName(Object serviceDistrictName) {
            this.serviceDistrictName = serviceDistrictName;
        }

        public String getServiceVillage() {
            return serviceVillage;
        }

        public void setServiceVillage(String serviceVillage) {
            this.serviceVillage = serviceVillage;
        }

        public Object getServiceVillageName() {
            return serviceVillageName;
        }

        public void setServiceVillageName(Object serviceVillageName) {
            this.serviceVillageName = serviceVillageName;
        }

        public int getSellVolume() {
            return sellVolume;
        }

        public void setSellVolume(int sellVolume) {
            this.sellVolume = sellVolume;
        }

        public int getRentVolume() {
            return rentVolume;
        }

        public void setRentVolume(int rentVolume) {
            this.rentVolume = rentVolume;
        }

        public String getRealDegree() {
            return realDegree;
        }

        public void setRealDegree(String realDegree) {
            this.realDegree = realDegree;
        }

        public int getSatisfyDegree() {
            return satisfyDegree;
        }

        public void setSatisfyDegree(int satisfyDegree) {
            this.satisfyDegree = satisfyDegree;
        }

        public String getProfessionalDegree() {
            return professionalDegree;
        }

        public void setProfessionalDegree(String professionalDegree) {
            this.professionalDegree = professionalDegree;
        }

        public Object getOffice() {
            return office;
        }

        public void setOffice(Object office) {
            this.office = office;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public Object getNo() {
            return no;
        }

        public void setNo(Object no) {
            this.no = no;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getEmail() {
            return email;
        }

        public void setEmail(Object email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public Object getLoginIp() {
            return loginIp;
        }

        public void setLoginIp(Object loginIp) {
            this.loginIp = loginIp;
        }

        public String getLoginDate() {
            return loginDate;
        }

        public void setLoginDate(String loginDate) {
            this.loginDate = loginDate;
        }

        public String getLoginFlag() {
            return loginFlag;
        }

        public void setLoginFlag(String loginFlag) {
            this.loginFlag = loginFlag;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public Object getOldLoginName() {
            return oldLoginName;
        }

        public void setOldLoginName(Object oldLoginName) {
            this.oldLoginName = oldLoginName;
        }

        public Object getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(Object newPassword) {
            this.newPassword = newPassword;
        }

        public Object getOldLoginIp() {
            return oldLoginIp;
        }

        public void setOldLoginIp(Object oldLoginIp) {
            this.oldLoginIp = oldLoginIp;
        }

        public String getOldLoginDate() {
            return oldLoginDate;
        }

        public void setOldLoginDate(String oldLoginDate) {
            this.oldLoginDate = oldLoginDate;
        }

        public Object getSessionId() {
            return sessionId;
        }

        public void setSessionId(Object sessionId) {
            this.sessionId = sessionId;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public Object getBirthday() {
            return birthday;
        }

        public void setBirthday(Object birthday) {
            this.birthday = birthday;
        }

        public String getStar() {
            return star;
        }

        public void setStar(String star) {
            this.star = star;
        }

        public String getAlise() {
            return alise;
        }

        public void setAlise(String alise) {
            this.alise = alise;
        }

        public int getPop() {
            return pop;
        }

        public void setPop(int pop) {
            this.pop = pop;
        }

        public Object getOfficeName() {
            return officeName;
        }

        public void setOfficeName(Object officeName) {
            this.officeName = officeName;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public Object getSmrzState() {
            return smrzState;
        }

        public void setSmrzState(Object smrzState) {
            this.smrzState = smrzState;
        }

        public Object getZzrzState() {
            return zzrzState;
        }

        public void setZzrzState(Object zzrzState) {
            this.zzrzState = zzrzState;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public Object getSid() {
            return sid;
        }

        public void setSid(Object sid) {
            this.sid = sid;
        }

        public int getSortTotal() {
            return sortTotal;
        }

        public void setSortTotal(int sortTotal) {
            this.sortTotal = sortTotal;
        }

        public Object getSortPrise() {
            return sortPrise;
        }

        public void setSortPrise(Object sortPrise) {
            this.sortPrise = sortPrise;
        }

        public Object getRange() {
            return range;
        }

        public void setRange(Object range) {
            this.range = range;
        }

        public int getPoint() {
            return point;
        }

        public void setPoint(int point) {
            this.point = point;
        }

        public String getRecentStatus() {
            return recentStatus;
        }

        public void setRecentStatus(String recentStatus) {
            this.recentStatus = recentStatus;
        }

        public String getExpertType() {
            return expertType;
        }

        public void setExpertType(String expertType) {
            this.expertType = expertType;
        }

        public String getStore() {
            return store;
        }

        public void setStore(String store) {
            this.store = store;
        }

        public int getContinuitySign() {
            return continuitySign;
        }

        public void setContinuitySign(int continuitySign) {
            this.continuitySign = continuitySign;
        }

        public Object getOrderRange() {
            return orderRange;
        }

        public void setOrderRange(Object orderRange) {
            this.orderRange = orderRange;
        }

        public String getInviteCode() {
            return inviteCode;
        }

        public void setInviteCode(String inviteCode) {
            this.inviteCode = inviteCode;
        }

        public Object getRole() {
            return role;
        }

        public void setRole(Object role) {
            this.role = role;
        }

        public boolean isAdmin() {
            return admin;
        }

        public void setAdmin(boolean admin) {
            this.admin = admin;
        }

        public boolean isGuanzu() {
            return guanzu;
        }

        public void setGuanzu(boolean guanzu) {
            this.guanzu = guanzu;
        }

        public boolean isReceiveOrgMsg() {
            return receiveOrgMsg;
        }

        public void setReceiveOrgMsg(boolean receiveOrgMsg) {
            this.receiveOrgMsg = receiveOrgMsg;
        }

        public String getRoleNames() {
            return roleNames;
        }

        public void setRoleNames(String roleNames) {
            this.roleNames = roleNames;
        }

        public boolean isReceiveBrokerMsg() {
            return receiveBrokerMsg;
        }

        public void setReceiveBrokerMsg(boolean receiveBrokerMsg) {
            this.receiveBrokerMsg = receiveBrokerMsg;
        }

        public List<?> getCertifications() {
            return certifications;
        }

        public void setCertifications(List<?> certifications) {
            this.certifications = certifications;
        }
    }
}
