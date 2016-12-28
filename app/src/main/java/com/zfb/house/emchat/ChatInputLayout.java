package com.zfb.house.emchat;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.util.EMLog;
import com.easemob.util.PathUtil;
import com.lemon.util.LogUtils;
import com.zfb.house.R;
import com.zfb.house.emchat.temp.OnKeyWordsClick;
import com.zfb.house.model.bean.UserBean;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzh on 2015-10-13.
 * 调用方式：请一定要setBaseActivity
 */
public class ChatInputLayout extends LinearLayout implements View.OnClickListener, EMEventListener {
    private OnKeyWordsClick onKeyWordsClick;//设置回调
    private static final String TAG = "ChatInputLayout";

    public static final int REQUEST_CODE_CAMERA = 18;
    public static final int REQUEST_CODE_LOCAL = 19;
    public static final int REQUEST_CODE_SELECT_FILE = 24;

    private PasteEditText mEditTextContent;
    private View buttonSetModeKeyboard;
    private View buttonSetModeVoice;
    private View buttonSend;
    private View buttonPressToSpeak;
    // private ViewPager expressionViewpager;
    private LinearLayout emojiIconContainer;
    private LinearLayout btnContainer;
    private ImageView locationImgview;
    private View more;
    private int position;
    private ClipboardManager clipboard;
    private ViewPager expressionViewpager;
    private InputMethodManager manager;
    private List<String> reslist;
    private Drawable[] micImages;


    private ImageView iv_emoticons_normal;
    private ImageView iv_emoticons_checked;
    private RelativeLayout edittext_layout;
    private Button btnMore;
    private File cameraFile;

    private PowerManager.WakeLock wakeLock;
    private ImageView voiceCallBtn;
    private ImageView videoCallBtn;

    public ChatInputLayout(Context context) {
        super(context);
        initView();
    }

