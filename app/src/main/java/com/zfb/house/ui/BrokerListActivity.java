package com.zfb.house.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.emchat.Constant;
import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.BrokersDetailAdapter;
import com.zfb.house.adapter.PopAreaListAdapter;
import com.zfb.house.adapter.PopVillageAdapter;
import com.zfb.house.component.LoadMoreRecyclerView;
import com.zfb.house.component.PopupArea;
import com.zfb.house.component.PopupSort;
import com.zfb.house.model.bean.AreaData;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.model.bean.VillageData;
import com.zfb.house.model.param.AreaListParam;
import com.zfb.house.model.param.BrokerListParam;
import com.zfb.house.model.param.VillageParam;
import com.zfb.house.model.result.AreaListResult;
import com.zfb.house.model.result.BrokerListResult;
import com.zfb.house.model.result.VillageResult;

import java.util.List;

/**
 * 售房专家 or 租房专家
 * Created by Snekey on 2016/6/27.
 */
@Layout(id = R.layout.activity_broker_list)
public class BrokerListActivity extends LemonActivity {

    @FieldView(id = R.id.recycler_broker_list)
    private LoadMoreRecyclerView recyclerBrokerList;
    @FieldView(id = R.id.refresh_broker_list)
    private SwipeRefreshLayout refreshBrokerList;
    @FieldView(id = R.id.llayout_select_area)
    private LinearLayout llayoutSelectArea;
    //行政区
    @FieldView(id = R.id.tv_area)
    private TextView tvArea;
    //片区
    @FieldView(id = R.id.tv_district)
    private TextView tvDistrict;
    //小区
    @FieldView(id = R.id.tv_community)
    private TextView tvCommunity;

    private int pageNo = 1;
    private BrokerListParam brokerListParam;
    private BrokersDetailAdapter brokersDetailAdapter;
    private PopAreaListAdapter mAreaAdapter;
    private PopAreaListAdapter mDistrictAdapter;
    private PopVillageAdapter mVillageAdapter;
    private PopupArea mPopupArea;
    private AreaListParam mAreaListParam;
    private VillageParam mVillageParam;
    private int mRange = 0;//0:area;1:district
    private PopupSort mPopupSort;
    private PopupSort.OnSortListener mOnSortListener = new PopupSort.OnSortListener() {
        @Override
        public void toSort(int orderType) {
            brokerListParam.setOrderType(orderType);
            apiManager.saleRentBrokers(brokerListParam);
        }
    };

    //    点击地区或小区事件
    private PopAreaListAdapter.PopDataListener popDataListener = new PopAreaListAdapter.PopDataListener() {
        @Override
        public void getAreaData(String parentId) {
            switch (parentId.length()) {
                case 6:
                    mAreaListParam.setParentId(parentId);
                    apiManager.getAreaList(mAreaListParam);
                    brokerListParam.setRegionId(parentId);
                    brokerListParam.setServiceDistrictId("");
                    brokerListParam.setCommuntityId("");
                    mVillageAdapter.setData(null);
                    brokerListParam.setIsRefresh(true);
                    brokerListParam.setPageNo(1);
                    apiManager.saleRentBrokers(brokerListParam);
                    mPopupArea.dismiss();
                    break;
                case 8:
                    mVillageParam.setParentId(parentId);
                    apiManager.getVillage(mVillageParam);
                    brokerListParam.setCommuntityId("");
                    brokerListParam.setServiceDistrictId(parentId);
                    brokerListParam.setIsRefresh(true);
                    brokerListParam.setPageNo(1);
                    apiManager.saleRentBrokers(brokerListParam);
                    mPopupArea.dismiss();
                    break;
                case 11:
                    brokerListParam.setCommuntityId(parentId);
                    apiManager.saleRentBrokers(brokerListParam);
                    brokerListParam.setPageNo(1);
                    brokerListParam.setIsRefresh(true);
                    mPopupArea.dismiss();
                    break;
            }
        }
    };

