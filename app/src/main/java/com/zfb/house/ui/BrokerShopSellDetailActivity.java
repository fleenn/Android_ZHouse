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
import com.zfb.house.model.bean.SellItem;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.model.param.ConcernHouseParam;
import com.zfb.house.model.param.DeleteConcernParam;
import com.zfb.house.model.param.UserPersonalParam;
import com.zfb.house.model.result.ConcernHouseResult;
import com.zfb.house.model.result.DeleteConcernResult;
import com.zfb.house.model.result.UserPersonalResult;
import com.zfb.house.util.ToolUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称:  [zfbandroid]
 * 包:        [com.zfb.house.ui]
 * 类描述:    [经纪人店铺出售房源详情界面]
 * 创建人:    [xflu]
 * 创建时间:  [2016/6/3 10:06]
 * 修改人:    [xflu]
 * 修改时间:  [2016/6/3 10:06]
 * 修改备注:  [说明本次修改内容]
 * 版本:      [v1.0]
 */
@Layout(id = R.layout.activity_broker_shop_rent_detail)
public class BrokerShopSellDetailActivity extends LemonActivity {

    private static final String TAG = "BrokerShopSellDetailActivity";
    //出售房源实体
    private SellItem item;
    //当前经纪人实体
    private UserBean userBean;
    @FieldView(id = R.id.vp_photo)
    private ViewPager vpPhoto;
//    @FieldView(id = R.id.indicator)
//    private CirclePageIndicator indicator;

    //标题
    @FieldView(id = R.id.tv_shop_title)
    private TextView tvTitle;
    //时间
    @FieldView(id = R.id.tv_shop_time)
    private TextView tvTime;
    //区域-片区
    @FieldView(id = R.id.tv_shop_house)
    private TextView tvHouse;
    //头像
    @FieldView(id = R.id.im_avatar)
    private ImageView im_avatar;
    //名称
    @FieldView(id = R.id.tv_name)
    private TextView tv_name;
    //星级
    @FieldView(id = R.id.rating_star)
    private RatingBar rbStar;

    @FieldView(id = R.id.tv_main_info1)
    private TextView tv_main_info;
    @FieldView(id = R.id.tv_main_info_value)
    private TextView tv_main_info_value;
    @FieldView(id = R.id.tv_main_info2)
    private TextView tv_main_info2;
    @FieldView(id = R.id.tv_main_info2_value)
    private TextView tv_main_info2_value;
    @FieldView(id = R.id.tv_main_info3)
    private TextView tv_main_info3;
    @FieldView(id = R.id.tv_main_info3_value)
    private TextView tv_main_info3_value;

    //级别
    @FieldView(id = R.id.tv_shop_level)
    private TextView tvLevel;
    //状态
    @FieldView(id = R.id.tv_shop_statue)
    private TextView tvStatue;
    // 类别
    @FieldView(id = R.id.tv_shop_type)
    private TextView tvType;
    //单价/出租方式：
    @FieldView(id = R.id.tv_shop_price_key)
    private TextView tvPriceKey;
    //单价/出租方式——值：
    @FieldView(id = R.id.tv_shop_price_value)
    private TextView tvPriceValue;
    //朝向/级别/状态：
    @FieldView(id = R.id.tv_shop_orientation_key)
    private TextView tvOrientationKey;
    //朝向/级别/状态——值：
    @FieldView(id = R.id.tv_shop_orientation_value)
    private TextView tvOrientationValue;
    //装修：
    @FieldView(id = R.id.tv_shop_unit_renovate)
    private TextView tvRenovate;
    //楼层：
    @FieldView(id = R.id.tv_shop_unit_floor)
    private TextView tvFloor;
    @FieldView(id = R.id.tv_shop_unit_year)
    private TextView tvYear;//建筑年代：
    //产权性质/支付方式：
    @FieldView(id = R.id.tv_shop_property_key)
    private TextView tvPropertyKey;
    //产权性质/支付方式——值：
    @FieldView(id = R.id.tv_shop_property_value)
    private TextView tvPropertyValue;
    //有无电梯：
    @FieldView(id = R.id.tv_shop_automatic)
    private TextView tvAutomatic;
    //几梯几户：
    @FieldView(id = R.id.tv_shop_unit_home)
    private TextView tvFac;
    //房源地址：
    @FieldView(id = R.id.tv_shop_address)
    private TextView tvAddress;
    //房源描述：
    @FieldView(id = R.id.tv_content)
    private TextView tvContent;

