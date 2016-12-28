package com.zfb.house.model.bean;

import java.util.ArrayList;

/**
 * Created by Snekey on 2016/5/11.
 */
public class PhotoModel {

    private String rootPath;
    private ArrayList<String> list;

    public PhotoModel(String rootPath, ArrayList<String> list) {

        this.rootPath = rootPath;
        this.list = list;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }
}
