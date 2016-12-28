package com.zfb.house.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zfb.house.R;
import com.zfb.house.model.bean.Banner;
import com.zfb.house.ui.BannerActivity;

import java.util.List;

/**
 * 首页banner适配器
 */
public class BannerAdapter extends InfinitePagerAdapter {

    private Context mContext;
    private List<Banner> imgUrls;
    private boolean isFinance;

    public void setIsFinance(boolean isFinance) {
        this.isFinance = isFinance;
    }

    public BannerAdapter(Context mContext, List<Banner> imgUrls) {
        this.mContext = mContext;
        this.imgUrls = imgUrls;
    }

    @Override
    public int getItemCount() {
        return imgUrls.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup container) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_pager_banner, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.img_banner);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,BannerActivity.class);
                intent.putExtra("url",imgUrls.get(position).getUrl());
                mContext.startActivity(intent);
            }
        });
        Glide.with(mContext).load(imgUrls.get(position).getImage()).placeholder(isFinance ? R.drawable.default_finance_banner : R.drawable.default_banner).into(holder.image);
        return convertView;
    }

    private static class ViewHolder {
        ImageView image;
    }
}

