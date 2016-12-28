package com.zfb.house.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.android.emchat.Constant;
import com.lemon.LemonFragment;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.model.BaseResult;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.HouseEntrustAdapter;
import com.zfb.house.component.LoadMoreRecyclerView;
import com.zfb.house.model.bean.EntrustItem;
import com.zfb.house.model.bean.MyAuthroizeRentHouse;
import com.zfb.house.model.bean.MyAuthroizeSellHouse;
import com.zfb.house.model.param.FindMyAuthroizeRentHouseParam;
import com.zfb.house.model.param.FindMyAuthroizeSellHouseParam;
import com.zfb.house.model.param.HouseEntrustDeleteParam;
import com.zfb.house.model.param.HouseEntrustDeleteSellParam;
import com.zfb.house.model.result.FindMyAuthroizeRentHouseResult;
import com.zfb.house.model.result.FindMyAuthroizeSellHouseResult;
import com.zfb.house.model.result.HouseEntrustRentDeleteResult;
import com.zfb.house.model.result.HouseEntrustSellDeleteResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户：我的 -> 我的委托 -> 房源委托
 * Created by Administrator on 2016/5/24.
 */
@Layout(id = R.layout.fragment_entrust_house)
public class UserHouseEntrustFragment extends LemonFragment {

    //页数
    public int mPageNo = 1;
    //自定义RecyclerView
    @FieldView(id = R.id.recycle_house_entrust)
    private LoadMoreRecyclerView recyclerUserEntrust;
    //下拉刷新
    @FieldView(id = R.id.refresh_house_entrust)
    private SwipeRefreshLayout refreshUserEntrust;
    //租售切换
    @FieldView(id = R.id.layout_bottom)
    private View layoutBottom;

    private FindMyAuthroizeRentHouseParam rentHouseParam;
    private FindMyAuthroizeSellHouseParam sellHouseParam;
    private HouseEntrustDeleteParam deleteParam;
    private HouseEntrustDeleteSellParam deleteSellParam;
    private int mSellOrRentType = 1;
    private String mToken;

    private HouseEntrustAdapter mAdapter;
    private List<EntrustItem> mListDatas;
    @FieldView(id = R.id.tv_switch)
    private TextView tvSwitch;
    private String mDeleteId;

