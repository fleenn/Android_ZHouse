package com.zfb.house.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Matrix;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.emchat.Constant;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.lemon.LemonActivity;
import com.lemon.LemonLocation;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.model.StatusCode;
import com.lemon.util.ParamUtils;
import com.lemon.util.PhoneUtil;
import com.lemon.util.ScreenUtil;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.ShopAdapter;
import com.zfb.house.component.RatingBar;
import com.zfb.house.emchat.ChatActivity;
import com.zfb.house.emchat.temp.CertificationBean;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.model.param.ConcernParam;
import com.zfb.house.model.param.DeleteConcernParam;
import com.zfb.house.model.param.UserPersonalParam;
import com.zfb.house.model.result.ConcernResult;
import com.zfb.house.model.result.DeleteConcernResult;
import com.zfb.house.model.result.UserPersonalResult;
import com.zfb.house.util.ToolUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称:  [zfbandroid]
 * 包:        [com.zfb.house.ui]
 * 类描述:    [经纪人店铺]
 * 创建人:    [xflu]
 * 创建时间:  [2016/6/3 10:06]
 * 修改人:    [xflu]
 * 修改时间:  [2016/6/3 10:06]
 * 修改备注:  [说明本次修改内容]
 * 版本:      [v1.0]
 */
@Layout(id = R.layout.activity_broker_shop)
public class BrokerShopActivity extends LemonActivity {
    private static final String TAG = "BrokerShopActivity";

    @FieldView(id = R.id.vp_shop)
    private ViewPager vpShop;
    @FieldView(id = R.id.img_cursor)
    private ImageView imgCursor;
    //出售文字
    @FieldView(id = R.id.txt_sell)
    private TextView txtSell;
    //出租文字
    @FieldView(id = R.id.txt_rent)
    private TextView txtRent;

    //头像
    @FieldView(id = R.id.img_avatar)
    private ImageView imgAvatar;
    //星级
    @FieldView(id = R.id.rb_star)
    private RatingBar rbStar;
    //姓名
    @FieldView(id = R.id.txt_name)
    private TextView txtName;
    //经纪人专家类型
    @FieldView(id = R.id.txt_type)
    private TextView txtExpertType;
    //所属公司
    @FieldView(id = R.id.txt_company)
    private TextView txtCompany;
    //距离
    @FieldView(id = R.id.txt_range)
    private TextView txtRange;
    //距离
    @FieldView(id = R.id.tv_distance_label)
    private TextView txtDistanceLabel;
    //服务片区
    @FieldView(id = R.id.txt_district)
    private TextView txtServiceDistrict;
    //服务小区
    @FieldView(id = R.id.txt_village)
    private TextView txtServiceVillage;
    //成交量
    @FieldView(id = R.id.txt_trading)
    private TextView txtTotal;
    //好评率
    @FieldView(id = R.id.txt_satisfy_degree)
    private TextView txtPrise;
    //实名认证图标
    @FieldView(id = R.id.img_real_name)
    private ImageView imgRealName;
    //资质认证图标
    @FieldView(id = R.id.img_qualification)
    private ImageView imgQualification;
    //名片认证图标
    @FieldView(id = R.id.img_card)
    private ImageView imgCard;
    //右侧两个按钮——需要隐藏
    @FieldView(id = R.id.rlayout_chat)
    private RelativeLayout rlayoutChat;
    @FieldView(id = R.id.rlayout_operate)
    private RelativeLayout rlayoutOperate;
    //经纪人店铺出租界面
    private BrokerShopRentRefreshFragment rentFragment;
    //经纪人店铺出售界面
    private BrokerShopSellRefreshFragment sellFragment;
    //底部选项
    @FieldView(id = R.id.ll_person)
    private LinearLayout llPerson;
    //关注图标
    @FieldView(id = R.id.img_concern)
    private ImageView imgConcern;
    @FieldView(id = R.id.ll_concern)
    private LinearLayout llConcern;
    private String token;
    //经纪人的ID
    private String brokerId;
    //当前经纪人
    private UserBean userBean;
    private ShopAdapter shopAdapter;
    private int offset = 0;
    private int currIndex = 0;
    private String phone, avatar, username, sid;

