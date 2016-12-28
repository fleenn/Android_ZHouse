package com.zfb.house.emchat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.emchat.Constant;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMConversation.EMConversationType;
import com.easemob.chat.EMMessage;
import com.google.gson.Gson;
import com.lemon.util.DisplayUtils;
import com.lemon.util.LogUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.emchat.temp.ChatUserBean;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.ui.AddFriendsActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.badgeview.BGABadgeTextView;

/**
 * 显示所有会话记录，比较简单的实现，更好的可能是把陌生人存入本地，这样取到的聊天记录是可控的
 * 系统admin返回回来的消息，里面的attributes为抢单消息的时候code为2，普通的为1
 */
public class ChatAllHistoryFragment extends Fragment implements OnClickListener {
    private InputMethodManager inputMethodManager;
    private ListView listViewCus;//经纪人
    private ListView listViewFri;//用户
    private ListView listViewSys;

    private ChatAllHistoryAdapter adapterCus;//经纪人
    private ChatAllHistoryAdapter adapterFri;//用户
    private ChatAllHistoryAdapter adapterSys;//系统

    private EditText query;
    private ImageButton clearSearch;
    public RelativeLayout errorItem;

    public TextView errorText;
    private boolean hidden;

    private List<EMConversation> conversationListCus = new ArrayList<EMConversation>();
    private List<EMConversation> conversationListFri = new ArrayList<EMConversation>();
    private List<EMConversation> conversationListSys = new ArrayList<EMConversation>();

    private TextView tvMsgSys;//系统
    private TextView tvMsgCus;//客户
    private TextView tvMsgBroker;//经纪人
    private BGABadgeTextView cricleTips;
    LinearLayout layoutMsgType;

    public final static int TYPE_SYS = 3;//系统
    public final static int TYPE_CUSTOM = 0;//用户
    public final static int TYPE_BROKER = 1;//经纪人
    public int current_type = TYPE_CUSTOM;//当前的类型

    int cusUnCur = 0;//消息未读数  --客户
    int cusUnSys = 0;//消息未读数  --系统
    int cusUnFri = 0;//消息未读数  --好友

    private View layoutAddNotice;//申请与通知
    private View viewLeft, viewCenter, viewRight;

    /*toolbar  新增 "编辑"按钮用于批量处理会话*/
    private TextView mTitleRightTv;//编辑
    private boolean isEdit = false;//由于 显示Adapter  checkBox 显示/隐藏
    private String userType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_conversation_history, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        getView().findViewById(R.id.rlayout_add_msg).setOnClickListener(this);
        userType = UserBean.getInstance(getActivity()).userType;
        //消息类型
        tvMsgBroker = (TextView) getView().findViewById(R.id.msg_tv_broker);
        tvMsgCus = (TextView) getView().findViewById(R.id.msg_tv_customer);
        cricleTips = (BGABadgeTextView) getView().findViewById(R.id.unread_msg_number);
        tvMsgBroker.setText("经纪人(0)");
        tvMsgCus.setText("用户(0)");
        viewLeft = getView().findViewById(R.id.underline_msg_left);
        viewCenter = getView().findViewById(R.id.underline_msg_center);
        viewRight = getView().findViewById(R.id.underline_msg_right);

        tvMsgSys = (TextView) getView().findViewById(R.id.msg_tv_sys);
        layoutMsgType = (LinearLayout) getView().findViewById(R.id.layout_msg_type);

        getView().findViewById(R.id.rlayout_message_left).setOnClickListener(this);
        getView().findViewById(R.id.rlayout_message_center).setOnClickListener(this);
        getView().findViewById(R.id.rlayout_message_right).setOnClickListener(this);
        layoutAddNotice = getView().findViewById(R.id.layout_addnotice);
        layoutAddNotice.setOnClickListener(this);

        errorItem = (RelativeLayout) getView().findViewById(R.id.rl_error_item);
        errorText = (TextView) errorItem.findViewById(R.id.tv_connect_errormsg);

        loadConversationsWithRecentChat(current_type);

        listViewCus = (ListView) getView().findViewById(R.id.listCus);
        listViewFri = (ListView) getView().findViewById(R.id.listFri);
        listViewSys = (ListView) getView().findViewById(R.id.listSys);

        adapterCus = new ChatAllHistoryAdapter(getActivity(), 1, conversationListCus);
        adapterFri = new ChatAllHistoryAdapter(getActivity(), 1, conversationListFri);
        adapterSys = new ChatAllHistoryAdapter(getActivity(), 1, conversationListSys);
        // 设置adapter
        listViewCus.setAdapter(adapterCus);
        listViewFri.setAdapter(adapterFri);
        listViewSys.setAdapter(adapterSys);

