package com.zfb.house.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.component.HomeGalleryImagesLayout;
import com.zfb.house.model.bean.MomentsContent;
import com.zfb.house.model.bean.UserBean;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * 我的->我的房友圈adapter
 */
public class MyMomentsAdapter extends RecyclerView.Adapter {
    private static final int NORMAL_ITEM = 0;
    private static final int GROUP_ITEM = 1;

    private List<MomentsContent> mDate;
    private Context mContext;
    private OnRecycleViewListener mOnRecycleViewListener;

    public MyMomentsAdapter(Context context) {
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
                return R.layout.item_list_my_moments;
            case GROUP_ITEM:
                return R.layout.item_list_my_moments_title;
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

    public void setData(List<MomentsContent> datas) {
        mDate = datas;
    }

    public List<MomentsContent> getData() {
        return mDate;
    }

    public void addData(List<MomentsContent> datas) {
        mDate.addAll(datas);
    }

    public void setOnRecycleViewListener(OnRecycleViewListener mOnRecycleViewListener) {
        this.mOnRecycleViewListener = mOnRecycleViewListener;
    }

    public interface OnRecycleViewListener {
        void onItemDelete(int position, String eliteId);

        void onItemClick(int position, String eliteId);

        void onPraiseClick(int position, MomentsContent momentsContent, View view);

        void onCollectionClick(int position, MomentsContent momentsContent, View view);
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

                if (isAssignableFrom(parameterTypes, MyMomentsAdapter.class, View.class)) {
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

        MomentsContent momentsContent = mDate.get(position);
        String currentDate = momentsContent.getEliteTime().substring(0, 10);
        int prevIndex = position - 1;
        boolean isGroup = mDate.get(prevIndex).getEliteTime().substring(0, 10).equals(currentDate);
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
        final MomentsContent momentsContent = mDate.get(position);
        if (null == momentsContent) {
            return;
        }
        switch (getItemViewType(position)) {
            case GROUP_ITEM:
                GroupItemHolder holderGroup = (GroupItemHolder) viewHolder;
                //日期
                String month = momentsContent.getEliteTime().substring(5, 7);//月
                String day = momentsContent.getEliteTime().substring(8, 10);//日
                holderGroup.txtDate.setText(month + "月" + day + "日");
            case NORMAL_ITEM:
                final NormalItemHolder holder = (NormalItemHolder) viewHolder;
                //发布的内容
                holder.tvContent.setText(momentsContent.getContent());
                //发布时间
                holder.tvTime.setText((momentsContent.getEliteTime()).substring(11, 16));
                //显示图片
                setImagesLayout(holder, momentsContent);
                //发布位置
                setLocation(holder, momentsContent.getLocation());
                //设置数量
                setCount(holder, momentsContent);
                //设置图标
                setIcon(holder, momentsContent);

                //“删除”按钮监听事件，只有自己的房友圈才可以操作删除的动作
                if (momentsContent.getUserId().equals(UserBean.getInstance(mContext).id)) {
                    holder.txtDelete.setVisibility(View.VISIBLE);
                    holder.txtDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (null != mOnRecycleViewListener) {
                                mOnRecycleViewListener.onItemDelete(position, momentsContent.getId());
                            }
                        }
                    });
                }
                //“item”的监听事件
                holder.llayoutItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != mOnRecycleViewListener) {
                            mOnRecycleViewListener.onItemClick(position, momentsContent.getId());
                        }
                    }
                });
                //点击评论进如详情页面
                holder.rlayoutChat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != mOnRecycleViewListener) {
                            mOnRecycleViewListener.onItemClick(position, momentsContent.getId());
                        }
                    }
                });
                holder.rlayoutPraise.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != mOnRecycleViewListener) {
                            mOnRecycleViewListener.onPraiseClick(position, momentsContent, v);
                        }
                    }
                });
                holder.rlayoutCollection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != mOnRecycleViewListener) {
                            mOnRecycleViewListener.onCollectionClick(position, momentsContent, v);
                        }
                    }
                });
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

    /**
     * 初始化图片 列表
     *
     * @param holder
     * @param bean
     */
    private void setImagesLayout(NormalItemHolder holder, MomentsContent bean) {
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
    private void setLocation(NormalItemHolder holder, String location) {
        if (!ParamUtils.isEmpty(location)) {
            if (location.length() >= 15) {
                String transLocation = location.substring(0, 15) + " …";
                holder.tvMomentsLocation.setText(transLocation);
            } else {
                holder.tvMomentsLocation.setText(location);
            }
            holder.llayoutLocation.setVisibility(View.VISIBLE);
        }else {
            holder.llayoutLocation.setVisibility(View.GONE);
        }
    }

    /**
     * 设置数量
     *
     * @param holder
     * @param momentsContent
     */
    private void setCount(NormalItemHolder holder, MomentsContent momentsContent) {
        holder.tvPraiseCount.setText(momentsContent.getPraiseCount() + "");
        holder.tvShareCount.setText(momentsContent.getShareCount() + "");
        holder.tvCollectionCount.setText(momentsContent.getCollectionCount() + "");
        holder.tvReplyCount.setText(momentsContent.getReplyCount() + "");
    }

    /**
     * 设置图标
     *
     * @param holder
     * @param momentsContent
     */
    private void setIcon(NormalItemHolder holder, MomentsContent momentsContent) {
        holder.imgPraise.setSelected(momentsContent.isPraise());
        holder.imgCollection.setSelected(momentsContent.isCollect());
    }

    //带日期标题item的ViewHolder
    class GroupItemHolder extends NormalItemHolder {
        TextView txtDate;//日期

        public GroupItemHolder(View itemView) {
            super(itemView);
            txtDate = (TextView) itemView.findViewById(R.id.my_moments_date);
        }
    }

    //普通item的ViewHolder
    class NormalItemHolder extends RecyclerView.ViewHolder {
        TextView tvTime;//发布时间
        TextView tvContent;//发布的内容
        HomeGalleryImagesLayout homeGalleryImagesLayout;//照片区域
        LinearLayout llayoutLocation;//位置区域
        LinearLayout llayoutComment;//评论区域
        TextView tvMomentsLocation;//位置
        ImageView imgPraise;//点赞的图标
        TextView tvPraiseCount;//点赞的数量
        TextView tvShareCount;//分享的数量
        ImageView imgCollection;//收藏的图标
        TextView tvCollectionCount;//收藏的数量
        TextView tvReplyCount;//评论的数量
        TextView txtDelete;//删除
        RelativeLayout rlayoutPraise;//点赞区域
        RelativeLayout rlayoutShare;//分享区域
        RelativeLayout rlayoutCollection;//收藏区域
        RelativeLayout rlayoutChat;//评论区域
        LinearLayout llayoutItem;//item区域

        public int position;

        public NormalItemHolder(View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.txt_my_time);
            tvContent = (TextView) itemView.findViewById(R.id.txt_my_content);
            homeGalleryImagesLayout = (HomeGalleryImagesLayout) itemView.findViewById(R.id.myMomentsGalleryImagesLayout);
            llayoutLocation = (LinearLayout) itemView.findViewById(R.id.llayout_my_location);
            llayoutComment = (LinearLayout) itemView.findViewById(R.id.txt_my_comment);
            tvMomentsLocation = (TextView) itemView.findViewById(R.id.txt_my_location);
            imgPraise = (ImageView) itemView.findViewById(R.id.img_my_praise);
            tvPraiseCount = (TextView) itemView.findViewById(R.id.txt_my_praise);
            tvShareCount = (TextView) itemView.findViewById(R.id.txt_my_share);
            imgCollection = (ImageView) itemView.findViewById(R.id.img_my_collection);
            tvCollectionCount = (TextView) itemView.findViewById(R.id.txt_my_collection);
            tvReplyCount = (TextView) itemView.findViewById(R.id.txt_my_chat);
            txtDelete = (TextView) itemView.findViewById(R.id.txt_my_delete);
            rlayoutPraise = (RelativeLayout) itemView.findViewById(R.id.rlayout_my_praise);
            rlayoutShare = (RelativeLayout) itemView.findViewById(R.id.rlayout_my_share);
            rlayoutCollection = (RelativeLayout) itemView.findViewById(R.id.rlayout_my_collection);
            rlayoutChat = (RelativeLayout) itemView.findViewById(R.id.rlayout_my_chat);
            llayoutItem = (LinearLayout) itemView.findViewById(R.id.llayout_item);
        }
    }

}
