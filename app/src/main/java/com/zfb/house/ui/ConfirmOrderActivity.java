package com.zfb.house.ui;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.event.UpdatePointEvent;
import com.lemon.model.StatusCode;
import com.lemon.util.EventUtil;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.model.bean.Goods;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.model.param.GoodsExchangeParam;
import com.zfb.house.model.result.GoodsExchangeResult;

/**
 * 确认订单
 * Created by HourGlassRemember on 2016/8/8.
 */
@Layout(id = R.layout.activity_confirm_order)
public class ConfirmOrderActivity extends LemonActivity {

    //商品照片
    @FieldView(id = R.id.img_order_picture)
    private ImageView imgOrderPic;
    //商品名称
    @FieldView(id = R.id.txt_order_name)
    private TextView txtOrderName;
    //商品所需金币数量
    @FieldView(id = R.id.txt_order_coin)
    private TextView txtOrderCoin;
    //商品数量
    @FieldView(id = R.id.txt_order_number)
    private TextView txtOrderNumber;
    //收货人
    @FieldView(id = R.id.edt_order_receiver)
    private EditText edtOrderReceiver;
    //收货地址
    @FieldView(id = R.id.edt_order_address)
    private EditText edtOrderAddress;
    //收货人联系号码
    @FieldView(id = R.id.edt_order_phone)
    private EditText edtOrderPhone;
    //备注
    @FieldView(id = R.id.edt_order_remarks)
    private EditText edtOrderRemarks;
    //合计金币数
    @FieldView(id = R.id.txt_order_all_coin)
    private TextView txtOrderAllCoin;
    //减
    @FieldView(id = R.id.img_sub)
    private ImageView imgSub;
    //兑换数量
    @FieldView(id = R.id.edt_order_number)
    private EditText edtOrderNumber;
    //加
    @FieldView(id = R.id.img_add)
    private ImageView imgAdd;

    //传过来的商品实体
    private Goods item;
    private String receiver, address, mobile, remarks;
    private int num = 1, coin;

    @Override
    protected void initView() {
        setCenterText(R.string.title_confirm_order);
    }

    @Override
    protected void initData() {
        item = (Goods) getIntent().getSerializableExtra("item");
        if (ParamUtils.isNull(item)) {
            return;
        }
        if (!ParamUtils.isEmpty(item.getItemPic())) {
            Glide.with(mContext).load(item.getItemPic().split(",")[0]).into(imgOrderPic);
        }
        txtOrderName.setText(item.getItemName());
        txtOrderCoin.setText(item.getItemPoint());
        coin = Integer.parseInt(txtOrderCoin.getText().toString().trim());

        imgSub.setOnClickListener(new OnOrderClickListener());
        imgSub.setTag("-");
        imgAdd.setOnClickListener(new OnOrderClickListener());
        imgAdd.setTag("+");
        edtOrderNumber.addTextChangedListener(new OnTextChangeListener());
        txtOrderAllCoin.setText("合计：" + coin * num + "金币");
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        finish();
    }

    /**
     * 加、减按钮事件监听器
     */
    private class OnOrderClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            //得到目前输入框的数字
            String numString = edtOrderNumber.getText().toString().trim();
            //如果为空或是没数字就设置为0
            if (ParamUtils.isEmpty(numString)) {
                num = 0;
                edtOrderNumber.setText("0");
                txtOrderNumber.setText("0");
                txtOrderAllCoin.setText("合计：0金币");
            } else {
                //点击“-”号就递减一
                if (view.getTag().equals("-")) {
                    if (--num <= 0) {//先减再判断
                        num++;
                        lemonMessage.sendMessage("数量最少为1");
                    } else {
                        edtOrderNumber.setText(String.valueOf(num));
                        txtOrderNumber.setText(String.valueOf(num));
                        txtOrderAllCoin.setText("合计：" + coin * num + "金币");
                    }
                } else if (view.getTag().equals("+")) {
                    if (++num <= 0) {//先加再判断
                        num--;
                    } else {
                        edtOrderNumber.setText(String.valueOf(num));
                        txtOrderNumber.setText(String.valueOf(num));
                        txtOrderAllCoin.setText("合计：" + coin * num + "金币");
                    }
                }
            }
        }
    }

    /**
     * EditText输入变化事件监听器
     */
    private class OnTextChangeListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String numString = edtOrderNumber.getText().toString().trim();
            if (ParamUtils.isEmpty(numString)) {
                num = 0;
            } else {
                int numInt = Integer.parseInt(numString);
                if (numInt < 0) {
                    lemonMessage.sendMessage("请输入一个大于0的数字");
                } else {
                    num = numInt;
                    txtOrderNumber.setText(edtOrderNumber.getText().toString().trim());
                    txtOrderAllCoin.setText("合计：" + coin * num + "金币");
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    /**
     * 提交兑换
     */
    @OnClick(id = R.id.txt_submit_cashing)
    public void toSubmitCashing() {
        receiver = edtOrderReceiver.getText().toString().trim();
        if (ParamUtils.isEmpty(receiver)) {
            lemonMessage.sendMessage("请输入收货人姓名");
            return;
        }
        //收货人姓名字符限制：2-15个字符限制
        if (receiver.length() < 2 || receiver.length() > 15) {
            lemonMessage.sendMessage("收货人姓名：2-15个字符限制");
            return;
        }

        address = edtOrderAddress.getText().toString().trim();
        if (ParamUtils.isEmpty(address)) {
            lemonMessage.sendMessage("请输入收货地址");
            return;
        }

        mobile = edtOrderPhone.getText().toString().trim();
        if (ParamUtils.isEmpty(mobile)) {
            lemonMessage.sendMessage("请输入联系电话");
            return;
        }
        if (!ParamUtils.isPhoneNumberValid(mobile)) {
            lemonMessage.sendMessage(R.string.hint_input_phone_error);
            return;
        }

        remarks = edtOrderRemarks.getText().toString().trim();

        GoodsExchangeParam goodsExchangeParam = new GoodsExchangeParam();
        goodsExchangeParam.setToken(SettingUtils.get(mContext, "token", null));
        goodsExchangeParam.setItemId(item.getId());
        goodsExchangeParam.setNum(Integer.parseInt(edtOrderNumber.getText().toString().trim()));
        goodsExchangeParam.setReceiver(receiver);
        goodsExchangeParam.setAddress(address);
        goodsExchangeParam.setMobile(mobile);
        //有备注才上传备注，没备注就不用上传
        if (!ParamUtils.isEmpty(remarks)) {
            goodsExchangeParam.setRemarks(remarks);
        }
        apiManager.itemExchange(goodsExchangeParam);
    }

    /**
     * 商品兑换
     *
     * @param result
     */
    public void onEventMainThread(GoodsExchangeResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {//兑换成功
            lemonMessage.sendMessage("兑换成功", "0");
            //刷新最新积
            int point = result.getData().getGetPoint();
            EventUtil.sendEvent(new UpdatePointEvent(point));

            //更新本地积分数量
            UserBean userBean = UserBean.getInstance(mContext);
            userBean.point = point;
            UserBean.updateUserBean(mContext, userBean);

            //返回最新已经兑换的个数
            Intent intent = new Intent();
            intent.putExtra("changeCount", Integer.parseInt(edtOrderNumber.getText().toString().trim()));
            setResult(RESULT_OK, intent);
            finish();
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

}
