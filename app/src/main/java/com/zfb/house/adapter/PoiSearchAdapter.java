package com.zfb.house.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.lemon.util.ParamUtils;
import com.zfb.house.R;

import java.util.List;

/**
 * Created by Snekey on 2016/6/1.
 */
public class PoiSearchAdapter extends BaseAdapter {

    private Context mContext;
    private List<PoiInfo> mDate;
    private LayoutInflater mLayoutInflater;

    public PoiSearchAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setmDate(List<PoiInfo> mDate) {
        this.mDate = mDate;
    }

    @Override
    public int getCount() {
        return ParamUtils.isEmpty(mDate) ? 0 : mDate.size();
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
        final ViewHolder holder;
        final PoiInfo poiInfo = mDate.get(position);
        if (convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.item_list_poi,null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_poi_name);
            holder.tv_adress = (TextView) convertView.findViewById(R.id.tv_poi_adress);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.tv_name.setText(poiInfo.name);
        holder.tv_adress.setText(poiInfo.address);
        return convertView;
    }

    private class ViewHolder{
        private TextView tv_name;
        private TextView tv_adress;
    }


}
