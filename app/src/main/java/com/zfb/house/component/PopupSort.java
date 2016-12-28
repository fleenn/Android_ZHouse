package com.zfb.house.component;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.lemon.util.ScreenUtil;
import com.zfb.house.R;

/**
 * 排序
 * Created by Snekey on 2016/7/1.
 */
public class PopupSort implements View.OnClickListener {
    private PopupWindow mPopupWindow;
    private View vBottomView;
    private ImageView imgStarTick;
    private ImageView imgDealTick;
    private ImageView imgCountTick;
    private OnSortListener mOnSortListener;
    private Context mContext;

    public OnSortListener getmOnSortListener() {
        return mOnSortListener;
    }

    public void setmOnSortListener(OnSortListener mOnSortListener) {
        this.mOnSortListener = mOnSortListener;
    }

    public interface OnSortListener {
        void toSort(int orderType);
    }

    public PopupSort(Context context, View vBottomView) {
        this.mContext = context;
        this.vBottomView = vBottomView;
        View view = LayoutInflater.from(context).inflate(R.layout.popup_sort, null);
        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        view.findViewById(R.id.rlayout_star).setOnClickListener(this);
        view.findViewById(R.id.rlayout_deal_count).setOnClickListener(this);
        view.findViewById(R.id.rlayout_praise_count).setOnClickListener(this);
        imgStarTick = (ImageView) view.findViewById(R.id.img_star_tick);
        imgDealTick = (ImageView) view.findViewById(R.id.img_deal_tick);
        imgCountTick = (ImageView) view.findViewById(R.id.img_count_tick);
    }

    public void show() {
        mPopupWindow.showAsDropDown(vBottomView, ScreenUtil.dip2px(mContext, -60), ScreenUtil.dip2px(mContext, -9));
    }

    public void dismiss() {
        mPopupWindow.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlayout_star:
                toCheck(5);
                mOnSortListener.toSort(5);
                break;
            case R.id.rlayout_deal_count:
                toCheck(4);
                mOnSortListener.toSort(4);
                break;
            case R.id.rlayout_praise_count:
                toCheck(3);
                mOnSortListener.toSort(3);
                break;
        }
    }

    private void toCheck(int index) {
        imgStarTick.setVisibility(index == 5 ? View.VISIBLE : View.GONE);
        imgDealTick.setVisibility(index == 4 ? View.VISIBLE : View.GONE);
        imgCountTick.setVisibility(index == 3 ? View.VISIBLE : View.GONE);
    }
}
