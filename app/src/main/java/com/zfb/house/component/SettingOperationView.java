package com.zfb.house.component;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lemon.event.HideOperationView;
import com.zfb.house.R;
import com.zfb.house.model.bean.RentItem;
import com.zfb.house.model.bean.SellItem;

import java.util.Set;

import de.greenrobot.event.EventBus;

/**
 * Created by linwenbing on 16/6/13.
 */
public class SettingOperationView extends LinearLayout implements OnClickListener{
    public static final int SELL = 0;
    public static final int RENT = 1;
    public interface OnOperationClickListener{
        void OnOpenClick(int type);
        void OnEditClick(Object homeDataBean,int type);
        void OnShareClick(Object homeDataBean,int type);
        void OnDeleteClick(Object homeDataBean,int type);
        void OnUpClick(Object homeDataBean,int type);
        void OnTopClick(Object homeDataBean,int type);
    }

    public void setLlOpeation(LinearLayout llOpeation) {
        this.llOpeation = llOpeation;
    }

    public static void setmCurrentShowBtnID(String mCurrentShowBtnID) {
        SettingOperationView.mCurrentShowBtnID = mCurrentShowBtnID;
    }

    public void setRentBean(RentItem rentBean) {
        this.rentBean = rentBean;
    }

    public void setmListPostion(int mListPostion) {
        this.mListPostion = mListPostion;
    }

    private OnOperationClickListener onOperationClickListener;
    private LinearLayout llOpeation;
    public static String mCurrentShowBtnID = "-1";//当前需要显示按钮的id号
    private Object rentBean;
    private int mListPostion;
    private static Set<OnOperationClickListener> mOnClickListeners;
    private TextView tvUpOrDown,tvTop;
    private View vTop;
    private int mSellOrRentType = 0;
    private OnOperationClickListener mOnClickListener;

