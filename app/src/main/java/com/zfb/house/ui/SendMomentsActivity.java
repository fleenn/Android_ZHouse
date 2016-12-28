package com.zfb.house.ui;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.emchat.Constant;
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
import com.zfb.house.component.ChosePicSchemaLayout;
import com.zfb.house.component.LoadDialog;
import com.zfb.house.component.PickPhotoLayout;
import com.zfb.house.iml.OnImageChoiceListener;
import com.zfb.house.model.param.QiNiuParam;
import com.zfb.house.model.param.SendMomentsParam;
import com.zfb.house.model.result.QiNiuResult;
import com.zfb.house.model.result.SendMomentsResult;
import com.zfb.house.util.ToolUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Snekey on 2016/5/8.
 */
@Layout(id = R.layout.activity_send_moments)
public class SendMomentsActivity extends LemonActivity implements OnImageChoiceListener {

    private final static int GET_LOCAL_IMAGE_REQUEST = 0x001;// 相册
    private final static int CAPTURE_IMAGE_REQUEST = 0x002;// 拍照

    //    Intent固定传递参数key
    public static final String EXTRA_IMAGE_URLS = "image_urls";
    //    Intent传递已有图片数组
    public static final String EXIST_IMAGE_URLS = "exist_image_urls";
    //    添加照片控件
    @FieldView(id = R.id.pickPhotoLayout)
    private PickPhotoLayout pickPhotoLayout;
    //    编辑内容
    @FieldView(id = R.id.et_edit_content)
    private EditText etEditContent;
    //    选择照片来源控件
    @FieldView(id = R.id.chosePicSchemaLayout)
    private ChosePicSchemaLayout chosePicSchemaLayout;
    //    地图选点后的位置信息
    @FieldView(id = R.id.tv_position)
    private TextView tvPosition;

    private double mLat;
    private double mLng;
    private String mLocation = "";
    private File mImageFile;
    private LoadDialog mLoadDialog;//加载框

    @Override
    protected void initView() {
        setLtText(R.string.cancel);
        setRtText(R.string.title_publish);
        setRtTextColor(R.color.my_orange_two);
        mLoadDialog = new LoadDialog(mContext);
//        初始化控件监听事件
        pickPhotoLayout.setOnAddImageListener(new PickPhotoLayout.OnAddImageListener() {
            @Override
            public void OnRequestAdd() {
                hideKeyboard();
                if (chosePicSchemaLayout != null) {
                    chosePicSchemaLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void initData() {
        if (SettingUtils.get(mContext, Constant.IS_CUSTOMIZE, false)) {
            mLat = SettingUtils.get(mContext, Constant.CUSTOMIZE_LAT, Constant.DEFAULT_LAT);
            mLng = SettingUtils.get(mContext, Constant.CUSTOMIZE_LNG, Constant.DEFAULT_LNG);
        } else {
            mLat = SettingUtils.get(mContext, Constant.LAT, Constant.DEFAULT_LAT);
            mLng = SettingUtils.get(mContext, Constant.LNG, Constant.DEFAULT_LNG);
        }
    }

    /**
     * 取消
     */
    @OnClick(id = R.id.tv_title_lt)
    public void toBack() {
        hideKeyboard();
        finish();
    }


    /**
     * 发布房友圈
     */
    @OnClick(id = R.id.tv_title_rt)
    public void toPublish() {
        if (getSelectedImages().size() == 0 && ParamUtils.isEmpty(etEditContent.getText().toString())) {
            lemonMessage.sendMessage(R.string.toast_content_empty);
            return;
        }
        QiNiuParam qiNiuParam = new QiNiuParam();
        apiManager.uploadToken(qiNiuParam);
        mTvTitleRt.setClickable(false);
    }

    /**
     * 跳转地图界面
     */
    @OnClick(id = R.id.rlayout_location)
    public void toLocated() {
        Intent intent = new Intent(mContext, MapActivity.class);
        startActivityForResult(intent, Constant.MAP_REQUEST);
    }

    /**
     * 获取本地图片
     */
    public boolean getLocalImage() {
        // TODO: 2016/5/11 内存卡余量判断
//        if (SDCardUtils.isSDCardSapceForWriteWithTip(this,
//                MultiCard.TYPE_IMAGE, 0, true)) {
//            lemonMessage.sendMessage("内存卡存储空间不足");
//            return false;
//        }
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
        if (StorageUtil.isSDCardSapceForWriteWithTip(this,
                MultiCard.TYPE_IMAGE, 0, true)) {
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
     * 根据URI获取文件路径
     *
     * @param context
     * @param uri
     * @return
     */
    public String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
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
            mLoadDialog.show();
        }

        @Override
        public void complete(File srcFile, String httpUrl) {
            httpUrls += "," + httpUrl;

        }

        @Override
        public void error(File srcFile) {
        }
    }

    /**
     * 获取七牛token后异步上传，获取返回值后调用服务器上传接口
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
                MyOnUploadFileListener listener = new MyOnUploadFileListener();
                ImageUploadUtil.OnUploadMultiFile(token, files, listener, mContext);
                String photoStr = listener.getHttpUrls();
                String userToken = SettingUtils.get(mContext, "token", null);
                String content = etEditContent.getText().toString();
                SendMomentsParam sendMomentsParam = new SendMomentsParam();
                sendMomentsParam.setToken(userToken);
                sendMomentsParam.setContent(content);
                sendMomentsParam.setPhoto(photoStr);
                sendMomentsParam.setLat(mLat + "");
                sendMomentsParam.setLng(mLng + "");
                sendMomentsParam.setLocation(mLocation);
                apiManager.saveHouseElite(sendMomentsParam);
            }

        }).start();
    }

    /**
     * 发送房友圈返回值
     *
     * @param result
     */
    public void onEventMainThread(SendMomentsResult result) {
        mLoadDialog.dismiss();
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            ToolUtil.updatePoint(mContext, result.getData().getTotalPoint(), result.getData().getGetPoint(), R.string.toast_send_success);
            setResult(RESULT_OK);
            hideKeyboard();
            finish();
        } else {
            lemonMessage.sendMessage("发送失败");
            mTvTitleRt.setClickable(true);
        }
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
                        if (!(filePath.endsWith(".jpg")
                                || filePath.endsWith(".jpeg")
                                || filePath.endsWith(".png")
                                || filePath.endsWith(".bmp")
                                || filePath.endsWith(".gif") || filePath
                                .startsWith("/external/images"))) {
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
            case CAPTURE_IMAGE_REQUEST:
                if (mImageFile == null || !mImageFile.exists()) {// 有可能不存在图片
                    // fail
                    return;
                }
                // N930拍照取消也产生字节为0的文件
                if (mImageFile.length() <= 0) {
                    mImageFile.delete();
                    // fail
                    return;
                }
                mImageFile = MultiMediaUtil.scaleImage(this, mImageFile);
                //mDynImageLayout.addImageView(mImageFile.getAbsolutePath());
                onAddImageView(mImageFile.getAbsolutePath());
                break;
            case Constant.MAP_REQUEST:
                if (resultCode == RESULT_OK) {
                    mLat = data.getDoubleExtra("lat", 0.0);
                    mLng = data.getDoubleExtra("lng", 0.0);
                    if (!data.getStringExtra("location").trim().equals("")) {
                        mLocation = data.getStringExtra("location");
                        tvPosition.setText(mLocation);
                    }
                }
                break;
            default:
                break;
        }
    }

}
