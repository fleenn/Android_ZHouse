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
public class SellModel {

    /**
     * pageNo : 1
     * pageSize : 111
     * count : 5
     * list : [{"officeId":"059206","remark":"此处是房源描述","serverAreaId":"05920604","validityPeriod":"","communtityName":"汇景新城中心","unitPrice":"0","pop":"5","shopTag":"","contactSex":"2","userType":"0","contacts":"啦啦啦","dbName":"mysql","shopCategoryName":"","acceptDate":"","userId":"","age":"2013","isNewRecord":false,"userName":"","decorationLevel":"1","releaseType":"0","directionName":"北","updateBy":"","browseQuantity":"0","limit":0,"transfer":"false","validateCode":"","propertyRightTypeName":"国有产权","regionName":"翔安区","shopState":"0","display":"0","property":"false","officeLevel":"0","category":"0","resourceTypeName":"住宅","traffic":"","decorationLevelName":"豪华装修","floor":"10/20","officeType":"0","collectionNum":"0","cutEnabled":"false","serverAreaName":"新店","flatShareType":"","release":"false","serviceDistrict":"","contactWay":"18150088010","sellTagName":"可议价","direction":"6","shopStateName":"","userPhoto":"","excellent":"","propertyRightType":"1","createBy":"","id":"4f593077b5994a198be53eb68b54ad1b","title":"测试房源详情普通住宅出售","syncState":"0","area":"100.0","propertyCost":"0","officeName":"翔安区","synchronizedId":"","layout":"2,1,2,,","wishPrice":"200","serviceVillage":"05920604002","createDate":"","areaEnabled":"0.0","totalPrice":"0","communtityNameName":"汇景新城中心","resourceNum":"SZ05920604002000001","releaseFlag":"0","provides":"","shopCategory":"0","sellTag":"2","photo":"http://7xid0d.com1.z0.glb.clouddn.com/pic474170796-966.jpg","updateDate":"","propertyCompany":"","around":"","resourceType":"1","shopType":"0","address":"此处是地址","y":"24.625431","brokers":"","x":"118.249528"},{"officeId":"059203","remark":"","serverAreaId":"05920102","validityPeriod":"","communtityName":"东卉花园","unitPrice":"0","pop":"4","shopTag":"","contactSex":"1","userType":"0","contacts":"余","dbName":"mysql","shopCategoryName":"","acceptDate":"","userId":"","age":"","isNewRecord":false,"userName":"","decorationLevel":"0","releaseType":"0","updateBy":"","browseQuantity":"0","limit":0,"transfer":"false","validateCode":"","propertyRightTypeName":"国有产权","regionName":"湖里区","shopState":"0","display":"0","property":"false","officeLevel":"0","category":"0","resourceTypeName":"商铺","traffic":"","decorationLevelName":"","floor":"3/6","officeType":"0","collectionNum":"0","cutEnabled":"true","serverAreaName":"白鹭洲片区","flatShareType":"","release":"false","serviceDistrict":"","contactWay":"18046053861","sellTagName":"","direction":"","shopStateName":"","userPhoto":"","excellent":"","propertyRightType":"1","createBy":"","id":"33891b47e1db41bc9bd3bf83f9f07ccb","title":"商铺出售测试","syncState":"0","area":"35.0","propertyCost":"0","officeName":"湖里区","synchronizedId":"72c057807f0d4fa4861ef88593389823","layout":"","wishPrice":"250","serviceVillage":"05920102004","createDate":"","areaEnabled":"0.0","totalPrice":"0","communtityNameName":"东卉花园","resourceNum":"SS05920102004000002","shopTypeName":"住宅底商","releaseFlag":"0","provides":"","shopCategory":"0","sellTag":"","photo":"http://7xid0d.com1.z0.glb.clouddn.com/pic477810988-117.jpg,http://7xid0d.com1.z0.glb.clouddn.com/pic477810988-345.jpg,http://7xid0d.com1.z0.glb.clouddn.com/pic477810988-324.jpg,http://7xid0d.com1.z0.glb.clouddn.com/pic477489865-996.jpg","updateDate":"","propertyCompany":"","around":"","resourceType":"4","shopType":"1","address":"","y":"24.480978","brokers":"","x":"118.092566"},{"officeId":"059203","remark":"此处是房源描述","serverAreaId":"05920503","validityPeriod":"","communtityName":"巴厘香泉","unitPrice":"0","pop":"3","shopTag":"","contactSex":"2","userType":"0","contacts":"天空之城","dbName":"mysql","shopCategoryName":"","acceptDate":"","userId":"","age":"2000","isNewRecord":false,"userName":"","decorationLevel":"1","releaseType":"0","directionName":"西北","updateBy":"","browseQuantity":"0","limit":0,"transfer":"false","validateCode":"","propertyRightTypeName":"国有产权","regionName":"湖里区","shopState":"0","display":"0","property":"false","officeLevel":"0","category":"0","resourceTypeName":"别墅","traffic":"","decorationLevelName":"豪华装修","floor":"/2","officeType":"0","collectionNum":"0","cutEnabled":"false","serverAreaName":"城南","flatShareType":"","release":"false","serviceDistrict":"","contactWay":"18150088010","sellTagName":"急售","direction":"10","shopStateName":"","userPhoto":"","excellent":"","propertyRightType":"1","createBy":"","id":"6a9c0bf2e5ff475599cdf885ed2efcfd","title":"测试房源详情 别墅出售","syncState":"0","area":"200.0","propertyCost":"0","officeName":"湖里区","synchronizedId":"","layout":"3,2,3,,","wishPrice":"299","serviceVillage":"05920503001","createDate":"","areaEnabled":"0.0","totalPrice":"0","communtityNameName":"巴厘香泉","resourceNum":"","releaseFlag":"0","provides":"","shopCategory":"0","sellTag":"3","photo":"http://7xid0d.com1.z0.glb.clouddn.com/pic478577476-447.jpg,http://7xid0d.com1.z0.glb.clouddn.com/pic474170908-882.jpg","updateDate":"","propertyCompany":"","around":"","resourceType":"2","shopType":"0","address":"此处是地址","y":"24.767525","brokers":"","x":"118.143593"},{"providesName":"货梯,暖气,","officeId":"059202","remark":"此处是房源描述","serverAreaId":"05920202","validityPeriod":"","communtityName":"沧二小区","unitPrice":"0","pop":"2","shopTag":"6,9","contactSex":"2","userType":"0","contacts":"啦啦啦","dbName":"mysql","shopCategoryName":"","acceptDate":"","userId":"","age":"2004","isNewRecord":false,"userName":"","decorationLevel":"1","releaseType":"0","updateBy":"","browseQuantity":"0","limit":0,"transfer":"false","validateCode":"","propertyRightTypeName":"国有产权","regionName":"海沧区","shopState":"0","display":"0","property":"false","officeLevel":"0","category":"0","resourceTypeName":"商铺","traffic":"","decorationLevelName":"豪华装修","floor":"3/5","officeType":"0","collectionNum":"0","cutEnabled":"true","serverAreaName":"沧林路","flatShareType":"","release":"false","serviceDistrict":"","contactWay":"18150088010","sellTagName":"","direction":"","shopStateName":"","userPhoto":"","excellent":"","propertyRightType":"1","createBy":"","shopTagName":"百货超市,其他,","id":"deb0ff515c59464d92ed645324c5b554","title":"测试房源详情 商铺出售","syncState":"0","area":"200.0","propertyCost":"0","officeName":"海沧区","synchronizedId":"","layout":"","wishPrice":"200","serviceVillage":"05920202003","createDate":"","areaEnabled":"0.0","totalPrice":"0","communtityNameName":"沧二小区","resourceNum":"SS05920202003000001","shopTypeName":"住宅底商","releaseFlag":"0","provides":"4,2","shopCategory":"0","sellTag":"","photo":"http://7xid0d.com1.z0.glb.clouddn.com/pic474171185-394.jpg","updateDate":"","propertyCompany":"","around":"","resourceType":"4","shopType":"1","address":"此处是地址","y":"24.50143","brokers":"","x":"118.042577"},{"officeId":"059201","remark":"此处是房源描述","serverAreaId":"05920402","validityPeriod":"","communtityName":"IOI园博湾","unitPrice":"0","pop":"1","shopTag":"","contactSex":"2","userType":"0","contacts":"天空之城","dbName":"mysql","shopCategoryName":"","acceptDate":"","userId":"","age":"2006","isNewRecord":false,"userName":"","decorationLevel":"3","releaseType":"0","updateBy":"","browseQuantity":"0","limit":0,"transfer":"false","validateCode":"","propertyRightTypeName":"集体产权","regionName":"思明区","shopState":"0","display":"0","property":"true","officeLevel":"1","category":"0","resourceTypeName":"写字楼","traffic":"","decorationLevelName":"简易装修","officeTypeName":"酒店写字楼","floor":"6/10","officeType":"4","collectionNum":"0","cutEnabled":"true","serverAreaName":"杏林区","flatShareType":"","release":"false","serviceDistrict":"","contactWay":"18150088010","sellTagName":"","direction":"","shopStateName":"","userPhoto":"","officeLevelName":"甲级","excellent":"","propertyRightType":"3","createBy":"","id":"f24db7efe84e4e94aecafcbb07025886","title":"测试房源详情 写字楼出售","syncState":"0","area":"200.0","propertyCost":"2000","officeName":"思明区","synchronizedId":"","layout":"","wishPrice":"203","serviceVillage":"05920402001","createDate":"","areaEnabled":"0.0","totalPrice":"0","communtityNameName":"IOI园博湾","resourceNum":"","releaseFlag":"0","provides":"","shopCategory":"0","sellTag":"","photo":"http://7xid0d.com1.z0.glb.clouddn.com/pic474171081-352.jpg","updateDate":"","propertyCompany":"此处是物业公司","around":"","resourceType":"3","shopType":"0","address":"此处是地址","y":"24.59149347387","brokers":"","x":"118.06285221359"}]
     * firstResult : 0
     * maxResults : 111
     */