    public SettingOperationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //在构造函数中将Xml中定义的布局解析出来。
        LayoutInflater.from(context).inflate(R.layout.layout_setting_operation, this, true);
        llOpeation = (LinearLayout) findViewById(R.id.ll_opeation);
        findViewById(R.id.btn_setting).setOnClickListener(this);
        findViewById(R.id.tv_operation_edit).setOnClickListener(this);
        findViewById(R.id.tv_operation_share).setOnClickListener(this);
        findViewById(R.id.tv_operation_delete).setOnClickListener(this);
        tvUpOrDown = (TextView) findViewById(R.id.tv_operation_up);
        tvUpOrDown.setOnClickListener(this);
        tvTop = (TextView) findViewById(R.id.tv_operation_top);
        tvTop.setOnClickListener(this);
        vTop = findViewById(R.id.v_operation_top);
    }

    public void hideOpeation(){
        llOpeation.setVisibility(View.GONE);
    }

    public void setData(RentItem model){
        mSellOrRentType = RENT;
        rentBean = model;
        if (mCurrentShowBtnID.equals(model.getId())){
            llOpeation.setVisibility(View.VISIBLE);
        }else {
            llOpeation.setVisibility(View.GONE);
        }

        if (model != null){
            if (model.getUpDown().equals("0")){
                tvUpOrDown.setText("上架");
                tvTop.setVisibility(View.GONE);
                vTop.setVisibility(View.GONE);
            }else{
                tvTop.setVisibility(View.VISIBLE);
                vTop.setVisibility(View.VISIBLE);
                tvUpOrDown.setText("下架");
            }
        }
    }

    public void setData(SellItem model){
        mSellOrRentType = SELL;
        rentBean = model;
        if (mCurrentShowBtnID.equals(model.getId())){
            Log.i("linwb","dddd = " + model.getId());
            llOpeation.setVisibility(View.VISIBLE);
        }else {
            llOpeation.setVisibility(View.GONE);
        }

        if (model != null){
            if (model.getUpDown().equals("0")){
                tvUpOrDown.setText("上架");
                tvTop.setVisibility(View.GONE);
                vTop.setVisibility(View.GONE);
            }else{
                tvTop.setVisibility(View.VISIBLE);
                vTop.setVisibility(View.VISIBLE);
                tvUpOrDown.setText("下架");
            }
        }
    }

    public void setListPosition(int position){
        mListPostion = position;
    }

    public void setOnOperationClickListener(OnOperationClickListener onClickListener){
        mOnClickListener = onClickListener;
//        if(mOnClickListeners == null ){
//            mOnClickListeners = new HashSet<>();
//        }
//        mOnClickListeners.add(onClickListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_setting:
                EventBus.getDefault().post(new HideOperationView());
                if (llOpeation.getVisibility() == View.VISIBLE){
                    llOpeation.setVisibility(View.GONE);
                    mCurrentShowBtnID = "-1";
                }else{
                    if(rentBean != null){
                        if (mSellOrRentType == RENT){
                            mCurrentShowBtnID = ((RentItem) rentBean).getId();
                        }else{
                            mCurrentShowBtnID = ((SellItem) rentBean).getId();
                        }
                    }

                    llOpeation.setVisibility(View.VISIBLE);
                }
                if(mOnClickListeners != null && !mOnClickListeners.isEmpty()){
                    for(OnOperationClickListener mOnClickListener : mOnClickListeners){
                        mOnClickListener.OnOpenClick(mSellOrRentType);
                    }
                }
                break;
            case R.id.tv_operation_edit://编辑
                llOpeation.setVisibility(View.GONE);
                mCurrentShowBtnID = "-1";
                if (mOnClickListener != null){
                    mOnClickListener.OnEditClick(rentBean, mSellOrRentType);
                }
//                if(mOnClickListeners != null && !mOnClickListeners.isEmpty()){
//                    for(OnOperationClickListener mOnClickListener : mOnClickListeners){
//                        Log.i("linwb","edit ===== ");
//                        mOnClickListener.OnEditClick(rentBean,mSellOrRentType);
//                    }
//                }

                break;
            case R.id.tv_operation_share://分享
                llOpeation.setVisibility(View.GONE);
                mCurrentShowBtnID = "-1";
                if(mOnClickListeners != null && !mOnClickListeners.isEmpty()){
                    for(OnOperationClickListener mOnClickListener : mOnClickListeners){
                        mOnClickListener.OnShareClick(rentBean,mSellOrRentType);
                    }
                }
                break;
            case R.id.tv_operation_delete://删除
                llOpeation.setVisibility(View.GONE);
                mCurrentShowBtnID = "-1";
                if (mOnClickListener != null){
                    mOnClickListener.OnDeleteClick(rentBean,mSellOrRentType);
                }
//                if(mOnClickListeners != null && !mOnClickListeners.isEmpty()){
//                    for(OnOperationClickListener mOnClickListener : mOnClickListeners){
//                        mOnClickListener.OnDeleteClick(rentBean,mSellOrRentType);
//                    }
//                }
                break;
            case R.id.tv_operation_up://上架
                llOpeation.setVisibility(View.GONE);
                mCurrentShowBtnID = "-1";
                if (mOnClickListener != null){
                    mOnClickListener.OnUpClick(rentBean, mSellOrRentType);
                }
//                if(mOnClickListeners != null && !mOnClickListeners.isEmpty()){
//                    for(OnOperationClickListener mOnClickListener : mOnClickListeners){
//                        mOnClickListener.OnUpClick(rentBean,mSellOrRentType);
//                    }
//                }
                break;
            case R.id.tv_operation_top://置顶
                llOpeation.setVisibility(View.GONE);
                mCurrentShowBtnID = "-1";
                if (mOnClickListener != null){
                    mOnClickListener.OnTopClick(rentBean,mSellOrRentType);
                }
//                if(mOnClickListeners != null && !mOnClickListeners.isEmpty()){
//                    for(OnOperationClickListener mOnClickListener : mOnClickListeners){
//                        mOnClickListener.OnTopClick(rentBean,mSellOrRentType);
//                    }
//                }
                break;
        }
    }
}
