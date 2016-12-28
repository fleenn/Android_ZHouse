package com.zfb.house.emchat.temp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMContactManager;
import com.google.gson.Gson;
import com.lemon.util.LogUtils;
import com.zfb.house.R;
import com.zfb.house.emchat.AlertDialog;
import com.zfb.house.emchat.DemoHXSDKHelper;
import com.zfb.house.emchat.HXSDKHelper;
import com.zfb.house.model.bean.UserBean;

/**
 * Created by user on 2015-10-28.
 */
public class AddFriendCodeFragment extends BaseActivityFragment implements View.OnClickListener{

    private ProgressDialog progressDialog;
    private EditText editText;
    private String userId;
    private String userName;//用户昵称
    private String imgUrl;
    public static AddFriendCodeFragment newInstance(Bundle args, BaseFragmentsActivity activity) {
        AddFriendCodeFragment fragment = new AddFriendCodeFragment();
        if (args != null) {
            System.out.println("args不为空");
            fragment.setArguments(args);
        } else {
            System.out.println("args为空");
        }
        fragment.setBaseActivity(activity);
        return fragment;
    }

    @Override
    protected void findViews() {
        editText = (EditText)findViewById(R.id.add_friend_et);
    }

    @Override
    protected void setListensers() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_addfriend_code;
    }

    @Override
    protected View initTitleView() {
        View titleView = inflater.inflate(R.layout.title_with_back, null);
        titleTextView = (TextView)titleView.findViewById(R.id.title_tv);
        titleTextView.setText(R.string.add_friend_code);

        rightBtn = (TextView) titleView.findViewById(R.id.titleRightBtn);
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setText("添加");
        rightBtn.setOnClickListener(this);

        titleView.findViewById(R.id.back_layout).setOnClickListener(this);
        return titleView;
    }

    @Override
    public void initFragmentData() {
        if(getArguments()!=null){
            userId = getArguments().getString("userId");
            userName = getArguments().getString("userName");
            imgUrl = getArguments().getString("imgUrl");
        }
    }

    @Override
    protected void response(int actionId, Object result) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_layout:
                getActivity().finish();
                break;
            case R.id.titleRightBtn:
                if(userId==null ||"".equals(userId)){
                    LogUtils.toast(getActivity(),"无效好友");
                    return;
                }
                if(TextUtils.isEmpty(
                        ((EditText) findViewById(R.id.add_friend_et)).getText().toString())){
                    LogUtils.toast(getActivity(),"请输入验证信息");
                    return;
                }
                
                //调用环信的东西
                addContact();
                break;
        }

    }


    /**
     *  添加contact
     * @param
     */
    public void addContact(){
        if(UserBean.getInstance(getActivity()).id!=null && UserBean.getInstance(getActivity()).id.equals(userId)){
            String str = getString(R.string.not_add_myself);
            startActivity(new Intent(getActivity(), AlertDialog.class).putExtra("msg", str));
            return;
        }

        if(((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList().containsKey(userId)){
            //提示已在好友列表中，无需添加
            if(EMContactManager.getInstance().getBlackListUsernames().contains(userId)){
                startActivity(new Intent(getActivity(), AlertDialog.class).putExtra("msg", "此用户已是你好友(被拉黑状态)，从黑名单列表中移出即可"));
                return;
            }
            String strin = getString(R.string.This_user_is_already_your_friend);
            startActivity(new Intent(getActivity(), AlertDialog.class).putExtra("msg", strin));
            return;
        }

        progressDialog = new ProgressDialog(getActivity());
        String stri = getResources().getString(R.string.Is_sending_a_request);
        progressDialog.setMessage(stri);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {

                try {
                    //demo写死了个reason，实际应该让用户手动填入
                    String s =  editText.getText().toString().trim();
                    AddFriendBean bean = new AddFriendBean();
                    bean.message=s;
                    bean.fromUserImageUrl=UserBean.getInstance(getActivity()).photo;
                    bean.fromUserName=UserBean.getInstance(getActivity()).name;
                    bean.fromUserId= UserBean.getInstance(getActivity()).id;
                    bean.toUserId=userId;
                    bean.toUserImageUrl=imgUrl;
                    bean.toUserName=userName;
                    Gson gson = new Gson();

                    EMContactManager.getInstance().addContact(userId,gson.toJson(bean) );
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s1 = getResources().getString(R.string.send_successful);
                            Toast.makeText(getActivity(), s1, Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }
                    });
                } catch (final Exception e) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s2 = getResources().getString(R.string.Request_add_buddy_failure);
                            Toast.makeText(getActivity(), s2 + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }


}
