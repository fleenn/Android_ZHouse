package com.zfb.house.ui;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.lemon.LemonActivity;
import com.lemon.LemonCacheManager;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.event.CloseEvent;
import com.lemon.util.MultiMediaUtil;
import com.lemon.util.ParamUtils;
import com.lemon.util.sdCard.FileSuffix;
import com.lemon.util.sdCard.MultiCard;
import com.lemon.util.sdCard.StorageUtil;
import com.zfb.house.R;
import com.zfb.house.component.ArraySelectWheelDialog;
import com.zfb.house.component.ChosePicSchemaLayout;
import com.zfb.house.component.PickPhotoLayout;
import com.zfb.house.component.ReleaseHousingEditView;
import com.zfb.house.component.ReleaseHousingSelectView;
import com.zfb.house.component.SelectWheelDialog;
import com.zfb.house.iml.OnImageChoiceListener;
import com.zfb.house.model.bean.PlotSearch;
import com.zfb.house.model.bean.ReleaseHousePram;
import com.zfb.house.model.bean.ReleasePhoto;
import com.zfb.house.model.bean.ReleasePull;
import com.zfb.house.model.bean.RentItem;
import com.zfb.house.model.bean.SellItem;
import com.zfb.house.util.ToolUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * 经纪人发布房源第一页
 * Created by linwenbing on 16/6/7.
 */
@Layout(id = R.layout.activity_broker_residential_sell)
public class BrokerResidentialSellActivity extends LemonActivity implements OnImageChoiceListener {

    private final static int GET_LOCAL_IMAGE_REQUEST = 0x001;// 相册
    private final static int CAPTURE_IMAGE_REQUEST = 0x002;// 拍照
    private final static int SELECT_HOUSE = 0x003;// 小区
    //Intent固定传递参数key
    public static final String EXTRA_IMAGE_URLS = "image_urls";
    //Intent传递已有图片数组
    public static final String EXIST_IMAGE_URLS = "exist_image_urls";
    //小区名称
    @FieldView(id = R.id.rhs_house_name)
    private ReleaseHousingSelectView rhsHouseName;
    //户型
    @FieldView(id = R.id.rhs_room_select)
    private ReleaseHousingSelectView rhsRoom;
    //朝向
    @FieldView(id = R.id.rhs_toward_select)
    private ReleaseHousingSelectView rhsToward;
    //装修程度
    @FieldView(id = R.id.rhs_decorate)
    private ReleaseHousingSelectView rhsDecorate;
    //级别
    @FieldView(id = R.id.rhs_rank_select)
    private ReleaseHousingSelectView rhsRank;
    //售价
    @FieldView(id = R.id.rhe_money)
    private ReleaseHousingEditView rheMoney;
    //面积
    @FieldView(id = R.id.rhs_area)
    private ReleaseHousingEditView rheArea;
    //楼层（第几层）
    @FieldView(id = R.id.et_floor_count)
    private EditText etFloorCount;
    //楼层（共几层）
    @FieldView(id = R.id.et_total_floor)
    private EditText etTotalFloor;
    //房源标题
    @FieldView(id = R.id.rhe_title)
    private ReleaseHousingEditView rheTitle;

    //户型
    private SelectWheelDialog roomDialog;
    private ArraySelectWheelDialog towardDialog, decorateDialog, rankDialog, typeDialog;
    private int mTag;
    private ReleasePull mReleasePull;
    //添加照片控件
    @FieldView(id = R.id.pickPhotoLayout)
    private PickPhotoLayout pickPhotoLayout;
    //选择照片来源控件
    @FieldView(id = R.id.chosePicSchemaLayout)
    private ChosePicSchemaLayout chosePicSchemaLayout;
    private File mImageFile;
    //发布房源的实体
    private ReleaseHousePram mReleaseHousePram;
    //发布房源的类型
    private int mResourceType;
    private ReleasePhoto mReleasePhoto;
    //出租实体
    private RentItem mRentBean;
    //出售实体
    private SellItem mSellBean;
    private ArrayList<String> editPhotoUrls = new ArrayList<>();

