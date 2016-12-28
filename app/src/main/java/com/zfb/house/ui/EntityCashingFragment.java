package com.zfb.house.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.emchat.Constant;
import com.lemon.LemonFragment;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.model.StatusCode;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.EntityCashingAdapter;
import com.zfb.house.component.LoadMoreRecyclerView;
import com.zfb.house.model.bean.Goods;
import com.zfb.house.model.param.GoodsListParam;
import com.zfb.house.model.result.GoodsListResult;
import com.zfb.house.util.ToolUtil;

import java.util.List;

/**
 * 积分商城 -> 兑换中心 -> 实物兑换
 * Created by HourGlassRemember on 2016/8/5.
 */
@Layout(id = R.layout.fragment_cashing_entity)
public class EntityCashingFragment extends LemonFragment {

    //页数
    public int mPageNo = 1;
    //自定义RecyclerView
    @FieldView(id = R.id.recycler_cashing_entity)
    protected LoadMoreRecyclerView recyclerEntity;
    @FieldView(id = R.id.refresh_entity_layout)
    protected SwipeRefreshLayout refreshEntity;
    //实物兑换适配器
    private EntityCashingAdapter adapter;
    //实物兑换请求参数
    private GoodsListParam goodsListParam;

    @Override
    protected void initView() {
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerEntity.setHasFixedSize(true);
        //为RecycleView设置默认的线性GridLayoutManager
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerEntity.setLayoutManager(layoutManager);
        ToolUtil.setRefreshing(refreshEntity, true, true);

        //设置下拉刷新
        refreshEntity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageNo = 1;
                goodsListParam.setRefresh(true);
                goodsListParam.setPageNo(mPageNo);//更新页数
                apiManager.itemList(goodsListParam);//通知服务器更新页数
            }
        });
        //设置上拉加载
        recyclerEntity.setLoadingMore(true);
        recyclerEntity.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPageNo++;//页数加一
                goodsListParam.setRefresh(false);
                goodsListParam.setPageNo(mPageNo);//更新页数
                apiManager.itemList(goodsListParam);//通知服务器更新页数
            }
        });

        adapter = new EntityCashingAdapter(getActivity());
        recyclerEntity.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        //调用“商品列表”接口
        goodsListParam = new GoodsListParam();
        goodsListParam.setToken(SettingUtils.get(getActivity(), "token", null));
        goodsListParam.setPageNo(mPageNo);
        goodsListParam.setPageSize(Constant.PAGE_SIZE);
        apiManager.itemList(goodsListParam);
    }

    /**
     * 获取积分商城首页中“特别推荐”列表数据
     *
     * @param result
     */
    public void onEventMainThread(GoodsListResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            //如果没数据就不做上拉加载
            if (ParamUtils.isNull(result.getData())) {
                recyclerEntity.notifyMoreFinish(false);
                return;
            }
            List<Goods> dataList = result.getData().getItems().getList();
            if (!ParamUtils.isEmpty(dataList)) {
                if (((GoodsListParam) result.getParam()).getIsRefresh()) {
                    adapter.setData(dataList);
                    recyclerEntity.notifiyChange();
                    refreshEntity.setRefreshing(false);
                } else {
                    adapter.addData(dataList);
                }
                recyclerEntity.notifyMoreFinish(ParamUtils.isEmpty(dataList) || dataList.size() < Constant.PAGE_SIZE ? false : true);
            }
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

}
