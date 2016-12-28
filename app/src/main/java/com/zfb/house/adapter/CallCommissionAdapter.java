package com.zfb.house.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.model.bean.CallCommission;

import java.util.List;

/**
 * Created by Snekey on 2016/7/29.
 */
public class CallCommissionAdapter extends BaseAdapter{

    private List<CallCommission> mData;
    private LayoutInflater mLayoutInflater;

    public List<CallCommission> getData() {
        return mData;
    }

    public void setData(List<CallCommission> mData) {
        this.mData = mData;
    }

    public CallCommissionAdapter(Context context) {
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return ParamUtils.isEmpty(mData) ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        CallCommission callCommission = mData.get(position);
        if (convertView == null){
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_list_commission,null);
            holder.tvSelection = (TextView) convertView.findViewById(R.id.tv_selection);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvSelection.setText(callCommission.getName());
        holder.tvSelection.setSelected(callCommission.isSelected());
        return convertView;
    }

    private class ViewHolder{
        private TextView tvSelection;
    }
}
