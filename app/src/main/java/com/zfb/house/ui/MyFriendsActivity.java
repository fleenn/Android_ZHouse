package com.zfb.house.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.emchat.Constant;
import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.model.StatusCode;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.MyFriendsAdapter;
import com.zfb.house.component.SideBar;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.model.param.MyFriendsParam;
import com.zfb.house.model.result.MyFriendsResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 我的->我的好友
 * Created by Administrator on 2016/5/17.
 */
@Layout(id = R.layout.activity_my_friends)
public class MyFriendsActivity extends LemonActivity implements SideBar.OnTouchingLetterChangedListener {

    //好友列表
    @FieldView(id = R.id.list_my_friends)
    private ListView mListView;
    //自定义右侧滑动条
    @FieldView(id = R.id.sidebar)
    private SideBar mSideBar;
    //显示在屏幕中央的字母浮层
    @FieldView(id = R.id.txt_mask)
    private TextView txtMask;

    //好友列表适配器
    private MyFriendsAdapter myFriendsAdapter;
    //经纪人好友列表
    private List<UserBean> brokerFriendsList = new ArrayList<>();
    //用户好友列表
    private List<UserBean> userFriendsList = new ArrayList<>();

    //经纪人好友
    @FieldView(id = R.id.rlayout_friends_broker)
    private RelativeLayout rlayoutBroker;
    @FieldView(id = R.id.txt_friends_broker)
    private TextView txtBroker;
    @FieldView(id = R.id.view_friends_broker)
    private View viewBroker;
    //用户好友
    @FieldView(id = R.id.rlayout_friends_user)
    private RelativeLayout rlayoutUser;
    @FieldView(id = R.id.txt_friends_user)
    private TextView txtUser;
    @FieldView(id = R.id.view_friends_user)
    private View viewUser;
    //token
    private String token;
    //角色
    private String userType;
    private MyFriendsParam fiendsParam;

