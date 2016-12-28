package com.zfb.house.component;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lemon.LemonContext;
import com.lemon.LemonMessage;
import com.lemon.util.sdCard.SDCardUtils;
import com.zfb.house.R;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Snekey on 2016/5/10.
 */
public class PickPhotoLayout extends LinearLayout {
    private static final String TAG = "DynImageLayout.java";
    private OnAddImageListener mOnAddImageListener;//点击添加图片的通知
    private ImageView addImageView;//默认的，添加图片的IV
    private LayoutParams params;//动态加载的布局参数
    private Set<Uri> mImageSet = new LinkedHashSet<Uri>();//图片集合
    private GalleryDialog mGalleryDialog;//图片预览对话框
    private TextView promptTV;//如果动态添加的图片集合为零，那么就在add图片按钮后面添加“点击添加图片”字样
    private int side = 0;
    private LemonMessage lemonMessage;

    private static final int maxImagesNum = 6;

    public interface OnAddImageListener {
        void OnRequestAdd();
    }

    public PickPhotoLayout(Context context) {
        super(context);
        initView();
    }

    public PickPhotoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @TargetApi(android.os.Build.VERSION_CODES.HONEYCOMB)
    public PickPhotoLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public void onDestroyView() {
        if (mGalleryDialog != null) {
            mGalleryDialog.cancel();
            mGalleryDialog = null;
        }
    }

