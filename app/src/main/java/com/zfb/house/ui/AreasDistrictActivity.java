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
import com.zfb.house.adapter.AreaRightListAdapter;
import com.zfb.house.model.bean.AreaData;
import com.zfb.house.model.bean.UserBean;
import com.zfb.house.model.param.AreaListParam;
import com.zfb.house.model.param.ServiceDistrictParam;
import com.zfb.house.model.param.ServiceVillageParam;
import com.zfb.house.model.result.AreaListResult;
import com.zfb.house.util.ToolUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户：我的->关注的区域->片区选择
 * 经纪人：我的->服务的区域->片区选择
 * Created by Administrator on 2016/5/12.
 */
@Layout(id = R.layout.activity_areas_district)
public class AreasDistrictActivity extends LemonActivity {

    private static final String TAG = "AreasDistrictActivity";
    //片区左边列表
    @FieldView(id = R.id.list_district_left)
    private ListView leftListview;
    //片区右边列表
    @FieldView(id = R.id.list_district_right)
    private ListView rightListview;
    //片区左边适配器
    private AreaLeftListAdapter leftAdapter;
    //片区右边适配器
    private AreaRightListAdapter rightAdapter;

    //从服务器返回的数据
    private List<AreaData> dataList = new ArrayList<>();
    //左边列表的数据
    private List<AreaData> leftList = new ArrayList<>();
    //右边列表的数据
    private List<AreaData> rightList = new ArrayList<>();
    //用户选择片区的列表
    private List<AreaData> selectList = new ArrayList<>();

    //角色
    private String userType;
    private String serviceVillageIds, serviceVillageNames;

