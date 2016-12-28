package com.zfb.house.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.model.bean.SellItem;
import com.zfb.house.util.ToolUtil;

import java.util.List;

/**
 * 我的 -> 收藏的房源adapter
 * Created by Administrator on 2016/5/27.
 */
public class CollectSellAdapter extends RecyclerView.Adapter {

    private List<SellItem> models;//数据
    private Context context;//上下文
    private boolean isEdit = false;

    public CollectSellAdapter(Context context) {
        this.context = context;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    //定义自定义接口
    public interface OnRecycleViewListener {
        void onItemClick(View view, int position, SellItem item);//item的监听

        void onDeleteClick(View view, int position, SellItem item);//删除按钮的监听
    }

    //声明接口
    private OnRecycleViewListener onRecycleViewListener;

    public void setOnRecycleViewListener(OnRecycleViewListener onRecycleViewListener) {
        this.onRecycleViewListener = onRecycleViewListener;
    }

    public void setData(List<SellItem> mData) {
        this.models = mData;
    }

    public void addData(List<SellItem> mData) {
        this.models.addAll(mData);
    }

    public List<SellItem> getData() {
        return models;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_collect_houses, parent, false);//加载布局文件
        return new CollectHouseViewHolder(view);
    }

    /**
     * 设置值
     *
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        CollectHouseViewHolder holder = (CollectHouseViewHolder) viewHolder;
        if (!ParamUtils.isNull(models)) {
            final SellItem model = models.get(position);
            //房子照片
            if (!ParamUtils.isEmpty(model.getPhoto())) {
                String[] photos = model.getPhoto().split(",");
                Glide.with(context).load(photos[0]).placeholder(R.drawable.default_banner).into(holder.imgAvatar);
            }
            //房源标题
            if (!ParamUtils.isEmpty(model.getTitle())) {
                holder.txtTitle.setText(model.getTitle());
            }
            //所在小区
            if (!ParamUtils.isEmpty(model.getServerAreaName())) {
                holder.txtVillage.setText(model.getServerAreaName());
            }
            //行政区
            if (!ParamUtils.isEmpty(model.getRegionName())) {
                holder.txtArea.setText(model.getRegionName());
            }
            //设置出售的字段
            holder.txtType.setText(ToolUtil.setSellType(model));
            //面积
            if (!ParamUtils.isEmpty(model.getArea())) {
                holder.txtAcreage.setText(model.getArea() + "m²");
            }
            //装修
            if (!ParamUtils.isEmpty(model.getDecorationLevelName())) {
                holder.txtDecorate.setText(model.getDecorationLevelName());
            }
            //朝向/级别
            if (!ParamUtils.isEmpty(model.getDirectionName())) {
                holder.txtOrientation.setText(model.getDirectionName());
            } else if (!ParamUtils.isEmpty(model.getOfficeLevelName())) {
                holder.txtOrientation.setText(model.getOfficeLevelName());
            }
            //售价
            if (!ParamUtils.isEmpty(model.getWishPrice())) {
                holder.txtPrice.setText(model.getWishPrice() + "万");
            }

            //如果设置了回调，则设置点击事件
            holder.rlayoutDelete.setVisibility(isEdit ? View.VISIBLE : View.GONE);
            holder.rlayoutDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != onRecycleViewListener) {
                        onRecycleViewListener.onDeleteClick(view, position, model);
                    }


                }
            });

            holder.llayoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != onRecycleViewListener) {
                        onRecycleViewListener.onItemClick(view, position, model);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return ParamUtils.isEmpty(models) ? 0 : models.size();
    }

    class CollectHouseViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;//房子照片
        TextView txtTitle;//房源标题
        TextView txtVillage;//所在小区
        TextView txtArea;//行政区
        TextView txtType;
        TextView txtAcreage;//面积
        TextView txtDecorate;//装修程度
        TextView txtOrientation;//朝向
        TextView txtPrice;//价格
        RelativeLayout rlayoutDelete;//删除按钮
        LinearLayout llayoutItem;//item

        public int position;

        public CollectHouseViewHolder(View itemView) {
            super(itemView);

            imgAvatar = (ImageView) itemView.findViewById(R.id.img_house_picture);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_house_title);
            txtVillage = (TextView) itemView.findViewById(R.id.txt_house_village);
            txtArea = (TextView) itemView.findViewById(R.id.txt_house_road);
            txtType = (TextView) itemView.findViewById(R.id.txt_house_style);
            txtAcreage = (TextView) itemView.findViewById(R.id.txt_house_acreage);
            txtDecorate = (TextView) itemView.findViewById(R.id.txt_house_decorate);
            txtOrientation = (TextView) itemView.findViewById(R.id.txt_house_orientation);
            txtPrice = (TextView) itemView.findViewById(R.id.txt_house_price);
            rlayoutDelete = (RelativeLayout) itemView.findViewById(R.id.rlayout_house_delete);
            llayoutItem = (LinearLayout) itemView.findViewById(R.id.llayout_house_item);
        }

    }
}
