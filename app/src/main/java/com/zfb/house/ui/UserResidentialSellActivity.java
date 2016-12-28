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

import com.google.gson.Gson;
import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.model.StatusCode;
import com.lemon.util.ImageUploadUtil;
import com.lemon.util.MultiMediaUtil;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.lemon.util.sdCard.FileSuffix;
import com.lemon.util.sdCard.MultiCard;
import com.lemon.util.sdCard.StorageUtil;
import com.zfb.house.R;
import com.zfb.house.component.ArraySelectWheelDialog;
import com.zfb.house.component.ChosePicSchemaLayout;
import com.zfb.house.component.LoadDialog;
import com.zfb.house.component.PickPhotoLayout;
import com.zfb.house.component.ReleaseHousingEditView;
import com.zfb.house.component.ReleaseHousingSelectView;
import com.zfb.house.component.SelectWheelDialog;
import com.zfb.house.iml.OnImageChoiceListener;
import com.zfb.house.model.bean.PlotSearch;
import com.zfb.house.model.bean.ReleaseHousePram;
import com.zfb.house.model.bean.ReleasePull;
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
import java.util.Set;
import java.util.UUID;

/**
 * 业主委托（用户）
 * Created by linwenbing on 16/6/15.
 */
@Layout(id = R.layout.activity_user_residential_sell)
public class UserResidentialSellActivity extends LemonActivity implements OnImageChoiceListener {

    private static final String TAG = "UserResidentialSellActivity";

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
    //售价
    @FieldView(id = R.id.rhe_money)
    private ReleaseHousingEditView rheMoney;
    //面积
    @FieldView(id = R.id.rhe_area)
    private ReleaseHousingEditView rheArea;
    //添加照片控件
    @FieldView(id = R.id.pickPhotoLayout)
    private PickPhotoLayout pickPhotoLayout;
    //选择照片来源控件
    @FieldView(id = R.id.chosePicSchemaLayout)
    private ChosePicSchemaLayout chosePicSchemaLayout;

    private int mTag;
    private File mImageFile;
    private ReleasePull mReleasePull;
    //户型
    private SelectWheelDialog roomDialog;
    private ArraySelectWheelDialog rankDialog;
    //发布房源的实体
    private ReleaseHousePram mReleaseHousePram;
    //发布房源的类型
    private int mResourceType;
    //判断是租还是售的标记，1代表出售，2代表出租
    private int isSellOrRent = 1;
    //加载对话框
    private LoadDialog mLoadDialog;
    //(integer, optional): 租金的类型，例如1：元/平米天，2： 元/平米月，3： 元/月
    private int rentalType;

