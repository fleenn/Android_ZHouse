package com.zfb.house.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lemon.LemonCacheManager;
import com.lemon.LemonContext;
import com.lemon.config.Config;
import com.lemon.util.GlideUtil;
import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.model.bean.AddPeopleItem;

import java.util.List;

/**
 * Created by Snekey on 2016/5/17.
 * 经纪人房源委托adpter
 */
public class AddSearchFriendsAdapter extends RecyclerView.Adapter {
    private List<AddPeopleItem> mDatas;
    private Context mContext;
    private LemonCacheManager cacheManager;
    public interface OnAddClickListener{
        void onClick(AddPeopleItem info);
        void onIntentClick(AddPeopleItem info);
    }

    private OnAddClickListener onAddClickListener;

    public OnAddClickListener getOnAddClickListener() {
        return onAddClickListener;
    }

    public void setOnAddClickListener(OnAddClickListener onAddClickListener) {
        this.onAddClickListener = onAddClickListener;
    }

    public AddSearchFriendsAdapter(Context context, List<AddPeopleItem> datas) {
        cacheManager = LemonContext.getBean(LemonCacheManager.class);
        this.mContext = context;
        mDatas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_search_friends, null);
        return new EntrustViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final EntrustViewHolder viewHolder = (EntrustViewHolder) holder;
        final AddPeopleItem info = mDatas.get(position);

        viewHolder.tvName.setText(info.getName());
        GlideUtil.getInstance().loadUrl(mContext, info.getPhoto(), viewHolder.ivHeader);
        if (TextUtils.isEmpty(judueFriends(info.getId()))){
            viewHolder.btnAdd.setText("添加");
            viewHolder.btnAdd.setBackgroundResource(R.drawable.shape_release_orange);
        }else{
            viewHolder.btnAdd.setText("已添加");
            viewHolder.btnAdd.setBackgroundResource(R.drawable.shape_release_gray);
        }
        viewHolder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(judueFriends(info.getId()))){
                    onAddClickListener.onClick(info);
                }
            }
        });

        viewHolder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddClickListener.onIntentClick(info);
            }
        });

    }

    @Override
    public int getItemCount() {
        return ParamUtils.isEmpty(mDatas) ? 0 : mDatas.size();
    }

    class EntrustViewHolder extends RecyclerView.ViewHolder {
        ImageView ivHeader;
        TextView tvName;
        Button btnAdd;
        LinearLayout llMain;


        public EntrustViewHolder(View itemView) {
            super(itemView);
            ivHeader = (ImageView) itemView.findViewById(R.id.iv_avatar);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            btnAdd = (Button)itemView.findViewById(R.id.btn_add);
            llMain = (LinearLayout)itemView.findViewById(R.id.search_friends_main);

        }
    }

    /**
     * 判断是否为好友
     * @return
     */
    private String judueFriends(String userId){
        if (TextUtils.isEmpty(userId)) return "error";
        List<String> userFriends = (List<String>) cacheManager.getBean(Config.getValue("userFriends"));
        List<String> brokerFriends = (List<String>) cacheManager.getBean(Config.getValue("brokerFriends"));
        if (userFriends != null){
            for (int i = 0;i < userFriends.size();i++){
                if (userId.equals(userFriends.get(i))){
                    return userId;
                }
            }
        }
        if (brokerFriends != null){
            for (int i = 0;i < brokerFriends.size();i++){
                if (userId.equals(brokerFriends.get(i))){
                    return userId;
                }
            }
        }

        return "";
    }
}
