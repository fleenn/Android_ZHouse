package com.zfb.house.model.bean;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * Created by linwenbing on 16/6/16.
 */
public class ReleasePhoto implements Serializable{
    private List<File> photoFiles;

    public List<File> getPhotoFiles() {
        return photoFiles;
    }

    public void setPhotoFiles(List<File> photoFiles) {
        this.photoFiles = photoFiles;
    }
}
