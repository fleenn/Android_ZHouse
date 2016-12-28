package com.lemon.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.lemon.util.sdCard.SDCardUtils;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 使用七牛云图片存储接口
 * Created by geolo on 2015/10/15.
 */
public class ImageUploadUtil {

    public interface OnUploadFileListener {
        void progress(File srcFile, double percent);

        void complete(File srcFile, String httpUrl);

        void error(File srcFile);
    }

    private static final String TAG = "ImageUploadUtil.java";
//    private static final String DOMAIN = "http://7xid0d.com1.z0.glb.clouddn.com/";
    private static final String DOMAIN = "http://od46im3be.bkt.clouddn.com/";

    private ImageUploadUtil() {
    }

    public static void OnUploadMultiFile(String uptoken, final List<File> files, final OnUploadFileListener onUploadListener, Context context) {
        if (TextUtils.isEmpty(uptoken)) {
            Log.e(TAG, "OnUploadMultiFile --> uptoken == null");
        }
        if (files == null || files.isEmpty()) {
            Log.e(TAG, "OnUploadMultiFile --> files == null");
        }
        if (onUploadListener == null) {
            Log.e(TAG, "OnUploadMultiFile --> onUploadListener == null");
        }
        for (File file : files) {
            if (file != null || !file.exists()) {
                OnUploadFile(uptoken, file, onUploadListener, context);
            } else {
                Log.e(TAG, "OnUploadMultiFile --> file == null");
            }
        }
    }

    public static void OnUploadFile(String uptoken, File file, final OnUploadFileListener onUploadListener, Context context) {
        if (TextUtils.isEmpty(uptoken)) {
            Log.e(TAG, "OnUploadFile --> uptoken == null");
            return;
        }
        if (file == null) {
            Log.e(TAG, "OnUploadFile --> files == null");
            return;
        }
        if (onUploadListener == null) {
            Log.e(TAG, "OnUploadFile --> onUploadListener == null");
            return;
        }


        final String tempPath = SDCardUtils.getWritePath(context.getApplicationContext(), "ZFB_upload_image.png");
        File tempFile = new File(tempPath);
        Log.v(TAG, ">>>>>>>>>> sendImageStream 开始压缩图片 <<<<<<<<<");
        boolean isScaleOK = MultiMediaUtil.scaleImageWithFilter(file,
                tempFile, 300, true, true, true, true);
        Log.v(TAG, ">>>>>>>>>> sendImageStream 结束压缩图片 <<<<<<<<<");
        boolean isOk;
        if (TextUtils.isEmpty(tempPath) || !isScaleOK) {
            Log.e(TAG, "--- 图片压缩失败,使用原图上传 --- ");
            isOk = false;
        } else {
            Log.e(TAG, "--- 图片压缩成功,使用压缩图上传 ---");
            isOk = true;
        }
        final File updateFile = isOk ? tempFile : file;
        final CountDownLatch signal = new CountDownLatch(1);
        UploadManager uploadManager = new UploadManager();
        String uuid = UUID.randomUUID().toString();
        final String expectKey = uuid + "geolo";
        Map<String, String> params = new HashMap<String, String>();
        params.put("x:foo", "fooval");
        final UploadOptions opt = new UploadOptions(params, null, true, new UpProgressHandler() {
            @Override
            public void progress(String key, double percent) {
                //publishProgress(5 + (int) (percent * 85));// 更新上传进度
                Log.v(TAG, "UploadOptions -- progress -- key:" + key + " ,percent:" + percent);
                if (onUploadListener != null) {
                    onUploadListener.progress(updateFile, percent);
                }
            }
        }, null);
        uploadManager.put(updateFile, expectKey, uptoken, new UpCompletionHandler() {
            public void complete(String key, ResponseInfo rinfo, JSONObject response) {
                File tempFile = new File(tempPath);
                if (tempFile != null && tempFile.exists()) {
                    tempFile.delete();
                }
                if (rinfo.isOK()) {  // 这里的key就是七牛返回的上传成功的文件名,其实与上面的imageFileName是一样的
                    Log.v(TAG, "uploadManager -- complete , key:" + key + " \n info:" + rinfo + "\n");
                    if (onUploadListener != null) {
                        onUploadListener.complete(updateFile, DOMAIN + key);
                    }
                } else {
                    Log.e(TAG, "uploadManager -- complete , error info:" + rinfo);
                    if (onUploadListener != null) {
                        onUploadListener.error(updateFile);
                    }
                }
                signal.countDown();
            }
        }, opt);

        try {
            signal.await(130, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            Log.e(TAG, "OnUpload", e);
        }
    }


    // 调用七牛云存储接口,删除相关图片
    public static void deleteAllImageInQiNiu(List<String> imageFileNameList) {
//        Config.ACCESS_KEY = "你的ak";
//        Config.SECRET_KEY = "你的sk";
//        Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
//        RSClient rs = new RSClient(mac);
//        List<EntryPath> entries = new ArrayList<EntryPath>();
//        for (String fileName : imageFileNameList) {
//            EntryPath entrypath = new EntryPath();
//            entrypath.bucket = "你的空间名称";
//            entrypath.key = fileName;
//            entries.add(entrypath);
//        }
//
//        if (entries.size() > 0) {
//            rs.batchDelete(entries);
//        }
//    }
    }
}
