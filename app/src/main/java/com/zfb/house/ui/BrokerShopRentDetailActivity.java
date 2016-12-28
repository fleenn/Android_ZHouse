package com.zfb.house.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lemon.LemonActivity;
import com.lemon.LemonContext;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.model.StatusCode;
import com.lemon.util.ParamUtils;
import com.lemon.util.PhoneUtil;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.PhotoPagerAdapter;
import com.zfb.house.component.CirclePageIndicator;
import com.zfb.house.component.RatingBar;
import com.zfb.house.emchat.ChatActivity;
import com.zfb.house.model.bean.RentItem;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.model.param.ConcernHouseParam;
import com.zfb.house.model.param.DeleteConcernParam;
import com.zfb.house.model.param.UserPersonalParam;
import com.zfb.house.model.result.ConcernHouseResult;
import com.zfb.house.model.result.DeleteConcernResult;
import com.zfb.house.model.result.UserPersonalResult;
import com.zfb.house.util.ToolUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称:  [zfbandroid]
 * 包:        [com.zfb.house.ui]
 * 类描述:    [经纪人店铺出租房源详情界面]
 * 创建人:    [xflu]
 * 创建时间:  [2016/6/3 10:06]
 * 修改人:    [xflu]
 * 修改时间:  [2016/6/3 10:06]
 * 修改备注:  [说明本次修改内容]
 * 版本:      [v1.0]
 */
@Layout(id = R.layout.activity_broker_shop_rent_detail)
public class BrokerShopRentDetailActivity extends LemonActivity {

    private static final String TAG = "BrokerShopRentDetailActivity";
    //出租房源实体
    private RentItem item;
    //当前经纪人实体
    private UserBean userBean;
    @FieldView(id = R.id.vp_photo)
    private ViewPager vpPhoto;
//    @FieldView(id = R.id.indicator)
//    private CirclePageIndicator indicator;

    //标题
    @FieldView(id = R.id.tv_shop_title)
    private TextView txtTitle;
    //时间
    @FieldView(id = R.id.tv_shop_time)
    private TextView txtTime;
    //区域-片区
    @FieldView(id = R.id.tv_shop_house)
    private TextView txtHouse;
    //头像
    @FieldView(id = R.id.im_avatar)
    private ImageView imgAvatar;
    //名称
    @FieldView(id = R.id.tv_name)
    private TextView txtName;
    //星级
    @FieldView(id = R.id.rating_star)
    private RatingBar rbStar;

    @FieldView(id = R.id.tv_main_info1)
    private TextView txtInfo1;
    @FieldView(id = R.id.tv_main_info_value)
    private TextView txtInfoValue1;
    @FieldView(id = R.id.tv_main_info2)
    private TextView txtInfo2;
    @FieldView(id = R.id.tv_main_info2_value)
    private TextView txtInfoValue2;
    @FieldView(id = R.id.tv_main_info3)
    private TextView txtInfo3;
    @FieldView(id = R.id.tv_main_info3_value)
    private TextView txtInfoValue3;

    //级别
    @FieldView(id = R.id.tv_shop_level)
    private TextView txtLevel;
    //状态
    @FieldView(id = R.id.tv_shop_statue)
    private TextView txtStatue;
    // 类别
    @FieldView(id = R.id.tv_shop_type)
    private TextView txtType;
    //单价/出租方式：
    @FieldView(id = R.id.tv_shop_price_key)
    private TextView txtPriceKey;
    //单价/出租方式——值：
    @FieldView(id = R.id.tv_shop_price_value)
    private TextView txtPriceValue;
    //朝向/级别/状态：
    @FieldView(id = R.id.tv_shop_orientation_key)
    private TextView txtOrientationKey;
    //朝向/级别/状态——值：
    @FieldView(id = R.id.tv_shop_orientation_value)
    private TextView txtOrientationValue;
    //装修：
    @FieldView(id = R.id.tv_shop_unit_renovate)
    private TextView txtRenovate;
    //楼层：
    @FieldView(id = R.id.tv_shop_unit_floor)
    private TextView txtFloor;
    //建筑年代：
    @FieldView(id = R.id.tv_shop_unit_year)
    private TextView txtYear;
    //产权性质/支付方式：
    @FieldView(id = R.id.tv_shop_property_key)
    private TextView txtPropertyKey;
    //产权性质/支付方式——值：
    @FieldView(id = R.id.tv_shop_property_value)
    private TextView txtPropertyValue;
    //有无电梯：
    @FieldView(id = R.id.tv_shop_automatic)
    private TextView txtAutomatic;
    //几梯几户：
    @FieldView(id = R.id.tv_shop_unit_home)
    private TextView txtFac;
    //房源地址：
    @FieldView(id = R.id.tv_shop_address)
    private TextView txtAddress;
    //房源描述：
    @FieldView(id = R.id.tv_content)
    private TextView txtContent;