    public ChatInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ChatInputLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        inflate(getContext() , R.layout.chatinput_layout , this);
        mEditTextContent = (PasteEditText) findViewById(R.id.et_sendmessage);
        buttonSetModeKeyboard = findViewById(R.id.btn_set_mode_keyboard);
        edittext_layout = (RelativeLayout) findViewById(R.id.edittext_layout);
        buttonSetModeVoice = findViewById(R.id.btn_set_mode_voice);
        buttonSend = findViewById(R.id.btn_send);
        buttonSend.setOnClickListener(this);
        buttonPressToSpeak = findViewById(R.id.btn_press_to_speak);
        expressionViewpager = (ViewPager) findViewById(R.id.vPager);
        emojiIconContainer = (LinearLayout) findViewById(R.id.ll_face_container);
        btnContainer = (LinearLayout) findViewById(R.id.ll_btn_container);
        locationImgview = (ImageView) findViewById(R.id.btn_location);
        iv_emoticons_normal = (ImageView) findViewById(R.id.iv_emoticons_normal);
        iv_emoticons_checked = (ImageView) findViewById(R.id.iv_emoticons_checked);
        btnMore = (Button) findViewById(R.id.btn_more);
        iv_emoticons_normal.setVisibility(View.VISIBLE);
        iv_emoticons_checked.setVisibility(View.INVISIBLE);
        iv_emoticons_normal.setOnClickListener(this);
        iv_emoticons_checked.setOnClickListener(this);
        more = findViewById(R.id.more);
        edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);
        voiceCallBtn = (ImageView) findViewById(R.id.btn_voice_call);
        videoCallBtn = (ImageView) findViewById(R.id.btn_video);

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

        // 表情list
        reslist = getExpressionRes(35);
        // 初始化表情viewpager
        List<View> views = new ArrayList<View>();
        View gv1 = getGridChildView(1);
        View gv2 = getGridChildView(2);
        views.add(gv1);
        views.add(gv2);
        expressionViewpager.setAdapter(new ExpressionPagerAdapter(views));
        edittext_layout.requestFocus();

        buttonPressToSpeak.setOnTouchListener(new PressToSpeakListen());
        mEditTextContent.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_active);
                } else {
                    edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);
                }

            }
        });
        mEditTextContent.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_active);
                more.setVisibility(View.GONE);
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
                emojiIconContainer.setVisibility(View.GONE);
                btnContainer.setVisibility(View.GONE);
            }
        });
        // 监听文字框
        mEditTextContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    btnMore.setVisibility(View.GONE);
                    buttonSend.setVisibility(View.VISIBLE);
                } else {
                    btnMore.setVisibility(View.VISIBLE);
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
    }


    /**
     * 消息图标点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        String st1 = getResources().getString(R.string.not_connect_to_server);
        int id = view.getId();
        if (id == R.id.btn_send) {// 点击发送按钮(发文字和表情)
            String s = mEditTextContent.getText().toString();
            sendText(s);
        } else if (id == R.id.btn_take_picture) {
            selectPicFromCamera();// 点击照相图标
        } else if (id == R.id.btn_picture) {
            selectPicFromLocal(); // 点击图片图标
        } else if (id == R.id.btn_location) { // 位置
//			startActivityForResult(new Intent(this, BaiduMapActivity.class), REQUEST_CODE_MAP);
        } else if (id == R.id.iv_emoticons_normal) { // 点击显示表情框
            more.setVisibility(View.VISIBLE);
            iv_emoticons_normal.setVisibility(View.INVISIBLE);
            iv_emoticons_checked.setVisibility(View.VISIBLE);
            btnContainer.setVisibility(View.GONE);
            emojiIconContainer.setVisibility(View.VISIBLE);
            hideKeyboard();
        } else if (id == R.id.iv_emoticons_checked) { // 点击隐藏表情框
            iv_emoticons_normal.setVisibility(View.VISIBLE);
            iv_emoticons_checked.setVisibility(View.INVISIBLE);
            btnContainer.setVisibility(View.VISIBLE);
            emojiIconContainer.setVisibility(View.GONE);
            more.setVisibility(View.GONE);

        } /*else if (id == R.id.btn_video) {
            // 点击摄像图标
			Intent intent = new Intent(ChatActivity.this, ImageGridActivity.class);
			startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
		} */ else if (id == R.id.btn_file) { // 点击文件图标
            selectFileFromLocal();
        } else if (id == R.id.btn_voice_call) { // 点击语音电话图标
            if (!EMChatManager.getInstance().isConnected())
                Toast.makeText(getContext(), st1, Toast.LENGTH_SHORT).show();
            else {
                /*startActivity(new Intent(ChatActivity.this, VoiceCallActivity.class).putExtra("username",
                        toChatUsername).putExtra("isComingCall", false));
                voiceCallBtn.setEnabled(false);
                toggleMore(null);*/
            }
        } /*else if (id == R.id.btn_video_call) { // 视频通话
			if (!EMChatManager.getInstance().isConnected())
				Toast.makeText(this, st1, 0).show();
			else{
				startActivity(new Intent(this, VideoCallActivity.class).putExtra("username", toChatUsername).putExtra(
						"isComingCall", false));
				videoCallBtn.setEnabled(false);
				toggleMore(null);
			}
		}*/else if(id== R.id.btn_more){
            toggleMore(view);
        }
    }

    /**
     * 事件监听
     * <p/>
     * see {@link EMNotifierEvent}
     */
    @Override
    public void onEvent(EMNotifierEvent event) {
        switch (event.getEvent()) {
            default:
                break;
        }
    }


    FragmentActivity activity;

    /**
     * 照相获取图片
     */
    public void selectPicFromCamera() {
        if (!CommonUtils.isExitsSdcard()) {
            String st = getResources().getString(R.string.sd_card_does_not_exist);
            Toast.makeText(getContext(), st, Toast.LENGTH_SHORT).show();
            return;
        }
        cameraFile = new File(PathUtil.getInstance().getImagePath(), UserBean.getInstance(getContext()).name/*DemoApplication.getInstance().getUserName()*/
                + System.currentTimeMillis() + ".jpg");
        cameraFile.getParentFile().mkdirs();
        if (activity != null) {
            activity.startActivityForResult(
                    new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
                    REQUEST_CODE_CAMERA);
        }
    }

    /**
     * 选择文件
     */
    private void selectFileFromLocal() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        if (activity != null) {
            activity.startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
        }
    }

    /**
     * 从图库获取图片
     */
    public void selectPicFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        if (activity != null) {
            activity.startActivityForResult(intent, REQUEST_CODE_LOCAL);
        }
    }

    /**
     * 发送文本消息
     *
     * @param content message content
     * @param content boolean resend
     */
    public void sendText(String content) {
        if (content.length() > 0) {
            if(onKeyWordsClick!=null){
                onKeyWordsClick.onItemClick(content);
            }
            mEditTextContent.setText("");
        }else{
            LogUtils.toast(getContext(),"请输入");
        }
    }

    /**
     * 发送语音
     *
     * @param filePath
     * @param fileName
     * @param length
     * @param isResend
     */
    private void sendVoice(String filePath, String fileName, String length, boolean isResend) {
    }

    /**
     * 发送图片
     *
     * @param filePath
     */
    private void sendPicture(final String filePath) {

    }

    /**
     * 发送视频消息
     */
    private void sendVideo(final String filePath, final String thumbPath, final int length) {

    }

    /**
     * 根据图库图片uri发送图片
     *
     * @param selectedImage
     */
    private void sendPicByUri(Uri selectedImage) {

    }

    /**
     * 发送位置信息
     *
     * @param latitude
     * @param longitude
     * @param imagePath
     * @param locationAddress
     */
    private void sendLocationMsg(double latitude, double longitude, String imagePath, String locationAddress) {
    }

    /**
     * 发送文件
     *
     * @param uri
     */
    private void sendFile(Uri uri) {
    }

    /**
     * 重发消息
     */
    private void resendMessage() {
    }

    /**
     * 显示语音图标按钮
     *
     * @param view
     */
    public void setModeVoice(View view) {
        hideKeyboard();
        edittext_layout.setVisibility(View.GONE);
        more.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        buttonSetModeKeyboard.setVisibility(View.VISIBLE);
        buttonSend.setVisibility(View.GONE);
        btnMore.setVisibility(View.VISIBLE);
        buttonPressToSpeak.setVisibility(View.VISIBLE);
        iv_emoticons_normal.setVisibility(View.VISIBLE);
        iv_emoticons_checked.setVisibility(View.INVISIBLE);
        btnContainer.setVisibility(View.VISIBLE);
        emojiIconContainer.setVisibility(View.GONE);

    }

    /**
     * 显示键盘图标
     *
     * @param view
     */
    public void setModeKeyboard(View view) {
        // mEditTextContent.setOnFocusChangeListener(new OnFocusChangeListener()
        // {
        // @Override
        // public void onFocusChange(View v, boolean hasFocus) {
        // if(hasFocus){
        // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        // }
        // }
        // });
        edittext_layout.setVisibility(View.VISIBLE);
        more.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        buttonSetModeVoice.setVisibility(View.VISIBLE);
        // mEditTextContent.setVisibility(View.VISIBLE);
        mEditTextContent.requestFocus();
        // buttonSend.setVisibility(View.VISIBLE);
        buttonPressToSpeak.setVisibility(View.GONE);
        if (TextUtils.isEmpty(mEditTextContent.getText())) {
            btnMore.setVisibility(View.VISIBLE);
            buttonSend.setVisibility(View.GONE);
        } else {
            btnMore.setVisibility(View.GONE);
            buttonSend.setVisibility(View.VISIBLE);
        }

    }


    /**
     * 点击进入群组详情
     *
     * @param view
     */
