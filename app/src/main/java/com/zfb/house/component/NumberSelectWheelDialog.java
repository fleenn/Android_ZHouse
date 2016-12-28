package com.zfb.house.component;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.zfb.house.R;
import com.zfb.house.component.Wheel.AbstractWheel;
import com.zfb.house.component.Wheel.OnWheelChangedListener;
import com.zfb.house.component.Wheel.WheelVerticalView;
import com.zfb.house.component.Wheel.adapter.NumericWheelAdapter;


public class NumberSelectWheelDialog extends Dialog implements View.OnClickListener{
	private static final int ROOM = 0;
	private static final int HALL = 1;
	private static final int TOILET = 2;
	private static final int BALCONY = 3;

	private Context mContext;
	private WheelVerticalView wheelVerticalView;
	private NumericWheelAdapter arrayWheelAdapter;
	private String mSelectValue;

	public void setListener(SelectWheelResultListener listener) {
		this.listener = listener;
	}

	private SelectWheelResultListener listener;
	private int mMax,mMin;
	private String mType;

	public NumberSelectWheelDialog(Context context) {
		super(context, R.style.loading_dialog_themes);
		mContext = context;
	}

	public NumberSelectWheelDialog(Context context, int min,int max,String type) {
		super(context, R.style.loading_dialog_themes);
		mContext = context;
		mMin = min;
		mMax = max;
		mType = type;

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

	private void initDialogView(){
		wheelVerticalView = (WheelVerticalView) findViewById(R.id.wv_select);
		arrayWheelAdapter = new NumericWheelAdapter(mContext,mMin,mMax,"%2d");
		arrayWheelAdapter.setItemResource(R.layout.adapter_select_wheel);
		arrayWheelAdapter.setItemTextResource(R.id.textView1);
		wheelVerticalView.setViewAdapter(arrayWheelAdapter);
		wheelVerticalView.setCyclic(true);
		arrayWheelAdapter.setTextType(mType);
		wheelVerticalView.setCurrentItem(0);
		mSelectValue = mMin  + mType;
		wheelVerticalView.addChangingListener(new WheelChangedListener());
	}

	/**
	 * @Description: TODO(时间滚轮控件)
	 */
	class WheelChangedListener implements OnWheelChangedListener {

		public WheelChangedListener() {
		}

		@Override
		public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
			mSelectValue = (mMin + newValue) + mType;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.share_choice:
			case R.id.cancel:
				dismiss();
				break;
			case R.id.sure:
				if (listener != null){
					listener.getSelectValue(mSelectValue);
				}

				dismiss();
				break;
		}
	}

	public interface SelectWheelResultListener {
		void getSelectValue(String time);
	}
}
