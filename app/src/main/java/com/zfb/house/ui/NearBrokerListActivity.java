package com.zfb.house.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.emchat.Constant;
import com.google.gson.Gson;
import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.config.Config;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.BrokersDetailAdapter;
import com.zfb.house.component.LoadMoreRecyclerView;
import com.zfb.house.model.bean.NearBrokerBody;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.model.param.NearBrokerParam;
import com.zfb.house.model.result.NearBrokerResult;

import java.util.List;

/**
 * 附近优秀社区专家
 * Created by Snekey on 2016/6/27.
 */
@Layout(id = R.layout.activity_near_broker_list)
public class NearBrokerListActivity extends LemonActivity {

    @FieldView(id = R.id.recycler_broker_list)
    private LoadMoreRecyclerView recyclerBrokerList;
    @FieldView(id = R.id.refresh_broker_list)
    private SwipeRefreshLayout refreshBrokerList;

    private double lat;
    private double lng;
    private int pageNo = 1;
    private String serviceVillage;
    private NearBrokerParam nearBrokerParam;
    private NearBrokerBody nearBrokerBody;
    private BrokersDetailAdapter brokersDetailAdapter;

    @Override
    protected void initView() {
        setCenterText(R.string.title_near_broker);
        recyclerBrokerList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerBrokerList.setLayoutManager(layoutManager);
        brokersDetailAdapter = new BrokersDetailAdapter(mContext);
        brokersDetailAdapter.setShowBrokerType(true);
        brokersDetailAdapter.setCountType(2);
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
                nearBrokerParam.setPageNo(pageNo);
                apiManager.nearByBroker(nearBrokerParam);
            }
        });
        recyclerBrokerList.setAutoLoadMoreEnable(true);
        recyclerBrokerList.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                pageNo++;
                nearBrokerParam.setPageNo(pageNo);
                apiManager.nearByBroker(nearBrokerParam);
            }
        });
    }

    @Override
    protected void initData() {
        lat = SettingUtils.get(mContext, "lat", 0.0);
        lng = SettingUtils.get(mContext, "lng", 0.0);
        nearBrokerParam = new NearBrokerParam();
        nearBrokerBody = new NearBrokerBody();
        nearBrokerBody.setLat(lat);
        nearBrokerBody.setLng(lng);
        nearBrokerBody.setRange(Config.getLongValue("broker_range"));
        nearBrokerParam.setPageNo(1);
        nearBrokerParam.setPageSize(Constant.PAGE_SIZE);
        nearBrokerParam.setBody(new Gson().toJson(nearBrokerBody));
        apiManager.nearByBroker(nearBrokerParam);
    }

    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        finish();
    }

    public void onEventMainThread(NearBrokerResult result) {
        if (ParamUtils.isNull(result.getData())) {
            recyclerBrokerList.notifyMoreFinish(false);
            return;
        }
        List<UserBean> data = result.getData();
        if (pageNo == 1) {
            brokersDetailAdapter.setData(data);
            recyclerBrokerList.notifiyChange();
            refreshBrokerList.setRefreshing(false);
        } else {
            brokersDetailAdapter.getData().addAll(data);
        }

        if (ParamUtils.isEmpty(data) || data.size() < Constant.PAGE_SIZE) {
            recyclerBrokerList.notifyMoreFinish(false);
        } else {
            recyclerBrokerList.notifyMoreFinish(true);
        }
    }
}