    @Override
    protected void initView() {
        setCenterText(R.string.img_mine_friends);
        setRtImg(R.drawable.mine_add_friends);
        token = SettingUtils.get(mContext, "token", null);

        //设置列表的间隔为空
        mListView.setDivider(null);
        //设置列表的监听事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserBean item;
                if (rlayoutBroker.isSelected()) {//经纪人好友
                    item = brokerFriendsList.get(position);
                    toDetail("1", item.id, ParamUtils.isEmpty(item.remark) ? item.name : item.remark, position);
                } else if (rlayoutUser.isSelected()) {//用户好友
                    item = userFriendsList.get(position);
                    toDetail("0", item.id, ParamUtils.isEmpty(item.remark) ? item.name : item.remark, position);
                }
            }
        });
    }

    @Override
    protected void initData() {
        mSideBar.setTextView(txtMask);
        mSideBar.setOnTouchingLetterChangedListener(this);

        //调用“好友列表”接口——请求经纪人好友列表
        fiendsParam = new MyFriendsParam();
        fiendsParam.setToken(token);
        fiendsParam.setUserType("1");
        apiManager.listMyFriends(fiendsParam);
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        finish();
    }

    /**
     * 添加好友
     */
    @OnClick(id = R.id.img_title_rt)
    public void toAddFriends() {
        startActivity(new Intent(mContext, AddFriendsActivity.class));
    }

    /**
     * 搜索好友
     */
    @OnClick(id = R.id.txt_search_friends)
    public void searchFriend() {
        Intent intent = new Intent(mContext, SearchFriendsActivity.class);
        intent.putExtra("brokerFriendsList", (Serializable) brokerFriendsList);
        intent.putExtra("userFriendsList", (Serializable) userFriendsList);
        startActivity(intent);
    }

    /**
     * 显示经纪人好友
     */
    @OnClick(id = R.id.rlayout_friends_broker)
    public void showBrokerFriendsList() {
        setFriendClick(true, brokerFriendsList);
    }

    /**
     * 显示用户好友
     */
    @OnClick(id = R.id.rlayout_friends_user)
    public void showUserFriendsList() {
        setFriendClick(false, userFriendsList);
    }

    /**
     * 设置经纪人好友 or 用户好友的点击事件
     * flag为true表示点击了经纪人好友，反之点击了用户好友
     *
     * @param flag     标记
     * @param dataList 数据源
     */
    public void setFriendClick(boolean flag, List<UserBean> dataList) {
        //经纪人好友
        rlayoutBroker.setSelected(flag);
        viewBroker.setSelected(flag);
        //用户好友
        rlayoutUser.setSelected(!flag);
        viewUser.setSelected(!flag);
        Resources resources = getResources();
        if (flag) {
            txtBroker.setTextColor(resources.getColor(R.color.my_orange_one));
            txtUser.setTextColor(resources.getColor(R.color.my_gray_one));
            viewBroker.setVisibility(View.VISIBLE);
            viewUser.setVisibility(View.GONE);
            txtBroker.setTextSize(16);
            txtUser.setTextSize(14);
        } else {
            txtUser.setTextColor(resources.getColor(R.color.my_orange_one));
            txtBroker.setTextColor(resources.getColor(R.color.my_gray_one));
            viewUser.setVisibility(View.VISIBLE);
            viewBroker.setVisibility(View.GONE);
            txtUser.setTextSize(16);
            txtBroker.setTextSize(14);
        }
        setDataList(mListView, dataList);
    }

    @Override
    public void onTouchingLetterChanged(String s) {
        int position = 0;
        // 该字母首次出现的位置
        if (null != myFriendsAdapter) {
            position = myFriendsAdapter.getPositionForSection(s.charAt(0));
        }
        if (position != -1) {
            mListView.setSelection(position);
        }
    }

    /**
     * 设置数据列表
     *
     * @param mListView ListView
     * @param dataList  数据源
     */
    private void setDataList(ListView mListView, List<UserBean> dataList) {
        myFriendsAdapter = ParamUtils.isEmpty(dataList) ? null : new MyFriendsAdapter(mContext, dataList);
        mListView.setAdapter(myFriendsAdapter);
    }

    /**
     * 自定义排序
     *
     * @param dataList 从服务器返回的数据列表
     * @return 排好序的数据列表
     */
    private List getSortedDataList(List dataList) {
        List result = new ArrayList();
        // 在这定义"第一个拼音是字母的列表项的位置" 默认它是-1
        int firstLocation = -1;
        // 第一次循环 在找第一个字母位置的同时 将第一个字母之后所有的元素加到返回值列表中
        for (int i = 0; i < dataList.size(); i++) {
            UserBean bean = (UserBean) dataList.get(i);
            if (firstLocation < 0) {
                // 如果到目前为止还没发现字母的话
                char char1 = 0;
                if (!ParamUtils.isEmpty(bean.nameFullPinyin)) {
                    char1 = bean.nameFullPinyin.toUpperCase().charAt(0);
                }
                if (char1 >= 'A' && char1 <= 'Z') {
                    //如果首字母是字母就标记这个位置
                    firstLocation = i;
                }
            }
            if (firstLocation >= 0) {
                // 如果曾经找到过字母就说明后面的都按照顺序加到列表就好
                result.add(dataList.get(i));
            }
        }
        // 如果整个列表都没找到字母 那还是将整个列表重新加一遍
        if (firstLocation == -1) {
            firstLocation = dataList.size();
        }
        // 到这里firstLocation的值要么是第一个字母的位置 要么就是列表的长度 按顺序直接往后加就行了
        for (int i = 0; i < firstLocation; i++) {
            result.add(dataList.get(i));
        }
        return result;
    }

    /**
     * 自定义比较方法
     *
     * @param left  列表中的数据
     * @param right
     * @return
     */
    private int customCompare(String left, String right) {
        int result;
        boolean isLeftLetterStarted = left.toUpperCase().charAt(0) >= 'A' && left.toUpperCase().charAt(0) <= 'Z';
        boolean isRightLetterStarted = right.toUpperCase().charAt(0) >= 'A' && right.toUpperCase().charAt(0) <= 'Z';
        if (isLeftLetterStarted == isRightLetterStarted) {//左边和右边都是字母或者都是特殊字符
            result = left.toUpperCase().compareTo(right.toUpperCase());
        } else if (isLeftLetterStarted && !isRightLetterStarted) {//左边是字母，右边是特殊字符，就强制认为左边小于右边
            result = -1;
        } else {//左边是特殊字符，右边是字母，就强制认为左边大于右边
            result = 1;
        }
        return result;
    }

    /**
     * 好友列表
     *
     * @param result
     */
    public void onEventMainThread(MyFriendsResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            //获得要请求的用户类型
            userType = ((MyFriendsParam) result.getParam()).getUserType();
            if (userType.equals("1")) {//经纪人好友
                brokerFriendsList = getSortedDataList(result.getData());
                //默认选中经纪人好友，显示经纪人好友列表的数据
                rlayoutBroker.performClick();
                setDataList(mListView, brokerFriendsList);

                fiendsParam.setUserType("0");
                apiManager.listMyFriends(fiendsParam);
            } else if (userType.equals("0")) {//用户好友
                userFriendsList = getSortedDataList(result.getData());
            }
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.REQUEST_MODIFY_REMARKS:// 修改备注
                    //用户返回的item
                    UserBean item = myFriendsAdapter.getItem(data.getIntExtra("position", 0));
                    if (data.getBooleanExtra("isDelete", false)) {
                        if (item.userType.equals("1")) {
                            brokerFriendsList.remove(item);
                        } else if (item.userType.equals("0")) {
                            userFriendsList.remove(item);
                        }
                    }
                    //当前显示的数据列表
                    List<UserBean> userList = item.userType.equals("1") ? brokerFriendsList : userFriendsList;
                    //获得用户返回的是否删除好友的标记，isDelete为true就表示已经删除好友，否则没有删除好友
                    String remark = data.getStringExtra("remark");
                    String nameFullPinyin = data.getStringExtra("pinyin");
                    if (!ParamUtils.isEmpty(remark) && !ParamUtils.isEmpty(nameFullPinyin) && userList.contains(item)) {
                        //获得用户返回的Item的备注名
                        item.remark = remark;
                        //获得用户返回的Item的备注名拼音
                        item.nameFullPinyin = nameFullPinyin;
                        // 将刚刚修改过的用户信息 取出 查找符合的位置并将其插入此位置
                        userList.remove(item);
                        // 是否插入过
                        boolean added = false;
                        for (int i = 0; i < userList.size(); i++) {
                            if (customCompare(userList.get(i).nameFullPinyin, item.nameFullPinyin) > 0) {
                                userList.add(i, item);
                                added = true;
                                break;
                            }
                        }
                        // 如果最后都没有插入过 就插入到最后
                        if (!added) {
                            userList.add(item);
                        }
                    }
                    //设置返回后显示的界面
                    if (item.userType.equals("1")) {
                        rlayoutBroker.performClick();
                    } else if (item.userType.equals("0")) {
                        rlayoutUser.performClick();
                    }
                    if (!ParamUtils.isEmpty(userList)) {
                        myFriendsAdapter.updateListView(userList);
                    }
                    break;
            }
        }
    }


}