    @Override
    protected void initView() {
        //隐藏右侧两个按钮
        rlayoutChat.setVisibility(View.GONE);
        rlayoutOperate.setVisibility(View.GONE);
        txtDistanceLabel.setVisibility(View.GONE);
        txtRange.setVisibility(View.GONE);
        rentFragment = new BrokerShopRentRefreshFragment();
        sellFragment = new BrokerShopSellRefreshFragment();
        brokerId = getIntentExtraStr("id");
        if (!ParamUtils.isNull(UserBean.getInstance(mContext)) && UserBean.getInstance(mContext).userType.equals("1")) {
            llPerson.setVisibility(View.GONE);
        } else {
            llPerson.setVisibility(View.VISIBLE);
        }
        if (ParamUtils.isEmpty(brokerId)) {
            setCenterText(R.string.title_mine_shop);
            rentFragment.setBrokerId(UserBean.getInstance(mContext).id);
            sellFragment.setBrokerId(UserBean.getInstance(mContext).id);
        } else {
            setCenterText(getIntentExtraStr("name") + "的店铺");
            rentFragment.setBrokerId(brokerId);
            sellFragment.setBrokerId(brokerId);
        }

        List<Fragment> fragments = new ArrayList();
        fragments.add(sellFragment);
        fragments.add(rentFragment);
        initCursorPos();
        shopAdapter = new ShopAdapter(getSupportFragmentManager(), fragments);
        vpShop.setAdapter(shopAdapter);
        vpShop.addOnPageChangeListener(new MyPageChangeListener());
        vpShop.setOffscreenPageLimit(2);
    }

