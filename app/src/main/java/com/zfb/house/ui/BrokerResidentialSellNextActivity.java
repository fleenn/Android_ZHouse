package com.zfb.house.ui;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lemon.LemonActivity;
import com.lemon.LemonCacheManager;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.event.CloseEvent;
import com.lemon.event.RefreshRentShopEvent;
import com.lemon.event.RefreshSellShopEvent;
import com.lemon.model.StatusCode;
import com.lemon.util.ImageUploadUtil;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.lemon.util.StringUtils;
import com.zfb.house.R;
import com.zfb.house.component.ArraySelectWheelDialog;
import com.zfb.house.component.LoadDialog;
import com.zfb.house.component.NumberSelectWheelDialog;
import com.zfb.house.component.ReleaseHousingEditView;
import com.zfb.house.component.ReleaseHousingMultiSelectView;
import com.zfb.house.component.ReleaseHousingSelectView;
import com.zfb.house.component.SelectWheelDialog;
import com.zfb.house.model.bean.ReleaseHousePram;
import com.zfb.house.model.bean.ReleaseHousingMultiBean;
import com.zfb.house.model.bean.ReleasePhoto;
import com.zfb.house.model.bean.ReleasePull;
import com.zfb.house.model.bean.ReleasePullItem;
import com.zfb.house.model.param.QiNiuParam;
import com.zfb.house.model.param.ReleaseRentHouseParam;
import com.zfb.house.model.param.ReleaseSellHouseParam;
import com.zfb.house.model.result.QiNiuResult;
import com.zfb.house.model.result.ReleaseRentHouseResult;
import com.zfb.house.model.result.ReleaseSellHouseResult;
import com.zfb.house.util.ToolUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 经纪人发布房源第二页
 * Created by linwenbing on 16/6/7.
 */
@Layout(id = R.layout.activity_broker_residential_sell_next)
public class BrokerResidentialSellNextActivity extends LemonActivity {

    private static final String TAG = "BrokerResidentialSellNextActivity";

    //售房标签（最多3个）
    @FieldView(id = R.id.rhm_release_sign)
    private ReleaseHousingMultiSelectView mslReleaseSign;
    //配套设施（多选）
    @FieldView(id = R.id.rhm_release_facilities)
    private ReleaseHousingMultiSelectView mslReleaseFacilities;
    //建筑年代
    @FieldView(id = R.id.rhs_year_select)
    private ReleaseHousingSelectView rhsYear;
    //产权性质
    @FieldView(id = R.id.rhs_propert_select)
    private ReleaseHousingSelectView rhsPropert;
    //有无电梯
    @FieldView(id = R.id.rhs_elevator_select)
    private ReleaseHousingSelectView rhsElevator;
    //几梯几户
    @FieldView(id = R.id.rhs_households_select)
    private ReleaseHousingSelectView rhsHouseholds;
    //出租方式
    @FieldView(id = R.id.rhs_rent_way_select)
    private ReleaseHousingSelectView rhsRentWay;
    //售房标签的RelativeLayout
    @FieldView(id = R.id.rl_release_sign)
    private RelativeLayout rlReleaseSign;
    //配套设施的RelativeLayout
    @FieldView(id = R.id.rl_release_facilities)
    private RelativeLayout rlReleaseFacilities;
    //物业公司
    @FieldView(id = R.id.rhe_real)
    private ReleaseHousingEditView rheReal;

    //地址
    @FieldView(id = R.id.rhm_release_address)
    private ReleaseHousingEditView rhm_release_address;
    //房源描述
    @FieldView(id = R.id.rhm_release_desc)
    private ReleaseHousingEditView rhm_release_desc;

