package com.zfb.house.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lemon.event.AnonLoginEvent;
import com.lemon.util.DateUtils;
import com.lemon.util.EventUtil;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.component.HomeGalleryImagesLayout;
import com.zfb.house.component.MTextView;
import com.zfb.house.component.RatingBar;
import com.zfb.house.emchat.SmileUtils;
import com.zfb.house.model.bean.MomentsContent;
import com.zfb.house.model.bean.ReplyContent;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.ui.MomentsDetailActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Snekey on 2016/5/17.
 * 房友圈adpter
 */
public class MomentsAdapter extends RecyclerView.Adapter {

    private List<MomentsContent> mDate;
    private Context mContext;
    private OnFunctionClickListener mOnClickListeners;
    private OnRecyclerViewListener onRecyclerViewListener;
    private boolean isContainHead;
    private LayoutInflater mLayoutInflater;

    public boolean isContainHead() {
        return isContainHead;
    }

    public void setIsContainHead(boolean isContainHead) {
        this.isContainHead = isContainHead;
    }

    public List<MomentsContent> getmDate() {
        return mDate;
    }

    public MomentsAdapter(Context context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<MomentsContent> datas) {
        mDate = datas;
    }

    public void addDatas(List<MomentsContent> datas) {
        mDate.addAll(datas);
    }

    public interface OnRecyclerViewListener {

        void onHeadClick(int position);

        void toPraise(int position);

        void toCollect(int position, View v);

        boolean onItemLongClick(int position);
    }

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    public interface OnFunctionClickListener {

        void OnCommentClick(View v, MomentsContent momentsContent, ReplyContent replyContent, int position);

        void OnCommentDelete(ReplyContent replyContent, int position);

    }

