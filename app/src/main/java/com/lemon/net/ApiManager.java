package com.lemon.net;

import android.content.Context;

import com.lemon.annotation.Autowired;
import com.lemon.annotation.Component;
import com.lemon.annotation.InvokeMethod;
import com.lemon.annotation.ParamType;
import com.lemon.annotation.ReturnType;
import com.lemon.model.BaseParam;
import com.lemon.model.param.AppUpdateParam;
import com.lemon.model.result.AppUpdateResult;
import com.zfb.house.model.param.AddFriendsParam;
import com.zfb.house.model.param.AliasParam;
import com.zfb.house.model.param.AreaListParam;
import com.zfb.house.model.param.AvatarParam;
import com.zfb.house.model.param.BrokerAcceptedParam;
import com.zfb.house.model.param.BrokerListParam;
import com.zfb.house.model.param.CallParam;
import com.zfb.house.model.param.CheckPhoneParam;
import com.zfb.house.model.param.CollectMomentsParam;
import com.zfb.house.model.param.CollectParam;
import com.zfb.house.model.param.CollectRentParam;
import com.zfb.house.model.param.CollectSellParam;
import com.zfb.house.model.param.CompanyParam;
import com.zfb.house.model.param.ComplaintParam;
import com.zfb.house.model.param.ConcernHouseParam;
import com.zfb.house.model.param.ConcernParam;
import com.zfb.house.model.param.ConcernPeopleParam;
import com.zfb.house.model.param.CoordinateParam;
import com.zfb.house.model.param.DayTaskParam;
import com.zfb.house.model.param.DeleteConcernParam;
import com.zfb.house.model.param.DeleteFriendParam;
import com.zfb.house.model.param.DeleteReplyParam;
import com.zfb.house.model.param.DeleteShopHouseParam;
import com.zfb.house.model.param.ExpertTypeParam;
import com.zfb.house.model.param.FeedBackParam;
import com.zfb.house.model.param.FinanceApplyParam;
import com.zfb.house.model.param.FinanceBannerParam;
import com.zfb.house.model.param.FindMyAuthroizeRentHouseParam;
import com.zfb.house.model.param.FindMyAuthroizeSellHouseParam;
import com.zfb.house.model.param.FindPwdParam;
import com.zfb.house.model.param.FriendMomentsParam;
import com.zfb.house.model.param.GoodsExchangeParam;
import com.zfb.house.model.param.GoodsListParam;
import com.zfb.house.model.param.GoodsParam;
import com.zfb.house.model.param.GrabCustomerListParam;
import com.zfb.house.model.param.GrabCustomerParam;
import com.zfb.house.model.param.GrabHouseListParam;
import com.zfb.house.model.param.GrabHouseRentParam;
import com.zfb.house.model.param.HouseEntrustCallDeleteParam;
import com.zfb.house.model.param.HouseEntrustDeleteParam;
import com.zfb.house.model.param.HouseEntrustDeleteSellParam;
import com.zfb.house.model.param.IndexParam;
import com.zfb.house.model.param.JudgeBrokerParam;
import com.zfb.house.model.param.LoginByPwdParam;
import com.zfb.house.model.param.LoginBySmsParam;
import com.zfb.house.model.param.MobileParam;
import com.zfb.house.model.param.MomentsDetailParam;
import com.zfb.house.model.param.MomentsParam;
import com.zfb.house.model.param.MomentsReplyParam;
import com.zfb.house.model.param.MyFriendsParam;
import com.zfb.house.model.param.MyMomentDeleteParam;
import com.zfb.house.model.param.MyMomentsParam;
import com.zfb.house.model.param.MyOrderParam;
import com.zfb.house.model.param.NearBrokerParam;
import com.zfb.house.model.param.PraiseParam;
import com.zfb.house.model.param.PutOnShopHouseParam;
import com.zfb.house.model.param.QiNiuParam;
import com.zfb.house.model.param.QualificationParam;
import com.zfb.house.model.param.ReadMsgParam;
import com.zfb.house.model.param.RealNameParam;
import com.zfb.house.model.param.RegisterParam;
import com.zfb.house.model.param.ReleasePullDataParam;
import com.zfb.house.model.param.ReleaseRentHouseParam;
import com.zfb.house.model.param.ReleaseSellHouseParam;
import com.zfb.house.model.param.RelevantParam;
import com.zfb.house.model.param.RemarkParam;
import com.zfb.house.model.param.RentListParam;
import com.zfb.house.model.param.SearchFriendsParam;
import com.zfb.house.model.param.SearchPlotParam;
import com.zfb.house.model.param.SellListParam;
import com.zfb.house.model.param.SendMomentsParam;
import com.zfb.house.model.param.ServiceDistrictParam;
import com.zfb.house.model.param.ServiceVillageParam;
import com.zfb.house.model.param.SexParam;
import com.zfb.house.model.param.SignInParam;
import com.zfb.house.model.param.SignParam;
import com.zfb.house.model.param.SmsParam;
import com.zfb.house.model.param.TopShopHouseParam;
import com.zfb.house.model.param.TradingParam;
import com.zfb.house.model.param.UserCallEntrustParam;
import com.zfb.house.model.param.UserEntrustDeleteParam;
import com.zfb.house.model.param.UserInfoParam;
import com.zfb.house.model.param.UserPersonalParam;
import com.zfb.house.model.param.UserPurposeListParam;
import com.zfb.house.model.param.UserRequirementParam;
import com.zfb.house.model.param.UserStatusParam;
import com.zfb.house.model.param.VillageParam;
import com.zfb.house.model.result.AddFriendsResult;
import com.zfb.house.model.result.AliasResult;
import com.zfb.house.model.result.AreaListResult;
import com.zfb.house.model.result.AvatarResult;
import com.zfb.house.model.result.BrokerAcceptedResult;
import com.zfb.house.model.result.BrokerListResult;
import com.zfb.house.model.result.CallResult;
import com.zfb.house.model.result.CheckPhoneResult;
import com.zfb.house.model.result.CollectMomentsResult;
import com.zfb.house.model.result.CollectRentResult;
import com.zfb.house.model.result.CollectResult;
import com.zfb.house.model.result.CollectSellResult;
import com.zfb.house.model.result.CompanyResult;
import com.zfb.house.model.result.ComplaintResult;
import com.zfb.house.model.result.ConcernHouseResult;
import com.zfb.house.model.result.ConcernPeopleResult;
import com.zfb.house.model.result.ConcernResult;
import com.zfb.house.model.result.CoordinateResult;
import com.zfb.house.model.result.DayTaskResult;
import com.zfb.house.model.result.DeleteCollectResult;
import com.zfb.house.model.result.DeleteConcernResult;
import com.zfb.house.model.result.DeleteFriendResult;
import com.zfb.house.model.result.DeleteReplyResult;
import com.zfb.house.model.result.DeleteShopHouseResult;
import com.zfb.house.model.result.ExpertTypeResult;
import com.zfb.house.model.result.FeedBackResult;
import com.zfb.house.model.result.FinanceApplyResult;
import com.zfb.house.model.result.FinanceBannerResult;
import com.zfb.house.model.result.FindMyAuthroizeRentHouseResult;
import com.zfb.house.model.result.FindMyAuthroizeSellHouseResult;
import com.zfb.house.model.result.FindPwdResult;
import com.zfb.house.model.result.FriendsMomentsResult;
import com.zfb.house.model.result.GoodsExchangeResult;
import com.zfb.house.model.result.GoodsListResult;
import com.zfb.house.model.result.GoodsResult;
import com.zfb.house.model.result.GrabCustomerListResult;
import com.zfb.house.model.result.GrabCustomerResult;
import com.zfb.house.model.result.GrabHouseRentListResult;
import com.zfb.house.model.result.GrabHouseRentResult;
import com.zfb.house.model.result.GrabHouseSellListResult;
import com.zfb.house.model.result.GrabHouseSellResult;
import com.zfb.house.model.result.GrabbedHouseRentListResult;
import com.zfb.house.model.result.GrabbedHouseSellListResult;
import com.zfb.house.model.result.HouseEntrustCallDeleteResult;
import com.zfb.house.model.result.HouseEntrustRentDeleteResult;
import com.zfb.house.model.result.HouseEntrustSellDeleteResult;
import com.zfb.house.model.result.IndexResult;
import com.zfb.house.model.result.JudgeBrokerResult;
import com.zfb.house.model.result.LoginResult;
import com.zfb.house.model.result.MobileResult;
import com.zfb.house.model.result.MomentsDetailResult;
import com.zfb.house.model.result.MomentsReplyResult;
import com.zfb.house.model.result.MyFriendsResult;
import com.zfb.house.model.result.MyMomentDeleteResult;
import com.zfb.house.model.result.MyMomentsResult;
import com.zfb.house.model.result.MyOrderResult;
import com.zfb.house.model.result.NearBrokerResult;
import com.zfb.house.model.result.NeighborMomentsResult;
import com.zfb.house.model.result.PraiseResult;
import com.zfb.house.model.result.PutOnShopHouseResult;
import com.zfb.house.model.result.QiNiuResult;
import com.zfb.house.model.result.QualificationResult;
import com.zfb.house.model.result.ReadMsgResult;
import com.zfb.house.model.result.RealNameResult;
import com.zfb.house.model.result.RegisterResult;
import com.zfb.house.model.result.ReleasePullDataResult;
import com.zfb.house.model.result.ReleaseRentHouseResult;
import com.zfb.house.model.result.ReleaseSellHouseResult;
import com.zfb.house.model.result.RelevantMomentsResult;
import com.zfb.house.model.result.RelevantResult;
import com.zfb.house.model.result.RemarkResult;
import com.zfb.house.model.result.RentListResult;
import com.zfb.house.model.result.SearchFriendsResult;
import com.zfb.house.model.result.SearchPlotResult;
import com.zfb.house.model.result.SellListResult;
import com.zfb.house.model.result.SendMomentsResult;
import com.zfb.house.model.result.ServiceDistrictResult;
import com.zfb.house.model.result.ServiceVillageResult;
import com.zfb.house.model.result.SexResult;
import com.zfb.house.model.result.SignInResult;
import com.zfb.house.model.result.SignResult;
import com.zfb.house.model.result.SmsResult;
import com.zfb.house.model.result.TopShopHouseResult;
import com.zfb.house.model.result.TradingResult;
import com.zfb.house.model.result.UserCallEntrustResult;
import com.zfb.house.model.result.UserEntrustCallDeleteResult;
import com.zfb.house.model.result.UserInfoResult;
import com.zfb.house.model.result.UserPersonalResult;
import com.zfb.house.model.result.UserPurposeListResult;
import com.zfb.house.model.result.UserRequirementResult;
import com.zfb.house.model.result.UserStatusResult;
import com.zfb.house.model.result.VillageResult;