    //售房标签（商铺配套）
    @FieldView(id = R.id.tv_release_sign_title)
    private TextView signTitle;
    //(最多3个)
    @FieldView(id = R.id.tv_release_sign_content)
    private TextView signContent;
    //配套设施（可经营类别）
    @FieldView(id = R.id.tv_release_facilities_title)
    private TextView facTitle;
    //分割线一
    @FieldView(id = R.id.divider_release_one)
    private View dividerOne;
    //分割线二
    @FieldView(id = R.id.divider_release_two)
    private View dividerTwo;

    private ArraySelectWheelDialog propertDialog, evelatorDialog, householdsDialog, rentWayDialog;
    //几梯几户
    private SelectWheelDialog houseDialog;
    private NumberSelectWheelDialog yearDialog;

    private int mTag;
    private ReleasePull mReleasePull;
    //发布房源的实体
    private ReleaseHousePram mReleaseHousePram;
    //发布房源的类型
    private int mResourceType;
    //判断是租还是售的标记，1代表出售，2代表出租
    private int isSellOrRent = 1;
    private ReleasePhoto mReleasePhoto;
    private ArrayList<String> mEditPhotos = new ArrayList<>();
    private LoadDialog mLoadDialog;
    private List<String> shopTagList = new ArrayList<>();
    private List<String> sellTagList = new ArrayList<>();
    private List<String> providesList = new ArrayList<>();

    @Override
    protected void initView() {
        mReleaseHousePram = (ReleaseHousePram) getIntent().getSerializableExtra("body");
        mTag = getIntent().getIntExtra("tag", 0);
        mLoadDialog = new LoadDialog(this);
        if (mTag == ReleaseHousingActivity.HOUSING_SELL) {//住宅出售
            setCenterText(R.string.release_residential_title);
            mResourceType = 1;
            isSellOrRent = 1;
        } else if (mTag == ReleaseHousingActivity.OFFICE_SELL) {//写字楼出售
            setCenterText(R.string.release_residential_office_title);
            rlReleaseSign.setVisibility(View.GONE);
            rlReleaseFacilities.setVisibility(View.GONE);
            rhsElevator.setVisibility(View.GONE);
            rhsHouseholds.setVisibility(View.GONE);
            rheReal.setVisibility(View.VISIBLE);
            mResourceType = 3;
            isSellOrRent = 1;
            dividerOne.setVisibility(View.GONE);
            dividerTwo.setVisibility(View.GONE);
        } else if (mTag == ReleaseHousingActivity.SHOP_SELL) {//商铺出售
            setCenterText(R.string.release_residential_shop_title);
            rhsHouseholds.setTitle("当前状态");
            rhsElevator.setVisibility(View.GONE);
            mResourceType = 4;
            isSellOrRent = 1;
        } else if (mTag == ReleaseHousingActivity.VILLA_SELL) {//别墅出售
            setCenterText(R.string.release_residential_vial_sell);
            rhsElevator.setVisibility(View.GONE);
            rhsHouseholds.setVisibility(View.GONE);
            mResourceType = 2;
            isSellOrRent = 1;
        } else if (mTag == ReleaseHousingActivity.HOUSING_RENT) {//住宅出租
            setCenterText(R.string.release_residential_room_rent);
            rlReleaseSign.setVisibility(View.GONE);
            rhsRentWay.setVisibility(View.VISIBLE);
            rhsPropert.setTitle("支付方式");
            mResourceType = 1;
            isSellOrRent = 2;
            dividerOne.setVisibility(View.GONE);
        } else if (mTag == ReleaseHousingActivity.OFFICE_RENT) {//写字楼出租
            setCenterText(R.string.release_residential_office_rent);
            rhsPropert.setTitle("支付方式");
            rheReal.setVisibility(View.VISIBLE);
            rlReleaseSign.setVisibility(View.GONE);
            rlReleaseFacilities.setVisibility(View.GONE);
            dividerOne.setVisibility(View.GONE);
            dividerTwo.setVisibility(View.GONE);
            rhsElevator.setTitle("是否包含物业费");
            rhsHouseholds.setTitle("是否可分割");
            mResourceType = 3;
            isSellOrRent = 2;
        } else if (mTag == ReleaseHousingActivity.SHOP_RENT) {//商铺出租
            setCenterText(R.string.release_residential_shop_rent);
            rhsPropert.setTitle("支付方式");
            rhsElevator.setTitle("当前状态");
            rhsHouseholds.setTitle("是否可分割");
            mResourceType = 4;
            isSellOrRent = 2;
        } else if (mTag == ReleaseHousingActivity.VILLA_RENT) {//别墅出租
            setCenterText(R.string.release_residential_vial_rent);
            rhsHouseholds.setVisibility(View.GONE);
            rlReleaseSign.setVisibility(View.GONE);
            dividerOne.setVisibility(View.GONE);
            rhsElevator.setVisibility(View.GONE);
            rhsRentWay.setVisibility(View.VISIBLE);
            rhsPropert.setTitle("支付方式");
            rhsRentWay.setTitle("出租方式");
            mResourceType = 2;
            isSellOrRent = 2;
        }
        LemonCacheManager cacheManager = new LemonCacheManager();
        mReleasePull = cacheManager.getBean(ReleasePull.class);
        initTags();
        initReleaseView();
    }

