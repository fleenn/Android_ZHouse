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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMContactManager;
import com.zfb.house.R;
import com.zfb.house.emchat.temp.AddContactAdapter;
import com.zfb.house.emchat.temp.BaseLinearLayoutManager;
import com.zfb.house.emchat.temp.FriendSearchBean;
import com.zfb.house.model.bean.UserBean;

public class AddContactActivity extends FragmentActivity {
	public static void launch(Context context){
		Intent intent = new Intent();
		intent.setClass(context,AddContactActivity.class);
		context.startActivity(intent);
	}
	private EditText editText;
	private LinearLayout searchedUserLayout;
	private TextView nameText,mTextView;
	private Button searchBtn;
	private ImageView avatar;
	private InputMethodManager inputMethodManager;
	private String toAddUsername;
	private ProgressDialog progressDialog;

	private RecyclerView mRecyclerView;
	private BaseLinearLayoutManager layoutManager;
	SwipeRefreshLayout swipe_refresh;
	AddContactAdapter addContactAdapter;
	FriendSearchBean bean;

	@Override
	protected void onResume() {
		super.onResume();
		// onresume时，取消notification显示
		HXSDKHelper.getInstance().getNotifier().reset();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
		mTextView = (TextView) findViewById(R.id.add_list_friends);
		
		editText = (EditText) findViewById(R.id.edit_note);
		String strAdd = getResources().getString(R.string.add_friend);
		mTextView.setText(strAdd);;
		searchedUserLayout = (LinearLayout) findViewById(R.id.ll_user);
		nameText = (TextView) findViewById(R.id.name);
		searchBtn = (Button) findViewById(R.id.search);
		avatar = (ImageView) findViewById(R.id.avatar);
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);


		swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
		layoutManager = new BaseLinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mRecyclerView = (RecyclerView) findViewById(R.id.add_contact_list);
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setLayoutManager(layoutManager);
		addContactAdapter = new AddContactAdapter(bean,null,null,this);
		mRecyclerView.setAdapter(addContactAdapter);
		setListerner();
	}
	private void setListerner(){
		swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				searchContact(null);
			}
		});
	}
	
	/**
	 * 查找contact
	 * @param v
	 */
	public void searchContact(View v) {
		final String name = editText.getText().toString();
		String saveText = searchBtn.getText().toString();
		
		if (getString(R.string.button_search).equals(saveText)) {
			toAddUsername = name;
			if(TextUtils.isEmpty(name)) {
				String st = getResources().getString(R.string.Please_enter_a_mobile);
				startActivity(new Intent(this, AlertDialog.class).putExtra("msg", st));
				return;
			}

			swipe_refresh.setRefreshing(false);
			/*HttpUtils.findByMobile(this, new HttpUtils.HttpListener() {
				@Override
				public void onResponse(String response) {

					if(!TextUtils.isEmpty(response)){

						Gson gson = new Gson();

							FriendSearchBean friendSearchBean = gson.fromJson(response,FriendSearchBean.class);
						if(friendSearchBean!=null&&friendSearchBean.isSuccess()){
							addContactAdapter.setBean(friendSearchBean);
							addContactAdapter.notifyDataSetChanged();
							if(friendSearchBean.data!=null&&friendSearchBean.data.size()==0){
								LogUtils.toast(AddContactActivity.this,"好友不存在");
							}
						}
					}

				}

				@Override
				public void onErrorResponse(int code, String errorMessage) {

				}
			},toAddUsername);*/

			// TODO 从服务器获取此contact,如果不存在提示不存在此用户
			
			//服务器存在此用户，显示此用户和添加按钮
		/*	searchedUserLayout.setVisibility(View.VISIBLE);
			nameText.setText(toAddUsername);*/
			
		} 
	}	
	
	/**
	 *  添加contact
	 * @param view
	 */
	public void addContact(View view){
		if(UserBean.getInstance(this).name!=null && UserBean.getInstance(this).name.equals(nameText.getText().toString())){
			String str = getString(R.string.not_add_myself);
			startActivity(new Intent(this, AlertDialog.class).putExtra("msg", str));
			return;
		}
		
		if(((DemoHXSDKHelper)HXSDKHelper.getInstance()).getContactList().containsKey(nameText.getText().toString())){
		    //提示已在好友列表中，无需添加
		    if(EMContactManager.getInstance().getBlackListUsernames().contains(nameText.getText().toString())){
		        startActivity(new Intent(this, AlertDialog.class).putExtra("msg", "此用户已是你好友(被拉黑状态)，从黑名单列表中移出即可"));
		        return;
		    }
			String strin = getString(R.string.This_user_is_already_your_friend);
			startActivity(new Intent(this, AlertDialog.class).putExtra("msg", strin));
			return;
		}
		
		progressDialog = new ProgressDialog(this);
		String stri = getResources().getString(R.string.Is_sending_a_request);
		progressDialog.setMessage(stri);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		
		new Thread(new Runnable() {
			public void run() {
				
				try {
					//demo写死了个reason，实际应该让用户手动填入
					String s = getResources().getString(R.string.Add_a_friend);
					EMContactManager.getInstance().addContact(toAddUsername, s);
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							String s1 = getResources().getString(R.string.send_successful);
							Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_SHORT).show();
						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							String s2 = getResources().getString(R.string.Request_add_buddy_failure);
							Toast.makeText(getApplicationContext(), s2 + e.getMessage(), Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();
	}
	
	public void back(View v) {
		finish();
	}
}
