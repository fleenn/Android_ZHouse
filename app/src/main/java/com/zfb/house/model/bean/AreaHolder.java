package com.zfb.house.model.bean;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zfb.house.R;

/**
 * Created by Snekey on 2016/6/30.
 */
public class AreaHolder extends RecyclerView.ViewHolder{
    public TextView tvArea;
    public int position;

    public AreaHolder(View itemView) {
        super(itemView);

        tvArea = (TextView) itemView.findViewById(R.id.tv_area);

    }
}
