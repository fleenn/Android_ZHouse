package com.zfb.house.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lemon.util.DateUtils;
import com.lemon.util.GlideUtil;
import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.component.HomeGalleryImagesLayout;
import com.zfb.house.component.RatingBar;
import com.zfb.house.model.bean.MomentsContent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 我的 -> 收藏的房友圈adapter
 */
public class CollectMomentsAdapter extends RecyclerView.Adapter {

    private Context mContext;//上下文
    private List<MomentsContent> mDate;//数据
    private OnRecycleViewListener mOnRecycleViewListener;

    public CollectMomentsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<MomentsContent> datas) {
        mDate = datas;
    }

    public void addDatas(List<MomentsContent> datas) {
        mDate.addAll(datas);
    }

    public void setmOnRecycleViewListener(OnRecycleViewListener mOnRecycleViewListener) {
        this.mOnRecycleViewListener = mOnRecycleViewListener;
    }

    public interface OnRecycleViewListener {
        void onItemClick(int position, String eliteId);

        void onItemDelete(int position, String eliteId);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_collect_moments, parent, false);//加载布局文件
        return new CollectMomentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final MomentsContent momentsContent = mDate.get(position);
        if (null == momentsContent) {
            return;
        }
        CollectMomentsViewHolder holder = (CollectMomentsViewHolder) viewHolder;
        //头像
        GlideUtil.getInstance().loadUrl(mContext, momentsContent.getUserPhoto(), holder.imgAvatar);
        //用户名
        holder.tvName.setText(momentsContent.getUserName());
        //发布的内容
        holder.tvContent.setText(momentsContent.getContent());
        //身份标识
        setIdentity(holder, momentsContent.getType());
        //发布时间
        setDate(holder, momentsContent.getEliteTime());
        //设置星级
        if (!ParamUtils.isEmpty(momentsContent.getType())) {
            if (momentsContent.getType().equals("0")) {//用户没有星级
                holder.rbStar.setVisibility(View.GONE);
                holder.tvUserType.setVisibility(View.VISIBLE);
            } else {//经纪人才有星级
                holder.rbStar.setVisibility(View.VISIBLE);
                holder.tvUserType.setVisibility(View.GONE);
                holder.rbStar.setStar(momentsContent.getUserStar());
            }
        }
        //显示图片
        setImagesLayout(holder, momentsContent);
        //发布位置
        setLocation(holder, momentsContent.getLocation());

        //“查看全部评论”的监听事件
        holder.tvAllComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnRecycleViewListener) {
                    mOnRecycleViewListener.onItemClick(position, momentsContent.getId());
                }
            }
        });

        //“删除”的监听事件
        holder.llayoutDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnRecycleViewListener) {
                    mOnRecycleViewListener.onItemDelete(position, momentsContent.getId());
                }
            }
        });

        //“item”的监听事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 设置身份标示
     *
     * @param holder
     * @param type
     */
    private void setIdentity(CollectMomentsViewHolder holder, String type) {
        if (!ParamUtils.isEmpty(type)) {
            if (type.equals("0")) {//用户
                holder.imgIdentity.setImageResource(R.drawable.identity_user);
            } else if (type.equals("1")) {//经纪人
                holder.imgIdentity.setImageResource(R.drawable.identity_broker);
            }
        }
    }

    /**
     * 设置时间
     *
     * @param holder
     * @param dateStr
     */
    private void setDate(CollectMomentsViewHolder holder, String dateStr) {
        String dateTrans = "";
        try {
            String now = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date());
            if (DateUtils.compare(dateStr, now) < 0) {
                String month = dateStr.substring(5, 7);
                String day = dateStr.substring(8, 10);
                dateTrans = month + "/" + day;
            } else {
                dateTrans = dateStr.substring(11, 16);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.tvTime.setText(dateTrans);
    }

    /**
     * 初始化图片 列表
     *
     * @param holder
     * @param bean
     */
    private void setImagesLayout(CollectMomentsViewHolder holder, MomentsContent bean) {
        HomeGalleryImagesLayout imagesLayout = holder.homeGalleryImagesLayout;
        List<Uri> uriList = new ArrayList<>();
        String photoArray = bean.getPhoto();
        if (!ParamUtils.isEmpty(photoArray)) {
            try {
                String[] photos = photoArray.split(",");
                if (photos.length > 0) {
                    for (String photo : photos) {
                        uriList.add(Uri.parse(photo));
                    }
                }
            } catch (Exception e) {
                return;
            }
        }
        imagesLayout.setImageList(uriList);
    }

    /**
     * 设置定位信息
     *
     * @param holder
     * @param location
     */
    private void setLocation(CollectMomentsViewHolder holder, String location) {
        if (!ParamUtils.isEmpty(location)) {
            if (location.length() >= 15) {
                String transLocation = location.substring(0, 15) + " …";
                holder.tvMomentsLocation.setText(transLocation);
            } else {
                holder.tvMomentsLocation.setText(location);
            }
            holder.llayoutLocation.setVisibility(View.VISIBLE);
        } else {
            holder.llayoutLocation.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return ParamUtils.isEmpty(mDate) ? 0 : mDate.size();
    }

    class CollectMomentsViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;//头像
        TextView tvName;//用户名
        ImageView imgIdentity;//身份标识
        RatingBar rbStar;//星级
        TextView tvUserType;//用户没有星级，显示“客户”
        TextView tvContent;//发布的内容
        HomeGalleryImagesLayout homeGalleryImagesLayout;//照片区域
        LinearLayout llayoutLocation;//位置区域
        TextView tvMomentsLocation;//位置
        TextView tvTime;//发布时间
        TextView tvAllComment;//查看全部评论
        LinearLayout llayoutDelete;//删除

        public int position;

        public CollectMomentsViewHolder(View itemView) {
            super(itemView);
            imgAvatar = (ImageView) itemView.findViewById(R.id.img_collect_avatar);
            tvName = (TextView) itemView.findViewById(R.id.txt_collect_name);
            imgIdentity = (ImageView) itemView.findViewById(R.id.img_collect_identity);
            rbStar = (RatingBar) itemView.findViewById(R.id.rating_bar_collect);
            tvUserType = (TextView) itemView.findViewById(R.id.tv_user_type);
            tvContent = (TextView) itemView.findViewById(R.id.txt_collect_content);
            homeGalleryImagesLayout = (HomeGalleryImagesLayout) itemView.findViewById(R.id.collectMomentsGalleryImagesLayout);
            llayoutLocation = (LinearLayout) itemView.findViewById(R.id.llayout_collect_location);
            tvMomentsLocation = (TextView) itemView.findViewById(R.id.txt_collect_location);
            tvTime = (TextView) itemView.findViewById(R.id.txt_collect_time);
            tvAllComment = (TextView) itemView.findViewById(R.id.txt_all_comment);
            llayoutDelete = (LinearLayout) itemView.findViewById(R.id.llayout_collect_delete);
        }
    }


}
