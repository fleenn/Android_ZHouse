package com.zfb.house.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
 * 金币商城 -> 商品列表适配器
 * Created by Administrator on 2016/8/5.
 */
public class GoodsGridAdapter extends BaseAdapter {

    private Context mContext;//上下文
    private LayoutInflater inflater;
    private List<Goods> mData;//数据
    private OnItemClickListener onItemClickListener;

    public GoodsGridAdapter(Context mContext) {
        this.mContext = mContext;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public interface OnItemClickListener {
        void toGoodsDetail(int position);
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public List<Goods> getData() {
        return mData;
    }

    public void setData(List<Goods> mData) {
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return ParamUtils.isEmpty(mData) ? 0 : mData.size();
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
        ViewHolder holder;
        final Goods item = mData.get(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_grid_goods, parent, false);
            holder = new ViewHolder();
            holder.txtGoodsName = (TextView) convertView.findViewById(R.id.txt_goods_name);
            holder.txtGoodsCoin = (TextView) convertView.findViewById(R.id.txt_goods_coin);
            holder.imgPicture = (ImageView) convertView.findViewById(R.id.img_goods_picture);
            holder.rlayoutItem = (RelativeLayout) convertView.findViewById(R.id.rlayout_goods_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (!ParamUtils.isNull(item)) {
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
                    if (!ParamUtils.isNull(onItemClickListener)) {
                        onItemClickListener.toGoodsDetail(position);
                    }
                }
            });
        }
        return convertView;
    }

    class ViewHolder {
        TextView txtGoodsName;//商品名称
        TextView txtGoodsCoin;//商品所需金币数量
        ImageView imgPicture;//商品照片
        RelativeLayout rlayoutItem;//item
    }

}
