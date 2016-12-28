package com.lemon.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.lemon.LemonContext;
import com.lemon.LemonMessage;
import com.zfb.house.R;
import com.zfb.house.component.CircleImageView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Glide的管理类
 * Created by admin on 2016/1/12.
 * qinghui@pba.cn
 */
public class GlideUtil {

    private static GlideUtil mGlideInstance;
    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FOREWARD_SLASH = "/";

    private GlideUtil() {
    }

    public static GlideUtil getInstance() {

        if (mGlideInstance == null) {
            mGlideInstance = new GlideUtil();
        }
        return mGlideInstance;
    }

    /**
     * 图片链接加载
     *
     * @param context
     * @param imageUrl
     * @param imageView
     */
    public void loadUrl(Context context, String imageUrl, ImageView imageView) {
        loadUrl(context, imageUrl, imageView, R.drawable.default_avatar, R.drawable.default_avatar);
    }

    /**
     * 委托 与店铺图片链接加载
     *
     * @param context
     * @param imageUrl
     * @param imageView
     */
    public void loadUrlEntrust(Context context, String imageUrl, ImageView imageView) {
        loadUrl(context, imageUrl, imageView, R.drawable.defalut_shop_list, R.drawable.defalut_shop_list);
    }

    /**
     * 房友圈图片加载
     *
     * @param context
     * @param imageUrl
     * @param imageView
     */
    public void loadMomentsUrl(Context context, String imageUrl, ImageView imageView) {
        loadUrl(context, imageUrl, imageView, R.drawable.default_moments, R.drawable.default_moments);
    }

    /**
     * 长条占位图
     *
     * @param context
     * @param imageUrl
     * @param imageView
     */
//    public void loadUrlLonDefineg(Context context, String imageUrl, ImageView imageView) {
//        loadUrl(context, imageUrl, imageView, R.drawable.icon_mushu, R.color.corner);
//    }
    public void loadUrl(Context context, String imageUrl, ImageView imageView, int placeResourceId, int errorResourceId) {
        if (!isDestroy(context)) {
            Glide.with(context).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .crossFade().placeholder(placeResourceId).error(errorResourceId).into(imageView);
        }
    }

    /**
     * 图片链接加载,按缩略比显示
     *
     * @param context
     * @param imageUrl
     * @param imageView
     */
//    public void loadUrlThum(Context context, String imageUrl, ImageView imageView, float sizeMultiplier) {
//        if (!isDestroy(context)) {
//            Glide.with(context).load(imageUrl).crossFade().thumbnail(sizeMultiplier).diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .fitCenter().placeholder(R.color.app_bg).error(R.color.corner).into(imageView);
//        }
//    }

    /**
     * 图片链接加载 缓存为全比例缓存
     *
     * @param context
     * @param imageUrl
     * @param imageView
     */
//    public void loadUrlDiskCacheAll(Context context, String imageUrl, ImageView imageView) {
//        if (!isDestroy(context)) {
//            Glide.with(context).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade()
//                    .fitCenter().placeholder(R.color.app_bg).error(R.color.corner).into(imageView);
//        }
//    }

    /**
     * uri加载
     *
     * @param context
     * @param resourceId
     * @param imageView
     */
//    public void loadUri(Context context, int resourceId, ImageView imageView) {
//        if (!isDestroy(context)) {
//            Glide.with(context).load(resourceIdToUri(context, resourceId)).diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .crossFade().placeholder(R.color.app_bg).error(R.color.corner).into(imageView);
//        }
//    }

    /**
     * 资源id加载
     *
     * @param context
     * @param resourceId
     * @param imageView
     */
    public void loadRes(Context context, int resourceId, ImageView imageView) {
        if (!isDestroy(context)) {
            Glide.with(context).load(resourceId).crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).into(imageView);
        }
    }
//
//    public void loadResByPlace(Context context, int resourceId, ImageView imageView, int colorId) {
//        if (!isDestroy(context)) {
//            Glide.with(context).load(resourceId).crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .fitCenter().placeholder(colorId).error(R.color.corner).into(imageView);
//        }
//    }

    /**
     * 资源id加载圆形图
     *
     * @param context
     * @param resourceId
     * @param imageView
     */