    //售房标签：
    @FieldView(id = R.id.tv_shop_sell_flag)
    private TextView tv_shop_sell_flag;
    //是否分割：
    @FieldView(id = R.id.tv_shop_division)
    private TextView tv_shop_division;
    //是否包含物业费：
    @FieldView(id = R.id.tv_shop_contain_pay)
    private TextView tv_shop_contain_pay;
    //物业公司：
    @FieldView(id = R.id.tv_shop_manager_company)
    private TextView tv_shop_manager_company;
    //商铺配套：
    @FieldView(id = R.id.tv_shop_ancillary_facility)
    private TextView tv_shop_ancillary_facility;
    //配套设施：
    @FieldView(id = R.id.tv_shop_unit_fac)
    private TextView tv_shop_unit_fac;
    //经营类别：
    @FieldView(id = R.id.tv_shop_operate_category)
    private TextView tv_shop_operate_category;

    //有无电梯 几梯几户
    @FieldView(id = R.id.ll_shop_automatic)
    private LinearLayout ll_shop_automatic;
    //售房标签
    @FieldView(id = R.id.ll_shop_sell_flag)
    private LinearLayout ll_shop_sell_flag;
    //是否分割  是否包含物业费
    @FieldView(id = R.id.ll_shop_rent_info1)
    private LinearLayout ll_shop_rent_info1;
    //物业公司
    @FieldView(id = R.id.ll_shop_manager_company)
    private LinearLayout ll_shop_manager_company;
    //商铺配套
    @FieldView(id = R.id.ll_shop_devices)
    private LinearLayout ll_shop_devices;
    //配套设施
    @FieldView(id = R.id.ll_shop_home_devices)
    private LinearLayout ll_shop_home_devices;
    //经营类别
    @FieldView(id = R.id.ll_shop_operate_category)
    private LinearLayout ll_shop_operate_category;
    //底部信息
    @FieldView(id = R.id.ll_person)
    private LinearLayout ll_person;
    private String token;

