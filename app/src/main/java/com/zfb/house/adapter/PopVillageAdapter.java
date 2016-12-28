package com.zfb.house.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.model.bean.AreaHolder;
import com.zfb.house.model.bean.VillageData;

import java.util.List;

import static com.zfb.house.adapter.PopAreaListAdapter.PopDataListener;

/**
 * Created by Snekey on 2016/6/28.
 */
public class PopVillageAdapter extends RecyclerView.Adapter {
    private List<VillageData> mData;
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

    public List<VillageData> getData() {
        return mData;
    }

    public void setData(List<VillageData> mData) {
        this.mData = mData;
    }

    public PopVillageAdapter(Context context) {
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
        final VillageData villageData = mData.get(position);
        areaHolder.position = position;
        areaHolder.tvArea.setText(villageData.name);
        if (villageData.isSelect) {
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
                villageData.isSelect = true;
                p = position;
                mPopDataListener.getAreaData(villageData.id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ParamUtils.isEmpty(mData) ? 0 : mData.size();
    }

}
