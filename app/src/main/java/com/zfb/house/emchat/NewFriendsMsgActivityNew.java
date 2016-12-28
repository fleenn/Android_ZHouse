package com.zfb.house.emchat;

import android.content.ContentValues;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.google.gson.Gson;
import com.lemon.LemonActivity;
import com.lemon.LemonCacheManager;
import com.lemon.LemonContext;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.config.Config;
import com.lemon.model.StatusCode;
import com.lemon.util.LogUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.component.LoadDialog;
import com.zfb.house.emchat.temp.AddFriendBean;
import com.zfb.house.init.FriendsListInitializer;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.model.param.AddFriendsParam;
import com.zfb.house.model.result.AddFriendsResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linwenbing on 16/7/21.
 */
@Layout(id = R.layout.activity_new_friends_msg)
public class NewFriendsMsgActivityNew extends LemonActivity{
    @FieldView(id = R.id.list)
    private ListView listView;
    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        finish();
    }

    private LoadDialog  mLoadDialog;
    private List<InviteMessage> msgsN;
    private NewFriendsMsgAdapter adapter;
    private int mPosition;
    private AddFriendsParam addFriendsParam;
    private Gson gson = new Gson();
    private InviteMessgeDao messgeDao;
    private AddFriendBean friendBean;

    @Override
    protected void initView() {
        setCenterText(R.string.title_to_apply_friend);
        mLoadDialog = new LoadDialog(this);
    }

    @Override
    protected void initData() {
        super.initData();
        InviteMessgeDao dao = new InviteMessgeDao(this);
        List<InviteMessage> msgs = dao.getMessagesList();
        msgsN = new ArrayList<>();
        messgeDao = new InviteMessgeDao(this);

        if(msgs!=null){
            for(InviteMessage inviteMessage:msgs){
                LogUtils.i("好友申请", inviteMessage.toString());
                if(inviteMessage.getReason()==null ){
                    if(inviteMessage.getStatus()==InviteMessage.InviteMesageStatus.BEAGREED){
                        msgsN.add(inviteMessage);
                    }

                }else{
                    Gson gson = new Gson();
                    AddFriendBean bean = gson.fromJson(inviteMessage.getReason(), AddFriendBean.class);
                    if(bean!=null){
                        if(((bean.toUserId!=null && bean.toUserId.equals(UserBean.getInstance(this).id)))){
                            if (!TextUtils.isEmpty(judueFriends(bean.fromUserId))){
                                inviteMessage.setStatus(InviteMessage.InviteMesageStatus.AGREED);
                            }
                            msgsN.add(inviteMessage);
                        }
                    }
                }
            }
        }

        //设置adapter
        adapter = new NewFriendsMsgAdapter(this, 1, msgsN);
        listView.setAdapter(adapter);
        ChatLoginUtil.setNewFriendUnread();

        adapter.setOnAddClickListener(new NewFriendsMsgAdapter.OnAddClickListener() {
            @Override
            public void onClick(InviteMessage info, int position) {
                mPosition = position;
                mLoadDialog.show();
                addFriends(info);
            }
        });
    }

    /**
     * 判断是否为好友
     * @return
     */
    private String judueFriends(String userId){
        cacheManager = LemonContext.getBean(LemonCacheManager.class);
        Log.i("linwb","userId = " + userId);
        if (TextUtils.isEmpty(userId)) return "error";

        if (userId.equals("admin")) return "admin";
        List<String> userFriends = (List<String>) cacheManager.getBean(Config.getValue("userFriends"));
        List<String> brokerFriends = (List<String>) cacheManager.getBean(Config.getValue("brokerFriends"));
        if (userFriends != null){
            for (int i = 0;i < userFriends.size();i++){
                if (userId.equals(userFriends.get(i))){
                    return userId;
                }
            }
        }
        if (brokerFriends != null){
            for (int i = 0;i < brokerFriends.size();i++){
                if (userId.equals(brokerFriends.get(i))){
                    return userId;
                }
            }
        }

        return "";
    }


    private void addFriends(InviteMessage info){
        if (addFriendsParam == null) {
            addFriendsParam = new AddFriendsParam();
            addFriendsParam.setToken(SettingUtils.get(this, "token", null));
        }
        friendBean = gson.fromJson(info.getReason(), AddFriendBean.class);
        addFriendsParam.setFriendId(friendBean.fromUserId);
        apiManager.addFriend(addFriendsParam);
    }

    //接口返回值处理
    public void onEventMainThread(AddFriendsResult result) {
        mLoadDialog.dismiss();
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();

            MsgFriendsInfo info = new MsgFriendsInfo();
            info.setUserId(UserBean.getInstance(this).id);
            info.setUserName(UserBean.getInstance(this).name);
            info.setUserPhone(UserBean.getInstance(this).photo);
            msgsN.get(mPosition).setFrom(new Gson().toJson(info));
            Log.i("linwb", "msgsN == " + new Gson().toJson(info));
            acceptInvitation(msgsN.get(mPosition),info.getUserId());
            FriendsListInitializer bean = LemonContext.getBean(FriendsListInitializer.class);
            bean.getFriendListByLogin(SettingUtils.get(this, "token", null));
        }
    }

    /**
     * 同意好友请求
     * @param msg
     */
    private void acceptInvitation(final InviteMessage msg,final String userId) {

        new Thread(new Runnable() {
            public void run() {
                // 调用sdk的同意方法
                try {
                    //同意好友请求
                    Log.i("linwb","msg.getFrom() = " + msg.getFrom());
                    EMChatManager.getInstance().acceptInvitation(userId);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            msgsN.get(mPosition).setStatus(InviteMessage.InviteMesageStatus.AGREED);
                            adapter.notifyDataSetChanged();
                            // 更新db
                            ContentValues values = new ContentValues();
                            msg.setStatus(InviteMessage.InviteMesageStatus.AGREED);
                            values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg.getStatus().ordinal());
                            messgeDao.updateMessage(msg.getId(), values);
                        }
                    });
                } catch (final Exception e) {

                }
            }
        }).start();
    }
}
