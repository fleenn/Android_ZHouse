package com.lemon.util.sdCard;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.lemon.LemonContext;
import com.lemon.LemonMessage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 手机内置存储卡以及外置存储卡管理工具类 
 */
public class StorageUtil {

	private final static LemonMessage lemonMessage = LemonContext.getBean(LemonMessage.class);

    /**
     * 判断是否有SDCARD,外置存储卡及内置存储卡
     * FIXME 由多存储卡类提供空间存在的判断
     * @return
     */
    public static boolean isSDcardExist(Context context) {
	return MultiCard.getInstance(context).isSDCardExist();
    }


    /**
     * 判断是否有外置存储卡
     * @return
     */
    public static boolean isExternalSDCardExist(Context context) {
	return MultiCard.getInstance(context).isExternalSDCardExist();
    }

    /**
     * 根据文件类型检查外置及内置存储卡是否有空间可写
     * @param fileType 文件类型
     * @param type 检查范围,0表示所有,1表示只检查外置
     * @return
     */
    public static boolean isLimitSDCardSpaceForWrite(Context context, int fileType, int type) {
	return MultiCard.getInstance(context).checkSDCardSpace(fileType, type);
    }

    /**
     * 根据文件类型检查外置及内置存储卡是否超过预警空间
     * @param fileType 文件类型
     * @param type 检查范围,0表示所有,1表示只检查外置
     * @return
     */
    public static boolean isLimitSDCardSpaceForWriteWarning(Context context, int fileType, int type) {
	return MultiCard.getInstance(context).islimitSpaceWarning(fileType, type);
    }

    /**
     * 判断存储卡空间(外置、内置存储卡是否有空间可写)
     * @param context
     * @param fileType	文件类型
     * @param type		检查范围,0表示所有,1表示外置
     * @param bNeedTip	是否要做出提示语
     * @return true表示无存储卡或无空间可写,false表示ok
     */
    public static boolean isSDCardSapceForWriteWithTip(Context context, int fileType, int type, boolean bNeedTip) {
	if(type == 0 && !isSDcardExist(context)) {
	    if(bNeedTip) {
	    }
	    return true;
	} else if(type == 1 && !isExternalSDCardExist(context)) {
	    if(bNeedTip) {
	    }
	    return true;
	}

	if(!isLimitSDCardSpaceForWrite(context,fileType, type)) {
	    if(bNeedTip) {
	    }
	    return true;
	}

	if(!isLimitSDCardSpaceForWriteWarning(context,fileType, type)) {
	    if(bNeedTip) {
	    }
	}

	return false;
    }

    /**
     * 获取文件写入路径，无视错误
     * @param fileName 文件全名
     * @return 返回路径，返回null则拒绝写入操作
     */
    public static String getWritePathIgnoreError(Context context , String fileName) {
	return getWritePath(context, fileName, false, null, null);
    }

    /**
     * 获取文件写入路径
     * @param fileName 文件全名
     * @return 返回路径，返回null则拒绝写入操作
     */
    public static String getWritePath(Context context ,String fileName) {
	return getWritePath(context, fileName, true, null, null);
    }

    /**
     * 获取文件写入路径
     * @param fileName 文件全名
     * @param tip 是否带提示语
     * @param warnningTip 警告提示语
     * @param unwriteTip 拒绝写入提示语
     * @return 返回路径，返回null则拒绝写入操作
     */
    public static String getWritePath(Context context ,String fileName, boolean tip, String warnningTip, String unwriteTip) {
	try {
	    MultiCardFilePath path = MultiCard.getInstance(context).getWritePath(fileName);
	    if(path.getCode() == MultiCardFilePath.RET_LIMIT_SPACE_WARNNING) {
		if(tip) {
		    if(TextUtils.isEmpty(warnningTip)) {
		    } else {
		    }
		}
	    }
	    return path.getFilePath();
	} catch (LimitSpaceUnwriteException e) {
	    e.printStackTrace();
	    if(tip) {
		if(TextUtils.isEmpty(unwriteTip)) {
		} else {
		}
	    }
	} catch (IllegalArgumentException e) {
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * @param is
     * @param filePath
     * @return 保存失败，返回-1
     */
    public static long save(InputStream is, String filePath) {
	File f = new File(filePath);
	if (!f.getParentFile().exists()) {// 如果不存在上级文件夹
	    f.getParentFile().mkdirs();
	}
	try {
	    f.createNewFile();
	    FileOutputStream out = new FileOutputStream(f);
	    int read = 0;
	    byte[] bytes = new byte[1024];
	    while ((read = is.read(bytes)) != -1) {
		out.write(bytes, 0, read);
	    }
	    is.close();
	    out.flush();
	    out.close();
	    return f.length();
	} catch (IOException e) {
	    if(f!=null && f.exists()){
		f.delete();
	    }
	    return -1;
	}
    }

    /**
     * 保存图片
     * @param bm
     * @param filePath
     * @return 保存失败，返回-1
     */
    public static long save(Bitmap bm, String filePath) {
	InputStream is = Bitmap2IS(bm);
	return save(is , filePath);
    }

    /**将Bitmap对象转化成InputStream 对象*/
    private static InputStream Bitmap2IS(Bitmap bm){
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
	InputStream sbs = new ByteArrayInputStream(baos.toByteArray());
	return sbs;
    }
}
