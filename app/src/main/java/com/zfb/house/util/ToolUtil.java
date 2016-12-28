package com.zfb.house.util;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.TextView;

import com.lemon.event.ToastEvent;
import com.lemon.event.ToastEventRes;
import com.lemon.event.UpdatePointEvent;
import com.lemon.util.EventUtil;
import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.model.bean.ReleasePullItem;
import com.zfb.house.model.bean.RentItem;
import com.zfb.house.model.bean.SellItem;
import com.zfb.house.model.bean.UserBean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 工具类
 * Created by Administrator on 2016/5/19.
 */
public class ToolUtil {

    /**
     * 将选数组转化成字符串，中间用逗号分割
     *
     * @param strArray 要转化的数组
     * @return
     */
    public String convertArrayToStr(String[] strArray) {
        StringBuilder str = new StringBuilder();
        if (strArray != null && strArray.length > 0) {
            for (int i = 0; i < strArray.length; i++) {
                if (i < strArray.length - 1) {
                    str.append(strArray[i] + ",");
                } else {
                    str.append(strArray[i]);
                }
            }
        }
        return str.toString();
    }

    /**
     * 获得下拉选项中label这个值
     *
     * @param list
     * @return
     */
    public static String[] getReleasePullData(List<ReleasePullItem> list) {
        if (list == null) {
            return null;
        }
        int size = list.size();
        String[] arrays = new String[size];
        for (int i = 0; i < size; i++) {
            arrays[i] = list.get(i).getLabel();
        }
        return arrays;
    }

    /**
     * 根据value从List<ReleasePullItem>中得到Label
     *
     * @param value value值
     * @param list  要查找的列表
     * @return
     */
    public static String convertValueToLabel(String value, List<ReleasePullItem> list) {
        String label = "";
        if (!ParamUtils.isEmpty(list)) {
            for (ReleasePullItem item : list) {
                if (item.getValue().equals(value)) {
                    label = item.getLabel();
                }
            }
        }
        return label;
    }

    /**
     * 区域中的逗号转化为顿号
     *
     * @param s
     * @return
     */
    public static String convertDot(String s) {
        String converted = "";
        if (!ParamUtils.isEmpty(s)) {
            if (s.contains(",")) {
                converted = s.replace(",", "、");
                if (converted.endsWith("、")) {
                    converted = converted.substring(0, converted.length() - 1);
                }
            }
        }
        return converted;
    }

    /**
     * 设置房子的样式
     *
     * @param str 服务器返回的数据
     * @return
     */
    public static String setHouseLayout(String str) {
        int one = 0, two = 0, three = 0, four = 0;
        int num = 0;  //代表4个数中的第几个
        char arr[] = str.toCharArray();
        for (int i = 0; i < str.length(); i++) {
            if (',' == arr[i]) {
                num++;
                if (num == 1 && i != 0 && arr[i - 1] != ',' && Character.isDigit(arr[i - 1])) {
                    one = Integer.parseInt(String.valueOf(arr[i - 1]));
                    continue;
                }
                if (num == 2 && arr[i - 1] != ',' && Character.isDigit(arr[i - 1])) {
                    two = Integer.parseInt(String.valueOf(arr[i - 1]));
                    continue;
                }
                if (num == 3 && arr[i - 1] != ',' && Character.isDigit(arr[i - 1])) {
                    three = Integer.parseInt(String.valueOf(arr[i - 1]));
                    continue;
                }
                if (num == 4 && arr[i - 1] != ',' && Character.isDigit(arr[i - 1])) {
                    four = Integer.parseInt(String.valueOf(arr[i - 1]));
                }
            }
        }
        return (two > 0 ? two + "厅" : "") + (one > 0 ? one + "室" : "") + (three > 0 ? three + "卫" : "") + (four > 0 ? four + "阳" : "");
    }

    /**
     * 获取需求类型
     *
     * @param needType
     * @return
     */
    public static String getNeedType(String needType) {
        String type;
        switch (needType) {
            case "1":
                type = "出售";
                break;
            case "2":
                type = "出租";
                break;
            case "3":
                type = "租售";
                break;
            default:
                type = "";
        }
        return type;
    }

    /**
     * 获取佣金
     *
     * @param s
     * @return
     */
    public static String getBrokerage(String s) {
        String brokerage = "";
        if (s.equals("mt")) {
            brokerage = "面谈";
        } else {
            try {
                if (Float.valueOf(s) > 5) {
                    brokerage = s + "天";
                } else {
                    brokerage = s + "%";
                }
            } catch (Exception e) {
                e.printStackTrace();
                brokerage = "面谈";
            }
        }
        return brokerage;
    }

