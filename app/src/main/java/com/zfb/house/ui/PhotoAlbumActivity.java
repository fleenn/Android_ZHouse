package com.zfb.house.ui;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.zfb.house.R;
import com.zfb.house.adapter.AlbumGridAdapter;
import com.zfb.house.component.PhotoClassify;
import com.zfb.house.model.bean.PhotoModel;

import java.util.ArrayList;

/**
 * Created by Snekey on 2016/5/11.
 */
@Layout(id = R.layout.activity_photo_album)
public class PhotoAlbumActivity extends LemonActivity {

    private static final int SELECT_IMAGE_FROM_SDK = 1;

    @FieldView(id = R.id.gv_album)
    private GridView gvAlbum;
    private ArrayList<PhotoModel> imageUrls;
    private AlbumGridAdapter adapter;

    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        finish();
    }

    @Override
    protected void initView() {
        setCenterText(R.string.label_select_album);
        PhotoClassify pc = new PhotoClassify(getContentResolver());
        if (pc.getImageFromPhone())
            imageUrls = pc.getPhotoModels();
        else
            imageUrls = new ArrayList<PhotoModel>();
        adapter = new AlbumGridAdapter(mContext, imageUrls);
        gvAlbum.setAdapter(adapter);
        gvAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(mContext, PhotoActivity.class);
                intent.putStringArrayListExtra(SendMomentsActivity.EXTRA_IMAGE_URLS, imageUrls.get(arg2).getList());
                intent.putStringArrayListExtra(SendMomentsActivity.EXIST_IMAGE_URLS, getIntent().getStringArrayListExtra(SendMomentsActivity.EXIST_IMAGE_URLS));
                startActivityForResult(intent, SELECT_IMAGE_FROM_SDK);
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_IMAGE_FROM_SDK && resultCode == RESULT_OK) {
            Intent intent = new Intent();
            intent.setClass(PhotoAlbumActivity.this, SendMomentsActivity.class);
            intent.putStringArrayListExtra(SendMomentsActivity.EXTRA_IMAGE_URLS, data.getStringArrayListExtra(SendMomentsActivity.EXTRA_IMAGE_URLS));
            setResult(RESULT_OK, intent);
            finish();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
}
