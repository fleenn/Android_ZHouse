package com.zfb.house.model.bean;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * Created by linwenbing on 16/6/14.
 */
@JsonAutoDetect(JsonMethod.FIELD)
public class ReleasePull {

    /**
     * description : 写字楼级别
     * remark :
     * label : 甲级
     * value : 1
     */

    @JsonProperty("HOUSE_XZLJB")
    private List<ReleasePullItem> HOUSE_XZLJB;
    /**
     * description : 经纪人专家类型
     * remark :
     * label : 售房专家
     * value : 1
     */
    @JsonProperty("EXPERT_TYPE")
    private List<ReleasePullItem> EXPERT_TYPE;
    /**
     * description : 商铺类型
     * remark :
     * label : 住宅底商
     * value : 1
     */
    @JsonProperty("HOUSE_SPLX")
    private List<ReleasePullItem> HOUSE_SPLX;

    /**
     * description : 产权性质
     * remark :
     * label : 国有产权
     * value : 1
     */
    @JsonProperty("HOUSE_CQXZ")
    private List<ReleasePullItem> HOUSE_CQXZ;

    /**
     * description : 投诉原因
     * remark :
     * label : 色情
     * value : 1
     */
    @JsonProperty("USER_COMPLAIN")
    private List<ReleasePullItem> USER_COMPLAIN;

    /**
     * description : 支付方式
     * remark :
     * label : 押一付三
     * value : 1
     */
    @JsonProperty("HOUSE_ZFFS")
    private List<ReleasePullItem> HOUSE_ZFFS;

    /**
     * description : 出租方式
     * remark :
     * label : 整租
     * value : 1
     */
    @JsonProperty("HOUSE_CZFS")
    private List<ReleasePullItem> HOUSE_CZFS;

    /**
     * description : 朝向
     * remark :
     * label : 东西
     * value : 8
     */
    @JsonProperty("HOUSE_CX")
    private List<ReleasePullItem> HOUSE_CX;

    /**
     * description : 装修程度
     * remark :
     * label : 豪华装修
     * value : 1
     */
    @JsonProperty("HOUSE_ZXCD")
    private List<ReleasePullItem> HOUSE_ZXCD;
    /**
     * description : 商铺配套
     * remark :
     * label : 客梯
     * value : 1
     */
    @JsonProperty("HOUSE_SPPT")
    private List<ReleasePullItem> HOUSE_SPPT;
    /**
     * description : 户型
     * remark :
     * label : 单间
     * value : 1
     */
    @JsonProperty("HOUSE_HX")
    private List<ReleasePullItem> HOUSE_HX;
    /**
     * description : 写字楼类型
     * remark :
     * label : 纯写字楼
     * value : 1
     */
    @JsonProperty("HOUSE_XZLLX")
    private List<ReleasePullItem> HOUSE_XZLLX;
    /**
     * description : 可经营类别
     * remark :
     * label : 餐饮美食
     * value : 1
     */
    @JsonProperty("HOUSE_KJYLB")
    private List<ReleasePullItem> HOUSE_KJYLB;
    /**
     * description : 写字楼支付方式
     * remark :
     * label : 面议
     * value : 1
     */
    @JsonProperty("HOUSE_XZLZFFS")
    private List<ReleasePullItem> HOUSE_XZLZFFS;
    /**
     * description : 商铺状态
     * remark :
     * label : 营业中
     * value : 1
     */
    @JsonProperty("HOUSE_SPZT")
    private List<ReleasePullItem> HOUSE_SPZT;
    /**
     * description : 用户近期状态
     * remark :
     * label : 了解房产市场
     * value : 1
     */
    @JsonProperty("RECENT_STATUS")
    private List<ReleasePullItem> RECENT_STATUS;
    /**
     * description : 商铺类别
     * remark :
     * label : 商铺出租
     * value : 1
     */
    @JsonProperty("HOUSE_SPLB")
    private List<ReleasePullItem> HOUSE_SPLB;
    /**
     * description : 售房标签
     * remark :
     * label : 满二唯一
     * value : 1
     */
    @JsonProperty("HOUSE_SELL")
    private List<ReleasePullItem> HOUSE_SELL;
    /**
     * description : 房源配套
     * remark :
     * label : 床
     * value : 10
     */
    @JsonProperty("HOUSE_FYPT")
    private List<ReleasePullItem> HOUSE_FYPT;
    /**
     * description :金融申请类型
     * remark :
     * label : 交易服务
     * value : 1
     */
    @JsonProperty("FINACIAL_TYPE")
    private List<ReleasePullItem> FINACIAL_TYPE;

    public List<ReleasePullItem> getHOUSE_XZLJB() {
        return HOUSE_XZLJB;
    }

    public void setHOUSE_XZLJB(List<ReleasePullItem> HOUSE_XZLJB) {
        this.HOUSE_XZLJB = HOUSE_XZLJB;
    }

    public List<ReleasePullItem> getEXPERT_TYPE() {
        return EXPERT_TYPE;
    }

    public void setEXPERT_TYPE(List<ReleasePullItem> EXPERT_TYPE) {
        this.EXPERT_TYPE = EXPERT_TYPE;
    }

    public List<ReleasePullItem> getHOUSE_SPLX() {
        return HOUSE_SPLX;
    }

