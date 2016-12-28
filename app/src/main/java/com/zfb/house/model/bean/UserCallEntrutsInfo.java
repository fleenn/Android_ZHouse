package com.zfb.house.model.bean;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by linwenbing on 16/6/22.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserCallEntrutsInfo {


    /**
     * pageNo : 1
     * pageSize : 10
     * count : 0
     * list : [{"qdcs":0,"yyContent":null,"requireType":"1","messageTime":1466493362000,"userId":"6ff927abf8954474b3f0e0cc73672046","messageContent":"就","userPhoto":"http://7xid0d.com1.z0.glb.clouddn.com/pic487768897-793.jpg","brokerage":"30天","brokersList":[{"brokerPhone":"18906033949","brokerPhoto":"18906033949","brokerName":"蓝绍玲","brokerId":"03fd5da2f89f49e090918308bc186352","brokerStar":null}],"alise":"现充","messageId":"2da7c0afd70446a7830e19f767a1bce6"},{"qdcs":0,"yyContent":null,"requireType":"4","messageTime":1466060946000,"userId":"6ff927abf8954474b3f0e0cc73672046","messageContent":"Y","userPhoto":"http://7xid0d.com1.z0.glb.clouddn.com/pic487768897-793.jpg","brokerage":"1.5%","brokersList":[{"brokerPhone":"18906033949","brokerPhoto":"18906033949","brokerName":"蓝绍玲","brokerId":"03fd5da2f89f49e090918308bc186352","brokerStar":null},{"brokerPhone":"13107689332","brokerPhoto":"13107689332","brokerName":"盲","brokerId":"bbdca902dd3f4620a66cfe0176a8b606","brokerStar":null}],"alise":"现充","messageId":"2dcdf875038f40f5b0c12435875abf56"},{"qdcs":0,"yyContent":null,"requireType":"4","messageTime":1464607547000,"userId":"6ff927abf8954474b3f0e0cc73672046","messageContent":"high","userPhoto":"http://7xid0d.com1.z0.glb.clouddn.com/pic487768897-793.jpg","brokerage":"1.5%","brokersList":[{"brokerPhone":"18605922972","brokerPhoto":"18605922972","brokerName":"小戴哈哈","brokerId":"5bd000139fdb46179145e270259ec9ad","brokerStar":"0"},{"brokerPhone":"13255907967","brokerPhoto":"13255907967","brokerName":"测试账号","brokerId":"8aa35246cf8844ee8ec194a42125d729","brokerStar":null}],"alise":"现充","messageId":"33db9e5b3c264c51b6b66a4122e15c77"}]
     */

    private int pageNo;
    private int pageSize;
    private int count;
    /**
     * qdcs : 0
     * yyContent : null
     * requireType : 1
     * messageTime : 1466493362000
     * userId : 6ff927abf8954474b3f0e0cc73672046
     * messageContent : 就
     * userPhoto : http://7xid0d.com1.z0.glb.clouddn.com/pic487768897-793.jpg
     * brokerage : 30天
     * brokersList : [{"brokerPhone":"18906033949","brokerPhoto":"18906033949","brokerName":"蓝绍玲","brokerId":"03fd5da2f89f49e090918308bc186352","brokerStar":null}]
     * alise : 现充
     * messageId : 2da7c0afd70446a7830e19f767a1bce6
     */

    private List<ListBean> list;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private int qdcs;
        private String yyContent;
        private String requireType;
        private long messageTime;
        private String userId;
        private String messageContent;
        private String userPhoto;
        private String brokerage;
        private String alise;
        private String messageId;
        private double messageX;
        private double messageY;
        /**
         * brokerPhone : 18906033949
         * brokerPhoto : 18906033949
         * brokerName : 蓝绍玲
         * brokerId : 03fd5da2f89f49e090918308bc186352
         * brokerStar : null
         */

        private List<EntrustBrokerInfo> brokersList;

        public int getQdcs() {
            return qdcs;
        }

        public double getMessageX() {
            return messageX;
        }

        public void setMessageX(double messageX) {
            this.messageX = messageX;
        }

        public double getMessageY() {
            return messageY;
        }

        public void setMessageY(double messageY) {
            this.messageY = messageY;
        }

        public void setQdcs(int qdcs) {
            this.qdcs = qdcs;
        }

        public String getYyContent() {
            return yyContent;
        }

        public void setYyContent(String yyContent) {
            this.yyContent = yyContent;
        }

        public String getRequireType() {
            return requireType;
        }

        public void setRequireType(String requireType) {
            this.requireType = requireType;
        }

        public long getMessageTime() {
            return messageTime;
        }

        public void setMessageTime(long messageTime) {
            this.messageTime = messageTime;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getMessageContent() {
            return messageContent;
        }

        public void setMessageContent(String messageContent) {
            this.messageContent = messageContent;
        }

        public String getUserPhoto() {
            return userPhoto;
        }

        public void setUserPhoto(String userPhoto) {
            this.userPhoto = userPhoto;
        }

        public String getBrokerage() {
            return brokerage;
        }

        public void setBrokerage(String brokerage) {
            this.brokerage = brokerage;
        }

        public String getAlise() {
            return alise;
        }

        public void setAlise(String alise) {
            this.alise = alise;
        }

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        public List<EntrustBrokerInfo> getBrokersList() {
            return brokersList;
        }

        public void setBrokersList(List<EntrustBrokerInfo> brokersList) {
            this.brokersList = brokersList;
        }


    }
}
