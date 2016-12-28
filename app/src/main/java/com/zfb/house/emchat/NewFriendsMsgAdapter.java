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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.google.gson.Gson;
import com.lemon.LemonContext;
import com.zfb.house.R;
import com.zfb.house.emchat.temp.AddFriendBean;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.util.ImageUtil;

import java.util.List;


public class NewFriendsMsgAdapter extends ArrayAdapter<InviteMessage> {

	private Context context;
	private InviteMessgeDao messgeDao;
	public interface OnAddClickListener{
		void onClick(InviteMessage info,int position);
	}

	private OnAddClickListener onAddClickListener;

	public OnAddClickListener getOnAddClickListener() {
		return onAddClickListener;
	}

	public void setOnAddClickListener(OnAddClickListener onAddClickListener) {
		this.onAddClickListener = onAddClickListener;
	}

	public NewFriendsMsgAdapter(Context context, int textViewResourceId, List<InviteMessage> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		messgeDao = new InviteMessgeDao(context);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.row_invite_msg, null);
			holder.refuse = (Button) convertView.findViewById(R.id.user_state_no);//拒绝
			holder.avator = (ImageView) convertView.findViewById(R.id.avatar);
			holder.reason = (TextView) convertView.findViewById(R.id.message);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.status = (Button) convertView.findViewById(R.id.user_state);
			holder.groupContainer = (LinearLayout) convertView.findViewById(R.id.ll_group);
			holder.groupname = (TextView) convertView.findViewById(R.id.tv_groupName);
			// holder.time = (TextView) convertView.findViewById(R.id.time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		String str1 = context.getResources().getString(R.string.Has_agreed_to_your_friend_request);
		String str2 = context.getResources().getString(R.string.agree);
		