    public void setHOUSE_SPLX(List<ReleasePullItem> HOUSE_SPLX) {
        this.HOUSE_SPLX = HOUSE_SPLX;
    }

    public List<ReleasePullItem> getHOUSE_CQXZ() {
        return HOUSE_CQXZ;
    }

    public void setHOUSE_CQXZ(List<ReleasePullItem> HOUSE_CQXZ) {
        this.HOUSE_CQXZ = HOUSE_CQXZ;
    }

    public List<ReleasePullItem> getUSER_COMPLAIN() {
        return USER_COMPLAIN;
    }

    public void setUSER_COMPLAIN(List<ReleasePullItem> USER_COMPLAIN) {
        this.USER_COMPLAIN = USER_COMPLAIN;
    }

    public List<ReleasePullItem> getHOUSE_ZFFS() {
        return HOUSE_ZFFS;
    }

    public void setHOUSE_ZFFS(List<ReleasePullItem> HOUSE_ZFFS) {
        this.HOUSE_ZFFS = HOUSE_ZFFS;
    }

    public List<ReleasePullItem> getHOUSE_CZFS() {
        return HOUSE_CZFS;
    }

    public void setHOUSE_CZFS(List<ReleasePullItem> HOUSE_CZFS) {
        this.HOUSE_CZFS = HOUSE_CZFS;
    }

    public List<ReleasePullItem> getHOUSE_CX() {
        return HOUSE_CX;
    }

    public void setHOUSE_CX(List<ReleasePullItem> HOUSE_CX) {
        this.HOUSE_CX = HOUSE_CX;
    }

    public List<ReleasePullItem> getHOUSE_ZXCD() {
        return HOUSE_ZXCD;
    }

    public void setHOUSE_ZXCD(List<ReleasePullItem> HOUSE_ZXCD) {
        this.HOUSE_ZXCD = HOUSE_ZXCD;
    }

    public List<ReleasePullItem> getHOUSE_SPPT() {
        return HOUSE_SPPT;
    }

    public void setHOUSE_SPPT(List<ReleasePullItem> HOUSE_SPPT) {
        this.HOUSE_SPPT = HOUSE_SPPT;
    }

    public List<ReleasePullItem> getHOUSE_HX() {
        return HOUSE_HX;
    }

    public void setHOUSE_HX(List<ReleasePullItem> HOUSE_HX) {
        this.HOUSE_HX = HOUSE_HX;
    }

    public List<ReleasePullItem> getHOUSE_XZLLX() {
        return HOUSE_XZLLX;
    }

    public void setHOUSE_XZLLX(List<ReleasePullItem> HOUSE_XZLLX) {
        this.HOUSE_XZLLX = HOUSE_XZLLX;
    }

    public List<ReleasePullItem> getHOUSE_KJYLB() {
        return HOUSE_KJYLB;
    }

    public void setHOUSE_KJYLB(List<ReleasePullItem> HOUSE_KJYLB) {
        this.HOUSE_KJYLB = HOUSE_KJYLB;
    }

    public List<ReleasePullItem> getHOUSE_XZLZFFS() {
        return HOUSE_XZLZFFS;
    }

    public void setHOUSE_XZLZFFS(List<ReleasePullItem> HOUSE_XZLZFFS) {
        this.HOUSE_XZLZFFS = HOUSE_XZLZFFS;
    }

    public List<ReleasePullItem> getHOUSE_SPZT() {
        return HOUSE_SPZT;
    }

    public void setHOUSE_SPZT(List<ReleasePullItem> HOUSE_SPZT) {
        this.HOUSE_SPZT = HOUSE_SPZT;
    }

    public List<ReleasePullItem> getRECENT_STATUS() {
        return RECENT_STATUS;
    }

    public void setRECENT_STATUS(List<ReleasePullItem> RECENT_STATUS) {
        this.RECENT_STATUS = RECENT_STATUS;
    }

    public List<ReleasePullItem> getHOUSE_SPLB() {
        return HOUSE_SPLB;
    }

    public void setHOUSE_SPLB(List<ReleasePullItem> HOUSE_SPLB) {
        this.HOUSE_SPLB = HOUSE_SPLB;
    }

    public List<ReleasePullItem> getHOUSE_SELL() {
        return HOUSE_SELL;
    }

    public void setHOUSE_SELL(List<ReleasePullItem> HOUSE_SELL) {
        this.HOUSE_SELL = HOUSE_SELL;
    }

    public List<ReleasePullItem> getHOUSE_FYPT() {
        return HOUSE_FYPT;
    }

    public void setHOUSE_FYPT(List<ReleasePullItem> HOUSE_FYPT) {
        this.HOUSE_FYPT = HOUSE_FYPT;
    }

    public List<ReleasePullItem> getFINACIAL_TYPE() {
        return FINACIAL_TYPE;
    }

    public void setFINACIAL_TYPE(List<ReleasePullItem> FINACIAL_TYPE) {
        this.FINACIAL_TYPE = FINACIAL_TYPE;
    }

    public String getLabel(List<ReleasePullItem> list,String key){
        for (ReleasePullItem item:list){
            if(item.getValue().equals(key)){
                return item.getLabel();
            }
        }
        return key;
    }

    public String getValue(List<ReleasePullItem> list,String label){
        for (ReleasePullItem item:list){
            if(item.getLabel().equals(label)){
                return item.getLabel();
            }
        }
        return label;
    }
}