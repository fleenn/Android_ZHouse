package com.zfb.house.ui;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.emchat.Constant;
import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.event.MomentsTitlePhotoEvent;
import com.lemon.event.UpdateBrokerEvent;
import com.lemon.event.UpdateUserInfoEvent;
import com.lemon.model.StatusCode;
import com.lemon.util.EventUtil;
import com.lemon.util.GlideUtil;
import com.lemon.util.ImageUploadUtil;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.component.ArraySelectWheelDialog;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.model.param.AliasParam;
import com.zfb.house.model.param.AvatarParam;
import com.zfb.house.model.param.QiNiuParam;
import com.zfb.house.model.param.SexParam;
import com.zfb.house.model.param.UserInfoParam;
import com.zfb.house.model.param.UserPersonalParam;
import com.zfb.house.model.result.AliasResult;
import com.zfb.house.model.result.AvatarResult;
import com.zfb.house.model.result.QiNiuResult;
import com.zfb.house.model.result.SexResult;
import com.zfb.house.model.result.UserInfoResult;
import com.zfb.house.model.result.UserPersonalResult;
import com.zfb.house.util.ToolUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

/**
 * 我的（经纪人）->个人信息
 * Created by Administrator on 2016/6/16.
 */
@Layout(id = R.layout.activity_mine_broker_personal)
public class BrokerDataActivity extends LemonActivity {

    private static final String TAG = "BrokerDataActivity";
    //编辑昵称的请求码
    private final int REQUEST_MODIFY_ALIAS = 0x2;

    //相册的标记
    private final int SELECT_PICTURE = 0;
    private final int SELECT_PICTURE_FOR_MEIZU = 7;
    //相机的标记
    private final int SELECT_CAMERA = 1;

    //头像
    @FieldView(id = R.id.img_broker_avatar)
    private ImageView imgAvatar;
    //昵称
    @FieldView(id = R.id.txt_broker_name)
    private TextView txtName;
    //性别
    @FieldView(id = R.id.txt_broker_sex)
    private TextView txtSex;
    //联系电话
    @FieldView(id = R.id.txt_broker_phone)
    private TextView txtPhone;
    //所属公司
    @FieldView(id = R.id.edt_broker_company)
    private EditText edtCompany;
    //所属门店
    @FieldView(id = R.id.edt_broker_shop)
    private EditText edtShop;
    //已选择服务片区的个数
    @FieldView(id = R.id.txt_broker_distrust)
    private TextView txtDistrictNumber;
    //已选择服务小区的个数
    @FieldView(id = R.id.txt_broker_village)
    private TextView txtVillageNumber;
    //实名认证
    @FieldView(id = R.id.txt_broker_realname)
    private TextView txtRealName;
    //实名认证点击控件
    @FieldView(id = R.id.rlayout_broker_realname)
    private RelativeLayout rlayoutBrokerRealname;
    //资质认证
    @FieldView(id = R.id.txt_broker_qualifications)
    private TextView txtQualification;
    //资质认证点击控件
    @FieldView(id = R.id.rlayout_broker_qualification)
    private RelativeLayout rlayoutBrokerQualification;
    //个性签名
    @FieldView(id = R.id.edt_broker_sign)
    private EditText edtSign;

    private String avatarUrl;
    private Uri imgUri;
    private String token;
    //昵称
    private String name;
    //性别
    private int sex;
    //公司
    private String company;
    //门店
    private String shop;
    //个性签名
    private String sign;

    //已选服务片区的片区名字
    private String districtName;
    //已选服务小区的小区名字
    private String villageName;
    //表示用户是否有改动自己的个人信息
    private boolean isChange;

