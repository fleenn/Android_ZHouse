package com.zfb.house.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.model.bean.Goods;

import java.util.List;

/**
 * 特权兑换适配器
 * Created by HourGlassRemember on 2016/8/5.
 */
public class PrivilegeCashingAdapter extends RecyclerView.Adapter {

    private Context mContext;//上下文
    private List<Goods> mData;//数据

    public PrivilegeCashingAdapter(Context mContext) {
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
            Goods item = mData.get(position);
            //商品名称
            //商品所需金币数量
            //商品照片
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

        public EntityCashingViewHolder(View itemView) {
            super(itemView);

            txtGoodsName = (TextView) itemView.findViewById(R.id.txt_goods_name);
            txtGoodsCoin = (TextView) itemView.findViewById(R.id.txt_goods_coin);
            imgPicture = (ImageView) itemView.findViewById(R.id.img_goods_picture);
        }
    }

}
