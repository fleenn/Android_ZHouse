package com.zfb.house.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.model.bean.VillageData;

import java.util.List;

/**
 * 小区右边数据的adapter
 * Created by Administrator on 2016/5/16.
 */
public class VillageRightListAdapter extends BaseAdapter {
    private List<VillageData> mData;//数据
    private LayoutInflater mLayoutInflater;

    public VillageRightListAdapter(Context mContext, List mData) {
        this.mData = mData;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public VillageRightListAdapter(Context mContext){
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public List<VillageData> getData() {
        return mData;
    }

    public void setData(List<VillageData> mData) {
        this.mData = mData;
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
            convertView = mLayoutInflater.inflate(R.layout.item_list_area_right, parent, false);//加载布局文件
            holder.rlayoutItem = (RelativeLayout) convertView.findViewById(R.id.rlayout_area_item);
            holder.txtRight = (TextView) convertView.findViewById(R.id.txt_area_right);
            holder.viewIcon = convertView.findViewById(R.id.view_area_icon);
            holder.imgTick = (ImageView) convertView.findViewById(R.id.img_areas_tick);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mData != null && position < mData.size()) {
            final VillageData item = mData.get(position);
            holder.txtRight.setText(item.name);
            //更新选中与未选中的背景
            if (item.isSelect) {//选中
                holder.viewIcon.setVisibility(View.VISIBLE);
                holder.imgTick.setVisibility(View.VISIBLE);
            } else {//未选中
                holder.viewIcon.setVisibility(View.GONE);
                holder.imgTick.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    class ViewHolder {
        RelativeLayout rlayoutItem;
        TextView txtRight;
        View viewIcon;
        ImageView imgTick;
    }
}
