package com.zfb.house.component;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.component.Wheel.AbstractWheel;
import com.zfb.house.component.Wheel.OnWheelChangedListener;
import com.zfb.house.component.Wheel.WheelVerticalView;
import com.zfb.house.component.Wheel.adapter.ArrayWheelAdapter;
import com.zfb.house.model.bean.ReleasePullItem;
import com.zfb.house.util.ToolUtil;

import java.util.List;


public class ArraySelectWheelDialog extends Dialog implements View.OnClickListener {
    private static final int ROOM = 0;
    private static final int HALL = 1;
    private static final int TOILET = 2;
    private static final int BALCONY = 3;

    private Context mContext;
    private WheelVerticalView wheelVerticalView;
    private ArrayWheelAdapter arrayWheelAdapter;
    private String mSelectValue;
    private List<ReleasePullItem> mList;
    private String mItemValue;

    public void setListener(SelectWheelResultListener listener) {
        this.listener = listener;
    }

    private SelectWheelResultListener listener;

    private String[] mValues;

    public ArraySelectWheelDialog(Context context) {
        super(context, R.style.loading_dialog_themes);
        mContext = context;
    }

    public ArraySelectWheelDialog(Context context, String[] value) {
        super(context, R.style.loading_dialog_themes);
        mContext = context;
        mValues = value;
    }

    public ArraySelectWheelDialog(Context context, List<ReleasePullItem> list) {
        super(context, R.style.loading_dialog_themes);
        mContext = context;
        mList = list;
        mValues = ToolUtil.getReleasePullData(list);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_array_select_wheel);
        getWindow().setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.sure).setOnClickListener(this);
        findViewById(R.id.share_choice).setOnClickListener(this);

        initDialogView();
    }

    private void initDialogView() {
        wheelVerticalView = (WheelVerticalView) findViewById(R.id.wv_select);

        arrayWheelAdapter = new ArrayWheelAdapter(mContext, mValues);
        arrayWheelAdapter.setItemResource(R.layout.adapter_select_wheel);
        arrayWheelAdapter.setItemTextResource(R.id.textView1);
        wheelVerticalView.setViewAdapter(arrayWheelAdapter);
        wheelVerticalView.setCyclic(false);
        arrayWheelAdapter.setTextType("");
        wheelVerticalView.setCurrentItem(0);

        if (!ParamUtils.isNull(mValues)) {
            mSelectValue = mValues[0];
        }
        if (!ParamUtils.isEmpty(mList)) {
            mItemValue = mList.get(0).getValue();
        }
        wheelVerticalView.addChangingListener(new WheelChangedListener());
    }

    /**
     * @author <android-Jayu>
     * @Description: TODO(时间滚轮控件)
     * @date 2014年2月25日 上午10:59:50
     * @Email zongxu@pba.cn
     */
    class WheelChangedListener implements OnWheelChangedListener {

        public WheelChangedListener() {
        }

        @Override
        public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
            mSelectValue = mValues[newValue] + "";
            if (!ParamUtils.isEmpty(mList)) {
                mItemValue = mList.get(newValue).getValue();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_choice:
            case R.id.cancel:
                dismiss();
                break;
            case R.id.sure:
                if (listener != null) {
                    listener.getSelectValue(mSelectValue, mItemValue);
                }
                dismiss();
                break;
        }
    }

    public interface SelectWheelResultListener {
        void getSelectValue(String time, String id);
    }

}
