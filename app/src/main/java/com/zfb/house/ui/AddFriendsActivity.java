package com.zfb.house.ui;

import android.app.Dialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.easemob.chat.EMContactManager;
import com.google.gson.Gson;
import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.model.StatusCode;
import com.lemon.util.DisplayUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.AddSearchFriendsAdapter;
import com.zfb.house.component.LoadDialog;
import com.zfb.house.emchat.temp.AddFriendBean;
import com.zfb.house.model.bean.AddPeopleItem;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.model.param.SearchFriendsParam;
import com.zfb.house.model.result.SearchFriendsResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的 -> 我的好友 -> 添加好友
 * Created by Administrator on 2016/5/21.
 */
@Layout(id = R.layout.activity_add_friends)
public class AddFriendsActivity extends LemonActivity {

    @FieldView(id = R.id.et_input)
    private EditText etPhone;
    @FieldView(id = R.id.rv_people)
    private RecyclerView rvPeople;

    private AddSearchFriendsAdapter mAdapter;
    private List<AddPeopleItem> mList;
    private LoadDialog mLoadDialog;
    private SearchFriendsParam searchFriendsParam;

    @Override
    protected void initView() {
        setCenterText(R.string.title_to_add_friend);
        mLoadDialog = new LoadDialog(this);
    }

    @Override
    protected void initData() {
        super.initData();
        mList = new ArrayList<>();
        mAdapter = new AddSearchFriendsAdapter(this, mList);
        rvPeople.setLayoutManager(new LinearLayoutManager(this));
        rvPeople.setAdapter(mAdapter);
        mAdapter.setOnAddClickListener(new AddSearchFriendsAdapter.OnAddClickListener() {
            @Override
            public void onClick(AddPeopleItem info) {
                showAppayDialog(info);
            }

            @Override
            public void onIntentClick(AddPeopleItem info) {
                toDetail(info.getUserType(), info.getId(), info.getName());
            }
        });
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        finish();
    }

    @OnClick(id = R.id.tv_search)
    public void toSearch() {
        doSearch();
    }

    private void doAddFriends(final AddPeopleItem info, final String message) {
        mLoadDialog.show();
        new Thread(new Runnable() {
            public void run() {

                try {
                    //demo写死了个reason，实际应该让用户手动填入

                    String s = getResources().getString(R.string.Add_a_friend);
                    AddFriendBean bean = new AddFriendBean();
                    if (TextUtils.isEmpty(message)) {
                        bean.message = s;
                    } else {
                        bean.message = message;
                    }

                    bean.fromUserImageUrl = UserBean.getInstance(AddFriendsActivity.this).photo;
                    bean.fromUserName = UserBean.getInstance(AddFriendsActivity.this).name;
                    bean.fromUserId = UserBean.getInstance(AddFriendsActivity.this).id;
                    bean.toUserId = info.getId();
                    bean.toUserImageUrl = info.getPhoto();
                    bean.toUserName = info.getName();
                    Log.i("linwb","id == " + info.getId() + "name = " + info.getName());
                    Gson gson = new Gson();

                    EMContactManager.getInstance().addContact(info.getId(), gson.toJson(bean));
                    runOnUiThread(new Runnable() {
                        public void run() {
                            mLoadDialog.dismiss();
                            String s1 = getResources().getString(R.string.send_successful);
                            Toast.makeText(AddFriendsActivity.this, s1, Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            mLoadDialog.dismiss();
                            String s2 = getResources().getString(R.string.Request_add_buddy_failure);
                            Toast.makeText(AddFriendsActivity.this, s2 + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void doSearch() {
        String phone = etPhone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            lemonMessage.sendMessage("请输入手机号码");
            return;
        }
        mLoadDialog.show();
        if (searchFriendsParam == null) {
            searchFriendsParam = new SearchFriendsParam();
        }
        searchFriendsParam.setPhone(phone);
        apiManager.searchUser(searchFriendsParam);
    }

    public void onEventMainThread(SearchFriendsResult result) {
        mLoadDialog.dismiss();
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            mList.clear();
            mList.addAll(result.getData());
            for (int i = 0;i < mList.size();i++){
                if (mList.get(i).getId().equals(UserBean.getInstance(this).id)){
                    mList.remove(i);
                }
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    private void showAppayDialog(final AddPeopleItem info) {
        final Dialog dialog = new Dialog(this, R.style.loading_dialog_themes);

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_add_friends_apply, null);
        final EditText et = (EditText) view.findViewById(R.id.et_apply_content);
        view.findViewById(R.id.btn_sure).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (TextUtils.isEmpty(et.getText().toString())) {
                    lemonMessage.sendMessage("请输入内容");
                    return;
                }

                dialog.dismiss();
                doAddFriends(info, et.getText().toString());
            }
        });

        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });

        final int viewWidth = DisplayUtils.dip2px(this, 300);
        view.setMinimumWidth(viewWidth);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(view);
        dialog.show();
    }

}
