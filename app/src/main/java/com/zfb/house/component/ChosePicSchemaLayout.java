package com.zfb.house.component;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.zfb.house.R;
import com.zfb.house.ui.BrokerResidentialSellActivity;
import com.zfb.house.ui.SendMomentsActivity;
import com.zfb.house.ui.UserResidentialSellActivity;

/**
 * Created by Snekey on 2016/5/11.
 */
public class ChosePicSchemaLayout extends LinearLayout{

    private static final String TAG = "FriendsPublish.java";

    private View mTakePicBtn;//拍照
    private View mChoiceBtn;//相册
    private View mCancelBtn;//取消

    /** 视图第一次初始化  */
    private void initView(){
        inflate(getContext() , R.layout.layout_chose_pic_schema, this);
        mTakePicBtn = findViewById(R.id.tv_capture_photo);
        mChoiceBtn = findViewById(R.id.tv_select_photo);
        mCancelBtn = findViewById(R.id.tv_cancel);
        findViewById(R.id.tab_home_item_take_picture_layout).setOnClickListener(new MyOnClickListener());
        mTakePicBtn.setOnClickListener(new MyOnClickListener());
        mChoiceBtn.setOnClickListener(new MyOnClickListener());
        mCancelBtn.setOnClickListener(new MyOnClickListener());
    }

    private class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Context context = getContext();
            switch (v.getId()){
                case R.id.tv_capture_photo:
                    setVisibility(View.GONE);
                    // TODO: 2016/5/11 相机拍照部分 
                    if(context != null && context instanceof SendMomentsActivity){
                        SendMomentsActivity activity = (SendMomentsActivity) context;
                        activity.captureImage();
                    }else if(context != null && context instanceof BrokerResidentialSellActivity){
                        BrokerResidentialSellActivity activity = (BrokerResidentialSellActivity) context;
                        activity.captureImage();
                    }else if(context != null && context instanceof UserResidentialSellActivity){
                        UserResidentialSellActivity activity = (UserResidentialSellActivity) context;
                        activity.captureImage();
                    }
                    break;
                case R.id.tv_select_photo:
                    setVisibility(View.GONE);
                    if(context != null && context instanceof SendMomentsActivity){
                        SendMomentsActivity activity = (SendMomentsActivity) context;
                        activity.getLocalImage();
                    }else if(context != null && context instanceof BrokerResidentialSellActivity){
                        BrokerResidentialSellActivity activity = (BrokerResidentialSellActivity) context;
                        activity.getLocalImage();
                    }else if(context != null && context instanceof UserResidentialSellActivity){
                        UserResidentialSellActivity activity = (UserResidentialSellActivity) context;
                        activity.getLocalImage();
                    }
                    break;
                case R.id.tab_home_item_take_picture_layout:
                case R.id.tv_cancel:
                    setVisibility(View.GONE);
                    break;
            }
        }
    }

    /**************************************************************************/
    public ChosePicSchemaLayout(Context context) {
        super(context);
        initView();
    }

    public ChosePicSchemaLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @TargetApi(android.os.Build.VERSION_CODES.HONEYCOMB)
    public ChosePicSchemaLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

}