    @Override
    protected void initView() {
        token = SettingUtils.get(mContext, "token", "");
        setCenterText(R.string.title_personal_information);
        setRtText(R.string.save);
        //头像
        GlideUtil.getInstance().loadUrl(mContext, UserBean.getInstance(mContext).photo, imgAvatar);
        //昵称
        txtName.setText(UserBean.getInstance(mContext).name);
        //性别
        txtSex.setText(UserBean.getInstance(mContext).sex == 1 ? "男" : "女");
        //联系电话
        txtPhone.setText(UserBean.getInstance(mContext).phone);
        //所属公司
        edtCompany.setText(UserBean.getInstance(mContext).companyName);
        //所属门店
        edtShop.setText(UserBean.getInstance(mContext).store);
        //服务片区
        districtName = UserBean.getInstance(mContext).serviceDistrictName;
        int districtNumber = ParamUtils.isEmpty(districtName) ? 0 : getNumber(districtName.split(","));
        txtDistrictNumber.setText(districtNumber == 0 ? "请选择片区" : "已选择" + districtNumber + "个片区");
        //服务小区
        villageName = UserBean.getInstance(mContext).serviceVillageName;
        int villageNumber = ParamUtils.isEmpty(villageName) ? 0 : getNumber(villageName.split(","));
        txtVillageNumber.setText(villageNumber == 0 ? "请选择小区" : "已选择" + villageNumber + "个小区");
        //个性签名
        edtSign.setText(UserBean.getInstance(mContext).sign);
    }

    @Override
    protected void initData() {
//        创建头像临时存储位置
        try {
            File pictureFileDir = new File(Environment.getExternalStorageDirectory(), "/avatar");
            if (!pictureFileDir.exists()) {
                pictureFileDir.mkdirs();
            }
            File picFile = new File(pictureFileDir, "avatar.jpeg");
            if (!picFile.exists()) {
                picFile.createNewFile();
            }
            imgUri = Uri.fromFile(picFile);
        } catch (Exception e) {
            lemonMessage.sendMessage(R.string.toast_picture_error);
        }
        UserPersonalParam userPersonalParam = new UserPersonalParam();
        userPersonalParam.setToken(token);
        userPersonalParam.setTag(TAG);
        apiManager.getUserDetail(userPersonalParam);
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        hideKeyboard();
        if (isChange) {
            UserBean userBean = UserBean.getInstance(mContext);
            UpdateUserInfoEvent updateUserInfoEvent = new UpdateUserInfoEvent();
            updateUserInfoEvent.setPhoto(userBean.photo);
            updateUserInfoEvent.setName(userBean.name);
            updateUserInfoEvent.setSex(userBean.sex);
            EventUtil.sendEvent(updateUserInfoEvent);
        }
        finish();
    }

    /**
     * 处理返回键的事件
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        toBack();
    }

    /**
     * 保存
     */
    @OnClick(id = R.id.tv_title_rt)
    public void toSave() {
        sign = edtSign.getText().toString();//获取签名
        company = edtCompany.getText().toString();//获取公司
        shop = edtShop.getText().toString();//获取门店
        //更新所属公司、所属门店、个性签名的接口
        UserInfoParam userInfoParam = new UserInfoParam();
        userInfoParam.setToken(token);
        userInfoParam.setCompany(company);//所属公司
        userInfoParam.setStore(shop);//所属门店
        userInfoParam.setSign(sign);//签名
        apiManager.updateUserInfo(userInfoParam);
        hideKeyboard();
        finish();
    }

    public class UploadListener implements ImageUploadUtil.OnUploadFileListener {
        private String url;

        public String getUrl() {
            return url;
        }

        @Override
        public void progress(File srcFile, double percent) {

        }

        @Override
        public void complete(File srcFile, String httpUrl) {
            this.url = httpUrl;
        }

        @Override
        public void error(File srcFile) {

        }
    }

