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
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.event.MomentsTitlePhotoEvent;
import com.lemon.event.UpdateUserInfoEvent;
import com.lemon.model.StatusCode;
import com.lemon.util.EventUtil;
import com.lemon.util.GlideUtil;
import com.lemon.util.ImageUploadUtil;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.lemon.util.sdCard.FileSuffix;
import com.lemon.util.sdCard.StorageUtil;
import com.zfb.house.R;
import com.zfb.house.component.ArraySelectWheelDialog;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.model.param.AliasParam;
import com.zfb.house.model.param.AvatarParam;
import com.zfb.house.model.param.QiNiuParam;
import com.zfb.house.model.param.SexParam;
import com.zfb.house.model.result.AliasResult;
import com.zfb.house.model.result.AvatarResult;
import com.zfb.house.model.result.QiNiuResult;
import com.zfb.house.model.result.SexResult;
import com.zfb.house.util.ToolUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

/**
 * 我的（用户）->个人信息
 * Created by Administrator on 2016/5/25.
 */
@Layout(id = R.layout.activity_mine_user_personal)
public class UserDataActivity extends LemonActivity {

    private static final String TAG = "UserDataActivity";
    //编辑昵称的请求码
    private final int REQUEST_MODIFY_ALIAS = 0x2;
    private final int SELECT_PICTURE_FOR_MEIZU = 7;

    //相册的标记
    private final int SELECT_PICTURE = 0;
    //相机的标记
    private final int SELECT_CAMERA = 1;

    //头像
    @FieldView(id = R.id.img_user_avatar)
    private ImageView imgAvatar;
    //昵称
    @FieldView(id = R.id.txt_user_name)
    private TextView txtName;
    //性别
    @FieldView(id = R.id.txt_user_sex)
    private TextView txtSex;
    //联系电话
    @FieldView(id = R.id.txt_user_phone)
    private TextView txtPhone;

    private String avatarUrl;
    private Uri imgUri;
    private String token;
    //头像地址（Http开头的）
    private String photo;
    //昵称
    private String name;
    //性别
    private int sex;
    //表示用户是否有改动自己的个人信息
    private boolean isChange;

    /**
     * 从本地获取信息初始化界面
     */
    @Override
    protected void initView() {
        token = SettingUtils.get(mContext, "token", null);
        setCenterText(R.string.title_personal_information);

        //头像
        photo = UserBean.getInstance(mContext).photo;
        GlideUtil.getInstance().loadUrl(mContext, photo, imgAvatar);
        //昵称
        txtName.setText(UserBean.getInstance(mContext).name);
        //性别
        txtSex.setText(UserBean.getInstance(mContext).sex == 1 ? "男" : "女");
        //联系电话
        txtPhone.setText(UserBean.getInstance(mContext).phone);
    }

    @Override
    protected void initData() {
//        创建头像临时存储位置
        try {
            String fileName = "avatar.jpeg" + FileSuffix.JPEG;
            File imageFile = new File(StorageUtil.getWritePathIgnoreError(mContext, fileName));
            imgUri = Uri.fromFile(imageFile);
        } catch (Exception e) {
            lemonMessage.sendMessage(R.string.toast_picture_error);
        }
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
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
    @OnClick(id = R.id.rlayout_user_avatar)
    public void editAvatar() {// 编辑头像，从本地相册选取图片作为头像
        CharSequence[] items = {"相册", "相机"};
        new AlertDialog.Builder(this)
                .setTitle("选择图片来源")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == SELECT_PICTURE) {
//                            跳转图片剪切
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
                })
                .create().show();
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
        intent.putExtra("outputX", 240);
        intent.putExtra("outputY", 240);
        intent.putExtra("noFaceDetection", true); // no face detection
        intent.putExtra("scale", true);//设置图片可缩放
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);//指定图片的输出地址
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("circleCrop", false);
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
     * 编辑昵称
     */
    @OnClick(id = R.id.rlayout_user_alias)
    public void editAlias() {
        Intent intent = new Intent(this, ModifyAliasActivity.class);
        intent.putExtra("name", txtName.getText().toString().trim());
        intent.putExtra("userType", "0");
        startActivityForResult(intent, REQUEST_MODIFY_ALIAS);
    }

    /**
     * 编辑性别
     */
    @OnClick(id = R.id.rlayout_user_sex)
    public void editSex() {
        ArraySelectWheelDialog sexDialog = null;
        if (null != sexDialog) {
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
                    SexParam sexParam = new SexParam();
                    sexParam.setToken(token);
                    sexParam.setSex(sex);
                    sexParam.setShowDialog(true);
                    apiManager.updateSex(sexParam);
                }
            });
        }
    }

    /**
     * 获取七牛token并上传照片
     *
     * @param result
     */
    public void onEventMainThread(QiNiuResult result) {
        if (((QiNiuParam) result.getParam()).getTag().equals(TAG)) {
            HashMap data = (HashMap) result.getData();
            final String qiniuToken = (String) data.get("token");
            Log.i("linwb", "url = " + imgUri.getPath());
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
            //更新本地用户头像
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
            //更新本地用户昵称
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
            //更新本地用户性别
            UserBean.getInstance(mContext).sex = sex;
            UserBean.updateUserBean(mContext);
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            token = SettingUtils.get(mContext, "token", null);
            switch (requestCode) {
                case SELECT_PICTURE://相册
                    QiNiuParam qiNiuParam = new QiNiuParam();
                    qiNiuParam.setTag(TAG);
                    apiManager.uploadToken(qiNiuParam);
                    break;
                case SELECT_CAMERA://相机
                    CropImageIntent("com.android.camera.action.CROP", SELECT_CAMERA);
                    break;
                case SELECT_PICTURE_FOR_MEIZU:
                    CropImageIntent(data.getData());
                    break;
                case REQUEST_MODIFY_ALIAS://编辑昵称
                    //更新昵称
                    name = data.getStringExtra("name");
                    AliasParam aliasParam = new AliasParam();
                    aliasParam.setToken(token);
                    aliasParam.setAlise(name);
                    apiManager.updateAlise(aliasParam);
                    break;
            }
        }
    }

}

