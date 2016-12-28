package com.zfb.house.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lemon.LemonFragment;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.event.UpdateBrokerEvent;
import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.util.ToolUtil;

/**
 * 经纪人个人资料 -> 基本信息
 * Created by HourGlassRemember on 2016/8/25.
 */
@Layout(id = R.layout.fragment_broker_basic)
public class BrokerBasicFragment extends LemonFragment {

    //昵称，如果是好友的话并且有备注，这边就要显示昵称，其他情况下就不显示昵称
    @FieldView(id = R.id.rlayout_broker_name)
    private RelativeLayout rlayoutName;
    @FieldView(id = R.id.txt_broker_name)
    private TextView txtName;
    //联系电话
    @FieldView(id = R.id.txt_broker_phone)
    private TextView txtPhone;
    //所属公司
    @FieldView(id = R.id.txt_broker_company)
    private TextView txtCompany;
    //所属门店
    @FieldView(id = R.id.txt_broker_shop)
    private TextView txtShop;
    //实名认证
    @FieldView(id = R.id.txt_broker_realname)
    private TextView txtRealName;
    //资质认证
    @FieldView(id = R.id.txt_broker_qualification)
    private TextView txtQualification;
    //名片认证
    @FieldView(id = R.id.txt_broker_card)
    private TextView txtCard;

    @Override
    protected void initView() {
        Bundle bundle = getArguments();
        //好友备注
        String remark = bundle.getString("remark");
        //标识是否为好友经纪人
        boolean isBrokerFriend = bundle.getBoolean("isBrokerFriend");
        //经纪人实体
        UserBean item = (UserBean) bundle.getSerializable("item");
        if (!ParamUtils.isNull(item)) {
            //昵称，如果是好友的话并且有备注，这边就要显示昵称，其他情况下就不显示昵称
            if (isBrokerFriend && !ParamUtils.isEmpty(remark) && !remark.equals(item.name)) {
                rlayoutName.setVisibility(View.VISIBLE);
                if (!ParamUtils.isEmpty(item.name)) {
                    txtName.setText(item.name);
                }
            } else {
                rlayoutName.setVisibility(View.GONE);
            }
            //联系电话
            if (!ParamUtils.isEmpty(item.phone)) {
                txtPhone.setText(item.phone);
            }
            //所属公司
            if (!ParamUtils.isEmpty(item.companyName)) {
                txtCompany.setText(item.companyName);
            }
            //所属门店
            if (!ParamUtils.isEmpty(item.star)) {
                txtShop.setText(item.store);
            }
            //实名认证
            ToolUtil.setAuthentication(item.smrzState, txtRealName, getResources());
            //资质认证
            ToolUtil.setAuthentication(item.zzrzState, txtQualification, getResources());
            // 名片认证
            // TODO: 2016/8/25 目前资质认证和名片认证是在同一个页面的，所以字段先拿资质认证的字段来显示
            ToolUtil.setAuthentication(item.zzrzState, txtCard, getResources());
        }
    }

    /**
     * 更新经纪人基本信息
     * 主要包括所属公司、所属门店、实名认证、资质认证、名片认证
     *
     * @param event
     */
    public void onEventMainThread(UpdateBrokerEvent event) {
        //所属公司
        if (!ParamUtils.isEmpty(event.getCompany())) {
            txtCompany.setText(event.getCompany());
        }
        //所属门店
        if (!ParamUtils.isEmpty(event.getShop())) {
            txtShop.setText(event.getShop());
        }
        //实名认证
        if (!ParamUtils.isEmpty(event.getSmrzState())) {
            ToolUtil.setAuthentication(event.getSmrzState(), txtRealName, getResources());
        } else if ("1".equals(UserBean.getInstance(getActivity()).smrzState)) {
            txtRealName.setText(R.string.wait_check);
        }
        //资质认证、名片认证
        if (!ParamUtils.isEmpty(event.getZzrzState())) {
            // TODO: 2016/8/25 目前资质认证和名片认证是在同一个页面的，所以字段先拿资质认证的字段来显示
            ToolUtil.setAuthentication(event.getZzrzState(), txtQualification, getResources());
            ToolUtil.setAuthentication(event.getZzrzState(), txtCard, getResources());
        } else if ("1".equals(UserBean.getInstance(getActivity()).zzrzState)) {
            txtQualification.setText(R.string.wait_check);
            txtCard.setText(R.string.wait_check);
        }
    }

}