    //售房标签：
    @FieldView(id = R.id.tv_shop_sell_flag)
    private TextView txtSellFlag;
    //是否分割：
    @FieldView(id = R.id.tv_shop_division)
    private TextView txtDivision;
    //是否包含物业费：
    @FieldView(id = R.id.tv_shop_contain_pay)
    private TextView txtContainPay;
    //物业公司：
    @FieldView(id = R.id.tv_shop_manager_company)
    private TextView txtManagerCompany;
    //商铺配套：
    @FieldView(id = R.id.tv_shop_ancillary_facility)
    private TextView txtAncillaryFacility;
    //配套设施：
    @FieldView(id = R.id.tv_shop_unit_fac)
    private TextView txtUnitFac;
    //经营类别：
    @FieldView(id = R.id.tv_shop_operate_category)
    private TextView txtOperateCategory;

    //级别
    @FieldView(id = R.id.ll_shop_level)
    private LinearLayout llLevel;
    //状态/类别
    @FieldView(id = R.id.ll_shop_statue)
    private LinearLayout llStatue;
    //单价/出租方式 朝向/级别/状态
    @FieldView(id = R.id.ll_shop_price_orientation)
    private LinearLayout llPriceOrientation;
    //有无电梯 几梯几户
    @FieldView(id = R.id.ll_shop_automatic)
    private LinearLayout llAutomatic;
    //售房标签
    @FieldView(id = R.id.ll_shop_sell_flag)
    private LinearLayout llSellFlag;
    //是否分割  是否包含物业费
    @FieldView(id = R.id.ll_shop_rent_info1)
    private LinearLayout llRentInfo1;
    //是否包含物业费
    @FieldView(id = R.id.ll_shop_contain_pay)
    private LinearLayout llContainPay;
    //物业公司
    @FieldView(id = R.id.ll_shop_manager_company)
    private LinearLayout llManagerCompany;
    //商铺配套
    @FieldView(id = R.id.ll_shop_devices)
    private LinearLayout llDevices;
    //配套设施
    @FieldView(id = R.id.ll_shop_home_devices)
    private LinearLayout llHomeDevices;
    //经营类别
    @FieldView(id = R.id.ll_shop_operate_category)
    private LinearLayout llOperateCategory;
    //底部信息
    @FieldView(id = R.id.ll_person)
    private LinearLayout llPerson;
    private String token;

    @Override
    protected void initView() {
        //usertype == 0 表示用户，显示下面联系方式
        if (!ParamUtils.isNull(UserBean.getInstance(mContext))) {
            llPerson.setVisibility("0".equals(UserBean.getInstance(mContext).userType) ? View.VISIBLE : View.GONE);
        }

        token = SettingUtils.get(mContext, "token", null);
        userBean = cacheManager.getBean(getIntentExtraStr("useruuid"), UserBean.class);

        if (!ParamUtils.isNull(userBean)) {
            setBrokerInfo();
        } else {
            UserPersonalParam userPersonalParam = new UserPersonalParam();
            userPersonalParam.setId(getIntentExtraStr("useruuid"));
            userPersonalParam.setTag(TAG);
            apiManager.getUserDetail(userPersonalParam);
        }

        String uuid = getIntentExtraStr("uuid");
        item = cacheManager.getBean(uuid, RentItem.class);
        cacheManager.removeBean(uuid);

        //小区名称
        if (!ParamUtils.isEmpty(item.getCommuntityName())) {
            setCenterText(item.getCommuntityName());
        }
        //设置右上角图标
        setRtImg(ParamUtils.isEmpty(item.getSid()) || "".equals(item.getSid()) ? R.drawable.item_collect_normal : R.drawable.item_collect_press);

        //照片
        if (!ParamUtils.isEmpty(item.getPhoto())) {
            final String[] photoArray = item.getPhoto().split(",");
            List<ImageView> imageViewList = new ArrayList<>();
            if (!ParamUtils.isEmpty(photoArray)) {
                for (String value : photoArray) {
                    ImageView imageView = new ImageView(mContext);
                    //设置图片的填充方式
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Glide.with(mContext).load(value).placeholder(R.drawable.default_banner).into(imageView);
                    imageViewList.add(imageView);
                }
                //设置缓存页面
                vpPhoto.setOffscreenPageLimit(photoArray.length);
                vpPhoto.setAdapter(new PhotoPagerAdapter(mContext, imageViewList, photoArray));
//                indicator.setViewPager(vpPhoto);
            }
        }

        initNormalFields();
        initSpecialFields();
    }

