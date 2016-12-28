package com.zfb.house.ui;

import android.annotation.SuppressLint;
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
import com.lemon.event.ReFreshGrabListEvent;
import com.lemon.model.StatusCode;
import com.lemon.util.EventUtil;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.GrabCustomerAdapter;
import com.zfb.house.component.LoadMoreRecyclerView;
import com.zfb.house.emchat.temp.ChatUserBean;
import com.zfb.house.model.bean.GrabCustomerItem;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.model.param.GrabCustomerListParam;
import com.zfb.house.model.param.GrabCustomerParam;
import com.zfb.house.model.result.GrabCustomerListResult;
import com.zfb.house.model.result.GrabCustomerResult;
import com.zfb.house.util.ToolUtil;

import java.util.List;

/**
 * Created by Snekey on 2016/6/15.
 */
@SuppressLint("ValidFragment")
@Layout(id = R.layout.fragment_grab_list)
public class GrabCustomerFragment extends LemonFragment {
    public static final String TAG = "GrabCustomerFragment";

    @FieldView(id = R.id.img_status_convert)
    private TextView imgStatusConvert;
    @FieldView(id = R.id.refresh_grab_list)
    private SwipeRefreshLayout refreshGrabList;
    @FieldView(id = R.id.recycler_grab_list)
    private LoadMoreRecyclerView recyclerGrabList;
    @FieldView(id = R.id.rlayout_no_data)
    private RelativeLayout rlayoutNoData;
    private GrabCustomerAdapter mAdapter = null;
    private int mFlag;
    private GrabCustomerListParam grabCustomerListParam;
    private String token;
    private GrabCustomerAdapter.OnGrabCustomerListener onGrabCustomerListener = new GrabCustomerAdapter.OnGrabCustomerListener() {
        @Override
        public void toGrabCustomer(int position) {
            GrabCustomerParam grabCustomerParam = new GrabCustomerParam();
            grabCustomerParam.setToken(token);
            grabCustomerParam.setOrderId(mAdapter.getData().get(position).getOrderId());
            grabCustomerParam.setPosition(position);
            grabCustomerParam.setCustomerId(mAdapter.getData().get(position).getCustomerId());
            apiManager.robOrder(grabCustomerParam);
        }
    };

    public GrabCustomerFragment() {
    }

    public GrabCustomerFragment(int flag) {
        this.mFlag = flag;
    }

    @Override
    protected void initView() {
        mAdapter = new GrabCustomerAdapter(getActivity());
        imgStatusConvert.setVisibility(View.GONE);
        mAdapter.setOnGrabCustomerListener(onGrabCustomerListener);
        recyclerGrabList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerGrabList.setLayoutManager(layoutManager);
        recyclerGrabList.setAdapter(mAdapter);
        recyclerGrabList.setAutoLoadMoreEnable(false);
        token = SettingUtils.get(getContext(), "token", "");
        switch (mFlag) {
            case 0:
                grabCustomerListParam = new GrabCustomerListParam();
                grabCustomerListParam.setHandleType("2");
                grabCustomerListParam.setToken(token);
                apiManager.listOrder(grabCustomerListParam);
                refreshGrabList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        rlayoutNoData.setVisibility(View.GONE);
                        apiManager.listOrder(grabCustomerListParam);
                    }
                });
                imgStatusConvert.setVisibility(View.GONE);
                break;
            case 1:
                grabCustomerListParam = new GrabCustomerListParam();
                grabCustomerListParam.setHandleType("1");
                grabCustomerListParam.setToken(token);
                apiManager.listOrder(grabCustomerListParam);
                refreshGrabList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        rlayoutNoData.setVisibility(View.GONE);
                        apiManager.listOrder(grabCustomerListParam);
                    }
                });
                imgStatusConvert.setVisibility(View.GONE);
                break;
            default:
                break;
        }

    }

    public void onEventMainThread(GrabCustomerListResult result) {
        GrabCustomerListParam param = (GrabCustomerListParam) result.getParam();
        if (param.getHandleType().equals("2") && mFlag == GrabActivity.GRAB_CUSTOMER) {
//                可抢客户列表
            refreshCustomerList(result);
            mAdapter.setIsGrabbed(false);
        } else if (param.getHandleType().equals("1") && mFlag == GrabActivity.GRABBED_CUSTOMER) {
//                已抢客户列表
            refreshCustomerList(result);
            mAdapter.setIsGrabbed(true);
        }
        refreshGrabList.setRefreshing(false);
    }

    public void onEventMainThread(GrabCustomerResult result) throws EaseMobException {
//        抢客户
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            GrabCustomerParam param = (GrabCustomerParam) result.getParam();
            int position = param.getPosition();
            mAdapter.getData().get(position).setIsSuccess(true);
            recyclerGrabList.notifiyItemChange(position);
            ToolUtil.updatePoint(getActivity(), result.getData().getTotalPoint(), result.getData().getGetPoint(), R.string.toast_grab_success);
            //给客户发送抢单信息；
            sendToUser(UserBean.getInstance(getActivity()).id,mAdapter.getData().get(position),"您好，我接受了您发布的需求委托。很高兴为您服务");
            EventUtil.sendEvent(new ReFreshGrabListEvent(TAG));
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

    /**
     * 调用环信自动发送信息
     * @param fromUser
     * @param toUser
     * @param content
     * @throws EaseMobException
     */
    private void sendToUser(String fromUser,GrabCustomerItem toUser,String content) throws EaseMobException {
        //获取到与聊天人的会话对象。参数username为聊天人的userid或者groupid，后文中的username皆是如此
        EMConversation conversation = EMChatManager.getInstance().getConversationByType(toUser.getCustomerName(), EMConversation.EMConversationType.Chat);
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
        toUserBean.setImageUrl(toUser.getCustomerPohto());
        toUserBean.setUserName(toUser.getCustomerName());
        toUserBean.setUserType(0);
        Gson gson = new Gson();
        String fromUserBeanStr = gson.toJson(fromUserBean);
        String toUserBeanStr = gson.toJson(toUserBean);
        message.setAttribute("fromUser", fromUserBeanStr);
        message.setAttribute("toUser", toUserBeanStr);
        TextMessageBody txtBody = new TextMessageBody(content);
        message.addBody(txtBody);
        //设置接收人
        message.setReceipt(toUser.getCustomerId());
        //把消息加入到此会话对象中
        conversation.addMessage(message);
        //发送消息
        EMChatManager.getInstance().sendMessage(message);
    }

    private void refreshCustomerList(GrabCustomerListResult result) {
        if (ParamUtils.isNull(result.getData())) {
            rlayoutNoData.setVisibility(View.VISIBLE);
            return;
        }
        List<GrabCustomerItem> list = result.getData();
        mAdapter.setData(list);
        recyclerGrabList.notifiyChange();
        //无数据提示
        rlayoutNoData.setVisibility(ParamUtils.isEmpty(list) ? View.VISIBLE : View.GONE);
    }

    public void setRefreshing() {
        if (refreshGrabList != null) {
            ToolUtil.setRefreshing(refreshGrabList, true, true);
        }
    }
}
