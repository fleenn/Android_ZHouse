package com.zfb.house.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.emchat.Constant;
import com.easemob.EMCallBack;
import com.easemob.EMConnectionListener;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.EMValueCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.HanziToPinyin;
import com.lemon.LemonActivity;
import com.lemon.LemonContext;
import com.lemon.LemonLocation;
import com.lemon.LemonUpdate;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.config.Config;
import com.lemon.event.LogOutEvent;
import com.lemon.event.NotifyEvent;
import com.lemon.event.RefreshEvent;
import com.lemon.model.param.AppUpdateParam;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.component.InputMethodRelativeLayout;
import com.zfb.house.emchat.ChatAllHistoryFragment;
import com.zfb.house.emchat.DemoHXSDKHelper;
import com.zfb.house.emchat.HXSDKHelper;
import com.zfb.house.emchat.InviteMessage;
import com.zfb.house.emchat.InviteMessgeDao;
import com.zfb.house.emchat.User;
import com.zfb.house.emchat.UserDao;
import com.zfb.house.init.FriendsListInitializer;
import com.zfb.house.model.bean.UserBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.badgeview.BGABadgeTextView;

/**
 * 主界面
 * Created by Administrator on 2016/4/28.
 */
@Layout(id = R.layout.activity_main)
public class MainActivity extends LemonActivity implements EMEventListener {

    private String TAG = "MainActivity";

    //首页
    @FieldView(id = R.id.rlayout_home)
    private RelativeLayout rlayoutHome;
    //房友圈
    @FieldView(id = R.id.rlayout_moments)
    private RelativeLayout rlayoutMoments;
    //用户->一键呼叫、经纪人->一键抢单
    @FieldView(id = R.id.rlayout_center)
    private RelativeLayout rlayoutCenter;
    //消息
    @FieldView(id = R.id.rlayout_message)
    private RelativeLayout rlayoutMessage;
    //我的
    @FieldView(id = R.id.rlayout_mine)
    private RelativeLayout rlayoutMine;
    //底部中间图标
    @FieldView(id = R.id.img_center)
    private ImageView imgCenter;
    //底部中间文字
    @FieldView(id = R.id.txt_center)
    private TextView txtCenter;

    @FieldView(id = R.id.rlayout_tab)
    private RelativeLayout rlayoutTab;
    @FieldView(id = R.id.inputMethodRelativeLayout)
    private InputMethodRelativeLayout mInputMethodRelativeLayout;

    //未读消息提醒
    @FieldView(id = R.id.bga_tab_message)
    private BGABadgeTextView bgaMessage;
    //与我相关提醒
    @FieldView(id = R.id.bga_moments)
    private BGABadgeTextView bgaMoments;

    private HomeUserFragment homeUserFragment;
    private HomeBrokerFragment homeBrokerFragment;
    private MomentsFragment momentsFragment;
    private ChatAllHistoryFragment messageFragment;
    private MineUserFragment mineUserFragment;
    private MineBrokerFragment mineBrokerFragment;

    private boolean isExit = false;
    //角色
    private String userType = "0";


     /*-------------------------------环信-----------------------------------------------*/
    /**
     * 4. 好友变化监听 Listener
     */
    private InviteMessgeDao mInviteMessageDao;
    private UserDao mUserDao;
    //账号在别处登陆
    private boolean isConflict = false;
    // 账号被移除
    private boolean isCurrentAccountRemoved = false;
    private MyConnectionListener mConnectionListener;//环信 连接监听事件

    private boolean isClickCenter = false;//是否点击中间

