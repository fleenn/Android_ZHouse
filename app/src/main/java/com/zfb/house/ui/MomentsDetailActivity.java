package com.zfb.house.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lemon.LemonActivity;
import com.lemon.LemonContext;
import com.lemon.LemonMessage;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.event.RefreshMomentsEvent;
import com.lemon.model.StatusCode;
import com.lemon.net.ApiManager;
import com.lemon.util.DateUtils;
import com.lemon.util.EventUtil;
import com.lemon.util.GlideUtil;
import com.lemon.util.ParamUtils;
import com.lemon.util.ScreenUtil;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.component.HomeGalleryImagesLayout;
import com.zfb.house.component.MTextView;
import com.zfb.house.component.ObservableScrollView;
import com.zfb.house.component.RatingBar;
import com.zfb.house.emchat.ChatInputLayout;
import com.zfb.house.emchat.SmileUtils;
import com.zfb.house.emchat.temp.OnKeyWordsClick;
import com.zfb.house.model.bean.MomentsContent;
import com.zfb.house.model.bean.NewReplyContent;
import com.zfb.house.model.bean.ReplyContent;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.model.param.CollectParam;
import com.zfb.house.model.param.DeleteReplyParam;
import com.zfb.house.model.param.MomentsDetailParam;
import com.zfb.house.model.param.MomentsReplyParam;
import com.zfb.house.model.param.PraiseParam;
import com.zfb.house.model.result.CollectResult;
import com.zfb.house.model.result.DeleteCollectResult;
import com.zfb.house.model.result.DeleteReplyResult;
import com.zfb.house.model.result.MomentsDetailResult;
import com.zfb.house.model.result.MomentsReplyResult;
import com.zfb.house.model.result.PraiseResult;
import com.zfb.house.util.ToolUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 房友圈详情页面
 * Created by Snekey on 2016/7/5.
 */
@Layout(id = R.layout.activity_moments_detail)
public class MomentsDetailActivity extends LemonActivity {

    private static final String TAG = "MomentsDetailActivity";
    @FieldView(id = R.id.scroll_moments)
    private ObservableScrollView scrollMoments;
    @FieldView(id = R.id.img_avatar)
    private ImageView imgAvatar;
    @FieldView(id = R.id.tv_name)
    private TextView tvName;
    @FieldView(id = R.id.rating_bar)
    private RatingBar rbStar;
    @FieldView(id = R.id.tv_user_type)
    private TextView tvUserType;
    @FieldView(id = R.id.img_identity)
    private ImageView imgIdentity;
    @FieldView(id = R.id.tv_date)
    private TextView tvDate;
    @FieldView(id = R.id.tv_content)
    private TextView tvContent;
    @FieldView(id = R.id.homeGalleryImagesLayout)
    private HomeGalleryImagesLayout homeGalleryImagesLayout;
    @FieldView(id = R.id.llayout_location)
    private LinearLayout llayoutLocation;
    @FieldView(id = R.id.tv_moments_location)
    private TextView tvMomentsLocation;
    @FieldView(id = R.id.llayout_comment)
    private LinearLayout llayoutComment;
    @FieldView(id = R.id.img_praise)
    private ImageView imgPraise;
    @FieldView(id = R.id.tv_praise_count)
    private TextView tvPraiseCount;
    @FieldView(id = R.id.img_collect)
    private ImageView imgCollect;
    @FieldView(id = R.id.tv_collection_count)
    private TextView tvCollectionCount;
    @FieldView(id = R.id.img_chat)
    private ImageView imgChat;
    @FieldView(id = R.id.tv_reply_count)
    private TextView tvReplyCount;
    @FieldView(id = R.id.chat_moments)
    private ChatInputLayout chatMoments;
    @FieldView(id = R.id.llayout_bottom_bar)
    private LinearLayout llayoutBottomBar;

    private MomentsContent data;
    private String token;
    private boolean isPraise;
    private int praiseCount;
    private int collectCount;
    private String id;
    private boolean isChange;