    private void initTags() {
        sellTagList = StringUtils.string2List(mReleaseHousePram.getSellTag(), ",");
        shopTagList = StringUtils.string2List(mReleaseHousePram.getShopTag(), ",");
        providesList = StringUtils.string2List(mReleaseHousePram.getProvides(), ",");
        if (mTag == ReleaseHousingActivity.SHOP_SELL
                || mTag == ReleaseHousingActivity.SHOP_RENT) {//shop tag
            mslReleaseSign.setSelectNumber(providesList.size());
        } else {
            mslReleaseSign.setSelectNumber(sellTagList.size());
        }
    }

    private void initReleaseView() {
        List<ReleasePullItem> roomSignDatas = mReleasePull.getHOUSE_SELL();
        List<ReleasePullItem> shopSignDatas = mReleasePull.getHOUSE_SPPT();
        List<ReleaseHousingMultiBean> releaseDatas = new ArrayList<>();
        if (mTag == ReleaseHousingActivity.SHOP_SELL
                || mTag == ReleaseHousingActivity.SHOP_RENT) {//shop tag
            for (int i = 0; i < shopSignDatas.size(); i++) {
                ReleaseHousingMultiBean bean = new ReleaseHousingMultiBean();
                bean.setIsSelect(shopTagList.contains(shopSignDatas.get(i).getValue()));
                bean.setTitle(shopSignDatas.get(i).getLabel());
                bean.setValue(shopSignDatas.get(i).getValue());
                releaseDatas.add(bean);
            }
            signTitle.setText("商铺配套");
            signContent.setText("(多选)");
            mslReleaseSign.setMaxSelect(100);
        } else {
            for (int i = 0; i < roomSignDatas.size(); i++) {//sell tag
                ReleaseHousingMultiBean bean = new ReleaseHousingMultiBean();
                bean.setIsSelect(sellTagList.contains(roomSignDatas.get(i).getValue()));
                bean.setTitle(roomSignDatas.get(i).getLabel());
                bean.setValue(roomSignDatas.get(i).getValue());
                releaseDatas.add(bean);
            }
            mslReleaseSign.setMaxSelect(3);
        }

        mslReleaseSign.setViewData(releaseDatas);

        List<ReleaseHousingMultiBean> fasDatas = new ArrayList<>();
        List<ReleasePullItem> othersFacDatas = mReleasePull.getHOUSE_FYPT();
        List<ReleasePullItem> shopFacDatas = mReleasePull.getHOUSE_KJYLB();
        if (mTag == ReleaseHousingActivity.SHOP_SELL
                || mTag == ReleaseHousingActivity.SHOP_RENT) {
            for (int i = 0; i < shopFacDatas.size(); i++) {
                ReleaseHousingMultiBean bean = new ReleaseHousingMultiBean();
                bean.setIsSelect(providesList.contains(shopFacDatas.get(i).getValue()));
                bean.setTitle(shopFacDatas.get(i).getLabel());
                bean.setValue(shopFacDatas.get(i).getValue());
                fasDatas.add(bean);
            }
            facTitle.setText("可经营类别");
        } else {
            for (int i = 0; i < othersFacDatas.size(); i++) {//provides
                ReleaseHousingMultiBean bean = new ReleaseHousingMultiBean();
                bean.setIsSelect(providesList.contains(othersFacDatas.get(i).getValue()));
                bean.setTitle(othersFacDatas.get(i).getLabel());
                bean.setValue(othersFacDatas.get(i).getValue());
                fasDatas.add(bean);
            }
        }

        mslReleaseFacilities.setViewData(fasDatas);
        mslReleaseFacilities.setMaxSelect(100);
    }

