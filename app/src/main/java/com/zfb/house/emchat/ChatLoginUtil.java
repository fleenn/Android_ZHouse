package com.zfb.house.emchat;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.android.emchat.Constant;
import com.easemob.EMCallBack;
import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.EMValueCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.util.HanziToPinyin;
import com.google.gson.Gson;
import com.lemon.util.LogUtils;
import com.zfb.house.R;
import com.zfb.house.emchat.temp.ChatUserBean;
import com.zfb.house.emchat.temp.OnKeyWordsClick;
import com.zfb.house.model.bean.UserBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2015-10-20.
 */
public class ChatLoginUtil {
    private Activity mcontext=null;
    public ChatLoginUtil(Activity context){
        this.mcontext = context;
    }
    /**
     * 登陆环信
     *
     * @param bean
     */
    public void chatLogin(final UserBean bean) {
        if(bean==null){
            return;
        }
        // 调用sdk登陆方法登陆聊天服务器-手机号码跟id为环信用户名跟密码
        EMChatManager.getInstance().login(bean.id, bean.loginName, new EMCallBack() {
            @Override
            public void onSuccess() {
                try {
                    User user = ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getUserProfileManager().getCurrentUserInfo();
                    if (user != null) {
                        ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getUserProfileManager().getCurrentUserInfo().setAvatar(bean.photo);
                        ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getUserProfileManager().getCurrentUserInfo().setNick(bean.name);
                    }
                    // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                    // ** manually load all local groups and
                    EMGroupManager.getInstance().loadAllGroups();
                    EMChatManager.getInstance().loadAllConversations();
                    // 处理好友和群组
                    initializeContacts(mcontext);

                    init();
                } catch (Exception e) {
                    e.printStackTrace();
                    // 取好友或者群聊失败，不让进入主页面
                    mcontext.runOnUiThread(new Runnable() {
                        public void run() {
                            if (DemoHXSDKHelper.getInstance() != null) {
//                                DemoHXSDKHelper.getInstance().logout(true, null);
//                                UserManager.logout(mcontext,"");
                            }
                        }
                    });
                    return;
                }
                // 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
                   /* boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(
                            DemoApplication.currentUserNick.trim());*/
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(final int code, final String message) {
//                ToastUtil.showToast(mcontext,"登陆失败");
//                UserManager.logout(mcontext,"");
                saveErrorUserList();
            }

        });

    }
    private void initializeContacts(Context context) {
        Map<String, User> userlist = new HashMap<String, User>();
        // 添加user"申请与通知"
        User newFriends = new User();
        newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
        String strChat = context.getResources().getString(
                R.string.Application_and_notify);
        newFriends.setNick(strChat);

        userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
        // 添加"群聊"
        User groupUser = new User();
        String strGroup = context.getResources().getString(R.string.group_chat);
        groupUser.setUsername(Constant.GROUP_USERNAME);
        groupUser.setNick(strGroup);
        groupUser.setHeader("");
        userlist.put(Constant.GROUP_USERNAME, groupUser);

        // 添加"Robot"
        User robotUser = new User();
        String strRobot = context.getResources().getString(R.string.robot_chat);
        robotUser.setUsername(Constant.CHAT_ROBOT);
        robotUser.setNick(strRobot);
        robotUser.setHeader("");
        userlist.put(Constant.CHAT_ROBOT, robotUser);

        // 存入内存
        ((DemoHXSDKHelper) HXSDKHelper.getInstance()).setContactList(userlist);
        // 存入db
        UserDao dao = new UserDao(context);
        List<User> users = new ArrayList<User>(userlist.values());
        dao.saveContactList(users);
    }

    /**
     *  连接“环信”  登录失败或者出现连接错误码是 初始化一个长度为0 的用户列表userList，避免其他情况获取用户列表是出现null指针
     */
    private void saveErrorUserList(){

        Map<String, User> userList = new HashMap<String, User>();
        // 存入内存
        ((DemoHXSDKHelper) HXSDKHelper.getInstance()).setContactList(userList);
        // 存入db
        if(mcontext!=null){
        UserDao dao = new UserDao(mcontext);
        List<User> users = new ArrayList<User>(userList.values());
        dao.saveContactList(users);
        }
    }



    public void init() {
        EMChatManager.getInstance().getChatOptions().setUseRoster(true);

        inviteMessgeDao = new InviteMessgeDao(mcontext);
        userDao = new UserDao(mcontext);

        // setContactListener监听联系人的变化等
        EMContactManager.getInstance().setContactListener(new MyContactListener());
        // 注册一个监听连接状态的listener
        MyConnectionListener  connectionListener = new MyConnectionListener();
        EMChatManager.getInstance().addConnectionListener(connectionListener);
        EMChat.getInstance().setAppInited();
      }

    /**
     * 连接监听listener
     *
     */
    public class MyConnectionListener implements EMConnectionListener {

        @Override
        public void onConnected() {
            boolean groupSynced = HXSDKHelper.getInstance().isGroupsSyncedWithServer();
            boolean contactSynced = HXSDKHelper.getInstance().isContactsSyncedWithServer();

            // in case group and contact were already synced, we supposed to notify sdk we are ready to receive the events
            if(groupSynced && contactSynced){
                new Thread(){
                    @Override
                    public void run(){
                        HXSDKHelper.getInstance().notifyForRecevingEvents();
                    }
                }.start();
            }else{
                if(!groupSynced){
//                    asyncFetchGroupsFromServer();
                }

                if(!contactSynced){
                    asyncFetchContactsFromServer();
                }
                if(!HXSDKHelper.getInstance().isBlackListSyncedWithServer()){
//                    asyncFetchBlackListFromServer();
                }
            }
            mcontext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                }
            });
        }
        @Override
        public void onDisconnected(final int error) {
            final String st1 = mcontext.getResources().getString(R.string.can_not_connect_chat_server_connection);
            final String st2 = mcontext.getResources().getString(R.string.the_current_network);
            mcontext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (error == EMError.USER_REMOVED) {
                        // 显示帐号已经被移除
//                        showAccountRemovedDialog();
                        LogUtils.toast(mcontext,mcontext.getResources().getString(R.string.user_removed));
                    } else if (error == EMError.CONNECTION_CONFLICT) {
                        // 显示帐号在其他设备登陆dialog
                        LogUtils.toast(mcontext,mcontext.getResources().getString(R.string.connect_conflict));
                    } else {

                    }
                }

            });
        }
    }



    static void asyncFetchContactsFromServer(){
        HXSDKHelper.getInstance().asyncFetchContactsFromServer(new EMValueCallBack<List<String>>(){

            @Override
            public void onSuccess(List<String> usernames) {
                Context context = HXSDKHelper.getInstance().getAppContext();

                System.out.println("----------------"+usernames.toString());
                LogUtils.d("roster", "contacts size: " + usernames.size());
                Map<String, User> userlist = new HashMap<String, User>();
                for (String username : usernames) {
                    User user = new User();
                    user.setUsername(username);
                    userlist.put(username, user);
                }
                // 添加user"申请与通知"
                User newFriends = new User();
                newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
                String strChat = context.getString(R.string.Application_and_notify);
                newFriends.setNick(strChat);

                userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
                // 添加"群聊"
                User groupUser = new User();
                String strGroup = context.getString(R.string.group_chat);
                groupUser.setUsername(Constant.GROUP_USERNAME);
                groupUser.setNick(strGroup);
                groupUser.setHeader("");
                userlist.put(Constant.GROUP_USERNAME, groupUser);

                // 添加"聊天室"
                User chatRoomItem = new User();
                String strChatRoom = context.getString(R.string.chat_room);
                chatRoomItem.setUsername(Constant.CHAT_ROOM);
                chatRoomItem.setNick(strChatRoom);
                chatRoomItem.setHeader("");
                userlist.put(Constant.CHAT_ROOM, chatRoomItem);

                // 添加"Robot"
                User robotUser = new User();
                String strRobot = context.getString(R.string.robot_chat);
                robotUser.setUsername(Constant.CHAT_ROBOT);
                robotUser.setNick(strRobot);
                robotUser.setHeader("");
                userlist.put(Constant.CHAT_ROBOT, robotUser);

                // 存入内存
                ((DemoHXSDKHelper)HXSDKHelper.getInstance()).setContactList(userlist);
                // 存入db
                UserDao dao = new UserDao(context);
                List<User> users = new ArrayList<User>(userlist.values());
                dao.saveContactList(users);

                HXSDKHelper.getInstance().notifyContactsSyncListener(true);

                if(HXSDKHelper.getInstance().isGroupsSyncedWithServer()){
                    HXSDKHelper.getInstance().notifyForRecevingEvents();
                }

                ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getUserProfileManager().asyncFetchContactInfosFromServer(usernames,new EMValueCallBack<List<User>>() {

                    @Override
                    public void onSuccess(List<User> uList) {
                        ((DemoHXSDKHelper)HXSDKHelper.getInstance()).updateContactList(uList);
                        ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getUserProfileManager().notifyContactInfosSyncListener(true);
                    }

                    @Override
                    public void onError(int error, String errorMsg) {
                    }
                });
            }

            @Override
            public void onError(int error, String errorMsg) {
                HXSDKHelper.getInstance().notifyContactsSyncListener(false);
            }

        });
    }



    private InviteMessgeDao inviteMessgeDao;
    private UserDao userDao;
    /***
     * 好友变化listener
     *
     */
    public class MyContactListener implements EMContactListener {
        @Override
        public void onContactAdded(List<String> usernameList) {
            // 保存增加的联系人
            Map<String, User> localUsers = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getContactList();
            Map<String, User> toAddUsers = new HashMap<String, User>();
            for (String username : usernameList) {
                User user = setUserHead(username);
                // 添加好友时可能会回调added方法两次
                if (!localUsers.containsKey(username)) {
                    userDao.saveContact(user);
                }
                toAddUsers.put(username, user);
            }
            localUsers.putAll(toAddUsers);
            // 刷新ui
/*
            if (currentTabIndex == 1)
                contactListFragment.refresh();
*/

        }

        @Override
        public void onContactDeleted(final List<String> usernameList) {
            // 被删除
            Map<String, User> localUsers = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getContactList();
            for (String username : usernameList) {
                localUsers.remove(username);
                userDao.deleteContact(username);
                inviteMessgeDao.deleteMessage(username);
            }
            mcontext.runOnUiThread(new Runnable() {
                public void run() {
                    // 如果正在与此用户的聊天页面
                    String st10 = mcontext.getResources().getString(R.string.user_removed);
                    if (ChatActivity.activityInstance != null
                            && usernameList.contains(ChatActivity.activityInstance.getToChatUsername())) {
                        LogUtils.toast(mcontext, ChatActivity.activityInstance.getToChatUsername() + st10)
                                ;
                        ChatActivity.activityInstance.finish();
                    }
                    /*if(mcontext!=null && mcontext instanceof TabActivity){
                      ((TabActivity) mcontext).updateUnreadLabel();
                    }*/
                    // 刷新ui
//                    contactListFragment.refresh();
//                    chatHistoryFragment.refresh();
                }
            });

        }

        @Override
        public void onContactInvited(String username, String reason) {

            // 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不需要重复提醒
            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();

            for (InviteMessage inviteMessage : msgs) {
                if (inviteMessage.getGroupId() == null && inviteMessage.getFrom().equals(username)) {
                    inviteMessgeDao.deleteMessage(username);
                }
            }
            // 自己封装的javabean
            InviteMessage msg = new InviteMessage();
            msg.setFrom(username);
            msg.setTime(System.currentTimeMillis());
            msg.setReason(reason);
            LogUtils.d("ChatLoginUtil", username + "请求加你为好友,reason: " + reason);
            // 设置相应status
            msg.setStatus(InviteMessage.InviteMesageStatus.BEINVITEED);
            notifyNewIviteMessage(msg);
        }

        @Override
        public void onContactAgreed(String username) {
            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
            for (InviteMessage inviteMessage : msgs) {
                if (inviteMessage.getFrom().equals(username)) {
                    return;
                }
            }
            // 自己封装的javabean
            InviteMessage msg = new InviteMessage();
            msg.setFrom(username);
            msg.setTime(System.currentTimeMillis());
            LogUtils.d("ChatLoginUtil", username + "同意了你的好友请求");
            msg.setStatus(InviteMessage.InviteMesageStatus.BEAGREED);
            notifyNewIviteMessage(msg);
        }
        @Override
        public void onContactRefused(String username) {

            // 参考同意，被邀请实现此功能,demo未实现
            LogUtils.d(username, username + "拒绝了你的好友请求");
        }

    }
    /**
     * 保存邀请等msg
     *
     * @param msg
     */
    private void saveInviteMsg(InviteMessage msg) {
        // 保存msg
        inviteMessgeDao.saveMessage(msg);
        // 未读数加1
        User user = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getContactList().get(Constant.NEW_FRIENDS_USERNAME);
        if (user.getUnreadMsgCount() == 0)
            user.setUnreadMsgCount(user.getUnreadMsgCount() + 1);
    }
    /**
     * 保存提示新消息
     *
     * @param msg
     */
    private void notifyNewIviteMessage(InviteMessage msg) {
        saveInviteMsg(msg);
        // 提示有新消息
        HXSDKHelper.getInstance().getNotifier().viberateAndPlayTone(null);

        // 刷新bottom bar消息未读数
        /*if(mcontext!=null && mcontext instanceof TabActivity){
            ((TabActivity)mcontext).updateUnreadLableWhenContactChanged();
        }*/
        // 刷新好友页面ui
//        if (currentTabIndex == 1)
//            contactListFragment.refresh();
    }
    /**
     * set head
     *
     * @param username
     * @return
     */
    User setUserHead(String username) {
        User user = new User();
        user.setUsername(username);
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
            user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1)
                    .toUpperCase());
            char header = user.getHeader().toLowerCase().charAt(0);
            if (header < 'a' || header > 'z') {
                user.setHeader("#");
            }
        }
        return user;
    }


    public static void setNewFriendUnread(){
        if((HXSDKHelper.getInstance()!=null)&&((DemoHXSDKHelper)HXSDKHelper.getInstance())!=null&&((DemoHXSDKHelper)HXSDKHelper.getInstance()).getContactList()!=null){
            User user = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getContactList().get(Constant.NEW_FRIENDS_USERNAME);
            if (user != null)
                user.setUnreadMsgCount(0);
        }
    }
    /**
     * 环信是否可以用
     * @return
     */
    public static boolean ifEmChatValide(){
        if((HXSDKHelper.getInstance()!=null)&&((DemoHXSDKHelper)HXSDKHelper.getInstance())!=null){
            return true;
        }
        return false;
    }


    /**
     *
     发送消息给客户
     */
    public static void chatSend(Context context,String text,String toUserId,String toUserName,String toImgUrl,int toUserType, final OnKeyWordsClick keyWordsClick) {
        if(UserBean.getInstance(context)==null){
            return;
        }
        /////////////////////////
        //获取到与聊天人的会话对象。参数username为聊天人的userid或者groupid，后文中的username皆是如此
        EMConversation conversation = EMChatManager.getInstance().getConversation(UserBean.getInstance(context).id);
       //创建一条文本消息
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
        //如果是群聊，设置chattype,默认是单聊
        //message.setChatType(EMMessage.ChatType.Chat);
//设置消息body
        TextMessageBody txtBody = new TextMessageBody(text);
        message.addBody(txtBody);
        Gson gson = new Gson();
        ChatUserBean toUserBean = new ChatUserBean();
        toUserBean.setImageUrl(toImgUrl);
        toUserBean.setUserName(toUserName);
        toUserBean.setUserType(toUserType);
        ChatUserBean fromUserBean = new ChatUserBean();
        fromUserBean = new ChatUserBean();
        fromUserBean.setUserType(ChatAllHistoryFragment.TYPE_BROKER);
        fromUserBean.setImageUrl(UserBean.getInstance(context).photo);
        fromUserBean.setUserName(((UserBean.getInstance(context).name) == null ? UserBean.getInstance(context).loginName : UserBean.getInstance(context).name));
        message.setAttribute(ChatActivity.TO_USER, gson.toJson(toUserBean));
        message.setAttribute(ChatActivity.FROM_USER, gson.toJson(fromUserBean));
        conversation.addMessage(message);

       //设置接收人
        message.setReceipt(toUserId);
       //把消息加入到此会话对象中
        conversation.addMessage(message);
       //发送消息
        EMChatManager.getInstance().sendMessage(message, new EMCallBack(){
            @Override
            public void onSuccess() {
                if(keyWordsClick!=null){
                    keyWordsClick.onItemClick("");
                }
            }
            @Override
            public void onError(int i, String s) {
                if(keyWordsClick!=null){
                    keyWordsClick.onItemClick("");
                }
            }
            @Override
            public void onProgress(int i, String s) {
                if(keyWordsClick!=null){
                    keyWordsClick.onItemClick("");
                }
            }
        });
    }

    /**
     * 面骚扰设置
     * @param paramBoolean
     */
    public static void setNoInterrupt(boolean paramBoolean){
        if((HXSDKHelper.getInstance()!=null)&&((DemoHXSDKHelper)HXSDKHelper.getInstance())!=null&&(HXSDKHelper.getInstance()).getModel()!=null) {
            (HXSDKHelper.getInstance()).getModel().setSettingMsgNotification(paramBoolean);
            (HXSDKHelper.getInstance()).getModel().setSettingMsgSound(paramBoolean);
            (HXSDKHelper.getInstance()).getModel().setSettingMsgVibrate(paramBoolean);
        }
    }

    /**
     * 面骚扰设置
     * @param paramBoolean
     */
    public static void setNoInterruptS(boolean paramBoolean){
        if((HXSDKHelper.getInstance()!=null)&&((DemoHXSDKHelper)HXSDKHelper.getInstance())!=null&&(HXSDKHelper.getInstance()).getModel()!=null) {
            (HXSDKHelper.getInstance()).getModel().setSettingMsgSound(paramBoolean);
            (HXSDKHelper.getInstance()).getModel().setSettingMsgVibrate(paramBoolean);
        }
    }

    /**
     * 免骚扰设置
     * @return
     */
    public static boolean getNoInterrupt(){
        if((HXSDKHelper.getInstance()!=null)&&((DemoHXSDKHelper)HXSDKHelper.getInstance())!=null&&(HXSDKHelper.getInstance()).getModel()!=null) {
            return  (HXSDKHelper.getInstance()).getModel().getSettingMsgVibrate();
        }
        return false;
    }

}
