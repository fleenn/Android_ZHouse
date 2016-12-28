package com.zfb.house.ui;

import android.content.Intent;
import android.widget.TextView;

import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.model.bean.UserBean;

/**
 * 用户：我的->关注的区域 首页
 * 经纪人：我的->服务的区域 首页
 * Created by Administrator on 2016/5/12.
 */
@Layout(id = R.layout.activity_areas)
public class AreasActivity extends LemonActivity {

    private static final String TAG = "AreasActivity";
    //片区的请求码
    private static final int REQUEST_DISTRICT = 0x3;
    //小区的请求码
    private static final int REQUEST_VILLAGE = 0x4;

    //关注的片区 or 服务的片区
    @FieldView(id = R.id.txt_district)
    private TextView txtDistrict;
    //关注的小区 or 服务的小区
    @FieldView(id = R.id.txt_village)
    private TextView txtVillage;

    //三个片区
    @FieldView(id = R.id.txt_district_one)
    private TextView txtDistrictOne;
    @FieldView(id = R.id.txt_district_two)
    private TextView txtDistrictTwo;
    @FieldView(id = R.id.txt_district_three)
    private TextView txtDistrictThree;
    //三个小区
    @FieldView(id = R.id.txt_village_one)
    private TextView txtVillageOne;
    @FieldView(id = R.id.txt_village_two)
    private TextView txtVillageTwo;
    @FieldView(id = R.id.txt_village_three)
    private TextView txtVillageThree;
    //已选片区的片区Name数组
    private String[] districtNames;
    //已选小区的小区Name数组
    private String[] villageNames;

    @Override
    protected void initView() {
        String userType = UserBean.getInstance(mContext).userType;
        if (userType.equals("0")) {//用户
            setCenterText(R.string.img_mine_concern_areas);
            txtDistrict.setText(R.string.label_user_district);
            txtVillage.setText(R.string.label_user_village);
        } else if (userType.equals("1")) {//经纪人
            setCenterText(R.string.img_mine_service_areas);
            txtDistrict.setText(R.string.label_broker_district);
            txtVillage.setText(R.string.label_broker_village);
        }
    }

    @Override
    protected void initData() {
        //片区
        setDistrictData(UserBean.getInstance(mContext).serviceDistrictName);
        //小区
        setVillageData(UserBean.getInstance(mContext).serviceVillageName);
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        finish();
    }

    /**
     * 片区
     */
    @OnClick(id = R.id.rlayout_district)
    public void toDistrict() {
        startActivityForResult(new Intent(this, AreasDistrictActivity.class), REQUEST_DISTRICT);
    }

    /**
     * 小区
     */
    @OnClick(id = R.id.rlayout_village)
    public void toVillage() {//小区
        Intent intent = new Intent(this, AreasVillageActivity.class);
        String[] districtIds = UserBean.getInstance(mContext).serviceDistrict.split(",");
        if (!ParamUtils.isEmpty(districtIds)) {
            intent.putExtra("districtIds", districtIds);
            intent.putExtra("districtNames", districtNames);
        }
        startActivityForResult(intent, REQUEST_VILLAGE);
    }

    /**
     * 显示用户关注的片区 or 经纪人服务的片区
     *
     * @param serviceDistrictName
     */
    private void setDistrictData(String serviceDistrictName) {
        if (!ParamUtils.isEmpty(serviceDistrictName)) {
            districtNames = serviceDistrictName.split(",");
            switch (districtNames.length) {
                case 3:
                    txtDistrictThree.setText(districtNames[2].equals("null") ? "" : districtNames[2]);
                case 2:
                    txtDistrictTwo.setText(districtNames[1].equals("null") ? "" : districtNames[1]);
                case 1:
                    txtDistrictOne.setText(districtNames[0].equals("null") ? "" : districtNames[0]);
                    break;
            }
        }
    }

    /**
     * 显示用户关注的小区 or 经纪人服务的小区
     *
     * @param serviceVillageName
     */
    private void setVillageData(String serviceVillageName) {
        if (!ParamUtils.isEmpty(serviceVillageName)) {
            villageNames = serviceVillageName.split(",");
            switch (villageNames.length) {
                case 3:
                    txtVillageThree.setText(villageNames[2].equals("null") ? "" : villageNames[2]);
                case 2:
                    txtVillageTwo.setText(villageNames[1].equals("null") ? "" : villageNames[1]);
                case 1:
                    txtVillageOne.setText(villageNames[0].equals("null") ? "" : villageNames[0]);
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_DISTRICT://片区
                    setDistrictData(data.getStringExtra("districtName"));
                    setVillageData(data.getStringExtra("villageName"));
                    break;
                case REQUEST_VILLAGE://小区
                    setVillageData(data.getStringExtra("villageName"));
                    break;
            }
        }
    }

}