    @Override
    protected void initView() {
        super.initView();
        mListDatas = new ArrayList<>();
        mAdapter = new HouseEntrustAdapter(getActivity(), mListDatas);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerUserEntrust.setHasFixedSize(true);
        recyclerUserEntrust.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerUserEntrust.setAdapter(mAdapter);
        refreshUserEntrust.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageNo = 1;
                mAdapter.setSellOrRent(mSellOrRentType);
                if (mSellOrRentType == 1) {
                    sellHouseParam.setPageNo(mPageNo);
                    apiManager.findMyAuthroizeSellHouse(sellHouseParam);
                } else {
                    rentHouseParam.setPageNo(mPageNo);
                    apiManager.findMyAuthroizeRentHouse(rentHouseParam);
                }
            }
        });

        recyclerUserEntrust.setAutoLoadMoreEnable(true);
        recyclerUserEntrust.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                refreshUserEntrust.setRefreshing(false);
                mPageNo++;
                mAdapter.setSellOrRent(mSellOrRentType);
                if (mSellOrRentType == 1) {
                    sellHouseParam.setPageNo(mPageNo);
                    apiManager.findMyAuthroizeSellHouse(sellHouseParam);
                } else {
                    rentHouseParam.setPageNo(mPageNo);
                    apiManager.findMyAuthroizeRentHouse(rentHouseParam);
                }
            }
        });

        mAdapter.setOnDeleteClick(new HouseEntrustAdapter.OnDeleteClick() {
            @Override
            public void onResult(String id) {
                mDeleteId = id;
                if (deleteParam == null) {
                    deleteParam = new HouseEntrustDeleteParam();
                    deleteParam.setToken(mToken);
                }

                if (deleteSellParam == null) {
                    deleteSellParam = new HouseEntrustDeleteSellParam();
                    deleteSellParam.setToken(mToken);
                }
                if (TextUtils.isEmpty(id)) {
                    lemonMessage.sendMessage("请先选择");
                    return;
                }
                if (mSellOrRentType == 1) {
                    deleteSellParam.setSellHouseIds(id);
                    apiManager.deleteMySellHouse(deleteSellParam);
                } else {
                    deleteParam.setRentingHouseIds(id);
                    apiManager.deleteMyRentHouse(deleteParam);
                }
            }
        });
    }

    public void editRefreshView(boolean isEdit) {
        mAdapter.setIsEdit(isEdit);
        recyclerUserEntrust.notifiyChange();
    }

    @Override
    protected void initData() {
        super.initData();
        mToken = SettingUtils.get(getActivity(), "token", null);
        rentHouseParam = new FindMyAuthroizeRentHouseParam();
        rentHouseParam.setToken(mToken);
        rentHouseParam.setPageNo(mPageNo);
        rentHouseParam.setPageSize(Constant.PAGE_SIZE);

        sellHouseParam = new FindMyAuthroizeSellHouseParam();
        sellHouseParam.setToken(mToken);
        sellHouseParam.setPageSize(Constant.PAGE_SIZE);
        apiManager.findMyAuthroizeSellHouse(sellHouseParam);
    }

    /**
     * 删除成功刷新
     */
    private void deleteSucessRefresh() {
        layoutBottom.setVisibility(View.GONE);
        for (int j = 0; j < mListDatas.size(); j++) {
            if (mListDatas.get(j).getId().equals(mDeleteId)) {
                mListDatas.remove(mListDatas.get(j));
                break;
            }
        }
        recyclerUserEntrust.notifiyChange();
    }

    /**
     * 租售切换
     */
    @OnClick(id = R.id.tv_switch)
    public void switchWay() {
        mPageNo = 1;
        if (mSellOrRentType == 1) {
            mSellOrRentType = 2;
            tvSwitch.setText("售");
            sellHouseParam.setPageNo(mPageNo);
            apiManager.findMyAuthroizeRentHouse(sellHouseParam);
        } else {
            mSellOrRentType = 1;
            tvSwitch.setText("租");
            rentHouseParam.setPageNo(mPageNo);
            apiManager.findMyAuthroizeSellHouse(rentHouseParam);
        }
    }

    /**
     * 我的委托 -> 房源委托
     *
     * @param result
     */
    public void onEventMainThread(BaseResult result) {
        mAdapter.setSellOrRent(mSellOrRentType);
        List<EntrustItem> list = null;
        if (result instanceof FindMyAuthroizeRentHouseResult) {
            MyAuthroizeRentHouse rentHouse = ((FindMyAuthroizeRentHouseResult) result).getData();
            list = rentHouse.getList();
            if (mPageNo == 1) {
                mListDatas.clear();
            }

            mListDatas.addAll(list);
            recyclerUserEntrust.notifiyChange();
            refreshUserEntrust.setRefreshing(false);
            recyclerUserEntrust.notifyMoreFinish(list != null && list.size() == 10 ? true : false);
        } else if (result instanceof FindMyAuthroizeSellHouseResult) {
            MyAuthroizeSellHouse sellHouse = ((FindMyAuthroizeSellHouseResult) result).getData();
            list = sellHouse.getList();
            if (mPageNo == 1 && list != null && list.size() > 0) {
                mListDatas.clear();
            }

            mListDatas.addAll(list);
            recyclerUserEntrust.notifiyChange();
            refreshUserEntrust.setRefreshing(false);
            recyclerUserEntrust.notifyMoreFinish(list != null && list.size() == 10 ? true : false);
        } else if (result instanceof HouseEntrustRentDeleteResult
                || result instanceof HouseEntrustSellDeleteResult) {
            deleteSucessRefresh();
        }
    }

}
