package com.zfb.house.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.emchat.Constant;
import com.lemon.LemonContext;
import com.lemon.LemonFragment;
import com.lemon.LemonMessage;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.event.AnonLoginEvent;
import com.lemon.event.LocChangeEvent;
import com.lemon.event.RefreshEvent;
import com.lemon.event.RefreshMomentsEvent;
import com.lemon.model.BaseResult;
import com.lemon.model.StatusCode;
import com.lemon.util.EventUtil;
import com.lemon.util.ParamUtils;
import com.lemon.util.ScreenUtil;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.MomentsAdapter;
import com.zfb.house.component.LoadMoreRecyclerView;
import com.zfb.house.component.SpacesItemDecoration;
import com.zfb.house.emchat.ChatInputLayout;
import com.zfb.house.emchat.temp.OnKeyWordsClick;
import com.zfb.house.model.bean.MomentsContent;
import com.zfb.house.model.bean.MomentsData;
import com.zfb.house.model.bean.NewReplyContent;
import com.zfb.house.model.bean.ReplyContent;
import com.zfb.house.model.param.CollectParam;
import com.zfb.house.model.param.DeleteReplyParam;
import com.zfb.house.model.param.MomentsParam;
import com.zfb.house.model.param.MomentsReplyParam;
import com.zfb.house.model.param.PraiseParam;
import com.zfb.house.model.result.CollectResult;
import com.zfb.house.model.result.DeleteCollectResult;
import com.zfb.house.model.result.DeleteReplyResult;
import com.zfb.house.model.result.MomentsReplyResult;
import com.zfb.house.model.result.NeighborMomentsResult;
import com.zfb.house.model.result.PraiseResult;
import com.zfb.house.util.ToolUtil;

import java.util.List;

/**
 * Created by Snekey on 2016/5/23.
 */
@Layout(id = R.layout.fragment_neighbor_moments)
public class NeighborMomentsFragment extends LemonFragment {
    private static String TAG = "NeighborMomentsFragment";

    MomentsAdapter momentsAdapter;
    protected String token;
    protected String lat;
    protected String lng;

    @FieldView(id = R.id.recycler_moments)
    protected LoadMoreRecyclerView recyclerMoments;
    @FieldView(id = R.id.refresh_layout)
    protected SwipeRefreshLayout swipeRefreshLayout;
    @FieldView(id = R.id.chat_moments)
    protected ChatInputLayout chatMoments;
    @FieldView(id = R.id.rlayout_no_data)
    private RelativeLayout rlayoutNoData;
    //    设置位置信息
    @FieldView(id = R.id.llayout_location)
    private LinearLayout llayoutLocation;

    private MomentsParam momentsParam;

    public int mPageNo = 1;

