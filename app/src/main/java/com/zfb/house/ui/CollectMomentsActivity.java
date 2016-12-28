package com.zfb.house.ui;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.android.emchat.Constant;
import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.model.StatusCode;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.CollectMomentsAdapter;
import com.zfb.house.component.LoadMoreRecyclerView;
import com.zfb.house.model.bean.MomentsContent;
import com.zfb.house.model.param.CollectMomentsParam;
import com.zfb.house.model.param.CollectParam;
import com.zfb.house.model.result.CollectMomentsResult;
import com.zfb.house.model.result.DeleteCollectResult;
import com.zfb.house.util.ToolUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的->收藏的房友圈
 * Created by Administrator on 2016/6/21.
 */
@Layout(id = R.layout.activity_collect_moments)
public class CollectMomentsActivity extends LemonActivity {

    private static final String TAG = "CollectMomentsActivity";
    //页数
    private int collectPageNo = 1;
    //搜索页数
    private int searchPageNo = 1;
    private String token;
    //用户输入的文字
    private String mSearchContent;
    @FieldView(id = R.id.edt_search_content)
    private EditText edtContent;
    //提示找不到结果
    @FieldView(id = R.id.rlayout_hint)
    private RelativeLayout rlayoutHint;
    //是否正在搜索，默认为false——没有在搜素
    private boolean isSearch;
    //收藏的房友圈列表
    private List<MomentsContent> collectList = new ArrayList<>();
    //搜索出来的房友圈列表
    private List<MomentsContent> searchList = new ArrayList<>();

    //自定义RecyclerView
    @FieldView(id = R.id.recycler_collect_moments)
    private LoadMoreRecyclerView recyclerCollectMoments;
    //下拉刷新
    @FieldView(id = R.id.refresh_collect_moments)
    private SwipeRefreshLayout swipeRefreshLayout;
    //收藏的房友圈的请求参数
    private CollectMomentsParam collectMomentsParam;
    //收藏的房友圈的适配器
    private CollectMomentsAdapter collectMomentsAdapter;

