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

import com.lemon.util.DateUtils;
import com.lemon.util.GlideUtil;
import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.component.RatingBar;
import com.zfb.house.model.bean.RelevantContent;
import com.zfb.house.ui.MomentsDetailActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Snekey on 2016/7/7.
 */
public class RelevantAdapter extends RecyclerView.Adapter {
    private List<RelevantContent> mData;
    private Context mContext;
    private LayoutInflater mInflater;
    private OnClickUserDetail mOnClickUserDetail;

    public OnClickUserDetail getOnClickUserDetail() {
        return mOnClickUserDetail;
    }

    public void setOnClickUserDetail(OnClickUserDetail mOnClickUserDetail) {
        this.mOnClickUserDetail = mOnClickUserDetail;
    }

    public interface OnClickUserDetail {
        void toGetDetail(int position);
    }

    public List<RelevantContent> getData() {
        return mData;
    }

    public void setData(List<RelevantContent> mData) {
        this.mData = mData;
    }

    public void addData(List<RelevantContent> data) {
        mData.addAll(data);
    }

    public RelevantAdapter(Context mContext) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_list_relevant_moments, null);
        return new RelevantHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RelevantHolder relevantHolder = (RelevantHolder) holder;
        RelevantContent relevantContent = mData.get(position);
        relevantHolder.position = position;
        setDate(relevantHolder, relevantContent.getReplyTime());
        GlideUtil.getInstance().loadUrl(mContext, relevantContent.getReplyUserPhoto(), relevantHolder.imgAvatar);
        setName(relevantHolder, relevantContent);
        setStar(relevantHolder, relevantContent);
        setUserType(relevantHolder, relevantContent.getReplyUserType());
        setContent(relevantHolder, relevantContent.getReplyContent());
        setRepliedMoments(relevantHolder, relevantContent);
    }

    @Override
    public int getItemCount() {
        return ParamUtils.isEmpty(mData) ? 0 : mData.size();
    }

    /**
     * 设置时间
     *
     * @param holder
     * @param dateStr
     */
    private void setDate(RelevantHolder holder, String dateStr) {
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
     * 设置姓名
     *
     * @param holder
     * @param content
     */
    private void setName(RelevantHolder holder, RelevantContent content) {
        if (ParamUtils.isEmpty(content.getReplyUserAlise())) {
            holder.tvName.setText(content.getReplyUserName());
        } else {
            holder.tvName.setText(content.getReplyUserAlise());
        }
    }

    /**
     * 设置经纪人星级或用户标识
     *
     * @param holder
     * @param content
     */
    private void setStar(RelevantHolder holder, RelevantContent content) {
        if (content.getReplyUserType().equals("0")) {
            holder.ratingBar.setVisibility(View.GONE);
            holder.tvCustomer.setVisibility(View.VISIBLE);
        } else {
            holder.ratingBar.setVisibility(View.VISIBLE);
            holder.tvCustomer.setVisibility(View.GONE);
            holder.ratingBar.setStar(content.getReplyUserStar());
        }
    }

    /**
     * 回复的内容
     *
     * @param holder
     * @param content
     */
    private void setContent(RelevantHolder holder, String content) {
        if (content.equals("zan")) {
            holder.tvReplyContent.setText(R.string.label_praised);
            holder.tvReplyContent.setSelected(true);
        } else {
            holder.tvReplyContent.setText("回复了你：" + content);
            holder.tvReplyContent.setSelected(false);
        }
    }

    /**
     * 设置用户类型图标
     *
     * @param holder
     * @param userType
     */
    private void setUserType(RelevantHolder holder, String userType) {
        if (userType.equals("0")) {
            holder.imgIdentity.setSelected(false);
        } else {
            holder.imgIdentity.setSelected(true);
        }
    }

    /**
     * 被回复的房友圈
     *
     * @param holder
     * @param content
     */
    private void setRepliedMoments(RelevantHolder holder, RelevantContent content) {
        if (ParamUtils.isEmpty(content.getElitePhoto())) {
            holder.imgRepliedContent.setVisibility(View.GONE);
            holder.tvRepliedContent.setVisibility(View.VISIBLE);
            String eliteContent = content.getEliteContent();
            if (eliteContent.length() >= 20) {
                eliteContent = eliteContent.substring(0, 19) + "…";
            }
            holder.tvRepliedContent.setText(eliteContent);
        } else {
            holder.imgRepliedContent.setVisibility(View.VISIBLE);
            holder.tvRepliedContent.setVisibility(View.GONE);
            GlideUtil.getInstance().loadMomentsUrl(mContext, content.getElitePhoto(), holder.imgRepliedContent);
        }
    }

    private class RelevantHolder extends RecyclerView.ViewHolder {
        private ImageView imgAvatar;
        private TextView tvTime;
        private TextView tvName;
        private ImageView imgIdentity;
        private RatingBar ratingBar;
        private TextView tvCustomer;
        private TextView tvReplyContent;
        private TextView tvRepliedContent;
        private ImageView imgRepliedContent;
        private RelativeLayout rlayoutMoments;
        private int position;

        public RelevantHolder(View itemView) {
            super(itemView);

            imgAvatar = (ImageView) itemView.findViewById(R.id.img_avatar);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            imgIdentity = (ImageView) itemView.findViewById(R.id.img_identity);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rating_bar);
            tvCustomer = (TextView) itemView.findViewById(R.id.tv_customer);
            tvReplyContent = (TextView) itemView.findViewById(R.id.tv_reply_content);
            tvRepliedContent = (TextView) itemView.findViewById(R.id.tv_replied_content);
            imgRepliedContent = (ImageView) itemView.findViewById(R.id.img_replied_content);
            rlayoutMoments = (RelativeLayout) itemView.findViewById(R.id.rlayout_moments);

            imgAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickUserDetail.toGetDetail(position);
                }
            });

            rlayoutMoments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = mData.get(position).getEliteId();
                    Intent intent = new Intent(mContext, MomentsDetailActivity.class);
                    intent.putExtra("id", id);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
