package com.zfb.house.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lemon.util.GlideUtil;
import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.model.bean.UserBean;

import java.util.List;

/**
 * 我的 -> 我的好友->搜索好友adapter
 * Created by Administrator on 2016/6/29.
 */
public class SearchFriendsAdapter extends BaseAdapter {

    private Context mContext;//上下文
    private List<UserBean> mData;//数据
    private LayoutInflater mLayoutInflater;

    public SearchFriendsAdapter(Context mContext, List<UserBean> mData) {
        this.mContext = mContext;
        this.mData = mData;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return ParamUtils.isEmpty(mData) ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return ParamUtils.isEmpty(mData) ? null : mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_list_search_friends, parent, false);//初始化布局文件
            holder = new ViewHolder();
            holder.imgAvatar = (ImageView) convertView.findViewById(R.id.img_friend_avatar);
            holder.txtName = (TextView) convertView.findViewById(R.id.txt_friend_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (!ParamUtils.isNull(mData)) {
            UserBean item = mData.get(position);
            //好友头像
            GlideUtil.getInstance().loadUrl(mContext, item.photo, holder.imgAvatar);
            //好友名字，有备注就显示remark，没备注就显示name
            holder.txtName.setText(ParamUtils.isEmpty(item.remark) ? item.name : item.remark);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView imgAvatar;
        TextView txtName;
    }
}