    @Override
    protected void initView() {
        if (getIntentExtraInt("brokerType") == 1) {
            setCenterText(R.string.title_sell_broker);
        } else if (getIntentExtraInt("brokerType") == 2) {
            setCenterText(R.string.title_rent_broker);
        }
        setRtText(R.string.label_home_sort);
        setRtTextColor(R.color.black);
        mPopupArea = new PopupArea(mContext, llayoutSelectArea);
        mPopupSort = new PopupSort(mContext, mTvTitleRt);
        mPopupSort.setmOnSortListener(mOnSortListener);
        recyclerBrokerList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerBrokerList.setLayoutManager(layoutManager);
        brokersDetailAdapter = new BrokersDetailAdapter(mContext);
        brokersDetailAdapter.setmOnTouchDetailListener(new BrokersDetailAdapter.OnTouchDetailListener() {
            @Override
            public void toPersonalDetail(int position) {
                UserBean userBean = brokersDetailAdapter.getData().get(position);
                toDetail("1", userBean.id, ParamUtils.isEmpty(userBean.remark) ? userBean.name : userBean.remark);
            }
        });
        recyclerBrokerList.setAdapter(brokersDetailAdapter);
        refreshBrokerList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                brokerListParam.setIsRefresh(true);
                brokerListParam.setPageNo(pageNo);
                apiManager.saleRentBrokers(brokerListParam);
            }
        });
        recyclerBrokerList.setAutoLoadMoreEnable(true);
        recyclerBrokerList.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                pageNo++;
                brokerListParam.setPageNo(pageNo);
                brokerListParam.setIsRefresh(false);
                apiManager.saleRentBrokers(brokerListParam);
            }
        });
    }

    @Override
    protected void initData() {
        mAreaAdapter = new PopAreaListAdapter(mContext);
        mDistrictAdapter = new PopAreaListAdapter(mContext);
        mVillageAdapter = new PopVillageAdapter(mContext);
        mAreaAdapter.setPopDataListener(popDataListener);
        mDistrictAdapter.setPopDataListener(popDataListener);
        mVillageAdapter.setPopDataListener(popDataListener);
        brokerListParam = new BrokerListParam();
        brokerListParam.setBrokerType(getIntentExtraInt("brokerType"));
        brokerListParam.setPageNo(1);
        brokerListParam.setPageSize(Constant.PAGE_SIZE);
        brokerListParam.setOrderType(1);
        apiManager.saleRentBrokers(brokerListParam);
        mVillageParam = new VillageParam();
        mAreaListParam = new AreaListParam();
        mAreaListParam.setParentId("0592");
        apiManager.getAreaList(mAreaListParam);
    }

    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        finish();
    }

    @OnClick(id = R.id.tv_title_rt)
    public void toSort() {
        mPopupSort.show();
    }

    @OnClick(id = R.id.tv_area)
    public void toChooseArea() {
        mPopupArea.setAdapter(mAreaAdapter);
        changeColor(0);
        mPopupArea.show();
    }

    @OnClick(id = R.id.tv_district)
    public void toChooseDistrict() {
        if (ParamUtils.isEmpty(mDistrictAdapter.getData())) {
            lemonMessage.sendMessage("请先选择行政区");
        } else {
            mPopupArea.setAdapter(mDistrictAdapter);
            changeColor(1);
            mPopupArea.show();
        }
    }

    @OnClick(id = R.id.tv_community)
    public void toChooseCommubity() {
        if (ParamUtils.isEmpty(mVillageAdapter.getData())) {
            lemonMessage.sendMessage("请先选择片区");
        } else {
            mPopupArea.setAdapter(mVillageAdapter);
            changeColor(2);
            mPopupArea.show();
        }
    }

    //    改变选择的下拉框颜色
    private void changeColor(int index) {
        tvArea.setSelected(index == 0);
        tvDistrict.setSelected(index == 1);
        tvCommunity.setSelected(index == 2);
        tvArea.setBackgroundResource(index == 0 ? R.drawable.bg_spinner_press : R.drawable.bg_spinner);
        tvDistrict.setBackgroundResource(index == 1 ? R.drawable.bg_spinner_press : R.drawable.bg_spinner);
        tvCommunity.setBackgroundResource(index == 2 ? R.drawable.bg_spinner_press : R.drawable.bg_spinner);
    }

    /**
     * 经纪人列表
     *
     * @param result
     */
    public void onEventMainThread(BrokerListResult result) {
        if (ParamUtils.isNull(result.getData())) {
            recyclerBrokerList.notifyMoreFinish(false);
            return;
        }

        List<UserBean> data = result.getData().getList();
        if (((BrokerListParam) result.getParam()).getIsRefresh()) {
            brokersDetailAdapter.setData(data);
            recyclerBrokerList.notifiyChange();
            refreshBrokerList.setRefreshing(false);
        } else {
            brokersDetailAdapter.getData().addAll(data);
        }

        if (ParamUtils.isEmpty(data) || data.size() < 10) {
            recyclerBrokerList.notifyMoreFinish(false);
        } else {
            recyclerBrokerList.notifyMoreFinish(true);
        }

    }

    /**
     * 获取行政区或者片区
     * mRange：0:行政区;1:片区;
     *
     * @param result
     */
    public void onEventMainThread(AreaListResult result) {
        List<AreaData> data = result.getData();
        if (mRange == 0) {
            mAreaAdapter.setData(data);
            mPopupArea.setAdapter(mAreaAdapter);
            mRange = 1;
        } else {
            mDistrictAdapter.setData(data);
        }
    }

    /**
     * 获取小区
     *
     * @param result
     */
    public void onEventMainThread(VillageResult result) {
        List<VillageData> data = result.getData();
        mVillageAdapter.setData(data);
    }
}
