package com.zfb.house.iml;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Snekey on 2016/5/11.
 */
public interface OnImageChoiceListener{

    void onAddImageView(Uri uri);

    void onAddImageView(String path);

    /**
     *
     * @param list
     */
    void onAddImageViewList(ArrayList<String> list);

    /**
     * 获取已选择的图片uri集合
     * @return
     */
    Set<Uri> getSelectedImages();

}