    public void setOnFunctionClickListener(OnFunctionClickListener onClickListener) {
        this.mOnClickListeners = onClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_list_moments_layout, null);
        return new MomentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final int p;
        if (isContainHead) {
            p = position - 1;
        } else {
            p = position;
        }
        final MomentsViewHolder momentsViewHolder = (MomentsViewHolder) holder;
        momentsViewHolder.position = position;
        final MomentsContent momentsContent = mDate.get(p);
        Glide.with(mContext).load(momentsContent.getUserPhoto()).placeholder(R.drawable.default_avatar).into(momentsViewHolder.imgAvatar);
        setName(momentsViewHolder, momentsContent);
        setStar(momentsViewHolder, momentsContent);
        setContent(momentsViewHolder, momentsContent.getContent());
        setIdentity(momentsViewHolder, momentsContent.getType());
        setDate(momentsViewHolder, momentsContent.getEliteTime());
        setImagesLayout(momentsViewHolder, momentsContent);
        setLocation(momentsViewHolder, momentsContent.getLocation());
        setMoreReply(momentsViewHolder, momentsContent.isMore());
        setCommentContentLayout(momentsViewHolder, momentsContent, position);
        setCount(momentsViewHolder, momentsContent);
        momentsViewHolder.imgCollect.setSelected(momentsContent.isCollect());
        momentsViewHolder.imgPraise.setSelected(momentsContent.isPraise());
        momentsViewHolder.rlayoutPraise.setClickable(!momentsContent.isPraise());
        momentsViewHolder.rlayoutChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListeners != null) {
                    mOnClickListeners.OnCommentClick(v, momentsContent, null, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ParamUtils.isEmpty(mDate) ? 0 : mDate.size();
    }

    private void setName(MomentsViewHolder holder, MomentsContent momentsContent) {
        holder.tvName.setText(ParamUtils.isEmpty(momentsContent.getRemark()) ? momentsContent.getUserName() : momentsContent.getRemark());
    }

    /**
     * 设置用户星级
     *
     * @param holder
     * @param momentsContent
     */
    private void setStar(MomentsViewHolder holder, MomentsContent momentsContent) {
        if (momentsContent.getType().equals("1")) {
            holder.ratingBar.setVisibility(View.VISIBLE);
            holder.tvUserType.setVisibility(View.GONE);
            holder.ratingBar.setStar(momentsContent.getUserStar());
        } else {
            holder.ratingBar.setVisibility(View.GONE);
            holder.tvUserType.setVisibility(View.VISIBLE);
        }
    }

    private void setContent(MomentsViewHolder holder, String content) {
        if (!ParamUtils.isEmpty(content)) {
            holder.tvContent.setText(content);
            holder.tvContent.setVisibility(View.VISIBLE);
        } else {
            holder.tvContent.setVisibility(View.GONE);
        }
    }

    /**
     * 设置时间
     *
     * @param holder
     * @param dateStr
     */
    private void setDate(MomentsViewHolder holder, String dateStr) {
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
     * 设置身份标示
     *
     * @param holder
     * @param type
     */
    private void setIdentity(MomentsViewHolder holder, String type) {
        if (!ParamUtils.isEmpty(type)) {
            if (type.equals("0")) {
                holder.imgIdentity.setImageResource(R.drawable.identity_user);
            } else {
                holder.imgIdentity.setImageResource(R.drawable.identity_broker);
            }
        }
    }

    /**
     * 初始化图片 列表
     *
     * @param holder
     * @param bean
     */
    private void setImagesLayout(MomentsViewHolder holder, MomentsContent bean) {
        HomeGalleryImagesLayout imagesLayout = holder.homeGalleryImagesLayout;
        List<Uri> uriList = new ArrayList<Uri>();
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
    private void setLocation(MomentsViewHolder holder, String location) {
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

    private void setCount(MomentsViewHolder holder, MomentsContent momentsContent) {
        holder.tvPraiseCount.setText(momentsContent.getPraiseCount() + "");
        holder.tvShareCount.setText(momentsContent.getShareCount() + "");
        holder.tvCollectionCount.setText(momentsContent.getCollectionCount() + "");
        holder.tvReplyCount.setText(momentsContent.getReplyCount() + "");
    }

    private void setMoreReply(MomentsViewHolder holder, boolean hasMore) {
        if (hasMore) {
            holder.tvMoreReply.setVisibility(View.VISIBLE);
        } else {
            holder.tvMoreReply.setVisibility(View.GONE);
        }
    }

    /**
     * 评论监听
     *
     * @param holder
     * @param bean
     */
    private void setCommentContentLayout(final MomentsViewHolder holder, final MomentsContent bean, final int position) {
        LinearLayout layout = holder.llayoutComment;
        layout.removeAllViews();
        layout.setVisibility(View.GONE);
        if (bean != null && bean.getHouseEliteReply() != null && !ParamUtils.isEmpty(bean.getHouseEliteReply().getList())) {
            List<ReplyContent> replyContents = bean.getHouseEliteReply().getList();
            for (final ReplyContent replyContent : replyContents) {
                if (replyContent != null) {
                    layout.setVisibility(View.VISIBLE);
                    final View tvLayout = LayoutInflater.from(layout.getContext()).inflate(
                            R.layout.item_comment_detail, null);
                    String commentName = "<font color='#ef8200'>" + replyContent.getUserName() + ":" + "</font>";
                    if (!TextUtils.isEmpty(replyContent.getReplyUserName())) {
                        commentName = "<font color='#ef8200'>" + replyContent.getUserName() + "</font>"
                                + "<font color='#4d4d4d'>回复</font>"
                                + "<font color='#ef8200'>" + replyContent.getReplyUserName() + ":</font>";
                    }
                    String commentContent = replyContent.getReplyContent();
                    String showStr = commentName + commentContent;
                    MTextView mTextView = (MTextView) tvLayout.findViewById(R.id.home_comment_detail_comment_item_commentTV);
                    mTextView.setMText(SmileUtils.getSmiledText(mContext, showStr));
                    mTextView.setOnLinkSpanClickListener(new MyOnLinkSpanClickListener(replyContent));
                    if (!TextUtils.isEmpty(replyContent.getUserId()) && !ParamUtils.isNull(UserBean.getInstance(mContext)) && replyContent.getUserId().equals(UserBean.getInstance(mContext).id.trim())) {
                        //用户自己发的内容才可以删除
                        tvLayout.setTag(replyContent);
                        tvLayout.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(final View v) {
                                Object object = v.getTag();
                                if (object != null && object instanceof ReplyContent) {
                                    ReplyContent deletedContent = (ReplyContent) object;
                                    mOnClickListeners.OnCommentDelete(deletedContent, position);
                                }
                                return false;
                            }
                        });
                    } else if (!TextUtils.isEmpty(replyContent.getUserId())) {
                        tvLayout.setTag(replyContent);
                        tvLayout.setOnLongClickListener(null);
                        tvLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Object object = v.getTag();
                                if (object != null && object instanceof ReplyContent) {
                                    final ReplyContent tempbean = (ReplyContent) object;
                                    if (mOnClickListeners != null) {
                                        mOnClickListeners.OnCommentClick(tvLayout, bean, tempbean, holder.position);
                                    }
                                }
                            }
                        });
                    } else {
                        tvLayout.setTag(null);
                        tvLayout.setOnLongClickListener(null);
                    }

                    layout.addView(tvLayout);
                }
            }
        }
    }

    private class MyOnLinkSpanClickListener implements MTextView.OnLinkSpanClickListener {
        ReplyContent replyContent;//评论的对象内容

        MyOnLinkSpanClickListener(ReplyContent replyContent) {
            this.replyContent = replyContent;
        }

        @Override
        public void onClick(MTextView view, String clickContent) {
            if (replyContent != null && !TextUtils.isEmpty(clickContent)) {
                // TODO: 2016/5/19 点击评论昵称跳转
//                if (!TextUtils.isEmpty(replyContent.getReplyUserName())
//                        && clickContent.contains(replyContent.getReplyUserName())) {
//                    PersonalInfoActivity.launch(view.getContext(), replyContent.replyUserId,
//                            replyContent.replyUserId == UserBean.getInstance(view.getContext()).id);
//                } else if (!TextUtils.isEmpty(replyContent.getUserName()) && clickContent.contains(replyContent.getUserName())) {
//
//                    PersonalInfoActivity.launch(view.getContext(), replyContent.getUserId(),
//                            replyContent.getUserId() == UserBean.getInstance(view.getContext()).id);
//                }
            }
        }
    }

    class MomentsViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        ImageView imgAvatar;//头像
        TextView tvName;//昵称
        RatingBar ratingBar;//星级
        TextView tvUserType;//用户没有星级，显示“客户”
        ImageView imgIdentity;//身份标识
        TextView tvTime;//发表时间
        TextView tvContent;//发表内容
        HomeGalleryImagesLayout homeGalleryImagesLayout;//发表的照片
        LinearLayout llayoutLocation;
        TextView tvMoreReply;
        LinearLayout llayoutComment;
        TextView tvMomentsLocation;
        TextView tvPraiseCount;//赞数量
        TextView tvShareCount;//分享数量
        TextView tvCollectionCount;//收藏数量
        TextView tvReplyCount;//评论数量
        RelativeLayout rlayoutChat;//评论
        RelativeLayout rlayoutPraise;//评论
        RelativeLayout rlayoutCollect;//评论
        ImageView imgPraise;//点赞
        ImageView imgCollect;//评论
        public int position;//位置


        public MomentsViewHolder(View itemView) {
            super(itemView);

            imgAvatar = (ImageView) itemView.findViewById(R.id.img_avatar);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rating_bar);
            tvUserType = (TextView) itemView.findViewById(R.id.tv_user_type);
            imgIdentity = (ImageView) itemView.findViewById(R.id.img_identity);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            homeGalleryImagesLayout = (HomeGalleryImagesLayout) itemView.findViewById(R.id.homeGalleryImagesLayout);
            llayoutLocation = (LinearLayout) itemView.findViewById(R.id.llayout_location);
            tvMoreReply = (TextView) itemView.findViewById(R.id.tv_more_reply);
            llayoutComment = (LinearLayout) itemView.findViewById(R.id.llayout_comment);
            tvMomentsLocation = (TextView) itemView.findViewById(R.id.tv_moments_location);
            tvPraiseCount = (TextView) itemView.findViewById(R.id.tv_praise_count);
            tvShareCount = (TextView) itemView.findViewById(R.id.tv_share_count);
            tvCollectionCount = (TextView) itemView.findViewById(R.id.tv_collection_count);
            tvReplyCount = (TextView) itemView.findViewById(R.id.tv_reply_count);
            rlayoutChat = (RelativeLayout) itemView.findViewById(R.id.rlayout_chat);
            rlayoutPraise = (RelativeLayout) itemView.findViewById(R.id.rlayout_praise);
            rlayoutCollect = (RelativeLayout) itemView.findViewById(R.id.rlayout_collect);
            imgPraise = (ImageView) itemView.findViewById(R.id.img_praise);
            imgCollect = (ImageView) itemView.findViewById(R.id.img_collect);

