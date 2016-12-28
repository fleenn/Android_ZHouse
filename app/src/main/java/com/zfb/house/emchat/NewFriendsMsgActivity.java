/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zfb.house.emchat;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.google.gson.Gson;
import com.lemon.LemonContext;
import com.lemon.model.StatusCode;
import com.lemon.net.ApiManager;
import com.lemon.util.LogUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.component.LoadDialog;
import com.zfb.house.emchat.temp.AddFriendBean;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.model.param.AddFriendsParam;
import com.zfb.house.model.result.AddFriendsResult;

import java.util.ArrayList;
import java.util.List;


/**
 * 申请与通知
 *
 */
public class NewFriendsMsgActivity extends FragmentActivity {
	private ListView listView;
	private List<InviteMessage> msgsN;
	private NewFriendsMsgAdapter adapter;
	private AddFriendsParam addFriendsParam;
	private ApiManager apiManager;
	private Gson gson = new Gson();
	private LoadDialog mLoadDialog;
	private int mPosition;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_friends_msg);
		apiManager = LemonContext.getBean(ApiManager.class);
		mLoadDialog = new LoadDialog(this);
		TextView titleTv = (TextView) findViewById(R.id.tv_title_center);
		titleTv.setText("好友申请");
		titleTv.setVisibility(View.VISIBLE);
		listView = (ListView) findViewById(R.id.list);
		InviteMessgeDao dao = new InviteMessgeDao(this);
		List<InviteMessage> msgs = dao.getMessagesList();
		msgsN = new ArrayList<>();

		if(msgs!=null){
			for(InviteMessage inviteMessage:msgs){
				LogUtils.i("好友申请",inviteMessage.toString());
				LogUtils.i("linwb",inviteMessage.toString());
				if(inviteMessage.getReason()==null ){
					if(inviteMessage.getStatus()==InviteMessage.InviteMesageStatus.BEAGREED){
						msgsN.add(inviteMessage);
					}

				}else{
					Gson gson = new Gson();
					AddFriendBean bean = gson.fromJson(inviteMessage.getReason(), AddFriendBean.class);
					if(bean!=null){
						if(((bean.toUserId!=null&&bean.toUserId.equals(UserBean.getInstance(this).id))	)){
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
			public void onClick(InviteMessage info,int position) {
				mPosition = position;
				mLoadDialog.show();
				addFriends(info);
			}
		});
	}

	private void addFriends(InviteMessage info){
		if (addFriendsParam == null) {
			addFriendsParam = new AddFriendsParam();
			addFriendsParam.setToken(SettingUtils.get(this, "token", null));
		}
		AddFriendBean bean = gson.fromJson(info.getReason(), AddFriendBean.class);
		Log.i("linwb","id = " + bean.fromUserId);
		addFriendsParam.setFriendId(bean.fromUserId);
		apiManager.addFriend(addFriendsParam);
	}

	//接口返回值处理
	public void onEventMainThread(AddFriendsResult result) {
		mLoadDialog.dismiss();
		if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
			Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
			acceptInvitation(msgsN.get(mPosition));
		}

	}

	/**
	 * 同意好友请求
	 * @param msg
	 */
	private void acceptInvitation(final InviteMessage msg) {

		new Thread(new Runnable() {
			public void run() {
				// 调用sdk的同意方法
				try {
					if(msg.getGroupId() == null) //同意好友请求
						EMChatManager.getInstance().acceptInvitation(msg.getFrom());
					else //同意加群申请
						EMGroupManager.getInstance().acceptApplication(msg.getFrom(), msg.getGroupId());
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							msgsN.get(mPosition).setStatus(InviteMessage.InviteMesageStatus.AGREED);
							adapter.notifyDataSetChanged();
							// 更新db
							ContentValues values = new ContentValues();
							values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg.getStatus().ordinal());

						}
					});
				} catch (final Exception e) {

				}
			}
		}).start();
	}


	public void back(View view) {
		finish();
	}
}
