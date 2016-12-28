package com.zfb.house.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lemon.LemonActivity;
import com.lemon.util.GlideUtil;
import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.model.bean.EntrustBrokerInfo;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.ui.JudgeBrokerActivity;

import java.util.List;

/**
 * Created by linwenbing on 16/6/20.
 */
public class EntrustBrokerAdapter extends BaseAdapter {

    private Context mContext;
    private List<EntrustBrokerInfo> mDatas;

    public EntrustBrokerAdapter(Context context) {
        mContext = context;
    }

    public void setDatas(List<EntrustBrokerInfo> mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return ParamUtils.isEmpty(mDatas) ? 0 : mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return ParamUtils.isEmpty(mDatas) ? null : mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder viewHold = null;
        if (null == view) {
            viewHold = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_entrus_brokers, null);
            viewHold.user_head = (ImageView) view.findViewById(R.id.iv_header);
            viewHold.user_name = (TextView) view.findViewById(R.id.tv_broker_name);
            viewHold.tvJudge = (TextView) view.findViewById(R.id.tv_broker_price);
            view.setTag(viewHold);
        } else {
            viewHold = (ViewHolder) view.getTag();
        }

        final EntrustBrokerInfo info = mDatas.get(position);
        GlideUtil.getInstance().loadUrl(mContext, info.getBrokerPhoto(), viewHold.user_head);
        viewHold.user_name.setText(info.getBrokerName());
        /**
         * “评价”点击事件
         */
        viewHold.tvJudge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, JudgeBrokerActivity.class);
                intent.putExtra("id", info.getBrokerId());
                mContext.startActivity(intent);
            }
        });
        /**
         * 详情页跳转
         */
        viewHold.user_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LemonActivity)mContext).toDetail("1",mDatas.get(position).getBrokerId(),"");
            }
        });
        return view;
    }

    private class ViewHolder {
        ImageView user_head;
        TextView user_name, tvJudge;
    }

}