    @Override
    protected void initView() {
        setCenterText(R.string.title_district_choose);
        setRtText(R.string.save);

        userType = UserBean.getInstance(mContext).userType;
        //设置左边列表item的监听事件
        leftListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                rightAdapter = new AreaRightListAdapter(mContext, initRightListData(position));
                rightListview.setAdapter(rightAdapter);
            }
        });

        //设置右边列表item的监听事件
        rightListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AreaData item = (AreaData) rightAdapter.getItem(position);//当前选中的片区
                if (item.isSelect) {//选中，注意这边不能用rightListview.isItemChecked(position)判断是否选中！
                    item.setSelect(false);
                    selectList.remove(item);
                    rightAdapter.notifyDataSetChanged();
                } else {//未选中
                    if (selectList.size() >= 3) {//数量大于等于3个
                        lemonMessage.sendMessage(userType.equals("0") ? "最多只能关注3个片区！" : "最多只能服务3个片区！");//弹出提示信息
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
        //调用“获取省市区以及片区”接口——初始化左边ListView数据
        AreaListParam leftListParam = new AreaListParam();
        leftListParam.setLevel("4");
        apiManager.getAreaList(leftListParam);
        //调用“获取省市区以及片区”接口——初始化右边ListView数据
        AreaListParam rightListParam = new AreaListParam();
        rightListParam.setLevel("5");
        apiManager.getAreaList(rightListParam);
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
            lemonMessage.sendMessage(userType.equals("0") ? "最多只能关注3个片区！" : "最多只能服务3个片区！");//弹出提示信息
            return;
        }

        String token = SettingUtils.get(mContext, "token", null);
        //保存选中片区的ID集合到districtIdList
        String[] districtIdList = new String[3];
        for (int i = 0; i < selectList.size(); i++) {
            districtIdList[i] = selectList.get(i).id;
        }
        //将片区的ID集合转化成字符串，并用逗号隔开，存到serviceDistrictIds中以备上传到服务器中更新片区
        String serviceDistrictIds = new ToolUtil().convertArrayToStr(districtIdList);
        //调用“更新片区”接口
        ServiceDistrictParam serviceDistrictParam = new ServiceDistrictParam();
        serviceDistrictParam.setToken(token);
        serviceDistrictParam.setServiceDistrictIds(serviceDistrictIds);
        apiManager.updateServiceDistrict(serviceDistrictParam);

        //更新片区的同时更新小区，并保存小区ID集合到serverVillageId，保存小区名字集合到serverVillageName
        if (!ParamUtils.isEmpty(UserBean.getInstance(mContext).serviceVillage)) {
            String[] villageIdList = UserBean.getInstance(mContext).serviceVillage.split(",");
            String[] villageNameList = UserBean.getInstance(mContext).serviceVillageName.split(",");
            String[] serverVillageId = new String[3];
            String[] serverVillageName = new String[3];
            for (int j = 0, k = 0; j < districtIdList.length; j++) {
                if (!ParamUtils.isEmpty(districtIdList[j]) && !districtIdList[j].equals("null")) {
                    for (int i = 0; i < villageIdList.length; i++) {
                        if (!ParamUtils.isEmpty(villageIdList[i]) && !villageIdList[i].equals("null")) {
                            if (villageIdList[i].substring(0, 8).equals(districtIdList[j])) {
                                serverVillageId[k] = villageIdList[i];
                                serverVillageName[k] = villageNameList[i];
                                k++;
                            }
                        }
                    }
                }
            }
            serviceVillageIds = new ToolUtil().convertArrayToStr(serverVillageId);
            serviceVillageNames = new ToolUtil().convertArrayToStr(serverVillageName);
            //调用“更新小区”接口——用户在取消片区的时候也取消片区相应的小区
            ServiceVillageParam serviceVillageParam = new ServiceVillageParam();
            serviceVillageParam.setToken(token);
            serviceVillageParam.setServiceVillageIds(serviceVillageIds);
            apiManager.updateServiceVillage(serviceVillageParam);
        }

        //保存片区名集合到districtIdList
        String[] districtNameList = new String[3];
        for (int i = 0; i < selectList.size(); i++) {
            districtNameList[i] = selectList.get(i).name;
        }
        //更新本地用户数据
        UserBean userBean = UserBean.getInstance(mContext);
        userBean.serviceDistrict = serviceDistrictIds;
        String serviceDistrictNames = new ToolUtil().convertArrayToStr(districtNameList);
        userBean.serviceDistrictName = serviceDistrictNames;
        if (!ParamUtils.isEmpty(serviceVillageIds) && !ParamUtils.isEmpty(serviceVillageNames)) {
            userBean.serviceVillage = serviceVillageIds;
            userBean.serviceVillageName = serviceVillageNames;
        }
        UserBean.updateUserBean(mContext, userBean);

        //EventBus 更新经纪人服务的片区名和服务的小区名
        UpdateBrokerEvent updateBrokerEvent = new UpdateBrokerEvent();
        updateBrokerEvent.setServiceDistrictName(serviceDistrictNames);
        updateBrokerEvent.setServiceVillageName(serviceVillageNames);
        EventUtil.sendEvent(updateBrokerEvent);

        //将数据存到intent中，在finish之前传给上一次Activity
        Intent intent = new Intent();
        intent.putExtra("districtName", serviceDistrictNames);
        if (!ParamUtils.isEmpty(serviceVillageNames)) {
            intent.putExtra("villageName", serviceVillageNames);
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 初始化右边列表的数据
     *
     * @param position
     * @return
     */
    private List<AreaData> initRightListData(int position) {
        List<AreaData> itemList = new ArrayList<>();
        int count = 0;
        String parentId = leftList.get(position).id;
        for (int i = 0; i < rightList.size(); i++) {
            if (rightList.get(i).parentId.equals(parentId)) {//eg.059201
                count++;
            }
        }
        for (int i = 0, j = 0; i < rightList.size() && j < count; i++) {
            AreaData item = rightList.get(i);
            if (item.parentId.equals(parentId)) {//eg.059201
                itemList.add(j, item);
                j++;

                //用户已经选中的片区
                String district = UserBean.getInstance(mContext).serviceDistrictName;
                if (!ParamUtils.isEmpty(district)) {
                    String[] districtList = district.split(",");
                    for (int k = 0; k < districtList.length; k++) {
                        if (null != districtList[k] && item.name.equals(districtList[k])) {
                            if (!item.isSelect) {
                                //设置该项为选中状态
                                item.setSelect(true);
                                //加入到选中的片区列表中
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
     * 片区列表
     *
     * @param result
     */
    public void onEventMainThread(AreaListResult result) {
        dataList = result.getData();
        String dataType = dataList.get(0).type;
        if (dataType.equals("4")) {//地区
            leftList = dataList;
            leftAdapter = new AreaLeftListAdapter(mContext, leftList);
            leftListview.setAdapter(leftAdapter);
        } else if (dataType.equals("5")) {//片区
            rightList = dataList;
        }

        rightAdapter = new AreaRightListAdapter(mContext, initRightListData(0));
        rightListview.setAdapter(rightAdapter);
    }

}
