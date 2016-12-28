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
import com.zfb.house.adapter.CollectRentAdapter;
import com.zfb.house.component.LoadMoreRecyclerView;
import com.zfb.house.model.bean.CollectRentData;
import com.zfb.house.model.bean.RentItem;
import com.zfb.house.model.param.CollectRentParam;
import com.zfb.house.model.param.DeleteConcernParam;
import com.zfb.house.model.result.CollectRentResult;
import com.zfb.house.model.result.DeleteConcernResult;
import com.zfb.house.util.ToolUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 收藏的房源——出租界面
 * Created by Administrator on 2016/7/25.
 */
@Layout(id = R.layout.fragment_collect_rent)
public class CollectRentFragment extends LemonFragment {

    private static final String TAG = "CollectRentFragment";
    private List<RentItem> mListDatas = new ArrayList<>();
    //页数
    public int mPageNo = 1;
    private String token;

    //自定义RecyclerView
    @FieldView(id = R.id.recycler_collect_rent)
    protected LoadMoreRecyclerView recyclerRent;
    @FieldView(id = R.id.refresh_rent_layout)
    protected SwipeRefreshLayout refreshRent;
    //收藏出租房源的适配器
    private CollectRentAdapter adapter;
    private CollectRentParam rentParam;

    @Override
    protected void initView() {
        setTitle(R.string.rent);
        recyclerRent.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerRent.setLayoutManager(layoutManager);
        adapter = new CollectRentAdapter(getContext());
        recyclerRent.setAdapter(adapter);
        //下拉刷新
        refreshRent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageNo = 1;
                rentParam.setPageNo(mPageNo);
                apiManager.findSolicitudeRentHouse(rentParam);
            }
        });
        ToolUtil.setRefreshing(refreshRent, true, true);
        //上拉加载更多
        recyclerRent.setAutoLoadMoreEnable(true);
        recyclerRent.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                refreshRent.setRefreshing(false);
                mPageNo++;
                rentParam.setPageNo(mPageNo);
                apiManager.findSolicitudeRentHouse(rentParam);
            }
        });
    }

    @Override
    protected void initData() {
        token = SettingUtils.get(getActivity(), "token", null);
        //设置监听事件
        adapter.setOnRecycleViewListener(onRecycleViewListener);
        rentParam = new CollectRentParam();
        rentParam.setToken(token);
        rentParam.setPageNo(mPageNo);
        rentParam.setPageSize(Constant.PAGE_SIZE);
    }

    private CollectRentAdapter.OnRecycleViewListener onRecycleViewListener = new CollectRentAdapter.OnRecycleViewListener() {
        @Override
        public void onItemClick(View view, int position, RentItem item) {
            Intent intent = new Intent(getActivity(), BrokerShopRentDetailActivity.class);
            String uuid = UUID.randomUUID().toString();//房源ID
            LemonContext.getBean(LemonCacheManager.class).putBean(uuid, item);
            intent.putExtra("uuid", uuid);
            intent.putExtra("useruuid", item.getBrokers());//经纪人ID
            startActivity(intent);
        }

        @Override
        public void onDeleteClick(View view, int position, RentItem item) {
            DeleteConcernParam deleteConcernParam = new DeleteConcernParam();
            deleteConcernParam.setToken(token);
            deleteConcernParam.setSid(item.getSid());
            deleteConcernParam.setTag(TAG);
            apiManager.deleteConcern(deleteConcernParam);
        }
    };

    public void editRefreshView(boolean isEdit) {
        adapter.setEdit(isEdit);
        recyclerRent.notifiyChange();
    }

    /**
     * 收藏租房列表
     *
     * @param result
     */
    public void onEventMainThread(CollectRentResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            if (ParamUtils.isNull(result.getData())) {
                recyclerRent.notifyMoreFinish(false);
                return;
            }
            CollectRentData collectrentData = result.getData();
            mListDatas = collectrentData.getList();
            if (collectrentData.getPageNo() == 1) {
                adapter.setData(mListDatas);
                recyclerRent.notifiyChange();
            } else {
                adapter.addData(mListDatas);
                recyclerRent.notifyMoreFinish(true);
            }
            if (ParamUtils.isEmpty(mListDatas) || mListDatas.size() < Constant.PAGE_SIZE) {
                recyclerRent.notifyMoreFinish(false);
            } else {
                recyclerRent.notifyMoreFinish(true);
            }
            refreshRent.setRefreshing(false);
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

    /**
     * 删除收藏的房源——出租
     *
     * @param result
     */
    public void onEventMainThread(DeleteConcernResult result) {
        if (!((DeleteConcernParam) result.getParam()).getTag().equals(TAG)) {
            return;
        }
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            ToolUtil.setRefreshing(refreshRent, true, true);
            lemonMessage.sendMessage(R.string.toast_delete_success);
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

}
