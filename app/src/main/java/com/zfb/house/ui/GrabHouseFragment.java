package com.zfb.house.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.google.gson.Gson;
import com.lemon.LemonFragment;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.event.ReFreshGrabListEvent;
import com.lemon.model.BaseResult;
import com.lemon.model.StatusCode;
import com.lemon.util.EventUtil;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.GrabHouseAdapter;
import com.zfb.house.component.LoadMoreRecyclerView;
import com.zfb.house.emchat.temp.ChatUserBean;
import com.zfb.house.model.bean.GrabHouseItem;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.model.param.GrabHouseListParam;
import com.zfb.house.model.param.GrabHouseRentParam;
import com.zfb.house.model.param.GrabHouseSellParam;
import com.zfb.house.model.result.GrabHouseRentListResult;
import com.zfb.house.model.result.GrabHouseRentResult;
import com.zfb.house.model.result.GrabHouseSellListResult;
import com.zfb.house.model.result.GrabHouseSellResult;
import com.zfb.house.util.ToolUtil;

import java.util.List;

/**
 * Created by Snekey on 2016/6/15.
 */
@Layout(id = R.layout.fragment_grab_list)
public class GrabHouseFragment extends LemonFragment {
    public static final String TAG = "GrabCustomerFragment";

    @FieldView(id = R.id.img_status_convert)
    private TextView imgStatusConvert;
    @FieldView(id = R.id.refresh_grab_list)
    private SwipeRefreshLayout refreshGrabList;
    @FieldView(id = R.id.recycler_grab_list)
    private LoadMoreRecyclerView recyclerGrabList;
    @FieldView(id = R.id.rlayout_no_data)
    private RelativeLayout rlayoutNoData;

    private GrabHouseAdapter mAdapter;
    private int mGrabType = 0;//0:rent;1:sell
    private int mRentPageNO = 1;
    private int mSellPageNO = 1;
    private GrabHouseListParam grabHouseParam;
    private String token;

    private GrabHouseAdapter.OnGrabHouseListener onGrabListener = new GrabHouseAdapter.OnGrabHouseListener() {

        @Override
        public void toGrabHouseRent(int position) {
            GrabHouseRentParam grabHouseRentParam = new GrabHouseRentParam();
            grabHouseRentParam.setToken(token);
            grabHouseRentParam.setPosition(position);
            grabHouseRentParam.setHouseId(mAdapter.getData().get(position).getId());
            apiManager.robRentHouse(grabHouseRentParam);
        }

        @Override
        public void toGrabHouseSell(int position) {
            GrabHouseSellParam grabHouseSellParam = new GrabHouseSellParam();
            grabHouseSellParam.setToken(token);
            grabHouseSellParam.setPosition(position);
            grabHouseSellParam.setHouseId(mAdapter.getData().get(position).getId());
            apiManager.robSellHouse(grabHouseSellParam);
        }
    };

    @OnClick(id = R.id.img_status_convert)
    public void toChangeType() {
//            可抢售房/租房
        if (mGrabType == 0) {
            mRentPageNO = 1;
            imgStatusConvert.setText(R.string.label_sell);
            getRentList();
        } else {
            mSellPageNO = 1;
            imgStatusConvert.setText(R.string.label_rent);
            getSellList();
        }
    }

