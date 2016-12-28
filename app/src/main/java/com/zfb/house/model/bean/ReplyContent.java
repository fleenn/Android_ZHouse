package com.zfb.house.model.bean;

import java.util.List;

/**
 * Created by Snekey on 2016/5/8.
 */
public class ReplyContent {
    private String id;
    private String pageM;
    private boolean isNewRecord;
    private String remarks;
    private String replyContent;
    private String replyTime;
    private String userName;
    private String userId;
    private String photo;
    private String eliteId;
    private String replyUserId;
    private String replyUserName;
    private List certifications;

    @Override
    public String toString() {
        return "ReplyContent{" +
                "id='" + id + '\'' +
                ", pageM='" + pageM + '\'' +
                ", isNewRecord=" + isNewRecord +
                ", remarks='" + remarks + '\'' +
                ", replyContent='" + replyContent + '\'' +
                ", replyTime='" + replyTime + '\'' +
                ", userName='" + userName + '\'' +
                ", userId='" + userId + '\'' +
                ", photo='" + photo + '\'' +
                ", eliteId='" + eliteId + '\'' +
                ", replyUserId='" + replyUserId + '\'' +
                ", replyUserName='" + replyUserName + '\'' +
                ", certifications=" + certifications +
                '}';
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

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEliteId() {
        return eliteId;
    }

    public void setEliteId(String eliteId) {
        this.eliteId = eliteId;
    }

    public String getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(String replyUserId) {
        this.replyUserId = replyUserId;
    }

    public String getReplyUserName() {
        return replyUserName;
    }

    public void setReplyUserName(String replyUserName) {
        this.replyUserName = replyUserName;
    }

    public List getCertifications() {
        return certifications;
    }

    public void setCertifications(List certifications) {
        this.certifications = certifications;
    }
}
