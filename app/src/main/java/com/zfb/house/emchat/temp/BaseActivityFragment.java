package com.zfb.house.emchat.temp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lemon.util.SettingUtils;
import com.zfb.house.R;

/**
 * Class Function Instructions:基础fragment
 *
 * @Version 1.0.0
 * @Author Silence
 */
public abstract class BaseActivityFragment extends Fragment {

    protected BaseFragmentsActivity activity;

    /**
     * 窗口的整个界面view
     */
    protected View view;

    /**
     * 标题界面 view
     */
    protected RelativeLayout titleContentView;

    /**
     * 进度加载视图
     */
    private ProgressBar mProgressBar;

    /**
     * 具体的子界面view
     */
    protected View subContentView;


    protected LayoutInflater inflater;

    /**
     * 顶部布局标题
     */
    TextView titleTextView;

    TextView rightBtn;

    View loadMoreView;
    LinearLayout loadmore_list_footer_layout_progressLL;
    TextView loadmore_list_footer_layout_btn;


    @Override
    public final View onCreateView(LayoutInflater inflater,
                                   ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        View view = inflater.inflate(getContainerLayoutId(), container, false);
        setView(view);
        findBaseViews();
        findViews();
        setListensers();
        initFragmentData();
        return view;
    }


    public void setBaseActivity(BaseFragmentsActivity baseActivity) {
        this.activity = baseActivity;
    }


    /**
     * 寻找View
     *
     * @param id
     * @return
     */
    protected View findViewById(int id) {
        return view.findViewById(id);
    }


    private final int getContainerLayoutId() {
        return R.layout.base_activity_fragment;
    }

    private void setView(View view) {
        this.view = view;
    }


    /**
     * 将标题布局添加到界面顶部
     */
    private void findBaseViews() {
        titleContentView = (RelativeLayout) findViewById(R.id.rlTitle);
        mProgressBar = (ProgressBar) findViewById(R.id.base_fragment_progress);
        int subLayoutId = getLayoutId();
        ViewStub contentStub = (ViewStub) findViewById(R.id.content_stub);
        contentStub.setLayoutResource(subLayoutId);
        subContentView = contentStub.inflate();
        View titleView = initTitleView();
        if (titleView != null) {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            titleContentView.addView(titleView, lp);
        }
        loadMoreView = inflater.inflate(R.layout.loadmore_list_footer_layout, null);
        LinearLayout.LayoutParams llp =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        llp.gravity = Gravity.CENTER;
        loadMoreView.setLayoutParams(llp);
        loadmore_list_footer_layout_progressLL = (LinearLayout) loadMoreView.findViewById(R.id.loadmore_list_footer_layout_progressLL);
        loadmore_list_footer_layout_btn = (TextView) loadMoreView.findViewById(R.id.loadmore_list_footer_layout_btn);
        loadmore_list_footer_layout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideLoadMore();
                loadMoreData();
            }
        });
    }

    /*********************************继承子类需要实现方法***********************************************/


    /**
     * 界面控件初始化
     */
    protected abstract void findViews();

    /**
     * 界面控件点击事件
     */
    protected abstract void setListensers();

    /**
     * 界面布局文件ID
     *
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 页面顶部布局
     *
     * @return
     */
    protected abstract View initTitleView();

    /**
     * 页面数据初始化
     */
    public abstract void initFragmentData();

    /***************************公共标题******************************************/
    /**
     * 设置默认标题栏，返回键+文字
     *
     * @param onBackClickListener back按钮事件
     */
    protected View initTitleWithBackBtn(int titleRes,
                                        View.OnClickListener onBackClickListener) {
        return initTitleWithBackBtn(getActivity().getResources().getString(titleRes),
                onBackClickListener);
    }

    ;

    /**
     * 设置默认标题栏，返回键+文字
     *
     * @param onBackClickListener back按钮事件
     */
    protected View initTitleWithBackBtn(String title,
                                        View.OnClickListener onBackClickListener) {
        View titleView = inflater.inflate(R.layout.title_with_back, null);
        View backLayout = titleView.findViewById(R.id.back_layout);
        if (onBackClickListener != null) {
            backLayout.setOnClickListener(onBackClickListener);
        }
        titleTextView = (TextView) titleView
                .findViewById(R.id.title_tv);
        titleTextView.setText(title);
        return titleView;
    }

    ;

    /**
     * 设置默认标题栏，返回键+文字 点击返回键关闭该界面
     *
     * @param
     */
    protected View initTitleWithBackBtn(int titleTextResId) {
        return initTitleWithBackBtn(getActivity().getResources().getString(
                titleTextResId));
    }

    ;

    /**
     * 设置默认标题栏，返回键+文字 点击返回键关闭该界面
     *
     * @param
     */
    protected View initTitleWithBackBtn(String title) {
        View titleView = inflater.inflate(R.layout.title_with_back, null);
        View backLayout = titleView.findViewById(R.id.back_layout);
        titleTextView = (TextView) titleView
                .findViewById(R.id.title_tv);
        backLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });

        titleTextView.setText(title);
        return titleView;
    }

    ;

    /**
     * 设置默认标题栏，返回键+文字 点击返回键关闭该界面
     *
     * @param
     */
    protected View initTitleWithBackBtn(String title, String righttitle,
                                        View.OnClickListener listener) {
        View titleView = inflater.inflate(R.layout.title_with_back, null);
        View backLayout = titleView.findViewById(R.id.back_layout);
        titleTextView = (TextView) titleView
                .findViewById(R.id.title_tv);
        backLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });

        titleTextView.setText(title);

        rightBtn = (TextView) titleView.findViewById(R.id.titleRightBtn);
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setText(righttitle);
        rightBtn.setOnClickListener(listener);
        return titleView;
    }

    /**
     * 设置右边按钮是否可用
     *
     * @param enabled
     */
    protected void setRightBtnEnable(boolean enabled) {
        if (rightBtn != null) {
            rightBtn.setEnabled(enabled);
        }
    }


    /**
     * 设置标题名称
     *
     * @param title
     */
    public final void setTitle(String title) {
        titleTextView.setText(title);
    }

    public final void setTitle(int titleResId) {
        setTitle(getActivity().getResources().getString(titleResId));
    }

