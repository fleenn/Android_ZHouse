package com.zfb.house.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lemon.LemonActivity;
import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.model.bean.UserRequirement;
import com.zfb.house.ui.JudgeBrokerActivity;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Snekey on 2016/10/20.
 */
public class AcceptedBrokersAdapter extends BaseAdapter{

    private List<UserRequirement.WeiPaiUserListBean> mData;
    private LayoutInflater inflater;
    private Context mContext;

    public AcceptedBrokersAdapter(Context context, List<UserRequirement.WeiPaiUserListBean> list) {
        this.mContext = context;
        mData = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return ParamUtils.isEmpty(mData)?0:mData.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder  holder;
        final UserRequirement.WeiPaiUserListBean data = mData.get(position);
        if (convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_entrus_brokers,null);
            holder.ivHeader = (ImageView) convertView.findViewById(R.id.iv_header);
            holder.tvBrokerName = (TextView) convertView.findViewById(R.id.tv_broker_name);
            holder.tvBrokerPrice = (TextView) convertView.findViewById(R.id.tv_broker_price);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(mContext).load(data.getPhoto()).placeholder(R.drawable.default_avatar).into(holder.ivHeader);
        holder.ivHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LemonActivity)mContext).toDetail("0",mData.get(position).getId(),"");
            }
        });
        holder.tvBrokerName.setText(data.getName());
        holder.tvBrokerPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, JudgeBrokerActivity.class);
                intent.putExtra("id", data.getId());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder{
        private ImageView ivHeader;
        private TextView tvBrokerName;
        private TextView tvBrokerPrice;
    }
}
