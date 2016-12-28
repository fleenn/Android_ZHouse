package com.zfb.house.ui;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.emchat.Constant;
import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.event.RefreshMomentsEvent;
import com.lemon.model.BaseResult;
import com.lemon.model.StatusCode;
import com.lemon.util.ParamUtils;
import com.lemon.util.ScreenUtil;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.MyMomentsAdapter;
import com.zfb.house.component.SpacesItemDecoration;
import com.zfb.house.model.bean.MomentsContent;
import com.zfb.house.model.bean.MomentsData;
import com.zfb.house.model.param.CollectParam;
import com.zfb.house.model.param.FriendMomentsParam;
import com.zfb.house.model.param.MyMomentDeleteParam;
import com.zfb.house.model.param.MyMomentsParam;
import com.zfb.house.model.param.PraiseParam;
import com.zfb.house.model.result.CollectResult;
import com.zfb.house.model.result.DeleteCollectResult;
import com.zfb.house.model.result.FriendsMomentsResult;
import com.zfb.house.model.result.MyMomentDeleteResult;
import com.zfb.house.model.result.MyMomentsResult;
import com.zfb.house.model.result.PraiseResult;
import com.zfb.house.util.ToolUtil;

import java.util.List;

/**
 * 我的房友圈 or Ta的房友圈
 * Created by Administrator on 2016/5/25.
 */
@Layout(id = R.layout.activity_my_moments)
public class MyMomentsActivity extends LemonActivity {

    private static final String TAG = "MyMomentsActivity";
    //页数
    public int mPageNo = 1;

    //RecyclerView
    @FieldView(id = R.id.recycler_my_moments)
    private RecyclerView recyclerMyMoments;
    //下拉刷新
    @FieldView(id = R.id.refresh_my_moments)
    private SwipeRefreshLayout refreshMyMoments;
    //我的房友圈的请求参数
    private MyMomentsParam myMomentsParam;
    //好友的房友圈的请求参数
    private FriendMomentsParam friendMomentsParam;
    //我的房友圈的适配器
    private MyMomentsAdapter adapter;
    private LinearLayoutManager layoutManager;
    // 标记是否要查看好友的房友圈
    private Boolean isFriend;
    //好友房友圈的ID
    private String friendId;
    private String token;

