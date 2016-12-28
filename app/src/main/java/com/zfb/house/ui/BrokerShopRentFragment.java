package com.zfb.house.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;

import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.model.StatusCode;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.ShopRentAdapter;
import com.zfb.house.component.SettingOperationView;
import com.zfb.house.model.bean.RentItem;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.model.param.DeleteShopHouseParam;
import com.zfb.house.model.param.PutOnShopHouseParam;
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
 * 类描述:    [经纪人店铺出租界面]
 * 创建人:    [xflu]
 * 创建时间:  [2016/6/3 10:11]
 * 修改人:    [xflu]
 * 修改时间:  [2016/6/3 10:11]
 * 修改备注:  [说明本次修改内容]
 * 版本:      [v1.0]
 */
@Layout(id = R.layout.fragment_shop_rent)
public class BrokerShopRentFragment extends BrokerShopFragment {

    @FieldView(id = R.id.lv_rent)
    private ListView lv_rent;
    private ShopRentAdapter adapter;
    private DeleteShopHouseParam mDeleteParam;
    private PutOnShopHouseParam mPutOnParam;//上下架
    private TopShopHouseParam mTopParam;
    private List<RentItem> mListDatas = new ArrayList<>();
    private String mToken;
    private UserBean selfUserBean;

    @Override
    protected void initData() {
        setTitle(R.string.rent);
        mToken = SettingUtils.get(getActivity(), "token", null);
        selfUserBean = UserBean.getInstance(getActivity());
        handler.sendEmptyMessageDelayed(2, 500);
    }

    public void onEventMainThread(RentListResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            showRents(result.getData().getList());
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
        if (mSellOrRentType == SettingOperationView.RENT && result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            for (int i = 0; i < mListDatas.size(); i++) {
                if (mListDatas.get(i).getId().equals(mDeleteParam.getRentingHouseIds())) {
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
        if (mSellOrRentType == SettingOperationView.RENT && result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
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
        if (mSellOrRentType == SettingOperationView.RENT && result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            for (int i = 0; i < mListDatas.size(); i++) {
                if (mListDatas.get(i).getId().equals(mTopParam.getHouseId())) {
                    RentItem item = mListDatas.get(i);
                    mListDatas.remove(i);
                    mListDatas.add(0, item);
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
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
        adapter = new ShopRentAdapter(handler, getActivity(), userBean, mListDatas, onOperationClickListener);
        lv_rent.setAdapter(adapter);
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
            Log.i("linwb","edit ===== 111");
            if (type == SettingOperationView.RENT) {
                RentItem bean = (RentItem) homeDataBean;
                Intent editIntent = new Intent(getActivity(), BrokerResidentialSellActivity.class);
                editIntent.putExtra("tag", getResidetialTag(bean.getResourceType()));
                editIntent.putExtra("rent_bean", bean);
                getActivity().startActivity(editIntent);
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
                handler.sendEmptyMessageDelayed(2,300);
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

}
