package com.lemon.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.lang.reflect.Field;


/**
 * @author geolo
 */

public class ScreenUtil {

    public static int screenWidth;
    public static int screenHeight;
    public static float density;
    public static float scaleDensity;
    public static float xdpi;
    public static float ydpi;
    public static int densityDpi;
    public static int screenMin;// ����У���С��ֵ


    public static void init(Context context) {
        if (null == context) {
            return;
        }
        String str = "";
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        screenMin = (screenWidth > screenHeight) ? screenHeight : screenWidth;
        density = dm.density;
        scaleDensity = dm.scaledDensity;
        xdpi = dm.xdpi;
        ydpi = dm.ydpi;
        densityDpi = dm.densityDpi;
        str += "The absolute width:" + String.valueOf(screenWidth) + "pixels\n";

        str += "The absolute heightin:" + String.valueOf(screenHeight)
                + "pixels\n";
        str += "The logical density of the dis  play.:" + String.valueOf(density)
                + "\n";
        str += "The logical density of the scaledisplay.:" + String.valueOf(scaleDensity)
                + "\n";
        str += "X dimension :" + String.valueOf(xdpi) + "pixels per inch\n";
        str += "Y dimension :" + String.valueOf(ydpi) + "pixels per inch\n";
        str += "The logical densityDpi of the display.:" + String.valueOf(densityDpi) + "\n";
    }

    /**
     * dip转px
     *
     * @param dipValue
     * @param context
     * @return
     */
    public static int dip2px(Context context,float dipValue) { //#0001-
        if (density == 0.0){
            init(context);
        }
        final float scale = ScreenUtil.density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * px转dp
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context,float pxValue) {
        if (density == 0.0){
            init(context);
        }
        final float scale = ScreenUtil.density;
        return (int) ((pxValue - 0.5) / scale);
    }

    /**
     * 获取屏幕宽高的最小值
     */
    public static int getMinScreenWH(Context context) {
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        return (screenWidth > screenHeight) ? screenHeight : screenWidth;
    }

    /**
     * 获取状态栏 高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 返回屏幕分辨率,字符串型。如 320x480
     *
     * @param ctx
     * @return
     */
    public static String getScreenResolution(Context ctx) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) ctx
                .getApplicationContext().getSystemService(
                        Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels < metrics.heightPixels ? metrics.widthPixels : metrics.heightPixels;
        int height = metrics.widthPixels < metrics.heightPixels ? metrics.heightPixels : metrics.widthPixels;
        //String resolution = width + "x" + height;
        String resolution = width + "*" + height;
        return resolution;
    }


    /**
     * 返回屏幕分辨率,数组型。width小于height
     *
     * @param ctx
     * @return
     */

    public static int[] getScreenResolutionXY(Context ctx) {
        int[] resolutionXY = new int[2];
        if (resolutionXY[0] != 0) {
            return resolutionXY;
        }
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) ctx
                .getApplicationContext().getSystemService(
                        Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        resolutionXY[0] = metrics.widthPixels < metrics.heightPixels ? metrics.widthPixels : metrics.heightPixels;
        resolutionXY[1] = metrics.widthPixels < metrics.heightPixels ? metrics.heightPixels : metrics.widthPixels;
        return resolutionXY;
    }

    /**
     * 返回屏幕密度
     *
     * @param ctx
     * @return
     */

    public static float getScreenDensity(Context ctx) {
        return ctx.getResources().getDisplayMetrics().density;
    }

}