    @Override
    protected void initView() {
        setRtText(R.string.release_next);
        setRtTextColor(R.color.my_gray_three);

        mTag = getIntent().getIntExtra("tag", 0);
        if (mTag == ReleaseHousingActivity.HOUSING_SELL) {//住宅出售
            setCenterText(R.string.release_residential_title);
            mResourceType = 1;
        } else if (mTag == ReleaseHousingActivity.OFFICE_SELL) {//写字楼出售
            setCenterText(R.string.release_residential_office_title);
            rhsRank.setVisibility(View.VISIBLE);
            rhsToward.setVisibility(View.GONE);
            rhsRoom.setTitle("类型");
            mResourceType = 3;
        } else if (mTag == ReleaseHousingActivity.SHOP_SELL) {//商铺出售
            setCenterText(R.string.release_residential_shop_title);
            rhsRank.setVisibility(View.GONE);
            rhsToward.setVisibility(View.GONE);
            rhsRoom.setTitle("类型");
            rhsRank.setTitle("类别");
            mResourceType = 4;
        } else if (mTag == ReleaseHousingActivity.VILLA_SELL) {//别墅出售
            setCenterText(R.string.release_residential_vial_sell);
            mResourceType = 2;
            hideFloorCount();
        } else if (mTag == ReleaseHousingActivity.HOUSING_RENT) {//住宅出租
            setCenterText(R.string.release_residential_room_rent);
            rheMoney.setTitle("租金");
            rheMoney.setUnit("元/月");
            mResourceType = 1;
        } else if (mTag == ReleaseHousingActivity.OFFICE_RENT) {//写字楼出租
            setCenterText(R.string.release_residential_office_rent);
            rhsRank.setVisibility(View.VISIBLE);
            rhsToward.setVisibility(View.GONE);
            rhsRoom.setTitle("类型");
            rheMoney.setTitle("租金");
            rheMoney.setUnit("元/月");
            mResourceType = 3;
        } else if (mTag == ReleaseHousingActivity.SHOP_RENT) {//商铺出租
            setCenterText(R.string.release_residential_shop_rent);
            rhsRank.setVisibility(View.VISIBLE);
            rhsToward.setVisibility(View.GONE);
            rhsRoom.setTitle("类型");
            rhsRank.setTitle("类别");
            rheMoney.setTitle("租金");
            rheMoney.setUnit("元/月");
            mResourceType = 4;
        } else if (mTag == ReleaseHousingActivity.VILLA_RENT) {//别墅出租
            setCenterText(R.string.release_residential_vial_rent);
            rheMoney.setTitle("租金");
            rheMoney.setUnit("元/月");
            mResourceType = 2;
            hideFloorCount();
        }

        //    初始化控件监听事件
        pickPhotoLayout.setOnAddImageListener(new PickPhotoLayout.OnAddImageListener() {
            @Override
            public void OnRequestAdd() {
                if (chosePicSchemaLayout != null) {
                    chosePicSchemaLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        rheArea.setInputType(InputType.TYPE_CLASS_NUMBER);
        rheMoney.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    @Override
    protected void initData() {
        LemonCacheManager cacheManager = new LemonCacheManager();
        mReleasePull = cacheManager.getBean(ReleasePull.class);
        mReleaseHousePram = new ReleaseHousePram();
        initEditData();
    }

    /**
     * 因别墅只有“共xx层”，所以要隐藏“第xx层”
     */
    private void hideFloorCount() {
        etFloorCount.setVisibility(View.GONE);
        findViewById(R.id.tv_floor_count_title).setVisibility(View.GONE);
        findViewById(R.id.tv_floor_count_unit).setVisibility(View.GONE);
    }

    private void initEditData() {
        mRentBean = (RentItem) getIntent().getSerializableExtra("rent_bean");
        if (mRentBean != null) {
            mReleaseHousePram.setId(mRentBean.getId());
            mReleaseHousePram.setTitle(mRentBean.getTitle());
            mReleaseHousePram.setFloor(mRentBean.getFloor());
            mReleaseHousePram.setAddress(mRentBean.getAddress());
            mReleaseHousePram.setAge(mRentBean.getAge());
            mReleaseHousePram.setAround(mRentBean.getAround());
            mReleaseHousePram.setCategory(StringToInteger(mRentBean.getCategory()));
            mReleaseHousePram.setCommuntityName(mRentBean.getCommuntityName());
            mReleaseHousePram.setContacts(mRentBean.getContacts());
            mReleaseHousePram.setContactSex(StringToInteger(mRentBean.getContactSex()));
            mReleaseHousePram.setContactWay(mRentBean.getContactWay());
            mReleaseHousePram.setDecorationLevel(StringToInteger(mRentBean.getDecorationLevel()));
            mReleaseHousePram.setDirection(mRentBean.getDirection());
            mReleaseHousePram.setDisplay(StringToInteger(mRentBean.getDisplay()));
            mReleaseHousePram.setFlatShareType(mRentBean.getFlatShareType());
            mReleaseHousePram.setLayout(mRentBean.getLayout());
            mReleaseHousePram.setOfficeId(mRentBean.getOfficeId());
            mReleaseHousePram.setOfficeLevel(StringToInteger(mRentBean.getOfficeLevel()));
            mReleaseHousePram.setOfficeType(StringToInteger(mRentBean.getOfficeType()));
            mReleaseHousePram.setPhotos(mRentBean.getPhoto());
            mReleaseHousePram.setProperty(StringToBoolean(mRentBean.getProperty()));
            mReleaseHousePram.setPropertyCompany(mRentBean.getPropertyCompany());
            mReleaseHousePram.setPropertyCost(StringToInteger(mRentBean.getPropertyCost()));
            mReleaseHousePram.setPropertyRightTypeName(mRentBean.getPropertyRightTypeName());
            mReleaseHousePram.setPropertyRightType(mRentBean.getPropertyRightType());
            mReleaseHousePram.setProvides(mRentBean.getProvides());
            mReleaseHousePram.setRegionName(mRentBean.getRegionName());
            mReleaseHousePram.setReleaseType(StringToInteger(mRentBean.getReleaseFlag()));
            mReleaseHousePram.setRemark(mRentBean.getRemark());
            mReleaseHousePram.setRelease(StringToBoolean(mRentBean.getRelease()));
            mReleaseHousePram.setSellTag(mRentBean.getSellTag());
            mReleaseHousePram.setUserType(StringToInteger(mRentBean.getUserType()));
            mReleaseHousePram.setTransfer(StringToBoolean(mRentBean.getTraffic()));
            mReleaseHousePram.setSyncState(StringToInteger(mRentBean.getSyncState()));
            mReleaseHousePram.setShopType(StringToInteger(mRentBean.getShopType()));
            mReleaseHousePram.setShopStateName(mRentBean.getShopStateName());
            mReleaseHousePram.setShopState(StringToInteger(mRentBean.getShopState()));
            mReleaseHousePram.setServiceVillage(mRentBean.getServiceVillage());
            mReleaseHousePram.setServiceDistrict(mRentBean.getServiceDistrict());
            mReleaseHousePram.setShopCategoryName(mRentBean.getShopCategoryName());
            mReleaseHousePram.setShopCategory(StringToInteger(mRentBean.getShopCategory()));
            mReleaseHousePram.setServerAreaId(mRentBean.getServerAreaId());
            mReleaseHousePram.setShopTag(mRentBean.getShopTag());
            mReleaseHousePram.setResourceType(StringToInteger(mRentBean.getResourceType()));
            mReleaseHousePram.setLift(mRentBean.getLift());
            mReleaseHousePram.setLiftType(mRentBean.getLiftType());
            mReleaseHousePram.setPaymentTypeName(mRentBean.getPaymentTypeName());
            mReleaseHousePram.setRentalWayName(mRentBean.getRentalWayName());
            mReleaseHousePram.setRentType(mRentBean.getRentType());
            mReleaseHousePram.setCutEnabled(mRentBean.getCutEnabled());

            rhsHouseName.setContent(mRentBean.getCommuntityName());
            if (mTag == ReleaseHousingActivity.OFFICE_RENT) {//写字楼出租
                rhsRoom.setContent(mRentBean.getOfficeTypeName());
            } else if (mTag == ReleaseHousingActivity.SHOP_RENT) {//商铺出租
                rhsRoom.setContent(mRentBean.getShopTypeName());
            } else {//住宅出租、别墅出租
                rhsRoom.setContent(ToolUtil.setHouseLayout(mRentBean.getLayout()));
            }
            //面积
            rheArea.setContent(mRentBean.getArea());
            //出租租金
            rheMoney.setContent(mRentBean.getRental());
            //楼层
            setFloor(mRentBean.getFloor());
            //朝向
            rhsToward.setContent(convertDirectionName(mRentBean.getDirection()));
            //装修
            rhsDecorate.setContent(mRentBean.getDecorationLevelName());
            //标题
            rheTitle.setContent(mRentBean.getTitle());
            //级别
            rhsRank.setContent(mTag == ReleaseHousingActivity.SHOP_RENT ? mRentBean.getShopCategoryName() : mRentBean.getOfficeLevelName());
            showEditImage(mRentBean.getPhoto());
        }

        mSellBean = (SellItem) getIntent().getSerializableExtra("sell_bean");
        if (mSellBean != null) {
            mReleaseHousePram.setId(mSellBean.getId());
            mReleaseHousePram.setTitle(mSellBean.getTitle());
            mReleaseHousePram.setFloor(mSellBean.getFloor());
            mReleaseHousePram.setAddress(mSellBean.getAddress());
            mReleaseHousePram.setAge(mSellBean.getAge());
            mReleaseHousePram.setAround(mSellBean.getAround());
            mReleaseHousePram.setCategory(StringToInteger(mSellBean.getCategory()));
            mReleaseHousePram.setCommuntityName(mSellBean.getCommuntityName());
            mReleaseHousePram.setContacts(mSellBean.getContacts());
            mReleaseHousePram.setContactSex(StringToInteger(mSellBean.getContactSex()));
            mReleaseHousePram.setContactWay(mSellBean.getContactWay());
            mReleaseHousePram.setCutEnabled(mSellBean.getCutEnabled());
            mReleaseHousePram.setDecorationLevel(StringToInteger(mSellBean.getDecorationLevel()));
            mReleaseHousePram.setDirection(mSellBean.getDirection());
            mReleaseHousePram.setDisplay(StringToInteger(mSellBean.getDisplay()));
            mReleaseHousePram.setFlatShareType(mSellBean.getFlatShareType());
            mReleaseHousePram.setLayout(mSellBean.getLayout());
            mReleaseHousePram.setOfficeId(mSellBean.getOfficeId());
            mReleaseHousePram.setOfficeLevel(StringToInteger(mSellBean.getOfficeLevel()));
            mReleaseHousePram.setOfficeType(StringToInteger(mSellBean.getOfficeType()));
            mReleaseHousePram.setPhotos(mSellBean.getPhoto());
            mReleaseHousePram.setProperty(StringToBoolean(mSellBean.getProperty()));
            mReleaseHousePram.setPropertyCompany(mSellBean.getPropertyCompany());
            mReleaseHousePram.setPropertyCost(StringToInteger(mSellBean.getPropertyCost()));
            mReleaseHousePram.setPropertyRightTypeName(mSellBean.getPropertyRightTypeName());
            mReleaseHousePram.setPropertyRightType(mSellBean.getPropertyRightType());
            mReleaseHousePram.setProvides(mSellBean.getProvides());
            mReleaseHousePram.setRegionName(mSellBean.getRegionName());
            mReleaseHousePram.setReleaseType(StringToInteger(mSellBean.getReleaseFlag()));
            mReleaseHousePram.setRemark(mSellBean.getRemark());
            mReleaseHousePram.setRelease(StringToBoolean(mSellBean.getRelease()));
            mReleaseHousePram.setSellTag(mSellBean.getSellTag());
            mReleaseHousePram.setUserType(StringToInteger(mSellBean.getUserType()));
            mReleaseHousePram.setTransfer(StringToBoolean(mSellBean.getTraffic()));
            mReleaseHousePram.setSyncState(StringToInteger(mSellBean.getSyncState()));
            mReleaseHousePram.setShopType(StringToInteger(mSellBean.getShopType()));
            mReleaseHousePram.setShopStateName(mSellBean.getShopStateName());
            mReleaseHousePram.setShopState(StringToInteger(mSellBean.getShopState()));
            mReleaseHousePram.setServiceVillage(mSellBean.getServiceVillage());
            mReleaseHousePram.setServiceDistrict(mSellBean.getServiceDistrict());
            mReleaseHousePram.setShopCategoryName(mSellBean.getShopCategoryName());
            mReleaseHousePram.setShopCategory(StringToInteger(mSellBean.getShopCategory()));
            mReleaseHousePram.setServerAreaId(mSellBean.getServerAreaId());
            mReleaseHousePram.setShopTag(mSellBean.getShopTag());
            mReleaseHousePram.setResourceType(StringToInteger(mSellBean.getResourceType()));
            mReleaseHousePram.setLift(mSellBean.getLift());
            mReleaseHousePram.setLiftType(mSellBean.getLiftType());

            rhsHouseName.setContent(mSellBean.getCommuntityName());
            if (mTag == ReleaseHousingActivity.OFFICE_SELL) {//写字楼出售
                rhsRoom.setContent(mSellBean.getOfficeTypeName());
            } else if (mTag == ReleaseHousingActivity.SHOP_SELL) {//商铺出售
                rhsRoom.setContent(mSellBean.getShopTypeName());
            } else {//住宅出售、别墅出售
                rhsRoom.setContent(ToolUtil.setHouseLayout(mSellBean.getLayout()));
            }
            rheArea.setContent(mSellBean.getArea());
            rheMoney.setContent(mSellBean.getWishPrice());//售价
            setFloor(mSellBean.getFloor());
            rhsToward.setContent(convertDirectionName(mSellBean.getDirection()));
            rhsDecorate.setContent(mSellBean.getDecorationLevelName());
            rheTitle.setContent(mSellBean.getTitle());
            showEditImage(mSellBean.getPhoto());
            rhsRank.setContent(mSellBean.getOfficeLevelName());
        }
    }

    private void showEditImage(String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        String[] pathArray = path.split(",");
        if (!ParamUtils.isEmpty(pathArray)) {
            for (int i = 0; i < pathArray.length; i++) {
                onAddImageView(pathArray[i]);
            }
        }
    }

    /**
     * 设置楼层
     *
     * @param floor
     */
    private void setFloor(String floor) {
        if (!ParamUtils.isEmpty(floor) && floor.contains("/")) {
            String[] split = floor.split("/");
            etFloorCount.setText(split[0]);
            if (ParamUtils.isEmpty(split[1])) {
                etTotalFloor.setText("--");
            } else {
                etTotalFloor.setText(split[1]);
            }
        }
    }

    /**
     * 将String类型转化为Integer类型
     *
     * @param value String类型的值
     * @return Integer类型的值
     */
    private int StringToInteger(String value) {
        if (TextUtils.isEmpty(value)) {
            return 0;
        }
        return Integer.valueOf(value);
    }

    /**
     * 将String类型转化为Boolean类型
     *
     * @param value String类型的值
     * @return Boolean类型的值
     */
    private boolean StringToBoolean(String value) {
        if (TextUtils.isEmpty(value)) {
            return false;
        }
        if (value.equals("true")) {
            return true;
        }
        return false;
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        finish();
    }

    /**
     * 下一步
     */
    @OnClick(id = R.id.tv_title_rt)
    public void toNext() {
        if (judgeInputEmpty()) {
            return;
        }
        toIntent(mTag, BrokerResidentialSellNextActivity.class);
    }

    /**
     * 选择小区
     */
    @OnClick(id = R.id.rhs_house_name)
    public void selectHouse() {
        startActivityForResult(new Intent(this, PlotSearchActivity.class), SELECT_HOUSE);
    }

    /**
     * 选择户型
     */
    @OnClick(id = R.id.rhs_room_select)
    public void selectRoom() {//商铺
        if (mTag == ReleaseHousingActivity.SHOP_SELL || mTag == ReleaseHousingActivity.SHOP_RENT) {//商铺
            if (typeDialog != null) {
                typeDialog.show();
            } else {
                typeDialog = new ArraySelectWheelDialog(this, mReleasePull.getHOUSE_SPLX());
                typeDialog.setListener(
                        new ArraySelectWheelDialog.SelectWheelResultListener() {
                            @Override
                            public void getSelectValue(String time, String value) {
                                rhsRoom.setContent(time);
                                mReleaseHousePram.setShopType(stringToIntent(value));
                            }
                        });
            }
        } else if (mTag == ReleaseHousingActivity.OFFICE_SELL || mTag == ReleaseHousingActivity.OFFICE_RENT) {//写字楼
            if (typeDialog != null) {
                typeDialog.show();
            } else {
                typeDialog = new ArraySelectWheelDialog(this, mReleasePull.getHOUSE_XZLLX());
                typeDialog.setListener(new ArraySelectWheelDialog.SelectWheelResultListener() {
                    @Override
                    public void getSelectValue(String time, String value) {
                        rhsRoom.setContent(time);
                        mReleaseHousePram.setOfficeType(stringToIntent(value));
                    }
                });
            }
        } else {//住宅、别墅
            if (roomDialog != null) {
                roomDialog.show();
            } else {
                roomDialog = new SelectWheelDialog(this, 4);
                roomDialog.show();
                roomDialog.setListener(new SelectWheelDialog.SelectWheelResultListener() {
                    @Override
                    public void getSelectValue(String time) {
                        rhsRoom.setContent(time);
                        mReleaseHousePram.setLayout(time);
                    }
                });
            }
        }
    }

    /**
     * 朝向选择
     */
    @OnClick(id = R.id.rhs_toward_select)
    public void selectToward() {
        if (towardDialog != null) {
            towardDialog.show();
        } else {
            towardDialog = new ArraySelectWheelDialog(this, mReleasePull.getHOUSE_CX());
            towardDialog.show();
            towardDialog.setListener(new ArraySelectWheelDialog.SelectWheelResultListener() {
                @Override
                public void getSelectValue(String time, String value) {
                    rhsToward.setContent(time);
                    mReleaseHousePram.setDirection(value);
                }
            });
        }
    }

    /**
     * 装修程度
     */
    @OnClick(id = R.id.rhs_decorate)
    public void selectrDecorate() {
        if (decorateDialog != null) {
            decorateDialog.show();
        } else {
            decorateDialog = new ArraySelectWheelDialog(this, mReleasePull.getHOUSE_ZXCD());
            decorateDialog.show();
            decorateDialog.setListener(new ArraySelectWheelDialog.SelectWheelResultListener() {
                @Override
                public void getSelectValue(String time, String value) {
                    rhsDecorate.setContent(time);
                    mReleaseHousePram.setDecorationLevel(Integer.parseInt(value));
                }
            });
        }
    }

    /**
     * 级别选择
     */
    @OnClick(id = R.id.rhs_rank_select)
    public void selectRank() {
        if (rankDialog != null) {
            rankDialog.show();
        } else {
            if (mTag == ReleaseHousingActivity.OFFICE_SELL || mTag == ReleaseHousingActivity.OFFICE_RENT) {//写字楼出租出售
                rankDialog = new ArraySelectWheelDialog(this, mReleasePull.getHOUSE_XZLJB());
            } else if (mTag == ReleaseHousingActivity.SHOP_SELL || mTag == ReleaseHousingActivity.SHOP_RENT) {//商铺出租出售
                rankDialog = new ArraySelectWheelDialog(this, mReleasePull.getHOUSE_SPLB());
            }
            rankDialog.show();
            rankDialog.setListener(new ArraySelectWheelDialog.SelectWheelResultListener() {
                @Override
                public void getSelectValue(String time, String value) {
                    rhsRank.setContent(time);
                    if (mTag == ReleaseHousingActivity.OFFICE_SELL || mTag == ReleaseHousingActivity.OFFICE_RENT) {//写字楼出租出售
                        mReleaseHousePram.setOfficeLevel(Integer.parseInt(value));
                    } else {
                        mReleaseHousePram.setShopCategory(Integer.parseInt(value));
                    }
                }
            });
        }
    }

    /**
     * 判断是否全部填
     */
    private boolean judgeInputEmpty() {
        if (judgeSelectEmpty(rhsHouseName)) return true;
        if (judgeSelectEmpty(rhsRoom)) return true;
        if (judgeEditEmpty(rheMoney)) return true;
        if (judgeEditEmpty(rheArea)) return true;
        if (judgeEditEmpty(etFloorCount)) return true;
        if (judgeEditEmpty(etTotalFloor)) return true;
        if (judgeSelectEmpty(rhsToward)) return true;
        if (judgeSelectEmpty(rhsDecorate)) return true;
        if (judgeSelectEmpty(rhsRank)) return true;
        if (judgeEditEmpty(rheTitle)) return true;
        if (judgeZero(rheArea)) return true;
        if (judgeZero(rheMoney)) return true;
        if (judgeEditTextZero(etFloorCount, "楼层")) return true;
        if (judgeEditTextZero(etTotalFloor, "楼层")) return true;
        return false;
    }

    /**
     * 判断该选择的有没有选择
     *
     * @param view
     * @return
     */
    private boolean judgeSelectEmpty(ReleaseHousingSelectView view) {
        if (view.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(view.getContent())) {
                lemonMessage.sendMessage("请选择" + view.getTitle());
                return true;
            }
        }
        return false;
    }

    /**
     * 判断该输入的有没有输入
     *
     * @param view
     * @return
     */
    private boolean judgeEditEmpty(ReleaseHousingEditView view) {
        if (view.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(view.getContent())) {
                lemonMessage.sendMessage("请输入" + view.getTitle());
                return true;
            }
        }
        return false;
    }

    /**
     * 判断楼层有没有输入
     *
     * @param view
     * @return
     */
    private boolean judgeEditEmpty(EditText view) {
        if (view.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(view.getText())) {
                lemonMessage.sendMessage("请输入楼层");
                return true;
            }
        }
        return false;
    }

    /**
     * 判断不能为0的地方有没有为0
     *
     * @param view
     * @param title
     * @return
     */
    private boolean judgeEditTextZero(EditText view, String title) {
        if (view.getVisibility() == View.VISIBLE) {
            if (!TextUtils.isEmpty(view.getText().toString()) && stringToDouble(view.getText().toString()) <= 0) {
                lemonMessage.sendMessage(title + "不能为0");
                return true;
            }
        }
        return false;
    }

    /**
     * 判断不能为0的地方有没有为0
     *
     * @param view
     * @return
     */
    private boolean judgeZero(ReleaseHousingEditView view) {
        if (view.getVisibility() == View.VISIBLE) {
            if (!TextUtils.isEmpty(view.getContent()) && stringToDouble(view.getContent()) <= 0) {
                lemonMessage.sendMessage(view.getTitle() + "不能为0");
                return true;
            }
        }
        return false;
    }

    private void toIntent(int tag, Class<?> cls) {
        setPram();
        Intent intent = new Intent(this, cls);
        intent.putExtra("tag", tag);
        intent.putExtra("body", mReleaseHousePram);
        intent.putExtra("photo", mReleasePhoto);
        intent.putStringArrayListExtra("edit_photos", editPhotoUrls);
        if (!judgeValues()) {
            return;
        }
        startActivity(intent);
    }

    private boolean judgeValues() {
        try {
            if (etFloorCount.getVisibility() == View.VISIBLE) {
                if (Integer.valueOf(etFloorCount.getText().toString()) > Integer.valueOf(etTotalFloor.getText().toString())) {
                    lemonMessage.sendMessage("楼层信息不允许大于总楼层数");
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (ParamUtils.isEmpty(getSelectedImages())) {
            lemonMessage.sendMessage("图片不允许为空");
            return false;
        }
        return true;
    }

    private void setPram() {
        mReleaseHousePram.setReleaseType(0);//默认用0
//    mReleaseHousePram.setReleaseType(mResourceType);
        mReleaseHousePram.setArea(rheArea.getContent());
        Set<Uri> uris = getSelectedImages();
        List<File> files = new ArrayList<>();
        if (uris != null && !uris.isEmpty()) {
            for (Uri uri : uris) {
                Log.i("linwb", "urls = " + uri.toString());
                if (uri.toString().startsWith("http://")) {
                    editPhotoUrls.add(uri.toString());
                } else {
                    files.add(new File(getRealFilePath(mContext, uri)));
                }
            }
        }
        mReleasePhoto = new ReleasePhoto();
        mReleasePhoto.setPhotoFiles(files);
        mReleaseHousePram.setWishPrice(stringToIntent(rheMoney.getContent()));
        mReleaseHousePram.setTitle(rheTitle.getContent());
        mReleaseHousePram.setFloor(etFloorCount.getText() + "/" + etTotalFloor.getText());
        mReleaseHousePram.setRental(Integer.valueOf(rheMoney.getContent()));
        double pirce = Double.valueOf(rheMoney.getContent()) / (Double.valueOf(rheArea.getContent()));
        mReleaseHousePram.setUnitPrice((int) pirce);
    }

    private int stringToIntent(String value) {
        try {
            return Integer.valueOf(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 将String类型的值转化为Double类型的值
     *
     * @param value String类型的值
     * @return Double类型的值
     */
    private double stringToDouble(String value) {
        try {
            return Double.valueOf(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void onAddImageView(Uri uri) {
        pickPhotoLayout.addImageView(uri);
    }

    @Override
    public void onAddImageView(String path) {
        pickPhotoLayout.addImageView(path);
    }

    @Override
    public void onAddImageViewList(ArrayList<String> list) {
        pickPhotoLayout.repleceImgstrs(list);
    }

    @Override
    public Set<Uri> getSelectedImages() {
        return pickPhotoLayout.getSelectedImages();
    }

    /**
     * 获取本地图片
     */
    public boolean getLocalImage() {
        // TODO: 2016/5/11 内存卡余量判断
//    if (SDCardUtils.isSDCardSapceForWriteWithTip(this,
//            MultiCard.TYPE_IMAGE, 0, true)) {
//        lemonMessage.sendMessage("内存卡存储空间不足");
//        return false;
//    }
        new AsyncTask<String, Integer, Boolean>() {
            @Override
            protected void onPreExecute() {
                lemonMessage.sendMessage(getString(R.string.toast_waiting_for_open_album));
            }

            @Override
            protected Boolean doInBackground(String... params) {
                return true;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                try {
                    Set<Uri> uris = getSelectedImages();
                    if (uris != null) {
                        Intent mIntent = new Intent(mContext, PhotoAlbumActivity.class);
                        ArrayList<String> arrayList = new ArrayList<>();
                        for (Uri uri : uris) {
                            arrayList.add(uri.toString());
                        }
                        mIntent.putStringArrayListExtra(EXIST_IMAGE_URLS, arrayList);
                        startActivityForResult(mIntent, GET_LOCAL_IMAGE_REQUEST);
                    } else {
                        Intent picture = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(picture, GET_LOCAL_IMAGE_REQUEST);
                    }
                } catch (Exception e) {
                    Log.e("", "getLocalImage()", e);
                    lemonMessage.sendMessage(R.string.toast_error_to_open_album);
                }
            }
        }.execute();
        return true;
    }

    /**
     * 进入相机拍照
     */
    public boolean captureImage() {
        if (StorageUtil.isSDCardSapceForWriteWithTip(this, MultiCard.TYPE_IMAGE, 0, true)) {
            return false;
        }
        new AsyncTask<String, Integer, Boolean>() {
            @Override
            protected void onPreExecute() {
                lemonMessage.sendMessage(R.string.toast_open_camera);
            }

            @Override
            protected Boolean doInBackground(String... params) {
                return true;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String fileName = UUID.randomUUID().toString() + FileSuffix.JPG;
                mImageFile = new File(StorageUtil.getWritePathIgnoreError(mContext, fileName));
                Uri capturedImgUri = Uri.fromFile(mImageFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, capturedImgUri);
                try {
                    startActivityForResult(intent, CAPTURE_IMAGE_REQUEST);
                } catch (ActivityNotFoundException e) {
                    lemonMessage.sendMessage(R.string.toast_camera_invalid);
                }
            }
        }.execute();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GET_LOCAL_IMAGE_REQUEST:// 获取本地图片
                if (data != null) {
                    File mImageFile = null;
                    if (data.getData() != null) {
                        Uri uri = data.getData();
                        String filePath = uri.getPath();
                        if (!(filePath.endsWith(".jpg") || filePath.endsWith(".jpeg") || filePath.endsWith(".png")
                                || filePath.endsWith(".bmp") || filePath.endsWith(".gif") || filePath.startsWith("/external/images"))) {
                            lemonMessage.sendMessage(R.string.toast_img_format_error);
                            return;
                        }
                        //mDynImageLayout.addImageView(uri);
                        String imagePath = getRealFilePath(mContext, uri);
                        onAddImageView(imagePath);
                    } else if (data.getExtras() != null) {
                        ArrayList<String> list = data.getExtras().getStringArrayList(EXTRA_IMAGE_URLS);
                        if (list != null) {
                            //mDynImageLayout.repleceImgstrs(list);
                            onAddImageViewList(list);
                        }
                    }
                }
                break;
            case CAPTURE_IMAGE_REQUEST://拍照
                if (mImageFile == null || !mImageFile.exists()) {// 有可能不存在图片
                    return;
                }
                // N930拍照取消也产生字节为0的文件
                if (mImageFile.length() <= 0) {
                    mImageFile.delete();
                    return;
                }
                mImageFile = MultiMediaUtil.scaleImage(this, mImageFile);
                //mDynImageLayout.addImageView(mImageFile.getAbsolutePath());
                onAddImageView(mImageFile.getAbsolutePath());
                break;
            case SELECT_HOUSE://小区
                if (data == null) {
                    return;
                }
                PlotSearch plot = (PlotSearch) data.getExtras().getSerializable("plot_info");
                if (plot != null) {
                    rhsHouseName.setContent(plot.getName());
                    mReleaseHousePram.setCommuntityName(plot.getName());
                    mReleaseHousePram.setServerAreaId(plot.getPid());
                    mReleaseHousePram.setServiceVillage(plot.getId());
                }
                break;
            default:
                break;
        }
    }

    /**
     * 根据URI获取文件路径
     *
     * @param context
     * @param uri
     * @return
     */
    public String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) {
            return null;
        }
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public void onEventMainThread(CloseEvent event) {
        if (event.getType().equals(BrokerResidentialSellActivity.class.getSimpleName())) {
            finish();
        }
    }

}
