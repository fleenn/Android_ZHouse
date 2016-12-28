package com.zfb.house.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.lemon.LemonFragment;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.model.StatusCode;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.BrokerAcceptedHouseAdapter;
import com.zfb.house.model.bean.BrokerAccepted;
import com.zfb.house.model.param.BrokerAcceptedParam;
import com.zfb.house.model.result.BrokerAcceptedResult;

import java.util.List;

/**
 * Created by Snekey on 2016/10/21.
 */
@Layout(id = R.layout.fragment_accepted_house)
public class AcceptedHouseFragment extends LemonFragment{

    @FieldView(id = R.id.refresh_broker_accepted)
    private SwipeRefreshLayout refreshBrokerAccepted;
    @FieldView(id = R.id.recycle_broker_accepted)
    private RecyclerView recycleBrokerAccepted;

    private BrokerAcceptedHouseAdapter mAdapter;
    private String token;
    private BrokerAcceptedParam brokerAcceptedParam;

    @Override
    protected void initView() {
        refreshBrokerAccepted.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                brokerAcceptedParam.setToken(token);
                apiManager.listReceiveRequirement(brokerAcceptedParam);
            }
        });
        mAdapter = new BrokerAcceptedHouseAdapter(getActivity());
        recycleBrokerAccepted.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        token = SettingUtils.get(getActivity(), "token", "");
        brokerAcceptedParam = new BrokerAcceptedParam();
        brokerAcceptedParam.setToken(token);
        apiManager.listReceiveRequirement(brokerAcceptedParam);
    }

    public void onEventMainThread(BrokerAcceptedResult result){
        if (ParamUtils.isEmpty(result.getData())){
            return;
        }
        if(result.getResultCode().equals(StatusCode.SUCCESS.getCode())){
            List<BrokerAccepted> data = result.getData();
            mAdapter.setData(data);
            mAdapter.notifyDataSetChanged();
        }else {
            lemonMessage.sendMessage(result.getMessage());
        }
        refreshBrokerAccepted.setRefreshing(false);
    }
}