    private void setPram() {
        if (mTag == ReleaseHousingActivity.SHOP_SELL || mTag == ReleaseHousingActivity.SHOP_RENT) {//商铺
            mReleaseHousePram.setProvides(mslReleaseSign.getSelectValue());
            mReleaseHousePram.setShopTag(mslReleaseFacilities.getSelectValue());
        } else {
            mReleaseHousePram.setSellTag(mslReleaseSign.getSelectValue());
            mReleaseHousePram.setProvides(mslReleaseFacilities.getSelectValue());
        }
    }

    @Override
    protected void initData() {
        mReleasePhoto = (ReleasePhoto) getIntent().getSerializableExtra("photo");
        mEditPhotos = getIntent().getStringArrayListExtra("edit_photos");
        Log.i("linwb", "end === " + mEditPhotos.toString() + " size == " + mReleasePhoto.getPhotoFiles().size());
        initEditData();
    }

    private String changeEditPhotos() {
        String values = "";
        for (int i = 0; i < mEditPhotos.size(); i++) {
            values = values + mEditPhotos.get(i) + ",";
        }
        Log.i("linwb", "photo ==  " + values);
        return values;
    }

    private void initEditData() {
        if (mReleaseHousePram != null && !TextUtils.isEmpty(mReleaseHousePram.getId())) {
            if (ParamUtils.isEmpty(mReleaseHousePram.getAge())) {
                rhsYear.setContent("");
            } else {
                rhsYear.setContent(mReleaseHousePram.getAge().endsWith("年") ? mReleaseHousePram.getAge() : mReleaseHousePram.getAge() + "年");
            }
            if (isSellOrRent == 1) {//出售，产权性质
                rhsPropert.setContent(mReleaseHousePram.getPropertyRightTypeName());
            } else {//出租，支付方式
                if (mTag == ReleaseHousingActivity.OFFICE_RENT || mTag == ReleaseHousingActivity.SHOP_RENT) {
                    rhsPropert.setContent(mReleaseHousePram.getRentalWayName());
                } else {
                    rhsPropert.setContent(ParamUtils.isEmpty(mReleaseHousePram.getPaymentTypeName()) ? "" : mReleaseHousePram.getPaymentTypeName());
                }
            }

            if (mTag == ReleaseHousingActivity.OFFICE_RENT) {//写字楼出租
                rhsElevator.setContent(mReleaseHousePram.getPropertyCost() == 1 ? "包含" : "不包含");
            } else if (mTag == ReleaseHousingActivity.SHOP_RENT) {//商铺出租
                rhsElevator.setContent(mReleaseHousePram.getShopStateName());
            } else if (mTag == ReleaseHousingActivity.VILLA_RENT) {//别墅出租
                rhsElevator.setContent(mReleaseHousePram.getRentType());
            } else {//住宅出租
                rhsElevator.setContent(mReleaseHousePram.getLift().equals("1") ? "有电梯" : "无电梯");
            }

            if (mTag == ReleaseHousingActivity.SHOP_SELL) {//商铺出售
                if (mReleaseHousePram.getShopState() == 1) {
                    rhsHouseholds.setContent("营业中");
                } else if (mReleaseHousePram.getShopState() == 2) {
                    rhsHouseholds.setContent("闲置中");
                } else {
                    rhsHouseholds.setContent("新增");
                }
            } else if (mTag == ReleaseHousingActivity.OFFICE_RENT || mTag == ReleaseHousingActivity.SHOP_RENT) {//写字楼出租 or 商铺出租
                if (ParamUtils.isEmpty(mReleaseHousePram.getCutEnabled()) || mReleaseHousePram.getCutEnabled().equals("false")) {
                    rhsHouseholds.setContent("不可分割");
                } else {
                    rhsHouseholds.setContent("可分割");
                }
            } else {//其他：几梯几户
                rhsHouseholds.setContent(getLiftTypeValue(mReleaseHousePram.getLiftType()));
            }

            rhm_release_address.setContent(mReleaseHousePram.getAddress());
            rhm_release_desc.setContent(mReleaseHousePram.getRemark());
            rhsRentWay.setContent(convertRentWay(mReleaseHousePram.getRentType()));
            rheReal.setContent(mReleaseHousePram.getPropertyCompany());
        }
    }

