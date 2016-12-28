package com.zfb.house.ui;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;

import com.android.emchat.Constant;
import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.model.StatusCode;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.BrokersDetailAdapter;
import com.zfb.house.component.LoadMoreRecyclerView;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.model.param.ConcernPeopleParam;
import com.zfb.house.model.param.DeleteConcernParam;
import com.zfb.house.model.result.ConcernPeopleResult;
import com.zfb.house.model.result.DeleteConcernResult;
import com.zfb.house.util.ToolUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户：我的->关注的专家
 * Created by Administrator on 2016/5/6.
 */
@Layout(id = R.layout.activity_concern_expert)
public class ConcernExpertActivity extends LemonActivity {

    private static final String TAG = "UserExpert";
    //页数
    public int mPageNo = 1;

    //自定义RecyclerView
    @FieldView(id = R.id.recycler_user_expert)
    private LoadMoreRecyclerView recyclerUserExpert;
    //下拉刷新
    @FieldView(id = R.id.refresh_user_expert)
    private SwipeRefreshLayout refreshUserExpert;

    //查询“关注的专家”请求参数
    private ConcernPeopleParam concernPeopleParam;
    //关注的专家的适配器
    private BrokersDetailAdapter adapter;
    //关注的专家的数据
    private List<UserBean> dataList = new ArrayList<>();
    //判断是否要进行编辑状态
    private boolean isEdit = false;
    private String token;

    @Override
    protected void initView() {
        setCenterText(R.string.img_mine_expert);
        setRtText(R.string.edit);
        token = SettingUtils.get(mContext, "token", null);

        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerUserExpert.setHasFixedSize(true);
        //为RecycleView设置默认的线性LayoutManage
        recyclerUserExpert.setLayoutManager(new LinearLayoutManager(mContext));

        //设置下拉刷新
        refreshUserExpert.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageNo = 1;//初始化页数为1
                concernPeopleParam.setRefresh(true);
                concernPeopleParam.setPageNo(mPageNo);//更新页数
                apiManager.getConcernPeople(concernPeopleParam);//通知服务器更新页数
            }
        });
        //设置上拉加载
        recyclerUserExpert.setAutoLoadMoreEnable(true);
        recyclerUserExpert.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPageNo++;//页数加一
                concernPeopleParam.setRefresh(false);
                concernPeopleParam.setPageNo(mPageNo);//更新页数
                apiManager.getConcernPeople(concernPeopleParam);//通知服务器更新页数
            }
        });

        adapter = new BrokersDetailAdapter(mContext);
        recyclerUserExpert.setAdapter(adapter);
        //设置item中经纪人的专家类型（即租售类型）
        adapter.setShowBrokerType(true);
        adapter.setCountType(2);
        //点击item查看经纪人个人资料的监听事件
        adapter.setmOnTouchDetailListener(new BrokersDetailAdapter.OnTouchDetailListener() {
            @Override
            public void toPersonalDetail(int position) {
                UserBean item = adapter.getData().get(position);
                toDetail("1", item.id, ParamUtils.isEmpty(item.remark) ? item.name : item.remark, position);
            }
        });
        //设置删除某个item的监听事件
        adapter.setmOnOperateListener(new BrokersDetailAdapter.OnOperateListener() {
            @Override
            public void toOperate(UserBean item, int position) {
                DeleteConcernParam deleteConcernParam = new DeleteConcernParam();
                deleteConcernParam.setToken(token);
                deleteConcernParam.setSid(item.solicitideId);
                deleteConcernParam.setTag(TAG);
                apiManager.deleteConcern(deleteConcernParam);
                ToolUtil.setRefreshing(refreshUserExpert, true, true);
            }
        });
    }

    @Override
    protected void initData() {
        //调用“查询用户关注的专家”接口
        concernPeopleParam = new ConcernPeopleParam();
        concernPeopleParam.setPageNo(mPageNo);
        concernPeopleParam.setPageSize(Constant.PAGE_SIZE);
        concernPeopleParam.setToken(token);
        concernPeopleParam.setRefresh(true);
        apiManager.getConcernPeople(concernPeopleParam);
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        finish();
    }

    /**
     * 编辑
     */
    @OnClick(id = R.id.tv_title_rt)
    public void toEdit() {
        if (isEdit) {
            mTvTitleRt.setText(R.string.edit);
            isEdit = false;
        } else {
            mTvTitleRt.setText(R.string.complete);
            isEdit = true;
        }

        adapter.setEdit(isEdit);
        recyclerUserExpert.notifiyChange();
    }

    /**
     * 查看关注的专家列表
     *
     * @param result
     */
    public void onEventMainThread(ConcernPeopleResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            //如果没数据就不做上拉加载
            if (ParamUtils.isNull(result.getData())) {
                recyclerUserExpert.notifyMoreFinish(false);
                return;
            }
            dataList = result.getData().getList();
            if (((ConcernPeopleParam) result.getParam()).getIsRefresh()) {
                adapter.setData(dataList);
                recyclerUserExpert.notifiyChange();
                refreshUserExpert.setRefreshing(false);
            } else {
                adapter.addData(dataList);
            }
            recyclerUserExpert.notifyMoreFinish(ParamUtils.isEmpty(dataList) || dataList.size() < Constant.PAGE_SIZE ? false : true);
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

    /**
     * 删除某个关注的专家
     *
     * @param result
     */
    public void onEventMainThread(DeleteConcernResult result) {
        if (!((DeleteConcernParam) result.getParam()).getTag().equals(TAG)) {
            return;
        }
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            lemonMessage.sendMessage(R.string.toast_delete_success);
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.REQUEST_MODIFY_REMARKS:
                    ToolUtil.setRefreshing(refreshUserExpert, true, true);
                    break;
            }
        }
    }

}
