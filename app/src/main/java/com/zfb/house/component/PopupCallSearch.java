package com.zfb.house.component;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.emchat.Constant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.CallCommissionAdapter;
import com.zfb.house.model.bean.CallCommission;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by linwenbing on 16/6/24.
 */
public class PopupCallSearch implements View.OnClickListener {
    private Context mContext;
    private PopupWindow mPopupWindow;
    private View vSellLeftLine, vSellCenterLine, vRentLeftLine, vRentCenterLine, vLimitLeftLine, vLimitCenterLine;
    private ListView lvMoney;
    private TextView tvSell, tvRent, tvLimit, tvMoneyTitle, tvMt;
    private View vBottomView;
    private CallCommissionAdapter mAdapter;
    private ChoicePriceListener choicePriceListener;
    private int selectedPosition = -1;
    private List<CallCommission> mSellList;
    private List<CallCommission> mRentList;
    private LinearLayout llayoutBrokerageList;

    public ChoicePriceListener getChoicePriceListener() {
        return choicePriceListener;
    }

    public void setChoicePriceListener(ChoicePriceListener choicePriceListener) {
        this.choicePriceListener = choicePriceListener;
    }

    public interface ChoicePriceListener {
        void getBrokerage(CallCommission callCommission);

        void getRequireType(String requireType, String word);
    }

    public PopupCallSearch(Context context, View bottomView) {
        mContext = context;
        vBottomView = bottomView;
        final View view = LayoutInflater.from(mContext).inflate(R.layout.popup_call_search, null);
        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);

        vSellLeftLine = view.findViewById(R.id.v_vertical_line);
        vSellCenterLine = view.findViewById(R.id.line_sell);
        vRentLeftLine = view.findViewById(R.id.v_vertical_line_rent);
        vRentCenterLine = view.findViewById(R.id.line_rent);
        vLimitLeftLine = view.findViewById(R.id.v_vertical_line_nolimit);
        vLimitCenterLine = view.findViewById(R.id.line_no_limit);

        tvSell = (TextView) view.findViewById(R.id.txt_sell);
        tvRent = (TextView) view.findViewById(R.id.txt_rent);
        tvLimit = (TextView) view.findViewById(R.id.tv_nolimit);
        tvMoneyTitle = (TextView) view.findViewById(R.id.tv_money_title);
        tvMt = (TextView) view.findViewById(R.id.tv_mt);
        llayoutBrokerageList = (LinearLayout) view.findViewById(R.id.llayout_brokerage_list);

        lvMoney = (ListView) view.findViewById(R.id.lv_money);
        mAdapter = new CallCommissionAdapter(mContext);
        Gson gson = new Gson();
        Type type = new TypeToken<List<CallCommission>>() {
        }.getType();
        mSellList = gson.fromJson(Constant.CALL_SELL_BROKERAGE, type);
        mRentList = gson.fromJson(Constant.CALL_RENT_BROKERAGE, type);
        mAdapter.setData(mSellList);
        lvMoney.setAdapter(mAdapter);
        lvMoney.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CallCommission callCommission = mAdapter.getData().get(position);
                if (!ParamUtils.isNull(choicePriceListener)) {
                    choicePriceListener.getBrokerage(callCommission);
                    if (selectedPosition>-1){
                        mRentList.get(selectedPosition).setIsSelected(false);
                        mSellList.get(selectedPosition).setIsSelected(false);
                    }
                    callCommission.setIsSelected(true);
                    selectedPosition = position;
                }
            }
        });

        view.findViewById(R.id.ll_sell).setOnClickListener(this);
        view.findViewById(R.id.ll_rent).setOnClickListener(this);
        view.findViewById(R.id.ll_nolimit).setOnClickListener(this);
    }

    public void show() {
        mPopupWindow.showAsDropDown(vBottomView);
    }

    public void dismiss() {
        mPopupWindow.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_sell:
                if (!ParamUtils.isNull(choicePriceListener)) {
                    choicePriceListener.getRequireType("1", "卖房专家");
                }
                vSellLeftLine.setBackgroundColor(mContext.getResources().getColor(R.color.orange));
                vLimitLeftLine.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                vRentLeftLine.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                vSellCenterLine.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                vRentCenterLine.setBackgroundColor(mContext.getResources().getColor(R.color.underline_color));
                vLimitCenterLine.setBackgroundColor(mContext.getResources().getColor(R.color.underline_color));
                tvSell.setTextColor(mContext.getResources().getColor(R.color.orange));
                tvRent.setTextColor(mContext.getResources().getColor(R.color.my_gray_one));
                tvLimit.setTextColor(mContext.getResources().getColor(R.color.my_gray_one));
                tvMoneyTitle.setVisibility(View.VISIBLE);
                tvMt.setVisibility(View.GONE);
                llayoutBrokerageList.setVisibility(View.VISIBLE);
                mAdapter.setData(mSellList);
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.ll_rent:
                if (!ParamUtils.isNull(choicePriceListener)) {
                    choicePriceListener.getRequireType("2", "租房专家");
                }
                vSellLeftLine.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                vRentLeftLine.setBackgroundColor(mContext.getResources().getColor(R.color.orange));
                vLimitLeftLine.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                vSellCenterLine.setBackgroundColor(mContext.getResources().getColor(R.color.underline_color));
                vRentCenterLine.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                vLimitCenterLine.setBackgroundColor(mContext.getResources().getColor(R.color.underline_color));
                tvSell.setTextColor(mContext.getResources().getColor(R.color.my_gray_one));
                tvRent.setTextColor(mContext.getResources().getColor(R.color.orange));
                tvLimit.setTextColor(mContext.getResources().getColor(R.color.my_gray_one));
                tvMoneyTitle.setVisibility(View.VISIBLE);
                tvMt.setVisibility(View.GONE);
                llayoutBrokerageList.setVisibility(View.VISIBLE);
                mAdapter.setData(mRentList);
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.ll_nolimit:
                if (!ParamUtils.isNull(choicePriceListener)) {
                    choicePriceListener.getRequireType("3", "租售专家");
                    if (selectedPosition>-1){
                        mRentList.get(selectedPosition).setIsSelected(false);
                        mSellList.get(selectedPosition).setIsSelected(false);
                    }
                }
                vSellLeftLine.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                vRentLeftLine.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                vLimitLeftLine.setBackgroundColor(mContext.getResources().getColor(R.color.orange));
                vSellCenterLine.setBackgroundColor(mContext.getResources().getColor(R.color.underline_color));
                vRentCenterLine.setBackgroundColor(mContext.getResources().getColor(R.color.underline_color));
                vLimitCenterLine.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                tvSell.setTextColor(mContext.getResources().getColor(R.color.my_gray_one));
                tvRent.setTextColor(mContext.getResources().getColor(R.color.my_gray_one));
                tvLimit.setTextColor(mContext.getResources().getColor(R.color.orange));
                tvMoneyTitle.setVisibility(View.GONE);
                tvMt.setVisibility(View.VISIBLE);
                llayoutBrokerageList.setVisibility(View.GONE);
                mAdapter.setData(null);
                break;
        }
    }
}
