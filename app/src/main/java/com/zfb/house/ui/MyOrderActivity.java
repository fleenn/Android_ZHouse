package com.zfb.house.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.emchat.Constant;
import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.model.StatusCode;
import com.lemon.util.ScreenUtil;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.MyOrderAdapter;
import com.zfb.house.component.SpacesItemDecoration;
import com.zfb.house.model.bean.Order;
import com.zfb.house.model.bean.OrderData;
import com.zfb.house.model.param.MyOrderParam;
import com.zfb.house.model.result.MyOrderResult;

import java.util.List;

/**
 * 我的订单
 * Created by HourGlassRemember on 2016/8/9.
 */
@Layout(id = R.layout.activity_my_order)
public class MyOrderActivity extends LemonActivity {

    private static final String TAG = "MyOrderActivity";
    //页数
    public int mPageNo = 1;
    //RecyclerView
    @FieldView(id = R.id.recycler_my_order)
    private RecyclerView recyclerMyOrder;
    //下拉刷新
    @FieldView(id = R.id.refresh_my_order)
    private SwipeRefreshLayout refreshMyOrder;
    //我的订单的适配器
    private MyOrderAdapter adapter;
    private LinearLayoutManager layoutManager;
    //我的订单的请求参数
    private MyOrderParam myOrderParam;

    @Override
    protected void initView() {
        setCenterText(R.string.title_my_order);

        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerMyOrder.setHasFixedSize(true);
        // 为RecycleView设置默认的线性LayoutManage
        layoutManager = new LinearLayoutManager(mContext);
        recyclerMyOrder.setLayoutManager(layoutManager);
        //设置下拉刷新
        refreshMyOrder.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //设置SwipeRefreshLayout当前是否处于刷新状态，一般在请求数据的时候设置为true，在数据被加载到View中后，设置为false
                refreshMyOrder.setRefreshing(false);
                //更新页数
                mPageNo = 1;
                //更新数据
                myOrderParam.setPageNo(mPageNo);
                apiManager.myItems(myOrderParam);
            }
        });
        int space = ScreenUtil.dip2px(mContext, 6.7f);
        recyclerMyOrder.addItemDecoration(new SpacesItemDecoration(space));

        adapter = new MyOrderAdapter(mContext);
        recyclerMyOrder.setAdapter(adapter);

        //设置RecycleView的滚动监听事件
        recyclerMyOrder.setOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                /**
                 * 当RecycleView的状态为SCROLL_STATE_IDLE表示停下不动
                 * && 最后一项+1的数量等于适配器item的数量的时候，
                 * && 第一次加载的数据量大于Constant.PAGE_SIZE（每页显示的数量）的时候，（这个是在数据少于10的情况下不需要加载第二页）
                 * 说明已经到了本页最后一项了，此时就可以更新页数加载更多数据。
                 */
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == adapter.getItemCount()
                        && adapter.getItemCount() >= Constant.PAGE_SIZE) {
                    refreshMyOrder.setRefreshing(false);//设置下拉刷新为false
                    mPageNo++;//页数加一
                    //更新数据
                    myOrderParam.setPageNo(mPageNo);
                    apiManager.myItems(myOrderParam);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
    }

    @Override
    protected void initData() {
        //调用“我的订单”接口
        myOrderParam = new MyOrderParam();
        myOrderParam.setToken(SettingUtils.get(mContext, "token", null));
        myOrderParam.setPageNo(mPageNo);
        myOrderParam.setPageSize(Constant.PAGE_SIZE);
        apiManager.myItems(myOrderParam);
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        finish();
    }

    /**
     * 查看“我的订单”列表
     *
     * @param result
     */
    public void onEventMainThread(MyOrderResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            OrderData orderData = result.getData();
            List<Order> list = orderData.getList();
            if (orderData.getPageNo() == 1) {
                adapter.setData(list);
            } else {
                adapter.addData(list);
            }
            adapter.notifyDataSetChanged();
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

}
