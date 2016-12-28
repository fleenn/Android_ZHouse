package com.zfb.house.ui;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.emchat.Constant;
import com.lemon.LemonFragment;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.event.RefreshEvent;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.RelevantAdapter;
import com.zfb.house.component.LoadMoreRecyclerView;
import com.zfb.house.model.bean.Relevant;
import com.zfb.house.model.bean.RelevantContent;
import com.zfb.house.model.param.RelevantParam;
import com.zfb.house.model.result.RelevantResult;
import com.zfb.house.util.ToolUtil;

import java.util.List;

/**
 * Created by Snekey on 2016/7/7.
 */
@Layout(id = R.layout.fragment_relevant_moments)
public class RelevantMomentsFragment extends LemonFragment {

    @FieldView(id = R.id.recycler_moments)
    private LoadMoreRecyclerView recyclerMoments;
    @FieldView(id = R.id.refresh_layout)
    private SwipeRefreshLayout swipeRefreshLayout;
    @FieldView(id = R.id.rlayout_no_data)
    private RelativeLayout rlayoutNoData;

    private RelevantAdapter mRelevantAdapter;
    private int mPageNo = 1;
    private RelevantParam relevantParam;
    private String token;

    @Override
    protected void initView() {
        recyclerMoments.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerMoments.setLayoutManager(layoutManager);
        mRelevantAdapter = new RelevantAdapter(getContext());
        mRelevantAdapter.setOnClickUserDetail(new RelevantAdapter.OnClickUserDetail() {
            @Override
            public void toGetDetail(int position) {
                RelevantContent item = mRelevantAdapter.getData().get(position);
                toDetail(item.getReplyUserType(), item.getReplyUserId(), ParamUtils.isEmpty(item.getReplyUserAlise()) ? item.getReplyeeUserName() : item.getReplyUserAlise(), position);
            }
        });
        recyclerMoments.setAdapter(mRelevantAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageNo = 1;
                relevantParam.setPageNo(mPageNo);
                rlayoutNoData.setVisibility(View.GONE);
                apiManager.listMyRelationMsg(relevantParam);
            }
        });
        recyclerMoments.setAutoLoadMoreEnable(true);
        recyclerMoments.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                swipeRefreshLayout.setRefreshing(false);
                mPageNo++;
                relevantParam.setPageNo(mPageNo);
                apiManager.listMyRelationMsg(relevantParam);
            }
        });
    }

    @Override
    protected void initData() {
        token = SettingUtils.get(getContext(), "token", "");
        relevantParam = new RelevantParam();
        relevantParam.setToken(token);
        relevantParam.setPageNo(1);
        apiManager.listMyRelationMsg(relevantParam);
    }

    public void onEventMainThread(RelevantResult result) {
        updateList(result);
        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * 更新列表数据（上拉加载或者下拉刷新）
     *
     * @param result
     */
    private void updateList(RelevantResult result) {
        if (ParamUtils.isNull(result.getData())) {
            recyclerMoments.notifyMoreFinish(false);
            rlayoutNoData.setVisibility(View.VISIBLE);
            return;
        }
        Relevant relevant = result.getData();
        List<RelevantContent> list = relevant.getList();
        if (mPageNo == 1) {
            mRelevantAdapter.setData(list);
            recyclerMoments.notifiyChange();
            swipeRefreshLayout.setRefreshing(false);
        } else {
            mRelevantAdapter.addData(list);
        }
        //无数据提示
        rlayoutNoData.setVisibility(ParamUtils.isEmpty(list) ? View.VISIBLE : View.GONE);
        if (ParamUtils.isEmpty(list) || list.size() < Constant.PAGE_SIZE) {
            recyclerMoments.notifyMoreFinish(false);
        } else {
            recyclerMoments.notifyMoreFinish(true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1) {
            switch (requestCode) {
                case Constant.REQUEST_MODIFY_REMARKS:
                    //获得当前点击的item的位置position
                    int position = data.getIntExtra("position", 0);
                    mRelevantAdapter.getData().get(position).setReplyeeUserName(data.getStringExtra("remark"));
                    recyclerMoments.notifiyItemChange(position);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 注销后清除列表数据删除缓存的token
     *
     * @param event
     */
    public void onEventMainThread(RefreshEvent event) {
        relevantParam.setToken("");
        mRelevantAdapter.setData(null);
        recyclerMoments.notifiyChange();
        ToolUtil.setRefreshing(swipeRefreshLayout, true, true);
    }
}
