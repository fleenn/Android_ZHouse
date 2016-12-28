package com.zfb.house.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lemon.LemonCacheManager;
import com.lemon.LemonContext;
import com.lemon.util.GlideUtil;
import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.component.LoadMoreRecyclerView;
import com.zfb.house.component.SettingOperationView;
import com.zfb.house.model.bean.SellItem;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.ui.BrokerShopSellDetailActivity;
import com.zfb.house.util.ToolUtil;

import java.util.List;
import java.util.UUID;

/**
 * Created by Snekey on 2016/6/27.
 */
public class SellAdapter extends RecyclerView.Adapter {

    private LayoutInflater mInflater;
    private UserBean userBean;
    private UserBean selfUserBean;
    private SettingOperationView.OnOperationClickListener onOperationClickListener;
    LoadMoreRecyclerView loadMoreRecyclerView;
    private Context mContext;
    private List<SellItem> mData;
    private OnOperateListener mOnOperateListener;
    private OnTouchDetailListener mOnTouchDetailListener;


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

    public List<SellItem> getmData() {
        return mData;
    }

    public void setmData(List<SellItem> mData) {
        this.mData = mData;
    }

    public SellAdapter(Context mContext, UserBean userBean, List<SellItem> models, SettingOperationView.OnOperationClickListener onOperationClickListener, LoadMoreRecyclerView loadMoreRecyclerView) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        this.userBean = userBean;
        this.mData = models;
        this.onOperationClickListener = onOperationClickListener;
        selfUserBean = UserBean.getInstance(mContext);
        this.loadMoreRecyclerView = loadMoreRecyclerView;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_list_broker_shop, null);
        return new BrokersHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        BrokersHolder brokersHolder = (BrokersHolder) holder;
        final SellItem model = mData.get(position);
        brokersHolder.rl_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSettingView();
                Intent intent = new Intent(mContext, BrokerShopSellDetailActivity.class);
                String uuid = UUID.randomUUID().toString();//房源ID
                String useruuid = UUID.randomUUID().toString();//经纪人ID
                LemonContext.getBean(LemonCacheManager.class).putBean(uuid, model);
                LemonContext.getBean(LemonCacheManager.class).putBean(useruuid, userBean);
                intent.putExtra("uuid", uuid);
                intent.putExtra("useruuid", useruuid);
                mContext.startActivity(intent);
            }
        });

        brokersHolder.sov_operation.setListPosition(position);
        brokersHolder.sov_operation.setData(model);
        if (onOperationClickListener != null)
            brokersHolder.sov_operation.setOnOperationClickListener(onOperationClickListener);

        if (!ParamUtils.isNull(selfUserBean) && !ParamUtils.isNull(selfUserBean.id)
                && !ParamUtils.isNull(userBean) && !ParamUtils.isNull(userBean.id)
                && selfUserBean.id.equals(userBean.id)) {
            brokersHolder.sov_operation.setVisibility(View.VISIBLE);
            if (!ParamUtils.isEmpty(model.getUpDown()) && model.getUpDown().equals("1")) {
                brokersHolder.iv_shop_up.setVisibility(View.VISIBLE);
            } else {
                brokersHolder.iv_shop_up.setVisibility(View.GONE);
            }
        } else {
            brokersHolder.sov_operation.setVisibility(View.GONE);
            brokersHolder.iv_shop_up.setVisibility(View.GONE);
        }

        //房子照片
        if (!ParamUtils.isEmpty(model.getPhoto())) {
            String[] photos = model.getPhoto().split(",");
            Glide.with(mContext).load(photos[0]).placeholder(R.drawable.defalut_shop_list).into(brokersHolder.iv_shop_img);
        } else {
            GlideUtil.getInstance().loadUrl(mContext, "", brokersHolder.iv_shop_img);
        }
        //房源标题
        if (!ParamUtils.isEmpty(model.getTitle())) {
            brokersHolder.tv_shop_title.setText(model.getTitle());
        }
        //所在小区
        if (!ParamUtils.isEmpty(model.getCommuntityName())) {
            brokersHolder.tv_shop_village.setText(model.getCommuntityName());
        }
        //行政区
        if (!ParamUtils.isEmpty(model.getRegionName())) {
            brokersHolder.tv_shop_area.setText(model.getRegionName());
        }
        //设置出售的字段
        brokersHolder.tv_shop_type.setText(ToolUtil.setSellType(model));
        //面积
        if (!ParamUtils.isEmpty(model.getArea())) {
            brokersHolder.tv_shop_acreage.setText(model.getArea() + "㎡");
        }
        //装修
        if (!ParamUtils.isEmpty(model.getDecorationLevelName())) {
            brokersHolder.tv_shop_decorate.setText(model.getDecorationLevelName());
        }
        //朝向/级别
        if (!ParamUtils.isEmpty(model.getDirectionName())) {
            brokersHolder.tv_shop_orientation.setText(model.getDirectionName());
        } else if (!ParamUtils.isEmpty(model.getOfficeLevelName())) {
            brokersHolder.tv_shop_orientation.setText(model.getOfficeLevelName());
        }
        //售价
        if (!ParamUtils.isEmpty(model.getWishPrice())) {
            brokersHolder.tv_shop_price.setText(model.getWishPrice() + "万");
        }
    }

    public void hideSettingView() {
        SettingOperationView.setmCurrentShowBtnID("-1");
        loadMoreRecyclerView.notifiyChange();
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
        ImageView iv_shop_img;//房子照片
        TextView tv_shop_title;//房源标题
        TextView tv_shop_village;//所在小区
        TextView tv_shop_area;//行政区
        TextView tv_shop_type;
        TextView tv_shop_acreage;//面积
        TextView tv_shop_decorate;//装修
        TextView tv_shop_orientation;//朝向
        TextView tv_shop_price;//售价
        RelativeLayout rl_parent;
        SettingOperationView sov_operation;
        ImageView iv_shop_up;
        private int position;

        public BrokersHolder(View itemView) {
            super(itemView);
            tv_shop_title = (TextView) itemView.findViewById(R.id.broker_shop_title);
            tv_shop_village = (TextView) itemView.findViewById(R.id.broker_shop_district);
            tv_shop_area = (TextView) itemView.findViewById(R.id.broker_shop_village);
            tv_shop_type = (TextView) itemView.findViewById(R.id.broker_shop_style);
            tv_shop_acreage = (TextView) itemView.findViewById(R.id.broker_shop_acreage);
            tv_shop_decorate = (TextView) itemView.findViewById(R.id.broker_shop_decorate);
            tv_shop_orientation = (TextView) itemView.findViewById(R.id.broker_shop_orientation);
            tv_shop_price = (TextView) itemView.findViewById(R.id.broker_shop_price);
            iv_shop_img = (ImageView) itemView.findViewById(R.id.broker_shop_img);
            rl_parent = (RelativeLayout) itemView.findViewById(R.id.rl_parent);
            iv_shop_up = (ImageView) itemView.findViewById(R.id.iv_shop_up);
            sov_operation = (SettingOperationView) itemView.findViewById(R.id.sov_operation);
            SettingOperationView.setmCurrentShowBtnID("-1");
        }

    }

}
