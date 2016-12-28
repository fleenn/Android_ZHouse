package com.zfb.house.ui;

import android.content.Intent;

import com.lemon.LemonActivity;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.zfb.house.R;
import com.zfb.house.model.bean.ReleaseHousePram;
import com.zfb.house.model.bean.UserBean;

/**
 * 发布房源（经纪人） or 业主委托（用户）
 * Created by linwb on 16/6/6.
 */
@Layout(id = R.layout.activity_release_housing)
public class ReleaseHousingActivity extends LemonActivity {

    public static final int HOUSING_SELL = 0;//住宅出售
    public static final int OFFICE_SELL = 1;//写字楼出售
    public static final int SHOP_SELL = 2;//商铺出售
    public static final int VILLA_SELL = 3;//别墅出售

    public static final int HOUSING_RENT = 10;//住宅出租
    public static final int OFFICE_RENT = 20;//写字楼出租
    public static final int SHOP_RENT = 30;//商铺出租
    public static final int VILLA_RENT = 40;//别墅出租
    //角色
    private String userType = "0";

    @Override
    protected void initView() {
        setCenterText(R.string.release_house_title);
    }

    @Override
    protected void initData() {
        userType = UserBean.getInstance(mContext).userType;
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        finish();
    }

    /**
     * 住宅出售
     */
    @OnClick(id = R.id.txt_housing_sell_residential)
    public void intentSellResidential() {
        toIntent(HOUSING_SELL);
    }

    /**
     * 写字楼出售
     */
    @OnClick(id = R.id.txt_housing_sell_office)
    public void intentSellOffice() {
        toIntent(OFFICE_SELL);
    }

    /**
     * 商铺出售
     */
    @OnClick(id = R.id.txt_housing_sell_shop)
    public void intentSellShop() {
        toIntent(SHOP_SELL);
    }

    /**
     * 别墅出售
     */
    @OnClick(id = R.id.txt_housing_sell_villa)
    public void intentSellVilla() {
        toIntent(VILLA_SELL);
    }

    /**
     * 住宅出租
     */
    @OnClick(id = R.id.txt_housing_rent_residential)
    public void intentRentResidential() {
        toIntent(HOUSING_RENT);
    }

    /**
     * 写字楼出租
     */
    @OnClick(id = R.id.txt_housing_rent_office)
    public void intentRentOffice() {
        toIntent(OFFICE_RENT);
    }

    /**
     * 商铺出租
     */
    @OnClick(id = R.id.txt_housing_rent_shop)
    public void intentRentShop() {
        toIntent(SHOP_RENT);
    }

    /**
     * 别墅出租
     */
    @OnClick(id = R.id.txt_housing_rent_villa)
    public void intentRentVilla() {
        toIntent(VILLA_RENT);
    }

    private void toIntent(int tag) {
        Class<?> cls = null;
        if (userType.equals("0")) {//用户
            cls = UserResidentialSellActivity.class;
        } else {//经纪人
            cls = BrokerResidentialSellActivity.class;
        }
        Intent intent = new Intent(this, cls);
        intent.putExtra("tag", tag);
        startActivity(intent);
    }

}
