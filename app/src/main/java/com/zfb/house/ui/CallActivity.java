package com.zfb.house.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.PowerManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.emchat.Constant;
import com.baidu.mapapi.model.LatLng;
import com.easemob.EMError;
import com.easemob.util.VoiceRecorder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lemon.LemonActivity;
import com.lemon.LemonLocation;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.event.LocChangeEvent;
import com.lemon.model.StatusCode;
import com.lemon.util.DateUtils;
import com.lemon.util.EventUtil;
import com.lemon.util.ImageUploadUtil;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.lemon.util.palyer.PlayerEngineImpl;
import com.zfb.house.R;
import com.zfb.house.adapter.ChatAdapter;
import com.zfb.house.component.PopupCallSearch;
import com.zfb.house.emchat.CommonUtils;
import com.zfb.house.emchat.PasteEditText;
import com.zfb.house.emchat.VoicePlayClickListener;
import com.zfb.house.model.bean.CallCommission;
import com.zfb.house.model.bean.ChatMessage;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.model.param.CallParam;
import com.zfb.house.model.param.QiNiuParam;
import com.zfb.house.model.result.CallResult;
import com.zfb.house.model.result.QiNiuResult;
import com.zfb.house.util.ToolUtil;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 一键发布
 * Created by Administrator on 2016/4/24.
 */
@Layout(id = R.layout.activity_call)
public class CallActivity extends LemonActivity implements View.OnClickListener {

    public static final String Tag = "CallActivity";

