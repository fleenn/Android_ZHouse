package com.zfb.house.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lemon.util.GlideUtil;
import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.emchat.ChatActivity;
import com.zfb.house.model.bean.GrabHouseItem;
import com.zfb.house.util.ToolUtil;

import java.util.List;

/**
 * Created by Snekey on 2016/6/18.
 */
public class GrabHouseAdapter extends RecyclerView.Adapter{

    private List<GrabHouseItem> mData;
    private Context mContext;
    private boolean isGrabbedList;
    private int type = 0;//0:rent;1:sell
    private OnGrabHouseListener onGrabListener;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public void addDatas(List<GrabHouseItem> datas) {
        mData.addAll(datas);
    }

    public void setIsGrabbedList(boolean isGrabbedList) {
        this.isGrabbedList = isGrabbedList;
    }

    public void setOnGrabListener(OnGrabHouseListener onGrabListener) {
        this.onGrabListener = onGrabListener;
    }

    public GrabHouseAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public List<GrabHouseItem> getData() {
        return mData;
    }

    public void setData(List<GrabHouseItem> mData) {
        this.mData = mData;
    }

    public interface OnGrabHouseListener{
        void toGrabHouseRent(int position);
        void toGrabHouseSell(int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_grab_resources, null);
        return new GrabHouseHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        GrabHouseHolder grabHouseHolder = (GrabHouseHolder) holder;
        final GrabHouseItem grabHouseItem = mData.get(position);
        Glide.with(mContext).load(grabHouseItem.getPhoto()).placeholder(R.drawable.defalut_shop_list).into(grabHouseHolder.imgHouseIcon);
        grabHouseHolder.tvCommunicate.setText(grabHouseItem.getCommuntityName());
//        租房还是售房
        if (type == 0){
            grabHouseHolder.tvPrice.setText(mContext.getString(R.string.label_unit_month, grabHouseItem.getRental()));
            grabHouseHolder.imgTypeRent.setVisibility(View.VISIBLE);
            grabHouseHolder.imgTypeSell.setVisibility(View.GONE);
        }else {
            grabHouseHolder.tvPrice.setText(mContext.getString(R.string.label_unit_million, grabHouseItem.getWishPrice()));
            grabHouseHolder.imgTypeRent.setVisibility(View.GONE);
            grabHouseHolder.imgTypeSell.setVisibility(View.VISIBLE);
        }
//        判断已抢还是未抢列表
        if (isGrabbedList){
            grabHouseHolder.imgGrabChat.setVisibility(View.VISIBLE);
            grabHouseHolder.imgGrabStatus.setVisibility(View.GONE);
            grabHouseHolder.tvTel.setText(grabHouseItem.getContactWay());
            grabHouseHolder.imgGrabChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int userType = grabHouseItem.getUserType();
                    if (userType == 0){
                        userType = 1;
                    }else {
                        userType = 2;
                    }
                    ChatActivity.launch(mContext, userType, grabHouseItem.getContactUserId(), grabHouseItem.getContacts(), grabHouseItem.getContactPhoto());
                }
            });
        }else {
            grabHouseHolder.imgGrabStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type == 0) {
                        onGrabListener.toGrabHouseRent(position);
                        v.setClickable(false);
                    } else {
                        onGrabListener.toGrabHouseSell(position);
                        v.setClickable(false);
                    }
                }
            });
            grabHouseHolder.imgGrabChat.setVisibility(View.GONE);
            grabHouseHolder.imgGrabStatus.setVisibility(View.VISIBLE);
            if (grabHouseItem.isSuccess()){
                grabHouseHolder.imgGrabStatus.setSelected(true);
                grabHouseHolder.imgGrabStatus.setClickable(false);
            } else {
                grabHouseHolder.imgGrabStatus.setSelected(false);
            }
        }
        setLayout(grabHouseHolder, grabHouseItem);
        grabHouseHolder.tvArea.setText(String.valueOf(grabHouseItem.getArea()));
        grabHouseHolder.tvCustomerName.setText(grabHouseItem.getContacts());
    }

    @Override
    public int getItemCount() {
        return ParamUtils.isEmpty(mData) ? 0 : mData.size();
    }

    private void setLayout(GrabHouseHolder grabHouseHolder,GrabHouseItem grabHouseItem){
        String layout;
        if (!ParamUtils.isEmpty(grabHouseItem.getLayout())){
            layout = ToolUtil.setHouseLayout(grabHouseItem.getLayout());
        }else if (!ParamUtils.isEmpty(grabHouseItem.getShopType())){
            layout = grabHouseItem.getShopType();
        }else if (!ParamUtils.isEmpty(grabHouseItem.getOfficeType())){
            layout = grabHouseItem.getOfficeType();
        }else {
            layout = "暂无";
        }
        grabHouseHolder.tvLayout.setText(layout);
    }
    private class GrabHouseHolder extends RecyclerView.ViewHolder{

        private ImageView imgHouseIcon;
        private TextView tvCommunicate;
        private TextView tvLayout;
        private TextView tvArea;
        private TextView tvPrice;
        private ImageView imgGrabChat;
        private ImageView imgGrabStatus;
        private ImageView imgTypeRent;
        private ImageView imgTypeSell;
        private TextView tvCustomerName;
        private TextView tvTel;

        public GrabHouseHolder(View itemView) {
            super(itemView);

            imgHouseIcon = (ImageView) itemView.findViewById(R.id.img_house_icon);
            tvCommunicate = (TextView) itemView.findViewById(R.id.tv_communicate);
            tvLayout = (TextView) itemView.findViewById(R.id.tv_layout);
            tvArea = (TextView) itemView.findViewById(R.id.tv_area);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            imgGrabChat = (ImageView) itemView.findViewById(R.id.img_grab_chat);
            imgGrabStatus = (ImageView) itemView.findViewById(R.id.img_grab_status);
            imgTypeRent = (ImageView) itemView.findViewById(R.id.img_type_rent);
            imgTypeSell = (ImageView) itemView.findViewById(R.id.img_type_sell);
            tvCustomerName = (TextView) itemView.findViewById(R.id.tv_customer_name);
            tvTel = (TextView) itemView.findViewById(R.id.tv_tel);
        }
    }
}
