package com.zfb.house.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.model.SearchHistoryModel;
import com.zfb.house.ui.SearchResultActivity;

import java.util.List;

/**
 * Created by Snekey on 2016/6/8.
 */
public class SearchHistoryAdapter extends BaseAdapter {
    private Context mContext;
    private List<SearchHistoryModel> mData;
    private int brokerType = 1;

    public int getBrokerType() {
        return brokerType;
    }

    public void setBrokerType(int brokerType) {
        this.brokerType = brokerType;
    }

    public List<SearchHistoryModel> getData() {
        return mData;
    }

    public void setData(List<SearchHistoryModel> mData) {
        this.mData = mData;
    }

    public SearchHistoryAdapter(Context mContext) {
        this.mContext = mContext;
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
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_search_history,null);
            holder.tvHistoryName = (TextView) convertView.findViewById(R.id.tv_search_name);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvHistoryName.setText(mData.get(position).getHistoryName());
        holder.tvHistoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SearchResultActivity.class);
                intent.putExtra("id", mData.get(position).getHistoryId());
                intent.putExtra("brokerType", brokerType);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        private TextView tvHistoryName;
    }

}