    private int pageNo;
    private int pageSize;
    private int count;
    private int firstResult;
    private int maxResults;
    /**
     * officeId : 059206
     * remark : 此处是房源描述
     * serverAreaId : 05920604
     * validityPeriod :
     * communtityName : 汇景新城中心
     * unitPrice : 0
     * pop : 5
     * shopTag :
     * contactSex : 2
     * userType : 0
     * contacts : 啦啦啦
     * dbName : mysql
     * shopCategoryName :
     * acceptDate :
     * userId :
     * age : 2013
     * isNewRecord : false
     * userName :
     * decorationLevel : 1
     * releaseType : 0
     * directionName : 北
     * updateBy :
     * browseQuantity : 0
     * limit : 0
     * transfer : false
     * validateCode :
     * propertyRightTypeName : 国有产权
     * regionName : 翔安区
     * shopState : 0
     * display : 0
     * property : false
     * officeLevel : 0
     * category : 0
     * resourceTypeName : 住宅
     * traffic :
     * decorationLevelName : 豪华装修
     * floor : 10/20
     * officeType : 0
     * collectionNum : 0
     * cutEnabled : false
     * serverAreaName : 新店
     * flatShareType :
     * release : false
     * serviceDistrict :
     * contactWay : 18150088010
     * sellTagName : 可议价
     * direction : 6
     * shopStateName :
     * userPhoto :
     * excellent :
     * propertyRightType : 1
     * createBy :
     * id : 4f593077b5994a198be53eb68b54ad1b
     * title : 测试房源详情普通住宅出售
     * syncState : 0
     * area : 100.0
     * propertyCost : 0
     * officeName : 翔安区
     * synchronizedId :
     * layout : 2,1,2,,
     * wishPrice : 200
     * serviceVillage : 05920604002
     * createDate :
     * areaEnabled : 0.0
     * totalPrice : 0
     * communtityNameName : 汇景新城中心
     * resourceNum : SZ05920604002000001
     * releaseFlag : 0
     * provides :
     * shopCategory : 0
     * sellTag : 2
     * photo : http://7xid0d.com1.z0.glb.clouddn.com/pic474170796-966.jpg
     * updateDate :
     * propertyCompany :
     * around :
     * resourceType : 1
     * shopType : 0
     * address : 此处是地址
     * y : 24.625431
     * brokers :
     * x : 118.249528
     */

    private List<SellItem> list;

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

    public void setList(List<SellItem> list) {
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

    public List<SellItem> getList() {
        return list;
    }
}