    private void setBrokerInfo() {
        if (!ParamUtils.isNull(UserBean.getInstance(mContext)) && UserBean.getInstance(mContext).id.equals(userBean.id)) {//我的店铺
            llPerson.setVisibility(View.GONE);
            //名称
            txtName.setText(UserBean.getInstance((Context) LemonContext.getBean("mContext")).name);
            //星级
            rbStar.setStar((UserBean.getInstance((Context) LemonContext.getBean("mContext"))).star);
            //头像
            Glide.with(mContext).load(UserBean.getInstance((Context) LemonContext.getBean("mContext")).photo).placeholder(R.drawable.default_avatar).into(imgAvatar);
        } else {//其他经纪人的店铺
            //名称
            txtName.setText(userBean.name);
            //星级
            rbStar.setStar(userBean.star);
            //头像
            Glide.with(mContext).load(userBean.photo).placeholder(R.drawable.default_avatar).into(imgAvatar);
        }
    }

    private void initNormalFields() {
        //标题
        if (!ParamUtils.isEmpty(item.getTitle())) {
            txtTitle.setText(item.getTitle());
        }
        //时间
        if (!ParamUtils.isEmpty(item.getLastUpdateTime())) {
            txtTime.setText(item.getLastUpdateTime());
        }
        //区域-片区
        if (ParamUtils.isEmpty(item.getRegionName()) && !ParamUtils.isEmpty(item.getServerAreaName())) {
            txtHouse.setText(item.getServerAreaName());
        }
        if (!ParamUtils.isEmpty(item.getRegionName()) && ParamUtils.isEmpty(item.getServerAreaName())) {
            txtHouse.setText(item.getRegionName());
        }
        if (!ParamUtils.isEmpty(item.getRegionName()) && !ParamUtils.isEmpty(item.getServerAreaName())) {
            txtHouse.setText(item.getRegionName() + "-" + item.getServerAreaName());
        }

        //级别
        if (!ParamUtils.isEmpty(item.getOfficeLevelName())) {
            txtLevel.setText(item.getOfficeLevelName());
        }
        //状态
        if (!ParamUtils.isEmpty(item.getShopStateName())) {
            txtStatue.setText(item.getShopStateName());
        }
        //类别
        if (!ParamUtils.isEmpty(item.getShopCategoryName())) {
            txtType.setText(item.getShopCategoryName());
        }
        //装修：
        if (!ParamUtils.isEmpty(item.getDecorationLevelName())) {
            txtRenovate.setText(item.getDecorationLevelName());
        }
        //楼层：
        if (!ParamUtils.isEmpty(item.getFloor())) {
            txtFloor.setText(item.getFloor());
        }
        //建筑年代：
        txtYear.setText(ParamUtils.isEmpty(item.getAge()) ? "" : item.getAge() + "年");
        //有无电梯：
        if (!ParamUtils.isEmpty(item.getLift())) {
            txtAutomatic.setText(item.getLift().equals("0") ? "无" : "有");
        }
        //几梯几户：
        if (!ParamUtils.isEmpty(item.getLiftType())) {
            txtFac.setText(getLiftTypeValue(item.getLiftType()));
        }

        //售房标签：
        if (!ParamUtils.isEmpty(item.getSellTag())) {
            txtSellFlag.setText(convertSellTag(item.getSellTag()));
        }
        //是否分割：
        if (ParamUtils.isEmpty(item.getCutEnabled()) || item.getCutEnabled().equals("false")) {
            txtDivision.setText("不可分割");
        } else {
            txtDivision.setText("可分割");
        }
        //是否包含物业费：
        if (!ParamUtils.isEmpty(item.getPropertyCost())) {
            txtContainPay.setText("0".equals(item.getPropertyCost()) ? "否" : "是");
        }
        //物业公司：
        if (!ParamUtils.isEmpty(item.getPropertyCompany())) {
            txtManagerCompany.setText(item.getPropertyCompany());
        }
        //商铺配套：
        if (!ParamUtils.isEmpty(item.getProvides())) {
            txtAncillaryFacility.setText(convertSPPTName(item.getProvides()));
        }
        //配套设施：
        if (!ParamUtils.isEmpty(item.getProvides())) {
            txtUnitFac.setText(convertProvidesName(item.getProvides()));
        }
        //经营类别：
        if (!ParamUtils.isEmpty(item.getShopTag())) {
            txtOperateCategory.setText(convertShopTagName(item.getShopTag()));
        }

        //房源地址：
        if (!ParamUtils.isEmpty(item.getAddress())) {
            txtAddress.setText(item.getAddress());
        }
        //房源描述：
        if (!ParamUtils.isEmpty(item.getRemark())) {
            txtContent.setText(item.getRemark());
        }
    }

