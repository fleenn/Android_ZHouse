package com.zfb.house.component;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.lemon.util.ScreenUtil;
import com.zfb.house.R;

/**
 * Created by Snekey on 2016/6/28.
 */
public class PopupArea {

    private PopupWindow mPopupWindow;
    private LoadMoreRecyclerView recyclerAreaList;
    private View vBottomView;

    public void setAdapter(RecyclerView.Adapter adapter) {
        recyclerAreaList.setAdapter(adapter);
    }

    public PopupArea(Context context, View vBottomView) {
        this.vBottomView = vBottomView;
        final View view = LayoutInflater.from(context).inflate(R.layout.popup_select_area, null);
        int height = ScreenUtil.dip2px(context, 196);
        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, height);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        mPopupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.shape_bg_pop_area));
        recyclerAreaList = (LoadMoreRecyclerView) view.findViewById(R.id.recycler_area_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerAreaList.setLayoutManager(layoutManager);
        recyclerAreaList.setHasFixedSize(true);

    }

    public void show() {
        mPopupWindow.showAsDropDown(vBottomView);
    }

    public void dismiss() {
        mPopupWindow.dismiss();
    }
}
