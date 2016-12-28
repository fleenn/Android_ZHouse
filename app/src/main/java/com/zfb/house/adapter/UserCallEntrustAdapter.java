package com.zfb.house.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.lemon.util.TimeUtil;
import com.lemon.util.palyer.PlayerEngineImpl;
import com.lemon.util.palyer.PlayerEngineListener;
import com.zfb.house.R;
import com.zfb.house.model.bean.UserCallEntrutsInfo;
import com.zfb.house.util.ToolUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Snekey on 2016/5/17.
 *
 */
public class UserCallEntrustAdapter extends RecyclerView.Adapter {
    private static final int ANIM_START = 1;
    private static final int ANIM_STOP = 2;

    private List<UserCallEntrutsInfo.ListBean> mDatas;
    private Context mContext;
    private List<String> mSelectHouseIds;
    private boolean isEdit = false;
    private OnDeleteClick onDeleteClick;
    private EntrustBrokerAdapter adapter;
    private OnTouchDetailListener mOnTouchDetailListener;
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

    public interface OnTouchDetailListener {
        void toPersonalDetail(int position);
    }

    public OnTouchDetailListener getOnTouchDetailListener() {
        return mOnTouchDetailListener;
    }

    public void setOnTouchDetailListener(OnTouchDetailListener mOnTouchDetailListener) {
        this.mOnTouchDetailListener = mOnTouchDetailListener;
    }

    public List<UserCallEntrutsInfo.ListBean> getDatas() {
        return mDatas;
    }

    public void setDatas(List<UserCallEntrutsInfo.ListBean> mDatas) {
        this.mDatas = mDatas;
    }

    public List<String> getmSelectHouseIds() {
        return mSelectHouseIds;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public void setOnDeleteClick(OnDeleteClick onDeleteClick) {
        this.onDeleteClick = onDeleteClick;
    }

    public UserCallEntrustAdapter(Context context, List<UserCallEntrutsInfo.ListBean> datas) {
        this.mContext = context;
        mDatas = datas;
        mSelectHouseIds = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_entrust_call_user, parent, false);
        return new EntrustViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final EntrustViewHolder viewHolder = (EntrustViewHolder) holder;
        final UserCallEntrutsInfo.ListBean info = mDatas.get(position);

        viewHolder.tvMoney.setText(ToolUtil.getBrokerage(info.getBrokerage()));
        viewHolder.tvTime.setText(TimeUtil.change2DateInfo4cut(info.getMessageTime()));
        viewHolder.tvContent.setText(info.getMessageContent());
        viewHolder.tvWay.setText(ToolUtil.setExpertType(info.getRequireType()));
        viewHolder.rlayoutDelete.setVisibility(isEdit ? View.VISIBLE : View.GONE);

        if (info.getBrokersList() != null && info.getBrokersList().size() > 0) {
            viewHolder.gvContacts.setVisibility(View.VISIBLE);
            if (adapter == null) {
                adapter = new EntrustBrokerAdapter(mContext);
                adapter.setDatas(info.getBrokersList());
                viewHolder.gvContacts.setAdapter(adapter);
            } else {
                adapter.setDatas(info.getBrokersList());
                viewHolder.gvContacts.setAdapter(adapter);
            }
        } else {
            viewHolder.gvContacts.setVisibility(View.GONE);
        }
        viewHolder.rlayoutDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteClick.onResult(info.getMessageId());
            }
        });
        setVoice(viewHolder.ivVoice,info);
    }

    @Override
    public int getItemCount() {
        return ParamUtils.isEmpty(mDatas) ? 0 : mDatas.size();
    }

    /**
     * 设置音频
     *
     * @param imgVoice
     * @param info
     */
    private void setVoice(final ImageView imgVoice, final UserCallEntrutsInfo.ListBean info) {
        imgVoice.setTag(info.getMessageId());
        if (!ParamUtils.isEmpty(info.getYyContent())) {
            imgVoice.setImageResource(R.drawable.voice_yes);
            final AnimationDrawable[] ad = {new AnimationDrawable()};
            imgVoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PlayerEngineImpl.getInstance(mContext).play(info.getYyContent().hashCode(),info.getYyContent());
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
                        if (info.getMessageId().equals(imgVoice.getTag())){
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

    class EntrustViewHolder extends RecyclerView.ViewHolder {
        TextView tvWay, tvMoney, tvTime, tvContent;
        ImageView ivVoice;
        GridView gvContacts;
        RelativeLayout rlayoutDelete;

        public EntrustViewHolder(View itemView) {
            super(itemView);
            double lat = SettingUtils.get(mContext, "lat", 24.478314);
            double lng = SettingUtils.get(mContext, "lng", 118.111461);
            tvWay = (TextView) itemView.findViewById(R.id.tv_way);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvMoney = (TextView) itemView.findViewById(R.id.tv_money);
            ivVoice = (ImageView) itemView.findViewById(R.id.iv_voice);
            gvContacts = (GridView) itemView.findViewById(R.id.gv_contacts);
            rlayoutDelete = (RelativeLayout) itemView.findViewById(R.id.rlayout_delete);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }

    public interface OnDeleteClick {
        void onResult(String id);
    }

}
