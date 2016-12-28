/**
 * Copyright 2014 Zhenguo Jin (jinzhenguo1990@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lemon.util.sdCard;

import android.content.Context;
import android.text.TextUtils;

import com.lemon.LemonContext;
import com.lemon.LemonMessage;
import com.zfb.house.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * SDcard操作工具类
 *
 * @author jingle1267@163.com
 */
public final class SDCardUtils {

    /**
     * Don't let anyone instantiate this class.
     */
    private SDCardUtils() {
        throw new Error("Do not need instantiate!");
    }

    /**
     * Check the SD card
     *
     * @return 是否存在SDCard
     */
    public static boolean checkSDCardAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * Check if the file is exists
     *
     * @param filePath 文件路径
     * @param fileName 文件名
     * @return 是否存在文件
     */
    public static boolean isFileExistsInSDCard(String filePath, String fileName) {
        boolean flag = false;
        if (checkSDCardAvailable()) {
            File file = new File(filePath, fileName);
            if (file.exists()) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * Write file to SD card
     *
     * @param filePath 文件路径
     * @param filename 文件名
     * @param content  内容
     * @return 是否保存成功
     * @throws Exception
     */
    public static boolean saveFileToSDCard(String filePath, String filename,
                                           String content) throws Exception {
        boolean flag = false;
        if (checkSDCardAvailable()) {
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(filePath, filename);
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(content.getBytes());
            outStream.close();
            flag = true;
        }
        return flag;
    }

    /**
     * Read file as stream from SD card
     *
     * @param fileName String PATH =
     *                 Environment.getExternalStorageDirectory().getAbsolutePath() +
     *                 "/dirName";
     * @return Byte数组
     */
    public static byte[] readFileFromSDCard(String filePath, String fileName) {
        byte[] buffer = null;
        try {
            if (checkSDCardAvailable()) {
                String filePaht = filePath + "/" + fileName;
                FileInputStream fin = new FileInputStream(filePaht);
                int length = fin.available();
                buffer = new byte[length];
                fin.read(buffer);
                fin.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * Delete file
     *
     * @param filePath 文件路径
     * @param fileName filePath =
     *                 android.os.Environment.getExternalStorageDirectory().getPath()
     * @return 是否删除成功
     */
    public static boolean deleteSDFile(String filePath, String fileName) {
        File file = new File(filePath + "/" + fileName);
        if (!file.exists() || file.isDirectory())
            return false;
        return file.delete();
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
        LemonMessage lemonMessage = LemonContext.getBean(LemonMessage.class);
        try {
            MultiCardFilePath path = MultiCard.getInstance(context).getWritePath(fileName);
            if(path.getCode() == MultiCardFilePath.RET_LIMIT_SPACE_WARNNING) {
                if(tip && lemonMessage != null) {
                    if(TextUtils.isEmpty(warnningTip)) {
                        lemonMessage.sendMessage(R.string.toast_sdcard_not_enough_warning);
                    } else {
                        lemonMessage.sendMessage(warnningTip);
                    }
                }
            }
            return path.getFilePath();
        } catch (Exception e) {
            e.printStackTrace();
            if(tip && lemonMessage != null) {
                if(TextUtils.isEmpty(unwriteTip)) {
                    lemonMessage.sendMessage(R.string.toast_sdcard_not_enough);
                } else {
                    lemonMessage.sendMessage(unwriteTip);
                }
            }
        }
        return null;
    }
}
