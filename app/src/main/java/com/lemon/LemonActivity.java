package com.lemon;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.emchat.Constant;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.config.Config;
import com.lemon.event.ActivityEvent;
import com.lemon.event.AnonLoginEvent;
import com.lemon.event.StartLocationEvent;
import com.lemon.model.BaseResult;
import com.lemon.net.ApiManager;
import com.lemon.util.EventUtil;
import com.lemon.util.GlideUtil;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.model.bean.ReleasePull;
import com.zfb.house.model.bean.ReleasePullItem;
import com.zfb.house.ui.BrokerPersonalActivity;
import com.zfb.house.ui.UserPersonalActivity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 项目名称:  [Lemon]
 * 包:        [com.lemon.ui]
 * 类描述:    [类描述]
 * 创建人:    [XiaoFeng]
 * 创建时间:  [2015/12/21 20:34]
 * 修改人:    [XiaoFeng]
 * 修改时间:  [2015/12/21 20:34]
 * 修改备注:  [说明本次修改内容]
 * 版本:      [v1.0]
 */
public abstract class LemonActivity extends FragmentActivity {

    protected int layout;
    protected Context mContext;

    protected LemonCacheManager cacheManager;
    protected ApiManager apiManager;
    protected LemonDaoManager daoManager;
    protected LemonMessage lemonMessage;

    protected TextView mTvTitleLt;
    protected RelativeLayout mRlayoutTittleLtImg;
    protected ImageView mImgTitleLt;
    protected TextView mTvTitleCenter;
    protected TextView mTvTitleRt;
    protected RelativeLayout mRlayoutTitleRtImg;
    protected ImageView mImgTitleRt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        injectLayout();
        setLayout();
        setContentView(layout);
        mTvTitleLt = (TextView) findViewById(R.id.tv_title_lt);
        mRlayoutTittleLtImg = (RelativeLayout) findViewById(R.id.rlayout_tittle_lt_img);
        mImgTitleLt = (ImageView) findViewById(R.id.img_title_lt);
        mTvTitleCenter = (TextView) findViewById(R.id.tv_title_center);
        mTvTitleRt = (TextView) findViewById(R.id.tv_title_rt);
        mRlayoutTitleRtImg = (RelativeLayout) findViewById(R.id.rlayout_title_rt_img);
        mImgTitleRt = (ImageView) findViewById(R.id.img_title_rt);
        injectView();
        injectEvent();
        try {
            parentInit();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        initView();
        initData();
        init();
    }

    public <T> T findControl(int id) {
        return (T) findViewById(id);
    }

    /**
     * 设置对应的layout
     */
    protected void setLayout() {
    }

    protected void parentInit() throws InstantiationException, IllegalAccessException {
        mContext = this;
        EventBus.getDefault().post(new ActivityEvent(this));
        cacheManager = LemonContext.getBean(LemonCacheManager.class);
        apiManager = LemonContext.getBean(ApiManager.class);
        daoManager = LemonContext.getBean(LemonDaoManager.class);
        lemonMessage = LemonContext.getBean(LemonMessage.class);
    }

    private final void injectLayout() {
        //反射初始化布局
        Layout mLayout = getClass().getAnnotation(Layout.class);
        if (ParamUtils.isNull(mLayout)) {
            return;
        }

        layout = mLayout.id();
    }