        adapterCus.clearDeleteEMConversation();
        adapterFri.clearDeleteEMConversation();


        setListList();
        // 搜索框
        query = (EditText) getView().findViewById(R.id.query);
        String strSearch = getResources().getString(R.string.search);
        query.setHint(strSearch);
        // 搜索框中清除button
        clearSearch = (ImageButton) getView().findViewById(R.id.search_clear);
        query.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapterCus.getFilter().filter(s);
                if (s.length() > 0) {
                    clearSearch.setVisibility(View.VISIBLE);
                } else {
                    clearSearch.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        clearSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                query.getText().clear();
                hideSoftKeyboard();
            }
        });

//        changeTvState();//mTitleRightTv状态
//        adapterCus.setSelectAll(false);
//        mTitleRightTv = (TextView) getView().findViewById(R.id.message_edit_tv);
//        mTitleRightTv.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (current_type) {
//                    case TYPE_CUSTOM://客户
//                        if (adapterCus != null && adapterCus.getCount() > 0) {
//                            isEdit = !isEdit;
//                            adapterCus.setIsCheckEnable(isEdit);
//                            changeTvState();
//                        }
//                        break;
//                    case TYPE_BROKER://经纪人好友
//                        if (adapterFri != null && adapterFri.getCount() > 0) {
//                            isEdit = !isEdit;
//                            adapterFri.setIsCheckEnable(isEdit);
//                            changeTvState();
//                        }
//                        break;
//                    case TYPE_SYS:
//
//                        break;
//                    default:
//                        break;
//                }
//
//            }
//        });

    }

    private void setListList() {
        final String st2 = getResources().getString(R.string.Cant_chat_with_yourself);
        listViewCus.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current_type = TYPE_BROKER;
                Log.i("linwb", "dddddd22 = = = " + current_type);
                EMConversation conversation = adapterCus.getItem(position);
                String username = conversation.getUserName();
                if (username.equals(UserBean.getInstance(getActivity()).id))
                    Toast.makeText(getActivity(), st2, Toast.LENGTH_SHORT).show();
                else {
                    // 进入聊天页面
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    if (conversation.isGroup()) {
                        if (conversation.getType() == EMConversationType.ChatRoom) {
                            // it is group chat
                            intent.putExtra("chatType", ChatActivity.CHATTYPE_CHATROOM);
                            intent.putExtra("groupId", username);
                            startActivity(intent);
                        } else {
                            // it is group chat
                            intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                            intent.putExtra("groupId", username);
                            startActivity(intent);
                        }

                    } else {
                        // it is single chat
                        intent.putExtra("userId", username);
                        User user = UserUtils.getUserInfo(username);
                        if (user != null) {
                            Log.i("linwb", "dddddd = = = " + current_type);
                            ChatActivity.launch(getActivity(), current_type + 1, username, user.getNick(), user.getAvatar());
                        } else {//空的话传userId
                            Log.i("linwb", "dddddd11 = = = " + current_type + 1);
                            onitemclick(conversation, username);
                        }
                    }
                }
            }
        });
        // 注册上下文菜单
        registerForContextMenu(listViewCus);

        listViewCus.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                current_type = TYPE_BROKER;
                showDeleteDialog(position);
                return true;
            }
        });

        listViewFri.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                current_type = TYPE_CUSTOM;
                showDeleteDialog(position);
                return true;
            }
        });

        listViewSys.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                current_type = TYPE_SYS;
                showDeleteDialog(position);
                return true;
            }
        });

        listViewCus.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 隐藏软键盘
                hideSoftKeyboard();
                return false;
            }

        });
        //////////////
        listViewFri.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current_type = TYPE_CUSTOM;
                Log.i("linwb", "dddddd33 = = = " + current_type);
                EMConversation conversation = adapterFri.getItem(position);
                String username = conversation.getUserName();
                if (username.equals(UserBean.getInstance(getActivity()).id))
                    Toast.makeText(getActivity(), st2, Toast.LENGTH_SHORT).show();
                else {
                    // 进入聊天页面
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    if (conversation.isGroup()) {
                        if (conversation.getType() == EMConversationType.ChatRoom) {
                            // it is group chat
                            intent.putExtra("chatType", ChatActivity.CHATTYPE_CHATROOM);
                            intent.putExtra("groupId", username);
                            startActivity(intent);
                        } else {
                            // it is group chat
                            intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                            intent.putExtra("groupId", username);
                            startActivity(intent);
                        }

                    } else {
                        // it is single chat
                        intent.putExtra("userId", username);
                        User user = UserUtils.getUserInfo(username);
                        Log.e("ChatAllHistoryFragment", "userName==" + username);
                        if (user != null) {
                            Log.e("ChatAllHistoryFragment", "user: is not null" + user.toString());
                            ChatActivity.launch(getActivity(), current_type + 1, username, user.getNick(), user.getAvatar());
                        } else {//空的话传userId
                            Log.e("ChatAllHistoryFragment", "user: is  null");
                            onitemclick(conversation, username);
                        }
                    }
                }
            }
        });
        // 注册上下文菜单
        registerForContextMenu(listViewFri);

        listViewFri.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 隐藏软键盘
                hideSoftKeyboard();
                return false;
            }

        });
        //////////////////////////////
        listViewSys.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current_type = TYPE_SYS;
                EMConversation conversation = adapterSys.getItem(position);
                String username = conversation.getUserName();
                if (username.equals(UserBean.getInstance(getActivity()).id))
                    Toast.makeText(getActivity(), st2, Toast.LENGTH_SHORT).show();
                else {
                    // 进入聊天页面
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    if (conversation.isGroup()) {
                        if (conversation.getType() == EMConversationType.ChatRoom) {
                            // it is group chat
                            intent.putExtra("chatType", ChatActivity.CHATTYPE_CHATROOM);
                            intent.putExtra("groupId", username);
                            startActivity(intent);
                        } else {
                            // it is group chat
                            intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                            intent.putExtra("groupId", username);
                            startActivity(intent);
                        }

                    } else {
                        // it is single chat
                        intent.putExtra("userId", username);
                        User user = UserUtils.getUserInfo(username);
                        if (user != null) {
                            ChatActivity.launch(getActivity(), current_type, username, "系统消息", null);
                        } else {//空的话传userId
                            ChatActivity.launch(getActivity(), current_type, username, "系统消息", null);
                        }
                    }
                }
            }
        });
        // 注册上下文菜单
        registerForContextMenu(listViewSys);

        listViewSys.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 隐藏软键盘
                hideSoftKeyboard();
                return false;
            }

        });
    }

    private void showDeleteDialog(final int position) {
        final Dialog dialog = new Dialog(getActivity(), R.style.loading_dialog_themes);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_message_delete, null);
        final EditText et = (EditText) view.findViewById(R.id.et_apply_content);
        view.findViewById(R.id.btn_sure).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                deleteItem(position);
                dialog.dismiss();
            }
        });

        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });

        final int viewWidth = DisplayUtils.dip2px(getActivity(), 300);
        view.setMinimumWidth(viewWidth);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(view);
        dialog.show();
    }

    private void onitemclick(EMConversation conversation, String username) {
        if (conversation.getLastMessage().direct == EMMessage.Direct.SEND) {
            if (!conversation.getLastMessage().getStringAttribute(ChatActivity.TO_USER, "").equals("")) {
                Gson gson = new Gson();
                ChatUserBean toChat = gson.fromJson(conversation.getLastMessage().getStringAttribute(ChatActivity.TO_USER, ""), ChatUserBean.class);
                ChatActivity.launch(getActivity(), current_type + 1, username, toChat.getUserName(), toChat.getImageUrl());
            }
        } else if (conversation.getLastMessage().direct == EMMessage.Direct.RECEIVE) {
            if (!conversation.getLastMessage().getStringAttribute(ChatActivity.FROM_USER, "").equals("")) {
                Gson gson = new Gson();
                ChatUserBean fromChat = gson.fromJson(conversation.getLastMessage().getStringAttribute(ChatActivity.FROM_USER, ""), ChatUserBean.class);
                ChatActivity.launch(getActivity(), current_type + 1, username, fromChat.getUserName(), fromChat.getImageUrl());
            }
        } else {
            ChatActivity.launch(getActivity(), current_type + 1, username, username, null);
        }

    }

    void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // if(((AdapterContextMenuInfo)menuInfo).position > 0){ m,
        getActivity().getMenuInflater().inflate(R.menu.delete_message, menu);
        // }
    }

    private void deleteItem(int position) {
        boolean deleteMessage = false;
        switch (current_type) {
            case TYPE_BROKER:
                EMConversation tobeDeleteCons = adapterCus.getItem(position);
                // 删除此会话
                EMChatManager.getInstance().deleteConversation(tobeDeleteCons.getUserName(), tobeDeleteCons.isGroup(), deleteMessage);
                InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
                inviteMessgeDao.deleteMessage(tobeDeleteCons.getUserName());
                adapterCus.remove(tobeDeleteCons);
                adapterCus.notifyDataSetChanged();
                break;
            case TYPE_CUSTOM:
                EMConversation tobeDeleteConsF = adapterFri.getItem(position);
                // 删除此会话
                EMChatManager.getInstance().deleteConversation(tobeDeleteConsF.getUserName(), tobeDeleteConsF.isGroup(), deleteMessage);
                InviteMessgeDao inviteMessgeDaoFri = new InviteMessgeDao(getActivity());
                inviteMessgeDaoFri.deleteMessage(tobeDeleteConsF.getUserName());
                adapterFri.remove(tobeDeleteConsF);
                adapterFri.notifyDataSetChanged();
                break;
            case TYPE_SYS:
                EMConversation tobeDeleteConsSys = adapterSys.getItem(position);
                // 删除此会话
                EMChatManager.getInstance().deleteConversation(tobeDeleteConsSys.getUserName(), tobeDeleteConsSys.isGroup(), deleteMessage);
                InviteMessgeDao inviteMessgeDaoSys = new InviteMessgeDao(getActivity());
                inviteMessgeDaoSys.deleteMessage(tobeDeleteConsSys.getUserName());
                adapterSys.remove(tobeDeleteConsSys);
                adapterSys.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean handled = false;
        boolean deleteMessage = false;
//        if (item.getItemId() == R.id.delete_message) {
//            deleteMessage = true;
//            handled = true;
//        } /*else if (item.getItemId() == R.id.delete_conversation) {
        deleteMessage = false;
        handled = true;
//		}*/
        switch (current_type) {
            case TYPE_CUSTOM:
                EMConversation tobeDeleteCons = adapterCus.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
                // 删除此会话
                EMChatManager.getInstance().deleteConversation(tobeDeleteCons.getUserName(), tobeDeleteCons.isGroup(), deleteMessage);
                InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
                inviteMessgeDao.deleteMessage(tobeDeleteCons.getUserName());
                adapterCus.remove(tobeDeleteCons);
                adapterCus.notifyDataSetChanged();
                break;
            case TYPE_BROKER:
                EMConversation tobeDeleteConsF = adapterFri.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
                // 删除此会话
                EMChatManager.getInstance().deleteConversation(tobeDeleteConsF.getUserName(), tobeDeleteConsF.isGroup(), deleteMessage);
                InviteMessgeDao inviteMessgeDaoFri = new InviteMessgeDao(getActivity());
                inviteMessgeDaoFri.deleteMessage(tobeDeleteConsF.getUserName());
                adapterFri.remove(tobeDeleteConsF);
                adapterFri.notifyDataSetChanged();
                break;
            case TYPE_SYS:
                EMConversation tobeDeleteConsSys = adapterSys.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
                // 删除此会话
                EMChatManager.getInstance().deleteConversation(tobeDeleteConsSys.getUserName(), tobeDeleteConsSys.isGroup(), deleteMessage);
                InviteMessgeDao inviteMessgeDaoSys = new InviteMessgeDao(getActivity());
                inviteMessgeDaoSys.deleteMessage(tobeDeleteConsSys.getUserName());
                adapterSys.remove(tobeDeleteConsSys);
                adapterSys.notifyDataSetChanged();
                break;
        }


        // 更新消息未读数
//        ((TabActivity) getActivity()).updateUnreadLabel();

        return handled ? true : super.onContextItemSelected(item);
    }

    /**
     * 刷新页面
     */
    public void refresh() {
        loadConversationsWithRecentChat(current_type);
        setSysLabelContactChanged();
    }

    /**
     * 有联系人时刷新
     */
    public void refreshContact() {
        if (getActivity() == null) return;
        if (SettingUtils.get(getActivity(), "is_new_friends", false)) {
            if (cusUnSys == 0) {
                tvMsgSys.setText("系统(" + 1 + ")");
            }
        }
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                if (adapterCus != null) {
                    adapterCus.notifyDataSetChanged();
                }
                if (adapterFri != null) {
                    adapterFri.notifyDataSetChanged();
                }
                if (adapterSys != null) {
                    adapterSys.notifyDataSetChanged();
                }


               /* if (getActivity() instanceof TabActivity) {
                    ((TabActivity) getActivity()).updateUnreadLabel();
                }*/
                Log.i("linwb", "cusUnCur == " + cusUnCur + " cusUnFri = " + cusUnFri);
                tvMsgBroker.setText("经纪人(" + cusUnCur + ")");
                tvMsgCus.setText("用户(" + cusUnFri + ")");

                setSysLabel();

            } else if (msg.what == 1) {//当接收到新用户添加好友的时候，要同在主线程去改变系统label的值
                setSysLabel();
            }
            ifTaskEnd = true;
        }
    };

    /**
     * 有新用户的时候要改变这个值
     */
    public void setSysLabelContactChanged() {
        Message message = new Message();
        message.what = 1;
        mHandler.sendMessage(message);
    }

    public void refreshContactTips() {
        cricleTips.showCirclePointBadge();
//        layoutAddNotice.findViewById(R.id.unread_msg_number).setVisibility(View.VISIBLE);
//        ((TextView) layoutAddNotice.findViewById(R.id.unread_msg_number)).setText(" ");
    }


    /**
     * 设置系统的未读数量
     */
    public void setSysLabel() {
        try {
            if ((HXSDKHelper.getInstance()) != null && ((DemoHXSDKHelper) HXSDKHelper.getInstance() != null) && ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList() != null) {
                Map<String, User> users = ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList();
                if (users != null) {
                    if (users.get(Constant.NEW_FRIENDS_USERNAME) != null) {//系统消息的话把系统消息显示出来
                        User user = users.get(Constant.NEW_FRIENDS_USERNAME);

                        if (user.getUnreadMsgCount() > 0) {
//                            layoutAddNotice.findViewById(R.id.unread_msg_number).setVisibility(View.VISIBLE);
                            cricleTips.showCirclePointBadge();
                            //申请通知未读个数  (新的好友)
//                            ((TextView) layoutAddNotice.findViewById(R.id.unread_msg_number)).setText(String.valueOf(user.getUnreadMsgCount()));
                        } else {
//                            layoutAddNotice.findViewById(R.id.unread_msg_number).setVisibility(View.GONE);
                            cricleTips.hiddenBadge();
                        }
                    }
                }
            }
        } catch (Exception e) {

        }

        int cusUnSysTmp = cusUnSys;
//        if (!"".equals(((TextView) layoutAddNotice.findViewById(R.id.unread_msg_number)).getText().toString())) {
//        }
        tvMsgSys.setText("系统(" + cusUnSysTmp + ")");
        refreshContact();
    }

    private boolean ifTaskEnd = true;

    /**
     * 获取所有会话
     *
     * @param
     * @return +
     */
    private void loadConversationsWithRecentChat(int userType) {
        if (ifTaskEnd == false) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (ifTaskEnd == false) {
                        return;
                    }
                    ifTaskEnd = false;
                    // 获取所有会话，包括陌生人
                    Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
                    // 过滤掉messages size为0的conversation
                    /**
                     * 如果在排序过程中有新消息收到，lastMsgTime会发生变化
                     * 影响排序过程，Collection.sort会产生异常
                     * 保证Conversation在Sort过程中最后一条消息的时间不变
                     * 避免并发问题
                     */
                    List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
                    synchronized (conversations) {
                        for (EMConversation conversation : conversations.values()) {
                            if (conversation.getAllMessages().size() != 0) {
                                //if(conversation.getType() != EMConversationType.ChatRoom){
                                if (!conversation.getMessage(0).getStringAttribute("code", "").equals("3")) {//add linwb
                                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                                }
                            }
                        }
                    }
                    try {
                        // Internal is TimSort algorithm, has bug
                        sortConversationByLastChatTime(sortList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    List<EMConversation> list = new ArrayList<EMConversation>();
                    for (Pair<Long, EMConversation> sortItem : sortList) {
                        list.add(sortItem.second);
                        Log.e("ChatAllHistortFragment", "EMConversation.username==" + sortItem.second.getUserName());
                    }


                    //根据type获取到列表数据
                    List<EMConversation> conversationList = new ArrayList<>();
                    try {
//            List<String>contractUserName = EMChatManager.getInstance().getContactUserNames();//好友

//			List<String> blackList = EMContactManager.getInstance().getBlackListUsernames();//获取黑名单列表
                        conversationListCus.clear();
                        conversationListFri.clear();
                        conversationListSys.clear();

                        cusUnCur = 0;
                        cusUnFri = 0;
                        cusUnSys = 0;

                        //当toUser跟fromUser有值的时候判断toUser跟fromUser的数据情况
                        for (EMConversation emConversation : list) {
                            if (!emConversation.getUserName().equals(Constant.NEW_FRIENDS_USERNAME)
                                    && !emConversation.getUserName().equals(Constant.GROUP_USERNAME)
                                    && !emConversation.getUserName().equals(Constant.CHAT_ROOM)
                                    && !emConversation.getUserName().equals(Constant.CHAT_ROBOT)) {
                                if (emConversation.getUserName().equals("admin")) {//系统消息
                                    conversationListSys.add(emConversation);
                                    cusUnSys = emConversation.getUnreadMsgCount();
                                } else {
                                    if (!emConversation.getLastMessage().getStringAttribute(ChatActivity.FROM_USER, "").equals("")
                                            && !emConversation.getLastMessage().getStringAttribute(ChatActivity.TO_USER, "").equals("")) {
                                        //判断usertype是多少
                                        Gson gson = new Gson();
                                        ChatUserBean fromChat = gson.fromJson(emConversation.getLastMessage().getStringAttribute(ChatActivity.FROM_USER, ""), ChatUserBean.class);
                                        ChatUserBean toChat = gson.fromJson(emConversation.getLastMessage().getStringAttribute(ChatActivity.TO_USER, ""), ChatUserBean.class);
                                        Log.i("lee", "name == " + toChat.getUserType() + "type = " + toChat.getUserName());
                                        Log.i("lee", "name2 == " + fromChat.getUserType() + "type2 = " + fromChat.getUserName());

//
                                        int otherUsertype = 0;
                                        if (fromChat != null && !TextUtils.isEmpty(fromChat.getUserName()) &&
                                                fromChat.getUserName().equals(UserBean.getInstance(getActivity()).name)) {
                                            otherUsertype = toChat.getUserType();
                                        } else {
                                            if (fromChat != null) {
                                                otherUsertype = fromChat.getUserType();
                                            } else {
                                                otherUsertype = toChat.getUserType();
                                            }
                                        }

                                        Log.i("lee", "type == " + otherUsertype);
                                        if (otherUsertype == ChatAllHistoryFragment.TYPE_BROKER) {
                                            conversationListCus.add(emConversation);
                                            cusUnCur = cusUnCur + emConversation.getUnreadMsgCount();
                                            //保存用户数据
                                            saveUserInfo(emConversation, fromChat, toChat);
                                        } else {
                                            conversationListFri.add(emConversation);
                                            cusUnFri = cusUnFri + emConversation.getUnreadMsgCount();
                                            //保存用户数据
                                            saveUserInfo(emConversation, fromChat, toChat);
                                        }
//                                        if (fromChat != null && toChat != null && fromChat.getUserType() == ChatAllHistoryFragment.TYPE_BROKER
//                                                && toChat.getUserType() == ChatAllHistoryFragment.TYPE_BROKER) {
//                                            conversationListCus.add(emConversation);
//                                            cusUnCur = cusUnCur + emConversation.getUnreadMsgCount();
//                                            //保存用户数据
//                                            saveUserInfo(emConversation, fromChat, toChat);
//                                        } else if (!emConversation.getLastMessage().getStringAttribute(ChatActivity.FROM_USER, "").equals("")
//                                                && !emConversation.getLastMessage().getStringAttribute(ChatActivity.TO_USER, "").equals("")) {
//                                            //判断usertype是多少
//                                            if (fromChat != null && toChat != null) {
//                                                if (fromChat.getUserType() == ChatAllHistoryFragment.TYPE_CUSTOM
//                                                        || toChat.getUserType() == ChatAllHistoryFragment.TYPE_CUSTOM) {
//                                                    conversationListFri.add(emConversation);
//                                                    cusUnFri = cusUnFri + emConversation.getUnreadMsgCount();
//                                                    //保存用户数据
//                                                    saveUserInfo(emConversation, fromChat, toChat);
//                                                }
//                                            }
//                                        }
                                    }
                                }

                            }
                        }
                    } catch (Exception e) {
                        Log.e("==", e.getMessage());
                    }
                    //更新ui
                    Message message = new Message();
                    message.what = 0;
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                    LogUtils.v("ChatAllHistoryFragment", e.getMessage() + "  ");
                }
            }
        }).start();
    }


    /**
     * 从接收者那边返回的数据里面获取保存用户数据
     *
     * @param emConversation
     * @param fromChat
     */
    private void saveUserInfo(EMConversation emConversation, ChatUserBean fromChat, ChatUserBean toChat) {
        Log.i("linwb", "11 = " + fromChat.getImageUrl());
        //判断消息的类型
        if (emConversation.getLastMessage().direct == EMMessage.Direct.RECEIVE) {//如果是recive--send的不用管
            if (!TextUtils.isEmpty(fromChat.getImageUrl()) || !TextUtils.isEmpty(fromChat.getUserName())) {//如果接受到的消息的imageUrl不为空
                User user = UserUtils.getUserInfo(emConversation.getLastMessage().getUserName());
                if (user == null) {
                    user = new User();
                    user.setUsername(emConversation.getLastMessage().getUserName());
                }
                if (user.getAvatar() == null
                        || user.getNick() == null
                        || (!TextUtils.isEmpty(fromChat.getImageUrl()) && !fromChat.getImageUrl().equals(user.getAvatar()))
                        || (!TextUtils.isEmpty(fromChat.getUserName()) && !fromChat.getUserName().equals(user.getNick()))) {
                    user.setAvatar(fromChat.getImageUrl());
                    user.setNick(fromChat.getUserName());
                    UserUtils.saveUserInfo(user);
                }
            }
        } else if (emConversation.getLastMessage().direct == EMMessage.Direct.SEND) {
            if (!TextUtils.isEmpty(toChat.getImageUrl()) || !TextUtils.isEmpty(toChat.getUserName())) {//如果接受到的消息的imageUrl不为空
                User user = UserUtils.getUserInfo(emConversation.getLastMessage().getUserName());
                if (user == null) {
                    user = new User();
                    user.setUsername(emConversation.getLastMessage().getUserName());
                }
                if (user.getAvatar() == null
                        || user.getNick() == null
                        || !toChat.getImageUrl().equals(user.getAvatar())
                        || !toChat.getUserName().equals(user.getNick())) {
                    user.setAvatar(toChat.getImageUrl());
                    user.setNick(toChat.getUserName());
                    UserUtils.saveUserInfo(user);
                }
            }
//               if(!emConversation.getLastMessage().getStringAttribute(ChatActivity.TO_USER,"").equals("")){
//                    //判断usertype是多少
//                    Gson gson = new Gson();
//                    ChatUserBean toChat = gson.fromJson(emConversation.getLastMessage().getStringAttribute(ChatActivity.TO_USER,""),ChatUserBean.class);
//                    //保存用户数据
//                    holder.name.setText(toChat.getUserName());
//                    if(UserUtils.getUserInfo(username)==null){
//                        User user = new User();
//                        user.setAvatar(toChat.getImageUrl());
//                        user.setAvatar(toChat.getUserName());
//                        UserUtils.saveUserInfo(user);
//                    }
//                }else{
//                }
        }
    }

    /**
     * 根据最后一条消息的时间排序
     *
     * @param conversationList
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first == con2.first) {
                    return 0;
                } else if (con2.first > con1.first) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden) {
            refresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hidden) {
            refresh();
        }
    }

/*	@Override
    public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
        if(((MainActivity)getActivity()).isConflict){
        	outState.putBoolean("isConflict", true);
        }else if(((MainActivity)getActivity()).getCurrentAccountRemoved()){
        	outState.putBoolean(Constant.ACCOUNT_REMOVED, true);
        }
    }*/


    @Override
    public void onClick(View v) {
        Resources resources = getResources();
        switch (v.getId()) {//客户好友系统
            case R.id.rlayout_message_left://用户
                listViewFri.setVisibility(View.VISIBLE);
                listViewSys.setVisibility(View.GONE);
                listViewCus.setVisibility(View.GONE);
                tvMsgSys.setTextColor(resources.getColor(R.color.my_gray_one));
                tvMsgBroker.setTextColor(resources.getColor(R.color.my_gray_one));
                tvMsgCus.setTextColor(resources.getColor(R.color.my_orange_one));
                tvMsgCus.setTextSize(16);
                tvMsgBroker.setTextSize(14);
                tvMsgSys.setTextSize(14);
                viewCenter.setVisibility(View.VISIBLE);
                viewLeft.setVisibility(View.GONE);
                viewRight.setVisibility(View.GONE);

                current_type = TYPE_BROKER;

                layoutAddNotice.setVisibility(View.GONE);
                break;
            case R.id.rlayout_message_center://经纪人
                listViewFri.setVisibility(View.GONE);
                listViewSys.setVisibility(View.GONE);
                listViewCus.setVisibility(View.VISIBLE);
                tvMsgSys.setTextColor(resources.getColor(R.color.my_gray_one));
                tvMsgBroker.setTextColor(resources.getColor(R.color.my_orange_one));
                tvMsgCus.setTextColor(resources.getColor(R.color.my_gray_one));
                tvMsgBroker.setTextSize(16);
                tvMsgCus.setTextSize(14);
                tvMsgSys.setTextSize(14);
                viewLeft.setVisibility(View.VISIBLE);
                viewCenter.setVisibility(View.GONE);
                viewRight.setVisibility(View.GONE);

                current_type = TYPE_CUSTOM;
                /*EMChatManager.getInstance().getUnreadMsgsCount();
                if(conversationListCus.size()==0){
					loadConversationsWithRecentChat(TYPE_BROKER);
				}*/
                layoutAddNotice.setVisibility(View.GONE);
                break;
            case R.id.rlayout_message_right:
                listViewFri.setVisibility(View.GONE);
                listViewSys.setVisibility(View.VISIBLE);
                listViewCus.setVisibility(View.GONE);
                tvMsgSys.setTextColor(resources.getColor(R.color.my_orange_one));
                tvMsgBroker.setTextColor(resources.getColor(R.color.my_gray_one));
                tvMsgCus.setTextColor(resources.getColor(R.color.my_gray_one));
                tvMsgSys.setTextSize(16);
                tvMsgCus.setTextSize(14);
                tvMsgBroker.setTextSize(14);
                viewRight.setVisibility(View.VISIBLE);
                viewCenter.setVisibility(View.GONE);
                viewLeft.setVisibility(View.GONE);

                current_type = TYPE_SYS;

                layoutAddNotice.setVisibility(View.VISIBLE);
                //设置申请与通知的未读
                //获取本地好友列表
                // 把"申请与通知"添加到首位
                ((TextView) layoutAddNotice.findViewById(R.id.name)).setText("新的好友");

                Log.i("linwb", "is = == " + SettingUtils.get(getActivity(), "is_new_friends", false));
                if (SettingUtils.get(getActivity(), "is_new_friends", false)) {
//                    layoutAddNotice.findViewById(R.id.unread_msg_number).setVisibility(View.VISIBLE);
//                    ((TextView) layoutAddNotice.findViewById(R.id.unread_msg_number)).setText(" ");
                    cricleTips.showCirclePointBadge();
                }

                break;
            case R.id.rlayout_add_msg:
                startActivity(new Intent(getActivity(), AddFriendsActivity.class));
                break;
            case R.id.layout_addnotice:
                SettingUtils.set(getActivity(), "is_new_friends", false);
                cricleTips.hiddenBadge();
                if (cusUnSys == 0) {
                    tvMsgSys.setText("系统(" + 0 + ")");
                }
                ChatLoginUtil.setNewFriendUnread();
                //设置系统的未读消息数量
                setSysLabel();
                startActivity(new Intent(getActivity(), NewFriendsMsgActivityNew.class));
                break;

        }
        initTvState();
    }

    /**
     * 三种类型的刷新
     */
    public void refreshChatList(EMMessage message) {
        if (message == null) {
            return;
        }
        Log.i("linwb", "dd == " + EMChatManager.getInstance().getUnreadMsgsCount());
        //一个是好友通知
        refresh();
    }


