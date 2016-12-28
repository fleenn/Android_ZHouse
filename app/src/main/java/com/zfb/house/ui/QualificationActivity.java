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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
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
import com.zfb.house.component.ArraySelectWheelDialog;
import com.zfb.house.model.bean.QualificationBody;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.model.param.QiNiuParam;
import com.zfb.house.model.param.QualificationParam;
import com.zfb.house.model.result.QiNiuResult;
import com.zfb.house.model.result.QualificationResult;
import com.zfb.house.util.ImageUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

/**
 * 我的 -> 经纪人个人信息 -> 资质认证
 * Created by Administrator on 2016/6/17.
 */
@Layout(id = R.layout.activity_qualification)
public class QualificationActivity extends LemonActivity {

    private final String TAG = "QualificationActivity";
    //七牛token
    private String qiniuToken;
    //相册的标记
    private final int SELECT_PICTURE = 0;
    //相机的标记
    private final int SELECT_CAMERA = 1;
    //标记用户是选择要上传哪个地方的照片，相关证件——1，个人名片——2
    private int photo_id = 0;

    //证件号码
    @FieldView(id = R.id.edt_broker_certificate)
    private EditText edtId;
    //从业年限
    @FieldView(id = R.id.edt_broker_year)
    private TextView txtYear;
    //相关证件的照片
    @FieldView(id = R.id.img_qualification)
    private ImageView imgQualification;
    //个人名片的照片
    @FieldView(id = R.id.img_business_card)
    private ImageView imgBusinessCard;
    //证件类型
    @FieldView(id = R.id.rbt_group_type)
    private RadioGroup groupType;

    private Uri qualificationUri, businessCardUri;
    private File qualificationFile, businessCardFile;
    private String qualificationNo, year, qualificationUrl, qualificationType, businessCardUrl;

    @Override
    protected void initView() {
        setCenterText(R.string.title_qualification_authentication);
        setRtText(R.string.save);
    }

