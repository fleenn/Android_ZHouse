package com.lemon.util;

import com.lemon.bean.BeanFactory;
import com.lemon.net.ApiManager;
import com.zfb.house.model.param.SmsParam;

/**
 * Created by Snekey on 2016/4/24.
 */
public class SmsUtil {

    public static boolean sendSms(String phone){
        if(!ParamUtils.isPhoneNumberValid(phone)){
            return false;
        }
        SmsParam smsParam = new SmsParam();
        smsParam.setPhone(phone);
        BeanFactory.getInstance().getBean(ApiManager.class).getSms(smsParam);
        return true;
    }


}