    @Override
    protected void initView() {
        setCenterText(R.string.img_mine_collect_moments);
        token = SettingUtils.get(mContext, "token", null);

        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerCollectMoments.setHasFixedSize(true);
        // 为RecycleView设置默认的线性LayoutManage
        recyclerCollectMoments.setLayoutManager(new LinearLayoutManager(mContext));

        //设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);//关闭下拉刷新
                if (isSearch) {
                    searchPageNo = 1; //初始化页数为1
                    collectMomentsParam.setPageNo(searchPageNo);//更新页数
                    collectMomentsParam.setInclude(mSearchContent);
                } else {
                    collectPageNo = 1; //初始化页数为1
                    collectMomentsParam.setPageNo(collectPageNo);//更新页数
                }
                apiManager.listMC(collectMomentsParam);//通知服务器更新页数
            }
        });
        //设置上拉加载
        recyclerCollectMoments.setAutoLoadMoreEnable(true);
        recyclerCollectMoments.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                swipeRefreshLayout.setRefreshing(false);//关闭下拉刷新
                if (isSearch) {
                    searchPageNo++;//页数加一
                    collectMomentsParam.setPageNo(searchPageNo);//更新页数
                    collectMomentsParam.setInclude(mSearchContent);
                } else {
                    collectPageNo++;//页数加一
                    collectMomentsParam.setPageNo(collectPageNo);//更新页数
                }
                apiManager.listMC(collectMomentsParam);//通知服务器更新页数
            }
        });

        collectMomentsAdapter = new CollectMomentsAdapter(mContext);
        collectMomentsAdapter.setmOnRecycleViewListener(new CollectMomentsAdapter.OnRecycleViewListener() {
            /**
             * 删除一条收藏的房友圈的监听事件
             * @param position 当前item的位置
             * @param eliteId 当前item的ID
             */
            @Override
            public void onItemDelete(int position, String eliteId) {
                //调用“取消收藏的一条房友圈消息”接口
                CollectParam collectParam = new CollectParam();
                collectParam.setEliteId(eliteId);
                collectParam.setToken(token);
                collectParam.setPosition(position);
                collectParam.setTag(TAG);
                apiManager.delHouseEliteCollection(collectParam);
                ToolUtil.setRefreshing(swipeRefreshLayout, true, true);
            }

            /**
             * 查询当前这条房友圈消息详情页面的监听事件
             * @param position 当前item的位置
             * @param eliteId 当前item的ID
             */
            @Override
            public void onItemClick(int position, String eliteId) {
                Intent intent = new Intent(mContext, MomentsDetailActivity.class);
                intent.putExtra("id", eliteId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        //查看“收藏房友圈消息”接口
        setData(collectPageNo, isSearch);

        // 添加监听用户输入文字的事件
        edtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence str, int start, int before, int count) {
                mSearchContent = str.toString();
                //隐藏提示
                rlayoutHint.setVisibility(View.GONE);
                //清空搜索到的列表
                searchList.clear();
                //设置在搜素
                isSearch = true;
                if (!ParamUtils.isEmpty(mSearchContent)) {//有输入值
                    //把搜索页数恢复到1
                    searchPageNo = 1;
                    setData(searchPageNo, isSearch);
                } else {//无输入值
                    //设置无搜索
                    isSearch = false;
                    //把页数恢复到1
                    collectPageNo = 1;
                    setData(collectPageNo, isSearch);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        //手动隐藏软键盘
        hideKeyboard();
        finish();
    }

    /**
     * 请求服务器获取数据
     *
     * @param mPageNo  页数
     * @param isSearch 是否正在搜索
     */
    private void setData(int mPageNo, boolean isSearch) {
        collectMomentsParam = new CollectMomentsParam();
        collectMomentsParam.setToken(token);
        collectMomentsParam.setPageNo(mPageNo);
        collectMomentsParam.setPageSize(Constant.PAGE_SIZE);
        if (isSearch) {
            collectMomentsParam.setInclude(mSearchContent);
        }
        apiManager.listMC(collectMomentsParam);
    }

    /**
     * 设置收藏或搜索房友圈的数据列表
     *
     * @param list   列表
     * @param pageNo 页数
     */
    private void setDataList(List<MomentsContent> list, int pageNo) {
        if (pageNo == 1) {
            collectMomentsAdapter.setData(list);
            recyclerCollectMoments.setAdapter(collectMomentsAdapter);
            collectMomentsAdapter.notifyDataSetChanged();
        } else {
            collectMomentsAdapter.addDatas(list);
            recyclerCollectMoments.notifyMoreFinish(true);
        }
        if (ParamUtils.isEmpty(list) || list.size() < Constant.PAGE_SIZE) {
            recyclerCollectMoments.notifyMoreFinish(false);
        } else {
            recyclerCollectMoments.notifyMoreFinish(true);
        }
    }

    /**
     * 收藏房友圈的列表
     *
     * @param result
     */
    public void onEventMainThread(CollectMomentsResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            if (isSearch) {//如果在搜索…
                searchList = result.getData().getList();
                if (searchList.size() == 0) {//无搜索结果
                    setDataList(null, searchPageNo);
                    rlayoutHint.setVisibility(View.VISIBLE);
                } else {//有搜索结果
                    setDataList(searchList, searchPageNo);
                    rlayoutHint.setVisibility(View.GONE);
                }
            } else {
                collectList = result.getData().getList();
                setDataList(collectList, collectPageNo);
            }
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

    /**
     * 删除收藏的某条房友圈
     *
     * @param result
     */
    public void onEventMainThread(DeleteCollectResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            if (!((CollectParam) result.getParam()).getTag().equals(TAG)) {
                return;
            }
            lemonMessage.sendMessage(R.string.toast_delete_success);
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

}