//            更多评论
            tvMoreReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ParamUtils.isEmpty(SettingUtils.get(mContext, "token", ""))) {
                        EventUtil.sendEvent(new AnonLoginEvent());
                    } else {
                        MomentsContent momentsContent = mDate.get(isContainHead ? position - 1 : position);
                        if (momentsContent.isMore()) {
                            String id = momentsContent.getId();
                            Intent intent = new Intent(mContext, MomentsDetailActivity.class);
                            intent.putExtra("id", id);
                            mContext.startActivity(intent);
                        }
                    }
                }
            });

//            收藏
            rlayoutCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecyclerViewListener.toCollect(position, v);
//                    当用户为游客身份的时候不需要在点击后禁用点击事件，因为解除禁用是在接口返回成功之后，如果游客点击则不会去调用接口，会造成点击一次后无法再点击的情况
                    if (!ParamUtils.isEmpty(SettingUtils.get(mContext, "token", ""))) {
                        v.setClickable(false);
                    }
                }
            });

//            点赞
            rlayoutPraise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecyclerViewListener.toPraise(position);
                    if (!ParamUtils.isEmpty(SettingUtils.get(mContext, "token", ""))) {
                        v.setClickable(false);
                    }
                }
            });

            //头像点击事件
            imgAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onRecyclerViewListener) {
                        onRecyclerViewListener.onHeadClick(position);
                    }
                }
            });
        }

        @Override
        public boolean onLongClick(View v) {
            if (null != onRecyclerViewListener) {
                return onRecyclerViewListener.onItemLongClick(position);
            }
            return false;
        }
    }
}
