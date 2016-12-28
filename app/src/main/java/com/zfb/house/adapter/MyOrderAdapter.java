package com.zfb.house.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.model.bean.Order;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * 积分商城 -> 我的订单adapter
 */
public class MyOrderAdapter extends RecyclerView.Adapter {
    private static final int NORMAL_ITEM = 0;
    private static final int GROUP_ITEM = 1;

    private List<Order> mDate;
    private Context mContext;

    public MyOrderAdapter(Context context) {
        this.mContext = context;
    }

    /**
     * 获得资源ID
     *
     * @param viewType
     * @return
     */
    private int getLayoutResId(int viewType) {
        switch (viewType) {
            case NORMAL_ITEM:
                return R.layout.item_list_my_order;
            case GROUP_ITEM:
                return R.layout.item_list_my_order_title;
        }
        throw new IllegalArgumentException("不支持类型为" + viewType + "的布局类型");
    }

    public Class getViewHolderClass(int viewType) {
        switch (viewType) {
            case NORMAL_ITEM:
                return NormalItemHolder.class;
            case GROUP_ITEM:
                return GroupItemHolder.class;
        }
        throw new IllegalArgumentException("不支持类型为" + viewType + "的元素类型");
    }

    public void setData(List<Order> datas) {
        mDate = datas;
    }

    public List<Order> getDate() {
        return mDate;
    }

    public void addData(List<Order> datas) {
        mDate.addAll(datas);
    }

