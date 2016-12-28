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
import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.model.bean.Goods;
import com.zfb.house.ui.GoodsDetailActivity;

import java.util.List;

/**
 * 实物兑换适配器
 * Created by HourGlassRemember on 2016/8/5.
 */
public class EntityCashingAdapter extends RecyclerView.Adapter {

    private Context mContext;//上下文
    private List<Goods> mData;//数据

    public EntityCashingAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public List<Goods> getData() {
        return mData;
    }

    public void setData(List<Goods> mData) {
        this.mData = mData;
    }

    public void addData(List<Goods> mData) {
        this.mData.addAll(mData);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_grid_goods, parent, false);
        return new EntityCashingViewHolder(view);
    }

    /**
     * 设置值
     *
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        EntityCashingViewHolder holder = (EntityCashingViewHolder) viewHolder;
        if (!ParamUtils.isEmpty(mData)) {
            final Goods item = mData.get(position);
            //商品名称
            holder.txtGoodsName.setText(item.getItemName());
            //商品所需金币数量
            holder.txtGoodsCoin.setText(item.getItemPoint());
            //商品照片
            Glide.with(mContext).load(item.getItemPic()).placeholder(R.drawable.default_moments).into(holder.imgPicture);
            //item
            holder.rlayoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, GoodsDetailActivity.class);
                    intent.putExtra("itemId", item.getId());
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return ParamUtils.isEmpty(mData) ? 0 : mData.size();
    }

    class EntityCashingViewHolder extends RecyclerView.ViewHolder {
        TextView txtGoodsName;//商品名称
        TextView txtGoodsCoin;//商品所需金币数量
        ImageView imgPicture;//商品照片
        RelativeLayout rlayoutItem;//item

        public EntityCashingViewHolder(View itemView) {
            super(itemView);

            txtGoodsName = (TextView) itemView.findViewById(R.id.txt_goods_name);
            txtGoodsCoin = (TextView) itemView.findViewById(R.id.txt_goods_coin);
            imgPicture = (ImageView) itemView.findViewById(R.id.img_goods_picture);
            rlayoutItem = (RelativeLayout) itemView.findViewById(R.id.rlayout_goods_item);
        }
    }

}
