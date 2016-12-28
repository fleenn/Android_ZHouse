package com.zfb.house.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.model.StatusCode;
import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.PhotoPagerAdapter;
import com.zfb.house.component.CirclePageIndicator;
import com.zfb.house.model.bean.Goods;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.model.param.GoodsParam;
import com.zfb.house.model.result.GoodsResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品详情页面
 * Created by Administrator on 2016/8/5.
 */
@Layout(id = R.layout.activity_goods_detail)
public class GoodsDetailActivity extends LemonActivity {

    private static final int REQUEST_CONFIRM_ORDER = 0x1;

    //商品照片
    @FieldView(id = R.id.mvp_photo)
    private ViewPager vpPhoto;
    @FieldView(id = R.id.indicator)
    private CirclePageIndicator indicator;
    //商品名称
    @FieldView(id = R.id.txt_goods_name)
    private TextView txtName;
    //商品所需金币数量
    @FieldView(id = R.id.txt_goods_coin)
    private TextView txtCoin;
    //商品剩余个数
    @FieldView(id = R.id.txt_surplus_number)
    private TextView txtSurplusNumber;
    //商品详情
    @FieldView(id = R.id.txt_detail)
    private TextView txtDetail;
    //活动规则
    @FieldView(id = R.id.txt_rule)
    private TextView txtRule;

    //传过来的商品ID
    private String itemId;
    //商品实体
    private Goods item;
    //最新剩余可兑换商品数量
    private int totalCount = 0;

    @Override
    protected void initView() {
        setCenterText(R.string.title_goods_detail);
        setRtImg(R.drawable.shopping_cart);
    }

    @Override
    protected void initData() {
        itemId = getIntentExtraStr("itemId");
        GoodsParam goodsParam = new GoodsParam();
        goodsParam.setItemId(itemId);
        apiManager.item(goodsParam);
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

    /**
     * 立即兑换
     */
    @OnClick(id = R.id.btn_cashing)
    public void toCashing() {
        if (UserBean.getInstance(mContext).point < Integer.parseInt(txtCoin.getText().toString())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_hint_coin_lack, null);
            final Dialog dialog = builder.create();
            dialog.setCancelable(false);
            //取消
            TextView txtCancel = (TextView) contentView.findViewById(R.id.txt_cancel);
            txtCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            //去赚金币
            TextView txtMoreCoin = (TextView) contentView.findViewById(R.id.txt_more_coin);
            txtMoreCoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mContext, TaskCenterActivity.class));
                    dialog.dismiss();
                }
            });
            //去掉dialog默认的背景
            dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
            dialog.show();
            dialog.setContentView(contentView);
        } else {
            Intent intent = new Intent(mContext, ConfirmOrderActivity.class);
            intent.putExtra("item", item);
            startActivityForResult(intent, REQUEST_CONFIRM_ORDER);
        }
    }

    /**
     * 商品详情
     *
     * @param result
     */
    public void onEventMainThread(GoodsResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            item = result.getData();
            //商品照片
            String[] photoArray = item.getItemPic().split(",");
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
                indicator.setViewPager(vpPhoto);
            }
            //商品名称
            txtName.setText(item.getItemName());
            //商品所需金币数量
            txtCoin.setText(item.getItemPoint());
            //已兑换商品数量
            totalCount = item.getTotalCount() - item.getChangedCount();
            txtSurplusNumber.setText("剩余" + totalCount + "个");
            //商品详情
            txtDetail.setText(item.getItemDescription());
            //活动规则
            txtRule.setText(item.getItemRule());
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CONFIRM_ORDER://确认订单
                    txtSurplusNumber.setText("剩余" + (totalCount - data.getIntExtra("changeCount", 0)) + "个");
                    break;
            }
        }
    }

}
