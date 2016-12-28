package com.zfb.house.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lemon.util.GlideUtil;
import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.emchat.ChatActivity;
import com.zfb.house.model.bean.GrabHouseItem;
import com.zfb.house.util.ToolUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Snekey on 2016/5/17.
 * 经纪人房源委托adpter
 */
public class BrokerHouseEntrustAdapter extends RecyclerView.Adapter {
    private List<GrabHouseItem> mDatas;
    private Context mContext;
    private List<String> mSelectHouseIds;
    private int sellOrRendType = 1;
    private boolean isEdit = false;
    private boolean isSelectAll;

    public int getSellOrRendType() {
        return sellOrRendType;
    }

    public void setSellOrRendType(int sellOrRendType) {
        this.sellOrRendType = sellOrRendType;
    }

    public List<String> getmSelectHouseIds() {
        return mSelectHouseIds;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public void setIsSelectAll(boolean isSelectAll) {
        this.isSelectAll = isSelectAll;
    }

    public void selectAll() {
        mSelectHouseIds.clear();
        for (int i = 0; i < mDatas.size(); i++) {
            mSelectHouseIds.add(mDatas.get(i).getId());
        }
    }

    public void unSelectAll() {
        mSelectHouseIds.clear();
    }

    public BrokerHouseEntrustAdapter(Context context, List<GrabHouseItem> datas) {
        this.mContext = context;
        mDatas = datas;
        mSelectHouseIds = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_entrust_house_broker, parent, false);
        return new EntrustViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final EntrustViewHolder viewHolder = (EntrustViewHolder) holder;
        final GrabHouseItem info = mDatas.get(position);

        viewHolder.tvCommunityName.setText(info.getCommuntityName());
        viewHolder.tvUserName.setText(info.getUserName());
        viewHolder.tvArea.setText(info.getArea() + "㎡");
        GlideUtil.getInstance().loadUrlEntrust(mContext, getPhotoUrl(info.getPhoto()), viewHolder.ivIcon);

        if (sellOrRendType == 1) {
            viewHolder.tvMoney.setText(mContext.getString(R.string.label_unit_million, info.getWishPrice()));
            viewHolder.ivSign.setBackgroundResource(R.drawable.icon_sign_sell);
        } else {
            viewHolder.tvMoney.setText(mContext.getString(R.string.label_unit_month, info.getRental()));
            viewHolder.ivSign.setBackgroundResource(R.drawable.icon_sign_rent);
        }

        setLayout(viewHolder, info);

        if (!TextUtils.isEmpty(info.getContactWay())) {
            viewHolder.tvPhone.setText(info.getContactWay());
        }
        if (isEdit) {
            viewHolder.cbEditSelect.setVisibility(View.VISIBLE);
            viewHolder.ivMessage.setVisibility(View.GONE);
        } else {
            viewHolder.cbEditSelect.setVisibility(View.GONE);
            viewHolder.ivMessage.setVisibility(View.VISIBLE);
        }

        Log.i("linwb", "size = " + mSelectHouseIds.size());
        viewHolder.cbEditSelect.setChecked(mSelectHouseIds.contains(info.getId()) ? true : false);

        final CheckBox checkBox = viewHolder.cbEditSelect;

        viewHolder.cbEditSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!mSelectHouseIds.contains(mDatas.get(position).getId())) {
                        mSelectHouseIds.add(mDatas.get(position).getId());
                    }
                    checkBox.setChecked(true);
                } else {
                    if (mSelectHouseIds.contains(mDatas.get(position).getId())) {
                        mSelectHouseIds.remove(mDatas.get(position).getId());
                    }
                    checkBox.setChecked(false);
                }
            }
        });

        viewHolder.ivMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userType = info.getUserType();
                userType = userType == 0 ? 1 : 2;
                ChatActivity.launch(mContext, userType, info.getContactUserId(), info.getContacts(), info.getContactPhoto());
            }
        });

    }

    private String getPhotoUrl(String photo) {
        if (TextUtils.isEmpty(photo)) {
            return "";
        }
        String[] photoArrays = photo.split(",");
        if (photoArrays != null && photoArrays.length > 0) {
            return photoArrays[0];
        }
        return "";
    }

    @Override
    public int getItemCount() {
        return ParamUtils.isEmpty(mDatas) ? 0 : mDatas.size();
    }

    private void setLayout(EntrustViewHolder entrustViewHolder, GrabHouseItem grabHouseItem) {
        String layout;
        if (!ParamUtils.isEmpty(grabHouseItem.getLayout())) {
            layout = ToolUtil.setHouseLayout(grabHouseItem.getLayout());
        } else if (!ParamUtils.isEmpty(grabHouseItem.getShopType())) {
            layout = grabHouseItem.getShopType();
        } else if (!ParamUtils.isEmpty(grabHouseItem.getOfficeType())) {
            layout = grabHouseItem.getOfficeType();
        } else {
            layout = "暂无";
        }
        entrustViewHolder.tvModel.setText(layout);
    }

    class EntrustViewHolder extends RecyclerView.ViewHolder {
        TextView tvCommunityName, tvUserName, tvArea, tvMoney, tvModel, tvPhone;
        ImageView ivIcon, ivSign, ivMessage;
        CheckBox cbEditSelect;

        public EntrustViewHolder(View itemView) {
            super(itemView);
            tvCommunityName = (TextView) itemView.findViewById(R.id.tv_house_name);
            tvUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
            tvArea = (TextView) itemView.findViewById(R.id.tv_area);
            tvModel = (TextView) itemView.findViewById(R.id.tv_model);
            tvMoney = (TextView) itemView.findViewById(R.id.tv_money);
            tvPhone = (TextView) itemView.findViewById(R.id.tv_phone);
            ivIcon = (ImageView) itemView.findViewById(R.id.img_entrust_header);
            ivSign = (ImageView) itemView.findViewById(R.id.iv_sign);
            ivMessage = (ImageView) itemView.findViewById(R.id.iv_message);
            cbEditSelect = (CheckBox) itemView.findViewById(R.id.cb_edit_select);
        }
    }
}