    private void initSpecialFields() {
        //房源类型：1.住宅、2.别墅、3.写字楼、4.商铺
        if (item.getResourceType().equals("1")) {//住宅
            txtInfo1.setText(R.string.rental);
            txtInfoValue1.setText(item.getRental() + "元/月");
            txtInfo2.setText(R.string.layout);
            txtInfoValue2.setText(ToolUtil.setHouseLayout(item.getLayout()));
            txtInfo3.setText(R.string.area);
            txtInfoValue3.setText(item.getArea() + "㎡");

            txtPriceKey.setText(R.string.rent_way);
            txtPriceValue.setText(item.getRentTypeName());
            txtOrientationKey.setText(R.string.orientation);
            txtOrientationValue.setText(item.getDirectionName());
            txtPropertyKey.setText(R.string.pay_way);
            txtPropertyValue.setText(item.getPaymentTypeName());

            llRentInfo1.setVisibility(View.GONE);
            llManagerCompany.setVisibility(View.GONE);
            llSellFlag.setVisibility(View.GONE);
            llDevices.setVisibility(View.GONE);
            llOperateCategory.setVisibility(View.GONE);
        } else if (item.getResourceType().equals("2")) {//别墅
            txtInfo1.setText(R.string.rental);
            txtInfoValue1.setText(item.getRental() + "元/月");
            txtInfo2.setText(R.string.layout);
            txtInfoValue2.setText(ToolUtil.setHouseLayout(item.getLayout()));
            txtInfo3.setText(R.string.area);
            txtInfoValue3.setText(item.getArea() + "㎡");

            if (!ParamUtils.isEmpty(item.getFloor())) {
                txtFloor.setText("共" + item.getFloor().substring(1) + "层");
            }
            txtPriceKey.setText(R.string.rent_way);
            txtPriceValue.setText(item.getRentTypeName());
            txtOrientationKey.setText(R.string.orientation);
            txtOrientationValue.setText(item.getDirectionName());
            txtPropertyKey.setText(R.string.pay_way);
            txtPropertyValue.setText(item.getPaymentTypeName());

            llAutomatic.setVisibility(View.GONE);
            llRentInfo1.setVisibility(View.GONE);
            llManagerCompany.setVisibility(View.GONE);
            llSellFlag.setVisibility(View.GONE);
            llDevices.setVisibility(View.GONE);
            llOperateCategory.setVisibility(View.GONE);
        } else if (item.getResourceType().equals("3")) {//写字楼
            txtInfo1.setText(R.string.rental);
            txtInfoValue1.setText(item.getRental() + "元/月");
            txtInfo2.setText(R.string.shop_type);
            txtInfoValue2.setText(item.getOfficeTypeName());
            txtInfo3.setText(R.string.area);
            txtInfoValue3.setText(item.getArea() + "㎡");

            txtPropertyKey.setText(R.string.pay_way);
            txtPropertyValue.setText(item.getRentalWayName());

            llLevel.setVisibility(View.VISIBLE);
            llPriceOrientation.setVisibility(View.GONE);
            llAutomatic.setVisibility(View.GONE);
            llSellFlag.setVisibility(View.GONE);
            llDevices.setVisibility(View.GONE);
            llHomeDevices.setVisibility(View.GONE);
            llOperateCategory.setVisibility(View.GONE);
        } else if (item.getResourceType().equals("4")) {//商铺
            txtInfo1.setText(R.string.rental);
            txtInfoValue1.setText(item.getRental() + "元/月");
            txtInfo2.setText(R.string.shop_type);
            txtInfoValue2.setText(item.getShopTypeName());
            txtInfo3.setText(R.string.area);
            txtInfoValue3.setText(item.getArea() + "㎡");

            txtPropertyKey.setText(R.string.pay_way);
            txtPropertyValue.setText(item.getRentalWayName());

            llStatue.setVisibility(View.VISIBLE);
            llPriceOrientation.setVisibility(View.GONE);
            llAutomatic.setVisibility(View.GONE);
            llContainPay.setVisibility(View.GONE);
            llManagerCompany.setVisibility(View.GONE);
            llSellFlag.setVisibility(View.GONE);
            llHomeDevices.setVisibility(View.GONE);
        }
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        finish();
    }

