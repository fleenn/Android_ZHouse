package com.zfb.house.component;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.zfb.house.R;
import com.zfb.house.component.Wheel.AbstractWheel;
import com.zfb.house.component.Wheel.OnWheelChangedListener;
import com.zfb.house.component.Wheel.WheelVerticalView;
import com.zfb.house.component.Wheel.adapter.NumericWheelAdapter;

public class SelectWheelDialog extends Dialog implements View.OnClickListener {

    private static final int ROOM = 0;
    private static final int HALL = 1;
    private static final int TOILET = 2;
    private static final int BALCONY = 3;

    private Context mContext;
    private WheelVerticalView roomView, hallView, toiletView, balconyView;
    private NumericWheelAdapter roomAdapter, hallAdapter, toiletAdapter, balconyAdapter;
    private String mRoomValue, mHallValue, mToiletValue, mBalconyValue;
    private SelectWheelResultListener listener;
    //轮子的个数
    private int wheelNumber;

    public void setListener(SelectWheelResultListener listener) {
        this.listener = listener;
    }

    public SelectWheelDialog(Context mContext, int wheelNumber) {
        super(mContext, R.style.loading_dialog_themes);
        this.mContext = mContext;
        this.wheelNumber = wheelNumber;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_wheel);
        getWindow().setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.sure).setOnClickListener(this);
        findViewById(R.id.share_choice).setOnClickListener(this);

        roomView = (WheelVerticalView) findViewById(R.id.wv_room);
        hallView = (WheelVerticalView) findViewById(R.id.wvv_hall);
        balconyView = (WheelVerticalView) findViewById(R.id.wvv_balcon);
        toiletView = (WheelVerticalView) findViewById(R.id.wvv_toilet);
        if (wheelNumber == 2) {
            initTwoDialogView();
        } else if (wheelNumber == 4) {
            initFourDialogView();
        }
    }

    /**
     * 初始化两个轮子：几梯几户——x梯x户
     */
    private void initTwoDialogView() {
        balconyView.setVisibility(View.GONE);
        toiletView.setVisibility(View.GONE);

        roomAdapter = new NumericWheelAdapter(mContext, 1, 10, "%2d");
        roomAdapter.setItemResource(R.layout.adapter_select_wheel);
        roomAdapter.setItemTextResource(R.id.textView1);
        roomAdapter.setTextType("梯");
        roomView.setViewAdapter(roomAdapter);
        roomView.setCyclic(true);
        roomView.setCurrentItem(0);
        mRoomValue = "1梯";

        hallAdapter = new NumericWheelAdapter(mContext, 1, 20, "%2d");
        hallAdapter.setItemResource(R.layout.adapter_select_wheel);
        hallAdapter.setItemTextResource(R.id.textView1);
        hallAdapter.setTextType("户");
        hallView.setViewAdapter(hallAdapter);
        hallView.setCyclic(true);
        hallView.setCurrentItem(0);
        mHallValue = "1户";

        roomView.addChangingListener(new WheelChangedListener(ROOM));
        hallView.addChangingListener(new WheelChangedListener(HALL));
    }

    /**
     * 初始化四个轮子：户型——x室x厅x阳x卫
     */
    private void initFourDialogView() {
        roomAdapter = new NumericWheelAdapter(mContext, 1, 8, "%2d");
        roomAdapter.setItemResource(R.layout.adapter_select_wheel);
        roomAdapter.setItemTextResource(R.id.textView1);
        roomAdapter.setTextType("室");
        roomView.setViewAdapter(roomAdapter);
        roomView.setCyclic(true);
        roomView.setCurrentItem(0);
        mRoomValue = "1室";

        hallAdapter = new NumericWheelAdapter(mContext, 0, 5, "%2d");
        hallAdapter.setItemResource(R.layout.adapter_select_wheel);
        hallAdapter.setItemTextResource(R.id.textView1);
        hallAdapter.setTextType("厅");
        hallView.setViewAdapter(hallAdapter);
        hallView.setCyclic(true);
        hallView.setCurrentItem(0);
        mHallValue = "0厅";

        toiletAdapter = new NumericWheelAdapter(mContext, 0, 5, "%2d");
        toiletAdapter.setItemResource(R.layout.adapter_select_wheel);
        toiletAdapter.setItemTextResource(R.id.textView1);
        toiletAdapter.setTextType("卫");
        toiletView.setViewAdapter(toiletAdapter);
        toiletView.setCyclic(true);
        toiletView.setCurrentItem(0);
        mToiletValue = "0卫";

        balconyAdapter = new NumericWheelAdapter(mContext, 0, 5, "%2d");
        balconyAdapter.setItemResource(R.layout.adapter_select_wheel);
        balconyAdapter.setItemTextResource(R.id.textView1);
        balconyAdapter.setTextType("阳");
        balconyView.setViewAdapter(balconyAdapter);
        balconyView.setCyclic(true);
        balconyView.setCurrentItem(0);
        mBalconyValue = "0阳";

        roomView.addChangingListener(new WheelChangedListener(ROOM));
        hallView.addChangingListener(new WheelChangedListener(HALL));
        toiletView.addChangingListener(new WheelChangedListener(TOILET));
        balconyView.addChangingListener(new WheelChangedListener(BALCONY));
    }

    /**
     * @author <android-Jayu>
     * @Description: TODO(时间滚轮控件)
     * @date 2014年2月25日 上午10:59:50
     * @Email zongxu@pba.cn
     */
    class WheelChangedListener implements OnWheelChangedListener {
        private int tag;

        public WheelChangedListener(int tag) {
            this.tag = tag;
        }

        @Override
        public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
            Log.i("test", "newValue = " + newValue);
            switch (tag) {
                case ROOM:
                    if (wheelNumber == 2) {
                        mRoomValue = (newValue + 1) + "梯";
                    } else if (wheelNumber == 4) {
                        mRoomValue = (newValue + 1) + "室";
                    }
                    break;
                case HALL:
                    if (wheelNumber == 2) {
                        mHallValue = (newValue + 1) + "户";
                    } else if (wheelNumber == 4) {
                        mHallValue = newValue + "厅";
                    }
                    break;
                case BALCONY:
                    if (wheelNumber == 4) {
                        mToiletValue = newValue + "阳";
                    }
                    break;
                case TOILET:
                    if (wheelNumber == 4) {
                        mBalconyValue = newValue + "卫";
                    }
                    break;
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_choice:
            case R.id.cancel://取消
                dismiss();
                break;
            case R.id.sure://完成
                if (listener != null) {
                    if (wheelNumber == 2) {
                        listener.getSelectValue(mRoomValue + mHallValue);
                    } else if (wheelNumber == 4) {
                        listener.getSelectValue(mRoomValue + mHallValue + mToiletValue + mBalconyValue);
                    }
                }
                dismiss();
                break;
        }
    }

    public interface SelectWheelResultListener {
        void getSelectValue(String time);
    }

}