    /**
     * 2、根据第一步的标志调用对应的ViewHolder
     * 渲染具体的ViewHolder
     *
     * @param parent   ViewHolder的容器
     * @param viewType 一个标志，我们根据该标志可以实现渲染不同类型的ViewHolder
     * @return
     */
    @Override
    public NormalItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = generateView(parent, viewType);
        return generateViewHolder(view, viewType);
    }

    private NormalItemHolder generateViewHolder(View view, int viewType) {
        Class<RecyclerView.ViewHolder> viewHolderClass = getViewHolderClass(viewType);
        if (null == viewHolderClass) {
            return null;
        }
        Constructor<?>[] constructors = viewHolderClass.getDeclaredConstructors();
        if (constructors == null) {
            throw new IllegalArgumentException("Impossible to found a constructor for " + viewHolderClass.getSimpleName());
        }
        for (Constructor<?> constructor : constructors) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if (parameterTypes == null) {
                continue;
            }
            try {
                if (isAssignableFrom(parameterTypes, View.class)) {
                    Object viewHolder = constructor.newInstance(view);
                    return (NormalItemHolder) viewHolder;
                }

                if (isAssignableFrom(parameterTypes, MyOrderAdapter.class, View.class)) {
                    Object viewHolder = constructor.newInstance(this, view);
                    return (NormalItemHolder) viewHolder;
                }
            } catch (Exception e) {
                throw new RuntimeException("Impossible to instantiate " + viewHolderClass.getSimpleName(), e);
            }
        }
        throw new IllegalArgumentException("Impossible to found a constructor with a view for " + viewHolderClass.getSimpleName());
    }

    static boolean isAssignableFrom(Class<?>[] parameterTypes, Class<?>... classes) {
        if (parameterTypes == null || classes == null || parameterTypes.length != classes.length) {
            return false;
        }
        for (int i = 0; i < parameterTypes.length; i++) {
            if (!classes[i].isAssignableFrom(parameterTypes[i])) {
                return false;
            }
        }
        return true;
    }

    private View generateView(ViewGroup parent, int viewType) {
        int layoutResId = getLayoutResId(viewType);
        if (layoutResId == 0) {
            return null;
        }
        if (null == parent) {
            return null;
        }
        if (null == mContext) {
            return null;
        }
        return LayoutInflater.from(mContext).inflate(layoutResId, parent, false);
    }

    /**
     * 1、生成一个标志
     * 决定元素的布局使用哪种类型
     *
     * @param position 数据源LmData的下标
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        //第一个要显示时间
        if (position == 0)
            return GROUP_ITEM;

        Order order = mDate.get(position);
        String currentDate = order.getCreateTimeStr().substring(5);
        int prevIndex = position - 1;
        boolean isGroup = mDate.get(prevIndex).getCreateTimeStr().substring(5).equals(currentDate);
        return isGroup ? NORMAL_ITEM : GROUP_ITEM;
    }

    /**
     * 绑定ViewHolder的数据
     *
     * @param viewHolder ViewHolder
     * @param position   数据源mData的下标
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final Order order = mDate.get(position);
        if (null == order) {
            return;
        }
        switch (getItemViewType(position)) {
            case GROUP_ITEM:
                GroupItemHolder holderGroup = (GroupItemHolder) viewHolder;
                //日期
                holderGroup.txtDate.setText(order.getCreateTimeStr().substring(5));
            case NORMAL_ITEM:
                final NormalItemHolder holder = (NormalItemHolder) viewHolder;
                //商品照片
                Glide.with(mContext).load(order.getItemPic().split(",")[0]).into(holder.goodsPicture);
                //商品名称
                holder.txtGoodsName.setText(order.getItemName());
                //商品所需金币数量
                holder.txtGoodsCoin.setText(order.getItemPoint());
                //收货人
                holder.txtOrderReceipt.setText(order.getReceiver());
                //收货地址
                holder.txtOrderAddress.setText(order.getAddress());
                //联系电话
                holder.txtOrderPhone.setText(order.getMobile());
                //备注
                if (ParamUtils.isEmpty(order.getRemarks())) {
                    holder.rlOdrerRemarks.setVisibility(View.GONE);
                } else {
                    holder.rlOdrerRemarks.setVisibility(View.VISIBLE);
                    holder.txtOrderRemarks.setText(order.getRemarks());
                }
                //设置物流状态
                switch (order.getLogisticalStatus()) {
                    case 0://准备发货
                        holder.imgReady.setSelected(true);
                        holder.imgDistribution.setSelected(false);
                        holder.imgReceipt.setSelected(false);
                        break;
                    case 1://配送中
                        holder.imgReady.setSelected(false);
                        holder.imgDistribution.setSelected(true);
                        holder.imgReceipt.setSelected(false);
                        break;
                    case 2://已签收
                        holder.imgReady.setSelected(false);
                        holder.imgDistribution.setSelected(false);
                        holder.imgReceipt.setSelected(true);
                        break;
                }
                break;
        }
    }

    /**
     * RecycleView的Count设置为数据总数加1（footerView）
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return ParamUtils.isEmpty(mDate) ? 0 : mDate.size();
    }

    //带日期标题item的ViewHolder
    class GroupItemHolder extends NormalItemHolder {
        TextView txtDate;//日期

        public GroupItemHolder(View itemView) {
            super(itemView);
            txtDate = (TextView) itemView.findViewById(R.id.my_order_date);
        }
    }

    //普通item的ViewHolder
    class NormalItemHolder extends RecyclerView.ViewHolder {
        ImageView goodsPicture;//商品照片
        TextView txtGoodsName;//商品名称
        TextView txtGoodsCoin;//商品所需金币数量
        TextView txtGoodsNumber;//商品数量
        TextView txtOrderReceipt;//收货人
        TextView txtOrderAddress;//收货地址
        TextView txtOrderPhone;//联系电话
        TextView txtOrderRemarks;//备注
        RelativeLayout rlOdrerRemarks;
        ImageView imgReady;//准备发货图标
        ImageView imgDistribution;//配送中图标
        ImageView imgReceipt;//已签收图标

        public int position;

        public NormalItemHolder(View itemView) {
            super(itemView);
            goodsPicture = (ImageView) itemView.findViewById(R.id.img_order_picture);
            txtGoodsName = (TextView) itemView.findViewById(R.id.txt_goods_name);
            txtGoodsCoin = (TextView) itemView.findViewById(R.id.txt_goods_coin);
            txtGoodsNumber = (TextView) itemView.findViewById(R.id.txt_goods_number);
            txtOrderReceipt = (TextView) itemView.findViewById(R.id.txt_order_receiver);
            txtOrderAddress = (TextView) itemView.findViewById(R.id.txt_order_address);
            txtOrderPhone = (TextView) itemView.findViewById(R.id.txt_order_phone);
            txtOrderRemarks = (TextView) itemView.findViewById(R.id.txt_order_remarks);
            rlOdrerRemarks = (RelativeLayout) itemView.findViewById(R.id.rlayout_order_remarks);
            imgReady = (ImageView) itemView.findViewById(R.id.img_ready);
            imgDistribution = (ImageView) itemView.findViewById(R.id.img_distribution);
            imgReceipt = (ImageView) itemView.findViewById(R.id.img_receipt);
        }
    }

}
