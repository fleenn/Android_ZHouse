package com.zfb.house.ui;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.lemon.LemonActivityManager;
import com.lemon.LemonContext;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.config.Config;
import com.lemon.event.HideOperationView;
import com.lemon.event.RefreshRentShopEvent;
import com.lemon.model.StatusCode;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.RentAdapter;
import com.zfb.house.component.LoadMoreRecyclerView;
import com.zfb.house.component.SettingOperationView;
import com.zfb.house.model.bean.RentItem;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.model.param.DeleteShopHouseParam;
import com.zfb.house.model.param.PutOnShopHouseParam;
import com.zfb.house.model.param.RentListParam;
import com.zfb.house.model.param.TopShopHouseParam;
import com.zfb.house.model.result.DeleteShopHouseResult;
import com.zfb.house.model.result.PutOnShopHouseResult;
import com.zfb.house.model.result.RentListResult;
import com.zfb.house.model.result.TopShopHouseResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称:  [zfbandroid]
 * 包:        [com.zfb.house.ui]
 * 类描述:    [经纪人店铺出售界面]
 * 创建人:    [xflu]
 * 创建时间:  [2016/6/3 10:11]
 * 修改人:    [xflu]
 * 修改时间:  [2016/6/3 10:11]
 * 修改备注:  [说明本次修改内容]
 * 版本:      [v1.0]
 */
@Layout(id = R.layout.fragment_shop_rent_refresh)
public class BrokerShopRentRefreshFragment extends BrokerShopFragment {
    private DeleteShopHouseParam mDeleteParam;
    private PutOnShopHouseParam mPutOnParam;//上下架
    private TopShopHouseParam mTopParam;
    private List<RentItem> mListDatas = new ArrayList<>();
    protected String mToken;

    @FieldView(id = R.id.recycler_lv_rent)
    private LoadMoreRecyclerView recyclerBrokerList;
    @FieldView(id = R.id.refresh_lv_rent)
    private SwipeRefreshLayout refreshBrokerList;
    private UserBean selfUserBean;
    RentAdapter adapter;

