package com.zfb.house.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lemon.util.LogUtils;

/**
 * Class Function Instructions: 加载图片
 *
 * @Version 1.0.0
 * @Author Silence
 */
public class ImageUtil {
    static String TAG = "ImageUtil_Volly";

    public void loadImageByVolley(ImageView mImageView, String imageUrl, Context context, int defaultDrawableId, int failureDrawableId) {
        LogUtils.i(TAG, "imageUrl ==" + imageUrl);
        if(TextUtils.isEmpty(imageUrl)){
            LogUtils.e(TAG , "imageUrl is null");
            if(mImageView != null){
                mImageView.setImageResource(defaultDrawableId);
            }
            return ;
        }
        Glide.with(context).load(imageUrl)
                //.centerCrop()
//                .placeholder(defaultDrawableId)
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(failureDrawableId)
                .into(mImageView);
//        Glide.get(context).trimMemory(ComponentCallbacks2.TRIM_MEMORY_COMPLETE);
    }

    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath( final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
