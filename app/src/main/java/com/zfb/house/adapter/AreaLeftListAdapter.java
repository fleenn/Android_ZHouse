package com.zfb.house.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.model.bean.AreaData;

import java.util.List;

/**
 * 片区和小区左边数据的adapter
 * Created by Administrator on 2016/5/16.
 */
public class AreaLeftListAdapter extends BaseAdapter {

    private List<AreaData> mData;
    private LayoutInflater mLayoutInflater;

    public String getParentId(int position){
        return mData.get(position).id;
    }

    public AreaLeftListAdapter(Context mContext, List mData) {
        this.mData = mData;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return ParamUtils.isEmpty(mData) ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return ParamUtils.isEmpty(mData) ? null : mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_list_area_left, parent, false);//加载布局文件
            holder.txtLeft = (TextView) convertView.findViewById(R.id.txt_area_left);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (!ParamUtils.isNull(mData)) {
            AreaData item = mData.get(position);
            holder.txtLeft.setText(item.name);
        }
        return convertView;
    }

    class ViewHolder {
        TextView txtLeft;
    }
}