    @Override
    protected void initView() {
        mTag = getIntent().getIntExtra("tag", 0);
        mLoadDialog = new LoadDialog(this);
        if (mTag == ReleaseHousingActivity.HOUSING_SELL) {//住宅出售
            setCenterText(R.string.release_residential_user_title);
            mResourceType = 1;
            isSellOrRent = 1;
        } else if (mTag == ReleaseHousingActivity.OFFICE_SELL) {//写字楼出售
            setCenterText(R.string.release_residential_office_title);
            rhsRoom.setTitle("类型");
            mResourceType = 3;
            isSellOrRent = 1;
        } else if (mTag == ReleaseHousingActivity.SHOP_SELL) {//商铺出售
            setCenterText(R.string.release_residential_shop_title);
            rhsRoom.setTitle("类型");
            mResourceType = 4;
            isSellOrRent = 1;
        } else if (mTag == ReleaseHousingActivity.VILLA_SELL) {//别墅出售
            setCenterText(R.string.release_residential_vial_sell);
            mResourceType = 2;
            isSellOrRent = 1;
        } else if (mTag == ReleaseHousingActivity.HOUSING_RENT) {//住宅出租
            setCenterText(R.string.release_residential_room_user_rent);
            rheMoney.setTitle("租金");
            rheMoney.setUnit("元/月");
            mResourceType = 1;
            isSellOrRent = 2;
            rentalType = 3;
        } else if (mTag == ReleaseHousingActivity.OFFICE_RENT) {//写字楼出租
            setCenterText(R.string.release_residential_office_rent);
            rheMoney.setTitle("租金");
            rheMoney.setUnit("元/月");
            rhsRoom.setTitle("类型");
            mResourceType = 3;
            isSellOrRent = 2;
            rentalType = 3;
        } else if (mTag == ReleaseHousingActivity.SHOP_RENT) {//商铺出租
            setCenterText(R.string.release_residential_shop_rent);
            rhsRoom.setTitle("类型");
            rheMoney.setTitle("租金");
            rheMoney.setUnit("元/月");
            mResourceType = 4;
            rentalType = 3;
        } else if (mTag == ReleaseHousingActivity.VILLA_RENT) {//别墅出租
            setCenterText(R.string.release_residential_vial_rent);
            rheMoney.setTitle("租金");
            rheMoney.setUnit("元/月");
            mResourceType = 2;
            isSellOrRent = 2;
            rentalType = 3;
        }
        //设置售价和面积的输入类型为数字
        rheMoney.setInputType(InputType.TYPE_CLASS_NUMBER);
        rheArea.setInputType(InputType.TYPE_CLASS_NUMBER);
        //初始化控件监听事件
        pickPhotoLayout.setOnAddImageListener(new PickPhotoLayout.OnAddImageListener() {
            @Override
            public void OnRequestAdd() {
                if (chosePicSchemaLayout != null) {
                    chosePicSchemaLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void initData() {
        mReleaseHousePram = new ReleaseHousePram();
        mReleasePull = cacheManager.getBean(ReleasePull.class);
        Log.i("linwb", "hx = " + mReleasePull.getHOUSE_CX());
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
        if (isDataEmpty()) {
            return;
        }
        mLoadDialog.show();
        QiNiuParam qiNiuParam = new QiNiuParam();
        apiManager.uploadToken(qiNiuParam);
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
    public void selectRoom() {
        if (mTag == ReleaseHousingActivity.SHOP_RENT || mTag == ReleaseHousingActivity.SHOP_SELL) {//商铺
            if (rankDialog != null) {
                rankDialog.show();
            } else {
                rankDialog = new ArraySelectWheelDialog(this, mReleasePull.getHOUSE_SPLX());
                rankDialog.show();
                rankDialog.setListener(new ArraySelectWheelDialog.SelectWheelResultListener() {
                    @Override
                    public void getSelectValue(String time, String value) {
                        rhsRoom.setContent(time);
                        mReleaseHousePram.setShopType(stringToInteger(value));
                    }
                });
            }
        } else if (mTag == ReleaseHousingActivity.OFFICE_SELL || mTag == ReleaseHousingActivity.OFFICE_RENT) {//写字楼
            if (rankDialog != null) {
                rankDialog.show();
            } else {
                rankDialog = new ArraySelectWheelDialog(this, mReleasePull.getHOUSE_XZLLX());
                rankDialog.show();
                rankDialog.setListener(new ArraySelectWheelDialog.SelectWheelResultListener() {
                    @Override
                    public void getSelectValue(String time, String value) {
                        rhsRoom.setContent(time);
                        mReleaseHousePram.setOfficeType(stringToInteger(value));
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
                        time = time.replace("室", ",");
                        time = time.replace("厅", ",");
                        time = time.replace("阳", ",");
                        time = time.replace("卫", ",");
                        mReleaseHousePram.setLayout(time);
                    }
                });
            }
        }
    }

    /**
     * 判断数据是否为空
     *
     * @return
     */
    private boolean isDataEmpty() {
        if (judgeSelectEmpty(rhsHouseName)) return true;
        if (judgeSelectEmpty(rhsRoom)) return true;
        if (judgeEditEmpty(rheMoney)) return true;
        if (judgeEditEmpty(rheArea)) return true;
        if (judgeZero(rheMoney)) return true;
        if (judgeZero(rheArea)) return true;
        if (ParamUtils.isEmpty(getSelectedImages())) {
            lemonMessage.sendMessage("图片不允许为空");
            return true;
        }
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

    /**
     * 将String类型转化为Integer类型
     *
     * @param value String类型的值
     * @return Integer类型的值
     */
    private int stringToInteger(String value) {
        if (TextUtils.isEmpty(value)) {
            return 0;
        }
        return Integer.valueOf(value);
    }

    /**
     * @param value String类型的值
     * @return
     */
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
                Set<Uri> uris = getSelectedImages();
                List<File> files = new ArrayList<>();
                if (uris != null && !uris.isEmpty()) {
                    for (Uri uri : uris) {
                        files.add(new File(getRealFilePath(mContext, uri)));
                    }
                }
                //七牛上传监听事件
                MyOnUploadFileListener listener = new MyOnUploadFileListener();
                ImageUploadUtil.OnUploadMultiFile(token, files, listener, mContext);
                String photoStr = listener.getHttpUrls();
                mReleaseHousePram.setPhoto(photoStr);
                String userToken = SettingUtils.get(mContext, "token", null);
                mReleaseHousePram.setArea(rheArea.getContent());
                if (isSellOrRent == 1) {//出售
                    ReleaseSellHouseParam param = new ReleaseSellHouseParam();
                    param.setToken(userToken);
                    param.setHouseType(mResourceType);
                    param.setTag(TAG);
                    mReleaseHousePram.setWishPrice(stringToIntent(rheMoney.getContent()));
                    param.setHouseDealReqStr(new Gson().toJson(mReleaseHousePram));
                    apiManager.houseSellAndroid(param);
                } else {//出租
                    ReleaseRentHouseParam rentParam = new ReleaseRentHouseParam();
                    rentParam.setToken(userToken);
                    rentParam.setHouseType(mResourceType);
                    rentParam.setTag(TAG);
                    mReleaseHousePram.setWishPrice(stringToIntent(rheMoney.getContent()));
                    mReleaseHousePram.setRentalType(rentalType);
                    mReleaseHousePram.setRental(stringToIntent(rheMoney.getContent()));
                    rentParam.setHouseReqStr(new Gson().toJson(mReleaseHousePram));
                    apiManager.houseRentAndroid(rentParam);
                }
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
        handleReleaseResult(result.getResultCode(), result.getData().getTotalPoint(), result.getData().getGetPoint());
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
        handleReleaseResult(result.getResultCode(), result.getData().getTotalPoint(), result.getData().getGetPoint());
    }

    /**
     * 发布接口返回处理
     *
     * @param statusCode 发布结果状态码
     * @param totalPoint 总积分
     * @param getPoint   本次操作所得积分
     */
    private void handleReleaseResult(String statusCode, int totalPoint, int getPoint) {
        //隐藏加载对话框
        mLoadDialog.dismiss();
        //更新积分
        ToolUtil.updatePoint(mContext, totalPoint, getPoint);
        if (!statusCode.equals(StatusCode.SUCCESS.getCode())) {//发布失败
            lemonMessage.sendMessage(R.string.toast_release_fail);
        } else {//发布成功
            //更新积分
            ToolUtil.updatePoint(mContext, totalPoint, getPoint, R.string.toast_release_success);
            setResult(RESULT_OK);
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
//if (SDCardUtils.isSDCardSapceForWriteWithTip(this,
//        MultiCard.TYPE_IMAGE, 0, true)) {
//    lemonMessage.sendMessage("内存卡存储空间不足");
//    return false;
//}
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
                            arrayList.add(uri.getPath());
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
                        if (!(filePath.endsWith(".jpg") || filePath.endsWith(".jpeg") || filePath.endsWith(".png") || filePath.endsWith(".bmp")
                                || filePath.endsWith(".gif") || filePath.startsWith("/external/images"))) {
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
                    mReleaseHousePram.setCommuntityNameName(plot.getName());
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

}
