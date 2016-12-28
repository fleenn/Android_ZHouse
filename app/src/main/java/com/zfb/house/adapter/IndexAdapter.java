package com.zfb.house.adapter;

import android.content.Context;
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

import com.lemon.util.DateUtils;
import com.lemon.util.GlideUtil;
import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.component.HomeGalleryImagesLayout;
import com.zfb.house.component.MTextView;
import com.zfb.house.component.RatingBar;
import com.zfb.house.emchat.SmileUtils;
import com.zfb.house.model.bean.MomentsContent;
import com.zfb.house.model.bean.ReplyContent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Snekey on 2016/5/17.
 * 首页adapter
 */
public class IndexAdapter extends RecyclerView.Adapter {

    private List<MomentsContent> mDate;
    private Context mContext;
    private OnRecyclerViewListener onRecyclerViewListener;

    public IndexAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<MomentsContent> datas) {
        mDate = datas;
    }

    public List<MomentsContent> getDate() {
        return mDate;
    }

    public void addDatas(List<MomentsContent> datas) {
        mDate.addAll(datas);
    }

    public interface OnRecyclerViewListener {
        void onHeadClick(int position);

    }

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_home_moments_layout, null);
        return new MomentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int p) {
        final MomentsViewHolder momentsViewHolder = (MomentsViewHolder) holder;
        int position = p - 1;
        momentsViewHolder.position = position;
        final MomentsContent momentsContent = mDate.get(position);
        GlideUtil.getInstance().loadUrl(mContext, momentsContent.getUserPhoto(), momentsViewHolder.imgAvatar);
        momentsViewHolder.tvName.setText(momentsContent.getUserName());
        setStar(momentsViewHolder, momentsContent);
        setContent(momentsViewHolder, momentsContent.getContent());
        setIdentity(momentsViewHolder, momentsContent.getType());
        setDate(momentsViewHolder, momentsContent.getEliteTime());
        setImagesLayout(momentsViewHolder, momentsContent);
        setLocation(momentsViewHolder, momentsContent.getLocation());
        setCommentContentLayout(momentsViewHolder, momentsContent, position);
    }

    @Override
    public int getItemCount() {
        return ParamUtils.isEmpty(mDate) ? 0 : mDate.size();
    }

    /**
     * 设置用户星级
     * @param holder
     * @param momentsContent
     */
    private void setStar(MomentsViewHolder holder,MomentsContent momentsContent){
        if (momentsContent.getType().equals("1")){
            holder.ratingBar.setVisibility(View.VISIBLE);
            holder.tvUserType.setVisibility(View.GONE);
            holder.ratingBar.setStar(momentsContent.getUserStar());
        }else {
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
        List<Uri> uriList = new ArrayList();
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
                    mTextView.setMText(SmileUtils.getSmiledText(holder.itemView.getContext(), showStr));
                    layout.addView(tvLayout);
                }
            }
        }
    }

    class MomentsViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView tvName;
        RatingBar ratingBar;//星级
        TextView tvUserType;
        ImageView imgIdentity;
        TextView tvTime;
        TextView tvContent;
        HomeGalleryImagesLayout homeGalleryImagesLayout;
        LinearLayout llayoutLocation;
        LinearLayout llayoutComment;
        TextView tvMomentsLocation;
        RelativeLayout rlayoutChat;
        public int position;


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
            llayoutComment = (LinearLayout) itemView.findViewById(R.id.llayout_comment);
            tvMomentsLocation = (TextView) itemView.findViewById(R.id.tv_moments_location);
            rlayoutChat = (RelativeLayout) itemView.findViewById(R.id.rlayout_chat);

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
    }
}