    @Override
    protected void initView() {
        chatMoments.setBaseActivity(getActivity());
        recyclerMoments.setHasFixedSize(true);
        recyclerMoments.addOnScrollListener(new MyRecyclerViewScrollListener());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerMoments.setLayoutManager(layoutManager);
        momentsAdapter = new MomentsAdapter(getContext());
        recyclerMoments.setAdapter(momentsAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageNo = 1;
                momentsParam.setPageNo(mPageNo);
                momentsParam.setLat(lat);
                momentsParam.setLng(lng);
                momentsParam.setRange(MomentsFragment.RANGE);
                rlayoutNoData.setVisibility(View.GONE);
                apiManager.listN(momentsParam);
            }
        });
        ToolUtil.setRefreshing(swipeRefreshLayout, true, true);
        int space = ScreenUtil.dip2px(getActivity(), 6.7f);
        recyclerMoments.addItemDecoration(new SpacesItemDecoration(space));
        recyclerMoments.setAutoLoadMoreEnable(true);
        recyclerMoments.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                swipeRefreshLayout.setRefreshing(false);
                mPageNo++;
                momentsParam.setPageNo(mPageNo);
                momentsParam.setLat(lat);
                momentsParam.setLng(lng);
                momentsParam.setRange(MomentsFragment.RANGE);
                apiManager.listN(momentsParam);
            }
        });
        llayoutLocation.setVisibility(View.VISIBLE);
        llayoutLocation.setAlpha(0.9f);
    }

    @Override
    protected void initData() {
        token = SettingUtils.get(getActivity(), "token", "");
        if (SettingUtils.get(getActivity(), Constant.IS_CUSTOMIZE, false)) {
            lat = SettingUtils.get(getActivity(), Constant.CUSTOMIZE_LAT, Constant.DEFAULT_LAT) + "";
            lng = SettingUtils.get(getActivity(), Constant.CUSTOMIZE_LNG, Constant.DEFAULT_LNG) + "";
        } else {
            lat = SettingUtils.get(getActivity(), Constant.LAT, Constant.DEFAULT_LAT) + "";
            lng = SettingUtils.get(getActivity(), Constant.LNG, Constant.DEFAULT_LNG) + "";
        }
        momentsAdapter.setOnFunctionClickListener(mFunctionClickListener);
        momentsParam = new MomentsParam();
        momentsParam.setToken(token);
        momentsParam.setPageNo(mPageNo);
        momentsParam.setPageSize(Constant.PAGE_SIZE);
        momentsAdapter.setOnRecyclerViewListener(onRecyclerViewListener);
        View view = inflater.inflate(R.layout.head_neighbor_moments, null);
        recyclerMoments.addHeaderView(view);
        recyclerMoments.setHeaderEnable(true);
        momentsAdapter.setIsContainHead(true);
    }

    public void setRefreshing() {
        if (swipeRefreshLayout != null) {
            ToolUtil.setRefreshing(swipeRefreshLayout, true, true);
        }
    }

    private class MyRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (chatMoments != null && dy != 0) {
                chatMoments.setVisibility(View.GONE);
                chatMoments.hideKeyboard();
            }
        }
    }

    @OnClick(id = R.id.llayout_location)
    public void toLocate() {
        Intent intent = new Intent(getContext(), MapActivity.class);
        startActivityForResult(intent, MomentsFragment.REQUEST_LOCATE);
    }

    private class MyOnKeyWordsClick implements OnKeyWordsClick {
        //        房友圈bean
        MomentsContent momentsContent;
        //        评论区bean
        ReplyContent mReplyContent;
        int mPosition;


        MyOnKeyWordsClick(MomentsContent momentsContent, ReplyContent replyContent, int position) {
            this.momentsContent = momentsContent;
            this.mReplyContent = replyContent;
            this.mPosition = position;
        }

        @Override
        public void onItemClick(Object keyWord) {
            if (chatMoments != null) {
                chatMoments.setVisibility(View.GONE);
                chatMoments.hideKeyboard();
            }

            if (keyWord == null || TextUtils.isEmpty(keyWord.toString().trim())) {
                LemonContext.getBean(LemonMessage.class).sendMessage("不能发送空字符!");
                return;
            }
            Log.i("comment", "-- MyOnKeyWordsClick send comment keyWord:" + keyWord + " , id:" + momentsContent.getId());
            String replyUserId = null;
            if (mReplyContent != null) {
                replyUserId = mReplyContent.getUserId();
            }
            MomentsReplyParam momentsReplyParam = new MomentsReplyParam();
            momentsReplyParam.setEliteId(momentsContent.getId());
            momentsReplyParam.setReplyContent(keyWord.toString());
            momentsReplyParam.setReplyUserId(replyUserId);
            momentsReplyParam.setToken(token);
            momentsReplyParam.setPosition(mPosition);
            momentsReplyParam.setTag(TAG);
            apiManager.saveHouseEliteReply(momentsReplyParam);
        }
    }

    //评论信息  监听事件
    private MomentsAdapter.OnFunctionClickListener mFunctionClickListener =
            new MomentsAdapter.OnFunctionClickListener() {

                @Override
                public void OnCommentClick(View view, MomentsContent momentsContent, ReplyContent replyContent, int position) {
                    if (ParamUtils.isEmpty(token)) {
                        EventUtil.sendEvent(new AnonLoginEvent());
                        return;
                    }
                    if (view != null && recyclerMoments != null) {
                        int[] location = new int[2];
                        view.getLocationInWindow(location);
                        int y = location[1] - ScreenUtil.dip2px(getActivity(), 250);
                        if (y <= 0) {
                            y = 0;
                        }
                        recyclerMoments.scrollBy(0, y);
                    }
                    /** 点击列表子项，准备评论 */
                    if (chatMoments != null) {
                        chatMoments.setVisibility(View.VISIBLE);
                        chatMoments.showKeyBoard();
                        if (replyContent != null) {
                            //回复人的信息    id 和 name
                            chatMoments.getEditText().setHint("回复" + replyContent.getUserName());
                        } else {
                            chatMoments.getEditText().setHint("");
                        }
                        chatMoments.setOnKeyWordsClick(new MyOnKeyWordsClick(momentsContent, replyContent, position));
                    }
                }

                @Override
                public void OnCommentDelete(final ReplyContent replyContent, final int position) {
                    new AlertDialog
                            .Builder(getContext())
                            .setTitle("删除评论")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DeleteReplyParam deleteReplyParam = new DeleteReplyParam();
                                    deleteReplyParam.setReplyId(replyContent.getId());
                                    deleteReplyParam.setToken(token);
                                    deleteReplyParam.setPosition(position);
                                    deleteReplyParam.setReplyContent(replyContent);
                                    deleteReplyParam.setTag(TAG);
                                    apiManager.deleteHouseEliteReply(deleteReplyParam);
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                }
            };


    /**
     * 更新列表数据（上拉加载或者下拉刷新）
     *
     * @param result
     */
    private void updateList(BaseResult result) {
        if (ParamUtils.isNull(result.getData())) {
            recyclerMoments.notifyMoreFinish(false);
            rlayoutNoData.setVisibility(View.VISIBLE);
            return;
        }
        MomentsData momentsData = (MomentsData) result.getData();
        List<MomentsContent> list = momentsData.getList();
        if (mPageNo == 1) {
            momentsAdapter.setData(list);
            recyclerMoments.notifiyChange();
        } else {
            momentsAdapter.addDatas(list);
        }
        //无数据提示
        rlayoutNoData.setVisibility(ParamUtils.isEmpty(list) ? View.VISIBLE : View.GONE);
        if (ParamUtils.isEmpty(list) || list.size() < Constant.PAGE_SIZE) {
            recyclerMoments.notifyMoreFinish(false);
        } else {
            recyclerMoments.notifyMoreFinish(true);
        }
    }

    protected MomentsAdapter.OnRecyclerViewListener onRecyclerViewListener = new MomentsAdapter.OnRecyclerViewListener() {
        @Override
        public void onHeadClick(int position) {
            MomentsContent item = momentsAdapter.getmDate().get(position - 1);
            toDetail(item.getType(), item.getUserId(), ParamUtils.isEmpty(item.getRemark()) ? item.getUserName() : item.getRemark(), position - 1);
        }

        @Override
        public void toPraise(int position) {
            if (ParamUtils.isEmpty(token)) {
                EventUtil.sendEvent(new AnonLoginEvent());
                return;
            }
            MomentsContent momentsContent = momentsAdapter.getmDate().get(position - 1);
            PraiseParam praiseParam = new PraiseParam();
            praiseParam.setToken(token);
            praiseParam.setEliteId(momentsContent.getId());
            praiseParam.setPosition(position);
            praiseParam.setTag(TAG);
            apiManager.saveHouseElitePrise(praiseParam);
        }

        @Override
        public void toCollect(int position, View v) {
            if (ParamUtils.isEmpty(token)) {
                EventUtil.sendEvent(new AnonLoginEvent());
                return;
            }
            MomentsContent momentsContent = momentsAdapter.getmDate().get(position - 1);
            CollectParam collectParam = new CollectParam();
            collectParam.setEliteId(momentsContent.getId());
            collectParam.setToken(token);
            collectParam.setPosition(position);
            collectParam.setView(v);
            collectParam.setTag(TAG);
            if (momentsContent.isCollect()) {
                apiManager.delHouseEliteCollection(collectParam);
            } else {
                apiManager.saveHouseEliteCollection(collectParam);
            }
        }

        @Override
        public boolean onItemLongClick(int position) {
            return false;
        }
    };

    /**
     * 获取房友圈列表
     *
     * @param result
     */
    public void onEventMainThread(NeighborMomentsResult result) {
        updateList(result);
        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * 添加评论
     *
     * @param result
     */
    public void onEventMainThread(MomentsReplyResult result) {
        if (((MomentsReplyParam) result.getParam()).getTag().equals(TAG)) {
            if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
                MomentsReplyParam param = (MomentsReplyParam) result.getParam();
                int position = param.getPosition();
                MomentsContent momentsContent;
                momentsContent = momentsAdapter.getmDate().get(position - 1);
                NewReplyContent newReplyContent = result.getData();
                ReplyContent reply = newReplyContent.getReply();
                int count = newReplyContent.getCount();
                momentsContent.setReplyCount(count);
                momentsContent.getHouseEliteReply().getList().add(reply);
                ToolUtil.updatePoint(getActivity(), result.getData().getTotalPoint(), result.getData().getGetPoint());
                recyclerMoments.notifiyItemChange(position);
            }
        }
    }

    /**
     * 删除评论
     *
     * @param result
     */
    public void onEventMainThread(DeleteReplyResult result) {
        if (((DeleteReplyParam) result.getParam()).getTag().equals(TAG)) {
            if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
                DeleteReplyParam param = (DeleteReplyParam) result.getParam();
                int position = param.getPosition();
                momentsAdapter.getmDate().get(position - 1).getHouseEliteReply().getList().remove(param.getReplyContent());
                recyclerMoments.notifiyItemChange(position);
            } else {
                lemonMessage.sendMessage(R.string.toast_delete_reply_fail);
            }
        }
    }

    /**
     * 点赞
     *
     * @param result
     */
    public void onEventMainThread(PraiseResult result) {
        if (((PraiseParam) result.getParam()).getTag().equals(TAG)) {
            if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
                PraiseParam param = (PraiseParam) result.getParam();
                int position = param.getPosition();
                MomentsContent momentsContent;
                momentsContent = momentsAdapter.getmDate().get(position - 1);
                momentsContent.setPraise(true);
                int count = momentsContent.getPraiseCount() + 1;
                momentsContent.setPraiseCount(count);
                recyclerMoments.notifiyItemChange(position);
            } else {
                lemonMessage.sendMessage(R.string.toast_praise_fail);
            }
        }
    }

    /**
     * 收藏
     *
     * @param result
     */
    public void onEventMainThread(CollectResult result) {
        if (((CollectParam) result.getParam()).getTag().equals(TAG)) {
            if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
                CollectParam param = (CollectParam) result.getParam();
                int position = param.getPosition();
                MomentsContent momentsContent;
                momentsContent = momentsAdapter.getmDate().get(position - 1);
                momentsContent.setCollect(true);
                int count = momentsContent.getCollectionCount() + 1;
                momentsContent.setCollectionCount(count);
                recyclerMoments.notifiyItemChange(position);
                param.getView().setClickable(true);
            } else {
                lemonMessage.sendMessage(R.string.toast_collect_fail);
            }
        }
    }

    /**
     * 取消收藏
     *
     * @param result
     */
    public void onEventMainThread(DeleteCollectResult result) {
        if (((CollectParam) result.getParam()).getTag().equals(TAG)) {
            if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
                CollectParam param = (CollectParam) result.getParam();
                int position = param.getPosition();
                MomentsContent momentsContent;
                momentsContent = momentsAdapter.getmDate().get(position - 1);
                int count = momentsContent.getCollectionCount() - 1;
                momentsContent.setCollect(false);
                momentsContent.setCollectionCount(count);
                recyclerMoments.notifiyItemChange(position);
                param.getView().setClickable(true);
            } else {
                lemonMessage.sendMessage(R.string.toast_delete_collect_fail);
            }
        }
    }

    /**
     * 定位信息改变的时候刷新附近的房友圈，与改变的位置响应
     *
     * @param event
     */
    public void onEventMainThread(LocChangeEvent event) {
        if (event.getTag().equals(CallActivity.Tag)) {
            lat = String.valueOf(SettingUtils.get(getActivity(), Constant.LAT, Constant.DEFAULT_LAT));
            lng = String.valueOf(SettingUtils.get(getActivity(), Constant.LNG, Constant.DEFAULT_LNG));
            setRefreshing();
        }
    }

    /**
     * 刷新有改变的item
     *
     * @param event
     */
    public void onEventMainThread(RefreshMomentsEvent event) {
        String id = event.getId();
        int count = 1;
        if (ParamUtils.isEmpty(momentsAdapter.getmDate())) {
            return;
        }
        for (MomentsContent momentsContent : momentsAdapter.getmDate()) {
            if (momentsContent.getId().equals(id)) {
                momentsContent.setPraise(event.isPraise());
                momentsContent.setPraiseCount(event.getPraiseCount());
                if (event.getCollectCount() > momentsContent.getCollectionCount()) {
                    momentsContent.setCollect(true);
                } else if (event.getCollectCount() < momentsContent.getCollectionCount()) {
                    momentsContent.setCollect(false);
                }
                momentsContent.setCollectionCount(event.getCollectCount());
                recyclerMoments.notifiyItemChange(count);
                break;
            }
            count++;
        }
    }

    public void onEventMainThread(RefreshEvent event) {
        token = "";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1) {
            switch (requestCode) {
                case Constant.REQUEST_MODIFY_REMARKS:
                    //获得当前点击的item的位置position
                    int position = data.getIntExtra("position", 0);
                    momentsAdapter.getmDate().get(position).setRemark(data.getStringExtra("remark"));
                    recyclerMoments.notifiyItemChange(position + 1);
                    break;
                case MomentsFragment.REQUEST_LOCATE:
                    lat = String.valueOf(data.getDoubleExtra("lat", 0.0));
                    lng = String.valueOf(data.getDoubleExtra("lng", 0.0));
                    setRefreshing();
                    break;
                default:
                    break;
            }
        }
    }
}
