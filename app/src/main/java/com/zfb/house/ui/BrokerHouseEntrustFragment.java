package com.zfb.house.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lemon.LemonFragment;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.model.BaseResult;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.BrokerHouseEntrustAdapter;
import com.zfb.house.component.LoadMoreRecyclerView;
import com.zfb.house.model.bean.GrabHouse;
import com.zfb.house.model.bean.GrabHouseItem;
import com.zfb.house.model.param.GrabHouseListParam;
import com.zfb.house.model.param.HouseEntrustDeleteParam;
import com.zfb.house.model.param.HouseEntrustDeleteSellParam;
import com.zfb.house.model.result.GrabbedHouseRentListResult;
import com.zfb.house.model.result.GrabbedHouseSellListResult;
import com.zfb.house.model.result.HouseEntrustRentDeleteResult;
import com.zfb.house.model.result.HouseEntrustSellDeleteResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户：我的->我的委托->房源委托
 * Created by Administrator on 2016/5/24.
 */
@Layout(id = R.layout.fragment_entrust_house)
public class BrokerHouseEntrustFragment extends LemonFragment {

    //页数
    public int mPageNo = 1;

    //自定义RecyclerView
    @FieldView(id = R.id.recycle_house_entrust)
    private LoadMoreRecyclerView recyclerBrokerEntrust;
    //下拉刷新
    @FieldView(id = R.id.refresh_house_entrust)
    private SwipeRefreshLayout refreshBrokerEntrust;
    @FieldView(id = R.id.layout_bottom)
    private View layoutBottom;

    private GrabHouseListParam mGrabHouseParam;
    private HouseEntrustDeleteParam deleteRentParam;
    private HouseEntrustDeleteSellParam deleteSellParam;
    private int mSellOrRentType = 1;
    private String mToken;

    private BrokerHouseEntrustAdapter mAdapter;
    private List<GrabHouseItem> mListDatas;
    @FieldView(id = R.id.tv_switch)
    private TextView tvSwitch;