    /**
     * 初始化几梯几户
     *
     * @param value
     * @return
     */
    private String getLiftTypeValue(String value) {
        if (ParamUtils.isEmpty(value)) {
            return "";
        }
        String[] result = value.split(",");
        return Integer.valueOf(result[0]) + "梯" + Integer.valueOf(result[1]) + "户";
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        finish();
    }

    /**
     * 发布
     */
    @OnClick(id = R.id.btn_register)
    public void toRelease() {
        mLoadDialog.show();
        setPram();
        QiNiuParam qiNiuParam = new QiNiuParam();
        apiManager.uploadToken(qiNiuParam);
    }

    /**
     * 建筑年代
     */
    @OnClick(id = R.id.rhs_year_select)
    public void selectYear() {
        if (yearDialog != null) {
            yearDialog.show();
        } else {
            yearDialog = new NumberSelectWheelDialog(this, 1950, 2016, "年");
            yearDialog.show();
            yearDialog.setListener(new NumberSelectWheelDialog.SelectWheelResultListener() {
                @Override
                public void getSelectValue(String time) {
                    rhsYear.setContent(time.endsWith("年") ? time : time + "年");
                    if (time.endsWith("年")) {
                        time = time.replace("年", "");
                    }
                    mReleaseHousePram.setAge(time);
                }
            });
        }
    }

    /**
     * 产权性质
     */
    @OnClick(id = R.id.rhs_propert_select)
    public void selectPropert() {
        if (propertDialog != null) {
            propertDialog.show();
        } else {
            if (mTag == ReleaseHousingActivity.HOUSING_RENT || mTag == ReleaseHousingActivity.VILLA_RENT
                    || mTag == ReleaseHousingActivity.SHOP_RENT || mTag == ReleaseHousingActivity.OFFICE_RENT) {
                propertDialog = new ArraySelectWheelDialog(this, mTag == ReleaseHousingActivity.OFFICE_RENT || mTag == ReleaseHousingActivity.SHOP_RENT ?
                        mReleasePull.getHOUSE_XZLZFFS() : mReleasePull.getHOUSE_ZFFS());
            } else {
                propertDialog = new ArraySelectWheelDialog(this, mReleasePull.getHOUSE_CQXZ());
            }
            propertDialog.show();
            propertDialog.setListener(new ArraySelectWheelDialog.SelectWheelResultListener() {
                @Override
                public void getSelectValue(String time, String value) {
                    rhsPropert.setContent(time);
                    if (mTag == ReleaseHousingActivity.HOUSING_RENT || mTag == ReleaseHousingActivity.VILLA_RENT
                            || mTag == ReleaseHousingActivity.SHOP_RENT || mTag == ReleaseHousingActivity.OFFICE_RENT) {
                        if (mTag == ReleaseHousingActivity.OFFICE_RENT || mTag == ReleaseHousingActivity.SHOP_RENT) {//写字楼
                            mReleaseHousePram.setRentalWay(value);
                        } else {
                            mReleaseHousePram.setPaymentType(value);
                        }
                    } else {
                        mReleaseHousePram.setPropertyRightType(value);
                    }
                }
            });
        }
    }

