package com.lemon;

import android.content.Context;
import android.os.Handler;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.lemon.annotation.Autowired;
import com.lemon.annotation.Component;
import com.lemon.annotation.InitMethod;
import com.lemon.event.CurrentLocationEvent;
import com.lemon.event.StartLocationEvent;
import com.lemon.event.StopLocationEvent;
import com.lemon.event.ToastEvent;
import com.lemon.util.EventUtil;
import com.lemon.util.ParamUtils;

import de.greenrobot.event.EventBus;

/**
 * 项目名称:  [CarMonitor]
 * 包:        [com.lemon]
 * 类描述:    [类描述]
 * 创建人:    [XiaoFeng]
 * 创建时间:  [2016/1/9 14:56]
 * 修改人:    [XiaoFeng]
 * 修改时间:  [2016/1/9 14:56]
 * 修改备注:  [说明本次修改内容]
 * 版本:      [v1.0]
 */
@Component
public class LemonLocation {
    @Autowired
    public Context mContext;
    LocationClient mLocClient;
    public MyLocationListener myListener = new MyLocationListener();
    private static final int TIMEOUT = 2*60*1000;
    public BDLocation currentLocation;
    public String locationInfo = "";

    @InitMethod
    public void init(){
        SDKInitializer.initialize(mContext);
        EventUtil.register(this);
        mLocClient = new LocationClient(mContext);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setCoorType(BDLocation.BDLOCATION_BD09_TO_GCJ02);
        option.setScanSpan(300000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    public void onEventAsync(StartLocationEvent event){
        if(!mLocClient.isStarted()) {
            mLocClient.start();
        }
    }

    public void onEventAsync(StopLocationEvent event){
        mLocClient.stop();
    }

    public BDLocation getLocation(){
        return currentLocation;
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //取经纬度
            if (ParamUtils.isNull(location)) {
                EventBus.getDefault().post(new ToastEvent("定位失败"));
                return;
            }
            currentLocation = location;
            if(!isFindLocation){
                isFindLocation = true;
                findLocation(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()));
            }
            EventBus.getDefault().post(new CurrentLocationEvent(location));
            mLocClient.stop();
        }
    }

    private boolean isFindLocation = false;
    private void findLocation(LatLng latLng){
        GeoCoder geoCoder = GeoCoder.newInstance();
        //
        OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            // 反地理编码查询结果回调函数
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null
                        || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    // 没有检测到结果
                    EventBus.getDefault().post(new ToastEvent("抱歉，未能找到结果"));
                }
                if(!ParamUtils.isEmpty(result.getAddress())){
                    locationInfo = result.getAddress();
                }
            }
        };
        // 设置地理编码检索监听者
        geoCoder.setOnGetGeoCodeResultListener(listener);
        //
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
    }

    /**
     * 计算两点之间距离
     *
     * @param start
     * @param end
     * @return 米
     */
    public static double getDistance(LatLng start, LatLng end) {
        double lat1 = (Math.PI / 180) * start.latitude;
        double lat2 = (Math.PI / 180) * end.latitude;

        double lon1 = (Math.PI / 180) * start.longitude;
        double lon2 = (Math.PI / 180) * end.longitude;

        //地球半径
        double R = 6371;

        //两点间距离 km，如果想要米的话，结果*1000就可以了
        return Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;
    }

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            mLocClient.stop();
        }
    };

}
