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
import com.zfb.house.component.RatingBar;
import com.zfb.house.model.bean.Broker;

import java.util.List;

/**
 * Created by Snekey on 2016/5/6.
 */
public class BrokerGridAdapter extends BaseAdapter {

    private List<Broker> mDate;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private OnBrokerTouchListener onBrokerTouchListener;

    public BrokerGridAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public List<Broker> getmDate() {
        return mDate;
    }

    public void setmDate(List<Broker> mDate) {
        this.mDate = mDate;
    }

    public void setOnBrokerTouchListener(OnBrokerTouchListener onBrokerTouchListener) {
        this.onBrokerTouchListener = onBrokerTouchListener;
    }

    public interface OnBrokerTouchListener {
        void onTouch(int position);
    }

    @Override
    public int getCount() {
        return ParamUtils.isEmpty(mDate) ? 0 : mDate.size();
    }

    @Override
    public Object getItem(int position) {
        return ParamUtils.isEmpty(mDate) ? null : mDate.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final Broker broker = mDate.get(position);
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_grid_index_broker_layout, parent, false);
            holder = new ViewHolder();
            holder.imgIcon = (ImageView) convertView.findViewById(R.id.img_index_broker);
            holder.ratingBar = (RatingBar) convertView.findViewById(R.id.star_near_broker);
            holder.imgIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onBrokerTouchListener != null) {
                        onBrokerTouchListener.onTouch(position);
                    }
                }
            });
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_index_broker);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GlideUtil.getInstance().loadUrl(mContext, broker.photo, holder.imgIcon);
        holder.tvName.setText(broker.name);
        holder.ratingBar.setStar(broker.star);
        return convertView;
    }

    private class ViewHolder {
        ImageView imgIcon;
        TextView tvName;
        RatingBar ratingBar;
    }

}
