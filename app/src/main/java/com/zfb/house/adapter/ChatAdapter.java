package com.zfb.house.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lemon.util.GlideUtil;
import com.lemon.util.palyer.PlayerEngineImpl;
import com.zfb.house.R;
import com.zfb.house.model.bean.ChatMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Snekey on 2016/6/11.
 */
public class ChatAdapter extends BaseAdapter {

    private List<ChatMessage> msgList = new ArrayList<ChatMessage>();
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public ChatAdapter(Context mContext) {
        this.mContext = mContext;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public List<ChatMessage> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<ChatMessage> msgList) {
        this.msgList = msgList;
    }

    @Override
    public int getCount() {
        return msgList.size();
    }

    @Override
    public ChatMessage getItem(int position) {
        return msgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage msg = getItem(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_list_chat, null);
            holder = new ViewHolder();
            holder.imgPhoto = (ImageView) convertView.findViewById(R.id.img_photo);
            holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
            holder.imgStatus = (ImageView) convertView.findViewById(R.id.img_status);
            holder.rlayoutVoiceMessage = (LinearLayout) convertView.findViewById(R.id.rlayout_voice_message);
            holder.tvTextMessage = (TextView) convertView.findViewById(R.id.tv_text_message);
            holder.tvLength = (TextView) convertView.findViewById(R.id.tv_length);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final String path = msg.content;
        if (msg.status == 0) {
            holder.pb.setVisibility(View.VISIBLE);
            holder.imgStatus.setVisibility(View.GONE);
        } else if (msg.status == 1) {
            holder.pb.setVisibility(View.GONE);
            holder.imgStatus.setVisibility(View.GONE);
        } else if (msg.status == 2) {
            holder.pb.setVisibility(View.GONE);
            holder.imgStatus.setVisibility(View.VISIBLE);
        }
        GlideUtil.getInstance().loadUrl(mContext, msg.photoPath, holder.imgPhoto);
        if (getItemViewType(position) == 1) {
            //语音
            holder.tvLength.setText(msg.length + " \"");
            holder.rlayoutVoiceMessage.setVisibility(View.VISIBLE);
            holder.tvTextMessage.setVisibility(View.GONE);
            holder.rlayoutVoiceMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(path)) {
                        PlayerEngineImpl.getInstance(mContext).play(path.hashCode(), path);
                    }
                }
            });
        } else {
            //文字
            holder.tvTextMessage.setVisibility(View.VISIBLE);
            holder.rlayoutVoiceMessage.setVisibility(View.GONE);
            holder.tvTextMessage.setText(msg.content);
            convertView.setOnClickListener(null);
        }
        return convertView;
    }

    private class ViewHolder {
        LinearLayout rlayoutVoiceMessage;
        ImageView imgPhoto;
        TextView tvLength;
        TextView tvTextMessage;
        ProgressBar pb;
        ImageView imgStatus;
    }
}
