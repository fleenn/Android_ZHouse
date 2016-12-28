package com.zfb.house.ui;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.event.UpdateBrokerEvent;
import com.lemon.util.EventUtil;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.AreaLeftListAdapter;
import com.zfb.house.adapter.VillageRightListAdapter;
import com.zfb.house.model.bean.AreaData;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.model.bean.VillageData;
import com.zfb.house.model.param.ServiceVillageParam;
import com.zfb.house.model.param.VillageParam;
import com.zfb.house.model.result.VillageResult;
import com.zfb.house.util.ToolUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户：我的->关注的区域->小区选择
 * 经纪人：我的->服务的区域->小区选择
 * Created by Administrator on 2016/5/12.
 */
@Layout(id = R.layout.activity_areas_village)
public class AreasVillageActivity extends LemonActivity {

    //小区左边列表
    @FieldView(id = R.id.list_village_left)
    private ListView leftListview;
    //小区右边列表
    @FieldView(id = R.id.list_village_right)
    private ListView rightListview;
    //小区左边列表适配器
    private AreaLeftListAdapter leftAdapter;
    //小区右边列表适配器
    private VillageRightListAdapter rightAdapter;

    //左边列表的数据
    private List<AreaData> leftList = new ArrayList<>();
    //右边列表的数据
    private List<VillageData> rightList = new ArrayList<>();
    //用户选择小区的列表
    private List<VillageData> selectList = new ArrayList<>();
    private String districtId;
    //角色
    private String userType;