    @Override
    protected void initView() {
        setCenterText("房友圈详情");
        imgChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnCommentClick(v, data, null);
            }
        });
    }

    @Override
    protected void initData() {
        id = getIntent().getStringExtra("id");
        MomentsDetailParam momentsDetailParam = new MomentsDetailParam();
        momentsDetailParam.setEliteId(id);
        momentsDetailParam.setToken(SettingUtils.get(mContext, "token", ""));
        LemonContext.getBean(ApiManager.class).getHouseElite(momentsDetailParam);
        token = SettingUtils.get(mContext, "token", "");
    }

    @OnClick(id = R.id.img_praise)
    public void toPraise() {
        PraiseParam praiseParam = new PraiseParam();
        praiseParam.setToken(token);
        praiseParam.setEliteId(data.getId());
        praiseParam.setTag(TAG);
        apiManager.saveHouseElitePrise(praiseParam);
        imgPraise.setClickable(false);
        isChange = true;//（点赞、收藏等状态）是否有改动
    }

    @OnClick(id = R.id.img_avatar)
    public void toGetUserDetail() {
        String type = data.getType();//当前点击的item的用户类型
        String id = data.getUserId();//当前点击的item的id
        toDetail(type, id, ParamUtils.isEmpty(data.getRemark()) ? data.getUserName() : data.getRemark());
    }

    @OnClick(id = R.id.img_collect)
    public void toCollect() {
        CollectParam collectParam = new CollectParam();
        collectParam.setEliteId(data.getId());
        collectParam.setToken(token);
        collectParam.setTag(TAG);
        if (imgCollect.isSelected()) {
            apiManager.delHouseEliteCollection(collectParam);
        } else {
            apiManager.saveHouseEliteCollection(collectParam);
        }
        isChange = true;
        imgCollect.setClickable(false);
    }

    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        hideKeyboard();
        if (isChange) {
            RefreshMomentsEvent refreshMomentsEvent = new RefreshMomentsEvent();
            refreshMomentsEvent.setId(id);
            refreshMomentsEvent.setPraise(isPraise);
            refreshMomentsEvent.setPraiseCount(praiseCount);
            refreshMomentsEvent.setCollectCount(collectCount);
            EventUtil.sendEvent(refreshMomentsEvent);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        toBack();
    }

    /**
     * 名字或者备注
     *
     * @param momentsContent
     */
    private void setName(MomentsContent momentsContent) {
        if (ParamUtils.isEmpty(momentsContent.getRemark())) {
            tvName.setText(momentsContent.getUserName());
        } else {
            tvName.setText(momentsContent.getRemark());
        }
    }

    /**
     * 设置星级
     *
     * @param momentsContent
     */
    private void setBrokerStar(MomentsContent momentsContent) {
        if (momentsContent.getType().equals("1")) {
            tvUserType.setVisibility(View.GONE);
            rbStar.setVisibility(View.VISIBLE);
            rbStar.setStar(momentsContent.getUserStar());
        } else {
            tvUserType.setVisibility(View.VISIBLE);
            rbStar.setVisibility(View.GONE);
        }
    }

    /**
     * 用户类型标识
     *
     * @param type
     */
    private void setIdentity(String type) {
        if (!ParamUtils.isEmpty(type)) {
            if (type.equals("0")) {
                imgIdentity.setImageResource(R.drawable.identity_user);
            } else {
                imgIdentity.setImageResource(R.drawable.identity_broker);
            }
        }
    }

    /**
     * 设置时间
     *
     * @param dateStr
     */
    private void setDate(String dateStr) {
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
        tvDate.setText(dateTrans);
    }

    /**
     * 初始化图片 列表
     *
     * @param bean
     */
    private void setImagesLayout(MomentsContent bean) {
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
        homeGalleryImagesLayout.setImageList(uriList);
    }

    /**
     * 设置定位信息
     *
     * @param location
     */
    private void setLocation(String location) {
        if (!ParamUtils.isEmpty(location)) {
            tvMomentsLocation.setText(location);
            llayoutLocation.setVisibility(View.VISIBLE);
        } else {
            llayoutLocation.setVisibility(View.GONE);
        }
    }

    /**
     * 点赞、评论、收藏数量及图标颜色
     *
     * @param momentsContent
     */
    private void setCount(MomentsContent momentsContent) {
        tvPraiseCount.setText(momentsContent.getPraiseCount() + "");
        imgPraise.setSelected(momentsContent.isPraise());
        imgPraise.setClickable(!momentsContent.isPraise());
        tvCollectionCount.setText(momentsContent.getCollectionCount() + "");
        imgCollect.setSelected(momentsContent.isCollect());
        tvReplyCount.setText(momentsContent.getReplyCount() + "");
    }

    /**
     * 初始化评论列表，并且设置监听事件
     *
     * @param bean
     */
    private void setCommentContentLayout(final MomentsContent bean) {
        llayoutComment.removeAllViews();
        llayoutComment.setVisibility(View.GONE);
        if (bean != null && bean.getHouseEliteReply() != null && !ParamUtils.isEmpty(bean.getHouseEliteReply().getList())) {
            List<ReplyContent> replyContents = bean.getHouseEliteReply().getList();
            for (final ReplyContent replyContent : replyContents) {
                if (replyContent != null) {
                    llayoutComment.setVisibility(View.VISIBLE);
                    final View tvLayout = LayoutInflater.from(llayoutComment.getContext()).inflate(
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
                    if (!TextUtils.isEmpty(replyContent.getUserId()) && replyContent.getUserId().equals(UserBean.getInstance(tvLayout.getContext()).id.trim())) {
                        //用户自己发的内容才可以删除
                        tvLayout.setTag(replyContent);
                        tvLayout.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(final View v) {
                                Object object = v.getTag();
                                if (object != null && object instanceof ReplyContent) {
                                    ReplyContent deletedContent = (ReplyContent) object;
                                    OnCommentDelete(deletedContent);
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
                                    OnCommentClick(tvLayout, bean, tempbean);
                                }
                            }
                        });
                    } else {
                        tvLayout.setTag(null);
                        tvLayout.setOnLongClickListener(null);
                    }

                    llayoutComment.addView(tvLayout);
                }
            }
        }
    }

    //    添加评论
    public void OnCommentClick(View view, MomentsContent momentsContent, ReplyContent replyContent) {
        if (view != null) {
            int[] location = new int[2];
            view.getLocationInWindow(location);
            int y = location[1] - ScreenUtil.dip2px(mContext, 250);
            if (y <= 0) {
                y = 0;
            }
            scrollMoments.scrollTo(0, y);
        }
        /** 点击列表子项，准备评论 */
        if (chatMoments != null) {
            chatMoments.setVisibility(View.VISIBLE);
            chatMoments.showKeyBoard();
            if (replyContent != null) {
                //回复人的信息    id 和 name
                chatMoments.getEditText().setHint("回复" + replyContent.getUserName());
            } else {
                chatMoments.getEditText().setHint("");
            }
            chatMoments.setOnKeyWordsClick(new MyOnKeyWordsClick(momentsContent, replyContent));
        }
    }

    /**
     * 删除评论
     *
     * @param replyContent
     */
    public void OnCommentDelete(final ReplyContent replyContent) {
        new AlertDialog
                .Builder(mContext)
                .setTitle("删除评论")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteReplyParam deleteReplyParam = new DeleteReplyParam();
                        String token = SettingUtils.get(mContext, "token", null);
                        deleteReplyParam.setReplyId(replyContent.getId());
                        deleteReplyParam.setToken(token);
                        deleteReplyParam.setReplyContent(replyContent);
                        deleteReplyParam.setTag(TAG);
                        apiManager.deleteHouseEliteReply(deleteReplyParam);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * 点击输入框发送监听
     */
    private class MyOnKeyWordsClick implements OnKeyWordsClick {
        //        房友圈bean
        MomentsContent momentsContent;
        //        评论区bean
        ReplyContent mReplyContent;


        MyOnKeyWordsClick(MomentsContent momentsContent, ReplyContent replyContent) {
            this.momentsContent = momentsContent;
            this.mReplyContent = replyContent;
        }

        @Override
        public void onItemClick(Object keyWord) {
            if (chatMoments != null) {
                chatMoments.setVisibility(View.GONE);
                chatMoments.hideKeyboard();
            }

            if (keyWord == null || TextUtils.isEmpty(keyWord.toString().trim())) {
                LemonContext.getBean(LemonMessage.class).sendMessage("不能发送空字符!");
                return;
            }
            Log.i("comment", "-- MyOnKeyWordsClick send comment keyWord:" + keyWord + " , id:" + momentsContent.getId());
            String replyUserId = null;
            if (mReplyContent != null) {
                replyUserId = mReplyContent.getUserId();
            }
            String token = SettingUtils.get(mContext, "token", null);
            MomentsReplyParam momentsReplyParam = new MomentsReplyParam();
            momentsReplyParam.setEliteId(momentsContent.getId());
            momentsReplyParam.setReplyContent(keyWord.toString());
            momentsReplyParam.setReplyUserId(replyUserId);
            momentsReplyParam.setToken(token);
            momentsReplyParam.setTag(TAG);
            apiManager.saveHouseEliteReply(momentsReplyParam);
        }
    }

//    ---------------------------------------EventBus接收方法--------------------------------------------------------

    /**
     * 房友圈详细
     *
     * @param result
     */
    public void onEventMainThread(MomentsDetailResult result) {
        data = result.getData();
        isPraise = data.isPraise();
        praiseCount = data.getPraiseCount();
        collectCount = data.getCollectionCount();
        GlideUtil.getInstance().loadUrl(mContext, data.getUserPhoto(), imgAvatar);
        setName(data);
        setBrokerStar(data);
        tvContent.setText(data.getContent());
        setIdentity(data.getType());
        setDate(data.getEliteTime());
        setImagesLayout(data);
        setLocation(data.getLocation());
        setCount(data);
        setCommentContentLayout(data);
    }

    /**
     * 添加评论
     *
     * @param result
     */
    public void onEventMainThread(MomentsReplyResult result) {
        if (((MomentsReplyParam) result.getParam()).getTag().equals(TAG)) {
            if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
                NewReplyContent newReplyContent = result.getData();
                ReplyContent reply = newReplyContent.getReply();
                int count = newReplyContent.getCount();
                tvReplyCount.setText(String.valueOf(count));
                data.getHouseEliteReply().getList().add(reply);
                setCommentContentLayout(data);
                ToolUtil.updatePoint(mContext, result.getData().getTotalPoint(), result.getData().getGetPoint());
            } else {
                imgPraise.setClickable(true);
            }
        }
    }

    /**
     * 删除评论
     *
     * @param result
     */
    public void onEventMainThread(DeleteReplyResult result) {
        if (((DeleteReplyParam) result.getParam()).getTag().equals(TAG)) {
            if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
                DeleteReplyParam param = (DeleteReplyParam) result.getParam();
                data.getHouseEliteReply().getList().remove(param.getReplyContent());
                int count = data.getReplyCount() - 1;
                tvReplyCount.setText(String.valueOf(count));
                setCommentContentLayout(data);
            } else {
                lemonMessage.sendMessage(R.string.toast_delete_reply_fail);
            }
        }
    }

    /**
     * 点赞
     *
     * @param result
     */
    public void onEventMainThread(PraiseResult result) {
        if (((PraiseParam) result.getParam()).getTag().equals(TAG)) {
            if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
                int count = data.getPraiseCount() + 1;
                isPraise = true;
                praiseCount = count;
                imgPraise.setClickable(false);
                imgPraise.setSelected(true);
                tvPraiseCount.setText(String.valueOf(count));
            } else {
                lemonMessage.sendMessage(R.string.toast_praise_fail);
            }
        }
    }

    /**
     * 收藏
     *
     * @param result
     */
    public void onEventMainThread(CollectResult result) {
        if (((CollectParam) result.getParam()).getTag().equals(TAG)) {
            if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
                int count = data.getCollectionCount() + 1;
                collectCount = count;
                data.setCollectionCount(count);
                imgCollect.setClickable(true);
                imgCollect.setSelected(true);
                tvCollectionCount.setText(String.valueOf(count));
            } else {
                lemonMessage.sendMessage(R.string.toast_collect_fail);
            }
        }
    }

    /**
     * 取消收藏
     *
     * @param result
     */
    public void onEventMainThread(DeleteCollectResult result) {
        if (((CollectParam) result.getParam()).getTag().equals(TAG)) {
            if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
                int count = data.getCollectionCount() - 1;
                collectCount = count;
                data.setCollectionCount(count);
                imgCollect.setClickable(true);
                imgCollect.setSelected(false);
                tvCollectionCount.setText(String.valueOf(count));
            } else {
                lemonMessage.sendMessage(R.string.toast_delete_collect_fail);
            }
        }
    }
}
