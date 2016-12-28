package com.zfb.house.ui;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.lemon.LemonActivity;
import com.lemon.LemonContext;
import com.lemon.LemonMessage;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.zfb.house.R;
import com.zfb.house.adapter.PhotoGridAdapter;

import java.util.ArrayList;

/**
 * Created by Snekey on 2016/5/11.
 */
@Layout(id = R.layout.activity_photo)
public class PhotoActivity extends LemonActivity {

    @FieldView(id = R.id.gv_photo)
    private GridView gvPhoto;
    private ArrayList<String> listUri;
    private PhotoGridAdapter mAdapter;
    private ArrayList<String> mExistList;
    private static final int maxImagsNum = 6;

    LemonMessage lemonMessage;

    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBack() {
        finish();
    }

    @Override
    protected void initView() {
        setCenterText(R.string.label_select_photo);
        setRtText(R.string.complete);
        setRtTextColor(R.color.my_orange_two);
        lemonMessage = LemonContext.getBean(LemonMessage.class);
        listUri = getIntent().getStringArrayListExtra(SendMomentsActivity.EXTRA_IMAGE_URLS);
        mExistList = getIntent().getStringArrayListExtra(SendMomentsActivity.EXIST_IMAGE_URLS);
        mAdapter = new PhotoGridAdapter(mContext, listUri, mExistList);
        mExistList.removeAll(listUri);
        gvPhoto.setAdapter(mAdapter);
        gvPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                PhotoGridAdapter.ViewHolder holder = (PhotoGridAdapter.ViewHolder) arg1.getTag();
                if (!holder.getCheckBox().isChecked())

                    if (mAdapter.getSelectedCount() + (mExistList == null ? 0 : mExistList.size()) >= maxImagsNum) {
                        lemonMessage.sendMessage(mExistList == null ? "上传数量最多为9张" : "本次最多可选择" + (maxImagsNum - mExistList.size()) + "张,一共" + maxImagsNum + "张");
                        return;
                    }
                // 改变CheckBox的状态
                holder.getCheckBox().toggle();
                // 将CheckBox的选中状况记录下来
                mAdapter.getIsSelected().put(position,
                        holder.getCheckBox().isChecked());
                if (holder.getCheckBox().isChecked()) {
                    if (!mAdapter.getmSelectedList().contains(new Integer(position))) {
                        mAdapter.getmSelectedList().add(new Integer(position));
                    }
                } else {
                    if (mAdapter.getmSelectedList().contains(new Integer(position))) {
                        mAdapter.getmSelectedList().remove(new Integer(position));
                    }
                }
            }

        });
    }

    @OnClick(id = R.id.tv_title_rt)
    public void toFinish(){
        ArrayList<String> imgSelected = new ArrayList<String>();
                    /*for (Map.Entry<Integer, Boolean> myEntry : mAdapter
                            .getIsSelected().entrySet()) {
                        if (myEntry.getValue()) {
                            String url = listUri.get(myEntry.getKey());
                            imgSelected.add(url);
                        }
                    }*/
        for (Integer integer : mAdapter.getmSelectedList()) {
            String url = listUri.get(integer);
            imgSelected.add(url);
        }
        for (String url : mExistList) {
            imgSelected.add(url);
        }
        Intent intent = new Intent();
        intent.setClass(mContext, PhotoAlbumActivity.class);
        intent.putStringArrayListExtra(SendMomentsActivity.EXTRA_IMAGE_URLS, imgSelected);
        setResult(RESULT_OK, intent);
        finish();
    }
}
