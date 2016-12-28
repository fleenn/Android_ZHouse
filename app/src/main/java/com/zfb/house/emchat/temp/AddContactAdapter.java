package com.zfb.house.emchat.temp;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lemon.LemonContext;
import com.zfb.house.R;
import com.zfb.house.util.ImageUtil;


/**
 * Created by user on 2015-10-28.
 */
public class AddContactAdapter extends RecyclerView.Adapter<AddContactAdapter.ViewHolder> {
    private View mHeadView;
    private View mFootView;
    private static final int HEADVIEW_TYPE = 0x0001;
    private static final int FOOTVIEW_TYPE = 0x0002;
    private static final int MYTRUST_TYPE = 0x0003;
    FriendSearchBean mAKeyOrderBean ;
    private Activity activity;

    public AddContactAdapter(FriendSearchBean mAKeyOrderBean,
                             View headView, View footView, Activity activity) {
        this.mAKeyOrderBean = mAKeyOrderBean;
        this.mHeadView = headView;
        this.mFootView = footView;
        this.activity = activity;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        switch (viewType) {
            case HEADVIEW_TYPE:
                if (mHeadView != null) {
                    viewHolder = new ViewHolder(mHeadView);
                }
                break;
            case FOOTVIEW_TYPE:
                if (mFootView != null) {
                    viewHolder = new ViewHolder(mFootView);
                }
                break;
            case MYTRUST_TYPE:
            default:
                View view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.add_contact_lvitem, null);
                viewHolder = new ViewHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == MYTRUST_TYPE) {
            int rentingIndex = 0;
            if (mHeadView != null) {
                rentingIndex = position - 1;
            } else {
                rentingIndex = position;
            }
            if (getAkeyOrderCount() > 0) {
                final FriendSearchBean.FriendBean bean = mAKeyOrderBean.data.get(rentingIndex);
                if (bean != null) {
                    holder.obtainView(R.id.add_contact_list__layout,View.class).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AddFriendCodeActivity.launch(bean.id,bean.name,bean.photo, activity);
                        }
                    });
                    new ImageUtil().loadImageByVolley(holder.obtainView(R.id.add_contact_list_icon, ImageView.class),
                            bean.photo, (Context) LemonContext.getBean("mContext"), R.drawable.broker_default, R.drawable.broker_default);
                    holder.obtainView(R.id.add_contact_list_cusname, TextView.class).setText(bean.name + "(" + bean.phone+")");
                    holder.obtainView(R.id.add_contact_list_service,TextView.class).setText(bean.serviceDistrictName);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        int count = getAkeyOrderCount();
        if (mHeadView != null) {
            count += 1;
        }
        if (mFootView != null) {
            count += 1;
        }
        return count;
    }

    public int getAkeyOrderCount() {
        return mAKeyOrderBean != null && mAKeyOrderBean.data.size() > 0 ? mAKeyOrderBean.data.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            if (mHeadView != null) {
                return HEADVIEW_TYPE;
            }
        } else if (position == getItemCount() - 1) {
            if (mFootView != null) {
                return FOOTVIEW_TYPE;
            }
        }
        return MYTRUST_TYPE;
    }

    public void setFootView(View mFootView) {
        this.mFootView = mFootView;
    }


    public View getFootView() {
        return mFootView;
    }

    public View getHeadView() {
        return mHeadView;
    }
    public void setBean(FriendSearchBean bean){
        this.mAKeyOrderBean = bean;
    }


public class ViewHolder extends RecyclerViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