    @Override
    protected void initView() {
        setCenterText(R.string.title_village_choose);
        setRtText(R.string.save);

        userType = UserBean.getInstance(mContext).userType;
        //设置左边列表item的监听事件
        leftListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                rightAdapter = new VillageRightListAdapter(mContext, initRightListData(position));
                rightListview.setAdapter(rightAdapter);
            }
        });

        //设置右边列表item的监听事件
        rightListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VillageData item = (VillageData) rightAdapter.getItem(position);//当前选中的小区
                if (item.isSelect) {//选中，注意这边不能用rightListview.isItemChecked(position)判断是否选中！
                    item.setSelect(false);
                    selectList.remove(item);
                    rightAdapter.notifyDataSetChanged();
                } else {//未选中
                    if (selectList.size() >= 3) {//数量大于等于3个
                        lemonMessage.sendMessage(userType.equals("0") ? "最多只能关注3个小区！" : "最多只能服务3个小区！");//弹出提示信息
                    } else {//数量小于3个
                        item.setSelect(true);
                        selectList.add(item);
                        rightAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {
        leftList = initLeftListData();
        leftAdapter = new AreaLeftListAdapter(mContext, leftList);
        leftListview.setAdapter(leftAdapter);

        //调用“获取小区”接口——右边ListView数据
        districtId = UserBean.getInstance(mContext).serviceDistrict;
        if (!ParamUtils.isEmpty(districtId)) {
            String[] districtIdList = districtId.split(",");
            for (int k = 0; k < districtIdList.length; k++) {
                //调用“获取小区”接口——右边ListView数据
                VillageParam rightListParam = new VillageParam();
                rightListParam.setParentId(districtIdList[k]);
                apiManager.getVillage(rightListParam);
            }
        }
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        finish();
    }

    /**
     * 保存
     */
    @OnClick(id = R.id.tv_title_rt)
    public void toSaveData() {
        if (selectList.size() > 3) {
            lemonMessage.sendMessage(userType.equals("0") ? "最多只能关注3个小区！" : "最多只能服务3个小区！");//弹出提示信息
            return;
        }

        String token = SettingUtils.get(mContext, "token", null);
        //保存选中小区的ID集合到villageIdList
        String[] villageIdList = new String[3];
        for (int i = 0; i < selectList.size(); i++) {
            villageIdList[i] = selectList.get(i).id;
        }
        //将小区的ID集合转化成字符串，并用逗号隔开，存到serviceVillageIds中以备上传到服务器中更新用户关注的小区
        String serviceVillageIds = new ToolUtil().convertArrayToStr(villageIdList);
        //调用“更新用户关注的小区”接口
        ServiceVillageParam serviceVillageParam = new ServiceVillageParam();
        serviceVillageParam.setToken(token);
        serviceVillageParam.setServiceVillageIds(serviceVillageIds);
        apiManager.updateServiceVillage(serviceVillageParam);

        //保存小区名集合到villageIdList
        String[] villageNameList = new String[3];
        for (int i = 0; i < selectList.size(); i++) {
            villageNameList[i] = selectList.get(i).name;
        }
        String serviceVillageNames = new ToolUtil().convertArrayToStr(villageNameList);
        UserBean userBean = UserBean.getInstance(mContext);
        userBean.serviceVillage = serviceVillageIds;
        userBean.serviceVillageName = serviceVillageNames;
        UserBean.updateUserBean(mContext, userBean);

        //EventBus 更新经纪人服务的小区名
        UpdateBrokerEvent updateBrokerEvent = new UpdateBrokerEvent();
        updateBrokerEvent.setServiceVillageName(serviceVillageNames);
        EventUtil.sendEvent(updateBrokerEvent);

        //将数据存到intent中，在finish之前传给上一次Activity
        Intent intent = new Intent();
        intent.putExtra("villageName", serviceVillageNames);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 初始化左边列表的数据
     *
     * @return
     */
    private List<AreaData> initLeftListData() {
        Intent intent = getIntent();
        String[] districtIds = intent.getStringArrayExtra("districtIds");
        String[] districtNames = intent.getStringArrayExtra("districtNames");
        List<AreaData> selectDistrictList = new ArrayList<>();
        if (!ParamUtils.isEmpty(districtIds)) {
            for (int i = 0; i < districtIds.length; i++) {
                if (!districtIds[i].equals("null")) {
                    AreaData item = new AreaData();
                    item.id = districtIds[i];
                    item.name = districtNames[i];
                    selectDistrictList.add(item);
                }
            }
        }
        return selectDistrictList;
    }

    /**
     * 初始化右边列表的数据
     *
     * @param position
     * @return
     */
    private List<VillageData> initRightListData(int position) {
        String parentId = leftList.get(position).id;
        int count = 0;
        List<VillageData> itemList = new ArrayList<>();
        for (int i = 0; i < rightList.size(); i++) {
            if (rightList.get(i).pid.equals(parentId)) {//eg.059201
                count++;
            }
        }
        for (int i = 0, j = 0; i < rightList.size() && j < count; i++) {
            VillageData item = rightList.get(i);
            if (item.pid.equals(parentId)) {//eg.059201
                itemList.add(j, item);
                j++;

                //用户已经选中的小区
                String village = UserBean.getInstance(mContext).serviceVillageName;
                if (!ParamUtils.isEmpty(village)) {
                    String[] villageList = village.split(",");
                    for (int k = 0; k < villageList.length; k++) {
                        if (null != villageList[k] && item.name.equals(villageList[k])) {
                            if (!item.isSelect) {
                                //设置该项为选中状态
                                item.setSelect(true);
                                //加入到选中的小区列表中
                                selectList.add(item);
                            }
                        }
                    }
                }

            }
        }
        return itemList;
    }

    /**
     * 小区列表
     *
     * @param result
     */
    public void onEventMainThread(VillageResult result) {
        if (!ParamUtils.isEmpty(districtId)) {
            List<VillageData> tempList = result.getData();
            if (null != tempList && tempList.size() > 0) {
                if (tempList.get(0).type.equals("8")) {
                    rightList.addAll(tempList);
                }
            }
            rightAdapter = new VillageRightListAdapter(mContext, initRightListData(0));
            rightListview.setAdapter(rightAdapter);
        }
    }

}
