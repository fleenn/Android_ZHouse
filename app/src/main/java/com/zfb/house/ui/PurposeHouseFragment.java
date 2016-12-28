package com.zfb.house.ui;

import android.support.v4.widget.SwipeRefreshLayout;

import com.lemon.LemonFragment;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.model.StatusCode;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.UserPurposeAdapter;
import com.zfb.house.component.LoadMoreRecyclerView;
import com.zfb.house.model.bean.UserRequirement;
import com.zfb.house.model.param.UserPurposeListParam;
import com.zfb.house.model.result.UserPurposeListResult;

import java.util.List;

/**
 * 用户：我的->我的委托->聊天委托
 * Created by Administrator on 2016/5/24.
 */
@Layout(id = R.layout.fragment_purpose_house)
public class PurposeHouseFragment extends LemonFragment {

    @FieldView(id = R.id.refresh_user_purpose)
    private SwipeRefreshLayout refreshUserPurpose;
    @FieldView(id = R.id.recycle_user_purpose)
    private LoadMoreRecyclerView recycleUserPurpose;

    private UserPurposeAdapter mAdapter;
    private String token;
    private UserPurposeListParam userPurposeListParam;

    @Override
    protected void initView() {
        refreshUserPurpose.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                userPurposeListParam.setToken(token);
                apiManager.listMyRequirement(userPurposeListParam);
            }
        });
        mAdapter = new UserPurposeAdapter(getActivity());
        recycleUserPurpose.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        token = SettingUtils.get(getActivity(), "token", "");
        userPurposeListParam = new UserPurposeListParam();
        userPurposeListParam.setToken(token);
        apiManager.listMyRequirement(userPurposeListParam);
    }

    public void onEventMainThread(UserPurposeListResult result){
        if (ParamUtils.isEmpty(result.getData())){
            return;
        }
        if(result.getResultCode().equals(StatusCode.SUCCESS.getCode())){
            List<UserRequirement> data = result.getData();
            mAdapter.setData(data);
            recycleUserPurpose.notifiyChange();
        }else {
            lemonMessage.sendMessage(result.getMessage());
        }
        refreshUserPurpose.setRefreshing(false);
    }
}