		String str3 = context.getResources().getString(R.string.Request_to_add_you_as_a_friend);
		String str4 = context.getResources().getString(R.string.Apply_to_the_group_of);
		String str5 = context.getResources().getString(R.string.Has_agreed_to);
		String str6 = context.getResources().getString(R.string.Has_refused_to);
		String strRefuse = context.getResources().getString(R.string.agree_no);//拒绝
		final InviteMessage msg = getItem(position);
		if (msg != null) {
			if(msg.getGroupId() != null){ // 显示群聊提示
				holder.groupContainer.setVisibility(View.VISIBLE);
				holder.groupname.setText(msg.getGroupName());
			} else{
				holder.groupContainer.setVisibility(View.GONE);
			}
			final Gson gson = new Gson();
			final AddFriendBean bean = gson.fromJson(msg.getReason(),AddFriendBean.class);
			if(bean==null){
				holder.reason.setText(msg.getReason());
				holder.name.setText(msg.getFrom());
			}else{
				if(UserBean.getInstance(context).id.equals(msg.getFrom())){//如果是自己发出的
					holder.reason.setText(bean.message);
					holder.name.setText(bean.toUserName);
					// 设置用户头像
					if(!TextUtils.isEmpty(bean.toUserImageUrl)){
						new ImageUtil().loadImageByVolley(holder.avator,
								bean.toUserImageUrl, (Context) LemonContext.getBean("mContext"), R.drawable.broker_default, R.drawable.broker_default);
					}else{
						holder.avator.setImageResource(R.drawable.broker_default);
					}
				}else{
					holder.reason.setText(bean.message);
					holder.name.setText(bean.fromUserName);
					// 设置用户头像
					if(!TextUtils.isEmpty(bean.fromUserImageUrl)){
						new ImageUtil().loadImageByVolley(holder.avator,
								bean.fromUserImageUrl, (Context)LemonContext.getBean("mContext"), R.drawable.broker_default, R.drawable.broker_default);
					}else{
						holder.avator.setImageResource(R.drawable.broker_default);
					}
				}
			}
			// holder.time.setText(DateUtils.getTimestampString(new
			// Date(msg.getTime())));
			if (msg.getStatus() == InviteMessage.InviteMesageStatus.BEAGREED) {
				holder.refuse.setVisibility(View.GONE);
				holder.status.setVisibility(View.INVISIBLE);
				holder.reason.setText(str1);

				//设置昵称跟imageurl
				if(UserUtils.getUserInfo(msg.getFrom())!=null&&UserUtils.getUserInfo(msg.getFrom()).getNick()!=null&&!UserUtils.getUserInfo(msg.getFrom()).getNick().equals(msg.getFrom())){
					new ImageUtil().loadImageByVolley(holder.avator,
							UserUtils.getUserInfo(msg.getFrom()).getAvatar(), (Context)LemonContext.getBean("mContext"), R.drawable.default_avatar, R.drawable.broker_default);
					holder.name.setText(UserUtils.getUserInfo(msg.getFrom()).getNick());

				}else{
					/*HttpUtils.getUserListById(context, msg.getFrom(), new HttpUtils.HttpListener() {
						@Override
						public void onResponse(String response) {

							if(!TextUtils.isEmpty(response)){
								BaseBean baseBean =gson.fromJson(response,BaseBean.class);
								if(baseBean.isSuccess()){
									UserList userList = gson.fromJson(response,UserList.class);
									if(userList!=null&&userList.data!=null&&userList.data.size()>0){
										String nick = (TextUtils.isEmpty(userList.data.get(0).name))?userList.data.get(0).loginName:userList.data.get(0).name;
										holder.name.setText(nick);

										if(!TextUtils.isEmpty(userList.data.get(0).photo)){
											new ImageUtil().loadImageByVolley(holder.avator,
													userList.data.get(0).photo, (Context)LemonContext.getBean("mContext"), R.drawable.default_avatar, R.drawable.broker_default);
										}
										User user = new User();
										user.setAvatar(userList.data.get(0).photo);
										user.setUsername(userList.data.get(0).id);
										user.setNick(nick);
										UserUtils.saveUserInfo(user);
									}
								}
							}

						}
						@Override
						public void onErrorResponse(int code, String errorMessage) {

						}
					});*/

				}

			} else if (msg.getStatus() == InviteMessage.InviteMesageStatus.BEINVITEED || msg.getStatus() == InviteMessage.InviteMesageStatus.BEAPPLYED) {
				holder.status.setVisibility(View.VISIBLE);
				holder.status.setEnabled(true);
//				holder.status.setBackgroundResource(android.R.drawable.btn_default);
				holder.status.setText(str2);
				if(msg.getStatus() == InviteMessage.InviteMesageStatus.BEINVITEED){//被邀请

					/*holder.refuse.setVisibility(View.VISIBLE);////////////////////////////
					holder.refuse.setEnabled(true);
					holder.refuse.setBackgroundResource(android.R.drawable.btn_default);
					holder.refuse.setText(strRefuse);*/


					if (msg.getReason() == null) {
						// 如果没写理由
						holder.reason.setText(str3);
					}
				}else{ //入群申请
					if (TextUtils.isEmpty(msg.getReason())) {
						holder.reason.setText(str4 + msg.getGroupName());
					}
				}
				// 设置点击事件
				holder.status.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 同意别人发的好友请求
						if (onAddClickListener != null){
							onAddClickListener.onClick(msg,position);
						}
					}
				});
				holder.refuse.setOnClickListener(new OnClickListener() {//拒绝
					@Override
					public void onClick(View view) {
						refuseInvitation(holder.refuse,holder.status, msg);
					}
				});
			} else if (msg.getStatus() == InviteMessage.InviteMesageStatus.AGREED) {
				holder.status.setText(str5);
				holder.status.setEnabled(false);
				holder.refuse.setVisibility(View.GONE);
			} else if(msg.getStatus() == InviteMessage.InviteMesageStatus.REFUSED){
				holder.status.setText(str6);
				holder.status.setBackgroundDrawable(null);
				holder.status.setEnabled(false);
				holder.refuse.setVisibility(View.GONE);
			}
		}

		return convertView;
	}

	/**
	 * 同意好友请求或者群申请
	 * 
	 * @param button
	 * @param msg
	 */
	private void acceptInvitation(final Button button, final InviteMessage msg) {
		final ProgressDialog pd = new ProgressDialog(context);
		String str1 = context.getResources().getString(R.string.Are_agree_with);
		final String str2 = context.getResources().getString(R.string.Has_agreed_to);
		final String str3 = context.getResources().getString(R.string.Agree_with_failure);
		pd.setMessage(str1);
		pd.setCanceledOnTouchOutside(false);
		pd.show();

		new Thread(new Runnable() {
			public void run() {
				// 调用sdk的同意方法
				try {
					if(msg.getGroupId() == null) //同意好友请求
						EMChatManager.getInstance().acceptInvitation(msg.getFrom());
					else //同意加群申请
					    EMGroupManager.getInstance().acceptApplication(msg.getFrom(), msg.getGroupId());
					((Activity) context).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							pd.dismiss();
							button.setText(str2);
							msg.setStatus(InviteMessage.InviteMesageStatus.AGREED);
							// 更新db
							ContentValues values = new ContentValues();
							values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg.getStatus().ordinal());
							messgeDao.updateMessage(msg.getId(), values);
							button.setBackgroundDrawable(null);
							button.setEnabled(false);

						}
					});
				} catch (final Exception e) {
					((Activity) context).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							pd.dismiss();
							Toast.makeText(context, str3 + e.getMessage(), Toast.LENGTH_SHORT).show();
						}
					});

				}
			}
		}).start();
	}

	/**
	 * 同意好友请求或者群申请
	 *
	 * @param button
	 * @param msg
	 */
	private void refuseInvitation(final Button button,final Button btnAgree, final InviteMessage msg) {
		final ProgressDialog pd = new ProgressDialog(context);
		String str1 = context.getResources().getString(R.string.Are_refuse_with);
		final String str2 = context.getResources().getString(R.string.Has_refused_to);
		final String str3 = context.getResources().getString(R.string.refuse_with_failure);
		pd.setMessage(str1);
		pd.setCanceledOnTouchOutside(false);
		pd.show();

		new Thread(new Runnable() {
			public void run() {
				// 调用sdk的同意方法
				try {
					if(msg.getGroupId() == null) //同意好友请求
						EMChatManager.getInstance().refuseInvitation(msg.getFrom());
					else //同意加群申请
					{
						EMGroupManager.getInstance().declineApplication(msg.getFrom(), msg.getGroupId(),"拒绝加入此群");
					}
						((Activity) context).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							pd.dismiss();
							button.setText(str2);
							msg.setStatus(InviteMessage.InviteMesageStatus.REFUSED);
							// 更新db
							ContentValues values = new ContentValues();
							values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg.getStatus().ordinal());
							messgeDao.updateMessage(msg.getId(), values);
							button.setBackgroundDrawable(null);
							button.setEnabled(false);
							button.setVisibility(View.GONE);
							btnAgree.setBackgroundDrawable(null);
							btnAgree.setEnabled(false);
							btnAgree.setText(str2);
						}
					});
				} catch (final Exception e) {
					((Activity) context).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							pd.dismiss();
							Toast.makeText(context, str3 + e.getMessage(), Toast.LENGTH_SHORT).show();
						}
					});

				}
			}
		}).start();
	}

	private static class ViewHolder {
		ImageView avator;
		TextView name;
		TextView reason;
		Button status;
		Button refuse;//拒绝
		LinearLayout groupContainer;
		TextView groupname;
		// TextView time;
	}

	class UserList{
		public List<User>data;
		class User{
			public String id;
			public String name;
			public String loginName;
			public String photo;
		}
	}

}
