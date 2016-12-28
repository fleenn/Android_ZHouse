package com.zfb.house.model.bean;

import java.util.List;

/**
 * Created by Snekey on 2016/5/8.
 */
public class MomentsContent {
    private String id;
    private String pageM;
    private boolean isNewRecord;
    private String remarks;
    private String remark;
    private String x;
    private String y;
    private String location;
    private String content;
    private String eliteTime;
    private String userName;
    private String userId;
    private String userStar;
    private String userPhoto;
    private String range;
    private String photo;
    private String type;
    private int replyCount;
    private int praiseCount;
    private int shareCount;
    private int collectionCount;
    private MomentsReply houseEliteReply;
    private List houseElitePraise;
    private List certifications;
    private boolean collect;
    private boolean praise;
    private boolean sc;
    private boolean more;
    private String startEliteTime;
    private List<String> userIdList;

    public List<String> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<String> userIdList) {
        this.userIdList = userIdList;
    }

    public void setNewRecord(boolean newRecord) {
        isNewRecord = newRecord;
    }

    public String getStartEliteTime() {
        return startEliteTime;
    }

    public void setStartEliteTime(String startEliteTime) {
        this.startEliteTime = startEliteTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isMore() {
        return more;
    }

    public void setMore(boolean more) {
        this.more = more;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPageM() {
        return pageM;
    }

    public void setPageM(String pageM) {
        this.pageM = pageM;
    }

    public boolean isNewRecord() {
        return isNewRecord;
    }

    public void setIsNewRecord(boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEliteTime() {
        return eliteTime;
    }

    public void setEliteTime(String eliteTime) {
        this.eliteTime = eliteTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserStar() {
        return userStar;
    }

    public void setUserStar(String userStar) {
        this.userStar = userStar;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public int getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public int getCollectionCount() {
        return collectionCount;
    }

    public void setCollectionCount(int collectionCount) {
        this.collectionCount = collectionCount;
    }

    public MomentsReply getHouseEliteReply() {
        return houseEliteReply;
    }

    public void setHouseEliteReply(MomentsReply houseEliteReply) {
        this.houseEliteReply = houseEliteReply;
    }

    public List getHouseElitePraise() {
        return houseElitePraise;
    }

    public void setHouseElitePraise(List houseElitePraise) {
        this.houseElitePraise = houseElitePraise;
    }

    public List getCertifications() {
        return certifications;
    }

    public void setCertifications(List certifications) {
        this.certifications = certifications;
    }

    public boolean isCollect() {
        return collect;
    }

    public void setCollect(boolean collect) {
        this.collect = collect;
    }

    public boolean isPraise() {
        return praise;
    }

    public void setPraise(boolean praise) {
        this.praise = praise;
    }

    public boolean isSc() {
        return sc;
    }

    public void setSc(boolean sc) {
        this.sc = sc;
    }
}
