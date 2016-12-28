package com.zfb.house.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.emchat.Constant;
import com.baidu.mapapi.model.LatLng;
import com.lemon.LemonLocation;
import com.lemon.util.GlideUtil;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.lemon.util.palyer.PlayerEngineImpl;
import com.lemon.util.palyer.PlayerEngineListener;
import com.zfb.house.R;
import android.os.Handler;
import com.zfb.house.emchat.ChatActivity;
import com.zfb.house.model.bean.GrabCustomerItem;
import com.zfb.house.util.ToolUtil;

import java.util.List;

/**
 * Created by Snekey on 2016/6/16.
 */
public class GrabCustomerAdapter extends RecyclerView.Adapter {
    private static final int ANIM_START = 1;
    private static final int ANIM_STOP = 2;

    private List<GrabCustomerItem> mData;
    private Context mContext;
    private LatLng mMineLL;
    private LatLng mCustomerLL;
    private boolean isGrabbed;
    private OnGrabCustomerListener onGrabCustomerListener;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ANIM_START:
                    if (msg.obj instanceof AnimationDrawable){
                        ((AnimationDrawable)msg.obj).start();
                    }
                    break;
                case ANIM_STOP:
                    if (msg.obj instanceof AnimationDrawable){
                        ((AnimationDrawable)msg.obj).stop();
                    }
                    break;
            }

        }
    };

    public OnGrabCustomerListener getOnGrabCustomerListener() {
        return onGrabCustomerListener;
    }

    public void setOnGrabCustomerListener(OnGrabCustomerListener onGrabCustomerListener) {
        this.onGrabCustomerListener = onGrabCustomerListener;
    }

    public boolean isGrabbed() {
        return isGrabbed;
    }

    public void setIsGrabbed(boolean isGrabbed) {
        this.isGrabbed = isGrabbed;
    }

    public GrabCustomerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public List<GrabCustomerItem> getData() {
        return mData;
    }

    public void setData(List<GrabCustomerItem> data) {
        this.mData = data;
    }

    public interface OnGrabCustomerListener {
        void toGrabCustomer(int position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_grab_customer, null);
        return new GrabCustomerHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final GrabCustomerHolder grabCustomerHolder = (GrabCustomerHolder) holder;
        final GrabCustomerItem grabCustomerItem = mData.get(position);
        GlideUtil.getInstance().loadUrl(mContext, grabCustomerItem.getCustomerPohto(), grabCustomerHolder.imgCustomerAvatar);
        grabCustomerHolder.tvCustomerName.setText(grabCustomerItem.getCustomerName());
        setVoice(grabCustomerHolder.imgCustomerVoice, grabCustomerItem);
        setNeedType(grabCustomerHolder, grabCustomerItem);
        setBrokerage(grabCustomerHolder, grabCustomerItem.getBrokerage());
        setContent(grabCustomerHolder, grabCustomerItem.getTextMsgs());
        setDistance(grabCustomerHolder, grabCustomerItem);
        if (isGrabbed) {
            grabCustomerHolder.imgGrabStatus.setVisibility(View.GONE);
            grabCustomerHolder.imgGrabChat.setVisibility(View.VISIBLE);
            grabCustomerHolder.imgGrabChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChatActivity.launch(mContext, 2, grabCustomerItem.getCustomerId(), grabCustomerItem.getCustomerName(), grabCustomerItem.getCustomerPohto());
                }
            });

        } else {
            grabCustomerHolder.imgGrabStatus.setVisibility(View.VISIBLE);
            grabCustomerHolder.imgGrabChat.setVisibility(View.GONE);
            if (grabCustomerItem.isSuccess()) {
                grabCustomerHolder.imgGrabStatus.setSelected(true);
            } else {
                grabCustomerHolder.imgGrabStatus.setSelected(false);
            }
            grabCustomerHolder.imgGrabStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onGrabCustomerListener.toGrabCustomer(position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return ParamUtils.isEmpty(mData) ? 0 : mData.size();
    }

    /**
     * 设置音频
     *
     * @param imgVoice
     * @param grabCustomerItem
     */
    private void setVoice(final ImageView imgVoice, final GrabCustomerItem grabCustomerItem) {
        imgVoice.setTag(grabCustomerItem.getOrderId());
        if (!ParamUtils.isEmpty(grabCustomerItem.getVoiceMsgs())) {
            imgVoice.setImageResource(R.drawable.voice_yes);
            final AnimationDrawable[] ad = {new AnimationDrawable()};
            imgVoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PlayerEngineImpl.getInstance(mContext).play(grabCustomerItem.getVoiceMsgs().get(0).hashCode(), grabCustomerItem.getVoiceMsgs().get(0));
                    imgVoice.setImageResource(R.drawable.anim_order_voice);
                    ad[0] = (AnimationDrawable) imgVoice.getDrawable();
                    ad[0].start();
                }
            });
            PlayerEngineImpl.getInstance(mContext).setPlayingListening(new PlayerEngineListener() {
                @Override
                public void onLoading(int playID) {

                }

                @Override
                public void onPrepared(int playID, long duration) {
                    Message message = new Message();
                    message.obj = ad[0];
                    message.what = ANIM_START;
                    mHandler.sendMessage(message);
                }

                @Override
                public void onPlaying(int playID, long curPosition) {

                }

                @Override
                public void onBuffering(int playID, long curPosition) {

                }

                @Override
                public void onCompletion(int playID) {
                    if (ad[0].isRunning()) {
                        Message message = new Message();
                        message.obj = ad[0];
                        message.what = ANIM_STOP;
                        mHandler.sendMessage(message);
                        if (grabCustomerItem.getOrderId().equals(imgVoice.getTag())){
                            imgVoice.setImageResource(R.drawable.voice_yes);
                        }
                    }
                }

                @Override
                public void onPause(int playID) {
                    Message message = new Message();
                    message.obj = ad[0];
                    message.what = ANIM_STOP;
                    mHandler.sendMessage(message);
                }

                @Override
                public void onInterrupt(int playID) {

                }

                @Override
                public void onError(int playID, String error) {

                }
            });
        } else {
            imgVoice.setImageResource(R.drawable.voice_no);
            imgVoice.setOnClickListener(null);
        }
    }

    /**
     * 设置需求类型
     *
     * @param grabCustomerHolder
     * @param grabCustomerItem
     */
    private void setNeedType(GrabCustomerHolder grabCustomerHolder, GrabCustomerItem grabCustomerItem) {
        grabCustomerHolder.tvNeedType.setText(ToolUtil.getNeedType(grabCustomerItem.getRequireType()));
    }

    /**
     * 设置佣金
     *
     * @param grabCustomerHolder
     * @param s
     */
    private void setBrokerage(GrabCustomerHolder grabCustomerHolder, String s) {
        grabCustomerHolder.tvBrokerage.setText(ToolUtil.getBrokerage(s));
    }

    /**
     * 设置文本内容
     *
     * @param grabCustomerHolder
     * @param contentList
     */
    private void setContent(GrabCustomerHolder grabCustomerHolder, List<String> contentList) {
        String txtContent = "";
        for (String s : contentList) {
            txtContent += s;
        }
        grabCustomerHolder.tvContent.setText(txtContent);
    }

    /**
     * 设置距离
     *
     * @param grabCustomerHolder
     * @param grabCustomerItem
     */
    private void setDistance(GrabCustomerHolder grabCustomerHolder, GrabCustomerItem grabCustomerItem) {
        String subDistance;
        mCustomerLL = new LatLng(grabCustomerItem.getLat(), grabCustomerItem.getLng());
        String distance = LemonLocation.getDistance(mMineLL, mCustomerLL) + "";
        if (distance.length() >= 4) {
            subDistance = distance.substring(0, 4);
        } else {
            subDistance = distance;
        }
        grabCustomerHolder.tvDistance.setText("距" + subDistance + "km");
    }

    private class GrabCustomerHolder extends RecyclerView.ViewHolder {

        private ImageView imgCustomerAvatar;
        private TextView tvCustomerName;
        private TextView tvNeedType;
        private ImageView imgCustomerVoice;
        private TextView tvBrokerage;
        private TextView tvDistance;
        private TextView tvContent;
        private ImageView imgGrabStatus;
        private ImageView imgGrabChat;

        public GrabCustomerHolder(View itemView) {
            super(itemView);

            double lat = SettingUtils.get(mContext, "lat", Constant.DEFAULT_LAT);
            double lng = SettingUtils.get(mContext, "lng", Constant.DEFAULT_LNG);
            mMineLL = new LatLng(lat, lng);

            imgCustomerAvatar = (ImageView) itemView.findViewById(R.id.img_customer_avatar);
            tvCustomerName = (TextView) itemView.findViewById(R.id.tv_customer_name);
            tvNeedType = (TextView) itemView.findViewById(R.id.tv_need_type);
            imgCustomerVoice = (ImageView) itemView.findViewById(R.id.img_customer_voice);
            tvBrokerage = (TextView) itemView.findViewById(R.id.tv_brokerage);
            tvDistance = (TextView) itemView.findViewById(R.id.tv_distance);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            imgGrabStatus = (ImageView) itemView.findViewById(R.id.img_grab_status);
            imgGrabChat = (ImageView) itemView.findViewById(R.id.img_grab_chat);
        }
    }
}
