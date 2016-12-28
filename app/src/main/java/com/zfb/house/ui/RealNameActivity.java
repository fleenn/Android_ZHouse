package com.zfb.house.ui;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.model.StatusCode;
import com.lemon.util.BitmapUtil;
import com.lemon.util.ImageUploadUtil;
import com.lemon.util.ParamUtils;
import com.lemon.util.ScreenUtil;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.component.LoadDialog;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.model.param.QiNiuParam;
import com.zfb.house.model.param.RealNameParam;
import com.zfb.house.model.result.QiNiuResult;
import com.zfb.house.model.result.RealNameResult;
import com.zfb.house.util.ImageUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

/**
 * 我的 -> 经纪人个人信息 -> 实名认证
 * Created by Administrator on 2016/6/17.
 */
@Layout(id = R.layout.activity_realname)
public class RealNameActivity extends LemonActivity {

    private static final String TAG = "RealNameActivity";
    //相册的标记
    private final int SELECT_PICTURE = 0;
    //相机的标记
    private final int SELECT_CAMERA = 1;
    //标记用户是选择要上传哪个地方的照片，真实头像——1，身份证照——2
    private int photo_id = 0;

    //真实姓名
    @FieldView(id = R.id.edt_broker_relname)
    private EditText edtName;
    //身份证号
    @FieldView(id = R.id.edt_broker_id)
    private EditText edtId;
    //真实头像的照片
    @FieldView(id = R.id.img_portrait)
    private ImageView imgPortrait;
    //身份证件的照片
    @FieldView(id = R.id.img_id)
    private ImageView imgId;

    //    通过拍照获取的uri
    private Uri portraitUri, idUri;
    //    需要上传到七牛的文件
    private File portraitFile, idFile;
    //    需要传到服务器的图片url
    private String portraitUrl, idUrl;
    private LoadDialog mLoadDialog;//加载框

    @Override
    protected void initView() {
        setCenterText(R.string.title_realname_authentication);
        setRtText(R.string.save);
        mLoadDialog = new LoadDialog(mContext);
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        hideKeyboard();
        finish();
    }

    @Override
    protected void initData() {
        try {
            File pictureFileDir = new File(Environment.getExternalStorageDirectory(), "/broker");
            if (!pictureFileDir.exists()) {
                pictureFileDir.mkdir();
            }
            //真实头像
            File portraitFile = new File(pictureFileDir, "realPhoto.jpeg");
            if (!portraitFile.exists()) {
                portraitFile.createNewFile();
            }
            portraitUri = Uri.fromFile(portraitFile);
            //身份证照
            File idFile = new File(pictureFileDir, "realId.jpg");
            if (!idFile.exists()) {
                idFile.createNewFile();
            }
            idUri = Uri.fromFile(idFile);
        } catch (Exception e) {
            lemonMessage.sendMessage(R.string.toast_picture_error);
        }
    }

    /**
     * 保存
     */
    @OnClick(id = R.id.tv_title_rt)
    public void toSave() {
        String realName = edtName.getText().toString().trim();
        if (ParamUtils.isEmpty(realName)) {
            lemonMessage.sendMessage("请输入真实姓名！");
            return;
        }

        String realId = edtId.getText().toString().trim();
        if (ParamUtils.isEmpty(realId)) {
            lemonMessage.sendMessage("请输入身份证号！");
            return;
        }

        if (ParamUtils.isEmpty(portraitUrl) || ParamUtils.isEmpty(idUrl)) {
            lemonMessage.sendMessage("上传失败");
            return;
        }

        //调用“实名认证”接口
        String token = SettingUtils.get(mContext, "token", null);
        RealNameParam realNameParam = new RealNameParam();
        realNameParam.setToken(token);
        realNameParam.setRealName(realName);
        realNameParam.setIdentityNo(realId);
        realNameParam.setPhotoUrl(portraitUrl);
        realNameParam.setIdentityUrl(idUrl);
        apiManager.realNameAuthentication(realNameParam);
    }