    @Override
    protected void initView() {
        mAdapter = new GrabHouseAdapter(getContext());
        mAdapter.setOnGrabListener(onGrabListener);
        recyclerGrabList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerGrabList.setLayoutManager(layoutManager);
        recyclerGrabList.setAdapter(mAdapter);
        recyclerGrabList.setAutoLoadMoreEnable(true);
        recyclerGrabList.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mGrabType == 0) {
                    mRentPageNO++;
                    getRentList();
                } else {
                    mSellPageNO++;
                    getSellList();
                }
            }
        });
        token = SettingUtils.get(getContext(), "token", null);
        //默认先取租房列表
        getRentList();
        imgStatusConvert.setVisibility(View.VISIBLE);
    }

    //    初始化列表请求参数
    private void initListParam(int pageNO) {
        grabHouseParam = new GrabHouseListParam();
        grabHouseParam.setToken(token);
        grabHouseParam.setPageNo(pageNO);
        grabHouseParam.setPageSize(10);
    }

    //    获取可抢租房列表
    private void getRentList() {
        initListParam(mRentPageNO);
        apiManager.listCanRobRentHouseAndroid(grabHouseParam);
        refreshGrabList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRentPageNO = 1;
                grabHouseParam.setPageNo(mRentPageNO);
                rlayoutNoData.setVisibility(View.GONE);
                apiManager.listCanRobRentHouseAndroid(grabHouseParam);
            }
        });
    }

    //    获取可抢售房列表
    private void getSellList() {
        initListParam(mSellPageNO);
        apiManager.listCanRobSellHouseAndroid(grabHouseParam);
        refreshGrabList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSellPageNO = 1;
                grabHouseParam.setPageNo(mSellPageNO);
                rlayoutNoData.setVisibility(View.GONE);
                apiManager.listCanRobSellHouseAndroid(grabHouseParam);
            }
        });
    }

    /**
     * 调用环信自动发送信息
     * @param fromUser
     * @param toUser
     * @param content
     * @throws EaseMobException
     */
    private void sendToUser(String fromUser,GrabHouseItem toUser,String content) throws EaseMobException {
        //获取到与聊天人的会话对象。参数username为聊天人的userid或者groupid，后文中的username皆是如此
        EMConversation conversation = EMChatManager.getInstance().getConversationByType(toUser.getUserName(), EMConversation.EMConversationType.Chat);
        //创建一条文本消息
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
        //如果是群聊，设置chattype,默认是单聊
        message.setChatType(EMMessage.ChatType.Chat);
        //用户信息bean，包括接收方和发送方
        ChatUserBean fromUserBean = new ChatUserBean();
        ChatUserBean toUserBean = new ChatUserBean();
        //初始化发送方数据
        fromUserBean.setImageUrl(UserBean.getInstance(getActivity()).photo);
        fromUserBean.setUserName(UserBean.getInstance(getActivity()).name);
        fromUserBean.setUserType(1);
        //初始化接收方数据
        toUserBean.setImageUrl(toUser.getContactPhoto());
        toUserBean.setUserName(toUser.getUserName());
        toUserBean.setUserType(0);
        Gson gson = new Gson();
        String fromUserBeanStr = gson.toJson(fromUserBean);
        String toUserBeanStr = gson.toJson(toUserBean);
        message.setAttribute("fromUser", fromUserBeanStr);
        message.setAttribute("toUser", toUserBeanStr);
        TextMessageBody txtBody = new TextMessageBody(content);
        message.addBody(txtBody);
        //设置接收人
        message.setReceipt(toUser.getContactUserId());
        //把消息加入到此会话对象中
        conversation.addMessage(message);
        //发送消息
        EMChatManager.getInstance().sendMessage(message);
    }

    @Override
    public void onEventMainThread(BaseResult result) {
        if (result instanceof GrabHouseRentListResult) {
//            可抢出租房源列表
            if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
                mAdapter.setIsGrabbedList(false);
                mAdapter.setType(0);
                loadMoreRent(result);
                mGrabType = 1;
            }
        } else if (result instanceof GrabHouseSellListResult) {
//            可抢出售房源列表
            if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
                mAdapter.setIsGrabbedList(false);
                mAdapter.setType(1);
                loadMoreSell(result);
                mGrabType = 0;
            }
        }
        refreshGrabList.setRefreshing(false);
    }

    /**
     * 抢租房
     *
     * @param result
     */
    public void onEventMainThread(GrabHouseRentResult result) throws EaseMobException {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            GrabHouseRentParam param = (GrabHouseRentParam) result.getParam();
            int position = param.getPosition();
            mAdapter.getData().get(position).setIsSuccess(true);
            recyclerGrabList.notifiyItemChange(position);
            ToolUtil.updatePoint(getActivity(), result.getData().getTotalPoint(), result.getData().getGetPoint(), R.string.toast_grab_success);
            EventUtil.sendEvent(new ReFreshGrabListEvent(TAG));
            sendToUser(UserBean.getInstance(getActivity()).id, mAdapter.getData().get(position), "您好，我接受了您发布的房源委托。很高兴为您服务！");
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

    /**
     * 抢售房
     *
     * @param result
     */
    public void onEventMainThread(GrabHouseSellResult result) throws EaseMobException {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            GrabHouseSellParam param = (GrabHouseSellParam) result.getParam();
            int position = param.getPosition();
            mAdapter.getData().get(position).setIsSuccess(true);
            recyclerGrabList.notifiyItemChange(position);
            ToolUtil.updatePoint(getActivity(), result.getData().getTotalPoint(), result.getData().getGetPoint(), R.string.toast_grab_success);
            EventUtil.sendEvent(new ReFreshGrabListEvent(TAG));
            sendToUser(UserBean.getInstance(getActivity()).id, mAdapter.getData().get(position), "您好，我接受了您发布的房源委托。很高兴为您服务！");
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }


    //    加载更多租房
    private void loadMoreRent(BaseResult result) {
        if (ParamUtils.isNull(result.getData())) {
            recyclerGrabList.notifyMoreFinish(false);
            rlayoutNoData.setVisibility(View.VISIBLE);
            return;
        }
        List<GrabHouseItem> list = ((GrabHouseRentListResult) result).getData().getList();
        if (mRentPageNO == 1) {
            mAdapter.setData(list);
            recyclerGrabList.notifiyChange();
        } else {
            mAdapter.addDatas(list);
        }
        //无数据提示
        rlayoutNoData.setVisibility(ParamUtils.isEmpty(list) ? View.VISIBLE : View.GONE);
        if (ParamUtils.isEmpty(list) || list.size() < 10) {
            recyclerGrabList.notifyMoreFinish(false);
        } else {
            recyclerGrabList.notifyMoreFinish(true);
        }
    }

    //    加载更多售房
    private void loadMoreSell(BaseResult result) {
        if (ParamUtils.isNull(result.getData())) {
            recyclerGrabList.notifyMoreFinish(false);
            rlayoutNoData.setVisibility(View.VISIBLE);
            return;
        }
        List<GrabHouseItem> list = ((GrabHouseSellListResult) result).getData().getList();

        if (mSellPageNO == 1) {
            mAdapter.setData(list);
            recyclerGrabList.notifiyChange();
        } else {
            mAdapter.addDatas(list);
            recyclerGrabList.notifyMoreFinish(true);
        }
        //无数据提示
        rlayoutNoData.setVisibility(ParamUtils.isEmpty(list) ? View.VISIBLE : View.GONE);
        if (ParamUtils.isEmpty(list) || list.size() < 10) {
            recyclerGrabList.notifyMoreFinish(false);
        } else {
            recyclerGrabList.notifyMoreFinish(true);
        }
    }

    public void setRefreshing() {
        if (refreshGrabList != null) {
            ToolUtil.setRefreshing(refreshGrabList, true, true);
        }
    }
}
