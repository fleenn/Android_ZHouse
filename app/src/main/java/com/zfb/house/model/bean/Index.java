package com.zfb.house.model.bean;

import java.util.List;

/**
 * Created by Snekey on 2016/6/7.
 */
public class Index {

    /**
     * id : 5bd000139fdb46179145e270259ec9ad
     * name : 小戴
     * phone : 18605922972
     * mobile : 18605922972
     * userType : 1
     * lat : 25.11973380423066
     * lng : 118.9037234253212
     * preLat : 25.11973189695904
     * preLng : 118.9037234256893
     * photo : http://7xid0d.com1.z0.glb.clouddn.com/pic482201591-823.jpg
     * range : 7316191
     */

    private List<Broker> nearByBroker;
    /**
     * id : fbef0e94b6324384ac4d608551965b39
     * pageM : null
     * isNewRecord : false
     * remarks : null
     * x : 118.111544
     * y : 24.476618
     * location : 厦门市思明区后埭溪路22号禾祥美食中心内(近东方巴黎)味华香牛肉养生馆(后埭溪路店)
     * content : 测试地址
     * eliteTime : 2016-05-12 11:11:15
     * userName : ??
     * userId : bbb7cfe24e4b4bab845a4d1f57a77618
     * userStar : null
     * userPhoto : null
     * range : null
     * photo : null
     * type : 0
     * replyCount : 1
     * praiseCount : 0
     * shareCount : 0
     * collectionCount : 0
     * houseEliteReply : {"pageNo":1,"pageSize":10,"count":1,"list":[{"id":"6a5c195e0168425cabcbec70a83afb82","pageM":null,"isNewRecord":false,"remarks":null,"replyContent":"呃","replyTime":"2016-05-17 18:27:10","userName":"13800138014","userId":"13800138014","photo":"http://7xid0d.com1.z0.glb.clouddn.com/pic474946018-240.jpg","eliteId":"fbef0e94b6324384ac4d608551965b39","replyUserId":"","replyUserName":null,"certifications":[]}],"firstResult":0,"maxResults":10}
     * houseElitePraise : []
     * certifications : []
     * collect : false
     * praise : false
     * sc : false
     */

    private List<MomentsContent> pickupElite;
    /**
     * id : 1
     * title : 广告一
     * image : http://7xid0d.com1.z0.glb.clouddn.com/1462499808138file1?e=1462503408&amp;amp;token=4P_bABvR-QHFAEygEm3h72k8FztsB_Jri68dkVCI:YemzPaah1Da6Ev2Erk7aj2om38Y=
     * url : 123
     */

    private List<Banner> banner;

    public List<Broker> getNearByBroker() {
        return nearByBroker;
    }

    public void setNearByBroker(List<Broker> nearByBroker) {
        this.nearByBroker = nearByBroker;
    }

    public List<MomentsContent> getPickupElite() {
        return pickupElite;
    }

    public void setPickupElite(List<MomentsContent> pickupElite) {
        this.pickupElite = pickupElite;
    }

    public List<Banner> getBanner() {
        return banner;
    }

    public void setBanner(List<Banner> banner) {
        this.banner = banner;
    }

}