    @Override
    protected void initView() {
        mInputMethodRelativeLayout.setOnSizeChangedListener(new MyOnSizeChangedListener());
        setCenterButton();
        pushUnreadMsg();
        chatLogin();
        checkUpdate();
//        定位权限
        checkPermission(Constant.PERMISSION_CODE_LOC, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.PERMISSION_CODE_LOC && !ParamUtils.isEmpty(grantResults)) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //获取到权限
                LemonContext.getBean(LemonLocation.class).init();
            }
//            读取sd卡权限
        }
    }

    /**
     * 设置一键呼叫或者意见抢单按钮
     */
    private void setCenterButton() {
        //判断是否已登录
        if (!ParamUtils.isNull(UserBean.getInstance(mContext))) {
            userType = UserBean.getInstance(mContext).userType;
            if (userType.equals("0")) {//用户->一键发布
                imgCenter.setImageResource(R.drawable.main_call);
                txtCenter.setText(R.string.label_call);
            } else if (userType.equals("1")) {//经纪人->一键抢单
                imgCenter.setImageResource(R.drawable.main_grab);
                txtCenter.setText(R.string.label_grab);
            }
        } else {
            userType = "0";
            imgCenter.setImageResource(R.drawable.main_call);
            txtCenter.setText(R.string.label_call);
        }
    }

    //    推送本地缓存的未读信息条数
    private void pushUnreadMsg() {
        if (ParamUtils.isNull(UserBean.getInstance(mContext))) {
            bgaMoments.hiddenBadge();
            return;
        }
        int badge = SettingUtils.get(mContext, UserBean.getInstance(mContext).phone, 0);
        if (badge != 0) {

//            发往房友圈Fragment
//            sendMsgToFragment();
            bgaMoments.showTextBadge(String.valueOf(badge));
        }
    }

    private void checkUpdate() {
        if (Config.getBooleanValue("isupdate")) {
            LemonContext.getBean(LemonUpdate.class).setContext(mContext);
            AppUpdateParam param = new AppUpdateParam();
            apiManager.update(param);
        }
    }

    @Override
    protected void init() {
        if (!ParamUtils.isNull(getIntent().getExtras())) {//游客页面显示用户端的首页
            rlayoutMoments.performClick();
        } else {
            rlayoutHome.performClick();//默认选中首页界面
        }
    }

    /**
     * 首页
     */
    @OnClick(id = R.id.rlayout_home)
    public void toHomeFragment() {
        changePager(rlayoutHome, 0);
    }

    /**
     * 房友圈
     */
    @OnClick(id = R.id.rlayout_moments)
    public void toFriendsFragment() {
        changePager(rlayoutMoments, 1);
    }

    /**
     * 经纪人：一键抢单
     * 客户：一键发布
     */
    @OnClick(id = R.id.rlayout_center, anonymonus = false)
    public void toCenterFragment() {
        changePager(rlayoutCenter, 2);
    }

    /**
     * 消息
     */
    @OnClick(id = R.id.rlayout_message, anonymonus = false)
    public void toMessageFragment() {
        changePager(rlayoutMessage, 3);
    }

    /**
     * 我的
     */
    @OnClick(id = R.id.rlayout_mine, anonymonus = false)
    public void toMineFragment() {
        changePager(rlayoutMine, 4);
    }

    /**
     * 切换页面
     *
     * @param rlayout
     * @param index
     */
    private void changePager(RelativeLayout rlayout, int index) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        setSelected(ft);//隐藏所有的界面
        rlayout.setSelected(true);//选中当前页
        switch (index) {
            case 0://首页
                if (userType.equals("0")) {//用户
                    if (homeUserFragment == null) {
                        homeUserFragment = new HomeUserFragment();
                        ft.add(R.id.frame_content, homeUserFragment);
                    } else {
                        ft.show(homeUserFragment);
                    }
                } else if (userType.equals("1")) {//经纪人
                    if (homeBrokerFragment == null) {
                        homeBrokerFragment = new HomeBrokerFragment();
                        ft.add(R.id.frame_content, homeBrokerFragment);
                    } else {
                        ft.show(homeBrokerFragment);
                    }
                }
                break;
            case 1://房友圈
                if (momentsFragment == null) {
                    momentsFragment = new MomentsFragment();
                    ft.add(R.id.frame_content, momentsFragment);
                } else {
                    ft.show(momentsFragment);
                }
                break;
            case 2://一键发布、一键抢单
                isClickCenter = true;
                if (userType.equals("0")) {//用户->一键发布
                    startActivity(new Intent(mContext, CallActivity.class));
                } else if (userType.equals("1")) {//经纪人->一键抢单
                    startActivity(new Intent(mContext, GrabActivity.class));
                }
                break;
            case 3://消息
                if (messageFragment == null) {
                    messageFragment = new ChatAllHistoryFragment();
                    ft.add(R.id.frame_content, messageFragment);
                } else {
                    ft.show(messageFragment);
                }
                break;
            case 4://我的
                if (userType.equals("0")) {//用户
                    if (mineUserFragment == null) {
                        mineUserFragment = new MineUserFragment();
                        ft.add(R.id.frame_content, mineUserFragment);
                    } else {
                        ft.show(mineUserFragment);
                    }
                } else if (userType.equals("1")) {//经纪人
                    if (mineBrokerFragment == null) {
                        mineBrokerFragment = new MineBrokerFragment();
                        ft.add(R.id.frame_content, mineBrokerFragment);
                    } else {
                        ft.show(mineBrokerFragment);
                    }
                }
                break;
        }
        ft.commitAllowingStateLoss();
    }

    /**
     * 判断是否为选中状态，初始化的时候全部隐藏
     *
     * @param ft
     */
    private void setSelected(FragmentTransaction ft) {
        rlayoutHome.setSelected(false);
        rlayoutMoments.setSelected(false);
        rlayoutCenter.setSelected(false);
        rlayoutMessage.setSelected(false);
        rlayoutMine.setSelected(false);
        if (homeUserFragment != null) {
            ft.hide(homeUserFragment);
        }
        if (homeBrokerFragment != null) {
            ft.hide(homeBrokerFragment);
        }
        if (momentsFragment != null) {
            ft.hide(momentsFragment);
        }
        if (messageFragment != null) {
            ft.hide(messageFragment);
        }
        if (mineUserFragment != null) {
            ft.hide(mineUserFragment);
        }
        if (mineBrokerFragment != null) {
            ft.hide(mineBrokerFragment);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(rlayoutHome.getWindowToken(), 0);
            quit();
        }
        return true;
    }

    /**
     * 输入法对布局的影响
     */
    private class MyOnSizeChangedListener implements InputMethodRelativeLayout.OnSizeChangedListener {
        @Override
        public void onSizeChange(boolean flag) {
            if (flag) {
                if (rlayoutTab != null) {
                    rlayoutTab.setVisibility(View.GONE);
                }
                findViewById(R.id.rlayout_tab).setVisibility(View.GONE);
            } else {
                if (rlayoutTab != null) {
                    rlayoutTab.setVisibility(View.VISIBLE);
                }
                findViewById(R.id.rlayout_tab).setVisibility(View.VISIBLE);
            }
        }
    }

    public void onEventMainThread(NotifyEvent event) {
        String count = String.valueOf(event.getBadge());
        bgaMoments.showTextBadge(count);
    }

    public void onEventMainThread(LogOutEvent event) {
        finish();
    }

    public void onEventMainThread(RefreshEvent event) {
        bgaMoments.hiddenBadge();
        setCenterButton();
    }

    //    /**
