package com.zfb.house.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.emchat.Constant;
import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.BrokersDetailAdapter;
import com.zfb.house.component.LoadMoreRecyclerView;
import com.zfb.house.component.PopupSort;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.model.param.BrokerListParam;
import com.zfb.house.model.result.BrokerListResult;

import java.util.List;

/**
 * Created by Snekey on 2016/7/15.
 */
@Layout(id = R.layout.activity_near_broker_list)
public class SearchResultActivity extends LemonActivity {

    @FieldView(id = R.id.recycler_broker_list)
    private LoadMoreRecyclerView recyclerBrokerList;
    @FieldView(id = R.id.refresh_broker_list)
    private SwipeRefreshLayout refreshBrokerList;
    @FieldView(id = R.id.rlayout_hint)
    private RelativeLayout rlayoutHint;

    private BrokerListParam brokerListParam;
    private BrokersDetailAdapter brokersDetailAdapter;
    private PopupSort mPopupSort;
    private int mPageNo = 1;
    private PopupSort.OnSortListener mOnSortListener = new PopupSort.OnSortListener() {
        @Override
        public void toSort(int orderType) {
            brokerListParam.setOrderType(orderType);
            apiManager.saleRentBrokers(brokerListParam);
        }
    };

    @Override
    protected void initView() {
        setCenterText(R.string.title_search_result);
        setRtText(R.string.label_home_sort);
        setRtTextColor(R.color.black);
        mPopupSort = new PopupSort(mContext, mTvTitleRt);
        mPopupSort.setmOnSortListener(mOnSortListener);
        recyclerBrokerList.setHasFixedSize(true);
        recyclerBrokerList.setLayoutManager(new LinearLayoutManager(mContext));
        brokersDetailAdapter = new BrokersDetailAdapter(mContext);
        brokersDetailAdapter.setShowBrokerType(true);
        brokersDetailAdapter.setmOnTouchDetailListener(new BrokersDetailAdapter.OnTouchDetailListener() {
            @Override
            public void toPersonalDetail(int position) {
                toDetail("1", brokersDetailAdapter.getData().get(position).id, brokersDetailAdapter.getData().get(position).remark);
            }
        });
        recyclerBrokerList.setAdapter(brokersDetailAdapter);
        recyclerBrokerList.setAutoLoadMoreEnable(true);
        recyclerBrokerList.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPageNo++;
                brokerListParam.setPageNo(mPageNo);
                brokerListParam.setIsRefresh(false);
                apiManager.saleRentBrokers(brokerListParam);
            }
        });
        refreshBrokerList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageNo = 1;
                brokerListParam.setIsRefresh(true);
                brokerListParam.setPageNo(mPageNo);
                apiManager.saleRentBrokers(brokerListParam);
            }
        });
    }

    @Override
    protected void initData() {
        int brokerType = getIntent().getIntExtra("brokerType", 1);
        String id = getIntent().getStringExtra("id");
        brokerListParam = new BrokerListParam();
        brokerListParam.setPageNo(mPageNo);
        brokerListParam.setCommuntityId(id);
        brokerListParam.setBrokerType(brokerType);
        apiManager.saleRentBrokers(brokerListParam);
    }

    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        finish();
    }

    @OnClick(id = R.id.tv_title_rt)
    public void toSort() {
        mPopupSort.show();
    }

    public void onEventMainThread(BrokerListResult result) {
        if (ParamUtils.isNull(result.getData())) {
            recyclerBrokerList.notifyMoreFinish(false);
            return;
        }
        List<UserBean> data = result.getData().getList();
        if (((BrokerListParam) result.getParam()).getIsRefresh()) {
//            搜索结果为空的提示
            if (ParamUtils.isEmpty(data)) {
                rlayoutHint.setVisibility(View.VISIBLE);
            } else {
                rlayoutHint.setVisibility(View.GONE);
            }
            brokersDetailAdapter.setData(data);
            recyclerBrokerList.notifiyChange();
            refreshBrokerList.setRefreshing(false);
        } else {
            brokersDetailAdapter.getData().addAll(data);
            recyclerBrokerList.notifyMoreFinish(true);
        }
        if (ParamUtils.isEmpty(data) || data.size() < Constant.PAGE_SIZE) {
            recyclerBrokerList.notifyMoreFinish(false);
        } else {
            recyclerBrokerList.notifyMoreFinish(true);
        }
    }
}
