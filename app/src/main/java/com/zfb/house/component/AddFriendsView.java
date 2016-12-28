package com.zfb.house.component;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMContactManager;
import com.google.gson.Gson;
import com.lemon.LemonCacheManager;
import com.lemon.LemonContext;
import com.lemon.LemonMessage;
import com.lemon.net.ApiManager;
import com.lemon.util.DisplayUtils;
import com.lemon.util.GlideUtil;
import com.zfb.house.R;
import com.zfb.house.emchat.temp.AddFriendBean;
import com.zfb.house.model.bean.UserBean;

/**
 * Created by linwenbing on 16/7/5.
 */
public class AddFriendsView extends LinearLayout implements View.OnClickListener{
    private ImageView ivHeader;
    private LinearLayout llAddMain;
    protected ApiManager apiManager;
    private Context mContext;
    private LemonMessage lemonMessage;
    private LoadDialog mLoadDialog;
    private String mUid,mUserPhoto,mUserName;
    private LemonCacheManager cacheManager;
    private String mUserType = "0";
    private TextView tvName;
    private Button btnAdd;
    public AddFriendsView(Context context) {
        super(context);
    }

    public AddFriendsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_add_friends, this);
        apiManager = LemonContext.getBean(ApiManager.class);
        cacheManager = LemonContext.getBean(LemonCacheManager.class);
        mLoadDialog = new LoadDialog(mContext);
        initView();

    }

    private void initView(){
        llAddMain = (LinearLayout) findViewById(R.id.ll_add_friends_main);
        ivHeader = (ImageView) findViewById(R.id.iv_avatar);
        tvName = (TextView) findViewById(R.id.tv_name);
        findViewById(R.id.iv_delete).setOnClickListener(this);
        btnAdd = (Button)findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);

    }

    private void addFriends(){
        if (TextUtils.isEmpty(mUid)) return;

        showAppayDialog();
    }

    public void setHeaderAndUid(String url,String uid,String userName,String userType){
        mUid = uid;
        mUserPhoto = url;
        mUserName = userName;
        if (!TextUtils.isEmpty(userType)){
            mUserType = userType;
        }
        GlideUtil.getInstance().loadUrl(mContext, url, ivHeader);
    }

    private void  showAppayDialog(){
        final Dialog dialog = new Dialog(mContext, R.style.loading_dialog_themes);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_add_friends_apply, null);
        final EditText et = (EditText) view.findViewById(R.id.et_apply_content);
        view.findViewById(R.id.btn_sure).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (TextUtils.isEmpty(et.getText().toString())){
                    Toast.makeText(mContext,"请输入内容",Toast.LENGTH_SHORT).show();
                    return;
                }

                dialog.dismiss();
                doAddFriends(et.getText().toString());
            }
        });

        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });

        final int viewWidth = DisplayUtils.dip2px(mContext, 300);
        view.setMinimumWidth(viewWidth);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(view);
        dialog.show();
    }

    private void doAddFriends(final String message){
        mLoadDialog.show();
        new Thread(new Runnable() {
            public void run() {

                try {
                    //demo写死了个reason，实际应该让用户手动填入

                    String  s = getResources().getString(R.string.Add_a_friend);
                    AddFriendBean bean = new AddFriendBean();
                    if (TextUtils.isEmpty(message)) {
                        bean.message = s;
                    }else{
                        bean.message = message;
                    }

                    bean.fromUserImageUrl= UserBean.getInstance(mContext).photo;
                    bean.fromUserName=UserBean.getInstance(mContext).name;
                    bean.fromUserId= UserBean.getInstance(mContext).id;
                    bean.toUserId= mUid;
                    bean.toUserImageUrl= mUserPhoto;
                    bean.toUserName= mUserName;
                    Gson gson = new Gson();

                    EMContactManager.getInstance().addContact(mUid,gson.toJson(bean) );
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        public void run() {
                            mLoadDialog.dismiss();
                            String s1 = getResources().getString(R.string.send_successful);
                            Toast.makeText(mContext, s1, Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (final Exception e) {
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        public void run() {
                            mLoadDialog.dismiss();
                            String s2 = getResources().getString(R.string.Request_add_buddy_failure);
                            Toast.makeText(mContext, s2 + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add:
                showAppayDialog();
                break;
            case R.id.iv_delete:
                llAddMain.setVisibility(View.GONE);
                break;
        }
    }
}
