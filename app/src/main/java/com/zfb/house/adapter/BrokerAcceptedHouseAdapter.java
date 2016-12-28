package com.zfb.house.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lemon.LemonActivity;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.emchat.AlertDialog;
import com.zfb.house.emchat.ChatActivity;
import com.zfb.house.model.bean.BrokerAccepted;

import java.util.List;

/**
 * Created by Snekey on 2016/10/21.
 */
public class BrokerAcceptedHouseAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<BrokerAccepted> mData;

    interface OnRefreshListener{

    };

    public List<BrokerAccepted> getData() {
        return mData;
    }

    public void setData(List<BrokerAccepted> mData) {
        this.mData = mData;
    }

    public BrokerAcceptedHouseAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_broker_accept_house, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final BrokerAccepted brokerAccepted = mData.get(position);
        viewHolder.position = position;
        Glide.with(mContext).load(brokerAccepted.getUserPhoto()).placeholder(R.drawable.default_avatar).into(viewHolder.imgAvatar);
        viewHolder.tvNickname.setText(brokerAccepted.getLoginName());
        final String token = SettingUtils.get(mContext, "token", "");
        boolean isRead = SettingUtils.get(mContext, token+"HouseContact", false);
        if (isRead){
            viewHolder.tvTel.setText(brokerAccepted.getLoginName());
            viewHolder.tvTel.setOnClickListener(null);
        }else {
            viewHolder.tvTel.setText("点击显示");
            viewHolder.tvTel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
                    builder.setTitle("电子合作协议书");
                    builder.setMessage("甲方（客源方）： 厦门住房邦信息科技有限公司    \n" +
                            "乙方（房源方）： 住房邦平台注册经纪人\n" +
                            "     客户通过住房邦发布购房需求，平台将意向购房客户派单给平台合作的经纪机构，连接客户与经纪人，精准高效匹配对接房产信息及服务的移动互联网平台。为了双方共同客户在最短时间内需求得到满足，甲乙双方在诚信、平等的基础上就合作事项达成如下协议：\n" +
                            "     1、乙方签约成为甲方所属住房邦平台线上合作机构，同意所属该机构的从业人员入驻住房邦平台，享受平台派单服务和《海西晨报》报纸推广版面。\n" +
                            "2、甲方通过住房邦平台及《海西晨报》收集购房意向客源，派单推荐服务于该片区的合作经纪机构，提供客源给乙方，乙方接受甲方派单推荐提供线下交易服务，交易完成后支付平台推荐费。\n" +
                            "3、平台推荐费由乙方在收到客户交易佣金的3个工作日内，将购房交易佣金总额   20%   结算给甲方，甲方收到乙方推荐费的3个工作日内开具发票给乙方。\n" +
                            "     开户行：  中国建设银行股份有限公司厦门文灶支行  \n" +
                            "银行账号：  35101511001052514220    \n" +
                            "     4、乙方同意在接受甲方推荐后6个月内成交均受以上条款约束。\n" +
                            "5、若乙方为了自己的利益串通业主或客户私自成交而造成合作方受到损失，经发现，侵害方应承推荐费双倍损失，损失方可依此协议向法院提起诉讼，以索回损失。\n" +
                            "6、违约责任：双方签署本协议后发生争议，应协商解决。协商不成，可向当地法院诉讼解决。");
                    builder.setPositiveButton("同意", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SettingUtils.set(mContext,  token+"HouseContact", true);
                            BrokerAcceptedHouseAdapter.this.notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
            });
        }
        viewHolder.tvDistrict.setText(brokerAccepted.getAreaName());
        viewHolder.tvCommunity.setText(brokerAccepted.getVillageName());
        viewHolder.tvLayout.setText(brokerAccepted.getRoomType());
        viewHolder.tvArea.setText(brokerAccepted.getArea());
        viewHolder.tvMoney.setText(brokerAccepted.getCash());
        viewHolder.tvTime.setText(brokerAccepted.getApplyTime());
        viewHolder.tvDescription.setText(brokerAccepted.getDescription());
    }

    @Override
    public int getItemCount() {
        return ParamUtils.isEmpty(mData) ? 0 : mData.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rlayoutImgChat;
        private ImageView imgChat;
        private ImageView imgAvatar;
        private TextView tvNickname;
        private TextView tvTel;
        private TextView tvDistrict;
        private TextView tvCommunity;
        private TextView tvLayout;
        private TextView tvArea;
        private TextView tvMoney;
        private TextView tvTime;
        private TextView tvDescription;
        private int position;

        public ViewHolder(View itemView) {
            super(itemView);

            rlayoutImgChat = (RelativeLayout) itemView.findViewById(R.id.rlayout_img_chat);
            imgChat = (ImageView) itemView.findViewById(R.id.img_chat);
            imgAvatar = (ImageView) itemView.findViewById(R.id.img_avatar);
            tvNickname = (TextView) itemView.findViewById(R.id.tv_nickname);
            tvTel = (TextView) itemView.findViewById(R.id.tv_tel);
            tvDistrict = (TextView) itemView.findViewById(R.id.tv_district);
            tvCommunity = (TextView) itemView.findViewById(R.id.tv_community);
            tvLayout = (TextView) itemView.findViewById(R.id.tv_layout);
            tvArea = (TextView) itemView.findViewById(R.id.tv_area);
            tvMoney = (TextView) itemView.findViewById(R.id.tv_money);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_description);

            rlayoutImgChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChatActivity.launch(mContext, 0, mData.get(position).getUserId(),mData.get(position).getAlise(), mData.get(position).getUserPhoto());
                }
            });
            imgAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((LemonActivity)mContext).toDetail("0",mData.get(position).getUserId(),"");
                }
            });
        }
    }
}