/******************************数据请求相应********************************************************/
    /**
     * 响应页面变化的方法
     */
    protected abstract void response(int actionId, Object result);

    /* 发消息至主线程 */
    public final void responseAction(final int actionId,
                                     final Object result) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                response(actionId, result);
            }
        });
    }

    /**
     * 设置引导页--设置引导页的id，并且将对应的fragmen页面的名字放入
     */
    protected void setGuidePage(int drawableId, final String fragmentTag) {
        boolean ifFirst = SettingUtils.get(getActivity(),fragmentTag, true);
        if (ifFirst) {
            ((ImageView) findViewById(R.id.img_guide)).setVisibility(View.VISIBLE);
            ((ImageView) findViewById(R.id.img_guide)).setBackgroundResource(drawableId);
            ((ImageView) findViewById(R.id.img_guide)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ImageView) findViewById(R.id.img_guide)).setVisibility(View.GONE);
                    SettingUtils.set(getActivity(),fragmentTag, false);
                }
            });
        }

    }
    /******************************加载进度模块********************************************************************************/
    /**
     * 显示进度加载
     */
    protected void showProgress() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
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
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    /**
     * 展示加载更多
     */
    protected void showLoadMore() {
        loadmore_list_footer_layout_progressLL.setVisibility(View.GONE);
        loadmore_list_footer_layout_btn.setVisibility(View.VISIBLE);
    }

    /**
     * 点击加载更多
     */
    protected void hideLoadMore() {
        loadmore_list_footer_layout_progressLL.setVisibility(View.VISIBLE);
        loadmore_list_footer_layout_btn.setVisibility(View.GONE);
    }


    /**
     * 点击列表底部加载更多，需要子类实现
     */
    protected void loadMoreData() {
    }


}
