package com.zfb.house.ui;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.emchat.Constant;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.util.ParamUtils;
import com.lemon.util.SettingUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.PoiSearchAdapter;
import com.zfb.house.model.param.CoordinateParam;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Snekey on 2016/5/13.
 */
@Layout(id = R.layout.activity_map)
public class MapActivity extends LemonActivity {

    //    百度地图控件
    @FieldView(id = R.id.bmapView)
    private MapView mMapView;
    //    游标上方信息展示
    @FieldView(id = R.id.tv_position_name)
    private TextView tvPositionName;
    //    搜索输入框
    @FieldView(id = R.id.et_poi_search)
    private EditText etPoiSearch;
    //    检索结果区域
    @FieldView(id = R.id.rlayout_poi_list)
    private RelativeLayout rlayoutPoiList;
    //    检索结果listView
    @FieldView(id = R.id.lv_poi_list)
    private ListView lvPoiList;

    //    纬度
    private double lat = 0.0;
    //    经度
    private double lng = 0.0;
    //    百度地图实例
    private BaiduMap map;
    private MapStatusUpdate mMapStatusUpdate;
    //    地理编码实例
    private GeoCoder mGeoCoder;
    //    poi检索实例
    private PoiSearch mPoiSearch;
    //    poi检索关键字
    private String keyWord = "";
    //    poi检索结果adapter
    private PoiSearchAdapter mPoiSearchAdapter;
    //    poi检索结果list
    private List<PoiInfo> poiInfos;
    //    位置信息
    private String mLocation = "";
    //    是否拖动地图

    @Override
    protected void initView() {
        setLtImg(R.drawable.back);
        setCenterText(R.string.title_location);
        setRtText(R.string.complete);
        setRtTextColor(R.color.title_bar_bg);
//        禁用缩放按钮和比例尺
        mMapView.showScaleControl(false);
        mMapView.showZoomControls(false);
        map = mMapView.getMap();
//        禁用俯视
        map.getUiSettings().setOverlookingGesturesEnabled(false);
//        禁用旋转
        map.getUiSettings().setRotateGesturesEnabled(false);
//        获取本地缓存的地理位置信息
        if (SettingUtils.get(mContext, Constant.IS_CUSTOMIZE, false)) {
            lat = SettingUtils.get(mContext, Constant.CUSTOMIZE_LAT, 24.478314);
            lng = SettingUtils.get(mContext, Constant.CUSTOMIZE_LNG, 118.111461);
        } else {
            lat = SettingUtils.get(mContext, Constant.LAT, 24.478314);
            lng = SettingUtils.get(mContext, Constant.LNG, 118.111461);
        }
//        设置地图默认位置
        setMapStatus();
//        初始化地理编码
        mGeoCoder = GeoCoder.newInstance();
        mGeoCoder.setOnGetGeoCodeResultListener(geoListener);
//        初始化poi检索
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
        rlayoutPoiList.setAlpha(0.9f);
//        初始化listView
        mPoiSearchAdapter = new PoiSearchAdapter(mContext);
        lvPoiList.setAdapter(mPoiSearchAdapter);
        lvPoiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PoiInfo poiInfo = poiInfos.get(position);
                lat = poiInfo.location.latitude;
                lng = poiInfo.location.longitude;
                mLocation = poiInfo.name + poiInfo.address;
                Intent intent = new Intent();
                setCustomize(lat, lng);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                intent.putExtra("location", mLocation);
                setResult(RESULT_OK, intent);
                hideKeyboard();
                finish();
            }
        });
//        地图移动监听
        map.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
                tvPositionName.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
                LatLng target = mapStatus.target;
                mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(target));
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                LatLng target = mapStatus.target;
//                需要截取小数点后六位
                // TODO: 2016/8/13  longitudeE6 是否就是6位小数的坐标
                BigDecimal latDec = new BigDecimal(target.latitude);
                lat = latDec.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
                BigDecimal lngDec = new BigDecimal(target.longitude);
                lng = lngDec.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
        });
