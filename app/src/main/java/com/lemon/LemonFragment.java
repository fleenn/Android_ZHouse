package com.lemon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.emchat.Constant;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.bean.BeanFactory;
import com.lemon.config.Config;
import com.lemon.event.AnonLoginEvent;
import com.lemon.model.BaseResult;
import com.lemon.net.ApiManager;
import com.lemon.util.EventUtil;
import com.lemon.util.GlideUtil;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.ui.BrokerPersonalActivity;
import com.zfb.house.ui.UserPersonalActivity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 项目名称:  [Lemon]
 * 包:        [com.lemon]
 * 类描述:    [简要描述]
 * 创建人:    [xflu]
 * 创建时间:  [2016/1/21 15:12]
 * 修改人:    [xflu]
 * 修改时间:  [2016/1/21 15:12]
 * 修改备注:  [说明本次修改内容]
 * 版本:      [v1.0]
 */
public abstract class LemonFragment extends android.support.v4.app.Fragment {

    protected LayoutInflater inflater;
    protected View rootView;
    protected int layout;

    protected LemonDaoManager daoManager;
    protected LemonCacheManager cacheManager;
    protected ApiManager apiManager;
    protected android.support.v4.app.Fragment mFragment;
    protected LemonMessage lemonMessage;
    protected String title;

    protected TextView mTvTitleLt;
    protected RelativeLayout mRlayoutTittleLtImg;
    protected ImageView mImgTitleLt;
    protected TextView mTvTitleCenter;
    protected TextView mTvTitleRt;
    protected RelativeLayout mRlayoutTitleRtImg;
    protected ImageView mImgTitleRt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragment = this;
        this.inflater = inflater;
        injectLayout();
        setLayout();
        rootView = inflater.inflate(layout, container, false);
        mTvTitleLt = (TextView) rootView.findViewById(R.id.tv_title_lt);
        mRlayoutTittleLtImg = (RelativeLayout) rootView.findViewById(R.id.rlayout_tittle_lt_img);
        mImgTitleLt = (ImageView) rootView.findViewById(R.id.img_title_lt);
        mTvTitleCenter = (TextView) rootView.findViewById(R.id.tv_title_center);
        mTvTitleRt = (TextView) rootView.findViewById(R.id.tv_title_rt);
        mRlayoutTitleRtImg = (RelativeLayout) rootView.findViewById(R.id.rlayout_title_rt_img);
        mImgTitleRt = (ImageView) rootView.findViewById(R.id.img_title_rt);
        injectView();
        injectEvent();
        parentInit();
        initView();
        initData();
        init();
        return rootView;
    }

    private void parentInit() {

        cacheManager = BeanFactory.getInstance().getBean(LemonCacheManager.class);
        apiManager = BeanFactory.getInstance().getBean(ApiManager.class);
        daoManager = BeanFactory.getInstance().getBean(LemonDaoManager.class);
        lemonMessage = LemonContext.getBean(LemonMessage.class);
    }

    /**
     * 设置对应的layout
     */
    protected void setLayout() {
    }

    protected void initView() {
    }

    protected void initData() {
    }

    protected void init() {
    }

    protected void setTitle(String title){
        this.title = title;
    }

    protected void setTitle(int resId){
        this.title = getResources().getString(resId);
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
                continue;
            }

            try {
                field.setAccessible(true);
                if(view.parent() != 0){
                    field.set(this, rootView.findViewById(view.parent()).findViewById(view.id()));
                }else{
                    field.set(this, rootView.findViewById(view.id()));
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

            View view = rootView.findViewById(onClick.id());
            if (ParamUtils.isNull(view)) {
                continue;
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (!onClick.anonymonus() && ParamUtils.isEmpty(SettingUtils.get(((Context) LemonContext.getBean("mContext")), "token", ""))) {
                            EventBus.getDefault().post(new AnonLoginEvent());
                            return;
                        }
                        method.invoke(mFragment);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            notificationMessage(msg);
        }
    };

    public void notificationMessage(Message msg) {

    }

    public void onEventMainThread(BaseResult event) {

    }

    protected final void setLtText(int resId){
        mTvTitleLt.setVisibility(View.VISIBLE);
        mTvTitleLt.setText(resId);
        mRlayoutTittleLtImg.setVisibility(View.GONE);
    }

    protected final void setLtTextColor(int resId){
        mTvTitleLt.setTextColor(resId);
    }

    protected final void setCenterText(int resId){
        mTvTitleCenter.setVisibility(View.VISIBLE);
        mTvTitleCenter.setText(resId);
    }

    protected final void setRtText(int resId){
        mTvTitleRt.setVisibility(View.VISIBLE);
        mTvTitleRt.setText(resId);
    }

    protected final void setRtTextColor(int resId){
        mTvTitleRt.setTextColor(resId);
    }

    protected final void setRtImg(String imgUrl){
        mTvTitleRt.setVisibility(View.GONE);
        mRlayoutTitleRtImg.setVisibility(View.VISIBLE);
        GlideUtil.getInstance().loadUrl(getContext(), imgUrl, mImgTitleRt);
    }

    protected final void setRtImg(int resId){
        mTvTitleRt.setVisibility(View.GONE);
        mRlayoutTitleRtImg.setVisibility(View.VISIBLE);
        mImgTitleRt.setImageResource(resId);
    }

    protected final void setLtImg(String imgUrl){
        mTvTitleLt.setVisibility(View.GONE);
        mRlayoutTittleLtImg.setVisibility(View.VISIBLE);
        GlideUtil.getInstance().loadUrl(getContext(), imgUrl, mImgTitleLt);
    }

    protected final void setLtImg(int resId){
        mTvTitleLt.setVisibility(View.GONE);
        mRlayoutTittleLtImg.setVisibility(View.VISIBLE);
        mImgTitleLt.setImageResource(resId);
    }

    protected void toast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public String getTitle(){
        return title;
    }

    public void toDetail(String type, String id, String remark) {
        detail(type, id, remark, 0);
    }

    public void toDetail(String type, String id, String remark, int position) {
        detail(type, id, remark, position);
    }

    public void detail(String type, String id, String remark, int position) {
        boolean isTokenEmpty = ParamUtils.isEmpty(SettingUtils.get(getActivity(), "token", ""));
        if (type.equals("1")) {//经纪人
            //好友经纪人ID的集合
            Intent intent = new Intent(getActivity(), BrokerPersonalActivity.class);
            intent.putExtra("brokerId", id);
            if (ParamUtils.isNull(cacheManager.getBean(Config.getValue("brokerFriends")))){
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
            Intent intent = new Intent(getActivity(), UserPersonalActivity.class);
            intent.putExtra("userId", id);
            if (ParamUtils.isNull(cacheManager.getBean(Config.getValue("brokerFriends")))){
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


    @Override
    public void onStart() {
        super.onStart();
        EventUtil.register(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventUtil.unregister(this);
        System.gc();
    }
}
