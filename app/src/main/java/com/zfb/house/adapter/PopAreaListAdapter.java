package com.zfb.house.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.model.bean.AreaData;
import com.zfb.house.model.bean.AreaHolder;

import java.util.List;

/**
 * Created by Snekey on 2016/6/28.
 */
public class PopAreaListAdapter extends RecyclerView.Adapter {

    private List<AreaData> mData;
    private LayoutInflater inflater;
    private PopDataListener mPopDataListener;
    private int selectedColor;
    private int unSelectColor;
    private int p = -1;

    public PopDataListener getPopDataListener() {
        return mPopDataListener;
    }

    public void setPopDataListener(PopDataListener mPopDataListener) {
        this.mPopDataListener = mPopDataListener;
    }

    public interface PopDataListener {
        void getAreaData(String parentId);
    }

    public List<AreaData> getData() {
        return mData;
    }

    public void setData(List<AreaData> mData) {
        this.mData = mData;
    }

    public PopAreaListAdapter(Context context) {
        selectedColor = context.getResources().getColor(R.color.my_orange_one);
        unSelectColor = context.getResources().getColor(R.color.black);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_broker_area, parent, false);
        return new AreaHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        AreaHolder areaHolder = (AreaHolder) holder;
        final AreaData areaData = mData.get(position);
        areaHolder.position = position;
        areaHolder.tvArea.setText(areaData.name);
        if (areaData.isSelect) {
            areaHolder.tvArea.setTextColor(selectedColor);
        } else {
            areaHolder.tvArea.setTextColor(unSelectColor);
        }
        areaHolder.tvArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (p != -1 && p < mData.size()) {
                    mData.get(p).isSelect = false;
                }
                areaData.isSelect = true;
                p = position;
                mPopDataListener.getAreaData(areaData.id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ParamUtils.isEmpty(mData) ? 0 : mData.size();
    }
}
