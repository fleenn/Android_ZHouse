package com.zfb.house.component;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.lemon.LemonActivityManager;
import com.lemon.LemonContext;
import com.lemon.LemonMessage;
import com.lemon.util.ScreenUtil;
import com.zfb.house.R;

import java.util.ArrayList;
import java.util.List;

public class HomeGalleryImagesLayout extends LinearLayout {

    private static final String TAG = "HomeGalleryImagesLayout.java";
    private GalleryDialog mGalleryDialog;//图片预览对话框
    private List<Uri> imageUris = new ArrayList<Uri>();
    private LayoutParams params;//动态加载的布局参数
    private int side = 0;

    public HomeGalleryImagesLayout(Context context) {
        super(context);
        initView();
    }

    public HomeGalleryImagesLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @TargetApi(android.os.Build.VERSION_CODES.HONEYCOMB)
    public HomeGalleryImagesLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    /**
     * 视图第一次初始化
     */
    private void initView() {
        int paddingValue = ScreenUtil.dip2px(LemonContext.getBean(LemonActivityManager.class).getCurrentActivity(),6.6f);
        int marginBoth = ScreenUtil.dip2px(LemonContext.getBean(LemonActivityManager.class).getCurrentActivity(),19.9f);
        side = (ScreenUtil.screenWidth - marginBoth) / 3;
        side -= paddingValue;
        params = new LayoutParams(side, side);
        params.bottomMargin = paddingValue;
        params.rightMargin = paddingValue;
        this.setOrientation(LinearLayout.VERTICAL);
        mGalleryDialog = new GalleryDialog(getContext());
    }

    public void setImageList(List<Uri> tag) {
        removeAllViews();
        imageUris.clear();
        if (tag != null && tag instanceof List) {
            for (Uri uri : (List<Uri>) tag) {
                addImageView(uri);
            }
        }
    }

    private void addImageView(Uri uri) {
        imageUris.add(uri);
        createNewIV(uri);
    }

    private void createNewIV(Uri uri) {
        MyImageView imageView = getImageView();
        imageView.setUri(uri);
        Glide.with(getContext()).load(uri).placeholder(R.drawable.default_moments).into(imageView);
        addImageView(imageView);
    }

    private void addImageView(ImageView imageView) {
        int childCount = this.getChildCount();
        if (childCount == 0) {
            this.addView(createHorizontalLayout());
        }
        LinearLayout layout = (LinearLayout) getChildAt(getChildCount() - 1);
        layout.addView(imageView, params);
        if (isChildCrowd()) {
            this.addView(createHorizontalLayout());
        }
    }

    private MyImageView getImageView() {
        MyImageView imageView = new MyImageView(getContext());
        //imageView.setPadding(paddingValue, paddingValue, paddingValue, paddingValue);
        imageView.setOnClickListener(mClickListener);
        imageView.setScaleType(ScaleType.CENTER_CROP);
        return imageView;
    }

    /**
     * 子视图是否拥挤,需要再加一行视图
     */
    private boolean isChildCrowd() {
        int childCount = this.getChildCount();
        if (childCount == 0) {
            return true;
        } else {
            LinearLayout ll = (LinearLayout) this.getChildAt(childCount - 1);
            if (ll.getChildCount() > 2) {
                return true;
            }
        }
        return false;
    }

    private LinearLayout createHorizontalLayout() {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        return layout;
    }

    private OnClickListener mClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v instanceof MyImageView) {
                MyImageView imageView = (MyImageView) v;
                Uri uri = imageView.getUri();
                if (uri != null) {
                    if (mGalleryDialog != null) {
                        mGalleryDialog.hideRightButton();
                        mGalleryDialog.showSaveButton();
                        mGalleryDialog.showGallery(uri, imageUris, null);
                    }
                } else {
                    LemonContext.getBean(LemonMessage.class).sendMessage("没有找到指定图片!!");
                }
            }
        }
    };

    private class MyImageView extends ImageView {

        private Uri mUri;

        public MyImageView(Context context) {
            super(context);
        }

        public void setUri(Uri uri) {
            mUri = uri;
        }

        public Uri getUri() {
            return mUri;
        }
    }

}
