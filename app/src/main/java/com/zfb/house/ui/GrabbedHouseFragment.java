package com.zfb.house.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.emchat.Constant;
import com.lemon.LemonFragment;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.model.BaseResult;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.GrabHouseAdapter;
import com.zfb.house.component.LoadMoreRecyclerView;
import com.zfb.house.model.bean.GrabHouseItem;
import com.zfb.house.model.param.GrabHouseListParam;
import com.zfb.house.model.result.GrabbedHouseRentListResult;
import com.zfb.house.model.result.GrabbedHouseSellListResult;
import com.zfb.house.util.ToolUtil;

import java.util.List;

/**
 * Created by Snekey on 2016/6/22.
 */
@Layout(id = R.layout.fragment_grab_list)
public class GrabbedHouseFragment extends LemonFragment {
    @FieldView(id = R.id.img_status_convert)
    private TextView imgStatusConvert;
    @FieldView(id = R.id.refresh_grab_list)
    private SwipeRefreshLayout refreshGrabList;
    @FieldView(id = R.id.recycler_grab_list)
    private LoadMoreRecyclerView recyclerGrabList;
    @FieldView(id = R.id.rlayout_no_data)
    private RelativeLayout rlayoutNoData;

    private GrabHouseAdapter mAdapter = null;
    private int mGrabbedType = 0;//0:rent;1:sell
    private int mRentPageNO = 1;
    private int mSellPageNO = 1;
    private GrabHouseListParam grabHouseParam;
    private String token;

    @OnClick(id = R.id.img_status_convert)
    public void toChangeType() {
//            已抢售房/租房
        if (mGrabbedType == 0) {
            mRentPageNO = 1;
            mGrabbedType = 1;
            imgStatusConvert.setText(R.string.label_rent);
            getGrabbedSellList();
        } else {
            mSellPageNO = 1;
            mGrabbedType = 0;
            imgStatusConvert.setText(R.string.label_sell);
            getGrabbedRentList();
        }
    }

    @Override
    protected void initView() {
        mAdapter = new GrabHouseAdapter(getContext());
        recyclerGrabList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerGrabList.setLayoutManager(layoutManager);
        recyclerGrabList.setAdapter(mAdapter);
        recyclerGrabList.setAutoLoadMoreEnable(true);
        recyclerGrabList.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mGrabbedType == 0) {
                    mRentPageNO++;
                    getGrabbedRentList();
                } else {
                    mSellPageNO++;
                    getGrabbedSellList();
                }
            }
        });
        token = SettingUtils.get(getContext(), "token", null);
        //先获取已抢租房
        getGrabbedRentList();
        imgStatusConvert.setVisibility(View.VISIBLE);
    }

    //    获取已抢租房列表
    private void getGrabbedRentList() {
        initListParam(mRentPageNO);
        apiManager.myRentingHousesAndroid(grabHouseParam);
        refreshGrabList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRentPageNO = 1;
                grabHouseParam.setPageNo(mRentPageNO);
                rlayoutNoData.setVisibility(View.GONE);
                apiManager.myRentingHousesAndroid(grabHouseParam);
            }
        });
    }


    //    获取已抢售房列表
    private void getGrabbedSellList() {
        initListParam(mSellPageNO);
        apiManager.mySellHousesAndroid(grabHouseParam);
        refreshGrabList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSellPageNO = 1;
                grabHouseParam.setPageNo(mSellPageNO);
                rlayoutNoData.setVisibility(View.GONE);
                apiManager.mySellHousesAndroid(grabHouseParam);
            }
        });
    }

    //    初始化列表请求参数
    private void initListParam(int pageNO) {
        grabHouseParam = new GrabHouseListParam();
        grabHouseParam.setToken(token);
        grabHouseParam.setPageNo(pageNO);
    }

    @Override
    public void onEventMainThread(BaseResult result) {
        if (result instanceof GrabbedHouseRentListResult) {
//            已抢出租房源列表
            mAdapter.setIsGrabbedList(true);
            mAdapter.setType(0);
            loadMoreRent(result);
        } else if (result instanceof GrabbedHouseSellListResult) {
//            已抢出售房源
            mAdapter.setIsGrabbedList(true);
            mAdapter.setType(1);
            loadMoreSell(result);
        }
        refreshGrabList.setRefreshing(false);
    }

    //    加载更多租房
    private void loadMoreRent(BaseResult result) {
        if (ParamUtils.isNull(result.getData())) {
            recyclerGrabList.notifyMoreFinish(false);
            rlayoutNoData.setVisibility(View.VISIBLE);
            return;
        }
        List<GrabHouseItem> list = ((GrabbedHouseRentListResult) result).getData().getList();
        if (mRentPageNO == 1) {
            mAdapter.setData(list);
            recyclerGrabList.notifiyChange();
        } else {
            mAdapter.addDatas(list);
            recyclerGrabList.notifyMoreFinish(true);
        }
        //无数据提示
        rlayoutNoData.setVisibility(ParamUtils.isEmpty(list) ? View.VISIBLE : View.GONE);
        if (ParamUtils.isEmpty(list) || list.size() < Constant.PAGE_SIZE) {
            recyclerGrabList.notifyMoreFinish(false);
        } else {
            recyclerGrabList.notifyMoreFinish(true);
        }
    }

    //    加载更多售房
    private void loadMoreSell(BaseResult result) {
        if (ParamUtils.isNull(result.getData())) {
            recyclerGrabList.notifyMoreFinish(false);
            rlayoutNoData.setVisibility(View.VISIBLE);
            return;
        }
        List<GrabHouseItem> list = ((GrabbedHouseSellListResult) result).getData().getList();
        if (mSellPageNO == 1) {
            mAdapter.setData(list);
            recyclerGrabList.notifiyChange();
        } else {
            mAdapter.addDatas(list);
        }
        //无数据提示
        rlayoutNoData.setVisibility(ParamUtils.isEmpty(list) ? View.VISIBLE : View.GONE);
        if (ParamUtils.isEmpty(list) || list.size() < Constant.PAGE_SIZE) {
            recyclerGrabList.notifyMoreFinish(false);
        } else {
            recyclerGrabList.notifyMoreFinish(true);
        }
    }

    public void setRefreshing() {
        if (refreshGrabList != null) {
            ToolUtil.setRefreshing(refreshGrabList, true, true);
        }
    }
}
