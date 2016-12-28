package com.zfb.house.emchat;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.lemon.LemonContext;
import com.zfb.house.R;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.util.ImageUtil;

public class UserUtils {
    /**
     * 根据username获取相应user，由于demo没有真实的用户数据，这里给的模拟的数据；
     * @param username
     * @return
     */
    public static User getUserInfo(String username){
		User user =null;
		if(ChatLoginUtil.ifEmChatValide()){
			if(((DemoHXSDKHelper)HXSDKHelper.getInstance()).getContactList()==null){
				user = null;
			}else {
				user = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getContactList().get(username);
			}/*if(user == null){
				user = new User(username);
			}*/
		}

        if(user != null){
            //demo没有这些数据，临时填充
        	/*if(TextUtils.isEmpty(user.getNick()))
        		user.setNick(username);
*/        }
        return user;
    }
    
    /**
     * 设置用户头像
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView){
    	User user = getUserInfo(username);
		
        if(user != null && user.getAvatar() != null){
			new ImageUtil().loadImageByVolley(imageView,
					user.getAvatar(), (Context)LemonContext.getBean("mContext"), R.drawable.broker_default, R.drawable.broker_default);
        }else{
			new ImageUtil().loadImageByVolley(imageView,
					null, (Context)LemonContext.getBean("mContext"), R.drawable.broker_default, R.drawable.broker_default);
        }
    }
    
    /**
     * 设置当前用户头像
     */
	public static void setCurrentUserAvatar(Context context, ImageView imageView) {
		User user = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getUserProfileManager().getCurrentUserInfo();
		if (user != null && user.getAvatar() != null) {
			Log.i("linwb", "ll == " + user.getAvatar());
			new ImageUtil().loadImageByVolley(imageView,
					user.getAvatar(), (Context)LemonContext.getBean("mContext"), R.drawable.default_avatar, R.drawable.broker_default);
		} else {
			UserBean localUserBean = UserBean.getInstance(context);
			if (TextUtils.isEmpty(localUserBean.photo)) {
				new ImageUtil().loadImageByVolley(imageView,
						null, (Context)LemonContext.getBean("mContext"), R.drawable.default_avatar, R.drawable.broker_default);
			}else{
				new ImageUtil().loadImageByVolley(imageView,
						localUserBean.photo, (Context)LemonContext.getBean("mContext"), R.drawable.default_avatar, R.drawable.broker_default);

			}
		}
	}
    
    /**
     * 设置用户昵称
     */
    public static void setUserNick(String username,TextView textView){
    	User user = getUserInfo(username);
    	if(user != null){
    		textView.setText(user.getNick());
    	}else{
    		textView.setText(username);
    	}
    }

    /**
     * 设置当前用户昵称
     */
    public static void setCurrentUserNick(TextView textView){
    	User user = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getUserProfileManager().getCurrentUserInfo();
    	if(textView != null){
    		textView.setText(user.getNick());
    	}
    }
    
    /**
     * 保存或更新某个用户
     * @param newUser
     */
	public static void saveUserInfo(User newUser) {
		if(ChatLoginUtil.ifEmChatValide()==false){
			return;
		}
		if (newUser == null || newUser.getUsername() == null) {
			return;
		}

        if(ChatLoginUtil.ifEmChatValide()){
            ((DemoHXSDKHelper) HXSDKHelper.getInstance()).saveContact(newUser);
        }
	}
    
}
