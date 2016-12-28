package com.zfb.house.ui;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.android.emchat.Constant;
import com.baidu.location.BDLocation;
import com.lemon.LemonFragment;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.config.Config;
import com.lemon.event.AnonLoginEvent;
import com.lemon.event.CurrentLocationEvent;
import com.lemon.event.StartLocationEvent;
import com.lemon.util.EventUtil;
import com.lemon.util.ParamUtils;
import com.lemon.util.ScreenUtil;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.BannerAdapter;
import com.zfb.house.adapter.BrokerGridAdapter;
import com.zfb.house.adapter.IndexAdapter;
import com.zfb.house.adapter.InfiniteViewPager;
import com.zfb.house.component.LoadMoreRecyclerView;
import com.zfb.house.component.SpacesItemDecoration;
import com.zfb.house.model.bean.Banner;
import com.zfb.house.model.bean.Broker;
import com.zfb.house.model.bean.MomentsContent;
import com.zfb.house.model.param.CoordinateParam;
import com.zfb.house.model.param.IndexParam;
import com.zfb.house.model.result.IndexResult;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/4/24.
 */
@Layout(id = R.layout.fragment_home_user)
public class HomeUserFragment extends LemonFragment implements View.OnClickListener {
    //    轮播图
    private InfiniteViewPager infiniteViewPager;
    //    附近经纪人gridview
    private GridView gvIndexBroker;
    @FieldView(id = R.id.recycler_user_home)
    private LoadMoreRecyclerView recyclerHome;
    @FieldView(id = R.id.refresh_user_home)
    private SwipeRefreshLayout refreshHome;

    private IndexParam mIndexParam;
    private IndexAdapter mIndexAdapter;
    private BrokerGridAdapter brokerGridAdapter;
    private String token;

    @Override
    protected void initView() {
        mIndexAdapter = new IndexAdapter(getContext());
        mIndexAdapter.setOnRecyclerViewListener(new IndexAdapter.OnRecyclerViewListener() {
            @Override
            public void onHeadClick(int position) {
                MomentsContent momentsContent = mIndexAdapter.getDate().get(position);
                toDetail(momentsContent.getType(), momentsContent.getUserId(), "");
            }
        });
        recyclerHome.setAdapter(mIndexAdapter);
        recyclerHome.setHeaderEnable(true);
        int space = ScreenUtil.dip2px(getActivity(), 6.7f);
        recyclerHome.addItemDecoration(new SpacesItemDecoration(space));
        View headView = inflater.inflate(R.layout.user_home_head, null, false);
        recyclerHome.addHeaderView(headView);
        infiniteViewPager = (InfiniteViewPager) headView.findViewById(R.id.infiniteViewPagerUser);
        headView.findViewById(R.id.sv_home_search).setOnClickListener(this);
        headView.findViewById(R.id.llyout_user_entrust).setOnClickListener(this);
        headView.findViewById(R.id.llyout_user_selling).setOnClickListener(this);
        headView.findViewById(R.id.llyout_user_renting).setOnClickListener(this);
        headView.findViewById(R.id.llyout_user_service).setOnClickListener(this);
        headView.findViewById(R.id.txt_more_above).setOnClickListener(this);
        headView.findViewById(R.id.txt_more_moments).setOnClickListener(this);
        gvIndexBroker = (GridView) headView.findViewById(R.id.gv_index_broker);
        refreshHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                EventBus.getDefault().post(new StartLocationEvent());
            }
        });
    }

    @Override
    protected void initData() {
        brokerGridAdapter = new BrokerGridAdapter(getActivity());
        token = SettingUtils.get(getActivity(), "token", null);
        SettingUtils.set(getActivity(), Constant.IS_CUSTOMIZE, false);
        EventBus.getDefault().post(new StartLocationEvent());
        brokerGridAdapter.setOnBrokerTouchListener(new BrokerGridAdapter.OnBrokerTouchListener() {
            @Override
            public void onTouch(int position) {
                Broker broker = brokerGridAdapter.getmDate().get(position);
                toDetail("1", broker.id, broker.alise);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            搜索
            case R.id.sv_home_search:
                startActivity(new Intent(getContext(), SearchActivity.class));
                break;
            case R.id.llyout_user_entrust:
//                业主委托
                if (ParamUtils.isEmpty(SettingUtils.get(getActivity(), "token", ""))) {
                    EventUtil.sendEvent(new AnonLoginEvent());
                } else {
                    startActivity(new Intent(getActivity(), ReleaseHousingActivity.class));
                }
                break;
            case R.id.llyout_user_selling:
//                售房专家
                Intent intentForSell = new Intent(getContext(), BrokerListActivity.class);
                intentForSell.putExtra("brokerType", 1);
                startActivity(intentForSell);
                break;
            case R.id.llyout_user_renting:
//                租房专家
                Intent intentForRent = new Intent(getContext(), BrokerListActivity.class);
                intentForRent.putExtra("brokerType", 2);
                startActivity(intentForRent);
                break;
            case R.id.llyout_user_service:
//                购房委托
                if (ParamUtils.isEmpty(SettingUtils.get(getActivity(), "token", ""))) {
                    EventUtil.sendEvent(new AnonLoginEvent());
                } else {
                    startActivity(new Intent(getActivity(), SearchHouseActivity.class));
                }
                break;
            case R.id.txt_more_above:
//                更多专家
                startActivity(new Intent(getContext(), NearBrokerListActivity.class));
                break;
            case R.id.txt_more_moments:
                FragmentActivity activity = getActivity();
                ((MainActivity) activity).toFriendsFragment();
                break;
            default:
                break;
        }
    }

    //接口返回值处理
    public void onEventMainThread(IndexResult result) {
//        girdview adapter
        List<Broker> brokers = result.getData().getNearByBroker();
        brokerGridAdapter.setmDate(brokers);
        gvIndexBroker.setAdapter(brokerGridAdapter);
//        轮播图
        BannerAdapter bannerAdapter = new BannerAdapter(getActivity(), result.getData().getBanner());
        infiniteViewPager.setAdapter(bannerAdapter);
        infiniteViewPager.setAutoScrollTime(5000);
        infiniteViewPager.startAutoScroll();
        mIndexAdapter.setData(result.getData().getPickupElite());
        recyclerHome.notifiyChange();
        refreshHome.setRefreshing(false);
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
            if (!SettingUtils.get(getContext(), Constant.IS_CUSTOMIZE, false) && !ParamUtils.isNull(token)) {
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
        mIndexParam.setRange(Config.getIntValue("broker_range"));
        apiManager.index(mIndexParam);
    }

}
