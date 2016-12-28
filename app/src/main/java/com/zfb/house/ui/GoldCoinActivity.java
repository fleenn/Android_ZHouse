package com.zfb.house.ui;

import android.content.Intent;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.android.emchat.Constant;
import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.event.UpdatePointEvent;
import com.lemon.model.StatusCode;
import com.lemon.util.EventUtil;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.BannerAdapter;
import com.zfb.house.adapter.GoodsGridAdapter;
import com.zfb.house.adapter.InfiniteViewPager;
import com.zfb.house.model.bean.Banner;
import com.zfb.house.model.bean.GoodsData;
import com.zfb.house.model.bean.SignData;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.model.param.GoodsListParam;
import com.zfb.house.model.param.SignInParam;
import com.zfb.house.model.result.GoodsListResult;
import com.zfb.house.model.result.SignInResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 金币商城首页
 * Created by HourGlassRemember on 2016/8/4.
 */
@Layout(id = R.layout.activity_gold_coin)
public class GoldCoinActivity extends LemonActivity {

    private static final String TAG = "GoldCoinActivity";

    //轮播图
    @FieldView(id = R.id.infiniteViewPagerCoin)
    private InfiniteViewPager infiniteViewPagerCoin;
    //姓名
    @FieldView(id = R.id.txt_name)
    private TextView txtName;
    //连续签到数量
    @FieldView(id = R.id.txt_sign_number)
    private TextView txtSignNumber;
    //金币数量
    @FieldView(id = R.id.txt_coin_number)
    private TextView txtCoinNumber;
    //签到
    @FieldView(id = R.id.btn_sign)
    private Button btnSign;
    //特别推荐gridview
    @FieldView(id = R.id.gv_recommend)
    private GridView gvCommend;
    //    //特权兑换gridview
//    @FieldView(id = R.id.gv_privilege)
//    private GridView gvPrivilege;
    //商品列表适配器
    private GoodsGridAdapter goodsGridAdapter;
    private String token;

    @Override
    protected void initView() {
        setCenterText(R.string.title_gold_coin);
        setRtImg(R.drawable.shopping_cart);

        token = SettingUtils.get(mContext, "token", null);
        UserBean userBean = UserBean.getInstance(mContext);
        txtName.setText(userBean.name);
        txtCoinNumber.setText(userBean.point + "");

        goodsGridAdapter = new GoodsGridAdapter(mContext);
        goodsGridAdapter.setOnItemClickListener(new GoodsGridAdapter.OnItemClickListener() {
            @Override
            public void toGoodsDetail(int position) {
                Intent intent = new Intent(mContext, GoodsDetailActivity.class);
                intent.putExtra("itemId", goodsGridAdapter.getData().get(position).getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        //调用“商品列表”接口
        GoodsListParam goodsListParam = new GoodsListParam();
        goodsListParam.setToken(token);
        goodsListParam.setPageNo(1);
        goodsListParam.setPageSize(Constant.PAGE_SIZE);
        goodsListParam.setIsRecommand("1");
        apiManager.itemList(goodsListParam);
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        finish();
    }

    /**
     * 我的订单
     */
    @OnClick(id = R.id.img_title_rt)
    public void toMyOrder() {
        startActivity(new Intent(mContext, MyOrderActivity.class));
    }

    @Override
    public void onBackPressed() {
        toBack();
        super.onBackPressed();
    }

    /**
     * 签到
     */
    @OnClick(id = R.id.btn_sign)
    public void toSign() {
        //调用“签到”接口
        SignInParam signInParam = new SignInParam();
        signInParam.setToken(token);
        apiManager.signIn(signInParam);
    }

    /**
     * 金币规则
     */
    @OnClick(id = R.id.txt_coin_rule)
    public void toCoinRules() {
        startActivity(new Intent(mContext, CoinRuleActivity.class));
    }

    /**
     * 兑换中心
     */
    @OnClick(id = R.id.txt_cashing_center)
    public void toCashingCenter() {
        startActivity(new Intent(mContext, CashingCenterActivity.class));
    }

    /**
     * 任务中心
     */
    @OnClick(id = R.id.txt_task_center)
    public void toTaskCenter() {
        startActivity(new Intent(mContext, TaskCenterActivity.class));
    }

    /**
     * 游戏中心
     */
    @OnClick(id = R.id.txt_game_center)
    public void toGameCenter() {
        // TODO: 2016/8/11 需求还未确定
//        startActivity(new Intent(mContext, GameCenterActivity.class));
    }

    /**
     * 签到
     *
     * @param result
     */
    public void onEventMainThread(SignInResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {//签到成功
            lemonMessage.sendMessage("签到成功","0");
            SignData signData = result.getData();
            txtSignNumber.setText("您已连续签到" + signData.getContinueSign() + "天");
            txtCoinNumber.setText(signData.getTotalPoint() + "");
            btnSign.setText("明日签到+10");
            btnSign.setBackground(getResources().getDrawable(R.drawable.btn_sign_normal));

            //更新本地积分数量
            UserBean userBean = UserBean.getInstance(mContext);
            userBean.point = signData.getTotalPoint();
            UserBean.updateUserBean(mContext, userBean);

            //刷新最新积分
            EventUtil.sendEvent(new UpdatePointEvent(userBean.point));
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

    /**
     * 获取首页中“特别推荐”列表数据
     *
     * @param result
     */
    public void onEventMainThread(GoodsListResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            GoodsData goodsData = result.getData();
            txtSignNumber.setText("您已连续签到" + goodsData.getContinueSign() + "天");
            if (goodsData.isCanDone()) {
                btnSign.setText("今日签到+10");
                btnSign.setBackground(getResources().getDrawable(R.drawable.btn_sign_press));
            } else {
                btnSign.setText("明日签到+10");
                btnSign.setBackground(getResources().getDrawable(R.drawable.btn_sign_normal));
            }
            BannerAdapter bannerAdapter = new BannerAdapter(mContext, result.getData().getBanner());
            bannerAdapter.setIsFinance(true);
            infiniteViewPagerCoin.setAdapter(bannerAdapter);
            infiniteViewPagerCoin.setAutoScrollTime(5000);
            infiniteViewPagerCoin.startAutoScroll();
            //特别推荐
            gvCommend.setNumColumns(2);
            gvCommend.setAdapter(goodsGridAdapter);
            //设置适配器
            goodsGridAdapter.setData(goodsData.getItems().getList());
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

    /**
     * 刷新积分
     *
     * @param event
     */
    public void onEventMainThread(UpdatePointEvent event) {
        txtCoinNumber.setText(String.valueOf(event.getPoint()));
    }

}
