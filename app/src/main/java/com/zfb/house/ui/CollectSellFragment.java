package com.zfb.house.ui;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.emchat.Constant;
import com.lemon.LemonCacheManager;
import com.lemon.LemonContext;
import com.lemon.LemonFragment;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.model.StatusCode;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.CollectSellAdapter;
import com.zfb.house.component.LoadMoreRecyclerView;
import com.zfb.house.model.bean.CollectSellData;
import com.zfb.house.model.bean.SellItem;
import com.zfb.house.model.param.CollectSellParam;
import com.zfb.house.model.param.DeleteConcernParam;
import com.zfb.house.model.result.CollectSellResult;
import com.zfb.house.model.result.DeleteConcernResult;
import com.zfb.house.util.ToolUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 收藏的房源——出售界面
 * Created by Administrator on 2016/7/25.
 */
@Layout(id = R.layout.fragment_collect_sell)
public class CollectSellFragment extends LemonFragment {

    private static final String TAG = "CollectSellFragment";
    private List<SellItem> mListDatas = new ArrayList<>();
    //页数
    public int mPageNo = 1;
    private String token;

    //自定义RecyclerView
    @FieldView(id = R.id.recycler_collect_sell)
    protected LoadMoreRecyclerView recyclerSell;
    @FieldView(id = R.id.refresh_sell_layout)
    protected SwipeRefreshLayout refreshSell;
    //收藏出售房源的适配器
    private CollectSellAdapter adapter;
    private CollectSellParam sellParam;

    @Override
    protected void initView() {
        setTitle(R.string.sell);
        recyclerSell.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerSell.setLayoutManager(layoutManager);
        adapter = new CollectSellAdapter(getContext());
        recyclerSell.setAdapter(adapter);
        //下拉刷新
        refreshSell.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageNo = 1;
                sellParam.setPageNo(mPageNo);
                apiManager.findSolicitudeSellHouse(sellParam);
            }
        });
        ToolUtil.setRefreshing(refreshSell, true, true);
        //上拉加载更多
        recyclerSell.setAutoLoadMoreEnable(true);
        recyclerSell.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                refreshSell.setRefreshing(false);
                mPageNo++;
                sellParam.setPageNo(mPageNo);
                apiManager.findSolicitudeSellHouse(sellParam);
            }
        });
    }

    @Override
    protected void initData() {
        token = SettingUtils.get(getActivity(), "token", null);
        //设置监听事件
        adapter.setOnRecycleViewListener(onRecycleViewListener);
        sellParam = new CollectSellParam();
        sellParam.setToken(token);
        sellParam.setPageNo(mPageNo);
        sellParam.setPageSize(Constant.PAGE_SIZE);
    }

    private CollectSellAdapter.OnRecycleViewListener onRecycleViewListener = new CollectSellAdapter.OnRecycleViewListener() {
        @Override
        public void onItemClick(View view, int position, SellItem item) {
            Intent intent = new Intent(getActivity(), BrokerShopSellDetailActivity.class);
            String uuid = UUID.randomUUID().toString();//房源ID
            LemonContext.getBean(LemonCacheManager.class).putBean(uuid, item);
            intent.putExtra("uuid", uuid);
            intent.putExtra("useruuid", item.getBrokers());//经纪人ID
            startActivity(intent);
        }

        @Override
        public void onDeleteClick(View view, int position, SellItem item) {
            DeleteConcernParam deleteConcernParam = new DeleteConcernParam();
            deleteConcernParam.setToken(token);
            deleteConcernParam.setSid(item.getSid());
            deleteConcernParam.setTag(TAG);
            apiManager.deleteConcern(deleteConcernParam);
            ToolUtil.setRefreshing(refreshSell, true, true);
        }
    };

    public void editRefreshView(boolean isEdit) {
        adapter.setEdit(isEdit);
        recyclerSell.notifiyChange();
    }

    /**
     * 收藏售房列表
     *
     * @param result
     */
    public void onEventMainThread(CollectSellResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            if (ParamUtils.isNull(result.getData())) {
                recyclerSell.notifyMoreFinish(false);
                return;
            }
            CollectSellData collectSellData = result.getData();
            mListDatas = collectSellData.getList();
            if (collectSellData.getPageNo() == 1) {
                adapter.setData(mListDatas);
                recyclerSell.notifiyChange();
            } else {
                adapter.addData(mListDatas);
                recyclerSell.notifyMoreFinish(true);
            }
            recyclerSell.notifyMoreFinish(ParamUtils.isEmpty(mListDatas) || mListDatas.size() < Constant.PAGE_SIZE ? false : true);
            refreshSell.setRefreshing(false);
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

    /**
     * 删除收藏的房源——出售
     *
     * @param result
     */
    public void onEventMainThread(DeleteConcernResult result) {
        if (!((DeleteConcernParam) result.getParam()).getTag().equals(TAG)) {
            return;
        }
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            lemonMessage.sendMessage(R.string.toast_delete_success);
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

}
