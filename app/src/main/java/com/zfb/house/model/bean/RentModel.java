package com.zfb.house.model.bean;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

/**
 * 项目名称:  [zfbandroid]
 * 包:        [com.zfb.house.model.bean]
 * 类描述:    [类描述]
 * 创建人:    [XiaoFeng]
 * 创建时间:  [2016/6/4 17:31]
 * 修改人:    [XiaoFeng]
 * 修改时间:  [2016/6/4 17:31]
 * 修改备注:  [说明本次修改内容]
 * 版本:      [v1.0]
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RentModel {


    /**
     * pageNo : 1
     * pageSize : 111
     * count : 5
     * list : [{"totalRental":"560000.0","officeId":"059201","remark":"测试测试","serverAreaId":"05920101","communtityName":"大洲城市花园","pop":"5","shopTag":"1,10,6,2","paymentTypeName":"押一付三","contactSex":"1","userType":"0","contacts":"余","rentalWay":"1","dbName":"mysql","shopCategoryName":"商铺转让","acceptDate":"","userId":"","age":"","isNewRecord":false,"userName":"","decorationLevel":"2","updateBy":"","browseQuantity":"0","limit":0,"propertyRightTypeName":"","regionName":"思明区","shopState":"2","display":"0","property":"false","rental":"3500","officeLevel":"0","category":"0","resourceTypeName":"商铺","traffic":"","decorationLevelName":"中等装修","floor":"3/6","officeType":"0","collectionNum":"0","cutEnabled":"true","serverAreaName":"火车站片区","flatShareType":"0","release":"false","serviceDistrict":"","sellTagName":"","contactWay":"18046053861","direction":"","shopStateName":"闲置中","userPhoto":"","excellent":"","rentRequire":"0","createBy":"","rentType":"0","rentalWayName":"押一付三","shopTagName":"百货超市,服饰鞋包,餐饮美食,酒店宾馆,","id":"ea67a73e0ca94ba3ba2d478c93254b3b","title":"商铺出租","syncState":"0","area":"160.0","propertyCost":"0","officeName":"思明区","synchronizedId":"ed0ca47729f145fea22aac3aa7b67361","layout":"","serviceVillage":"05920101005","createDate":"","areaEnabled":"0.0","rentalType":"0","communtityNameName":"大洲城市花园","resourceNum":"RS05920101005000003","paymentType":"1","shopTypeName":"临街门面","releaseFlag":"0","provides":"","shopCategory":"2","photo":"http://7xid0d.com1.z0.glb.clouddn.com/pic477810087-876.jpg,http://7xid0d.com1.z0.glb.clouddn.com/pic477810231-368.jpg,http://7xid0d.com1.z0.glb.clouddn.com/pic477810231-464.jpg,http://7xid0d.com1.z0.glb.clouddn.com/pic477810253-382.jpg","updateDate":"","propertyCompany":"","resourceType":"4","around":"","shopType":"3","address":"","y":"24.487197","brokers":"","x":"118.114377"},{"totalRental":"811200.0","officeId":"059201","remark":"","serverAreaId":"05920102","communtityName":"Bingo城际商务中心","pop":"4","shopTag":"","paymentTypeName":"","contactSex":"2","userType":"0","contacts":"林","rentalWay":"1","dbName":"mysql","shopCategoryName":"","acceptDate":"","userId":"","age":"","isNewRecord":false,"userName":"","decorationLevel":"0","updateBy":"","browseQuantity":"0","limit":0,"propertyRightTypeName":"","regionName":"思明区","shopState":"0","display":"0","property":"true","rental":"5200","officeLevel":"2","category":"0","resourceTypeName":"写字楼","traffic":"","decorationLevelName":"","officeTypeName":"酒店写字楼","floor":"3/6","officeType":"4","collectionNum":"0","cutEnabled":"true","serverAreaName":"白鹭洲片区","flatShareType":"0","release":"false","serviceDistrict":"","sellTagName":"","contactWay":"15396256219","direction":"","shopStateName":"","userPhoto":"","officeLevelName":"乙级","excellent":"","rentRequire":"0","createBy":"","rentType":"0","rentalWayName":"押一付三","id":"8c6de96b353145e1b4f3f2657292c4b2","title":"写字楼出租测试房源","syncState":"0","area":"156.0","propertyCost":"600","officeName":"思明区","synchronizedId":"94e26cae665847d1b2284865999163af","layout":"","serviceVillage":"05920102001","createDate":"","areaEnabled":"0.0","rentalType":"0","communtityNameName":"Bingo城际商务中心","resourceNum":"RX05920102001000003","paymentType":"0","releaseFlag":"0","provides":"","shopCategory":"0","photo":"http://7xid0d.com1.z0.glb.clouddn.com/pic478581104-136.jpg","updateDate":"","propertyCompany":"","resourceType":"3","around":"","shopType":"0","address":"","y":"24.484865","brokers":"","x":"118.092528"},{"totalRental":"172500.0","providesName":"","officeId":"059202","remark":"","serverAreaId":"05920202","communtityName":"绿苑商城","pop":"3","shopTag":"","paymentTypeName":"押一付三","contactSex":"1","userType":"0","contacts":"庄","rentTypeName":"整租","rentalWay":"1","dbName":"mysql","shopCategoryName":"","acceptDate":"","userId":"","age":"2007","isNewRecord":false,"userName":"","decorationLevel":"2","directionName":"南","updateBy":"","browseQuantity":"0","limit":0,"propertyRightTypeName":"","regionName":"海沧区","shopState":"0","display":"0","property":"false","rental":"2300","officeLevel":"0","category":"0","resourceTypeName":"住宅","traffic":"","decorationLevelName":"中等装修","floor":"2/24","officeType":"0","collectionNum":"0","cutEnabled":"false","serverAreaName":"沧林路","flatShareType":"0","release":"false","serviceDistrict":"","sellTagName":"","contactWay":"18605922972","direction":"2","shopStateName":"","userPhoto":"","excellent":"","rentRequire":"0","createBy":"","rentType":"1","rentalWayName":"押一付三","id":"ec7ae844132849b78564ce4001200467","title":"绿苑商城两室一厅","syncState":"0","area":"75.0","propertyCost":"0","officeName":"海沧区","synchronizedId":"d5588c2108084786a7dc72cb7fee4360","layout":"2,1,1,,","serviceVillage":"05920202019","createDate":"","areaEnabled":"0.0","rentalType":"0","communtityNameName":"绿苑商城","resourceNum":"RZ05920202019000002","paymentType":"1","releaseFlag":"0","provides":"10,12,14,16,18","shopCategory":"0","photo":"http://7xid0d.com1.z0.glb.clouddn.com/pic470904674-153.jpg,http://7xid0d.com1.z0.glb.clouddn.com/pic470904674-681.jpg,http://7xid0d.com1.z0.glb.clouddn.com/pic470904674-613.jpg","updateDate":"","propertyCompany":"","resourceType":"1","around":"","shopType":"0","address":"海沧大桥附近","y":"24.495663510792","brokers":"","x":"118.05377953859"},{"totalRental":"4000000.0","providesName":"客梯,停车位,空调,网络,","officeId":"059204","remark":"此处是房源描述","serverAreaId":"05920401","communtityName":"保利.海上五月花","pop":"2","shopTag":"1,10,6,5","paymentTypeName":"押一付三","contactSex":"2","userType":"0","contacts":"啦啦啦","rentalWay":"1","dbName":"mysql","shopCategoryName":"商铺转让","acceptDate":"","userId":"","age":"2006","isNewRecord":false,"userName":"","decorationLevel":"4","updateBy":"","browseQuantity":"0","limit":0,"propertyRightTypeName":"","regionName":"集美区","shopState":"1","display":"0","property":"false","rental":"20000","officeLevel":"0","category":"0","resourceTypeName":"商铺","traffic":"","decorationLevelName":"毛坯","floor":"3/5","officeType":"0","collectionNum":"0","cutEnabled":"true","serverAreaName":"集美文教区","flatShareType":"0","release":"false","serviceDistrict":"","sellTagName":"","contactWay":"18150088010","direction":"","shopStateName":"营业中","userPhoto":"","excellent":"","rentRequire":"0","createBy":"","rentType":"0","rentalWayName":"押一付三","shopTagName":"百货超市,餐饮美食,酒店宾馆,美容美发,","id":"f0434d00981846378bcd49fc87a899dd","title":"测试房源详情 商铺出租","syncState":"0","area":"200.0","propertyCost":"0","officeName":"集美区","synchronizedId":"","layout":"","serviceVillage":"05920401002","createDate":"","areaEnabled":"0.0","rentalType":"0","communtityNameName":"保利.海上五月花","resourceNum":"RS05920401002000001","paymentType":"1","shopTypeName":"住宅底商","releaseFlag":"0","provides":"5,6,1,3","shopCategory":"2","photo":"http://7xid0d.com1.z0.glb.clouddn.com/pic474170092-969.jpg","updateDate":"","propertyCompany":"","resourceType":"4","around":"","shopType":"1","address":"此处是地址","y":"24.606611","brokers":"","x":"118.132929"},{"totalRental":"600000.0","providesName":"","officeId":"059201","remark":"","serverAreaId":"05920102","communtityName":"Bingo城际商务中心","pop":"1","shopTag":"","paymentTypeName":"年付不押","contactSex":"2","userType":"0","contacts":"天空之城","rentTypeName":"整租","rentalWay":"1","dbName":"mysql","shopCategoryName":"","acceptDate":"","userId":"","age":"2015","isNewRecord":false,"userName":"","decorationLevel":"0","updateBy":"","browseQuantity":"0","limit":0,"propertyRightTypeName":"","regionName":"思明区","shopState":"0","display":"0","property":"false","rental":"5000","officeLevel":"0","category":"0","resourceTypeName":"别墅","traffic":"","decorationLevelName":"","floor":"/6","officeType":"0","collectionNum":"0","cutEnabled":"false","serverAreaName":"白鹭洲片区","flatShareType":"0","release":"false","serviceDistrict":"","sellTagName":"","contactWay":"18150088010","direction":"","shopStateName":"","userPhoto":"","excellent":"","rentRequire":"0","createBy":"","rentType":"1","rentalWayName":"押一付三","id":"cc1e9cf16ac1447e9e956b3cdcf8c7a1","title":"哥哥哥哥哥哥哥哥哥哥哥哥姑姑高清","syncState":"0","area":"120.0","propertyCost":"0","officeName":"思明区","synchronizedId":"","layout":"3,2,3,,","serviceVillage":"05920102001","createDate":"","areaEnabled":"0.0","rentalType":"0","communtityNameName":"Bingo城际商务中心","resourceNum":"RB05920102001000001","paymentType":"6","releaseFlag":"0","provides":"10,18,17,16,12,13,15","shopCategory":"0","photo":"http://7xid0d.com1.z0.glb.clouddn.com/pic478667449-104.jpg,http://7xid0d.com1.z0.glb.clouddn.com/pic478667449-986.jpg,http://7xid0d.com1.z0.glb.clouddn.com/pic478667449-134.jpg,http://7xid0d.com1.z0.glb.clouddn.com/pic478667449-548.jpg,http://7xid0d.com1.z0.glb.clouddn.com/pic478667449-517.jpg","updateDate":"","propertyCompany":"","resourceType":"2","around":"","shopType":"0","address":"软件园","y":"24.484865","brokers":"","x":"118.092528"}]
     * firstResult : 0
     * maxResults : 111
     */

    private int pageNo;
    private int pageSize;
    private int count;
    private int firstResult;
    private int maxResults;
    /**
     * totalRental : 560000.0
     * officeId : 059201
     * remark : 测试测试
     * serverAreaId : 05920101
     * communtityName : 大洲城市花园
     * pop : 5
     * shopTag : 1,10,6,2
     * paymentTypeName : 押一付三
     * contactSex : 1
     * userType : 0
     * contacts : 余
     * rentalWay : 1
     * dbName : mysql
     * shopCategoryName : 商铺转让
     * acceptDate :
     * userId :
     * age :
     * isNewRecord : false
     * userName :
     * decorationLevel : 2
     * updateBy :
     * browseQuantity : 0
     * limit : 0
     * propertyRightTypeName :
     * regionName : 思明区
     * shopState : 2
     * display : 0
     * property : false
     * rental : 3500
     * officeLevel : 0
     * category : 0
     * resourceTypeName : 商铺
     * traffic :
     * decorationLevelName : 中等装修
     * floor : 3/6
     * officeType : 0
     * collectionNum : 0
     * cutEnabled : true
     * serverAreaName : 火车站片区
     * flatShareType : 0
     * release : false
     * serviceDistrict :
     * sellTagName :
     * contactWay : 18046053861
     * direction :
     * shopStateName : 闲置中
     * userPhoto :
     * excellent :
     * rentRequire : 0
     * createBy :
     * rentType : 0
     * rentalWayName : 押一付三
     * shopTagName : 百货超市,服饰鞋包,餐饮美食,酒店宾馆,
     * id : ea67a73e0ca94ba3ba2d478c93254b3b
     * title : 商铺出租
     * syncState : 0
     * area : 160.0
     * propertyCost : 0
     * officeName : 思明区
     * synchronizedId : ed0ca47729f145fea22aac3aa7b67361
     * layout :
     * serviceVillage : 05920101005
     * createDate :
     * areaEnabled : 0.0
     * rentalType : 0
     * communtityNameName : 大洲城市花园
     * resourceNum : RS05920101005000003
     * paymentType : 1
     * shopTypeName : 临街门面
     * releaseFlag : 0
     * provides :
     * shopCategory : 2
     * photo : http://7xid0d.com1.z0.glb.clouddn.com/pic477810087-876.jpg,http://7xid0d.com1.z0.glb.clouddn.com/pic477810231-368.jpg,http://7xid0d.com1.z0.glb.clouddn.com/pic477810231-464.jpg,http://7xid0d.com1.z0.glb.clouddn.com/pic477810253-382.jpg
     * updateDate :
     * propertyCompany :
     * resourceType : 4
     * around :
     * shopType : 3
     * address :
     * y : 24.487197
     * brokers :
     * x : 118.114377
     */

    private List<RentItem> list;

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setFirstResult(int firstResult) {
        this.firstResult = firstResult;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public void setList(List<RentItem> list) {
        this.list = list;
    }

    public int getPageNo() {
        return pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getCount() {
        return count;
    }

    public int getFirstResult() {
        return firstResult;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public List<RentItem> getList() {
        return list;
    }

}
