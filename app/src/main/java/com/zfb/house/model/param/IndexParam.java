package com.zfb.house.model.param;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * Created by Snekey on 2016/4/28.
 */
@Module(server = "zfb_server", name = "home/v2", httpMethod = "get")
public class IndexParam extends BaseParam {
    private double lon;
    private double lat;
    private long range;
    private String token;

    public long getRange() {
        return range;
    }

    public void setRange(long range) {
        this.range = range;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