    @Override
    protected void initData() {
        token = SettingUtils.get(mContext, "token", null);
        UserPersonalParam param = new UserPersonalParam();
        if (ParamUtils.isEmpty(brokerId)) {//查看自己的店铺
            param.setToken(token);
        } else {//查看其他经纪人的店铺
            param.setToken(token);
            param.setId(brokerId);
        }
        param.setTag(TAG);
        apiManager.getUserDetail(param);
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

    /**
     * 出售
     */
    @OnClick(id = R.id.txt_sell)
    public void sellClick() {
        vpShop.setCurrentItem(0);
    }

    /**
     * 出租
     */
    @OnClick(id = R.id.txt_rent)
    public void rentClick() {
        vpShop.setCurrentItem(1);
    }

    /**
     * 设置距离
     *
     * @param userBean
     * @return
     */
    private String setDistance(UserBean userBean) {
        LatLng mMineLL = new LatLng(SettingUtils.get(mContext, "lat", Constant.DEFAULT_LAT), SettingUtils.get(mContext, "lng", Constant.DEFAULT_LNG));//我的坐标
        LatLng mBrokerLL = new LatLng(userBean.lat, userBean.lng);//经纪人的坐标
        String distance = String.valueOf(LemonLocation.getDistance(mMineLL, mBrokerLL));
        if (distance.length() >= 4) {
            distance = distance.substring(0, 4);
        }
        return distance;
    }

    /**
     * 设置认证图标
     *
     * @param bean
     */
    private void setCertification(UserBean bean) {
        imgRealName.setVisibility(View.GONE);
        imgCard.setVisibility(View.GONE);
        imgQualification.setVisibility(View.GONE);
        if (!ParamUtils.isEmpty(bean.certifications)) {
            for (CertificationBean cb : bean.certifications) {
                if (cb.auditState.equals("2")) {//如果审核通过的话
                    switch (cb.certificationType) {
                        case "1"://实名认证
                            imgRealName.setVisibility(View.VISIBLE);
                            break;
                        case "2"://名片认证
                            imgCard.setVisibility(View.VISIBLE);
                            break;
                        case "3"://资质认证
                            imgQualification.setVisibility(View.VISIBLE);
                            break;
                    }
                } else {
                    switch (cb.certificationType) {
                        case "1"://实名认证
                            imgRealName.setVisibility(View.GONE);
                            break;
                        case "2"://名片认证
                            imgCard.setVisibility(View.GONE);
                            break;
                        case "3"://资质认证
                            imgQualification.setVisibility(View.GONE);
                            break;
                    }
                }
            }
        }
    }

    /**
     * 电话咨询
     */
    @OnClick(id = R.id.ll_phone)
    public void phoneClick() {
        PhoneUtil.callPhones(mContext, phone);
    }

    /**
     * 微聊
     */
    @OnClick(id = R.id.ll_wechat, anonymonus = false)
    public void chatClick() {
        ChatActivity.launch(mContext, 2, brokerId, username, avatar);
    }

    //初始化指示器位置
    public void initCursorPos() {
        // 初始化动画
        if (ScreenUtil.screenWidth == 0) {
            ScreenUtil.init(mContext);
        }
        int screenWidth = ScreenUtil.screenWidth;// 获取分辨率宽度
        offset = screenWidth / 2;// 计算偏移量

        Matrix matrix = new Matrix();
        matrix.postTranslate(0, 0);
        imgCursor.setImageMatrix(matrix);// 设置动画初始位置
    }

    public class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        int one = offset;// 页卡1 -> 页卡2 偏移量

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0://出售
                    changeTitleStyle(0);
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(one, 0, 0, 0);
                    }
                    break;
                case 1://出租
                    changeTitleStyle(1);
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(0, one, 0, 0);
                    }
                    break;
            }
            currIndex = arg0;
            if (null != animation) {
                animation.setFillAfter(true);// True:图片停在动画结束位置
                animation.setDuration(300);
            }
            imgCursor.startAnimation(animation);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(vpShop.getWindowToken(), 0);
        }
    }

    /**
     * 改变小标题文字的大小和颜色
     *
     * @param index
     */
    private void changeTitleStyle(int index) {
        Resources resources = getResources();
        txtSell.setTextColor(index == 0 ? resources.getColor(R.color.my_orange_two) : resources.getColor(R.color.my_gray_one));
        txtRent.setTextColor(index == 1 ? resources.getColor(R.color.my_orange_two) : resources.getColor(R.color.my_gray_one));
        txtSell.setTextSize(index == 0 ? 16 : 14);
        txtRent.setTextSize(index == 1 ? 16 : 14);
    }

    /**
     * 关注经纪人
     */
    @OnClick(id = R.id.ll_concern, anonymonus = false)
    public void concernClick() {
        if (!ParamUtils.isEmpty(sid)) {//如果关注了，点击代表取消关注
            DeleteConcernParam deleteConcernParam = new DeleteConcernParam();
            deleteConcernParam.setToken(token);
            deleteConcernParam.setSid(sid);
            deleteConcernParam.setTag(TAG);
            apiManager.deleteConcern(deleteConcernParam);
        } else {//反之就是关注当前经纪人
            ConcernParam concernParam = new ConcernParam();
            concernParam.setToken(token);
            concernParam.setSolicitideId(brokerId);
            apiManager.saveConcern(concernParam);
        }
        llConcern.setClickable(false);
    }

    /**
     * 经纪人详细信息
     *
     * @param result
     */
    public void onEventMainThread(UserPersonalResult result) {
        if (!((UserPersonalParam) result.getParam()).getTag().equals(TAG)) {
            return;
        }
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            userBean = result.getData();
            sid = userBean.sid;
            rentFragment.setUserBean(userBean);
            sellFragment.setUserBean(userBean);
            phone = userBean.phone;
            username = userBean.name;
            avatar = userBean.photo;
            //头像
            Glide.with(mContext).load(userBean.photo).placeholder(R.drawable.default_avatar).into(imgAvatar);
            //星级
            rbStar.setStar(userBean.star);
            //姓名
            txtName.setText(userBean.name);
            //专家类型
            txtExpertType.setVisibility(ParamUtils.isEmpty(userBean.expertType) ? View.GONE : View.VISIBLE);
            txtExpertType.setText(ToolUtil.setExpertType(userBean.expertType));
            //所属公司
            txtCompany.setText(userBean.companyName);
            //设置距离
            txtRange.setText(setDistance(userBean) + "km");
            //服务片区
            txtServiceDistrict.setText(ToolUtil.convertDot(userBean.serviceDistrictName));
            //服务小区
            txtServiceVillage.setText(ToolUtil.convertDot(userBean.serviceVillageName));
            //成交量
            txtTotal.setText(String.valueOf(Integer.valueOf(userBean.rentVolume) + Integer.valueOf(userBean.sellVolume)) + "套");
            //好评率
            txtPrise.setText((ParamUtils.isEmpty(userBean.satisfyDegree) ? "0" : userBean.satisfyDegree) + "%");
            //设置认证图标
            setCertification(userBean);
            //设置是否关注当前经纪人
            imgConcern.setSelected(ParamUtils.isEmpty(sid) ? false : true);
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

    /**
     * 关注经纪人
     *
     * @param result
     */
    public void onEventMainThread(ConcernResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            sid = result.getData().toString();
            imgConcern.setSelected(true);
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
        llConcern.setClickable(true);
    }

    /**
     * 删除关注经纪人
     *
     * @param result
     */
    public void onEventMainThread(DeleteConcernResult result) {
        if (!((DeleteConcernParam) result.getParam()).getTag().equals(TAG)) {
            return;
        }
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            sid = "";
            imgConcern.setSelected(false);
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
        llConcern.setClickable(true);
    }

}
