package com.zfb.house.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.lemon.LemonFragment;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.BrokerCallEntrustAdapter;
import com.zfb.house.component.LoadMoreRecyclerView;
import com.zfb.house.model.bean.GrabCustomerItem;
import com.zfb.house.model.param.GrabCustomerListParam;
import com.zfb.house.model.param.HouseEntrustCallDeleteParam;
import com.zfb.house.model.result.GrabCustomerListResult;
import com.zfb.house.model.result.HouseEntrustCallDeleteResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户：我的->我的委托->呼叫委托
 * Created by Administrator on 2016/5/24.
 */
@Layout(id = R.layout.fragment_entrust_call)
public class BrokerCallEntrustFragment extends LemonFragment {
    //自定义RecyclerView
    @FieldView(id = R.id.recycle_call_entrust)
    private LoadMoreRecyclerView recyclerUserExpert;
    //下拉刷新
    @FieldView(id = R.id.refresh_call_entrust)
    private SwipeRefreshLayout swipeRefreshLayout;

    @FieldView(id = R.id.layout_bottom)
    private View layoutBottom;

    private  GrabCustomerListParam grabCustomerListParam;
    private int mFlag = 1;

    private BrokerCallEntrustAdapter mAdapter;
    private List<GrabCustomerItem> mListDatas;
    private HouseEntrustCallDeleteParam deleteParam;
    private String mToken;


    @Override
    protected void initView() {
        super.initView();
        mListDatas = new ArrayList<>();
        mAdapter = new BrokerCallEntrustAdapter(getActivity(),mListDatas);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerUserExpert.setHasFixedSize(true);
        recyclerUserExpert.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerUserExpert.setAdapter(mAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                apiManager.listOrder(grabCustomerListParam);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mToken = SettingUtils.get(getContext(), "token", null);
        grabCustomerListParam = new GrabCustomerListParam();
        switch (mFlag) {
            case 1:
                grabCustomerListParam.setToken(mToken);
                grabCustomerListParam.setHandleType("1");
                apiManager.listOrder(grabCustomerListParam);
                break;
            case 2:
                grabCustomerListParam.setToken(mToken);
                grabCustomerListParam.setHandleType("2");
                apiManager.listOrder(grabCustomerListParam);
        }
    }

    public void editRefreshView(boolean isEdit){
        mAdapter.setIsEdit(isEdit);
        recyclerUserExpert.notifiyChange();
        Log.i("linwb", "is == " + isEdit);
        if (isEdit){
            layoutBottom.setVisibility(View.VISIBLE);
        }else{
            layoutBottom.setVisibility(View.GONE);
        }
    }

    //接口返回值处理
    public void onEventMainThread(GrabCustomerListResult result) {
        List<GrabCustomerItem> list =  result.getData();
        if (list != null && list.size() > 0){
            mListDatas.clear();
        }
        mListDatas.addAll(list);
        recyclerUserExpert.notifiyChange();
        swipeRefreshLayout.setRefreshing(false);
    }
    //接口返回值处理
    public void onEventMainThread(HouseEntrustCallDeleteResult result) {
        deleteSucessRefresh();
    }

    /**
     * 删除成功刷新
     */
    private void deleteSucessRefresh(){
        layoutBottom.setVisibility(View.GONE);
        List<String> deleteIds = mAdapter.getmSelectHouseIds();
        Log.i("linbw", "ids ==" + deleteIds.toString());
        for (int i = 0;i < deleteIds.size();i++){
            for (int j = 0;j < mListDatas.size();j++){
                if (mListDatas.get(j).getOrderId().equals(deleteIds.get(i))){
                    mListDatas.remove(mListDatas.get(j));
                    break;
                }
            }
        }

        ((BrokerEntrustActivity) getActivity()).setTitleRight();
        ((BrokerEntrustActivity) getActivity()).setIsEdit(false);
        mAdapter.setIsEdit(false);
        recyclerUserExpert.notifiyChange();
    }

    /**
     * 全选
     */
    @OnClick(id = R.id.tv_select_all)
    public void selectAll(){
        mAdapter.setIsSelectAll(true);
        recyclerUserExpert.notifiyChange();
    }

    /**
     * 删除
     */
    @OnClick(id = R.id.tv_select_delete)
    public void selectDelete(){
        if (deleteParam == null){
            deleteParam = new HouseEntrustCallDeleteParam();
            deleteParam.setToken(mToken);
        }
        if (TextUtils.isEmpty(getDeleteIds())){
            lemonMessage.sendMessage("请先选择");
            return;
        }
        deleteParam.setOrderIds(getDeleteIds());
        apiManager.deleteBelongOrder(deleteParam);

    }

    private String getDeleteIds(){
        List<String> deleteIds = mAdapter.getmSelectHouseIds();
        if (deleteIds == null || deleteIds.size() == 0)
            return null;

        String idValues = "";
        int size = deleteIds.size();
        for (int i = 0;i < size;i++){
            if (TextUtils.isEmpty(idValues)){
                idValues = deleteIds.get(i);
            }else{
                idValues = idValues + "," + deleteIds.get(i);
            }
        }

        Log.i("linwb", "va = " + idValues);
        return idValues;
    }

}