package com.zfb.house.ui;

import android.os.Bundle;
import android.view.View;
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
 * 经纪人个人资料 -> 服务信息
 * Created by HourGlassRemember on 2016/8/25.
 */
@Layout(id = R.layout.fragment_broker_service)
public class BrokerServiceFragment extends LemonFragment {

    //专家类型
    @FieldView(id = R.id.txt_broker_type)
    private TextView txtExpertType;
    //成交量
    @FieldView(id = R.id.txt_broker_volume)
    private TextView txtVolume;
    //好评率
    @FieldView(id = R.id.txt_broker_satisfy)
    private TextView txtSatisfy;
    //服务的片区
    @FieldView(id = R.id.txt_broker_district_one)
    private TextView txtDistrictOne;
    @FieldView(id = R.id.txt_broker_district_two)
    private TextView txtDistrictTwo;
    @FieldView(id = R.id.txt_broker_district_three)
    private TextView txtDistrictThree;
    //服务的小区
    @FieldView(id = R.id.txt_broker_village_one)
    private TextView txtVillageOne;
    @FieldView(id = R.id.txt_broker_village_two)
    private TextView txtVillageTwo;
    @FieldView(id = R.id.txt_broker_village_three)
    private TextView txtVillageThree;

    @Override
    protected void initView() {
        Bundle bundle = getArguments();
        //经纪人实体
        UserBean item = (UserBean) bundle.getSerializable("item");
        if (!ParamUtils.isNull(item)) {
            //专家类型
            if (!ParamUtils.isEmpty(item.expertType)) {//经纪人查看非自己的专家类型
                txtExpertType.setText(ToolUtil.setExpertType(item.expertType, true));
            } else if (!ParamUtils.isEmpty(UserBean.getInstance(getActivity()).id) && UserBean.getInstance(getActivity()).id.equals(item.id)) {
                //经纪人查看自己的专家类型，这里要注意一个是判空，因为存在游客模式，判空之后就不会有问题了
                txtExpertType.setText(Character.isDigit(item.expertType.charAt(0)) ? ToolUtil.setExpertType(item.expertType, true) : item.expertType);
            }
            //成交量
            txtVolume.setText((String.valueOf(Integer.valueOf(item.rentVolume) + Integer.valueOf(item.sellVolume))) + "套");
            //好评率
            if (!ParamUtils.isEmpty(item.satisfyDegree)) {
                txtSatisfy.setText(item.satisfyDegree + "%");
            }
            //服务的片区
            setDistrictData(ParamUtils.isEmpty(item.serviceDistrictName) ? "" : item.serviceDistrictName);
            //服务的小区
            setVillageData(ParamUtils.isEmpty(item.serviceVillageName) ? "" : item.serviceVillageName);
        }
    }

    /**
     * 显示经纪人服务的片区
     *
     * @param serviceDistrictIds 服务片区的ID集合字符串
     */
    private void setDistrictData(String serviceDistrictIds) {
        if (!ParamUtils.isEmpty(serviceDistrictIds)) {
            String[] district = serviceDistrictIds.split(",");
            switch (district.length) {
                case 3:
                    if (!"null".equals(district[2])) {
                        txtDistrictThree.setText(district[2]);
                        txtDistrictThree.setVisibility(View.VISIBLE);
                    } else {
                        txtDistrictThree.setVisibility(View.GONE);
                    }
                case 2:
                    if (!"null".equals(district[1])) {
                        txtDistrictTwo.setText(district[1]);
                        txtDistrictTwo.setVisibility(View.VISIBLE);
                    } else {
                        txtDistrictTwo.setVisibility(View.GONE);
                    }
                case 1:
                    if (!"null".equals(district[0])) {
                        txtDistrictOne.setText(district[0]);
                        txtDistrictOne.setVisibility(View.VISIBLE);
                    } else {
                        txtDistrictOne.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    }

    /**
     * 显示经纪人服务的小区
     *
     * @param serviceVillageIds 服务小区的ID集合字符串
     */
    private void setVillageData(String serviceVillageIds) {
        if (!ParamUtils.isEmpty(serviceVillageIds)) {
            String[] village = serviceVillageIds.split(",");
            switch (village.length) {
                case 3:
                    if (!"null".equals(village[2])) {
                        txtVillageThree.setText(village[2]);
                        txtVillageThree.setVisibility(View.VISIBLE);
                    } else {
                        txtVillageThree.setVisibility(View.GONE);
                    }
                case 2:
                    if (!"null".equals(village[1])) {
                        txtVillageTwo.setText(village[1]);
                        txtVillageTwo.setVisibility(View.VISIBLE);
                    } else {
                        txtVillageTwo.setVisibility(View.GONE);
                    }
                case 1:
                    if (!"null".equals(village[0])) {
                        txtVillageOne.setText(village[0]);
                        txtVillageOne.setVisibility(View.VISIBLE);
                    } else {
                        txtVillageOne.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    }

    /**
     * 更新经纪人服务信息
     * 主要包括服务的片区、服务的小区
     *
     * @param event
     */
    public void onEventMainThread(UpdateBrokerEvent event) {
        //服务的片区
        setDistrictData(event.getServiceDistrictName());
        //服务的小区
        setVillageData(event.getServiceVillageName());
    }

}