    @Override
    protected void initView() {
        super.initView();
        mListDatas = new ArrayList<>();
        mAdapter = new BrokerHouseEntrustAdapter(getActivity(), mListDatas);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerBrokerEntrust.setHasFixedSize(true);
        recyclerBrokerEntrust.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerBrokerEntrust.setAdapter(mAdapter);
        refreshBrokerEntrust.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageNo = 1;
                mGrabHouseParam.setPageNo(mPageNo);
                if (mSellOrRentType == 1) {
                    apiManager.mySellHousesAndroid(mGrabHouseParam);
                } else {
                    apiManager.myRentingHousesAndroid(mGrabHouseParam);
                }

            }
        });
        recyclerBrokerEntrust.setLoadingMore(false);

        recyclerBrokerEntrust.setAutoLoadMoreEnable(true);
        recyclerBrokerEntrust.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                refreshBrokerEntrust.setRefreshing(false);
                mPageNo++;
                mGrabHouseParam.setPageNo(mPageNo);
                if (mSellOrRentType == 1) {
                    apiManager.mySellHousesAndroid(mGrabHouseParam);
                } else {
                    apiManager.myRentingHousesAndroid(mGrabHouseParam);
                }
            }
        });
    }

    @Override
    protected void initData() {
        mToken = SettingUtils.get(getActivity(), "token", null);
        mGrabHouseParam = new GrabHouseListParam();
        mGrabHouseParam.setToken(mToken);
        mGrabHouseParam.setPageNo(mPageNo);
        apiManager.mySellHousesAndroid(mGrabHouseParam);
    }

    public void editRefreshView(boolean isEdit) {
        mAdapter.setIsEdit(isEdit);
        if (isEdit) {
            layoutBottom.setVisibility(View.VISIBLE);
        } else {
            layoutBottom.setVisibility(View.GONE);
            mAdapter.unSelectAll();
        }
        recyclerBrokerEntrust.notifiyChange();
    }

    /**
     * 删除成功刷新
     */
    private void deleteSucessRefresh() {
        layoutBottom.setVisibility(View.GONE);
        List<String> deleteIds = mAdapter.getmSelectHouseIds();
        Log.i("linbw", "ids ==" + deleteIds.toString());
        for (int i = 0; i < deleteIds.size(); i++) {
            for (int j = 0; j < mListDatas.size(); j++) {
                if (mListDatas.get(j).getId().equals(deleteIds.get(i))) {
                    mListDatas.remove(mListDatas.get(j));
                    break;
                }
            }
        }

        ((BrokerEntrustActivity) getActivity()).setTitleRight();
        ((BrokerEntrustActivity) getActivity()).setIsEdit(false);
        mAdapter.setIsEdit(false);
        recyclerBrokerEntrust.notifiyChange();
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
            mGrabHouseParam.setPageNo(mPageNo);
            apiManager.myRentingHousesAndroid(mGrabHouseParam);
        } else {
            mSellOrRentType = 1;
            tvSwitch.setText("租");
            mGrabHouseParam.setPageNo(mPageNo);
            apiManager.mySellHousesAndroid(mGrabHouseParam);
        }
    }

    /**
     * 全选
     */
    @OnClick(id = R.id.tv_select_all)
    public void selectAll() {
        mAdapter.selectAll();
        recyclerBrokerEntrust.notifiyChange();
    }

    /**
     * 删除
     */
    @OnClick(id = R.id.tv_select_delete)
    public void selectDelete() {
        if (deleteRentParam == null) {
            deleteRentParam = new HouseEntrustDeleteParam();
            deleteRentParam.setToken(mToken);
        }
        if (deleteSellParam == null) {
            deleteSellParam = new HouseEntrustDeleteSellParam();
            deleteSellParam.setToken(mToken);
        }
        if (TextUtils.isEmpty(getDeleteIds())) {
            lemonMessage.sendMessage("请先选择");
            return;
        }
        if (mSellOrRentType == 1) {
            deleteSellParam.setSellHouseIds(getDeleteIds());
            apiManager.deleteMySellHouse(deleteSellParam);
        } else {
            Log.i("linwb", "delete id = " + getDeleteIds());
            deleteRentParam.setRentingHouseIds(getDeleteIds());
            apiManager.deleteMyRentHouse(deleteRentParam);
        }
    }

    private String getDeleteIds() {
        List<String> deleteIds = mAdapter.getmSelectHouseIds();
        if (deleteIds == null || deleteIds.size() == 0) {
            return null;
        }
        String idValues = "";
        int size = deleteIds.size();
        for (int i = 0; i < size; i++) {
            idValues = TextUtils.isEmpty(idValues) ? deleteIds.get(i) : idValues + "," + deleteIds.get(i);
        }
        Log.i("linwb", "va = " + idValues);
        return idValues;
    }

    //接口返回值处理
    public void onEventMainThread(BaseResult result) {
        mAdapter.setSellOrRendType(mSellOrRentType);
        List<GrabHouseItem> list;
        if (result instanceof GrabbedHouseRentListResult) {
            GrabHouse grabHouse = (GrabHouse) result.getData();
            list = grabHouse.getList();
            if (mPageNo == 1) {
                mListDatas.clear();
            }

            recyclerBrokerEntrust.notifyMoreFinish(list != null && list.size() == 10 ? true : false);
            mAdapter.unSelectAll();
            mListDatas.addAll(list);
            recyclerBrokerEntrust.notifiyChange();
            refreshBrokerEntrust.setRefreshing(false);
        } else if (result instanceof GrabbedHouseSellListResult) {
            GrabHouse grabHouse = (GrabHouse) result.getData();
            list = grabHouse.getList();
            if (mPageNo == 1) {
                mListDatas.clear();
            }

            recyclerBrokerEntrust.notifyMoreFinish(list != null && list.size() == 10 ? true : false);
            mAdapter.unSelectAll();
            mListDatas.addAll(list);
            recyclerBrokerEntrust.notifiyChange();
            refreshBrokerEntrust.setRefreshing(false);
        } else if (result instanceof HouseEntrustRentDeleteResult
                || result instanceof HouseEntrustSellDeleteResult) {
            deleteSucessRefresh();
        }
    }

}
