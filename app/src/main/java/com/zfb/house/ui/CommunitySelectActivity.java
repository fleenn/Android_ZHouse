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
import com.zfb.house.adapter.VillageRightListAdapter;
import com.zfb.house.model.bean.VillageData;
import com.zfb.house.model.param.VillageParam;
import com.zfb.house.model.result.VillageResult;

import java.util.List;

/**
 * Created by Snekey on 2016/10/19.
 */
@Layout(id = R.layout.activity_community_select)
public class CommunitySelectActivity extends LemonActivity{
    private static final String TAG = "CommunitySelectActivity";

    @FieldView(id = R.id.lv_community)
    private ListView lvCommunity;

    private VillageRightListAdapter adapter;

    @Override
    protected void initView() {
        adapter = new VillageRightListAdapter(mContext);
        lvCommunity.setAdapter(adapter);
        lvCommunity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = adapter.getData().get(position).name;
                String communityId = adapter.getData().get(position).id;
                Intent intent = getIntent();
                intent.putExtra("name",name);
                intent.putExtra("id",communityId);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBask() {
        finish();
    }

    @Override
    protected void initData() {
        String parentId = getIntent().getStringExtra("parentId");
        VillageParam villageParam = new VillageParam();
        villageParam.setParentId(parentId);
        apiManager.getVillage(villageParam);
    }

    public void onEventMainThread(VillageResult result){
        List<VillageData> data = result.getData();
        adapter.setData(data);
        adapter.notifyDataSetChanged();
    }
}