/**
 * 项目名称:  [Lemon]
 * 包:        [com.lemon.net]
 * 类描述:    [简要描述]
 * 创建人:    [xflu]
 * 创建时间:  [2015/12/16 14:07]
 * 修改人:    [xflu]
 * 修改时间:  [2015/12/16 14:07]
 * 修改备注:  [说明本次修改内容]
 * 版本:      [v1.0]
 */
@Component
public class ApiManager {

    @Autowired
    public Context mContext;
    @Autowired
    public NetEngine netEngine;

    @ParamType(value = AppUpdateParam.class)
    @ReturnType(value = AppUpdateResult.class)
    @InvokeMethod(methodName = "getNewVersion")
    public void update(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 获取短信验证码
     *
     * @param param
     */
    @ParamType(value = SmsParam.class)
    @ReturnType(value = SmsResult.class)
    public void getSms(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 通过密码登录
     *
     * @param param
     */
    @ParamType(value = LoginByPwdParam.class)
    @ReturnType(value = LoginResult.class)
    public void loginByPass(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 通过短信登录
     *
     * @param param
     */
    @ParamType(value = LoginBySmsParam.class)
    @ReturnType(value = LoginResult.class)
    public void loginBySmscode(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 找回密码
     *
     * @param param
     */
    @ParamType(value = FindPwdParam.class)
    @ReturnType(value = FindPwdResult.class)
    public void forgetPass(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 注册
     *
     * @param param
     */
    @ParamType(value = RegisterParam.class)
    @ReturnType(value = RegisterResult.class)
    public void regist(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 检查手机是否注册
     *
     * @param param
     */
    @ParamType(value = CheckPhoneParam.class)
    @ReturnType(value = CheckPhoneResult.class)
    public void registedCheck(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 获取首页banner以及附近经纪人信息
     *
     * @param param
     */
    @ParamType(value = IndexParam.class)
    @ReturnType(value = IndexResult.class)
    public void index(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 上传坐标信息
     *
     * @param param
     */
    @ParamType(value = CoordinateParam.class)
    @ReturnType(value = CoordinateResult.class)
    public void updateCoordinate(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 获取七牛token
     *
     * @param param
     */
    @ParamType(value = QiNiuParam.class)
    @ReturnType(value = QiNiuResult.class)
    public void uploadToken(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 发布房友圈
     *
     * @param param
     */
    @ParamType(value = SendMomentsParam.class)
    @ReturnType(value = SendMomentsResult.class)
    public void saveHouseElite(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 获取用户详细信息
     *
     * @param param
     */
    @ParamType(value = UserPersonalParam.class)
    @ReturnType(value = UserPersonalResult.class)
    public void getUserDetail(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 用户更新最近状态
     *
     * @param param
     */
    @ParamType(value = UserStatusParam.class)
    @ReturnType(value = UserStatusResult.class)
    public void updateUserStatus(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 更新专家类型
     *
     * @param param
     */
    @ParamType(value = ExpertTypeParam.class)
    @ReturnType(value = ExpertTypeResult.class)
    public void updateExpertType(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 更新头像
     *
     * @param param
     */
    @ParamType(value = AvatarParam.class)
    @ReturnType(value = AvatarResult.class)
    public void updatePhoto(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 更新别名
     *
     * @param param
     */
    @ParamType(value = AliasParam.class)
    @ReturnType(value = AliasResult.class)
    public void updateAlise(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 更新性别
     *
     * @param param
     */
    @ParamType(value = SexParam.class)
    @ReturnType(value = SexResult.class)
    public void updateSex(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 更新公司
     *
     * @param param
     */
    @ParamType(value = CompanyParam.class)
    @ReturnType(value = CompanyResult.class)
    public void updateCompany(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 更新签名
     *
     * @param param
     */
    @ParamType(value = SignParam.class)
    @ReturnType(value = SignResult.class)
    public void updateSign(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 更新用户信息
     *
     * @param param
     */
    @ParamType(value = UserInfoParam.class)
    @ReturnType(value = UserInfoResult.class)
    public void updateUserInfo(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 实名认证
     *
     * @param param
     */
    @ParamType(value = RealNameParam.class)
    @ReturnType(value = RealNameResult.class)
    public void realNameAuthentication(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 资质认证
     *
     * @param param
     */
    @ParamType(value = QualificationParam.class)
    @ReturnType(value = QualificationResult.class)
    public void qualificationAuthentication(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 成交上传
     *
     * @param param
     */
    @ParamType(value = TradingParam.class)
    @ReturnType(value = TradingResult.class)
    public void uploadTrading(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 用户签到
     *
     * @param param
     */
    @ParamType(value = SignInParam.class)
    @ReturnType(value = SignInResult.class)
    public void signIn(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 查询用户关注的专家
     *
     * @param param
     */
    @ParamType(value = ConcernPeopleParam.class)
    @ReturnType(value = ConcernPeopleResult.class)
    public void getConcernPeople(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 关注专家
     *
     * @param param
     */
    @ParamType(value = ConcernParam.class)
    @ReturnType(value = ConcernResult.class)
    public void saveConcern(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 关注房源
     *
     * @param param
     */
    @ParamType(value = ConcernHouseParam.class)
    @ReturnType(value = ConcernHouseResult.class)
    public void concernHouse(BaseParam param) {
        netEngine.invoke(param);
    }


    /**
     * 删除关注的经纪人或房源
     *
     * @param param
     */
    @ParamType(value = DeleteConcernParam.class)
    @ReturnType(value = DeleteConcernResult.class)
    public void deleteConcern(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 获取省市区以及片区
     *
     * @param param
     */
    @ParamType(value = AreaListParam.class)
    @ReturnType(value = AreaListResult.class)
    public void getAreaList(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 获取小区
     *
     * @param param
     */
    @ParamType(value = VillageParam.class)
    @ReturnType(value = VillageResult.class)
    public void getVillage(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 更新用户关注的片区 or 更新经纪人服务的片区
     *
     * @param param
     */
    @ParamType(value = ServiceDistrictParam.class)
    @ReturnType(value = ServiceDistrictResult.class)
    public void updateServiceDistrict(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 更新用户关注的小区 or 更新经纪人服务的小区
     *
     * @param param
     */
    @ParamType(value = ServiceVillageParam.class)
    @ReturnType(value = ServiceVillageResult.class)
    public void updateServiceVillage(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 好友列表
     *
     * @param param
     */
    @ParamType(value = MyFriendsParam.class)
    @ReturnType(value = MyFriendsResult.class)
    public void listMyFriends(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 修改好友备注
     *
     * @param param
     */
    @ParamType(value = RemarkParam.class)
    @ReturnType(value = RemarkResult.class)
    public void modifyRemark(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 投诉好友
     *
     * @param param
     */
    @ParamType(value = ComplaintParam.class)
    @ReturnType(value = ComplaintResult.class)
    public void saveComplain(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 删除好友
     *
     * @param param
     */
    @ParamType(value = DeleteFriendParam.class)
    @ReturnType(value = DeleteFriendResult.class)
    public void deleteFriend(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 查看自己房友圈的内容
     *
     * @param param
     */
    @ParamType(value = MyMomentsParam.class)
    @ReturnType(value = MyMomentsResult.class)
    public void listM(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 查看好友房友圈的内容
     *
     * @param param
     */
    @ParamType(value = FriendMomentsParam.class)
    @ReturnType(value = FriendsMomentsResult.class)
    public void listO(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 查看收藏房友圈消息
     *
     * @param param
     */
    @ParamType(value = CollectMomentsParam.class)
    @ReturnType(value = CollectMomentsResult.class)
    public void listMC(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 删除房友圈消息
     *
     * @param param
     */
    @ParamType(value = MyMomentDeleteParam.class)
    @ReturnType(value = MyMomentDeleteResult.class)
    public void deleteHouseElite(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 查询收藏的所有房源
     *
     * @param param
     */
    @ParamType(value = CollectSellParam.class)
    @ReturnType(value = CollectSellResult.class)
    public void findSolicitudeSellHouse(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 查询收藏的租房房源
     *
     * @param param
     */
    @ParamType(value = CollectRentParam.class)
    @ReturnType(value = CollectRentResult.class)
    public void findSolicitudeRentHouse(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 附近人的房友圈
     *
     * @param param
     */
    @ParamType(value = MomentsParam.class)
    @ReturnType(value = NeighborMomentsResult.class)
    public void listN(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 查看好友的房友圈消息
     *
     * @param param
     */
    @ParamType(value = MomentsParam.class)
    @ReturnType(value = FriendsMomentsResult.class)
    public void listF(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 与我相关的房友圈
     *
     * @param param
     */
    @ParamType(value = MomentsParam.class)
    @ReturnType(value = RelevantMomentsResult.class)
    public void listC(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 评论房友圈
     *
     * @param param
     */
    @ParamType(value = MomentsReplyParam.class)
    @ReturnType(value = MomentsReplyResult.class)
    public void saveHouseEliteReply(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 删除房友圈评论
     *
     * @param param
     */
    @ParamType(value = DeleteReplyParam.class)
    @ReturnType(value = DeleteReplyResult.class)
    public void deleteHouseEliteReply(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 出租房源列表
     *
     * @param param
     */
    @ParamType(value = RentListParam.class)
    @ReturnType(value = RentListResult.class)
    public void rentList(BaseParam param) {
        netEngine.invoke(param);
    }


    /**
     * 销售房源列表
     *
     * @param param
     */
    @ParamType(value = SellListParam.class)
    @ReturnType(value = SellListResult.class)
    public void sellList(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 一键呼叫
     *
     * @param param
     */
    @ParamType(value = CallParam.class)
    @ReturnType(value = CallResult.class)
    public void sendMsgToNearbyBrokers(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 获取发布房源下拉信息
     *
     * @param param
     */
    @ParamType(value = ReleasePullDataParam.class)
    @ReturnType(value = ReleasePullDataResult.class)
    public void getAll(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 小区搜索
     *
     * @param param
     */
    @ParamType(value = SearchPlotParam.class)
    @ReturnType(value = SearchPlotResult.class)
    public void getVillageName(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 发布出售房源
     *
     * @param param
     */
    @ParamType(value = ReleaseSellHouseParam.class)
    @ReturnType(value = ReleaseSellHouseResult.class)
    @InvokeMethod(methodName = "houseSellAndroid")
    public void houseSellAndroid(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 发布出租房源
     *
     * @param param
     */
    @InvokeMethod(methodName = "houseRentAndroid")
    @ParamType(value = ReleaseRentHouseParam.class)
    @ReturnType(value = ReleaseRentHouseResult.class)
    public void houseRentAndroid(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 编辑出售房源
     *
     * @param param
     */
    @InvokeMethod(methodName = "updateHouseSellAndroid")
    @ParamType(value = ReleaseSellHouseParam.class)
    @ReturnType(value = ReleaseSellHouseResult.class)
    public void updateHouseSell(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 编辑出租房源
     *
     * @param param
     */
    @InvokeMethod(methodName = "updateHouseRentAndroid")
    @ParamType(value = ReleaseRentHouseParam.class)
    @ReturnType(value = ReleaseRentHouseResult.class)
    public void updateHouseRent(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 更新电话号码
     *
     * @param param
     */
    @ParamType(value = MobileParam.class)
    @ReturnType(value = MobileResult.class)
    public void updateMobile(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 获取可抢/已抢用户列表
     *
     * @param param
     */
    @ParamType(value = GrabCustomerListParam.class)
    @ReturnType(value = GrabCustomerListResult.class)
    public void listOrder(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 获取可抢出租房源
     *
     * @param param
     */
    @ParamType(value = GrabHouseListParam.class)
    @ReturnType(value = GrabHouseRentListResult.class)
    public void listCanRobRentHouseAndroid(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 获取可抢出售房源
     *
     * @param param
     */
    @ParamType(value = GrabHouseListParam.class)
    @ReturnType(value = GrabHouseSellListResult.class)
    public void listCanRobSellHouseAndroid(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 获取已抢出租房源
     *
     * @param param
     */
    @ParamType(value = GrabHouseListParam.class)
    @ReturnType(value = GrabbedHouseRentListResult.class)
    public void myRentingHousesAndroid(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 获取已抢出售房源
     *
     * @param param
     */
    @ParamType(value = GrabHouseListParam.class)
    @ReturnType(value = GrabbedHouseSellListResult.class)
    public void mySellHousesAndroid(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 抢客户
     *
     * @param param
     */
    @ParamType(value = GrabCustomerParam.class)
    @ReturnType(value = GrabCustomerResult.class)
    public void robOrder(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 点赞
     *
     * @param param
     */
    @ParamType(value = PraiseParam.class)
    @ReturnType(value = PraiseResult.class)
    public void saveHouseElitePrise(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 收藏房友圈
     *
     * @param param
     */
    @ParamType(value = CollectParam.class)
    @ReturnType(value = CollectResult.class)
    public void saveHouseEliteCollection(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 取消收藏的房友圈
     *
     * @param param
     */
    @ParamType(value = CollectParam.class)
    @ReturnType(value = DeleteCollectResult.class)
    public void delHouseEliteCollection(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 抢出租房源
     *
     * @param param
     */
    @ParamType(value = GrabHouseRentParam.class)
    @ReturnType(value = GrabHouseRentResult.class)
    public void robRentHouse(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 抢出租房源
     *
     * @param param
     */
    @ParamType(value = GrabHouseRentParam.class)
    @ReturnType(value = GrabHouseSellResult.class)
    public void robSellHouse(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 我的委托出租房源
     *
     * @param param
     */
    @ParamType(value = FindMyAuthroizeRentHouseParam.class)
    @ReturnType(value = FindMyAuthroizeRentHouseResult.class)
    public void findMyAuthroizeRentHouse(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 查询我的委托出售房源
     *
     * @param param
     */
    @ParamType(value = FindMyAuthroizeSellHouseParam.class)
    @ReturnType(value = FindMyAuthroizeSellHouseResult.class)
    public void findMyAuthroizeSellHouse(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 删除我的委托-出租房源
     *
     * @param param
     */
    @ParamType(value = HouseEntrustDeleteParam.class)
    @ReturnType(value = HouseEntrustRentDeleteResult.class)
    public void deleteMyRentHouse(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 删除我的委托-出售房源
     *
     * @param param
     */
    @ParamType(value = HouseEntrustDeleteSellParam.class)
    @ReturnType(value = HouseEntrustSellDeleteResult.class)
    public void deleteMySellHouse(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 删除我的委托-呼叫委托
     *
     * @param param
     */
    @ParamType(value = HouseEntrustCallDeleteParam.class)
    @ReturnType(value = HouseEntrustCallDeleteResult.class)
    public void deleteBelongOrder(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 删除我的委托-呼叫委托(用户)
     *
     * @param param
     */
    @ParamType(value = UserEntrustDeleteParam.class)
    @ReturnType(value = UserEntrustCallDeleteResult.class)
    public void deleteOrder(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 我的委托——呼叫委托
     *
     * @param param
     */
    @ParamType(value = UserCallEntrustParam.class)
    @ReturnType(value = UserCallEntrustResult.class)
    public void myOrder(BaseParam param) {
        netEngine.invoke(param);
    }

    @ParamType(value = DeleteShopHouseParam.class)
    @ReturnType(value = DeleteShopHouseResult.class)
    @InvokeMethod(methodName = "deleteMyRentHouse")
    public void deleteMyHouse(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 上下架
     *
     * @param param
     */
    @ParamType(value = PutOnShopHouseParam.class)
    @ReturnType(value = PutOnShopHouseResult.class)
    public void putOnHouse(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 置顶
     *
     * @param param
     */
    @ParamType(value = TopShopHouseParam.class)
    @ReturnType(value = TopShopHouseResult.class)
    public void topHouse(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 获取附近
     *
     * @param param
     */
    @ParamType(value = NearBrokerParam.class)
    @ReturnType(value = NearBrokerResult.class)
    public void nearByBroker(BaseParam param) {
        netEngine.invoke(param);
    }


//    /**
//     * 查看房友圈与我相关消息，含点赞
//     *
//     * @param param
//     */
//    @ParamType(value = ListMyRelationMsgParam.class)
//    @ReturnType(value = ListMyRelationMsgResult.class)
//    public void listMyRelationMsg(BaseParam param) {
//        netEngine.invoke(param);
//    }

    /**
     * 查找租售专家
     *
     * @param param
     */
    @ParamType(value = BrokerListParam.class)
    @ReturnType(value = BrokerListResult.class)
    public void saleRentBrokers(BaseParam param) {
        netEngine.invoke(param);
    }


    /**
     * 添加好友
     *
     * @param param
     */
    @ParamType(value = AddFriendsParam.class)
    @ReturnType(value = AddFriendsResult.class)
    public void addFriend(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 查找用户
     *
     * @param param
     */
    @ParamType(value = SearchFriendsParam.class)
    @ReturnType(value = SearchFriendsResult.class)
    public void searchUser(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 房友圈详情页
     *
     * @param param
     */
    @ParamType(value = MomentsDetailParam.class)
    @ReturnType(value = MomentsDetailResult.class)
    public void getHouseElite(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 房友圈详情页
     *
     * @param param
     */
    @ParamType(value = RelevantParam.class)
    @ReturnType(value = RelevantResult.class)
    public void listMyRelationMsg(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 已读消息反馈
     *
     * @param param
     */
    @ParamType(value = ReadMsgParam.class)
    @ReturnType(value = ReadMsgResult.class)
    public void readMsg(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 意见反馈
     *
     * @param param
     */
    @ParamType(value = FeedBackParam.class)
    @ReturnType(value = FeedBackResult.class)
    public void saveFeedBack(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 售后金融申请
     *
     * @param param
     */
    @ParamType(value = FinanceApplyParam.class)
    @ReturnType(value = FinanceApplyResult.class)
    public void finacialApply(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 金融banner获取
     *
     * @param param
     */
    @ParamType(value = FinanceBannerParam.class)
    @ReturnType(value = FinanceBannerResult.class)
    public void finacialBanner(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 评价
     *
     * @param param
     */
    @ParamType(value = JudgeBrokerParam.class)
    @ReturnType(value = JudgeBrokerResult.class)
    public void saveEvaluate(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 商品列表
     *
     * @param param
     */
    @ParamType(value = GoodsListParam.class)
    @ReturnType(value = GoodsListResult.class)
    public void itemList(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 商品详情
     *
     * @param param
     */
    @ParamType(value = GoodsParam.class)
    @ReturnType(value = GoodsResult.class)
    public void item(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 商品兑换
     *
     * @param param
     */
    @ParamType(value = GoodsExchangeParam.class)
    @ReturnType(value = GoodsExchangeResult.class)
    public void itemExchange(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 我的订单
     *
     * @param param
     */
    @ParamType(value = MyOrderParam.class)
    @ReturnType(value = MyOrderResult.class)
    public void myItems(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 每日任务 or 新手任务
     *
     * @param param
     */
    @ParamType(value = DayTaskParam.class)
    @ReturnType(value = DayTaskResult.class)
    public void myDailyTask(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 发送用户找房需求
     *
     * @param param
     */
    @ParamType(value = UserRequirementParam.class)
    @ReturnType(value = UserRequirementResult.class)
    public void sendUserRequirement(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 获取用户已发送的需求列表
     *
     * @param param
     */
    @ParamType(value = UserPurposeListParam.class)
    @ReturnType(value = UserPurposeListResult.class)
    public void listMyRequirement(BaseParam param) {
        netEngine.invoke(param);
    }

    /**
     * 获取用户已发送的需求列表
     *
     * @param param
     */
    @ParamType(value = BrokerAcceptedParam.class)
    @ReturnType(value = BrokerAcceptedResult.class)
    public void listReceiveRequirement(BaseParam param) {
        netEngine.invoke(param);
    }
}
