package com.zfb.house.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.model.bean.PhotoModel;

import java.util.ArrayList;

/**
 * Created by Snekey on 2016/5/11.
 */
public class AlbumGridAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<PhotoModel> list;
    private static final String PATH = "file://";

    public AlbumGridAdapter(Context context, ArrayList<PhotoModel> list) {
        this.mContext = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return ParamUtils.isEmpty(list)?0:list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position).getList().get(0);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_album_layout, parent, false);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.img_view);
            viewHolder.tvCount = (TextView) convertView.findViewById(R.id.txt_count);
            viewHolder.tvPathName = (TextView) convertView.findViewById(R.id.txt_pathname);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        String url = getItem(position);
        String count = list.get(position).getList().size() + "";
        String pathName = list.get(position).getRootPath();
        Glide.with(mContext).load(PATH + url).into(viewHolder.imageView);
        viewHolder.tvCount.setText(count);
        viewHolder.tvPathName.setText(pathName);
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView tvCount;
        TextView tvPathName;
    }
}
