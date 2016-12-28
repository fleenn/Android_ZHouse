package com.zfb.house.ui;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.zfb.house.R;
import com.zfb.house.adapter.AreaLeftListAdapter;
import com.zfb.house.adapter.AreaRightListAdapter;
import com.zfb.house.model.bean.AreaData;
import com.zfb.house.model.param.AreaListParam;
import com.zfb.house.model.result.AreaListResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Snekey on 2016/10/18.
 */
@Layout(id = R.layout.activity_district_select)
public class DistrictSelectActivity extends LemonActivity {

    //片区左边列表
    @FieldView(id = R.id.list_district_left)
    private ListView leftListView;
    //片区右边列表
    @FieldView(id = R.id.list_district_right)
    private ListView rightListView;
    //片区左边适配器
    private AreaLeftListAdapter leftAdapter;
    //片区右边适配器
    private AreaRightListAdapter rightAdapter;
    //    片区数据
    private List<AreaData> areaList;

    @Override
    protected void initView() {
        setCenterText("您购房的意向片区");
        rightAdapter = new AreaRightListAdapter(mContext);
        rightListView.setAdapter(rightAdapter);
        leftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                rightAdapter.setmData(getAreaData(position));
                rightAdapter.notifyDataSetChanged();
            }
        });

        rightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (rightAdapter!=null){
                    String districtId = rightAdapter.getmData().get(position).id;
                    String name = rightAdapter.getmData().get(position).name;
                    Intent intent = getIntent();
                    intent.putExtra("name",name);
                    intent.putExtra("id",districtId);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
    }

    private List<AreaData> getAreaData(int position) {
        if (areaList.size() == 0) {
            return null;
        }
        ArrayList<AreaData> list = new ArrayList<>();
        String parentId = leftAdapter.getParentId(position);
        for (AreaData areaData : areaList) {
            if (areaData.parentId.equals(parentId)) {
                list.add(areaData);
            }
        }
        return list;
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
    public void toBask() {
        finish();
    }

    /**
     * 片区列表
     *
     * @param result
     */
    public void onEventMainThread(AreaListResult result) {
        String dataType = result.getData().get(0).type;
        if (dataType.equals("4")) {//地区
            leftAdapter = new AreaLeftListAdapter(mContext, result.getData());
            leftListView.setAdapter(leftAdapter);
        }
        if (dataType.equals("5")) {//片区
            areaList = result.getData();
            rightAdapter.setmData(getAreaData(0));
            rightAdapter.notifyDataSetChanged();
        }
    }
}
