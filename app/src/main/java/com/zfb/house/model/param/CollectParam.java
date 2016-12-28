package com.zfb.house.model.param;

import android.view.View;

import com.lemon.annotation.Module;
import com.lemon.model.BaseParam;

/**
 * Created by Snekey on 2016/6/20.
 */
@Module(server = "zfb_server", name = "houseElite/v2")
public class CollectParam extends BaseParam {
    private String eliteId;
    private String token;
    private int position;
    private View view;
    private String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public String getEliteId() {
        return eliteId;
    }

    public void setEliteId(String eliteId) {
        this.eliteId = eliteId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