    @Override
    protected void initView() {
        //usertype == 0 表示用户，显示下面联系方式
        if (!ParamUtils.isNull(UserBean.getInstance(mContext))) {
            ll_person.setVisibility("0".equals(UserBean.getInstance(mContext).userType) ? View.VISIBLE : View.GONE);
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

        item = cacheManager.getBean(getIntentExtraStr("uuid"), SellItem.class);

        //小区名称
        if (!ParamUtils.isEmpty(item.getCommuntityName())) {
            setCenterText(item.getCommuntityName());
        }
        //设置右上角图标
        setRtImg(ParamUtils.isEmpty(item.getSid()) || "".equals(item.getSid()) ? R.drawable.item_collect_normal : R.drawable.item_collect_press);

        //照片
        if (!ParamUtils.isEmpty(item.getPhoto())) {
            String[] photoArray = item.getPhoto().split(",");
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
            ll_person.setVisibility(View.GONE);
            //名称
            tv_name.setText(UserBean.getInstance((Context) LemonContext.getBean("mContext")).name);
            //星级
            rbStar.setStar((UserBean.getInstance((Context) LemonContext.getBean("mContext"))).star);

            Glide.with(mContext).load(UserBean.getInstance((Context) LemonContext.getBean("mContext")).photo).placeholder(R.drawable.default_avatar).into(im_avatar);
        } else {//其他经纪人的店铺
            //名称
            tv_name.setText(userBean.name);
            //星级
            rbStar.setStar(userBean.star);
            //头像
            Glide.with(mContext).load(userBean.photo).placeholder(R.drawable.default_avatar).into(im_avatar);
        }
    }

    private void initNormalFields() {
        //标题
        if (!ParamUtils.isEmpty(item.getTitle())) {
            tvTitle.setText(item.getTitle());
        }
        //时间
        if (!ParamUtils.isEmpty(item.getLastUpdateTime())) {
            tvTime.setText(item.getLastUpdateTime());
        }
        //区域-片区
        if (ParamUtils.isEmpty(item.getRegionName()) && !ParamUtils.isEmpty(item.getServerAreaName())) {
            tvHouse.setText(item.getServerAreaName());
        }
        if (!ParamUtils.isEmpty(item.getRegionName()) && ParamUtils.isEmpty(item.getServerAreaName())) {
            tvHouse.setText(item.getRegionName());
        }
        if (!ParamUtils.isEmpty(item.getRegionName()) && !ParamUtils.isEmpty(item.getServerAreaName())) {
            tvHouse.setText(item.getRegionName() + "-" + item.getServerAreaName());
        }

        //级别
        if (!ParamUtils.isEmpty(item.getOfficeLevelName())) {
            tvLevel.setText(item.getOfficeLevelName());
        }
        //状态
        if (!ParamUtils.isEmpty(item.getShopStateName())) {
            tvStatue.setText(item.getShopStateName());
        }
        //类别
        if (!ParamUtils.isEmpty(item.getShopCategoryName())) {
            tvType.setText(item.getShopCategoryName());
        }
        //装修：
        if (!ParamUtils.isEmpty(item.getDecorationLevelName())) {
            tvRenovate.setText(item.getDecorationLevelName());
        }
        //楼层：
        if (!ParamUtils.isEmpty(item.getFloor())) {
            tvFloor.setText(item.getFloor());
        }
        //建筑年代：
        tvYear.setText(ParamUtils.isEmpty(item.getAge()) ? "" : item.getAge() + "年");
        //有无电梯：
        if (!ParamUtils.isEmpty(item.getLift())) {
            tvAutomatic.setText(item.getLift().equals("0") ? "无" : "有");
        }
        //几梯几户：
        if (!ParamUtils.isEmpty(item.getLiftType())) {
            tvFac.setText(getLiftTypeValue(item.getLiftType()));
        }

        //售房标签：
        if (!ParamUtils.isEmpty(item.getSellTag())) {
            tv_shop_sell_flag.setText(convertSellTag(item.getSellTag()));
        }
        //是否分割：
        if (!ParamUtils.isEmpty(item.getIsCutEnabled())) {
            tv_shop_division.setText(item.getIsCutEnabled());
        }
        //是否包含物业费：
        if (!ParamUtils.isEmpty(item.getPropertyCost())) {
            tv_shop_contain_pay.setText("0".equals(item.getPropertyCost()) ? "否" : "是");
        }
        //物业公司：
        if (!ParamUtils.isEmpty(item.getPropertyCompany())) {
            tv_shop_manager_company.setText(item.getPropertyCompany());
        }
        //商铺配套：
        if (!ParamUtils.isEmpty(item.getProvides())) {
            tv_shop_ancillary_facility.setText(ToolUtil.convertDot(item.getProvidesName()));
        }
        //配套设施：
        if (!ParamUtils.isEmpty(item.getProvides())) {
            tv_shop_unit_fac.setText(convertProvidesName(item.getProvides()));
        }
        //经营类别：
        if (!ParamUtils.isEmpty(item.getShopTag())) {
            tv_shop_operate_category.setText(convertShopTagName(item.getShopTag()));
        }

        //房源地址：
        if (!ParamUtils.isEmpty(item.getAddress())) {
            tvAddress.setText(item.getAddress());
        }
        //房源描述：
        if (!ParamUtils.isEmpty(item.getRemark())) {
            tvContent.setText(item.getRemark());
        }
    }

    private void initSpecialFields() {
        //房源类型：1.住宅、2.别墅、3.写字楼、4.商铺
        if (item.getResourceType().equals("1")) {//住宅
            tv_main_info.setText(R.string.price);
            tv_main_info_value.setText(item.getWishPrice() + "万");
            tv_main_info2.setText(R.string.layout);
            tv_main_info2_value.setText(ToolUtil.setHouseLayout(item.getLayout()));
            tv_main_info3.setText(R.string.area);
            tv_main_info3_value.setText(item.getArea() + "㎡");

            tvPriceKey.setText(R.string.unit_price);
            tvPriceValue.setText(new DecimalFormat("#.00").format(Float.parseFloat(item.getWishPrice()) / Float.parseFloat(item.getArea()) * 100) + "元/㎡");
            tvOrientationKey.setText(R.string.orientation);
            tvOrientationValue.setText(item.getDirectionName());
            tvPropertyKey.setText(R.string.property);
            tvPropertyValue.setText(item.getPropertyRightTypeName());

            ll_shop_rent_info1.setVisibility(View.GONE);
            ll_shop_manager_company.setVisibility(View.GONE);
            ll_shop_devices.setVisibility(View.GONE);
            ll_shop_operate_category.setVisibility(View.GONE);
        } else if (item.getResourceType().equals("2")) {//别墅
            tv_main_info.setText(R.string.price);
            tv_main_info_value.setText(item.getWishPrice() + "万");
            tv_main_info2.setText(R.string.layout);
            tv_main_info2_value.setText(ToolUtil.setHouseLayout(item.getLayout()));
            tv_main_info3.setText(R.string.area);
            tv_main_info3_value.setText(item.getArea() + "㎡");

            if (!ParamUtils.isEmpty(item.getFloor())) {
                tvFloor.setText("共" + item.getFloor().substring(1) + "层");
            }
            tvPriceKey.setText(R.string.unit_price);
            tvPriceValue.setText(new DecimalFormat("#.00").format(Float.parseFloat(item.getWishPrice()) / Float.parseFloat(item.getArea()) * 100) + "元/㎡");
            tvOrientationKey.setText(R.string.orientation);
            tvOrientationValue.setText(item.getDirectionName());
            tvPropertyKey.setText(R.string.property);
            tvPropertyValue.setText(item.getPropertyRightTypeName());

            ll_shop_automatic.setVisibility(View.GONE);
            ll_shop_rent_info1.setVisibility(View.GONE);
            ll_shop_manager_company.setVisibility(View.GONE);
            ll_shop_devices.setVisibility(View.GONE);
            ll_shop_operate_category.setVisibility(View.GONE);
        } else if (item.getResourceType().equals("3")) {//写字楼
            tv_main_info.setText(R.string.price);
            tv_main_info_value.setText(item.getWishPrice() + "万");
            tv_main_info2.setText(R.string.shop_type);
            tv_main_info2_value.setText(item.getOfficeTypeName());
            tv_main_info3.setText(R.string.area);
            tv_main_info3_value.setText(item.getArea() + "㎡");

            tvPriceKey.setText(R.string.unit_price);
            tvPriceValue.setText(new DecimalFormat("#.00").format(Float.parseFloat(item.getWishPrice()) / Float.parseFloat(item.getArea()) * 100) + "元/㎡");
            tvOrientationKey.setText(R.string.level);
            tvOrientationValue.setText(item.getOfficeLevelName());
            tvPropertyKey.setText(R.string.property);
            tvPropertyValue.setText(item.getPropertyRightTypeName());

            ll_shop_automatic.setVisibility(View.GONE);
            ll_shop_rent_info1.setVisibility(View.GONE);
            ll_shop_sell_flag.setVisibility(View.GONE);
            ll_shop_devices.setVisibility(View.GONE);
            ll_shop_home_devices.setVisibility(View.GONE);
            ll_shop_operate_category.setVisibility(View.GONE);
        } else if (item.getResourceType().equals("4")) {//商铺
            tv_main_info.setText(R.string.price);
            tv_main_info_value.setText(item.getWishPrice() + "万");
            tv_main_info2.setText(R.string.shop_type);
            tv_main_info2_value.setText(item.getShopTypeName());
            tv_main_info3.setText(R.string.area);
            tv_main_info3_value.setText(item.getArea() + "㎡");
            ll_shop_manager_company.setVisibility(View.GONE);
            tvPriceKey.setText(R.string.unit_price);
            tvPriceValue.setText(new DecimalFormat("#.00").format(Float.parseFloat(item.getWishPrice()) / Float.parseFloat(item.getArea()) * 100) + "元/㎡");
            tvOrientationKey.setText(R.string.statue);
            tvOrientationValue.setText(item.getShopStateName());
            tvPropertyKey.setText(R.string.property);
            tvPropertyValue.setText(item.getPropertyRightTypeName());

            ll_shop_automatic.setVisibility(View.GONE);
            ll_shop_rent_info1.setVisibility(View.GONE);
            ll_shop_sell_flag.setVisibility(View.GONE);
            ll_shop_home_devices.setVisibility(View.GONE);
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
