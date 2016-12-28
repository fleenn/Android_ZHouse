package com.zfb.house.ui;

import android.content.Intent;

import com.lemon.LemonActivity;
import com.lemon.LemonCacheManager;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.zfb.house.R;
import com.zfb.house.adapter.BannerAdapter;
import com.zfb.house.adapter.InfiniteViewPager;
import com.zfb.house.model.bean.Banner;
import com.zfb.house.model.bean.ReleasePull;
import com.zfb.house.model.param.FinanceBannerParam;
import com.zfb.house.model.result.FinanceBannerResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户首页 -> 售后金融
 * Created by Administrator on 2016/7/13.
 */
@Layout(id = R.layout.activity_after_sales_finance)
public class AfterSalesFinanceActivity extends LemonActivity {

    //轮播图
    @FieldView(id = R.id.infiniteViewPagerFinance)
    private InfiniteViewPager infiniteViewPager;
    private ReleasePull mReleasePull;

    @Override
    protected void initView() {
        setCenterText(R.string.title_after_sales_finance);
    }

    @Override
    protected void initData() {
        LemonCacheManager cacheManager = new LemonCacheManager();
        mReleasePull = cacheManager.getBean(ReleasePull.class);

        //调用“金融banner获取”接口
        FinanceBannerParam financeBannerParam = new FinanceBannerParam();
        apiManager.finacialBanner(financeBannerParam);
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        finish();
    }

    /**
     * 立即预约
     */
    @OnClick(id = R.id.btn_appointment)
    public void toAppointment() {
        Intent intent = new Intent(mContext, FinanceInformationActivity.class);
        intent.putExtra("type", mReleasePull.getFINACIAL_TYPE().get(0).getValue());
        startActivity(intent);
    }

    /**
     * 二手房转按揭担保
     */
    @OnClick(id = R.id.rlayout_mortgage_guarantee)
    public void toMortgageGuarantee() {
        toIntent("二手房转按揭担保", R.drawable.bg_mortgage_guarantee, mReleasePull.getFINACIAL_TYPE().get(1).getValue());
    }

    /**
     * 二手房同名转按
     */
    @OnClick(id = R.id.rlayout_same_name)
    public void toSameName() {
        toIntent("二手房同名转按", R.drawable.bg_same_name, mReleasePull.getFINACIAL_TYPE().get(2).getValue());
    }

    /**
     * 二手房交易垫资
     */
    @OnClick(id = R.id.rlayout_underwritten_transaction)
    public void toUnderwrittenTransaction() {
        toIntent("二手房交易垫资", R.drawable.bg_underwritten_transaction, mReleasePull.getFINACIAL_TYPE().get(3).getValue());
    }

    /**
     * 抵押贷款
     */
    @OnClick(id = R.id.rlayout_mortgage_loan)
    public void toMortgageLoan() {
        toIntent("抵押贷款", R.drawable.bg_mortgage_loan, mReleasePull.getFINACIAL_TYPE().get(4).getValue());
    }

    /**
     * 资金过桥
     */
    @OnClick(id = R.id.rlayout_capital_bridge)
    public void toCapitalBridge() {
        toIntent("资金过桥", R.drawable.bg_capital_bridge, mReleasePull.getFINACIAL_TYPE().get(5).getValue());
    }

    /**
     * 不良资产回收
     */
    @OnClick(id = R.id.rlayout_recovery_assets)
    public void toRecoveryAssets() {
        toIntent("不良资产回收", R.drawable.bg_recovery_assets, mReleasePull.getFINACIAL_TYPE().get(6).getValue());
    }

    /**
     * 跳转方法
     *
     * @param title
     * @param bg
     */
    public void toIntent(String title, int bg, String type) {
        Intent intent = new Intent(mContext, FinanceFunctionActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("bg", bg);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    /**
     * 金融页面banner
     *
     * @param result
     */
    public void onEventMainThread(FinanceBannerResult result) {
        BannerAdapter bannerAdapter = new BannerAdapter(mContext, result.getData().getBanner());
        bannerAdapter.setIsFinance(true);
        infiniteViewPager.setAdapter(bannerAdapter);
        infiniteViewPager.setAutoScrollTime(5000);
        infiniteViewPager.startAutoScroll();
    }

}