//        获取焦点监听
        etPoiSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    rlayoutPoiList.setVisibility(View.VISIBLE);
                    mTvTitleRt.setVisibility(View.GONE);
                    setCenterText(R.string.title_search_location);
                    etPoiSearch.setCursorVisible(true);
                } else {
                    rlayoutPoiList.setVisibility(View.GONE);
                    mTvTitleRt.setVisibility(View.VISIBLE);
                    setCenterText(R.string.title_location);
                    etPoiSearch.setCursorVisible(false);
                }
            }
        });
//        输入文字监听
        etPoiSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                mPoiSearch.searchInCity((new PoiCitySearchOption())
                        .city("厦门")
                        .keyword(text)
                        .pageNum(10));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setMapStatus() {
        LatLng point = new LatLng(lat, lng);
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(point)
                .zoom(17)
                .build();
        mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        map.setMapStatus(mMapStatusUpdate);
    }

    //    反地理编码
    OnGetGeoCoderResultListener geoListener = new OnGetGeoCoderResultListener() {
        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                // 没有检测到结果
            } else {
                if (!ParamUtils.isEmpty(result.getPoiList())) {
                    List<PoiInfo> poiList = result.getPoiList();
                    if (poiList.get(0) != null) {
                        PoiInfo poiInfo = poiList.get(0);
                        String location = poiInfo.address + poiInfo.name;
                        tvPositionName.setText(location);
                        mLocation = location;
                    }
                } else {
                    tvPositionName.setText("未知地点");
                    mLocation = null;
                }
            }
        }

        @Override
        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                // 没有检测到结果
            }
        }
    };

    private void setCustomize(double lat, double lng) {
        SettingUtils.set(mContext, Constant.IS_CUSTOMIZE, true);
        SettingUtils.set(mContext, Constant.CUSTOMIZE_LAT, lat);
        SettingUtils.set(mContext, Constant.CUSTOMIZE_LNG, lng);
    }

    //    poi检索
    OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
        public void onGetPoiResult(PoiResult result) {
//            获取POI检索结果
            poiInfos = result.getAllPoi();
            mPoiSearchAdapter.setmDate(poiInfos);
            mPoiSearchAdapter.notifyDataSetChanged();
        }

        public void onGetPoiDetailResult(PoiDetailResult result) {
//            获取Place详情页检索结果
        }
    };

    //    重置游标位置
    @OnClick(id = R.id.img_reset_position)
    public void toResetPosition() {
        SettingUtils.set(mContext, Constant.IS_CUSTOMIZE, false);
        lat = SettingUtils.get(mContext, Constant.LAT, Constant.DEFAULT_LAT);
        lng = SettingUtils.get(mContext, Constant.LNG, Constant.DEFAULT_LNG);
        setMapStatus();
    }

    //    返回按钮
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        if (rlayoutPoiList.getVisibility() == View.VISIBLE) {
            etPoiSearch.clearFocus();
            hideKeyboard();
        } else {
            finish();
        }
    }

    //    完成按钮点击事件
    @OnClick(id = R.id.tv_title_rt)
    public void toFinish() {
        if (lat != 0.0 && lng != 0.0) {
            CoordinateParam coordinateParam = new CoordinateParam();
            coordinateParam.setLon(lng);
            coordinateParam.setLat(lat);
            coordinateParam.setToken(SettingUtils.get(mContext, "token", ""));
            apiManager.updateCoordinate(coordinateParam);
            SettingUtils.set(mContext, Constant.CUSTOMIZE_LAT, lat);
            SettingUtils.set(mContext, Constant.CUSTOMIZE_LNG, lng);
            if (SettingUtils.get(mContext, Constant.LAT, Constant.DEFAULT_LAT) != lat || SettingUtils.get(mContext, Constant.LNG, Constant.DEFAULT_LNG) != lng) {
                SettingUtils.set(mContext, Constant.IS_CUSTOMIZE, true);
            } else {
                SettingUtils.set(mContext, Constant.IS_CUSTOMIZE, false);
            }
            Intent intent = new Intent();
            intent.putExtra("lat", lat);
            intent.putExtra("lng", lng);
            intent.putExtra("location", mLocation);
            setResult(RESULT_OK, intent);
        }
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mPoiSearch.destroy();
        mGeoCoder.destroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}