    @Override
    protected void initData() {
        try {
            File pictureFileDir = new File(Environment.getExternalStorageDirectory(), "/brokerQualification");
            if (!pictureFileDir.exists()) {
                pictureFileDir.mkdir();
            }
            //相关证件
            File qualificationFile = new File(pictureFileDir, "qualification.jpeg");
            if (!qualificationFile.exists()) {
                qualificationFile.createNewFile();
            }
            qualificationUri = Uri.fromFile(qualificationFile);
            //个人名片
            File businessCardFile = new File(pictureFileDir, "businessCard.jpeg");
            if (!businessCardFile.exists()) {
                businessCardFile.createNewFile();
            }
            businessCardUri = Uri.fromFile(businessCardFile);
        } catch (Exception e) {
            lemonMessage.sendMessage(R.string.toast_picture_error);
        }

        groupType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) findViewById(group.getCheckedRadioButtonId());
                qualificationType = radioButton.getText().toString();
            }
        });
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        hideKeyboard();
        finish();
    }

    /**
     * 保存
     */
    @OnClick(id = R.id.tv_title_rt)
    public void toSave() {
        qualificationNo = edtId.getText().toString().trim();
        if (ParamUtils.isEmpty(qualificationNo)) {
            lemonMessage.sendMessage("请输入证件号码");
            return;
        }

        year = txtYear.getText().toString();
        if (year.equals("请选择从业年限")) {
            lemonMessage.sendMessage("请选择从业年限");
            return;
        }

        if (ParamUtils.isEmpty(qualificationType)) {
            lemonMessage.sendMessage("请选择证件类型");
            return;
        }

//        if (ParamUtils.isEmpty(qualificationUrl) || ParamUtils.isEmpty(businessCardUrl)) {
//            lemonMessage.sendMessage("上传失败");
//            return;
//        }

        //调用“资质认证”接口
        String token = SettingUtils.get(mContext, "token", "");
        final QualificationBody qualificationBody = new QualificationBody();
        qualificationBody.qualificationNo = qualificationNo;
        qualificationBody.year = year;
        qualificationBody.qualificationUrl = qualificationUrl;
        qualificationBody.qualificationType = qualificationType;
        qualificationBody.businessCardUrl = businessCardUrl;
        QualificationParam qualificationParam = new QualificationParam();
        qualificationParam.setToken(token);
        qualificationParam.setBody(new Gson().toJson(qualificationBody));
        apiManager.qualificationAuthentication(qualificationParam);
    }

    /**
     * 从业年限
     */
    @OnClick(id = R.id.rlayout_year)
    public void selectYear() {
        ArraySelectWheelDialog sexDialog = null;
        if (sexDialog != null) {
            sexDialog.show();
        } else {
            String[] values = {"0年", "1年", "2年", "3年", "4年", "5年", "6年", "7年", "8年", "9年", "10年", "11年", "12年", "12年", "14年", "15年", "16年", "17年", "18年", "19年", "20年", "21年", "22年",
                    "23年", "24年", "25年", "26年", "27年", "28年", "29年", "30年", "31年", "32年", "33年", "34年", "35年", "36年", "37年", "38年", "39年", "40年", "41年", "42年", "43年", "44年",
                    "45年", "46年", "47年", "48年", "49年", "50年"};
            sexDialog = new ArraySelectWheelDialog(mContext, values);
            sexDialog.show();
            sexDialog.setListener(new ArraySelectWheelDialog.SelectWheelResultListener() {
                @Override
                public void getSelectValue(String time, String value) {
                    txtYear.setText(time);
                }
            });
        }
    }

    /**
     * 上传相关证件的照片
     */
    @OnClick(id = R.id.img_qualification)
    public void addQualification() {
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
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, qualificationUri);
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
     * 上传个人名片的照片
     */
    @OnClick(id = R.id.img_business_card)
    public void addBusinessCard() {
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
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, businessCardUri);
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
     * 获取七牛token
     *
     * @param result
     */
    public void onEventMainThread(QiNiuResult result) {
        if (((QiNiuParam) result.getParam()).getTag().equals(TAG)) {
            HashMap data = (HashMap) result.getData();
            qiniuToken = (String) data.get("token");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (photo_id == 1) {//真实头像
                        UploadListener listener = new UploadListener();
                        ImageUploadUtil.OnUploadFile(qiniuToken, qualificationFile, listener, mContext);
                        qualificationUrl = listener.getUrl();
                    } else if (photo_id == 2) {//身份证照
                        UploadListener listener = new UploadListener();
                        ImageUploadUtil.OnUploadFile(qiniuToken, businessCardFile, listener, mContext);
                        businessCardUrl = listener.getUrl();
                    }
                }
            }).start();
        }
    }

    /**
     * 保存“资质认证”信息
     *
     * @param result
     */
    public void onEventMainThread(QualificationResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            lemonMessage.sendMessage("保存成功");
            UserBean userBean = UserBean.getInstance(mContext);
            userBean.zzrzState = "1";
            UserBean.updateUserBean(mContext, userBean);
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
                    if (photo_id == 1) {//相关证件
                        Glide.with(mContext).load(data.getData()).into(imgQualification);
                        String realFilePath = ImageUtil.getRealFilePath(mContext, data.getData());
                        qualificationFile = new File(realFilePath);
                    } else if (photo_id == 2) {//个人名片
                        Glide.with(mContext).load(data.getData()).into(imgBusinessCard);
                        String realFilePath = ImageUtil.getRealFilePath(mContext, data.getData());
                        businessCardFile = new File(realFilePath);
                    }
                    break;
                case SELECT_CAMERA://相机
                    apiManager.uploadToken(qiNiuParam);
                    int targetHeight = ScreenUtil.dip2px(mContext, (float) 116.7);
                    try {
                        if (photo_id == 1) {//相关证件
                            Bitmap bitmapFromStream = BitmapUtil.getBitmapFromStream(getContentResolver().openInputStream(qualificationUri), targetHeight, targetHeight);
                            imgQualification.setImageBitmap(bitmapFromStream);
                        } else if (photo_id == 2) {//个人名片
                            Bitmap bitmapFromStream = BitmapUtil.getBitmapFromStream(getContentResolver().openInputStream(businessCardUri), targetHeight, targetHeight);
                            imgBusinessCard.setImageBitmap(bitmapFromStream);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

}
