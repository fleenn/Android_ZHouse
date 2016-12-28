package com.zfb.house.ui;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.SearchFriendsAdapter;
import com.zfb.house.model.bean.UserBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的-> 我的好友-> 搜索好友
 * Created by Administrator on 2016/6/29.
 */
@Layout(id = R.layout.activity_search_friends)
public class SearchFriendsActivity extends LemonActivity {

    //用户输入的文字
    @FieldView(id = R.id.edt_search_content)
    private EditText edtName;
    //搜索结果列表
    @FieldView(id = R.id.list_search_friends)
    private ListView mListView;
    //提示找不到好友的区域
    @FieldView(id = R.id.rlayout_hint)
    private RelativeLayout rlayoutHint;
    //搜索好友的适配器
    private SearchFriendsAdapter searchFriendsAdapter;
    //我的所有好友的列表
    private List<UserBean> myFriendsList = new ArrayList<>();
    //搜索出来好友的列表
    private List<UserBean> searchFriendsList = new ArrayList<>();
    //标记，flag=true表示搜索备注，flag=false表示搜索昵称，默认认为用户搜索昵称(false)
    private Boolean flag = false;

    @Override
    protected void initData() {
        mListView.setDivider(null);
        Intent intent = getIntent();
        myFriendsList = (List<UserBean>) intent.getSerializableExtra("brokerFriendsList");
        myFriendsList.addAll((List<UserBean>) intent.getSerializableExtra("userFriendsList"));

        //添加监听用户输入文字的事件
        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence str, int start, int before, int count) {
                //隐藏提示
                rlayoutHint.setVisibility(View.GONE);
                //清空搜索到的列表
                searchFriendsList.clear();
                if (!ParamUtils.isEmpty(str.toString())) {//有输入值
                    for (UserBean item : myFriendsList) {
                        //搜索所有好友的备注remark
                        if (!ParamUtils.isEmpty(item.remark)) {
                            if (item.remark.contains(str.toString())) {
                                flag = true;
                                searchFriendsList.add(item);
                            }
                        }
                        //搜索所有好友的名字name
                        if (!ParamUtils.isEmpty(item.name)) {
                            if (item.name.contains(str.toString())) {
                                if (!flag) {
                                    searchFriendsList.add(item);
                                }
                            }
                        }
                    }
                    if (searchFriendsList.size() == 0) {//无搜索结果
                        mListView.setAdapter(null);
                        rlayoutHint.setVisibility(View.VISIBLE);
                    } else {//有搜索结果
                        searchFriendsAdapter = new SearchFriendsAdapter(mContext, searchFriendsList);
                        mListView.setAdapter(searchFriendsAdapter);
                    }
                } else {//无输入值
                    mListView.setAdapter(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //设置列表的监听事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserBean item = searchFriendsList.get(position);
                String userId = item.id;
                String userType = item.userType;
                if (!ParamUtils.isEmpty(userType)) {
                    userType = userType.equals("1") ? "1" : "0";
                    if (!ParamUtils.isEmpty(userId) && !ParamUtils.isEmpty(userType)) {
                        toDetail(userType, userId, ParamUtils.isEmpty(item.remark) ? item.name : item.remark);
                    }
                }
            }
        });
    }

    /**
     * 取消搜索好友
     */
    @OnClick(id = R.id.txt_search_cancel)
    public void toCancel() {
        finish();
    }

}

