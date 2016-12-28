package com.zfb.house.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.model.bean.DayTaskData;

import java.util.HashMap;
import java.util.List;

/**
 * 每日任务/新手任务的adapter
 * Created by Administrator on 2016/8/11.
 */
public class DayTaskAdapter extends BaseAdapter {

    private Context mContext;
    private List<DayTaskData> mData;
    private LayoutInflater mLayoutInflater;

    public DayTaskAdapter(Context mContext, List<DayTaskData> mData) {
        this.mContext = mContext;
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
        return ParamUtils.isEmpty(mData) ? 0 : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_list_task_day, parent, false);
            holder.txtTaskName = (TextView) convertView.findViewById(R.id.txt_task_name);
            holder.txtTaskDetail = (TextView) convertView.findViewById(R.id.txt_task_detail);
            holder.txtTaskCoin = (TextView) convertView.findViewById(R.id.txt_task_coin);
            holder.txtTAskState = (TextView) convertView.findViewById(R.id.txt_task_state);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (!ParamUtils.isNull(mData)) {
            DayTaskData item = mData.get(position);
            if (!ParamUtils.isEmpty(item.getComment())) {
                //注意|是特殊字符，主要使用转义才能起到分割的作用
                String[] task = item.getComment().split("\\|");
                holder.txtTaskName.setText(task[0]);
                holder.txtTaskDetail.setText(task[1]);
            }
            holder.txtTaskCoin.setText(String.valueOf(item.getTaskPoint()));
            if (item.getDoneTime() == 0) {//doneTime为0就代表未完成
                holder.txtTAskState.setText("未完成");
                holder.txtTAskState.setSelected(false);
            } else {//否则为已完成
                holder.txtTAskState.setText("已完成");
                holder.txtTAskState.setSelected(true);
            }
        }
        return convertView;
    }

    class ViewHolder {
        TextView txtTaskName;//任务名
        TextView txtTaskDetail;//任务详情
        TextView txtTaskCoin;//任务所得积分
        TextView txtTAskState;//任务状态
    }

}