    private final void injectView() {
        //反射初始化视图
        Field[] fields = getClass().getDeclaredFields();
        if (ParamUtils.isEmpty(fields)) {
            return;
        }
        for (Field field : fields) {
            FieldView view = field.getAnnotation(FieldView.class);
            if (ParamUtils.isNull(view)) {
                continue;//这个原来是return，此处改为continue
            }

            try {
                field.setAccessible(true);
                if (view.parent() != 0) {
                    field.set(this, findViewById(view.parent()).findViewById(view.id()));
                } else {
                    field.set(this, findViewById(view.id()));

                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private final void injectEvent() {
        //反射初始化事件
        Method[] methods = getClass().getDeclaredMethods();
        if (ParamUtils.isEmpty(methods)) {
            return;
        }


        for (final Method method : methods) {
            final OnClick onClick = method.getAnnotation(OnClick.class);
            if (ParamUtils.isNull(onClick)) {
                continue;
            }

            View view = findViewById(onClick.id());
            if (ParamUtils.isNull(view)) {
                continue;
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (!onClick.anonymonus() && ParamUtils.isEmpty(SettingUtils.get(mContext, "token", ""))) {
                            EventUtil.sendEvent(new AnonLoginEvent());
                            return;
                        }
                        method.invoke(mContext);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    protected void before() {
    }

    protected void initView() {
    }

    protected void initData() {
    }

    protected void init() {
    }

    public void onEventMainThread(BaseResult result) {

    }

    protected final void setLtText(int resId) {
        mTvTitleLt.setVisibility(View.VISIBLE);
        mTvTitleLt.setText(resId);
        mRlayoutTittleLtImg.setVisibility(View.GONE);
    }

    protected final void setLtTextColor(int resId) {
        Resources resources = getResources();
        int color = resources.getColor(resId);
        mTvTitleLt.setTextColor(color);
    }

    protected final void setCenterText(int resId) {
        mTvTitleCenter.setVisibility(View.VISIBLE);
        mTvTitleCenter.setText(resId);
    }

    protected final void setCenterText(String s) {
        mTvTitleCenter.setVisibility(View.VISIBLE);
        mTvTitleCenter.setText(s);
    }

    protected final void setRtText(int resId) {
        mTvTitleRt.setVisibility(View.VISIBLE);
        mTvTitleRt.setText(resId);
    }

    protected final void setRtTextColor(int resId) {
        Resources resources = getResources();
        int color = resources.getColor(resId);
        mTvTitleRt.setTextColor(color);
    }

    protected final void setRtImg(String imgUrl) {
        mTvTitleRt.setVisibility(View.GONE);
        mRlayoutTitleRtImg.setVisibility(View.VISIBLE);
        GlideUtil.getInstance().loadUrl(mContext, imgUrl, mImgTitleRt);
    }

    protected final void setRtImg(int resId) {
        mTvTitleRt.setVisibility(View.GONE);
        mRlayoutTitleRtImg.setVisibility(View.VISIBLE);
        mImgTitleRt.setImageResource(resId);
    }

    protected final void setLtImg(String imgUrl) {
        mTvTitleLt.setVisibility(View.GONE);
        mRlayoutTittleLtImg.setVisibility(View.VISIBLE);
        GlideUtil.getInstance().loadUrl(mContext, imgUrl, mImgTitleLt);
    }

    protected final void setLtImg(int resId) {
        mTvTitleLt.setVisibility(View.GONE);
        mRlayoutTittleLtImg.setVisibility(View.VISIBLE);
        mImgTitleLt.setImageResource(resId);
    }

    public void backClick(View v) {
        finish();
    }

    protected void setLayoutValue(int layout) {
        this.layout = layout;
    }

    public void startNextActivity(Class<? extends Activity> nextActivity, boolean finishCurrent) {
        Intent intent = new Intent(this, nextActivity);
        startActivity(intent);
        if (finishCurrent) {
            finish();
        }
    }

    public void startNextActivity(Intent intent, boolean finishCurrent) {
        startActivity(intent);
        if (finishCurrent) {
            finish();
        }
    }

    protected String getIntentExtraStr(String name) {
        return getIntent().getStringExtra(name);
    }


    protected boolean getIntentExtraBoolean(String name) {
        return getIntent().getBooleanExtra(name, false);
    }

    protected int getIntentExtraInt(String name) {
        return getIntent().getIntExtra(name, -1);
    }

    protected String getEditTextValue(int id) {
        EditText tv = (EditText) findViewById(id);
        String value = tv.getText().toString();
        return value;
    }

    protected void setEditTextValue(int id, String text) {
        EditText tv = (EditText) findViewById(id);
        tv.setText(text);
    }

    protected void setTextViewValue(int id, String text) {
        TextView tv = (TextView) findViewById(id);
        tv.setText(text);
    }

    protected String getTextViewValue(int id) {
        TextView tv = (TextView) findViewById(id);
        String value = tv.getText().toString();
        return value;
    }

    protected boolean isEditTextEmpty(int id) {
        String value = getEditTextValue(id);
        return ParamUtils.isEmpty(value);
    }

    protected void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected void toast(int id) {
        Toast.makeText(this, mContext.getString(id), Toast.LENGTH_SHORT).show();
    }

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            notificationMessage(msg);
        }
    };

    public void notificationMessage(Message msg) {

    }

    public void toDetail(String type, String id, String remark) {
        detail(type, id, remark, 0);
    }

    public void toDetail(String type, String id, String remark, int position) {
        detail(type, id, remark, position);
    }

    /**
     * @param type     经纪人或者客户 0：客户；1：经纪人
     * @param id       用户id
     * @param remark   别名，无则传空值
     * @param position 在list中的位置，无则传0
     */
    public void detail(String type, String id, String remark, int position) {
        boolean isTokenEmpty = ParamUtils.isEmpty(SettingUtils.get(mContext, "token", ""));
        if (type.equals("1")) {//经纪人
            //好友经纪人ID的集合
            Intent intent = new Intent(this, BrokerPersonalActivity.class);
            intent.putExtra("brokerId", id);
            if (ParamUtils.isNull(cacheManager.getBean(Config.getValue("brokerFriends")))) {
                intent.putExtra("isBrokerFriend", false);
                startActivity(intent);
                return;
            }
            List<String> brokerFriends = (List<String>) cacheManager.getBean(Config.getValue("brokerFriends"));
            if (!isTokenEmpty && brokerFriends.contains(id)) {//好友
                intent.putExtra("isBrokerFriend", true);
                intent.putExtra("remark", remark);
                intent.putExtra("position", position);
                startActivityForResult(intent, Constant.REQUEST_MODIFY_REMARKS);
            } else {//非好友
                intent.putExtra("isBrokerFriend", false);
                startActivity(intent);
            }
        } else if (type.equals("0")) {//用户
            Intent intent = new Intent(this, UserPersonalActivity.class);
            intent.putExtra("userId", id);
            if (ParamUtils.isNull(cacheManager.getBean(Config.getValue("brokerFriends")))) {
                intent.putExtra("isUserFriend", false);
                startActivity(intent);
                return;
            }
            //好友用户ID的集合
            List<String> userFriends = (List<String>) cacheManager.getBean(Config.getValue("userFriends"));
            if (userFriends.contains(id)) {//好友
                intent.putExtra("isUserFriend", true);
                intent.putExtra("remark", remark);
                intent.putExtra("position", position);
                startActivityForResult(intent, Constant.REQUEST_MODIFY_REMARKS);
            } else {//非好友
                intent.putExtra("isUserFriend", false);
                startActivity(intent);
            }
        }
    }

    /**
     * 请求权限
     *
     * @param id         请求授权的id 唯一标识即可
     * @param permission 请求的权限
     */
    protected void checkPermission(int id, String... permission) {
        //版本判断
        if (Build.VERSION.SDK_INT >= 23) {
            //减少是否拥有权限
            List<String> denyList = checkPermissionDeny(permission);
            //弹出对话框接收权限
            if (denyList.size() != 0) {
                requestPermissions(denyList.toArray(new String[denyList.size()]), id);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private List<String> checkPermissionDeny(String... permission) {
        List<String> denyList = new ArrayList<>();
        for (String s : permission) {
            if (checkSelfPermission(s) == PackageManager.PERMISSION_DENIED) {
                denyList.add(s);
            }
        }
        return denyList;
    }

    //    隐藏软键盘
    public void hideKeyboard() {
        if (mContext != null) {
            if (this.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
                if (getCurrentFocus() != null) {
                    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
    }

    public String convertSPPTName(String value) {
        if (ParamUtils.isEmpty(value)) {
            return "";
        }
        ReleasePull pull = cacheManager.getBean(ReleasePull.class);
        if (ParamUtils.isNull(pull) || ParamUtils.isEmpty(value)) {
            return value;
        }

        String[] arrays = value.split(",");
        if (ParamUtils.isEmpty(arrays)) {
            return value;
        }

        String result = "";
        for (String item : arrays) {
            result += pull.getLabel(pull.getHOUSE_SPPT(), item) + "、";
        }
        if (result.endsWith("、")) {
            result = result.substring(0, result.length() - 1);
        }

        return result;
    }

    public String convertProvidesName(String value) {
        if (ParamUtils.isEmpty(value)) {
            return "";
        }
        ReleasePull pull = cacheManager.getBean(ReleasePull.class);
        if (ParamUtils.isNull(pull) || ParamUtils.isEmpty(value)) {
            return value;
        }

        String[] arrays = value.split(",");
        if (ParamUtils.isEmpty(arrays)) {
            return value;
        }

        String result = "";
        for (String item : arrays) {
            result += pull.getLabel(pull.getHOUSE_FYPT(), item) + "、";
        }
        if (result.endsWith("、")) {
            result = result.substring(0, result.length() - 1);
        }

        return result;
    }


    public String convertShopTagName(String value) {
        if (ParamUtils.isEmpty(value)) {
            return "";
        }
        ReleasePull pull = cacheManager.getBean(ReleasePull.class);
        if (ParamUtils.isNull(pull) || ParamUtils.isEmpty(value)) {
            return value;
        }

        String[] arrays = value.split(",");
        if (ParamUtils.isEmpty(arrays)) {
            return value;
        }

        String result = "";
        for (String item : arrays) {
            result += pull.getLabel(pull.getHOUSE_KJYLB(), item) + "、";
        }
        if (result.endsWith("、")) {
            result = result.substring(0, result.length() - 1);
        }

        return result;
    }

    public String convertSellTag(String value) {
        if (ParamUtils.isEmpty(value)) {
            return "";
        }
        ReleasePull pull = cacheManager.getBean(ReleasePull.class);
        if (ParamUtils.isNull(pull) || ParamUtils.isEmpty(value)) {
            return value;
        }

        String[] arrays = value.split(",");
        if (ParamUtils.isEmpty(arrays)) {
            return value;
        }

        String result = "";
        for (String item : arrays) {
            result += pull.getLabel(pull.getHOUSE_SELL(), item) + "、";
        }
        if (result.endsWith("、")) {
            result = result.substring(0, result.length() - 1);
        }

        return result;
    }

    public String convertDirectionNum(String value) {
        if (ParamUtils.isEmpty(value)) {
            return "";
        }
        ReleasePull pull = cacheManager.getBean(ReleasePull.class);
        if (ParamUtils.isNull(pull)) {
            return value;
        }

        List<ReleasePullItem> list = pull.getHOUSE_CX();
        for (ReleasePullItem item : list) {
            if (value.equals(item.getLabel())) {
                return item.getValue();
            }
        }
        return value;
    }

    public String convertRentWay(String value) {
        if (ParamUtils.isEmpty(value) || "0".equals(value)) {
            return "";
        }
        ReleasePull pull = cacheManager.getBean(ReleasePull.class);
        if (ParamUtils.isNull(pull)) {
            return value;
        }

        List<ReleasePullItem> list = pull.getHOUSE_CZFS();
        for (ReleasePullItem item : list) {
            if (value.equals(item.getValue())) {
                return item.getLabel();
            }
        }
        return value;
    }

    public String convertPayment(String value) {
        if (ParamUtils.isEmpty(value)) {
            return "";
        }
        ReleasePull pull = cacheManager.getBean(ReleasePull.class);
        if (ParamUtils.isNull(pull)) {
            return value;
        }

        List<ReleasePullItem> list = pull.getHOUSE_ZFFS();
        for (ReleasePullItem item : list) {
            if (value.equals(item.getValue())) {
                return item.getLabel();
            }
        }
        return value;
    }

    public String convertDirectionName(String value) {
        if (ParamUtils.isEmpty(value)) {
            return "";
        }
        ReleasePull pull = cacheManager.getBean(ReleasePull.class);
        if (ParamUtils.isNull(pull)) {
            return value;
        }

        List<ReleasePullItem> list = pull.getHOUSE_CX();
        for (ReleasePullItem item : list) {
            if (value.equals(item.getValue())) {
                return item.getLabel();
            }
        }
        return value;
    }

    public String convertPaymentTypeName(String value) {
        if (ParamUtils.isEmpty(value)) {
            return "";
        }
        ReleasePull pull = cacheManager.getBean(ReleasePull.class);
        if (ParamUtils.isNull(pull)) {
            return value;
        }

        List<ReleasePullItem> list = pull.getHOUSE_XZLZFFS();
        for (ReleasePullItem item : list) {
            if (value.equals(item.getValue())) {
                return item.getLabel();
            }
        }
        return value;
    }

    public String convertPropertyRightName(String value) {
        if (ParamUtils.isEmpty(value)) {
            return "";
        }
        ReleasePull pull = cacheManager.getBean(ReleasePull.class);
        if (ParamUtils.isNull(pull)) {
            return value;
        }

        List<ReleasePullItem> list = pull.getHOUSE_CQXZ();
        for (ReleasePullItem item : list) {
            if (value.equals(item.getValue())) {
                return item.getLabel();
            }
        }
        return value;
    }

    /**
     * 获取位置
     */
    protected void getLocation() {
        EventBus.getDefault().post(new StartLocationEvent());
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventUtil.register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        System.gc();
        EventUtil.unregister(this);
    }

    @Override
    public void finish() {
        LemonContext.getBean(LemonActivityManager.class).removeCurrentActivity();
        super.finish();
    }

    /**
     * 不根据系统字体大小的改变而改变app内字体大小
     *
     * @return
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }
}
