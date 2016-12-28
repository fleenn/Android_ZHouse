package com.zfb.house.emchat.temp;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.lemon.util.LogUtils;
import com.zfb.house.R;


/**
 * Created by geolo123 on 2015/7/21.
 */
public abstract class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment.java";
    private Handler mHandler = new Handler();
    /**
     * 标题栏
     */
    public TitleView mTitleView;
    /**
     * 内容显示区
     */
    private ViewGroup mContentLayout;
    /**
     * 进度加载视图
     */
    private ProgressBar mProgressBar;
    private View mProgressLayout;

    private TitleTopLeftClickListener mTitleTopLeftClickListener;
    private TitleTopRightClickListener mTitleTopRightClickListener;
    private TitleTopCenterClickListener mTitleTopCenterClickListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mTitleTopLeftClickListener = new TitleTopLeftClickListener();
        mTitleTopRightClickListener = new TitleTopRightClickListener();
        mTitleTopCenterClickListener = new TitleTopCenterClickListener();

        View view = inflater.inflate(R.layout.base_fragment, container, false);
        mTitleView = (TitleView) view.findViewById(R.id.base_fragment_title);
        mContentLayout = (ViewGroup) view.findViewById(R.id.base_fragment_content);
        mProgressLayout = view.findViewById(R.id.base_fragment_progress_layout);
        mProgressBar = (ProgressBar) view.findViewById(R.id.base_fragment_progress);

        mTitleView.setLeftIcon(R.drawable.title_top_back_icon_selector);
        mTitleView.setLeftClickListener(mTitleTopLeftClickListener);
        mTitleView.setRightClickListener(mTitleTopRightClickListener);
        mTitleView.setCenterClickListener(mTitleTopCenterClickListener);
        mContentLayout.addView(onCreateView(inflater, savedInstanceState));
        return view;
    }

    public void setBackgroundColor(int color) {
        if(mTitleView!=null){
        mTitleView.setBackgroundColor(color);
        }
    }

    /**
     * 获取标题栏输入框控件
     */
    public EditText getTitleCenterET() {
        if (mTitleView != null) {
            return mTitleView.getCenterET();
        }
        return null;
    }

    /**
     * 设置标题栏左边的按钮图标
     */
    protected final void setTitleLeftIcon(int resId) {
        if (mTitleView != null) {
            mTitleView.setLeftIcon(resId);
        }
    }

    /**
     * 设置标题栏中间的文字内容
     */
    protected final void setTitleTV(CharSequence text) {
        if (mTitleView != null) {
            mTitleView.setTitleTV(text);
        }
    }

    /**
     * 设置标题栏中间的文字颜色
     */
    public void setTitleColor(int color) {
        if (mTitleView != null) {
            mTitleView.setTitleColor(color);
        }
    }

    /**
     * 设置虚拟的搜索布局的切图
     */
    protected final void setTitleCenterImage(int drawbleId) {
        if (mTitleView != null) {
            mTitleView.setTitleCenterImage(drawbleId);
        }
    }

    /**
     * 设置标题栏右边的文字内容
     */
    protected final void setTitleRightTV(CharSequence text) {
        if (mTitleView != null) {
            mTitleView.setRightTV(text);
        }
    }

    /**
     * 设置标题栏右边的文字颜色
     */
    protected final void setRightColorTV(int color) {
        if (mTitleView != null) {
            mTitleView.setRightColorTV(color);
        }
    }


    /**
     * 设置标题栏左边的按钮图标
     */
    protected final void setTitleRightIcon(int resId) {
        LogUtils.v(TAG, "--- setTitleRightIcon --- ");
        if (mTitleView != null) {
            mTitleView.setRightIcon(resId);
        }
    }

    /**
     * 隐藏标题栏右边的内容
     */
    protected final void hideTitleRightTV() {
        if (mTitleView != null) {
            mTitleView.hideRightTV();
        }
    }

    /**
     * 隐藏标题栏
     */
    protected final void hideTitleTop() {
        if (mTitleView != null) {
            mTitleView.setVisibility(View.GONE);
        }
    }

    /**
     * 显示进度加载
     */
    protected void showProgress() {
        if (mProgressLayout != null) {
            mProgressLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 显示进度加载
     *
     * @param delayMillis 自动关闭的延时
     */
    protected void showProgressDelayed(long delayMillis) {
        showProgress();
        mProgressBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgress();
            }
        }, delayMillis);
    }

    /**
     * 隐藏进度加载
     */
    protected void hideProgress() {
        if (mProgressLayout != null) {
            mProgressLayout.setVisibility(View.GONE);
        }
    }

    /***************************************************************************************/
    private class TitleTopLeftClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            onTitleTopLeftClick();
        }
    }

    private class TitleTopCenterClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            onTitleTopCenterClick();
        }
    }

    private class TitleTopRightClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            onTitleTopRightClick();
        }
    }

    /*********************************************************************************************/
    public abstract View onCreateView(LayoutInflater inflater, Bundle savedInstanceState);

    /**
     * 标题栏，左边按钮的点击事件
     */
    public abstract void onTitleTopLeftClick();

    /**
     * 标题栏，中间的点击事件
     */
    public abstract void onTitleTopCenterClick();

    /**
     * 标题栏，右边按钮的点击事件
     */
    public abstract void onTitleTopRightClick();

    /**
     * 所有继承BackHandledFragment的子类都将在这个方法中实现物理Back键按下后的逻辑
     * FragmentActivity捕捉到物理返回键点击事件后会首先询问Fragment是否消费该事件
     * 如果没有Fragment消息时FragmentActivity自己才会消费该事件
     */
    public abstract boolean onBackPressed();
}
