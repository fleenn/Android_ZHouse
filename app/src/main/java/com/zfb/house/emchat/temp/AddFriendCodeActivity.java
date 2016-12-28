package com.zfb.house.emchat.temp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


/**
 * 新增好友的验证
 * Created by zzh on 2015-10-28.
 */
public class AddFriendCodeActivity extends BaseFragmentsActivity {
    public static void launch(String userId,String userName,String imgUrl,Context context){
        Intent intent = new Intent();
        intent.setClass(context, AddFriendCodeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userId",userId);
        bundle.putString("userName",userName);
        bundle.putString("imgUrl",imgUrl);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
    @Override
    protected BaseActivityFragment createFragment(Bundle arg0) {
        return AddFriendCodeFragment.newInstance(arg0, AddFriendCodeActivity.this);
    }

    @Override
    public void executeAction(int actionId, Object params) {

    }
}
