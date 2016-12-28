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
import com.zfb.house.adapter.UserCallEntrustAdapter;
import com.zfb.house.component.LoadMoreRecyclerView;
import com.zfb.house.model.bean.UserCallEntrutsInfo;
import com.zfb.house.model.param.UserCallEntrustParam;
import com.zfb.house.model.param.UserEntrustDeleteParam;
import com.zfb.house.model.result.UserCallEntrustResult;
import com.zfb.house.model.result.UserEntrustCallDeleteResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户：我的->我的委托->房源委托
 * Created by Administrator on 2016/5/24.
 */
@Layout(id = R.layout.fragment_entrust_house)
public class UserCallEntrustFragment extends LemonFragment {

    //页数
    public int mPageNo = 1;

    //自定义RecyclerView
    @FieldView(id = R.id.recycle_house_entrust)
    private LoadMoreRecyclerView recyclerUserExpert;
    //下拉刷新
    @FieldView(id = R.id.refresh_house_entrust)
    private SwipeRefreshLayout swipeRefreshLayout;
    @FieldView(id = R.id.layout_bottom)
    private View layoutBottom;

    private UserCallEntrustParam rentHouseParam;
    private UserEntrustDeleteParam deleteParam;
    private int mSellOrRentType = 1;
    private String mToken;

    private UserCallEntrustAdapter mAdapter;
    private List<UserCallEntrutsInfo.ListBean> mListDatas;
    @FieldView(id = R.id.tv_switch)
    private TextView tvSwitch;
    private String mDeleteId;
    @Override
    protected void initView() {
        super.initView();
        mListDatas = new ArrayList<>();
        tvSwitch.setVisibility(View.GONE);
        mAdapter = new UserCallEntrustAdapter(getActivity(),mListDatas);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerUserExpert.setHasFixedSize(true);
        recyclerUserExpert.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerUserExpert.setAdapter(mAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageNo = 1;
                rentHouseParam.setPageNo(mPageNo);
                apiManager.myOrder(rentHouseParam);
            }
        });

        recyclerUserExpert.setAutoLoadMoreEnable(true);
        recyclerUserExpert.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                swipeRefreshLayout.setRefreshing(false);
                mPageNo++;
                rentHouseParam.setPageNo(mPageNo);
                apiManager.myOrder(rentHouseParam);
            }
        });

        mAdapter.setOnDeleteClick(new UserCallEntrustAdapter.OnDeleteClick() {
            @Override
            public void onResult(String id) {
                mDeleteId = id;
                if (deleteParam == null) {
                    deleteParam = new UserEntrustDeleteParam();
                    deleteParam.setToken(mToken);
                }
                if (TextUtils.isEmpty(id)) {
                    lemonMessage.sendMessage("请先选择");
                    return;
                }
                deleteParam.setOrderIds(id);
                apiManager.deleteOrder(deleteParam);
            }
        });
    }


    public void editRefreshView(boolean isEdit){
        mAdapter.setIsEdit(isEdit);
        recyclerUserExpert.notifiyChange();
    }

    @Override
    protected void initData() {
        super.initData();
        mToken = SettingUtils.get(getActivity(), "token", null);
        rentHouseParam = new UserCallEntrustParam();
        rentHouseParam.setToken(mToken);
        rentHouseParam.setPageNo(mPageNo);
        rentHouseParam.setPageSize(Constant.PAGE_SIZE);
        apiManager.myOrder(rentHouseParam);
    }

    //接口返回值处理
    public void onEventMainThread(BaseResult result) {
        if (result instanceof UserCallEntrustResult) {
            UserCallEntrutsInfo rentHouse = ((UserCallEntrustResult) result).getData();
            List<UserCallEntrutsInfo.ListBean> list = rentHouse.getList();
            if (mPageNo == 1){
                mListDatas.clear();
            }

            if (list != null && list.size() == 10){
                recyclerUserExpert.notifyMoreFinish(true);
            }else{
                recyclerUserExpert.notifyMoreFinish(false);
            }
            mListDatas.addAll(list);
            recyclerUserExpert.notifiyChange();
            swipeRefreshLayout.setRefreshing(false);
        }else if (result instanceof UserEntrustCallDeleteResult){
            deleteSucessRefresh();
        }
    }

    /**
     * 删除成功刷新
     */
    private void deleteSucessRefresh(){
        layoutBottom.setVisibility(View.GONE);
        for (int j = 0;j < mListDatas.size();j++){
            if (mListDatas.get(j).getMessageId().equals(mDeleteId)){
                mListDatas.remove(mListDatas.get(j));
                break;
            }
        }

        recyclerUserExpert.notifiyChange();
    }

    @OnClick(id = R.id.tv_switch)
    public void switchWay(){

    }
}
