package com.zfb.house.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;

import com.lemon.LemonActivityManager;
import com.lemon.LemonContext;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.model.StatusCode;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.ShopSellAdapter;
import com.zfb.house.component.SettingOperationView;
import com.zfb.house.model.bean.SellItem;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.model.param.DeleteShopHouseParam;
import com.zfb.house.model.param.PutOnShopHouseParam;
import com.zfb.house.model.param.TopShopHouseParam;
import com.zfb.house.model.result.DeleteShopHouseResult;
import com.zfb.house.model.result.PutOnShopHouseResult;
import com.zfb.house.model.result.SellListResult;
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
@Layout(id = R.layout.fragment_shop_sell)
public class BrokerShopSellFragment extends BrokerShopFragment {
    private ShopSellAdapter adapter;
    private DeleteShopHouseParam mDeleteParam;
    private PutOnShopHouseParam mPutOnParam;//上下架
    private TopShopHouseParam mTopParam;
    private List<SellItem> mListDatas = new ArrayList<>();
    protected String mToken;

    @FieldView(id = R.id.lv_sell)
    private ListView lv_sell;
    private UserBean selfUserBean;

    @Override
    protected void initData() {
        setTitle(R.string.sell);
        mToken = SettingUtils.get(getActivity(), "token", null);
        selfUserBean = UserBean.getInstance(getActivity());
        handler.sendEmptyMessageDelayed(1, 100);
    }

    public void onEventMainThread(SellListResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            showSells(result.getData().getList());
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

    /**
     * 删除回调
     *
     * @param result
     */
    public void onEventMainThread(DeleteShopHouseResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            for (int i = 0; i < mListDatas.size(); i++) {
                if (!TextUtils.isEmpty(mDeleteParam.getRentingHouseIds()) && mListDatas.get(i).getId().equals(mDeleteParam.getRentingHouseIds())) {
                    mListDatas.remove(i);
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    /**
     * 上下架回调
     *
     * @param result
     */
    public void onEventMainThread(PutOnShopHouseResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            for (int i = 0; i < mListDatas.size(); i++) {
                if (mListDatas.get(i).getId().equals(mPutOnParam.getHouseId())) {
                    if (mListDatas.get(i).getUpDown().equals("0")) {
                        mListDatas.get(i).setUpDown("1");
                    } else {
                        mListDatas.get(i).setUpDown("0");
                    }
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    /**
     * 置顶回调
     *
     * @param result
     */
    public void onEventMainThread(TopShopHouseResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            for (int i = 0; i < mListDatas.size(); i++) {
                if (mListDatas.get(i).getId().equals(mTopParam.getHouseId())) {
                    SellItem item = mListDatas.get(i);
                    mListDatas.remove(i);
                    mListDatas.add(0, item);
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    private void showSells(List<SellItem> list) {
        if (!ParamUtils.isNull(selfUserBean) && !ParamUtils.isNull(selfUserBean.id)
                && !ParamUtils.isNull(userBean) && !ParamUtils.isNull(userBean.id)
                && selfUserBean.id.equals(userBean.id)) {
            mListDatas = list;
        } else {
            getUpDatas(list);
        }
        adapter = new ShopSellAdapter(handler, getActivity(), userBean, mListDatas, onOperationClickListener);
        lv_sell.setAdapter(adapter);
    }

    /**
     * 获取上架数据
     */
    private void getUpDatas(List<SellItem> list) {
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
            if (type == SettingOperationView.SELL)
                adapter.notifyDataSetChanged();
        }

        @Override
        public void OnEditClick(Object homeDataBean, int type) {
            mSellOrRentType = type;
            Log.i("linwb","aa == ");
            if (type == SettingOperationView.SELL) {
                SellItem bean = (SellItem) homeDataBean;
                Intent editIntent = new Intent(LemonContext.getBean(LemonActivityManager.class).getCurrentActivity(), BrokerResidentialSellActivity.class);
                editIntent.putExtra("tag", getResidetialTag(bean.getResourceType()));
                editIntent.putExtra("sell_bean", bean);
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
            if (type == SettingOperationView.SELL) {
                SellItem bean = (SellItem) homeDataBean;
                if (mDeleteParam == null) {
                    mDeleteParam = new DeleteShopHouseParam();
                    mDeleteParam.setToken(mToken);
                    mDeleteParam.setHouseType("2");
                }
                mDeleteParam.setRentingHouseIds(bean.getId());
                apiManager.deleteMyHouse(mDeleteParam);
            }
        }

        @Override
        public void OnUpClick(Object homeDataBean, int type) {
            mSellOrRentType = type;
            if (type == SettingOperationView.SELL) {
                SellItem bean = (SellItem) homeDataBean;
                if (mPutOnParam == null) {
                    mPutOnParam = new PutOnShopHouseParam();
                    mPutOnParam.setToken(mToken);
                    mPutOnParam.setHouseType("2");
                }
                if (bean.getUpDown().equals("0")) {
                    mPutOnParam.setUpDown("1");//1上架
                } else {
                    mPutOnParam.setUpDown("0");//0下架
                }

                Log.i("linwb", "shangjai == " + bean.getUpDown());
                mPutOnParam.setHouseId(bean.getId());
                apiManager.putOnHouse(mPutOnParam);
                handler.sendEmptyMessageDelayed(1,100);
            }
        }

        @Override
        public void OnTopClick(Object homeDataBean, int type) {
            mSellOrRentType = type;
            if (type == SettingOperationView.SELL) {
                SellItem bean = (SellItem) homeDataBean;
                if (mTopParam == null) {
                    mTopParam = new TopShopHouseParam();
                    mTopParam.setToken(mToken);
                    mTopParam.setHouseType("2");
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
            return ReleaseHousingActivity.HOUSING_SELL;
        } else if (resourceType.equals("2")) {
            return ReleaseHousingActivity.VILLA_SELL;
        } else if (resourceType.equals("3")) {
            return ReleaseHousingActivity.OFFICE_SELL;
        } else if (resourceType.equals("4")) {
            return ReleaseHousingActivity.SHOP_SELL;
        }
        return 10;
    }

}
