package com.zfb.house.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.emchat.Constant;
import com.baidu.mapapi.model.LatLng;
import com.lemon.LemonLocation;
import com.lemon.event.AnonLoginEvent;
import com.lemon.util.EventUtil;
import com.lemon.util.GlideUtil;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.component.RatingBar;
import com.zfb.house.emchat.ChatActivity;
import com.zfb.house.emchat.temp.CertificationBean;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.util.ToolUtil;

import java.util.List;

/**
 * Created by Snekey on 2016/6/27.
 */
public class BrokersDetailAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<UserBean> mData;
    private LayoutInflater mInflater;
    private OnOperateListener mOnOperateListener;
    private OnTouchDetailListener mOnTouchDetailListener;
    private LatLng mMineLL;//我的坐标
    private int countType = 2;//0:租；1:售；2：租和售
    private boolean showBrokerType;//是否展示经纪人类型
    private boolean isEdit;

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public void setShowBrokerType(boolean showBrokerType) {
        this.showBrokerType = showBrokerType;
    }

    public void setCountType(int countType) {
        this.countType = countType;
    }

    public OnOperateListener getmOnOperateListener() {
        return mOnOperateListener;
    }

    public void setmOnOperateListener(OnOperateListener mOnOperateListener) {
        this.mOnOperateListener = mOnOperateListener;
    }

    public OnTouchDetailListener getmOnTouchDetailListener() {
        return mOnTouchDetailListener;
    }

    public void setmOnTouchDetailListener(OnTouchDetailListener mOnTouchDetailListener) {
        this.mOnTouchDetailListener = mOnTouchDetailListener;
    }

    public interface OnOperateListener {
        void toOperate(UserBean item, int position);
    }

    public interface OnTouchDetailListener {
        void toPersonalDetail(int position);
    }

    public List<UserBean> getData() {
        return mData;
    }

    public void setData(List<UserBean> mData) {
        this.mData = mData;
    }

    public void addData(List<UserBean> mData) {
        this.mData.addAll(mData);
    }

    public BrokersDetailAdapter(Context mContext) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_list_broker, null);
        return new BrokersHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        BrokersHolder brokersHolder = (BrokersHolder) holder;
        final UserBean item = mData.get(position);
        brokersHolder.position = position;
        //头像
        GlideUtil.getInstance().loadUrl(mContext, item.photo, brokersHolder.imgAvatar);
        //星级
        brokersHolder.rbStar.setStar(item.star);
        //姓名
        brokersHolder.txtName.setText(ParamUtils.isEmpty(item.remark) ? item.name : item.remark);
        //专家类型
        setBrokerType(brokersHolder, item);
        //所属公司
        brokersHolder.txtCompany.setText(item.companyName);
        //设置距离
        setDistance(brokersHolder, item);
        //服务片区
        brokersHolder.txtDistrict.setText(ToolUtil.convertDot(item.serviceDistrictName));
        //服务小区
        brokersHolder.txtVillage.setText(ToolUtil.convertDot(item.serviceVillageName));
        //设置成交量
        setDealCount(brokersHolder, item);
        //好评率
        brokersHolder.txtSatisfyDegree.setText((ParamUtils.isEmpty(item.satisfyDegree) ? "0" : item.satisfyDegree) + "%");
        //删除
        brokersHolder.rlayoutOperate.setVisibility(isEdit ? View.VISIBLE : View.GONE);
        brokersHolder.txtOperate.setText(R.string.delete);
        brokersHolder.rlayoutOperate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnOperateListener.toOperate(item, position);
            }
        });
        //设置认证图标
        setCertification(brokersHolder, item);
    }

    /**
     * 设置是否显示经纪人类型
     *
     * @param holder
     * @param bean
     */
    private void setBrokerType(BrokersHolder holder, UserBean bean) {
        String expertType = "";
        if (showBrokerType) {
            switch (bean.expertType) {
                case "0"://无
                    expertType = "";
                    break;
                case "1"://售房专家
                    expertType = "售";
                    break;
                case "2"://租房专家
                    expertType = "租";
                    break;
                case "3"://租售专家
                    expertType = "租/售";
                    break;
            }
        } else {
            expertType = "";
        }
        holder.txtType.setText(expertType);
    }

    /**
     * 设置认证图标
     *
     * @param brokersHolder
     * @param bean
     */
    private void setCertification(BrokersHolder brokersHolder, UserBean bean) {
        brokersHolder.imgRealName.setVisibility(View.GONE);
        brokersHolder.imgCard.setVisibility(View.GONE);
        brokersHolder.imgQualification.setVisibility(View.GONE);
        if (!ParamUtils.isEmpty(bean.certifications)) {
            for (CertificationBean cb : bean.certifications) {
                if (cb.auditState.equals("2")) {//如果审核通过的话
                    switch (cb.certificationType) {
                        case "1"://实名认证
                            brokersHolder.imgRealName.setVisibility(View.VISIBLE);
                            break;
                        case "2"://名片认证
                            brokersHolder.imgCard.setVisibility(View.VISIBLE);
                            break;
                        case "3"://资质认证
                            brokersHolder.imgQualification.setVisibility(View.VISIBLE);
                            break;
                    }
                } else {
                    switch (cb.certificationType) {
                        case "1"://实名认证
                            brokersHolder.imgRealName.setVisibility(View.GONE);
                            break;
                        case "2"://名片认证
                            brokersHolder.imgCard.setVisibility(View.GONE);
                            break;
                        case "3"://资质认证
                            brokersHolder.imgQualification.setVisibility(View.GONE);
                            break;
                    }
                }
            }
        }
    }

    /**
     * 设置距离
     *
     * @param holder
     * @param bean
     */
    private void setDistance(BrokersHolder holder, UserBean bean) {
        LatLng mBrokerLL = new LatLng(bean.lat, bean.lng);
        String distance = String.valueOf(LemonLocation.getDistance(mMineLL, mBrokerLL));
        if (distance.length() >= 4) {
            distance = distance.substring(0, 4);
        }
        holder.txtRange.setText(distance + "km");
    }

    /**
     * 设置成交量
     *
     * @param holder
     * @param bean
     */
    private void setDealCount(BrokersHolder holder, UserBean bean) {
        String dealCount = "";
        switch (countType) {
            case 0:
                dealCount = bean.rentVolume;
                break;
            case 1:
                dealCount = bean.sellVolume;
                break;
            case 2:
                dealCount = String.valueOf(Integer.valueOf(bean.rentVolume) + Integer.valueOf(bean.sellVolume));
                break;
        }
        holder.txtTrading.setText(dealCount + "套");
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return ParamUtils.isEmpty(mData) ? 0 : mData.size();
    }

    private class BrokersHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;//头像
        RatingBar rbStar;//星级
        TextView txtName;//姓名
        TextView txtType;//类型——租/售
        TextView txtCompany;//所属公司
        TextView txtRange;//距离
        TextView txtDistrict;//服务片区
        TextView txtVillage;//服务小区
        TextView txtTrading;//成交量
        TextView txtSatisfyDegree;//好评率
        ImageView imgRealName;//实名认证图标
        ImageView imgQualification;//资质认证图标
        ImageView imgCard;//名片认证图标
        RelativeLayout rlayoutOperate;
        TextView txtOperate;
        RelativeLayout rlayoutChat;//聊天按钮
        RelativeLayout llayoutDetail;//item
        private int position;

        public BrokersHolder(View itemView) {
            super(itemView);

            double lat = SettingUtils.get(mContext, "lat", Constant.DEFAULT_LAT);
            double lng = SettingUtils.get(mContext, "lng", Constant.DEFAULT_LNG);
            mMineLL = new LatLng(lat, lng);

            imgAvatar = (ImageView) itemView.findViewById(R.id.img_avatar);
            rbStar = (RatingBar) itemView.findViewById(R.id.rb_star);
            txtName = (TextView) itemView.findViewById(R.id.txt_name);
            txtType = (TextView) itemView.findViewById(R.id.txt_type);
            txtCompany = (TextView) itemView.findViewById(R.id.txt_company);
            txtRange = (TextView) itemView.findViewById(R.id.txt_range);
            txtDistrict = (TextView) itemView.findViewById(R.id.txt_district);
            txtVillage = (TextView) itemView.findViewById(R.id.txt_village);
            txtTrading = (TextView) itemView.findViewById(R.id.txt_trading);
            txtSatisfyDegree = (TextView) itemView.findViewById(R.id.txt_satisfy_degree);
            imgRealName = (ImageView) itemView.findViewById(R.id.img_real_name);
            imgQualification = (ImageView) itemView.findViewById(R.id.img_qualification);
            imgCard = (ImageView) itemView.findViewById(R.id.img_card);
            rlayoutOperate = (RelativeLayout) itemView.findViewById(R.id.rlayout_operate);
            txtOperate = (TextView) itemView.findViewById(R.id.txt_operate);
            rlayoutChat = (RelativeLayout) itemView.findViewById(R.id.rlayout_chat);
            llayoutDetail = (RelativeLayout) itemView.findViewById(R.id.rlayout_item_detail);

            llayoutDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnTouchDetailListener.toPersonalDetail(position);
                }
            });

            rlayoutChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ParamUtils.isEmpty(SettingUtils.get(mContext, "token", ""))) {
                        EventUtil.sendEvent(new AnonLoginEvent());
                        return;
                    }
                    ChatActivity.launch(mContext, 1, mData.get(position).id, mData.get(position).name, mData.get(position).photo);
                }
            });
        }

    }

}
