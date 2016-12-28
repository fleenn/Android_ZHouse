package com.zfb.house.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.math.BigDecimal;

/**
 * 数据清除管理器类
 */
public class DataCleanUtil {

    /**
     * 获得所有的缓存大小
     *
     * @param context 上下文环境
     * @return
     * @throws Exception
     */
    public static String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = getFolderSize(context.getCacheDir());//获取缓存文件的大小
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//如果有外部储存卡的话，
            cacheSize += getFolderSize(context.getExternalCacheDir());//就加上外部储存卡的大小
        }
        return getFormatSize(cacheSize);//最后记得格式化单位
    }

    /**
     * 删除所有缓存内容
     *
     * @param context 上下文环境
     */
    public static void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());//删除缓存文件
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//如果有外部储存卡的话，
            deleteDir(context.getExternalCacheDir());//也删除外部存储卡的缓存文件
        }
    }

    /**
     * 删除文件
     *
     * @param dir 文件目录
     * @return
     */
    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {//如果文件不为空并且文件下面还有子文件
            String[] children = dir.list();//保存文件目录列表到一个字符串数组
            for (int i = 0; i < children.length; i++) {//遍历文件目录列表中的内容
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();//最后删除文件
    }

    /**
     * 获取文件大小
     * Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
     * Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
     *
     * @param file 文件
     * @return
     * @throws Exception
     */
    public static long getFolderSize(File file) throws Exception {
        long size = 0;//文件大小
        try {
            File[] fileList = file.listFiles();//文件列表
            for (int i = 0; i < fileList.length; i++) {//遍历文件列表
                if (fileList[i].isDirectory()) {  // 如果下面还有文件，
                    size = size + getFolderSize(fileList[i]);//就再加上下面文件的大小
                } else {//没有的话，
                    size = size + fileList[i].length();//加上文件的大小
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     *
     * @param size 文件大小
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
//            return size + "Byte";
            return size + "M";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

}
