package com.zfb.house.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.model.bean.UserRequirement;

import java.util.List;

/**
 * Created by Snekey on 2016/10/20.
 */
public class UserPurposeAdapter extends RecyclerView.Adapter {

    private List<UserRequirement> mData;
    private Context mContext;

    public UserPurposeAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public List<UserRequirement> getData() {
        return mData;
    }

    public void setData(List<UserRequirement> mData) {
        this.mData = mData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_user_purpose_house, null);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        UserRequirement userRequirement = mData.get(position);
        viewHolder.tvTime.setText(userRequirement.getApplyTime());
        viewHolder.tvDistrict.setText(userRequirement.getAreaName());
        viewHolder.tvCommunity.setText(userRequirement.getVillageName());
        viewHolder.tvLayout.setText(userRequirement.getRoomType());
        viewHolder.tvArea.setText(userRequirement.getArea());
        viewHolder.tvMoney.setText(userRequirement.getCash());
        viewHolder.gvBrokers.setAdapter(new AcceptedBrokersAdapter(mContext,userRequirement.getWeiPaiUserList()));
    }

    @Override
    public int getItemCount() {
        return ParamUtils.isEmpty(mData)?0:mData.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTime;
        private TextView tvDistrict;
        private TextView tvCommunity;
        private TextView tvLayout;
        private TextView tvArea;
        private TextView tvMoney;
        private TextView tvDescription;
        private GridView gvBrokers;
        public int position;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvDistrict = (TextView) itemView.findViewById(R.id.tv_district);
            tvCommunity = (TextView) itemView.findViewById(R.id.tv_community);
            tvLayout = (TextView) itemView.findViewById(R.id.tv_layout);
            tvArea = (TextView) itemView.findViewById(R.id.tv_area);
            tvMoney = (TextView) itemView.findViewById(R.id.tv_money);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_description);
            gvBrokers = (GridView) itemView.findViewById(R.id.gv_brokers);

            tvDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage(mData.get(position).getDescription());
                    builder.setTitle("其他说明");
                    builder.setCancelable(false);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
            });
        }
    }
}
