package com.zfb.house.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.lemon.LemonLocation;
import com.lemon.util.GlideUtil;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.lemon.util.palyer.PlayerEngineImpl;
import com.lemon.util.palyer.PlayerEngineListener;
import com.zfb.house.R;
import com.zfb.house.emchat.ChatActivity;
import com.zfb.house.model.bean.GrabCustomerItem;
import com.zfb.house.util.ToolUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Snekey on 2016/6/16.
 */
public class BrokerCallEntrustAdapter extends RecyclerView.Adapter{
    private static final int ANIM_START = 1;
    private static final int ANIM_STOP = 2;

    private List<GrabCustomerItem> mDatas;
    private Context mContext;
    private LatLng mMineLL;
    private LatLng mCustomerLL;
    private List<String> mSelectHouseIds;
    private boolean isEdit = false;
    public List<String> getmSelectHouseIds() {
        return mSelectHouseIds;
    }
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

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    private boolean isSelectAll;
    public void setIsSelectAll(boolean isSelectAll) {
        this.isSelectAll = isSelectAll;
    }

    public  BrokerCallEntrustAdapter(Context context,List<GrabCustomerItem> datas){
        mContext = context;
        mDatas = datas;
        mSelectHouseIds = new ArrayList<>();

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_entrust_call_broker, parent,false);
        return new GrabCustomerHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final GrabCustomerHolder grabCustomerHolder = (GrabCustomerHolder) holder;
        final GrabCustomerItem grabCustomerItem = mDatas.get(position);
        //头像
        GlideUtil.getInstance().loadUrl(mContext, grabCustomerItem.getCustomerPohto(), grabCustomerHolder.imgGrabAvatar);
        grabCustomerHolder.tvCustomerName.setText(grabCustomerItem.getCustomerName());
        grabCustomerHolder.tvNeedType.setText(ToolUtil.getNeedType(grabCustomerItem.getRequireType()));
        grabCustomerHolder.tvDays.setText(ToolUtil.getBrokerage(grabCustomerItem.getBrokerage()));
        if (grabCustomerItem.getTextMsgs() != null)
            grabCustomerHolder.tvContent.setText(changeString(grabCustomerItem.getTextMsgs().toString()));

        if (isEdit){
            grabCustomerHolder.cbEditSelect.setVisibility(View.VISIBLE);
            grabCustomerHolder.ivMessage.setVisibility(View.GONE);
        }else{
            grabCustomerHolder.cbEditSelect.setVisibility(View.GONE);
            grabCustomerHolder.ivMessage.setVisibility(View.VISIBLE);
        }

        if (isSelectAll){
            grabCustomerHolder.cbEditSelect.setChecked(true);
        }else{
            grabCustomerHolder.cbEditSelect.setChecked(false);
        }

        grabCustomerHolder.ivMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatActivity.launch(mContext, 1, grabCustomerItem.getCustomerId(), grabCustomerItem.getCustomerName(), grabCustomerItem.getCustomerPohto());
            }
        });

        grabCustomerHolder.cbEditSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mSelectHouseIds.add(mDatas.get(position).getOrderId());
                }else {
                    mSelectHouseIds.remove(mDatas.get(position).getOrderId());
                }
            }
        });

        mCustomerLL = new LatLng(grabCustomerItem.getLat(), grabCustomerItem.getLng());
        String distance = LemonLocation.getDistance(mMineLL, mCustomerLL) + "";
        grabCustomerHolder.tvDistance.setText("距" + distance.substring(0, 4) + "km");
        setVoice(grabCustomerHolder.imgCustomerVoice,grabCustomerItem);
    }

    private String changeString(String value){
        if (TextUtils.isEmpty(value)) return "";

        value = value.replace("[","");
        value = value.replace("]","");
        return value;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
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

    private class GrabCustomerHolder extends RecyclerView.ViewHolder{

        private ImageView imgGrabAvatar;
        private TextView tvCustomerName;
        private TextView tvNeedType;
        private ImageView imgCustomerVoice,ivMessage;
        private CheckBox cbEditSelect;
        private TextView tvDays;
        private TextView tvDistance;
        private TextView tvContent;


        public GrabCustomerHolder(View itemView) {
            super(itemView);

            double lat = SettingUtils.get(mContext, "lat", 24.478314);
            double lng = SettingUtils.get(mContext, "lng", 118.111461);
            mMineLL = new LatLng(lat, lng);
            imgGrabAvatar = (ImageView) itemView.findViewById(R.id.img_grab_avatar);
            tvCustomerName = (TextView) itemView.findViewById(R.id.tv_customer_name);
            tvNeedType = (TextView) itemView.findViewById(R.id.tv_need_type);
            imgCustomerVoice = (ImageView) itemView.findViewById(R.id.img_customer_voice);
            tvDays = (TextView) itemView.findViewById(R.id.tv_days);
            tvDistance = (TextView) itemView.findViewById(R.id.tv_distance);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            ivMessage = (ImageView) itemView.findViewById(R.id.iv_call_message);
            cbEditSelect = (CheckBox) itemView.findViewById(R.id.cb_edit_select);
        }
    }
}
