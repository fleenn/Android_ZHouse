package com.zfb.house.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lemon.util.GlideUtil;
import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.model.bean.EntrustItem;
import com.zfb.house.util.ToolUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Snekey on 2016/5/17.
 * 我的委托 or 接受的委托 adapter
 */
public class HouseEntrustAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<EntrustItem> mDatas;
    private boolean isEdit = false;
    private boolean isSelectAll;
    private int sellOrRent = 1;//1售2租
    private OnDeleteClick onDeleteClick;
    private EntrustBrokerAdapter adapter;
    private List<String> mSelectHouseIds;

    public List<String> getmSelectHouseIds() {
        return mSelectHouseIds;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public int getSellOrRent() {
        return sellOrRent;
    }

    public void setSellOrRent(int sellOrRent) {
        this.sellOrRent = sellOrRent;
    }

    public void setIsSelectAll(boolean isSelectAll) {
        this.isSelectAll = isSelectAll;
    }

    public void setOnDeleteClick(OnDeleteClick onDeleteClick) {
        this.onDeleteClick = onDeleteClick;
    }

    public List<EntrustItem> getDatas() {
        return mDatas;
    }

    public void setDatas(List<EntrustItem> mDatas) {
        this.mDatas = mDatas;
    }

    public HouseEntrustAdapter(Context context, List<EntrustItem> datas) {
        this.mContext = context;
        mDatas = datas;
        mSelectHouseIds = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_entrust_house_user, parent, false);
        return new EntrustViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final EntrustViewHolder viewHolder = (EntrustViewHolder) holder;
        final EntrustItem info = mDatas.get(position);

        viewHolder.tvCommunityName.setText(info.getCommuntityName());
        viewHolder.tvArea.setText(info.getArea() + "㎡");
        GlideUtil.getInstance().loadUrlEntrust(mContext, getPhotoUrl(info.getPhoto()), viewHolder.ivIcon);
        if (!TextUtils.isEmpty(info.getResourceType()) && (info.getResourceType().equals("1") || info.getResourceType().equals("2"))) {
            viewHolder.tvModel.setText(ToolUtil.setHouseLayout(info.getLayout()));
            Log.i("linwb", "td===== " + ToolUtil.setHouseLayout(info.getLayout()));
        } else if (!TextUtils.isEmpty(info.getResourceType()) && info.getResourceType().equals("3")) {
            viewHolder.tvModel.setText(info.getOfficeTypeName());
        } else {
            viewHolder.tvModel.setText(info.getShopTypeName());
        }
        if (sellOrRent == 2) {
            if (!TextUtils.isEmpty(info.getRental())) {
                viewHolder.tvMoney.setText(info.getRental() + "元/月");
            }
            viewHolder.ivSign.setBackgroundResource(R.drawable.icon_sign_rent);
        } else {
            if (!TextUtils.isEmpty(info.getWishPrice())) {
                viewHolder.tvMoney.setText(info.getWishPrice() + "万元");
            }
            viewHolder.ivSign.setBackgroundResource(R.drawable.icon_sign_sell);
        }

        viewHolder.tvDelete.setVisibility(isEdit ? View.VISIBLE : View.GONE);

        if (info.getBrokersList() != null && info.getBrokersList().size() > 0) {
            viewHolder.gvContacts.setVisibility(View.VISIBLE);
            if (adapter == null) {
                adapter = new EntrustBrokerAdapter(mContext);
                adapter.setDatas(info.getBrokersList());
                viewHolder.gvContacts.setAdapter(adapter);
            } else {
                adapter.setDatas(info.getBrokersList());
                viewHolder.gvContacts.setAdapter(adapter);
            }
        } else {
            viewHolder.gvContacts.setVisibility(View.GONE);
        }
        viewHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("linwb", "click = " + info.getId());
                onDeleteClick.onResult(info.getId());
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

    class EntrustViewHolder extends RecyclerView.ViewHolder {
        TextView tvCommunityName, tvDelete, tvArea, tvMoney, tvModel;
        ImageView ivIcon, ivSign;
        GridView gvContacts;

        public EntrustViewHolder(View itemView) {
            super(itemView);
            tvCommunityName = (TextView) itemView.findViewById(R.id.tv_house_name);
            tvArea = (TextView) itemView.findViewById(R.id.tv_area);
            tvModel = (TextView) itemView.findViewById(R.id.tv_model);
            tvMoney = (TextView) itemView.findViewById(R.id.tv_money);
            ivIcon = (ImageView) itemView.findViewById(R.id.img_entrust_header);
            ivSign = (ImageView) itemView.findViewById(R.id.iv_sign);
            gvContacts = (GridView) itemView.findViewById(R.id.gv_contacts);
            tvDelete = (TextView) itemView.findViewById(R.id.tv_delete);
        }
    }

    public interface OnDeleteClick {
        void onResult(String id);
    }

}