    /**
     * 选取头像
     */
    @OnClick(id = R.id.rlayout_broker_avatar)
    public void editAvatar() {// 编辑头像，从本地相册选取图片作为头像
        CharSequence[] items = {"相册", "相机"};
        new AlertDialog.Builder(this)
                .setTitle("选择图片来源")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == SELECT_PICTURE) {// 跳转图片剪切
                            if (Build.BRAND.equals("Meizu")) {
                                Intent intent = new Intent(Intent.ACTION_PICK, null);
                                intent.setType("image/*");
                                startActivityForResult(intent, SELECT_PICTURE_FOR_MEIZU);
                            } else {
                                CropImageIntent(Intent.ACTION_PICK, SELECT_PICTURE);
                            }
                        } else {
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
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                                    try {
                                        startActivityForResult(intent, SELECT_CAMERA);
                                    } catch (ActivityNotFoundException e) {
                                        lemonMessage.sendMessage(R.string.toast_camera_invalid);
                                    }
                                }
                            }.execute();
                        }
                    }
                }).create().show();
    }

    /**
     * 调用图片剪辑程序
     */
    public void CropImageIntent(String action, int type) {
        Intent intent = new Intent(action, null);
        if (type == SELECT_CAMERA) {//相机
            intent.setDataAndType(imgUri, "image/*");//查看数据为imgUri、类型为image的图片
        } else {
            intent.setType("image/*");//查看类型为image的图片
        }
        intent.putExtra("crop", "true"); //设置图片可裁剪
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 240);//设置裁剪区的宽度
        intent.putExtra("outputY", 240);//设置裁剪区的高度
        intent.putExtra("noFaceDetection", true); // no face detection
        intent.putExtra("scale", true);//设置图片可缩放，并保持笔记
        intent.putExtra("return-data", false);//设置是否将数据保留在Bitmap中返回
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);//指定图片的输出地址
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("circleCrop", false);//设置不是圆形裁剪区域
        startActivityForResult(intent, SELECT_PICTURE);
    }

    /**
     * 调用图片剪辑程序
     */
    public void CropImageIntent(Uri imgUriMeizu) {
        Intent intent = new Intent("com.android.camera.action.CROP", null);
        intent.setDataAndType(imgUriMeizu, "image/*");//查看数据为imgUri、类型为image的图片
        intent.putExtra("crop", "true"); //设置图片可裁剪
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 240);//设置裁剪区的宽度
        intent.putExtra("outputY", 240);//设置裁剪区的高度
        intent.putExtra("noFaceDetection", true); // no face detection
        intent.putExtra("scale", true);//设置图片可缩放，并保持笔记
        intent.putExtra("return-data", false);//设置是否将数据保留在Bitmap中返回
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);//指定图片的输出地址
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("circleCrop", false);//设置不是圆形裁剪区域
        startActivityForResult(intent, SELECT_PICTURE);
    }

    /**
     * 编辑“昵称”
     */
    @OnClick(id = R.id.rlayout_broker_alias)
    public void editAlias() {
        Intent intent = new Intent(this, ModifyAliasActivity.class);
        intent.putExtra("name", txtName.getText());
        intent.putExtra("userType", "1");
        startActivityForResult(intent, REQUEST_MODIFY_ALIAS);
    }

    /**
     * 编辑“性别”
     */
    @OnClick(id = R.id.rlayout_broker_sex)
    public void editSex() {
        ArraySelectWheelDialog sexDialog = null;
        if (sexDialog != null) {
            sexDialog.show();
        } else {
            String[] values = {"男", "女"};
            sexDialog = new ArraySelectWheelDialog(mContext, values);
            sexDialog.show();
            sexDialog.setListener(new ArraySelectWheelDialog.SelectWheelResultListener() {
                @Override
                public void getSelectValue(String time, String value) {
                    txtSex.setText(time);
                    sex = time.equals("男") ? 1 : 0;
                    //调用“更新性别”的接口
                    SexParam sexParam = new SexParam();
                    sexParam.setToken(token);
                    sexParam.setSex(sex);
                    apiManager.updateSex(sexParam);
                }
            });
        }
    }

    /**
     * 编辑“服务的片区”
     */
    @OnClick(id = R.id.rlayout_broker_distrust)
    public void editDistruct() {
        startActivity(new Intent(this, AreasDistrictActivity.class));
    }

    /**
     * 编辑“服务的小区”
     */
    @OnClick(id = R.id.rlayout_broker_village)
    public void editVillage() {
        Intent intent = new Intent(this, AreasVillageActivity.class);
        String[] districtIds = UserBean.getInstance(mContext).serviceDistrict.split(",");
        if (!ParamUtils.isEmpty(districtIds)) {
            intent.putExtra("districtIds", districtIds);
            intent.putExtra("districtNames", UserBean.getInstance(mContext).serviceDistrictName.split(","));
        }
        startActivity(intent);
    }

    /**
     * 实名认证
     */
    @OnClick(id = R.id.rlayout_broker_realname)
    public void editRealname() {
        if (UserBean.getInstance(mContext).smrzState.equals("0") || UserBean.getInstance(mContext).smrzState.equals("3")) {
            startActivityForResult(new Intent(this, RealNameActivity.class), Constant.REQUEST_REAL_NAME);
        } else {
            lemonMessage.sendMessage(txtRealName.getText().toString());
        }
    }

    /**
     * 资质认证
     */
    @OnClick(id = R.id.rlayout_broker_qualification)
    public void editQualification() {
        if (UserBean.getInstance(mContext).zzrzState.equals("0") || UserBean.getInstance(mContext).zzrzState.equals("3")) {
            startActivityForResult(new Intent(this, QualificationActivity.class), Constant.REQUEST_QUALIFICATION);
        } else {
            lemonMessage.sendMessage(txtQualification.getText().toString());
        }
    }

    /**
     * 计算选中片区或小区的数量
     *
     * @param values 已选中片区或小区的集合
     * @return 已选中片区或小区的数量
     */
    private int getNumber(String[] values) {
        int number = 0;
        for (int i = 0; i < values.length; i++) {
            if (!values[i].equals("null")) {
                number++;
            }
        }
        return number;
    }

    /**
     * 获取七牛token并上传照片
     *
     * @param result
     */
    public void onEventMainThread(QiNiuResult result) {
        HashMap data = (HashMap) result.getData();
        if (((QiNiuParam) result.getParam()).getTag().equals(TAG)) {
            final String qiniuToken = (String) data.get("token");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    File file = new File(imgUri.getPath());
                    UploadListener uploadListener = new UploadListener();
                    ImageUploadUtil.OnUploadFile(qiniuToken, file, uploadListener, mContext);
                    avatarUrl = uploadListener.getUrl();
                    //更新头像
                    if (!ParamUtils.isEmpty(avatarUrl)) {
                        AvatarParam avatarParam = new AvatarParam();
                        avatarParam.setToken(token);
                        avatarParam.setPhotoUrl(avatarUrl);
                        apiManager.updatePhoto(avatarParam);
                    }
                }
            }).start();
        }
    }

    /**
     * 头像
     *
     * @param result
     */
    public void onEventMainThread(AvatarResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            isChange = true;
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imgUri));
                imgAvatar.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //更新本地用户的头像
            UserBean.getInstance(mContext).photo = avatarUrl;
            UserBean.updateUserBean(mContext);
            EventUtil.sendEvent(new MomentsTitlePhotoEvent());
            //刷新最新积分
            ToolUtil.updatePoint(mContext, result.getData().getTotalPoint(), result.getData().getGetPoint());
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

    /**
     * 昵称
     *
     * @param result
     */
    public void onEventMainThread(AliasResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            isChange = true;
            txtName.setText(name);
            //更新本地用户的昵称
            UserBean.getInstance(mContext).name = name;
            UserBean.updateUserBean(mContext);
            //刷新最新积分
            ToolUtil.updatePoint(mContext, result.getData().getTotalPoint(), result.getData().getGetPoint());
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

    /**
     * 性别
     *
     * @param result
     */
    public void onEventMainThread(SexResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            isChange = true;
            //更新本地用户的性别
            UserBean.getInstance(mContext).sex = sex;
            UserBean.updateUserBean(mContext);
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

    /**
     * 所属公司、所属门店、个性签名
     *
     * @param result
     */
    public void onEventMainThread(UserInfoResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            //更新本地经纪人信息，包括所属公司、所属门店、个性签名
            UserBean.getInstance(mContext).companyName = company;
            UserBean.getInstance(mContext).store = shop;
            UserBean.getInstance(mContext).sign = sign;
            UserBean.updateUserBean(mContext);

            //EventBus更新经纪人信息，包括所属公司、所属门店
            UpdateBrokerEvent updateBrokerEvent = new UpdateBrokerEvent();
            updateBrokerEvent.setCompany(company);
            updateBrokerEvent.setShop(shop);
            EventUtil.sendEvent(updateBrokerEvent);
            lemonMessage.sendMessage(R.string.toast_save_success);
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

    /**
     * 更新经纪人信息
     * 主要包括实名认证、资质认证（里面包括名片认证）
     *
     * @param result
     */
    public void onEventMainThread(UserPersonalResult result) {
        if (!((UserPersonalParam) result.getParam()).getTag().equals(TAG)) {
            return;
        }
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode()) && !ParamUtils.isNull(result.getData())) {
            //更新本地经纪人信息
            UserBean data = result.getData();
            UserBean.updateUserBean(mContext, data);
            //实名认证
            ToolUtil.setAuthentication(data.smrzState, txtRealName, getResources());
            //资质认证
            ToolUtil.setAuthentication(data.zzrzState, txtQualification, getResources());
        }
    }

    /**
     * 更新经纪人服务信息
     * 主要包括服务的片区的个数、服务的小区的个数
     *
     * @param event
     */
    public void onEventMainThread(UpdateBrokerEvent event) {
        //服务的片区
        if (!ParamUtils.isEmpty(event.getServiceDistrictName())) {
            districtName = event.getServiceDistrictName();
            txtDistrictNumber.setText("已选择" + getNumber(districtName.split(",")) + "个片区");
        }
        //服务的小区
        if (!ParamUtils.isEmpty(event.getServiceVillageName())) {
            villageName = event.getServiceVillageName();
            txtVillageNumber.setText("已选择" + getNumber(villageName.split(",")) + "个小区");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        QiNiuParam qiNiuParam;
        if (resultCode == RESULT_OK) {
            token = SettingUtils.get(mContext, "token", null);
            switch (requestCode) {
                case SELECT_PICTURE://相册
                    qiNiuParam = new QiNiuParam();
                    qiNiuParam.setTag(TAG);
                    apiManager.uploadToken(qiNiuParam);
                    Log.i(TAG, qiNiuParam.toString());
                    break;
                case SELECT_PICTURE_FOR_MEIZU:
                    CropImageIntent(data.getData());
                    break;
                case SELECT_CAMERA://相机
                    qiNiuParam = new QiNiuParam();
                    qiNiuParam.setTag(TAG);
                    apiManager.uploadToken(qiNiuParam);
                    CropImageIntent("com.android.camera.action.CROP", SELECT_CAMERA);
                    break;
                case REQUEST_MODIFY_ALIAS://编辑昵称
                    //更新昵称
                    name = data.getStringExtra("name");
                    AliasParam aliasParam = new AliasParam();
                    aliasParam.setToken(token);
                    aliasParam.setAlise(name);
                    apiManager.updateAlise(aliasParam);
                    break;
                case Constant.REQUEST_REAL_NAME://实名认证
                    txtRealName.setText(R.string.wait_check);
                    rlayoutBrokerRealname.setClickable(false);
                    lemonMessage.sendMessage(R.string.waiting_check);
                    //更新本地经纪人实名认证
                    UserBean.getInstance(mContext).smrzState = "1";
                    UserBean.updateUserBean(mContext);
                    //EventBus更新经纪人实名认证
                    UpdateBrokerEvent updateBrokerEvent = new UpdateBrokerEvent();
                    updateBrokerEvent.setSmrzState("1");
                    EventUtil.sendEvent(updateBrokerEvent);
                    break;
                case Constant.REQUEST_QUALIFICATION://资质认证、名片认证
                    txtQualification.setText(R.string.wait_check);
                    rlayoutBrokerQualification.setClickable(false);
                    lemonMessage.sendMessage(R.string.waiting_check);
                    //更新本地经纪人资质认证、名片认证
                    UserBean.getInstance(mContext).zzrzState = "1";
                    UserBean.updateUserBean(mContext);
                    //EventBus更新经纪人资质认证、名片认证
                    updateBrokerEvent = new UpdateBrokerEvent();
                    updateBrokerEvent.setZzrzState("1");
                    EventUtil.sendEvent(updateBrokerEvent);
                    break;
            }
        }
    }

}
