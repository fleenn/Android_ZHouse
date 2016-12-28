package com.lemon.util;

import de.greenrobot.event.EventBus;

/**
 * Created by Snekey on 2016/6/1.
 */
public class EventUtil {

    public static void register(Object context){
        if (!EventBus.getDefault().isRegistered(context)) {
            EventBus.getDefault().register(context);
        }
    }
    public static void unregister(Object context){
        if (EventBus.getDefault().isRegistered(context)) {
            EventBus.getDefault().unregister(context);
        }
    }
    public static void sendEvent(Object object){
        EventBus.getDefault().post(object);
    }
}