    /**
     * 上传真实头像
     */
    @OnClick(id = R.id.img_portrait)
    public void addPoritraitImg() {
        photo_id = 1;
        CharSequence[] items = {"相册", "相机"};
        new AlertDialog.Builder(this)
                .setTitle("选择图片来源")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == SELECT_PICTURE) {
                            Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
                            albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            startActivityForResult(albumIntent, SELECT_PICTURE);
                        } else {
                            new AsyncTask<String, Integer, Boolean>() {
                                protected void onPostExecute() {
                                    lemonMessage.sendMessage(R.string.toast_open_camera);
                                }

                                @Override
                                protected Boolean doInBackground(String... params) {
                                    return true;
                                }

                                @Override
                                protected void onPostExecute(Boolean aBoolean) {
                                    super.onPostExecute(aBoolean);
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    try {
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, portraitUri);
                                        startActivityForResult(intent, SELECT_CAMERA);
                                    } catch (ActivityNotFoundException e) {
                                        e.printStackTrace();
                                        lemonMessage.sendMessage(R.string.toast_camera_invalid);
                                    }
                                }
                            }.execute();
                        }
                    }
                }).create().show();
    }

    /**
     * 上传身份证照
     */
    @OnClick(id = R.id.img_id)
    public void addIdImg() {
        photo_id = 2;
        CharSequence[] items = {"相册", "相机"};
        new AlertDialog.Builder(this)
                .setTitle("选择图片来源")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == SELECT_PICTURE) {
                            Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
                            albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            startActivityForResult(albumIntent, SELECT_PICTURE);
                        } else {
                            new AsyncTask<String, Integer, Boolean>() {
                                protected void onPostExecute() {
                                    lemonMessage.sendMessage(R.string.toast_open_camera);
                                }

                                @Override
                                protected Boolean doInBackground(String... params) {
                                    return true;
                                }

                                @Override
                                protected void onPostExecute(Boolean aBoolean) {
                                    super.onPostExecute(aBoolean);
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    try {
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, idUri);
                                        startActivityForResult(intent, SELECT_CAMERA);
                                    } catch (ActivityNotFoundException e) {
                                        e.printStackTrace();
                                        lemonMessage.sendMessage(R.string.toast_camera_invalid);
                                    }
                                }
                            }.execute();
                        }
                    }
                }).create().show();
    }

    public class UploadListener implements ImageUploadUtil.OnUploadFileListener {
        private String url;

        public String getUrl() {
            return url;
        }

        @Override
        public void progress(File srcFile, double percent) {
            mLoadDialog.show();
        }

        @Override
        public void complete(File srcFile, String httpUrl) {
            mLoadDialog.dismiss();
            this.url = httpUrl;
        }

        @Override
        public void error(File srcFile) {

        }
    }

    public void onEventMainThread(QiNiuResult result) {
        if (result.getErrorCode().equals("200")) {
            if (((QiNiuParam) result.getParam()).getTag().equals(TAG)) {
                HashMap data = (HashMap) result.getData();
                final String qiniuToken = (String) data.get("token");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (photo_id == 1) {//真实头像
                            UploadListener listener = new UploadListener();
                            ImageUploadUtil.OnUploadFile(qiniuToken, portraitFile, listener, mContext);
                            portraitUrl = listener.getUrl();
                        } else if (photo_id == 2) {//身份证照
                            UploadListener listener = new UploadListener();
                            ImageUploadUtil.OnUploadFile(qiniuToken, idFile, listener, mContext);
                            idUrl = listener.getUrl();
                        }
                    }
                }).start();
            }
        }
    }

    /**
     * 保存“实名认证”信息
     *
     * @param result
     */
    public void onEventMainThread(RealNameResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            lemonMessage.sendMessage("保存成功");
            UserBean userBean = UserBean.getInstance(mContext);
            userBean.smrzState = "1";
            UserBean.updateUserBean(mContext,userBean);
            setResult(RESULT_OK);
            finish();
        } else {
            lemonMessage.sendMessage(result.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        QiNiuParam qiNiuParam = new QiNiuParam();
        qiNiuParam.setTag(TAG);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_PICTURE://相册
                    apiManager.uploadToken(qiNiuParam);
                    if (photo_id == 1) {//真实头像
                        Glide.with(mContext).load(data.getData()).into(imgPortrait);
                        String realFilePath = ImageUtil.getRealFilePath(mContext, data.getData());
                        portraitFile = new File(realFilePath);
                    } else if (photo_id == 2) {//身份证照
                        Glide.with(mContext).load(data.getData()).into(imgId);
                        String realFilePath = ImageUtil.getRealFilePath(mContext, data.getData());
                        idFile = new File(realFilePath);
                    }
                    break;
                case SELECT_CAMERA://相机
                    apiManager.uploadToken(qiNiuParam);
                    int targetHeight = ScreenUtil.dip2px(mContext, (float) 116.7);
                    try {
                        if (photo_id == 1) {//真实头像
                            Bitmap bitmap = BitmapUtil.getBitmapFromStream(getContentResolver().openInputStream(portraitUri), targetHeight, targetHeight);
                            portraitFile = new File(portraitUri.getPath());
                            imgPortrait.setImageBitmap(bitmap);
                        } else if (photo_id == 2) {//身份证照
                            Bitmap bitmap = BitmapUtil.getBitmapFromStream(getContentResolver().openInputStream(idUri), targetHeight, targetHeight);
                            idFile = new File(portraitUri.getPath());
                            imgId.setImageBitmap(bitmap);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

}
