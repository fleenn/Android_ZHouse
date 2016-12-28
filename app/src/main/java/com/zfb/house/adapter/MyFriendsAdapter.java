package com.zfb.house.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.lemon.util.GlideUtil;
import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.model.bean.UserBean;

import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

/**
 * 我的 -> 我的好友 -> 好友列表adapter
 * Created by Administrator on 2016/5/21.
 */
public class MyFriendsAdapter extends BaseAdapter implements SectionIndexer {
    private String TAG = "MyFriendsAdapter";

    private Context mContext;//上下文
    private List<UserBean> mData;//数据
    private LayoutInflater mLayoutInflater;
    //标记#号第一次出现的位置
    private int sharpLocation = -1;

    public MyFriendsAdapter(Context mContext, List mData) {
        this.mContext = mContext;
        sharpLocation = -1;
        this.mData = mData;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param mData
     */
    public void updateListView(List<UserBean> mData) {
        sharpLocation = -1;
        this.mData = mData;
        notifyDataSetInvalidated();//重绘整个控件
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ParamUtils.isEmpty(mData) ? 0 : mData.size();
    }

    @Override
    public UserBean getItem(int position) {
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
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_list_my_friends, parent, false);//初始化布局文件
            holder.rlayoutGroupName = (RelativeLayout) convertView.findViewById(R.id.rlayout_group_name);
            holder.txtGroupName = (TextView) convertView.findViewById(R.id.txt_group_name);
            holder.txtChildName = (TextView) convertView.findViewById(R.id.txt_child_name);
            holder.imgAvatar = (ImageView) convertView.findViewById(R.id.img_child_head);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (!ParamUtils.isNull(mData)) {
            UserBean item = mData.get(position);
            //分组名
            if (!ParamUtils.isEmpty(item.nameFullPinyin)) {
                if (Character.isLetter(item.nameFullPinyin.toUpperCase().charAt(0))) {//首字母是字符
                    setGroupText(position, holder, item.nameFullPinyin);
                } else {//首字母为非字符
                    if (sharpLocation == -1) {
                        sharpLocation = position;
                        holder.rlayoutGroupName.setVisibility(View.VISIBLE);
                        holder.txtGroupName.setText("#");
                    }
                    if (position > sharpLocation) {
                        holder.rlayoutGroupName.setVisibility(View.GONE);
                    }
                }
            }
            //设置每一组中item的文字
            holder.txtChildName.setText(ParamUtils.isEmpty(item.remark) ? item.name : item.remark);
            //头像
            GlideUtil.getInstance().loadUrl(mContext, item.photo, holder.imgAvatar);
        }
        return convertView;
    }

    private void setGroupText(int position, ViewHolder holder, String label) {
        char firstChar = label.toUpperCase().charAt(0); //获取每个item字符串的头一个字符
        if (position == 0) {  //若为第一个位置直接设置组view就行
            holder.rlayoutGroupName.setVisibility(View.VISIBLE);
            holder.txtGroupName.setText(label.substring(0, 1).toUpperCase());
        } else { //若不是，需判断当前item首字母与上一个item首字母是否一致，再设置组view
            String preLabel = mData.get(position - 1).nameFullPinyin;
//            Log.i(TAG, "preLabel = " + preLabel);
//            Log.i(TAG, "preFirstChar = " + preLabel.toUpperCase().charAt(0));
            if (!ParamUtils.isEmpty(preLabel)) {
                char preFirstChar = preLabel.toUpperCase().charAt(0); //获取上一个item的首字母
                if (firstChar != preFirstChar) {
                    holder.rlayoutGroupName.setVisibility(View.VISIBLE);
                    holder.txtGroupName.setText(label.substring(0, 1).toUpperCase());
                } else {//若与上一个item首字母一致则不需要重复设置组view
                    holder.rlayoutGroupName.setVisibility(View.GONE);
                }
            }
        }

    }

    class ViewHolder {
        RelativeLayout rlayoutGroupName;
        TextView txtGroupName;
        TextView txtChildName;
        ImageView imgAvatar;
    }

    /**
     * 获取组信息的数组，比如这里可以返回char[]{'A','B',...}
     *
     * @return
     */
    @Override
    public Object[] getSections() {
        return null;
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     *
     * @param sectionIndex
     * @return
     */
    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < getCount(); i++) {
            String str = mData.get(i).nameFullPinyin;
            char firstChar = str.toUpperCase().charAt(0);
            if (firstChar == sectionIndex) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     *
     * @param position
     * @return
     */
    @Override
    public int getSectionForPosition(int position) {
        return mData.get(position).nameFullPinyin.charAt(0);
    }


}