    //选择专家类型
    @FieldView(id = R.id.tv_broker)
    private TextView tvBroker;

    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        hideKeyboard();
        finish();
    }

    @OnClick(id = R.id.rlayout_title_rt_img)
    public void toLocate() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivityForResult(intent, Constant.MAP_REQUEST);
    }

    @FieldView(id = R.id.llayout_option)
    private LinearLayout llayoutOption;

    @OnClick(id = R.id.llayout_select)
    public void toShowPopup() {
        setLeftTextStyle(true);
        popupCallSearch.show();
    }

    @OnClick(id = R.id.tv_brokerage)
    public void toShowPopupAnother() {
        setLeftTextStyle(true);
        popupCallSearch.show();
    }

    /**
     * 设置左边文字“选择专家类型”的样式
     *
     * @param flag
     */
    private void setLeftTextStyle(boolean flag) {
        Resources resources = getResources();
        if (flag) {
            tvBroker.setTextColor(resources.getColor(R.color.my_orange_one));
            tvBroker.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.broker), null, resources.getDrawable(R.drawable.down_arrow_press), null);
        } else {
            tvBroker.setTextColor(resources.getColor(R.color.my_gray_one));
            tvBroker.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.broker), null, resources.getDrawable(R.drawable.down_arrow), null);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hideKeyboard();
    }

    private View recordingContainer;
    private ImageView micImage;
    private TextView recordingHint;
    private ListView listView;
    private PasteEditText mEditTextContent;
    private View buttonSetModeKeyboard;
    private View buttonSetModeVoice;
    private View buttonSend;
    private View buttonPressToSpeak;
    private InputMethodManager manager;
    private Drawable[] micImages;
    // 给谁发送消息
    private VoiceRecorder voiceRecorder;
    private RelativeLayout edittext_layout;
    private PowerManager.WakeLock wakeLock;
    private ChatAdapter chatAdapter;
    private String mBrokerage = "mt";
    private PopupCallSearch popupCallSearch;
    private TextView tvBrokerType;
    private TextView tvBrokerage;
    private double mLat;
    private double mLng;
    private String mRequireType = "租售专家";
    private String mRequireTypeId = "3";
    private String userId;

    @Override
    protected void initView() {
        setCenterText(R.string.title_call);
        setRtText(R.string.label_position);
        setRtImg(R.drawable.near_position);
        findViewById(R.id.btn_set_mode_voice).setOnClickListener(this);
        findViewById(R.id.btn_set_mode_keyboard).setOnClickListener(this);
        checkPermission(Constant.PERMISSION_CODE_RADIO, android.Manifest.permission.RECORD_AUDIO);
        userId = UserBean.getInstance(mContext).id;
        popupCallSearch = new PopupCallSearch(this, llayoutOption);
        popupCallSearch.setChoicePriceListener(new PopupCallSearch.ChoicePriceListener() {
            @Override
            public void getBrokerage(CallCommission callCommission) {
                mBrokerage = callCommission.getValue();
                tvBrokerage.setText(callCommission.getName());
                popupCallSearch.dismiss();
                setLeftTextStyle(false);
                tvBrokerType.setText(mRequireType);
            }

            @Override
            public void getRequireType(String requireType, String word) {
                mRequireTypeId = requireType;
                mRequireType = word;
                if (word.equals("租售专家")) {
                    tvBrokerType.setText(word);
                    tvBrokerage.setText("不限");
                    mBrokerage = "mt";
                    popupCallSearch.dismiss();
                    setLeftTextStyle(false);
                }
            }
        });
        recordingContainer = findViewById(R.id.recording_container);
        micImage = (ImageView) findViewById(R.id.mic_image);
        recordingHint = (TextView) findViewById(R.id.recording_hint);
        listView = (ListView) findViewById(R.id.list);
        mEditTextContent = (PasteEditText) findViewById(R.id.et_sendmessage);
        buttonSetModeKeyboard = findViewById(R.id.btn_set_mode_keyboard);
        edittext_layout = (RelativeLayout) findViewById(R.id.edittext_layout);
        buttonSetModeVoice = findViewById(R.id.btn_set_mode_voice);
        buttonSend = findViewById(R.id.btn_send);
        buttonSend.setOnClickListener(this);
        buttonPressToSpeak = findViewById(R.id.btn_press_to_speak);
        edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);

        // 动画资源文件,用于录制语音时
        micImages = new Drawable[]{getResources().getDrawable(R.drawable.record_animate_01),
                getResources().getDrawable(R.drawable.record_animate_02),
                getResources().getDrawable(R.drawable.record_animate_03),
                getResources().getDrawable(R.drawable.record_animate_04),
                getResources().getDrawable(R.drawable.record_animate_05),
                getResources().getDrawable(R.drawable.record_animate_06),
                getResources().getDrawable(R.drawable.record_animate_07),
                getResources().getDrawable(R.drawable.record_animate_08),
                getResources().getDrawable(R.drawable.record_animate_09),
                getResources().getDrawable(R.drawable.record_animate_10),
                getResources().getDrawable(R.drawable.record_animate_11),
                getResources().getDrawable(R.drawable.record_animate_12),
                getResources().getDrawable(R.drawable.record_animate_13),
                getResources().getDrawable(R.drawable.record_animate_14)};


        listView = (ListView) findViewById(R.id.list);
        View headView = getLayoutInflater().inflate(R.layout.nearby_header, null);
        tvBrokerType = (TextView) headView.findViewById(R.id.tv_broker_type);
        tvBrokerage = (TextView) headView.findViewById(R.id.tv_brokerage);
        listView.addHeaderView(headView);
        chatAdapter = new ChatAdapter(mContext);
        listView.setAdapter(chatAdapter);
        //判断间隔时间，如果上一次的时间为0，说明超过15分钟或者第一次进入该页面，那么间隔时间为默认值0
        double interval = 0.0;
        if (SettingUtils.get(mContext, userId + Constant.FIRST_SEND_TIME, 0L) != 0L) {
            interval = DateUtils.compare(new Date().getTime(), SettingUtils.get(mContext, userId + Constant.FIRST_SEND_TIME, 0L));
        }
        //间隔时间超过15分钟，清空上一次发布的时间以及保存的发布信息
        if (interval >= 15.0) {
            SettingUtils.set(mContext, userId + Constant.FIRST_SEND_TIME, 0L);
            SettingUtils.set(mContext, userId + Constant.CALL_HISTORY, "");
        } else {
            //不超过15分钟的情况，载入发布信息刷新列表。
            Type type = new TypeToken<List<ChatMessage>>() {
            }.getType();
            List<ChatMessage> data = new Gson().fromJson(SettingUtils.get(mContext, userId + Constant.CALL_HISTORY, ""), type);
            if (!ParamUtils.isEmpty(data)) {
                chatAdapter.setMsgList(data);
                chatAdapter.notifyDataSetChanged();
            }
        }
        edittext_layout.requestFocus();
        voiceRecorder = new VoiceRecorder(micImageHandler);
        buttonPressToSpeak.setOnTouchListener(new PressToSpeakListen());
        mEditTextContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_active);
                } else {
                    edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);
                }

            }
        });
        mEditTextContent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_active);
            }
        });
        // 监听文字框
        mEditTextContent.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    buttonSend.setVisibility(View.VISIBLE);
                } else {
                    buttonSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    protected void initData() {
        mLat = SettingUtils.get(mContext, Constant.LAT, Constant.DEFAULT_LAT);
        mLng = SettingUtils.get(mContext, Constant.LNG, Constant.DEFAULT_LNG);
        if (SettingUtils.get(mContext, Constant.IS_CUSTOMIZE, false)) {
            final double customizeLat = SettingUtils.get(mContext, Constant.CUSTOMIZE_LAT, Constant.DEFAULT_LAT);
            final double customizeLng = SettingUtils.get(mContext, Constant.CUSTOMIZE_LNG, Constant.DEFAULT_LNG);
            LatLng latLng = new LatLng(mLat, mLng);
            LatLng customizeLatLng = new LatLng(customizeLat, customizeLng);
            double distance = LemonLocation.getDistance(latLng, customizeLatLng);
            if (distance > 1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                final Dialog noticeDialog = builder.create();
                noticeDialog.setCancelable(false);
                View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_hints_diatance, null);
                TextView txtContent = (TextView) contentView.findViewById(R.id.txt_dialog_content);
                txtContent.setText("当前位置与您设定的位置相差超过1000m，是否使用当前位置？");
                Button btnSure = (Button) contentView.findViewById(R.id.btn_dialog_sure);
                btnSure.setText("重新设定");
                btnSure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLat = customizeLat;
                        mLng = customizeLng;
                        startActivityForResult(new Intent(mContext, MapActivity.class), Constant.MAP_REQUEST);
                        noticeDialog.dismiss();
                    }
                });
                Button btnCancel = (Button) contentView.findViewById(R.id.btn_dialog_cancel);
                btnCancel.setText("使用当前");
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LocChangeEvent locChangeEvent = new LocChangeEvent();
                        locChangeEvent.setTag(Tag);
                        SettingUtils.set(mContext, Constant.IS_CUSTOMIZE, false);
                        EventUtil.sendEvent(locChangeEvent);
                        noticeDialog.dismiss();
                    }
                });
                noticeDialog.show();
                noticeDialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
                noticeDialog.setContentView(contentView);
            }
        }
    }

    /**
     * 显示语音图标按钮
     *
     * @param view
     */
    public void setModeVoice(View view) {
        hideKeyboard();
        edittext_layout.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        buttonSetModeKeyboard.setVisibility(View.VISIBLE);
        buttonSend.setVisibility(View.GONE);
        buttonPressToSpeak.setVisibility(View.VISIBLE);
    }

    /**
     * 显示键盘图标
     *
     * @param view
     */
    public void setModeKeyboard(View view) {
        edittext_layout.setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);
        buttonSetModeVoice.setVisibility(View.VISIBLE);
        // mEditTextContent.setVisibility(View.VISIBLE);
        mEditTextContent.requestFocus();
        // buttonSend.setVisibility(View.VISIBLE);
        buttonPressToSpeak.setVisibility(View.GONE);
        if (TextUtils.isEmpty(mEditTextContent.getText())) {
            buttonSend.setVisibility(View.GONE);
        } else {
            buttonSend.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_set_mode_voice:
                setModeVoice(v);
                break;
            case R.id.btn_set_mode_keyboard:
                setModeKeyboard(v);
                break;
            case R.id.btn_send:
                if (ParamUtils.isEmpty(mBrokerage)) {
                    lemonMessage.sendMessage(R.string.toast_pick_up_brokerage);
                    return;
                }
                final ChatMessage message = new ChatMessage();
                message.type = 0;
                message.content = mEditTextContent.getText().toString();
                /*---------------------------2016/2/16新增本地头像----------------------------------------*/
                UserBean bean = UserBean.getInstance(mContext);//获取本地头像
                if (bean != null) {
                    message.photoPath = bean.photo;
                }
                 /*---------------------------2016/2/16-------------------------------------------------*/
                chatAdapter.getMsgList().add(message);
                chatAdapter.notifyDataSetChanged();
                mEditTextContent.setText("");
                CallParam callParam = new CallParam();
                String token = SettingUtils.get(mContext, "token", null);
                callParam.setToken(token);
                callParam.setLat(mLat);
                callParam.setLng(mLng);
                callParam.setTextMsg(message.content);
                callParam.setBrokerage(mBrokerage);
                callParam.setRequireType(mRequireTypeId);
                callParam.setMessage(message);
                apiManager.sendMsgToNearbyBrokers(callParam);
                break;
        }
    }

    private void sendVoice() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 21) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                //获取到权限
                lemonMessage.sendMessage("不要拒绝麦克风");
            }
        }
    }

    class PressToSpeakListen implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (ParamUtils.isEmpty(mBrokerage)) {
                lemonMessage.sendMessage(R.string.toast_pick_up_brokerage);
                return false;
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!CommonUtils.isExitsSdcard()) {
                        String st4 = getResources().getString(R.string.Send_voice_need_sdcard_support);
                        Toast.makeText(mContext, st4, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    try {
                        v.setPressed(true);
                        wakeLock.acquire();
                        if (VoicePlayClickListener.isPlaying)
                            VoicePlayClickListener.currentPlayListener.stopPlayVoice();
                        recordingContainer.setVisibility(View.VISIBLE);
                        recordingHint.setText(getString(R.string.move_up_to_cancel));
                        recordingHint.setBackgroundColor(Color.TRANSPARENT);
                        voiceRecorder.startRecording("test", "nearby", getApplicationContext());
                    } catch (Exception e) {
                        e.printStackTrace();
                        v.setPressed(false);
                        if (wakeLock.isHeld())
                            wakeLock.release();
                        if (voiceRecorder != null)
                            voiceRecorder.discardRecording();
                        recordingContainer.setVisibility(View.INVISIBLE);
                        Toast.makeText(mContext, R.string.recoding_fail, Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    return true;
                case MotionEvent.ACTION_MOVE: {
                    if (event.getY() < 0) {
                        recordingHint.setText(getString(R.string.release_to_cancel));
                        recordingHint.setBackgroundResource(R.drawable.recording_text_hint_bg);
                    } else {
                        recordingHint.setText(getString(R.string.move_up_to_cancel));
                        recordingHint.setBackgroundColor(Color.TRANSPARENT);
                    }
                    return true;
                }
                case MotionEvent.ACTION_UP:
                    v.setPressed(false);
                    recordingContainer.setVisibility(View.INVISIBLE);
                    if (wakeLock.isHeld())
                        wakeLock.release();
                    if (event.getY() < 0) {
                        // discard the recorded audio.
                        voiceRecorder.discardRecording();

                    } else {
                        // stop recording and send voice file
                        String st1 = getResources().getString(R.string.Recording_without_permission);
                        String st2 = getResources().getString(R.string.The_recording_time_is_too_short);
                        String st3 = getResources().getString(R.string.send_failure_please);
                        try {
                            int length = voiceRecorder.stopRecoding();
                            if (length > 0) {
                                sendVoice(voiceRecorder.getVoiceFilePath(),
                                        length, false);
                            } else if (length == EMError.INVALID_FILE) {
                                Toast.makeText(mContext, st1, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, st2, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(mContext, st3, Toast.LENGTH_SHORT).show();
                        }

                    }
                    return true;
                default:
                    recordingContainer.setVisibility(View.INVISIBLE);
                    if (voiceRecorder != null)
                        voiceRecorder.discardRecording();
                    return false;
            }
        }
    }

    private Handler micImageHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            // 切换msg切换图片
            micImage.setImageDrawable(micImages[msg.what]);
        }
    };

    private void sendVoice(final String filePath, int length, boolean isResend) {
        if (!(new File(filePath).exists())) {
            return;
        }
        String url = "http://........"; // your URL here


        final ChatMessage msg = new ChatMessage();
        msg.type = 1;
        msg.content = filePath;
        msg.length = length;
         /*---------------------------2016/2/16新增本地头像----------------------------------------*/
        UserBean bean = UserBean.getInstance(mContext);//获取本地头像
        if (bean != null) {
            msg.photoPath = bean.photo;
        }
        /*---------------------------2016/2/16-------------------------------------------------*/
        chatAdapter.getMsgList().add(msg);
        chatAdapter.notifyDataSetChanged();
        QiNiuParam qiNiuParam = new QiNiuParam();
        qiNiuParam.setFilePath(filePath);
        qiNiuParam.setMessage(msg);
        apiManager.uploadToken(qiNiuParam);
    }

    public void onEventMainThread(QiNiuResult result) {
        HashMap<String, String> data = (HashMap<String, String>) result.getData();
        final String qiNiuToken = data.get("token");
        final QiNiuParam qiNiuParam = (QiNiuParam) result.getParam();
        final ChatMessage message = qiNiuParam.getMessage();
        if (!ParamUtils.isEmpty(qiNiuToken)) {
            final UploadPhotoTask uploadPhotoTask = new UploadPhotoTask();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ImageUploadUtil.OnUploadFile(qiNiuToken, new File(qiNiuParam.getFilePath()), uploadPhotoTask, mContext);
                    String httpUrl = uploadPhotoTask.getHttpUrl();
                    CallParam callParam = new CallParam();
                    String token = SettingUtils.get(mContext, "token", null);
                    callParam.setToken(token);
                    callParam.setLat(mLat);
                    callParam.setLng(mLng);
                    callParam.setVoiceMsg(httpUrl);
                    callParam.setBrokerage(mBrokerage);
                    callParam.setRequireType(mRequireTypeId);
                    callParam.setMessage(message);
                    apiManager.sendMsgToNearbyBrokers(callParam);
                }
            }).start();
        } else {
            message.status = 2;
            chatAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 是否发布成功
     *
     * @param result
     */
    public void onEventMainThread(CallResult result) {
        ChatMessage message = ((CallParam) result.getParam()).getMessage();
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            ToolUtil.updatePoint(mContext, result.getData().getTotalPoint(), result.getData().getGetPoint());
            message.status = 1;
            //如果时间记录为0，说明超过15分钟了或者初次发布，那么这一条算新需求，记录时间点
            if (SettingUtils.get(mContext, userId + Constant.FIRST_SEND_TIME, 0L) == 0L) {
                SettingUtils.set(mContext, userId + Constant.FIRST_SEND_TIME, new Date().getTime());
            }
            //记录发布信息；
            String s = new Gson().toJson(chatAdapter.getMsgList());
            SettingUtils.set(mContext, userId + Constant.CALL_HISTORY, s);
        } else {
            message.status = 2;
        }
        chatAdapter.notifyDataSetChanged();
    }

    private class UploadPhotoTask implements ImageUploadUtil.OnUploadFileListener {
        private String httpUrl;

        public String getHttpUrl() {
            return httpUrl;
        }

        @Override
        public void progress(File srcFile, double percent) {
        }

        @Override
        public void complete(File srcFile, final String httpUrl) {
            this.httpUrl = httpUrl;
        }

        @Override
        public void error(File srcFile) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.MAP_REQUEST) {
                mLat = data.getDoubleExtra("lat", Constant.DEFAULT_LAT);
                mLng = data.getDoubleExtra("lng", Constant.DEFAULT_LNG);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        PlayerEngineImpl.getInstance(mContext).stop();
    }
}

