package com.zfb.house.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zfb.house.R;

/**
 * Created by Snekey on 2016/5/12.
 */
public class PickedPhotoLayout extends LinearLayout{
    
    private ImageView imgPickedPhoto;
    private ImageView imgDeletePhoto;
    public PickedPhotoLayout(Context context) {
        super(context);
        init();
    }

    public PickedPhotoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PickedPhotoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        inflate(getContext(), R.layout.item_grid_photo_picked_layout,this);
        imgPickedPhoto = (ImageView) findViewById(R.id.img_picked_photo);
        imgDeletePhoto = (ImageView) findViewById(R.id.img_delete_photo);
    }

    public ImageView getImgPickedPhoto() {
        return imgPickedPhoto;
    }

    public void setImgPickedPhoto(ImageView imgPickedPhoto) {
        this.imgPickedPhoto = imgPickedPhoto;
    }

    public ImageView getImgDeletePhoto() {
        return imgDeletePhoto;
    }

    public void setImgDeletePhoto(ImageView imgDeletePhoto) {
        this.imgDeletePhoto = imgDeletePhoto;
    }
}
