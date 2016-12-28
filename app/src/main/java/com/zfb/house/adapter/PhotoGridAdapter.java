package com.zfb.house.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lemon.util.ParamUtils;
import com.zfb.house.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Snekey on 2016/5/11.
 */
public class PhotoGridAdapter extends BaseAdapter{

    private ArrayList<String> mImageUrls;
    private ArrayList<String> mExistList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private static final String PATH = "file://";
    // 用来控制CheckBox的选中状况
    private HashMap<Integer, Boolean> isSelected;
    private List<Integer> mSelectedList;//所有插入的集合

    public PhotoGridAdapter(Context context, ArrayList<String> imageUrls, ArrayList<String> existList) {
        mContext = context;
        mImageUrls = imageUrls;
        mExistList = existList;
        mLayoutInflater = LayoutInflater.from(context);
        initDate();
    }

    @Override
    public int getCount() {
        return ParamUtils.isEmpty(mImageUrls)?0:mImageUrls.size();
    }

    @Override
    public String getItem(int position) {
        return mImageUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final int index = position;
        final String url = getItem(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_grid_photo_layout, parent, false);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.img_photo);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.cb_select);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.checkBox.setChecked(getIsSelected().get(position));
        Glide.with(parent.getContext()).load("file://" + url).into(viewHolder.imageView);
        return convertView;
    }

    private void initDate() {
        isSelected = new HashMap<Integer, Boolean>();
        mSelectedList = new ArrayList<>();
        for (int i = 0; i < mImageUrls.size(); i++) {
            //判断是否已经勾选过
            boolean flag = false;
            for (String url : mExistList){
                if (mImageUrls.get(i).equals(url)){
                    flag = true;
                    break;
                }
            }
            getIsSelected().put(i, flag ? true : false);
            if(flag){
                mSelectedList.add(i);
            }
        }
    }

    public HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public int getSelectedCount() {
        int count = 0;
        for (Map.Entry<Integer, Boolean> myEntry : getIsSelected()
                .entrySet()) {
            if (myEntry.getValue()) {
                count++;
            }
        }
        return count;
    }

    public List<Integer> getmSelectedList() {
        return mSelectedList;
    }

    public void setmSelectedList(List<Integer> mSelectedList) {
        this.mSelectedList = mSelectedList;
    }

    public class ViewHolder {
        ImageView imageView;
        CheckBox checkBox;

        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }

        public void setCheckBox(CheckBox checkBox) {
            this.checkBox = checkBox;
        }
    }

}