//     * 发往fragment的event，可能在发送的时候该fragment还未实例化，所以需要先进行实例化再发送event
//     **/
//    private void sendMsgToFragment(){
//        if (momentsFragment == null) {
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            momentsFragment = new MomentsFragment();
//            ft.add(R.id.frame_content,momentsFragment);
//            ft.hide(momentsFragment);
//            ft.commitAllowingStateLoss();
//        }
////        momentsFragment.showCiclePoint();
//    }

    /**
     * 隐藏activity未读信息提示圆点，供fragment触发事件后调用
     */
    public void hideBadge() {
        bgaMoments.hiddenBadge();
    }

    /**
     * 退出应用
     */
    public void quit() {
        if (!isExit) {
            isExit = true;
            lemonMessage.sendMessage("再按一次退出程序");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    /*---------------------------环信监听事件-------------------------------------------------------*/
    @Override
    protected void onResume() {
        super.onResume();
        if (!isConflict && !isCurrentAccountRemoved) {
            updateUnreadLabel();
            EMChatManager.getInstance().activityResumed();
        }
        DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper.getInstance();
        sdkHelper.pushActivity(this);

        EMChatManager.getInstance().registerEventListener(this);//环信注册各种event事件

        if (isClickCenter) {
            changePager(rlayoutHome, 0);
            isClickCenter = false;
        }
    }

    @Override
    public void onEvent(EMNotifierEvent event) {
        switch (event.getEvent()) {
            case EventNewMessage:
                EMMessage message = (EMMessage) event.getData();
                if (message != null && "3".equals(message.getStringAttribute("code", ""))) {
                } else {
                    HXSDKHelper.getInstance().getNotifier().onNewMsg(message);
                    updateUnreadLableWhenContactChanged();
                }
                break;
            case EventOfflineMessage:
                updateUnreadLableWhenContactChanged();
                break;
            case EventConversationListChanged:
                updateUnreadLableWhenContactChanged();
                break;


            default:
                break;
        }
    }

    public void updateUnreadLableWhenContactChanged() {
        updateUnreadLabel();
        if (messageFragment != null) {
            messageFragment.refresh();
        }
    }

    /*
   收到消息，并且当前的页面在TabActivity里面的时候，要刷新界面
    */
    public void refreshChatList(EMMessage message) {
        if (message != null) {
            if (message.getUserName() != null) {
                if (messageFragment != null) {
                    messageFragment.refreshChatList(message);
                }
            }

        }
    }

    private void chatLogin() {
        // 1.监听联系人的变化等 setContactListener
        //2.注册群聊相关的Listener
        //3. 注册一个 监听链接
        mInviteMessageDao = new InviteMessgeDao(this);
        mUserDao = new UserDao(this);
        EMContactManager.getInstance().setContactListener(new MyContactListener());
        mConnectionListener = new MyConnectionListener();
        EMChatManager.getInstance().addConnectionListener(mConnectionListener);
    }

    public class MyContactListener implements EMContactListener {

        @Override
        public void onContactAdded(List<String> list) {
            Map<String, User> localUsers = ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList();

            Map<String, User> toAddUsers = new HashMap<String, User>();
            for (String userName : list) {
                User user = setUserHeader(userName);
                //添加好友时 可能会回调用added 两次
                if (!localUsers.containsKey(userName)) {
                    mUserDao.saveContact(user);
                }
                toAddUsers.put(userName, user);
            }
            localUsers.putAll(toAddUsers);
        }

        @Override
        public void onContactDeleted(List<String> list) {
            Map<String, User> localUsers = ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList();
            for (String userName : list) {
                User user = setUserHeader(userName);
                localUsers.remove(userName);
                mInviteMessageDao.deleteMessage(userName);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateUnreadLabel();
                }
            });
        }

        @Override
        public void onContactInvited(String username, String reason) {
            //接受邀请的消息,如果不处理的话(同意或拒绝),掉线后,服务器会自动再发过来，所以客户端不需要重新提醒
            Log.i("linwb", "annnnnnnnnnn＝＝＝＝＝" + username);
            List<InviteMessage> msgs = mInviteMessageDao.getMessagesList();
            for (InviteMessage msg : msgs) {
                if (msg.getGroupId() == null && msg.getFrom().equals(username)) {
                    mInviteMessageDao.deleteMessage(username);
                }
            }

            //自己封装 javabean
            InviteMessage msg = new InviteMessage();
            msg.setFrom(username);
            msg.setTime(System.currentTimeMillis());
            msg.setReason(reason);
            msg.setStatus(InviteMessage.InviteMesageStatus.BEINVITEED);
            notifyNewInviteMessage(msg);
        }

        @Override
        public void onContactAgreed(String username) {
            List<InviteMessage> msgs = mInviteMessageDao.getMessagesList();
            for (InviteMessage msg : msgs) {
                if (msg.getFrom().equals(username)) {
                    return;
                }
            }
            Log.i("linwb", "aaaaaaaaaa");
            //自己封装 javabean
            InviteMessage msg = new InviteMessage();
            msg.setFrom(username);
            msg.setTime(System.currentTimeMillis());
            msg.setStatus(InviteMessage.InviteMesageStatus.BEAGREED);

            FriendsListInitializer bean = LemonContext.getBean(FriendsListInitializer.class);
            bean.getFriendListByLogin(SettingUtils.get(MainActivity.this, "token", null));
//            notifyNewInviteMessage(msg);
        }

        @Override
        public void onContactRefused(String usename) {
        }
    }

    /**
     * 保存提示消息
     *
     * @param msg
     */
    private void notifyNewInviteMessage(InviteMessage msg) {
        saveInviteMsg(msg);
        //提示有新消息
        HXSDKHelper.getInstance().getNotifier().viberateAndPlayTone(null);
        //刷新bottom bar 消息未读数  UI  自己开发代码完成 "申请与通知" 栏目个数
        int count = getUnreadMsgCountTotal() + 1;
        Log.i("linwb", "count == " + count);
        if (count > 0) {
            bgaMessage.showTextBadge(String.valueOf(count));
        } else {
            bgaMessage.hiddenBadge();
        }

        SettingUtils.set(this, "is_new_friends", true);

    }

    /**
     * 保存邀请等 信息 msg
     *
     * @param msg
     */
    private void saveInviteMsg(InviteMessage msg) {
        mInviteMessageDao.saveMessage(msg);
        //未读个数+1
//        User user = ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList().get(Constant.NEW_FRIENDS_USERNAME);
//        if (user.getUnreadMsgCount() == 0) {
//            user.setUnreadMsgCount(user.getUnreadMsgCount() + 1);
//        }
    }

    /**
     * 1.刷新未读消息数
     */
    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        Log.i("main", "count == " + count);
        if (count > 0) {
            bgaMessage.showTextBadge(String.valueOf(count));
        } else {
            bgaMessage.hiddenBadge();
        }
    }

    /**
     * 2.获取未读消息数
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
        for (EMConversation conversation : EMChatManager.getInstance().getAllConversations().values()) {
            if (conversation.getType() == EMConversation.EMConversationType.ChatRoom
                    || (conversation.getMessage(0) != null && conversation.getMessage(0).getStringAttribute("code", "").equals("3")))
                chatroomUnreadMsgCount = chatroomUnreadMsgCount + conversation.getUnreadMsgCount();
        }
        return unreadMsgCountTotal - chatroomUnreadMsgCount;
    }

    /**
     * 3.连接监听Listener
     */
    public class MyConnectionListener implements EMConnectionListener {

        @Override
        public void onConnected() {
            boolean groupSynced = HXSDKHelper.getInstance().isGroupsSyncedWithServer();
            boolean contactSynced = HXSDKHelper.getInstance().isContactsSyncedWithServer();
            if (groupSynced && contactSynced) {
                new Thread() {
                    @Override
                    public ClassLoader getContextClassLoader() {
                        return super.getContextClassLoader();
                    }

                    @Override
                    public void run() {
                        //SDK 会发送broadcast intent 通知UI，比如好友邀请
                        HXSDKHelper.getInstance().notifyForRecevingEvents();
                    }
                }.start();
            } else {
                if (!groupSynced) {
                    // 同步操作，从服务器获取群组列表
                    asyncFetchGounpsFromServer();
                }
                if (!contactSynced) {
                    asyncFetchContactsFromServer();
                }
                if (!HXSDKHelper.getInstance().isBlackListSyncedWithServer()) {
                    asyncFetchBlackListFromServer();
                }
            }

        }

        @Override
        public void onDisconnected(final int error) {
        }
    }

    /**
     * 3.1同步操作，从服务器获取群组列表
     * 该方法会记录更新状态，可以通过isSyncingGroupsFromServer获取是否正在更新
     * 和HXPreferenceUtils.getInstance().getSettingSyncGroupsFinished()获取是否更新已经完成
     *
     * @throws EaseMobException 从服务器端获取当前用户的所有群组 （此操作只返回群组列表，并不获取群组的所有成员信息，
     *                          如果要更新某个群组包括成员的全部信息，需要再调用 getGroupFromServer(String groupId),，一般来说取到后需要保存一下，
     *                          调用createOrUpdateLocalGroup(EMGroup)） this api will get groups from remote server and update local groups
     */
    private void asyncFetchGounpsFromServer() {
        HXSDKHelper.getInstance().asyncFetchGroupsFromServer(new EMCallBack() {
            @Override
            public void onSuccess() {
                HXSDKHelper.getInstance().noitifyGroupSyncListeners(true);
                if (HXSDKHelper.getInstance().isContactsSyncedWithServer()) {
                    HXSDKHelper.getInstance().notifyForRecevingEvents();
                }
            }

            @Override
            public void onError(int i, String s) {
                HXSDKHelper.getInstance().noitifyGroupSyncListeners(false);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    /**
     * 3.2同步操作   同步服务器联系人的用户名列表
     */
    private void asyncFetchContactsFromServer() {
        HXSDKHelper.getInstance().asyncFetchContactsFromServer(new EMValueCallBack<List<String>>() {
            @Override
            public void onSuccess(List<String> usernames) {
                Context context = HXSDKHelper.getInstance().getAppContext();
                Map<String, User> userList = new HashMap<String, User>();
                for (String username : usernames) {
                    User user = new User();
                    user.setUsername(username);
                    setUserHeader(username, user);
                    userList.put(username, user);
                }

                //添加 User  申请和通知
                User newFriends = new User();
                newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
                String newfriendsNick = context.getString(R.string.Application_and_notify);//申请和通知
                newFriends.setNick(newfriendsNick);
                newFriends.setHeader("");
                userList.put(Constant.NEW_FRIENDS_USERNAME, newFriends);

                //添加 群聊
                User groupUser = new User();
                String groupNick = context.getString(R.string.group_chat);
                groupUser.setUsername(Constant.GROUP_USERNAME);
                groupUser.setNick(groupNick);
                groupUser.setHeader("");
                userList.put(Constant.GROUP_USERNAME, groupUser);

                //添加聊天室
                User chatRoomItem = new User();
                String chatroom = context.getString(R.string.chat_room);
                chatRoomItem.setUsername(Constant.CHAT_ROOM);
                chatRoomItem.setNick(chatroom);
                chatRoomItem.setHeader("");
                userList.put(Constant.CHAT_ROOM, chatRoomItem);

                //添加 "Robot" 环信小助手
                User robotUser = new User();
                String robot = context.getString(R.string.robot_chat);
                robotUser.setUsername(Constant.CHAT_ROBOT);
                robotUser.setNick(robot);
                robotUser.setHeader("");
                userList.put(Constant.CHAT_ROBOT, robotUser);

                //存入内存
                ((DemoHXSDKHelper) HXSDKHelper.getInstance()).setContactList(userList);
                UserDao dao = new UserDao(context);
                List<User> users = new ArrayList<User>(userList.values());
                dao.saveContactList(users);

                HXSDKHelper.getInstance().notifyContactsSyncListener(true);
                if (HXSDKHelper.getInstance().isGroupsSyncedWithServer()) {
                    HXSDKHelper.getInstance().notifyForRecevingEvents();
                }

                ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getUserProfileManager().asyncFetchContactInfosFromServer(usernames, new EMValueCallBack<List<User>>() {
                    @Override
                    public void onSuccess(List<User> users) {
                        ((DemoHXSDKHelper) HXSDKHelper.getInstance()).updateContactList(users);
                        ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getUserProfileManager().notifyContactInfosSyncListener(true);
                    }

                    @Override
                    public void onError(int i, String s) {
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                HXSDKHelper.getInstance().notifyContactsSyncListener(false);
            }
        });
    }

    /**
     * 设置header属性,方便通讯录中对联系人按header（ABCD...字母）分类显示
     *
     * @param username
     * @param user
     */
    private void setUserHeader(String username, User user) {
        String headerName = null;
        if (!TextUtils.isEmpty(user.getNick())) {
            headerName = user.getNick();
        } else {
            headerName = user.getUsername();
        }
        if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
            user.setHeader("");
        } else if (Character.isDigit(headerName.charAt(0))) {
            user.setHeader("#");
        } else {
            user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1).toUpperCase());
            char header = user.getHeader().toLowerCase().charAt(0);
            if (header < 'a' || header > 'z') {
                user.setHeader("#");
            }

        }
    }

    /**
     * setHeader   设置用户User首字母
     *
     * @param username
     * @return
     */
    private User setUserHeader(String username) {
        User user = new User();
        user.setUsername(username);
        String headerName = null;
        if (TextUtils.isEmpty(user.getNick())) {
            headerName = user.getUsername();
        } else {
            headerName = user.getNick();
        }

        if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
            user.setHeader("");
        } else if (Character.isDigit(headerName.charAt(0))) {
            user.setHeader("#");
        } else {
            user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1).toUpperCase());
            char header = user.getHeader().toLowerCase().charAt(0);
            if (header < 'a' || header > 'z') {
                user.setHeader("#");
            }
        }
        return user;
    }

    /**
     * 3.3同步操作   从服务器获取黑名单中的用户的usernames get black list from server
     */
    private void asyncFetchBlackListFromServer() {
        HXSDKHelper.getInstance().asyncFetchBlackListFromServer(new EMValueCallBack<List<String>>() {
            @Override
            public void onSuccess(List<String> value) {
                EMContactManager.getInstance().saveBlackList(value);
                HXSDKHelper.getInstance().notifyBlackListSyncListener(true);
            }

            @Override
            public void onError(int i, String s) {
                HXSDKHelper.getInstance().notifyBlackListSyncListener(false);
            }
        });
    }
}