    int pageNo = 1;
    @Override
    protected void initView() {
        recyclerBrokerList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerBrokerList.setLayoutManager(layoutManager);
        adapter = new RentAdapter(getActivity(), userBean, mListDatas, onOperationClickListener,recyclerBrokerList);
        recyclerBrokerList.setAdapter(adapter);
        refreshBrokerList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                RentListParam param = new RentListParam();
                param.setToken(SettingUtils.get(LemonContext.getAppContext(), "token", ""));
                param.setPageNo(1);
                param.setPageSize(Config.getIntValue("list_size"));
                param.setBrokers(ParamUtils.isEmpty(id) ? SettingUtils.get(getActivity(), "id", "") : id);
                param.setIsRefresh(true);
                apiManager.rentList(param);
            }
        });
        recyclerBrokerList.setAutoLoadMoreEnable(true);
        recyclerBrokerList.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                pageNo++;
                RentListParam param = new RentListParam();
                param.setToken(SettingUtils.get(LemonContext.getAppContext(), "token", ""));
                param.setPageNo(pageNo);
                param.setPageSize(Config.getIntValue("list_size"));
                param.setBrokers(ParamUtils.isEmpty(id) ? SettingUtils.get(getActivity(), "id", "") : id);
                param.setIsRefresh(false);
                apiManager.rentList(param);
            }
        });
    }

    @Override
    protected void initData() {
        setTitle(R.string.rent);
        mToken = SettingUtils.get(getActivity(), "token", null);
        selfUserBean = UserBean.getInstance(getActivity());
        handler.sendEmptyMessageDelayed(2, 300);
    }

    public void onEventMainThread(RentListResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            mListDatas = new ArrayList<>();
            showRents(result.getData().getList());

            if (ParamUtils.isNull(result.getData())) {
                recyclerBrokerList.notifyMoreFinish(false);
                return;
            }

            List<RentItem> data = mListDatas;
            if (((RentListParam) result.getParam()).getIsRefresh()) {
                adapter.setmData(data);
                recyclerBrokerList.notifiyChange();
                refreshBrokerList.setRefreshing(false);
            } else {
                adapter.getmData().addAll(data);
            }

            if (ParamUtils.isEmpty(data) || data.size() < Config.getIntValue("list_size")) {
                recyclerBrokerList.notifyMoreFinish(false);
            } else {
                recyclerBrokerList.notifyMoreFinish(true);
            }
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

    @Override
    public void setUserBean(UserBean userBean) {
        super.setUserBean(userBean);
        adapter.setUserBean(userBean);
    }

    /**
     * 删除回调
     *
     * @param result
     */
    public void onEventMainThread(DeleteShopHouseResult result) {
        if (mSellOrRentType == SettingOperationView.RENT && result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            handler.sendEmptyMessageDelayed(2, 300);
        }
    }

    /**
     * 上下架回调
     *
     * @param result
     */
    public void onEventMainThread(PutOnShopHouseResult result) {
        if (mSellOrRentType == SettingOperationView.RENT && result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            handler.sendEmptyMessageDelayed(2, 300);
        }else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

    /**
     * 置顶回调
     *
     * @param result
     */
    public void onEventMainThread(TopShopHouseResult result) {
        if (mSellOrRentType == SettingOperationView.RENT && result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            handler.sendEmptyMessageDelayed(2, 300);
        }else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

    private void showRents(List<RentItem> list) {
        if (!ParamUtils.isNull(selfUserBean) && !ParamUtils.isNull(selfUserBean.id)
                && !ParamUtils.isNull(userBean) && !ParamUtils.isNull(userBean.id)
                && selfUserBean.id.equals(userBean.id)) {
            mListDatas = list;
        } else {
            getUpDatas(list);
        }
    }

    /**
     * 获取上架数据
     */
    private void getUpDatas(List<RentItem> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (!ParamUtils.isEmpty(list.get(i).getUpDown()) && list.get(i).getUpDown().equals("1")) {
                mListDatas.add(list.get(i));
            }
        }
    }

    private SettingOperationView.OnOperationClickListener onOperationClickListener = new SettingOperationView.OnOperationClickListener() {
        @Override
        public void OnOpenClick(int type) {
            mSellOrRentType = type;
            if (type == SettingOperationView.RENT)
                adapter.notifyDataSetChanged();
        }

        @Override
        public void OnEditClick(Object homeDataBean, int type) {
            mSellOrRentType = type;
            if (type == SettingOperationView.RENT) {
                RentItem bean = (RentItem) homeDataBean;
                Intent editIntent = new Intent(LemonContext.getBean(LemonActivityManager.class).getCurrentActivity(), BrokerResidentialSellActivity.class);
                editIntent.putExtra("tag", getResidetialTag(bean.getResourceType()));
                editIntent.putExtra("rent_bean", bean);
                LemonContext.getBean(LemonActivityManager.class).getCurrentActivity().startActivity(editIntent);
            }
        }

        @Override
        public void OnShareClick(Object homeDataBean, int type) {
            mSellOrRentType = type;
        }

        @Override
        public void OnDeleteClick(Object homeDataBean, int type) {
            mSellOrRentType = type;
            if (type == SettingOperationView.RENT) {
                RentItem bean = (RentItem) homeDataBean;
                if (mDeleteParam == null) {
                    mDeleteParam = new DeleteShopHouseParam();
                    mDeleteParam.setToken(mToken);
                    mDeleteParam.setHouseType("1");
                }
                mDeleteParam.setRentingHouseIds(bean.getId());
                apiManager.deleteMyHouse(mDeleteParam);
            }
        }

        @Override
        public void OnUpClick(Object homeDataBean, int type) {
            mSellOrRentType = type;
            if (type == SettingOperationView.RENT) {
                RentItem bean = (RentItem) homeDataBean;
                if (mPutOnParam == null) {
                    mPutOnParam = new PutOnShopHouseParam();
                    mPutOnParam.setToken(mToken);
                    mPutOnParam.setHouseType("1");
                }
                if (bean.getUpDown().equals("0")) {
                    mPutOnParam.setUpDown("1");//1上架
                } else {
                    mPutOnParam.setUpDown("0");//0下架
                }

                mPutOnParam.setHouseId(bean.getId());
                apiManager.putOnHouse(mPutOnParam);
            }
        }

        @Override
        public void OnTopClick(Object homeDataBean, int type) {
            mSellOrRentType = type;
            if (type == SettingOperationView.RENT) {
                RentItem bean = (RentItem) homeDataBean;
                if (mTopParam == null) {
                    mTopParam = new TopShopHouseParam();
                    mTopParam.setToken(mToken);
                    mTopParam.setHouseType("1");
                }

                mTopParam.setHouseId(bean.getId());
                apiManager.topHouse(mTopParam);
            }
        }
    };

    protected int getResidetialTag(String resourceType) {
        if (TextUtils.isEmpty(resourceType))
            return 10;

        if (resourceType.equals("1")) {
            return ReleaseHousingActivity.HOUSING_RENT;
        } else if (resourceType.equals("2")) {
            return ReleaseHousingActivity.VILLA_RENT;
        } else if (resourceType.equals("3")) {
            return ReleaseHousingActivity.OFFICE_RENT;
        } else if (resourceType.equals("4")) {
            return ReleaseHousingActivity.SHOP_RENT;
        }
        return 10;
    }

    public void onEventMainThread(RefreshRentShopEvent result) {
        handler.sendEmptyMessageDelayed(2, 300);
    }

    public void onEventMainThread(HideOperationView result) {
        SettingOperationView.setmCurrentShowBtnID("-1");
        recyclerBrokerList.notifiyChange();
    }
}