    /**
     * 视图第一次初始化
     */
    private void initView() {
        lemonMessage = LemonContext.getBean(LemonMessage.class);
        side = (int) getResources().getDimension(R.dimen.friends_pick_photo_size);//ScreenUtil.screenWidth / 5 - 10;
        params = new LayoutParams(side, side);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.friends_pick_photo_rt_margin);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.friends_left_margin);
        params.topMargin = getResources().getDimensionPixelSize(R.dimen.friends_pick_photo_top_margin);
        mGalleryDialog = new GalleryDialog(getContext());
        initAddImage();
    }

    /**
     * 生成添加图片按钮的布局
     */
    private void initAddImage() {
        LinearLayout layout = createHorizontalLayout();
        layout.addView(getAddImage(), params);
        if (mImageSet.isEmpty()) {
            //layout.addView(getPromptTV());
        }
        this.addView(layout);

    }

    /**
     * 图片复位
     */
    public void refreshAllImage() {
        removeAllViews();
        initAddImage();
        for (Uri uri : mImageSet) {
            createNewIV(uri);
        }

        if (mImageSet.size() >= maxImagesNum) {
            addImageView.setVisibility(View.GONE);
        } else {
            addImageView.setVisibility(View.VISIBLE);
        }


    }

    /**
     * 添加图片监听
     */
    public void setOnAddImageListener(OnAddImageListener onAddImageListener) {
        mOnAddImageListener = onAddImageListener;
    }

    private ImageView getAddImage() {
        addImageView = new ImageView(getContext());
//		int paddingValue = ScreenUtil.dip2px(5);
//		int width = getResources().getDimensionPixelSize(R.dimen.discuss_add_width);
//		addImageView.setLayoutParams(new LayoutParams(width, width));
        //addImageView.setPadding(paddingValue, paddingValue, paddingValue, paddingValue);
        addImageView.setOnClickListener(mClickListener);
        addImageView.setImageResource(R.drawable.bg_add_photo);
        // TODO: 2016/5/11 add touch selector 
//        addImageView.setBackgroundResource(R.drawable.common_trans_selector);
        return addImageView;
    }

    private View getPromptTV() {
        promptTV = new TextView(getContext());//如果动态添加的图片集合为零，那么就在add图片按钮后面添加“点击添加图片”字样
        promptTV.setText("点击添加图片");
        promptTV.setTextColor(Color.rgb(188, 188, 188));//#bcbcbc
        promptTV.setTextSize(26);
        promptTV.setPadding(5, 0, 80, 0);
        promptTV.setOnClickListener(mClickListener);
        return promptTV;
    }

    public void repleceImgstrs(Collection<String> newImageLocationPaths) {
        if (mImageSet != null) {
            mImageSet.clear();
            for (String path : newImageLocationPaths) {
                addImageView(path);
            }
            refreshAllImage();
        }
    }

    public void repleceImgs(Collection<Uri> newUri) {
        if (mImageSet != null) {
            mImageSet.clear();
            for (Uri uri : newUri) {
                addImageView(uri);
            }
            refreshAllImage();
        }
    }

    public void addImageView(String imageLocationPath) {
        addImageView(Uri.parse(imageLocationPath));
    }

    public void addImageView(Uri uri) {
        if (mImageSet != null && mImageSet.size() < maxImagesNum) {
            if (mImageSet.add(uri)) {
                createNewIV(uri);
            } else {
                lemonMessage.sendMessage(R.string.toast_is_picked);
            }
            if (mImageSet.size() >= maxImagesNum) {
                addImageView.setVisibility(View.GONE);
            } else {
                addImageView.setVisibility(View.VISIBLE);
            }
        } else {
            addImageView.setVisibility(View.GONE);
            lemonMessage.sendMessage("最多只能放" + maxImagesNum + "张");
        }
    }

    public void createNewIV(Uri uri) {
        try {
            String urlString = uri.toString();
            if (!urlString.startsWith("http://")) {
                urlString = "file://" + urlString;
            }
            ImageView imageView = getImageView();
            imageView.setId((int) System.currentTimeMillis());
            imageView.setTag(imageView.getId(), uri);
            //imageView.setDrawingCacheEnabled(true);
//			new ImageUtil().loadImageByVolley(imageView,
//					urlString, getContext(), R.drawable.empty_photo,
//					R.drawable.empty_photo);
            Glide.with(getContext()).load(urlString).into(imageView);
            //Bitmap bitmap = MultiMediaUtil.decodeFile(getContext() ,uri,-1);
            //imageView.setImageBitmap(bitmap);
            addImageView(imageView);
            //bitmap.recycle();
        } catch (Exception e) {
            Log.e(TAG, ">>>>>>>>>>>>> createNewIV() <<<<<<<<<<<" + e);
        }

    }

    private void addImageView(ImageView imageView) {
        int childCount = this.getChildCount();
        if (childCount > 0) {
            LinearLayout layout = (LinearLayout) getChildAt(childCount - 1);

            if (childCount == 1) {
                layout.removeView(promptTV);//去掉“点击添加图片”的提示
            }
            layout.removeViewAt(layout.getChildCount() - 1);
            layout.addView(imageView, params);
            if (isChildCrowd()) {
                LinearLayout lastLayout = createHorizontalLayout();
                lastLayout.addView(getAddImage(), params);
                this.addView(lastLayout);

            } else {
                layout.addView(getAddImage(), params);
            }
        }
    }

    private ImageView getImageView() {
        ImageView imageView = new ImageView(getContext());
        imageView.setOnClickListener(mClickListener);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    /**
     * 子视图是否拥挤,需要再加一行视图
     */
    private boolean isChildCrowd() {
        int childCount = getChildCount();
        if (childCount == 0) {
            return true;
        } else {
            LinearLayout ll = (LinearLayout) getChildAt(childCount - 1);
            if (ll.getChildCount() % 4 == 0) {
                return true;
            }
        }
        return false;
    }

    private LinearLayout createHorizontalLayout() {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.HORIZONTAL);
        return layout;
    }

    private OnClickListener mClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (!SDCardUtils.checkSDCardAvailable()) {//判断是否存在SD卡
                lemonMessage.sendMessage("不存在SD卡, 无法发送图片!!");
                return;
            }

            Object object = v.getTag(v.getId());
            if (object == null) {
                if (mImageSet != null && mImageSet.size() < maxImagesNum && mOnAddImageListener != null) {
                    mOnAddImageListener.OnRequestAdd();
                }
            } else {
                if (object instanceof Uri) {
                    Uri uri = (Uri) object;
                    if (mGalleryDialog != null) {

                        mGalleryDialog.showGallery(uri, mImageSet, mDeleteListener);
                    }
                } else {
                    lemonMessage.sendMessage("没有找到指定图片!!");
                }
            }
        }
    };

    private GalleryDialog.OnDeleteListener mDeleteListener = new GalleryDialog.OnDeleteListener() {

        @Override
        public void onDeleteImage(Uri uri) {
            mImageSet.remove(uri);
            refreshAllImage();
        }
    };

    /**
     * 获取已选的图片集合
     */
    public Set<Uri> getSelectedImages() {
        return mImageSet;
    }
}