    /**
     * 反射调用swipeRefresh刷新
     *
     * @param refreshLayout
     * @param refreshing
     * @param notify
     */
    public static void setRefreshing(SwipeRefreshLayout refreshLayout, boolean refreshing, boolean notify) {
        Class<? extends SwipeRefreshLayout> refreshLayoutClass = refreshLayout.getClass();
        if (refreshLayoutClass != null) {
            try {
                Method setRefreshing = refreshLayoutClass.getDeclaredMethod("setRefreshing", boolean.class, boolean.class);
                setRefreshing.setAccessible(true);
                setRefreshing.invoke(refreshLayout, refreshing, notify);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置专家类型
     *
     * @param type
     * @return
     */
    public static String setExpertType(String type) {
        String expertType = "";
        switch (type) {
            case "0"://无
                expertType = "";
                break;
            case "1"://售房专家
                expertType = "售";
                break;
            case "2"://租房专家
                expertType = "租";
                break;
            case "3"://租售专家
                expertType = "租/售";
                break;
        }
        return expertType;
    }

    /**
     * 个人资料中设置专家类型
     *
     * @param type
     * @param isPersonal
     * @return
     */
    public static String setExpertType(String type, boolean isPersonal) {
        if (isPersonal) {
            String expertType = "";
            switch (type) {
                case "0"://无
                    expertType = "";
                    break;
                case "1"://售房专家
                    expertType = "售房专家";
                    break;
                case "2"://租房专家
                    expertType = "租房专家";
                    break;
                case "3"://租售专家
                    expertType = "租售专家";
                    break;
            }
            return expertType;
        }
        return "";
    }

    /**
     * 设置经纪人三种认证的值，包括实名认证、资质认证、名片认证
     *
     * @param type      认证类型
     * @param textValue 要设置的控件
     */
    public static void setAuthentication(String type, TextView textValue, Resources resources) {
        switch (type) {
            case "0"://未提交
                textValue.setText("未认证");
                break;
            case "1"://等待审核
                textValue.setText("等待审核");
                break;
            case "2"://审核通过
                textValue.setText("已认证");
                textValue.setTextColor(resources.getColor(R.color.my_orange_two));
                break;
            case "3"://审核不通过
                textValue.setText("审核不通过");
                break;
            default:
                textValue.setText("未认证");
        }
    }

    /**
     * 设置出售的字段
     * <p/>
     * 住宅：别墅：layout
     * 写字楼：officeTypeName
     * 商铺：shopTypeName
     *
     * @return
     */
    public static String setSellType(SellItem model) {
        String result = "";
        if (!ParamUtils.isEmpty(model.getResourceType())) {
            switch (model.getResourceType()) {
                case "1"://住宅
                case "2"://别墅
                    result = ToolUtil.setHouseLayout(model.getLayout());
                    break;
                case "3"://写字楼
                    result = model.getOfficeTypeName();
                    break;
                case "4"://商铺
                    result = model.getShopTypeName();
                    break;
            }
        }
        return result;
    }

    /**
     * 设置出租的字段
     * <p/>
     * 住宅：别墅：layout
     * 写字楼：officeTypeName
     * 商铺：shopTypeName
     *
     * @return
     */
    public static String setRentType(RentItem model) {
        String result = "";
        if (!ParamUtils.isEmpty(model.getResourceType())) {
            switch (model.getResourceType()) {
                case "1"://住宅
                case "2"://别墅
                    result = ToolUtil.setHouseLayout(model.getLayout());
                    break;
                case "3"://写字楼
                    result = model.getOfficeTypeName();
                    break;
                case "4"://商铺
                    result = model.getShopTypeName();
                    break;
            }
        }
        return result;
    }

    public static void updatePoint(Context context, int totalPoint, int getPoint) {
        updatePoint(context, totalPoint, getPoint, 0);
    }

    public static void updatePoint(Context context, int totalPoint, int getPoint, int msgId) {
        if (getPoint != 0) {
            UserBean.getInstance(context).point = totalPoint;
            UserBean.updateUserBean(context);
            EventUtil.sendEvent(new UpdatePointEvent(totalPoint));
            EventBus.getDefault().post(new ToastEvent("+" + getPoint, "1"));
        } else {
            if (msgId != 0) {
                EventBus.getDefault().post(new ToastEventRes(msgId));
            }
        }
    }


}