    /**
     * 有无电梯
     */
    @OnClick(id = R.id.rhs_elevator_select)
    public void selectElevator() {
        if (evelatorDialog != null) {
            evelatorDialog.show();
        } else {
            if (mTag == ReleaseHousingActivity.OFFICE_RENT) {//写字楼出租
                String[] values = {"包含", "不包含"};
                evelatorDialog = new ArraySelectWheelDialog(this, values);
            } else if (mTag == ReleaseHousingActivity.SHOP_RENT) {//商铺出租
                evelatorDialog = new ArraySelectWheelDialog(this, mReleasePull.getHOUSE_SPZT());
            } else if (mTag == ReleaseHousingActivity.VILLA_RENT) {//别墅出租
                evelatorDialog = new ArraySelectWheelDialog(this, mReleasePull.getHOUSE_CZFS());
            } else {//住宅出租
                String[] values = {"有电梯", "无电梯"};
                evelatorDialog = new ArraySelectWheelDialog(this, values);
            }
            evelatorDialog.show();
            evelatorDialog.setListener(new ArraySelectWheelDialog.SelectWheelResultListener() {
                @Override
                public void getSelectValue(String time, String value) {
                    rhsElevator.setContent(time);
                    if (mTag == ReleaseHousingActivity.OFFICE_RENT) {
                        mReleaseHousePram.setPropertyCost(time.equals("包含") ? 1 : 0);
                    } else if (mTag == ReleaseHousingActivity.SHOP_RENT) {
                        int state = 1;
                        if (time.equals("营业中")) {
                            state = 1;
                        } else if (time.equals("闲置中")) {
                            state = 2;
                        } else {
                            state = 3;
                        }
                        mReleaseHousePram.setShopState(state);
                        mReleaseHousePram.setShopStateName(time);
                    } else if (mTag == ReleaseHousingActivity.VILLA_RENT) {//别墅出租

                    } else {
                        mReleaseHousePram.setLift(time.equals("有电梯") ? "1" : "0");
                    }
                }
            });
        }
    }

    /**
     * 几梯几户
     */
    @OnClick(id = R.id.rhs_households_select)
    public void selectHouseholds() {
        if (mTag == ReleaseHousingActivity.SHOP_SELL) {//商铺出售
            if (householdsDialog != null) {
                householdsDialog.show();
            } else {
                householdsDialog = new ArraySelectWheelDialog(this, mReleasePull.getHOUSE_SPZT());
                householdsDialog.setListener(new ArraySelectWheelDialog.SelectWheelResultListener() {
                    @Override
                    public void getSelectValue(String time, String value) {
                        rhsHouseholds.setContent(time);
                        int state = 1;
                        if (time.equals("营业中")) {
                            state = 1;
                        } else if (time.equals("闲置中")) {
                            state = 2;
                        } else {
                            state = 3;
                        }
                        mReleaseHousePram.setShopState(state);
                    }
                });
            }
        } else if (mTag == ReleaseHousingActivity.OFFICE_RENT || mTag == ReleaseHousingActivity.SHOP_RENT) {//写字楼出租 or 商铺出租
            if (householdsDialog != null) {
                householdsDialog.show();
            } else {
                String[] values = {"可分割", "不可分割"};
                householdsDialog = new ArraySelectWheelDialog(this, values);
                householdsDialog.setListener(new ArraySelectWheelDialog.SelectWheelResultListener() {
                    @Override
                    public void getSelectValue(String time, String value) {
                        rhsHouseholds.setContent(time);
                        mReleaseHousePram.setCutEnabled(time.equals("可分割") ? "true" : "false");
                    }
                });
            }
        } else {//其他——几梯几户
            if (houseDialog != null) {
                houseDialog.show();
            } else {
                houseDialog = new SelectWheelDialog(this, 2);
                houseDialog.show();
                houseDialog.setListener(new SelectWheelDialog.SelectWheelResultListener() {
                    @Override
                    public void getSelectValue(String time) {
                        rhsHouseholds.setContent(time);
                        time = time.replace("梯", ",");
                        time = time.replace("户", ",");
                        mReleaseHousePram.setLiftType(time);
                    }
                });
            }
        }
    }

