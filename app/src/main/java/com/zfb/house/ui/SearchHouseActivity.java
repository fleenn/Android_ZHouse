package com.zfb.house.ui;

import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.model.StatusCode;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.model.param.UserRequirementParam;
import com.zfb.house.model.result.UserRequirementResult;

import java.lang.reflect.Field;

/**
 * Created by Snekey on 2016/10/18.
 */
@Layout(id = R.layout.activity_search_house)
public class SearchHouseActivity extends LemonActivity {
    private static final String TAG = "SearchHouseActivity";

    private static final int REQ_DISTRICT = 1;
    private static final int REQ_COMMUNITY = 2;
    private static final int REQ_LAYOUT = 3;
    private static final int REQ_AREA = 4;
    private static final int REQ_MONEY = 5;

    //    地区
    @FieldView(id = R.id.tv_district)
    private TextView tvDistrict;
    //    小区
    @FieldView(id = R.id.tv_community)
    private TextView tvCommunity;
    //    格局
    @FieldView(id = R.id.tv_layout)
    private TextView tvLayout;
    //    面积
    @FieldView(id = R.id.tv_area)
    private TextView tvArea;
    //    金额
    @FieldView(id = R.id.tv_money)
    private TextView tvMoney;
    //    附加说明
    @FieldView(id = R.id.et_description)
    private EditText etDescription;

    private UserRequirementParam userRequirementParam;

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBask() {
        finish();
    }

    @OnClick(id = R.id.llayout_district)
    public void toDistrict() {
        Intent intent = new Intent(mContext, DistrictSelectActivity.class);
        startActivityForResult(intent, REQ_DISTRICT);
    }

    @OnClick(id = R.id.llayout_community)
    public void toCommunity() {
        if (!ParamUtils.isEmpty(userRequirementParam.getAreaId())) {
            Intent intent = new Intent(mContext, CommunitySelectActivity.class);
            intent.putExtra("parentId", userRequirementParam.getAreaId());
            startActivityForResult(intent, REQ_COMMUNITY);
        } else {
            lemonMessage.sendMessage("请先选择片区");
        }
    }

    @OnClick(id = R.id.llayout_layout)
    public void toLayout() {
        Intent intent = new Intent(mContext, LayoutTagActivity.class);
        startActivityForResult(intent, REQ_LAYOUT);
    }

    @OnClick(id = R.id.llayout_area)
    public void toArea() {
        Intent intent = new Intent(mContext, AreaTagActivity.class);
        startActivityForResult(intent, REQ_AREA);
    }

    @OnClick(id = R.id.llayout_money)
    public void toMoney() {
        Intent intent = new Intent(mContext, MoneyTagActivity.class);
        startActivityForResult(intent, REQ_MONEY);
    }

    /**
     * 发布需求
     */
    @OnClick(id = R.id.btn_send)
    public void toSend() throws IllegalAccessException {
        userRequirementParam.setToken(SettingUtils.get(mContext, "token", ""));
        userRequirementParam.setDescription(etDescription.getText().toString());
        Field[] fields = userRequirementParam.getClass().getDeclaredFields();
        for (Field field : fields) {
            switch (field.getName()) {
                case "roomType":
                    field.setAccessible(true);
                    String type = (String) field.get(userRequirementParam);
                    if (ParamUtils.isEmpty(type)) {
                        lemonMessage.sendMessage("请选择房屋格局！");
                        return;
                    }
                    break;
                case "area":
                    field.setAccessible(true);
                    String area = (String) field.get(userRequirementParam);
                    if (ParamUtils.isEmpty(area)) {
                        lemonMessage.sendMessage("请选择房屋面积！");
                        return;
                    }
                    break;
                case "cash":
                    field.setAccessible(true);
                    String cash = (String) field.get(userRequirementParam);
                    if (ParamUtils.isEmpty(cash)) {
                        lemonMessage.sendMessage("请选择购房金额！");
                        return;
                    }
                    break;
                default:
                    break;

            }
        }
        apiManager.sendUserRequirement(userRequirementParam);
    }

    @Override
    protected void initView() {
        setCenterText("描述您的购房需求");
    }

    @Override
    protected void initData() {
        userRequirementParam = new UserRequirementParam();
    }

    public void onEventMainThread(UserRequirementResult result) {
        if (!result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            lemonMessage.sendMessage(result.getMessage());
        } else {
            lemonMessage.sendMessage("发布成功！");
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_DISTRICT:
                    String districtId = data.getStringExtra("id");
                    String districtName = data.getStringExtra("name");
                    tvDistrict.setText(districtName);
                    userRequirementParam.setAreaId(districtId);
                    userRequirementParam.setAreaName(districtName);
                    break;
                case REQ_COMMUNITY:
                    String communityId = data.getStringExtra("id");
                    String communityName = data.getStringExtra("name");
                    tvCommunity.setText(communityName);
                    userRequirementParam.setVillageId(communityId);
                    userRequirementParam.setVillageName(communityName);
                    break;
                case REQ_LAYOUT:
                    String layout = data.getStringExtra("layout");
                    tvLayout.setText(layout);
                    userRequirementParam.setRoomType(layout);
                    break;
                case REQ_AREA:
                    String area = data.getStringExtra("area");
                    tvArea.setText(area);
                    userRequirementParam.setArea(area);
                    break;
                case REQ_MONEY:
                    String money = data.getStringExtra("money");
                    tvMoney.setText(money);
                    userRequirementParam.setCash(money);
                    break;
            }
        }
    }
}