//    public void loadRoundRes(final Context context, int resourceId, final ImageView imageView) {
//        if (!isDestroy(context)) {
//            Glide.with(context).load(resourceId).asBitmap().fitCenter().diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar)
//                    .into(new BitmapImageViewTarget(imageView) {
//                        @Override
//                        protected void setResource(Bitmap resource) {
//                            RoundedBitmapDrawable circularBitmapDrawable =
//                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
//                            circularBitmapDrawable.setCircular(true);
//                            imageView.setImageDrawable(circularBitmapDrawable);
//                        }
//                    });
//        }
//    }

    /**
     * 从网络加载圆角图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadCircleImage(final Context context, String url, final CircleImageView imageView) {
        Glide.with(context).load(url)
                .asBitmap()
                .placeholder(R.drawable.default_avatar)
                .error(R.drawable.default_avatar)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });

    }


    /**
     * 本地图片加载
     *
     * @param context
     * @param filePath
     * @param imageView
     * @param sizeMultiplier 缩略比
     */
    public void loadFile(Context context, String filePath, ImageView imageView, float sizeMultiplier) {
        if (!isDestroy(context)) {
            if (filePath.startsWith("file://")) {
                filePath = filePath.replace("file://", "");
            }
            Glide.with(context).load(new File(filePath)).thumbnail(sizeMultiplier).crossFade()
                    .fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.bg_btn_sms).error(R.color.black).into(imageView);
        }
    }

    /**
     * 本地圆形图
     *
     * @param context
     * @param filePath
     * @param imageView
     * @param sizeMultiplier
     */
//    public void loadFileRound(final Context context, String filePath, final ImageView imageView, float sizeMultiplier) {
//        if (!isDestroy(context)) {
//            Glide.with(context).load(new File(filePath)).asBitmap().fitCenter().thumbnail(sizeMultiplier).diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .placeholder(R.color.app_bg).error(R.color.corner)
//                    .into(new BitmapImageViewTarget(imageView) {
//                        @Override
//                        protected void setResource(Bitmap resource) {
//                            RoundedBitmapDrawable circularBitmapDrawable =
//                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
//                            circularBitmapDrawable.setCircular(true);
//                            imageView.setImageDrawable(circularBitmapDrawable);
//                        }
//                    });
//        }
//    }

    /**
     * 不带动画
     *
     * @param context
     * @param imageUrl
     * @param imageView
     */
//    public void loadUrlNoAnimation(Context context, String imageUrl, ImageView imageView) {
//        if (!isDestroy(context)) {
//            Glide.with(context).load(imageUrl).dontAnimate().diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .fitCenter().placeholder(R.color.app_bg).error(R.color.corner).into(imageView);
//        }
//    }

    /**
     * 把图片资源转成Uri
     *
     * @param context
     * @param resourceId
     * @return
     */
    public Uri resourceIdToUri(Context context, int resourceId) {
        return Uri.parse(ANDROID_RESOURCE + context.getPackageName() + FOREWARD_SLASH + resourceId);
    }

    private boolean isDestroy(Context context) {
        if (context == null) {
            return true;
        }
        if (context instanceof FragmentActivity) {
            if (((FragmentActivity) context).isFinishing()) {
                return true;
            }
        } else if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 使用V4包自带的圆形图片截取方式加载圆形图片
     *
     * @param context
     * @param imageUrl
     * @param imageView
     */
//    public void loadRoundBitmap(final Context context, String imageUrl, final ImageView imageView) {
//        if (!isDestroy(context)) {
//            Glide.with(context).load(imageUrl).asBitmap().fitCenter().diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .placeholder(R.drawable.no_face_circle).error(R.drawable.no_face_circle)
//                    .into(new BitmapImageViewTarget(imageView) {
//                        @Override
//                        protected void setResource(Bitmap resource) {
//                            RoundedBitmapDrawable circularBitmapDrawable =
//                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
//                            circularBitmapDrawable.setCircular(true);
//                            imageView.setImageDrawable(circularBitmapDrawable);
//                        }
//                    });
//        }
//    }


    /**
     * 保存图片
     *
     * @param context
     * @param url
     */
    public void saveImage(final Context context, String url) {
        if (!isDestroy(context)) {
            Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .toBytes(Bitmap.CompressFormat.JPEG, 80)
                    .into(new SimpleTarget<byte[]>() {
                        @Override
                        public void onResourceReady(final byte[] resource, GlideAnimation<? super byte[]> glideAnimation) {
                            new AsyncTask<Void, Void, String>() {
                                @Override
                                protected String doInBackground(Void... params) {
                                    String result;
                                    File sdcard = Environment.getExternalStorageDirectory();
                                    File file = new File(sdcard + "/DCIM/" + System.currentTimeMillis() + ".jpg");
                                    File dir = file.getParentFile();
                                    result = file.getAbsolutePath();
                                    try {
                                        if (!dir.mkdirs() && (!dir.exists() || !dir.isDirectory())) {
                                            throw new IOException("Cannot ensure parent directory for file " + file);
                                        }
                                        BufferedOutputStream s = new BufferedOutputStream(new FileOutputStream(file));
                                        s.write(resource);
                                        s.flush();
                                        s.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    return result;
                                }

                                @Override
                                protected void onPostExecute(String result) {
                                    super.onPostExecute(result);
                                    if (TextUtils.isEmpty(result)) {
                                        LemonContext.getBean(LemonMessage.class).sendMessage("图片保存失败，请稍后重试!");
                                    } else {
                                        LemonContext.getBean(LemonMessage.class).sendMessage("图片已保存到" + result);
                                    }
                                }
                            }.execute();
                        }
                    })
            ;
        }
    }
}