//    /**
//     * mTitleRightTv  状态切换
//     * 1.当临时会话个数为0时 ，mTitleRightTv控件直接隐藏
//     * 2.当临时会话个数大于0时，mTitleRightTv 文字   编辑/完成  切换
//     */
//    private void changeTvState() {
//
//        if (isEdit) {
//            mTitleRightTv.setText("完成");
//        } else {
//            mTitleRightTv.setText("编辑");
//
//            switch (current_type) {
//                case TYPE_CUSTOM://客户
//                    if (adapterCus != null && adapterCus.getCount() > 0) {
//                        List<EMConversation> deleteList = adapterCus.getDeleteAllEMConversation();
//                        if (deleteList != null && deleteList.size() > 0) {
//                            for (EMConversation tobeDeleteCons : deleteList) {
//                                // 删除此会话
//                                if (tobeDeleteCons != null) {
//                                    EMChatManager.getInstance().deleteConversation(tobeDeleteCons.getUserName(), tobeDeleteCons.isGroup(), true);
//                                    InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
//                                    inviteMessgeDao.deleteMessage(tobeDeleteCons.getUserName());
//                                    adapterCus.remove(tobeDeleteCons);
//                                }
//                            }
//                            adapterCus.clearDeleteEMConversation();
//                        }
//                    }
//                    break;
//                case TYPE_BROKER://经纪人好友
//                    if (adapterFri != null && adapterFri.getCount() > 0) {
//                        List<EMConversation> deleteList = adapterFri.getDeleteAllEMConversation();
//                        if (deleteList != null && deleteList.size() > 0) {
//                            for (EMConversation tobeDeleteCons : deleteList) {
//                                // 删除此会话
//                                if (tobeDeleteCons != null) {
//                                    EMChatManager.getInstance().deleteConversation(tobeDeleteCons.getUserName(), tobeDeleteCons.isGroup(), true);
//                                    InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
//                                    inviteMessgeDao.deleteMessage(tobeDeleteCons.getUserName());
//                                    adapterFri.remove(tobeDeleteCons);
//                                }
//                            }
//                            adapterFri.clearDeleteEMConversation();
//                        }
//                    }
//                    break;
//                case TYPE_SYS:
//
//                    break;
//                default:
//                    break;
//            }
//        }
//    }

    /**
     * 初始化mTitleRightTv 状态
     */
    private void initTvState() {
        isEdit = false;
        if (mTitleRightTv != null) {
            mTitleRightTv.setText("编辑");
        }
        if (adapterCus != null && adapterCus.getCount() > 0) {
            adapterCus.setIsCheckEnable(false);
            adapterCus.clearDeleteEMConversation();
        }
        if (adapterFri != null && adapterFri.getCount() > 0) {
            adapterFri.setIsCheckEnable(false);
            adapterFri.clearDeleteEMConversation();
        }
    }

    /**
     * 批量删除会话列表
     */
    private void deleteEMConversation() {

    }

}