    /**
     * 出租方式
     */
    @OnClick(id = R.id.rhs_rent_way_select)
    public void rentWay() {
        if (rentWayDialog != null) {
            rentWayDialog.show();
        } else {
            rentWayDialog = new ArraySelectWheelDialog(this, mReleasePull.getHOUSE_CZFS());
            rentWayDialog.show();
            rentWayDialog.setListener(new ArraySelectWheelDialog.SelectWheelResultListener() {
                @Override
                public void getSelectValue(String time, String value) {
                    rhsRentWay.setContent(time);
                    mReleaseHousePram.setRentType(value);
                }
            });
        }
    }

    /**
     * 七牛
     *
     * @param event
     */
    public void onEventMainThread(QiNiuResult event) {
        HashMap<String, String> data = (HashMap<String, String>) event.getData();
        final String token = data.get("token");
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyOnUploadFileListener listener = new MyOnUploadFileListener();
                ImageUploadUtil.OnUploadMultiFile(token, mReleasePhoto.getPhotoFiles(), listener, mContext);
                String photoStr = listener.getHttpUrls();
                String userToken = SettingUtils.get(mContext, "token", null);
                mReleaseHousePram.setPhoto(changeEditPhotos() + photoStr);
                Log.i("linwb", "photos = " + changeEditPhotos() + photoStr);

                //todo 暂时先处理
                String layout = mReleaseHousePram.getLayout();
                String oldLayout = mReleaseHousePram.getLayout();
                if (!ParamUtils.isEmpty(layout)) {
                    if (layout.contains("室") || layout.contains("厅") || layout.contains("卫") || layout.contains("阳")) {
                        layout = layout.replace("室", ",");
                        layout = layout.replace("厅", ",");
                        layout = layout.replace("卫", ",");
                        layout = layout.replace("阳", ",");
                    }
                    layout = layout.replace(",,", ",0,");
                    mReleaseHousePram.setLayout(layout);
                }

                //todo 暂时处理
                String liftType = mReleaseHousePram.getLiftType();
                String oldLiftType = mReleaseHousePram.getLiftType();
                if (!ParamUtils.isEmpty(liftType)) {
                    if (liftType.contains("梯") || liftType.contains("户")) {
                        liftType = liftType.replace("梯", ",");
                        liftType = liftType.replace("户", ",");
                    }
                    liftType = liftType.replace(",,", ",0,");
                    mReleaseHousePram.setLiftType(liftType);
                }
                mReleaseHousePram.setAddress(rhm_release_address.getContent());
                mReleaseHousePram.setRemark(rhm_release_desc.getContent());
                mReleaseHousePram.setPropertyCompany(rheReal.getContent());

                if (isSellOrRent == 1) {//出售
                    ReleaseSellHouseParam param = new ReleaseSellHouseParam();
                    param.setToken(userToken);
                    param.setHouseType(mResourceType);
                    param.setHouseDealReqStr(new Gson().toJson(mReleaseHousePram));
                    param.setTag(TAG);
                    if (TextUtils.isEmpty(mReleaseHousePram.getId())) {
                        apiManager.houseSellAndroid(param);
                    } else {
                        apiManager.updateHouseSell(param);
                    }
                } else {//出租
                    ReleaseRentHouseParam rentParam = new ReleaseRentHouseParam();
                    rentParam.setToken(userToken);
                    rentParam.setHouseType(mResourceType);
                    rentParam.setTag(TAG);
                    rentParam.setHouseReqStr(new Gson().toJson(mReleaseHousePram));
                    if (TextUtils.isEmpty(mReleaseHousePram.getId())) {
                        apiManager.houseRentAndroid(rentParam);
                    } else {
                        apiManager.updateHouseRent(rentParam);
                    }
                }
                mReleaseHousePram.setLayout(oldLayout);
                mReleaseHousePram.setLiftType(oldLiftType);
            }

        }).start();
    }

    /**
     * 发布租房返回值
     *
     * @param result
     */
    public void onEventMainThread(ReleaseRentHouseResult result) {
        if (!((ReleaseRentHouseParam) result.getParam()).getTag().equals(TAG)) {
            return;
        }
        if (ParamUtils.isNull(result.getData())) {//没有积分
            handleReleaseResult(result.getResultCode());
        } else {//有积分
            handleReleaseResult(result.getResultCode(), result.getData().getTotalPoint(), result.getData().getGetPoint());
        }
        EventBus.getDefault().post(new RefreshRentShopEvent());
    }

    /**
     * 发布售房返回值
     *
     * @param result
     */
    public void onEventMainThread(ReleaseSellHouseResult result) {
        if (!((ReleaseSellHouseParam) result.getParam()).getTag().equals(TAG)) {
            return;
        }
        if (ParamUtils.isNull(result.getData())) {//没有积分
            handleReleaseResult(result.getResultCode());
        } else {//有积分
            handleReleaseResult(result.getResultCode(), result.getData().getTotalPoint(), result.getData().getGetPoint());
        }
        EventBus.getDefault().post(new RefreshSellShopEvent());
    }

    /**
     * 发布接口返回处理——无积分
     *
     * @param statusCode
     */
    private void handleReleaseResult(String statusCode) {
        mLoadDialog.dismiss();
        if (!statusCode.equals(StatusCode.SUCCESS.getCode())) {
            lemonMessage.sendMessage(R.string.toast_release_fail);
        } else {
            setResult(RESULT_OK);
            EventBus.getDefault().post(new CloseEvent(BrokerResidentialSellActivity.class.getSimpleName()));
            finish();
        }
    }

    /**
     * 发布接口返回处理——有积分
     *
     * @param
     */
    private void handleReleaseResult(String statusCode, int totalPoint, int getPoint) {
        mLoadDialog.dismiss();
        if (!statusCode.equals(StatusCode.SUCCESS.getCode())) {
            lemonMessage.sendMessage(R.string.toast_release_fail);
        } else {
            ToolUtil.updatePoint(mContext, totalPoint, getPoint, R.string.toast_release_success);
            setResult(RESULT_OK);
            EventBus.getDefault().post(new CloseEvent(BrokerResidentialSellActivity.class.getSimpleName()));
            finish();
        }
    }

    /**
     * 七牛上传监听事件
     */
    private class MyOnUploadFileListener implements ImageUploadUtil.OnUploadFileListener {
        private String httpUrls = "";

        public String getHttpUrls() {
            return httpUrls;
        }

        @Override
        public void progress(File srcFile, double percent) {
        }

        @Override
        public void complete(File srcFile, String httpUrl) {
            if (TextUtils.isEmpty(httpUrls)) {
                httpUrls = httpUrl;
            } else {
                httpUrls += "," + httpUrl;
            }
        }

        @Override
        public void error(File srcFile) {
        }
    }

}