/*	public void toGroupDetails(View view) {
		if (room == null && group == null) {
			Toast.makeText(getApplicationContext(), R.string.gorup_not_found, 0).show();
			return;
		}
		if(chatType == CHATTYPE_GROUP){
			startActivityForResult((new Intent(this, GroupDetailsActivity.class).putExtra("groupId", toChatUsername)),
					REQUEST_CODE_GROUP_DETAIL);
		}else{
			startActivityForResult((new Intent(this, ChatRoomDetailsActivity.class).putExtra("roomId", toChatUsername)),
					REQUEST_CODE_GROUP_DETAIL);
		}
	}*/

    /**
     * 显示或隐藏图标按钮页
     *
     * @param view
     */
    public void toggleMore(View view) {
        if (more.getVisibility() == View.GONE) {
            EMLog.d(TAG, "more gone");
            hideKeyboard();
            more.setVisibility(View.VISIBLE);
            btnContainer.setVisibility(View.VISIBLE);
            emojiIconContainer.setVisibility(View.GONE);
        } else {
            if (emojiIconContainer.getVisibility() == View.VISIBLE) {
                emojiIconContainer.setVisibility(View.GONE);
                btnContainer.setVisibility(View.VISIBLE);
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
            } else {
                more.setVisibility(View.GONE);
            }

        }

    }

    /**
     * 点击文字输入框
     *
     * @param v
     */
    public void editClick(View v) {
        if (more.getVisibility() == View.VISIBLE) {
            more.setVisibility(View.GONE);
            iv_emoticons_normal.setVisibility(View.VISIBLE);
            iv_emoticons_checked.setVisibility(View.INVISIBLE);
        }

    }


    /**
     * 按住说话listener
     */
    class PressToSpeakListen implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return false;
        }
    }

    /**
     * 获取表情的gridview的子view
     *
     * @param i
     * @return
     */
    private View getGridChildView(int i) {
        View view = View.inflate(getContext(), R.layout.expression_gridview, null);
        ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
        List<String> list = new ArrayList<String>();
        if (i == 1) {
            List<String> list1 = reslist.subList(0, 20);
            list.addAll(list1);
        } else if (i == 2) {
            list.addAll(reslist.subList(20, reslist.size()));
        }
        list.add("delete_expression");
        final ExpressionAdapter expressionAdapter = new ExpressionAdapter(getContext(), 1, list);
        gv.setAdapter(expressionAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filename = expressionAdapter.getItem(position);
                try {
                    // 文字输入框可见时，才可输入表情
                    // 按住说话可见，不让输入表情
                    if (buttonSetModeKeyboard.getVisibility() != View.VISIBLE) {

                        if (filename != "delete_expression") { // 不是删除键，显示表情
                            // 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
                            Class clz = Class.forName("com.zfb.house.emchat.SmileUtils");
                            Field field = clz.getField(filename);
                            mEditTextContent.append(SmileUtils.getSmiledText(getContext(),
                                    (String) field.get(null)));
                        } else { // 删除文字或者表情
                            if (!TextUtils.isEmpty(mEditTextContent.getText())) {

                                int selectionStart = mEditTextContent.getSelectionStart();// 获取光标的位置
                                if (selectionStart > 0) {
                                    String body = mEditTextContent.getText().toString();
                                    String tempStr = body.substring(0, selectionStart);
                                    int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                                    if (i != -1) {
                                        CharSequence cs = tempStr.substring(i, selectionStart);
                                        if (SmileUtils.containsKey(cs.toString()))
                                            mEditTextContent.getEditableText().delete(i, selectionStart);
                                        else
                                            mEditTextContent.getEditableText().delete(selectionStart - 1,
                                                    selectionStart);
                                    } else {
                                        mEditTextContent.getEditableText().delete(selectionStart - 1, selectionStart);
                                    }
                                }
                            }

                        }
                    }
                } catch (Exception e) {
                }

            }
        });
        return view;
    }

    public List<String> getExpressionRes(int getSum) {
        List<String> reslist = new ArrayList<String>();
        for (int x = 1; x <= getSum; x++) {
            String filename = "ee_" + x;

            reslist.add(filename);

        }
        return reslist;

    }

    private Activity mContext;
    public void setBaseActivity(Activity activity) {
        this.mContext = activity;
    }

    /**
     * 隐藏软键盘
     */
    public void hideKeyboard() {
        if(mContext != null){
            manager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (mContext.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
                if (mContext.getCurrentFocus() != null) {
                    manager.hideSoftInputFromWindow(mContext.getCurrentFocus().getWindowToken(), 0);//强制关闭------
                }else{
                    manager.hideSoftInputFromWindow(mEditTextContent.getWindowToken(), 0); //强制隐藏键盘
                }
            }else{
                manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//表示打开则关闭，关闭则打开。
            }
        }else if(getContext() != null){
            manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//表示打开则关闭，关闭则打开。
        }
    }

    public void showKeyBoard(){
        if(mContext != null){
            manager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (mContext.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {

                    mEditTextContent.requestFocus();
                    mEditTextContent.setFocusable(true);
                    mEditTextContent.setFocusableInTouchMode(true);
                    manager.showSoftInput(mEditTextContent, InputMethodManager.SHOW_FORCED);

            }else{
                manager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);//表示打开则关闭，关闭则打开。
            }
        }
    }

    public void setOnKeyWordsClick(OnKeyWordsClick keyWordsClick){
        this.onKeyWordsClick = keyWordsClick;
    }
    /**
     * 获取消息
     * @return
     */
    public String getMessage(){
        String s = mEditTextContent.getText().toString();
        return s;
    }

    public EditText getEditText(){
        return mEditTextContent;
    }

}