    @Override
    protected void initView() {
        Intent intent = getIntent();
        isFriend = intent.getBooleanExtra("isFriend", false);
        friendId = intent.getStringExtra("friendId");
        if (isFriend) {
            setCenterText(intent.getStringExtra("friendName") + "的房友圈");
        } else {
            setCenterText(R.string.img_mine_moments);
        }
        token = SettingUtils.get(mContext, "token", null);

        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerMyMoments.setHasFixedSize(true);
        // 为RecycleView设置默认的线性LayoutManage
        layoutManager = new LinearLayoutManager(mContext);
        recyclerMyMoments.setLayoutManager(layoutManager);
        //设置下拉刷新
        refreshMyMoments.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //设置SwipeRefreshLayout当前是否处于刷新状态，一般在请求数据的时候设置为true，在数据被加载到View中后，设置为false
                refreshMyMoments.setRefreshing(false);
                mPageNo = 1;
                if (isFriend) {//查看好友的房友圈
                    friendMomentsParam.setPageNo(mPageNo);
                    apiManager.listO(friendMomentsParam);
                } else {//查看自己的房友圈
                    myMomentsParam.setPageNo(mPageNo);
                    apiManager.listM(myMomentsParam);
                }
            }
        });
        int space = ScreenUtil.dip2px(mContext, 6.7f);
        recyclerMyMoments.addItemDecoration(new SpacesItemDecoration(space));

        adapter = new MyMomentsAdapter(mContext);
        recyclerMyMoments.setAdapter(adapter);
        //设置监听事件
        adapter.setOnRecycleViewListener(new MyMomentsAdapter.OnRecycleViewListener() {
            /**
             * 删除一条房友圈消息的监听事件
             * @param position 当前item的位置
             * @param eliteId 当前item的ID
             */
            @Override
            public void onItemDelete(int position, String eliteId) {
                MyMomentDeleteParam myMomentDeleteParam = new MyMomentDeleteParam();
                myMomentDeleteParam.setToken(token);
                myMomentDeleteParam.setEliteId(eliteId);
                apiManager.deleteHouseElite(myMomentDeleteParam);
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

            /**
             * 点赞
             * @param position 当前item的位置
             * @param momentsContent 当前item的数据
             * @param view 当前item的视图
             */
            @Override
            public void onPraiseClick(int position, MomentsContent momentsContent, View view) {
                //没点过赞的才能点赞
                if (!momentsContent.isPraise()) {
                    PraiseParam praiseParam = new PraiseParam();
                    praiseParam.setToken(token);
                    praiseParam.setEliteId(momentsContent.getId());
                    praiseParam.setPosition(position);
                    praiseParam.setTag(TAG);
                    apiManager.saveHouseElitePrise(praiseParam);
                }
            }

            /**
             * 收藏
             * @param position 当前item的位置
             * @param momentsContent 当前item的数据
             * @param view 当前item的视图
             */
            @Override
            public void onCollectionClick(int position, MomentsContent momentsContent, View view) {
                CollectParam collectParam = new CollectParam();
                collectParam.setEliteId(momentsContent.getId());
                collectParam.setToken(token);
                collectParam.setPosition(position);
                collectParam.setView(view);
                collectParam.setTag(TAG);
                collectParam.getView().setClickable(false);
                if (momentsContent.isCollect()) {
                    apiManager.delHouseEliteCollection(collectParam);
                } else {
                    apiManager.saveHouseEliteCollection(collectParam);
                }
            }
        });

        //设置RecycleView的滚动监听事件
        recyclerMyMoments.setOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    refreshMyMoments.setRefreshing(false);//设置下拉刷新为false
                    mPageNo++;//页数加一
                    //更新数据
                    if (isFriend) {//查看好友的房友圈
                        friendMomentsParam.setPageNo(mPageNo);
                        apiManager.listO(friendMomentsParam);
                    } else {//查看自己的房友圈
                        myMomentsParam.setPageNo(mPageNo);
                        apiManager.listM(myMomentsParam);
                    }
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
        if (isFriend) {//查看好友的房友圈
            friendMomentsParam = new FriendMomentsParam();
            friendMomentsParam.setPageNo(mPageNo);
            friendMomentsParam.setPageSize(Constant.PAGE_SIZE);
            friendMomentsParam.setToken(token);
            friendMomentsParam.setUserid(friendId);
            apiManager.listO(friendMomentsParam);
        } else {//查看自己的房友圈
            myMomentsParam = new MyMomentsParam();
            myMomentsParam.setPageNo(mPageNo);
            myMomentsParam.setPageSize(Constant.PAGE_SIZE);
            myMomentsParam.setToken(token);
            apiManager.listM(myMomentsParam);
        }
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        finish();
    }

    /**
     * 我的房友圈列表 or Ta的房友圈列表
     *
     * @param result
     */
    public void onEventMainThread(BaseResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            if ((result instanceof MyMomentsResult) || (result instanceof FriendsMomentsResult)) {
                MomentsData momentsData = (MomentsData) result.getData();
                List<MomentsContent> list = momentsData.getList();
                if (momentsData.getPageNo() == 1) {
                    adapter.setData(list);
                } else {
                    adapter.addData(list);
                }
                adapter.notifyDataSetChanged();
            }
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

    /**
     * 删除一条房友圈信息
     *
     * @param result
     */
    public void onEventMainThread(MyMomentDeleteResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            ToolUtil.setRefreshing(refreshMyMoments, true, true);
            lemonMessage.sendMessage(R.string.toast_delete_success);
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

    /**
     * 点赞
     *
     * @param result
     */
    public void onEventMainThread(PraiseResult result) {
        PraiseParam praiseParam = (PraiseParam) result.getParam();
        if (!praiseParam.getTag().equals(TAG)) {
            return;
        }
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            int position = praiseParam.getPosition();
            MomentsContent momentsContent = adapter.getData().get(position);
            momentsContent.setPraise(true);
            momentsContent.setPraiseCount(momentsContent.getPraiseCount() + 1);
            adapter.notifyItemChanged(position);
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

    /**
     * 收藏
     *
     * @param result
     */
    public void onEventMainThread(CollectResult result) {
        CollectParam collectParam = (CollectParam) result.getParam();
        if (!collectParam.getTag().equals(TAG)) {
            return;
        }
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            int position = collectParam.getPosition();
            MomentsContent momentsContent = adapter.getData().get(position);
            momentsContent.setCollect(true);
            momentsContent.setCollectionCount(momentsContent.getCollectionCount() + 1);
            adapter.notifyItemChanged(position);
            collectParam.getView().setClickable(true);
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

    /**
     * 取消收藏
     *
     * @param result
     */
    public void onEventMainThread(DeleteCollectResult result) {
        CollectParam collectParam = (CollectParam) result.getParam();
        if (!collectParam.getTag().equals(TAG)) {
            return;
        }
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            int position = collectParam.getPosition();
            MomentsContent momentsContent = adapter.getData().get(position);
            momentsContent.setCollect(false);
            momentsContent.setCollectionCount(momentsContent.getCollectionCount() - 1);
            adapter.notifyItemChanged(position);
            collectParam.getView().setClickable(true);
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

    /**
     * 刷新有改变的item
     *
     * @param event
     */
    public void onEventMainThread(RefreshMomentsEvent event) {
        if (ParamUtils.isEmpty(adapter.getData())) {
            return;
        }
        int count = 0;
        String id = event.getId();
        for (MomentsContent momentsContent : adapter.getData()) {
            if (momentsContent.getId().equals(id)) {
                momentsContent.setPraise(event.isPraise());
                momentsContent.setPraiseCount(event.getPraiseCount());
                if (event.getCollectCount() > momentsContent.getCollectionCount()) {
                    momentsContent.setCollect(true);
                } else if (event.getCollectCount() < momentsContent.getCollectionCount()) {
                    momentsContent.setCollect(false);
                }
                momentsContent.setCollectionCount(event.getCollectCount());
                adapter.notifyItemChanged(count);
                break;
            }
            count++;
        }
    }

}
