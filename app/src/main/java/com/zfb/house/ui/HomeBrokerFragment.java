package com.zfb.house.ui;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.emchat.Constant;
import com.baidu.location.BDLocation;
import com.lemon.LemonFragment;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.config.Config;
import com.lemon.event.CurrentLocationEvent;
import com.lemon.event.StartLocationEvent;
import com.lemon.util.ScreenUtil;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.BannerAdapter;
import com.zfb.house.adapter.IndexAdapter;
import com.zfb.house.adapter.InfiniteViewPager;
import com.zfb.house.component.LoadMoreRecyclerView;
import com.zfb.house.component.SpacesItemDecoration;
import com.zfb.house.model.bean.Banner;
import com.zfb.house.model.bean.MomentsContent;
import com.zfb.house.model.param.CoordinateParam;
import com.zfb.house.model.param.IndexParam;
import com.zfb.house.model.result.IndexResult;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 经纪人首页
 * Created by Administrator on 2016/4/24.
 */
@Layout(id = R.layout.fragment_home_broker)
public class HomeBrokerFragment extends LemonFragment implements View.OnClickListener {

    //    轮播图
    private InfiniteViewPager infiniteViewPager;
    @FieldView(id = R.id.refresh_broker_home)
    private SwipeRefreshLayout refreshBrokerHome;
    @FieldView(id = R.id.recycler_broker_home)
    private LoadMoreRecyclerView recyclerBrokerHome;

    private IndexParam mIndexParam;
    private IndexAdapter mIndexAdapter;
    String token;

    @Override
    protected void initView() {
        mIndexAdapter = new IndexAdapter(getContext());
        recyclerBrokerHome.setAdapter(mIndexAdapter);
        mIndexAdapter.setOnRecyclerViewListener(new IndexAdapter.OnRecyclerViewListener() {
            @Override
            public void onHeadClick(int position) {
                MomentsContent momentsContent = mIndexAdapter.getDate().get(position);
                toDetail(momentsContent.getType(), momentsContent.getUserId(), "");
            }
        });
        View view = inflater.inflate(R.layout.broker_home_head, null);
        recyclerBrokerHome.addHeaderView(view);
        recyclerBrokerHome.setHeaderEnable(true);
        infiniteViewPager = (InfiniteViewPager) view.findViewById(R.id.infiniteViewPagerBroker);
        view.findViewById(R.id.sv_broker_home).setOnClickListener(this);
        view.findViewById(R.id.llyout_broker_release).setOnClickListener(this);
        view.findViewById(R.id.llyout_broker_store).setOnClickListener(this);
        view.findViewById(R.id.llyout_broker_selling).setOnClickListener(this);
        view.findViewById(R.id.llyout_broker_renting).setOnClickListener(this);
        view.findViewById(R.id.txt_more_moments).setOnClickListener(this);
        int space = ScreenUtil.dip2px(getActivity(), 6.7f);
        recyclerBrokerHome.addItemDecoration(new SpacesItemDecoration(space));
        refreshBrokerHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                EventBus.getDefault().post(new StartLocationEvent());
            }
        });

        GridView grid = new GridView(getActivity());
        grid.getSelectedItemId();
        grid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void initData() {
        token = SettingUtils.get(getActivity(), "token", null);
        EventBus.getDefault().post(new StartLocationEvent());
    }

    //接口返回值处理
    public void onEventMainThread(IndexResult result) {
        BannerAdapter bannerAdapter = new BannerAdapter(getActivity(), result.getData().getBanner());
        infiniteViewPager.setAdapter(bannerAdapter);
        infiniteViewPager.setAutoScrollTime(5000);
        infiniteViewPager.startAutoScroll();
        mIndexAdapter.setData(result.getData().getPickupElite());
        recyclerBrokerHome.notifiyChange();
        refreshBrokerHome.setRefreshing(false);
    }

    public void onEventMainThread(CurrentLocationEvent event) {
        BDLocation location = event.getLocation();
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        if (latitude == 0.0 && longitude == 0.0) {
            latitude = 24.478314;
            longitude = 118.111461;
        } else {
//                    上传坐标
            if (!SettingUtils.get(getContext(), Constant.IS_CUSTOMIZE, false)) {
                CoordinateParam coordinateParam = new CoordinateParam();
                coordinateParam.setLon(longitude);
                coordinateParam.setLat(latitude);
                coordinateParam.setToken(token);
                apiManager.updateCoordinate(coordinateParam);
            }
            SettingUtils.set(getActivity(), "lat", latitude);
            SettingUtils.set(getActivity(), "lng", longitude);
        }
//                调用首页接口
        mIndexParam = new IndexParam();
        mIndexParam.setLat(latitude);
        mIndexParam.setLon(longitude);
        mIndexParam.setToken(token);
        mIndexParam.setRange(Config.getLongValue("broker_range"));
        apiManager.index(mIndexParam);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sv_broker_home:
                startActivity(new Intent(getContext(), SearchActivity.class));
                break;
            case R.id.llyout_broker_release:
//                发布房源
                startActivity(new Intent(getActivity(), ReleaseHousingActivity.class));
                break;
            case R.id.llyout_broker_store:
//                经纪人店铺
                startActivity(new Intent(getActivity(), BrokerShopActivity.class));
                break;
            case R.id.llyout_broker_selling:
//                售房专家
                Intent intentForSell = new Intent(getContext(), BrokerListActivity.class);
                intentForSell.putExtra("brokerType", 1);
                startActivity(intentForSell);
                break;
            case R.id.llyout_broker_renting:
//                租房专家
                Intent intentForRent = new Intent(getContext(), BrokerListActivity.class);
                intentForRent.putExtra("brokerType", 2);
                startActivity(intentForRent);
                break;
            case R.id.txt_more_moments:
                FragmentActivity activity = getActivity();
                ((MainActivity) activity).toFriendsFragment();
                break;
            default:
                break;
        }
    }
}