    /**
     * 收藏
     */
    @OnClick(id = R.id.img_title_rt, anonymonus = false)
    public void collectClick() {
        if (ParamUtils.isEmpty(item.getSid()) || "".equals(item.getSid())) {//当前无收藏——收藏
            ConcernHouseParam param = new ConcernHouseParam();
            param.setToken(token);
            param.setSolicitideId(item.getId());
            apiManager.concernHouse(param);
        } else {//当前有收藏——取消收藏
            DeleteConcernParam deleteConcernParam = new DeleteConcernParam();
            deleteConcernParam.setToken(token);
            deleteConcernParam.setSid(item.getSid());
            deleteConcernParam.setTag(TAG);
            apiManager.deleteConcern(deleteConcernParam);
        }
    }

    /**
     * 电话咨询
     */
    @OnClick(id = R.id.ll_phone)
    public void phoneClick() {
        PhoneUtil.callPhones(mContext, userBean.phone);
    }

    /**
     * 微聊
     */
    @OnClick(id = R.id.ll_wechat, anonymonus = false)
    public void chatClick() {
        ChatActivity.launch(mContext, 2, userBean.id, userBean.name, userBean.photo);
    }

    /**
     * 收藏房源
     *
     * @param result
     */
    public void onEventMainThread(ConcernHouseResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            //修改状态是为了能够再次点击
            item.setSid("1");
            setRtImg(R.drawable.item_collect_press);
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

    /**
     * 取消收藏房源
     *
     * @param result
     */
    public void onEventMainThread(DeleteConcernResult result) {
        if (!((DeleteConcernParam) result.getParam()).getTag().equals(TAG)) {
            return;
        }
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            item.setSid("");
            setRtImg(R.drawable.item_collect_normal);
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

    /**
     * 查看用户详细信息
     *
     * @param result
     */
    public void onEventMainThread(UserPersonalResult result) {
        if (!((UserPersonalParam) result.getParam()).getTag().equals(TAG)) {
            return;
        }
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            userBean = result.getData();
            setBrokerInfo();
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

    /**
     * 初始化几梯几户
     *
     * @param value
     * @return
     */
    private String getLiftTypeValue(String value) {
        if (ParamUtils.isEmpty(value)) {
            return "";
        }
        String resultValue = "";
        String[] result = value.split(",");
        if (result.length == 2 && !ParamUtils.isEmpty(result[0]) && !ParamUtils.isEmpty(result[1])) {
            resultValue = Integer.valueOf(result[0]) + "梯" + Integer.valueOf(result[1]) + "户";
        }
        return resultValue;
    }

}
