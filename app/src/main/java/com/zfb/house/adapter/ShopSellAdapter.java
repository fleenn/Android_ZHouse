package com.zfb.house.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lemon.LemonCacheManager;
import com.lemon.LemonContext;
import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.component.SettingOperationView;
import com.zfb.house.model.bean.SellItem;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.ui.BrokerShopSellDetailActivity;
import com.zfb.house.util.ToolUtil;

import java.util.List;
import java.util.UUID;

/**
 * 项目名称:  [zfbandroid]
 * 包:        [com.zfb.house.adapter]
 * 类描述:    [类描述]
 * 创建人:    [XiaoFeng]
 * 创建时间:  [2016/6/5 23:11]
 * 修改人:    [XiaoFeng]
 * 修改时间:  [2016/6/5 23:11]
 * 修改备注:  [说明本次修改内容]
 * 版本:      [v1.0]
 */
public class ShopSellAdapter extends BaseAdapter {

    private List<SellItem> models;
    private Handler handler;
    private Context context;
    private LayoutInflater mInflater;
    private UserBean userBean;
    private UserBean selfUserBean;
    private SettingOperationView.OnOperationClickListener onOperationClickListener;

    public ShopSellAdapter(Handler handler, Context context, UserBean userBean,
                           List<SellItem> models, SettingOperationView.OnOperationClickListener onOperationClickListener) {
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.handler = handler;
        this.context = context;
        this.models = models;
        this.userBean = userBean;
        this.onOperationClickListener = onOperationClickListener;
        selfUserBean = UserBean.getInstance(context);
    }

    @Override
    public int getCount() {
        return ParamUtils.isEmpty(models) ? 0 : models.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final SellItem model = models.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_list_broker_shop, null);
            holder = new ViewHolder();
            holder.tv_shop_title = (TextView) convertView.findViewById(R.id.broker_shop_title);
            holder.tv_shop_village = (TextView) convertView.findViewById(R.id.broker_shop_district);
            holder.tv_shop_road = (TextView) convertView.findViewById(R.id.broker_shop_village);
            holder.tv_shop_style = (TextView) convertView.findViewById(R.id.broker_shop_style);
            holder.tv_shop_acreage = (TextView) convertView.findViewById(R.id.broker_shop_acreage);
            holder.tv_shop_decorate = (TextView) convertView.findViewById(R.id.broker_shop_decorate);
            holder.tv_shop_orientation = (TextView) convertView.findViewById(R.id.broker_shop_orientation);
            holder.tv_shop_price = (TextView) convertView.findViewById(R.id.broker_shop_price);
            holder.iv_shop_img = (ImageView) convertView.findViewById(R.id.broker_shop_img);
            holder.rl_parent = (RelativeLayout) convertView.findViewById(R.id.rl_parent);
            holder.iv_shop_up = (ImageView) convertView.findViewById(R.id.iv_shop_up);
            holder.sov_operation = (SettingOperationView) convertView.findViewById(R.id.sov_operation);
            SettingOperationView.setmCurrentShowBtnID("-1");
            convertView.setTag(holder);
            if (onOperationClickListener != null)
                holder.sov_operation.setOnOperationClickListener(onOperationClickListener);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.rl_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSettingView();
                Intent intent = new Intent(context, BrokerShopSellDetailActivity.class);
                String uuid = UUID.randomUUID().toString();//房源ID
                String useruuid = UUID.randomUUID().toString();//经纪人ID
                LemonContext.getBean(LemonCacheManager.class).putBean(uuid, model);
                LemonContext.getBean(LemonCacheManager.class).putBean(useruuid, userBean);
                intent.putExtra("uuid", uuid);
                intent.putExtra("useruuid", useruuid);
                context.startActivity(intent);
            }
        });

        holder.sov_operation.setListPosition(position);
        holder.sov_operation.setData(model);


        if (!ParamUtils.isNull(selfUserBean) && !ParamUtils.isNull(selfUserBean.id)
                && !ParamUtils.isNull(userBean) && !ParamUtils.isNull(userBean.id)
                && selfUserBean.id.equals(userBean.id)) {
            holder.sov_operation.setVisibility(View.VISIBLE);
            if (!ParamUtils.isEmpty(model.getUpDown()) && model.getUpDown().equals("1")) {
                holder.iv_shop_up.setVisibility(View.VISIBLE);
            } else {
                holder.iv_shop_up.setVisibility(View.GONE);
            }
        } else {
            holder.sov_operation.setVisibility(View.GONE);
            holder.iv_shop_up.setVisibility(View.GONE);
        }

        //照片
        if (!ParamUtils.isEmpty(model.getPhoto())) {
            String[] photos = model.getPhoto().split(",");
            Glide.with(context).load(photos[0]).placeholder(R.drawable.defalut_shop_list).into(holder.iv_shop_img);
        }
        //标题
        if (!ParamUtils.isEmpty(model.getTitle())) {
            holder.tv_shop_title.setText(model.getTitle());
        }
        //所在小区
        if (!ParamUtils.isEmpty(model.getCommuntityName())) {
            holder.tv_shop_village.setText(model.getCommuntityName());
        }else {
            holder.tv_shop_village.setText("未知小区");
        }
        //房子样式，比如：2室2厅1卫1阳、房子类型
        if (!ParamUtils.isEmpty(model.getLayout())) {
            holder.tv_shop_style.setText(ToolUtil.setHouseLayout(model.getLayout()));
        } else if (!ParamUtils.isEmpty(model.getOfficeTypeName())) {
            holder.tv_shop_style.setText(model.getOfficeTypeName());
        }else {
            holder.tv_shop_style.setText("");
        }
        //装修
        if (!ParamUtils.isEmpty(model.getDecorationLevelName())) {
            holder.tv_shop_decorate.setText(model.getDecorationLevelName());
        }
        //面积
        if (!ParamUtils.isEmpty(model.getArea())) {
            holder.tv_shop_acreage.setText(model.getArea() + "㎡");
        }
        //朝向/级别
        if (!ParamUtils.isEmpty(model.getDirectionName())) {
            holder.tv_shop_orientation.setText(model.getDirectionName());
        } else if (!ParamUtils.isEmpty(model.getOfficeLevelName())) {
            holder.tv_shop_orientation.setText(model.getOfficeLevelName());
        }
        //售价
        if (!ParamUtils.isEmpty(model.getWishPrice())) {
            holder.tv_shop_price.setText(model.getWishPrice() + "万");
        }

        return convertView;
    }

    public void hideSettingView() {
        SettingOperationView.setmCurrentShowBtnID("-1");
        notifyDataSetChanged();
    }

    private class ViewHolder {
        ImageView iv_shop_img;//照片
        TextView tv_shop_title;//标题
        TextView tv_shop_village;//所在小区
        TextView tv_shop_road;
        TextView tv_shop_style;//房子样式，比如：2室2厅1卫1阳、房子类型
        TextView tv_shop_acreage;//面积
        TextView tv_shop_decorate;//装修
        TextView tv_shop_orientation;//朝向
        TextView tv_shop_price;//售价
        RelativeLayout rlayout_shop_setting;
        RelativeLayout rl_parent;
        SettingOperationView sov_operation;
        ImageView iv_shop_up;

    }
}
