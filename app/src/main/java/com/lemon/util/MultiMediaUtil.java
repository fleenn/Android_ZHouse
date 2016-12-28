package com.lemon.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;

import com.lemon.util.sdCard.FileSuffix;
import com.lemon.util.sdCard.StorageUtil;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**多媒体处理工具类,例如截图，裁剪等*/
public class MultiMediaUtil {

	public static final long MAX_SIZE_IMAGE = 200 * 1024;// 图片允许的大小200k
	public static final int MAX_HEIGHT_WIDTH_IMAGE = ScreenUtil.screenMin; // 拍照图片最大宽高
	public static final String O_TEMP_PIC_NAME = "O_temp.jpg";// 发出去临时图片名
	public static final String I_TEMP_PIC_NAME = "I_temp.jpg";// 传进来临时图片名

	/**
	 * 是否需要缩放图片
	 * 
	 * 1.图片尺寸大于200k
	 * 2.图片大小大于屏幕最宽
	 */
	public static Boolean isNeedScaleImage(String filename){
		// 判断文件大小
		File imageFile = new File(filename);
		if(!imageFile.exists()){
			return false;
		}
		if(imageFile.length() > MAX_SIZE_IMAGE){
			return true;
		}

		// 判断文件尺寸
		Options bitmapOptions = new Options();
		bitmapOptions.inJustDecodeBounds = true;// 只取得outHeight(图片原始高度)和
		bitmapOptions.inSampleSize = 1;
		// outWidth(图片的原始宽度)而不加载图片
		Bitmap bitmap = BitmapFactory.decodeFile(filename, bitmapOptions);
		if(bitmap == null){
			return false;	
		}
		if(bitmap.getWidth() > ScreenUtil.screenMin || bitmap.getHeight() > ScreenUtil.screenMin){
			return true;
		}
		return false;
	}

	/**
	 * 压缩图片：用于拍照后
	 * @param context
	 * @param imageFile
	 */
	public static File scaleImage(Context context, File imageFile) {
		boolean isScale = false;
		if (isNeedScaleImage(imageFile.getAbsolutePath())) {// 如果图片超过200K，则进行压缩
			isScale = true;
		}
		// 图片处理
		String tempPath = StorageUtil.getWritePathIgnoreError(context, O_TEMP_PIC_NAME);
		File fileTemp = new File(tempPath);
		if (fileTemp.exists()) {
			fileTemp.delete();
		}
		scaleImageWithFilter(imageFile, fileTemp, MAX_HEIGHT_WIDTH_IMAGE, true, true, isScale ,true);
		// 删除原来图片，把临时文件改为目标文件
		imageFile.delete();
		fileTemp.renameTo(imageFile);
		return imageFile;
	}

	/**
	 * 压缩图片：用于一般情况
	 * @param context
	 * @param uri
	 * @return
	 */
	public static File scaleImage(Context context, Uri uri) {
		final SimpleDateFormat imageFileSD = new SimpleDateFormat("yyyyMMdd_hhmmss_SSS");
		String fileName = "O_" + imageFileSD.format(new Date()) + FileSuffix.JPG;
		String origBitmapPath = StorageUtil.getWritePathIgnoreError(context ,fileName);
		long origBitmapSize = 0;
		try {
			origBitmapSize = StorageUtil.save(context.getContentResolver().openInputStream(uri), origBitmapPath);
			if (isNeedScaleImage(origBitmapPath)) {// 如果图片超过200K，则进行压缩
				File mImageFile = new File(origBitmapPath);
				// 图片处理
				String tempPath = StorageUtil.getWritePathIgnoreError(context ,O_TEMP_PIC_NAME);
				File fileTemp = new File(tempPath);
				if (fileTemp.exists()) {
					fileTemp.delete();
				}
				scaleImageWithFilter(mImageFile, fileTemp, MAX_HEIGHT_WIDTH_IMAGE, false, false, true,true);
				// 删除原来图片，把临时文件改为目标文件
				mImageFile.delete();
				fileTemp.renameTo(mImageFile);
				origBitmapSize = mImageFile.length();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		File imageFile = new File(origBitmapPath);
		return imageFile;
	}

	/**
	 * 压缩图片：用于一般情况
	 * @param context
	 * @return
	 */
	public static File scaleImage(Context context, Bitmap bm) {
		final SimpleDateFormat imageFileSD = new SimpleDateFormat("yyyyMMdd_hhmmss_SSS");
		String fileName = "O_" + imageFileSD.format(new Date()) + FileSuffix.JPG;
		String origBitmapPath = StorageUtil.getWritePathIgnoreError(context ,fileName);
		long origBitmapSize = 0;
		try {
			origBitmapSize = StorageUtil.save(bm, origBitmapPath);
			if (isNeedScaleImage(origBitmapPath)) {// 如果图片超过200K，则进行压缩
				File mImageFile = new File(origBitmapPath);
				// 图片处理
				String tempPath = StorageUtil.getWritePathIgnoreError(context ,O_TEMP_PIC_NAME);
				File fileTemp = new File(tempPath);
				if (fileTemp.exists()) {
					fileTemp.delete();
				}
				scaleImageWithFilter(mImageFile, fileTemp, MAX_HEIGHT_WIDTH_IMAGE, false, false, true,true);
				// 删除原来图片，把临时文件改为目标文件
				mImageFile.delete();
				fileTemp.renameTo(mImageFile);
				origBitmapSize = mImageFile.length();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		File imageFile = new File(origBitmapPath);
		return imageFile;
	}

	/**
	 * 等比例缩放图片（带滤波器）
	 * 
	 * @param srcFile
	 *            来源文件
	 * @param dstFile
	 *            目标文件
	 * @param dstMaxWH
	 *            目标文件宽高最大值
	 * @param bContrast
	 *            提高对比度滤波器，可使图片变亮丽
	 * @param bSharp
	 *            锐化图片，可使图片清晰（暂时无效果）
	 * @param isScale
	 *            是否缩放
	 * @param bRotate
	 * 			是否旋转
	 */
	public static Boolean scaleImageWithFilter(File srcFile, File dstFile,
			int dstMaxWH, Boolean bContrast, Boolean bSharp, Boolean isScale ,Boolean bRotate) {
		Boolean bRet = false;

		// 路径文件不存在
		if (!srcFile.exists()) {
			return bRet;
		}

		try {
			// 判断是否旋转
			float rotate = 90.0F;
			if(bRotate){
				ExifInterface localExifInterface = new ExifInterface(srcFile.getAbsolutePath());
				int rotateInt = localExifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
				rotate = getImageRotate(rotateInt);
			}

			// 打开源文件
			Bitmap srcBitmap;
			{
				java.io.InputStream is;
				is = new FileInputStream(srcFile);
				Options opts = getOptionsWithInSampleSize(srcFile.getPath(), dstMaxWH);
				srcBitmap = BitmapFactory.decodeStream(is, null, opts);
				if (srcBitmap == null){
					return bRet;
				}
			}
			// 原图片宽高
			int width = srcBitmap.getWidth();
			int height = srcBitmap.getHeight();
			// 获得缩放因子
			float scale = 1.f;
			if (width > dstMaxWH || height > dstMaxWH) {
				float scaleTemp = (float) dstMaxWH / (float) width;
				float scaleTemp2 = (float) dstMaxWH / (float) height;
				if (scaleTemp > scaleTemp2)
					scale = scaleTemp2;
				else
					scale = scaleTemp;
			}
			// 图片缩放
			Bitmap dstBitmap = srcBitmap;
			if(isScale || bRotate){
				Matrix matrix = new Matrix();
				if(isScale && scale != 1.f){
					dstBitmap = srcBitmap;
					matrix.postScale(scale, scale);					
				}
				if(bRotate && rotate != 0){//rotate等于0的时候，dstBitmap和srcBitmap是用同一个内存引用地址
					matrix.postRotate(rotate);	
					dstBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, width, height, matrix, true);
				}
			}

			// 提高对比度
			if (bContrast) {
				Bitmap tempBitmap = Bitmap.createBitmap(dstBitmap.getWidth(),
						dstBitmap.getHeight(), Config.ARGB_8888);

				Canvas canvas = new Canvas(tempBitmap);
				ColorMatrix cm = new ColorMatrix();
				float contrast = 30.f / 180.f; // 提高30对比度
				setContrast(cm, contrast);
				Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
				paint.setColorFilter(new ColorMatrixColorFilter(cm));
				canvas.drawBitmap(dstBitmap, 0, 0, paint);
			}
			// 提高锐化
			if (bSharp) {
				// TODO:添加锐化功能
			}
			// 保存文件
			if (dstFile.exists())
				dstFile.delete();
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(dstFile));
			dstBitmap.compress(CompressFormat.JPEG, 90, bos);

			if (srcBitmap!= null && !srcBitmap.isRecycled()){
				srcBitmap.recycle();
			}
			srcBitmap = null;
			
			if (dstBitmap != null && !dstBitmap.isRecycled()){
				dstBitmap.recycle();
			}
			dstBitmap = null;

			bos.flush();
			bos.close();
			bRet = true;
		} catch (Exception e) {
			Log.e("MultiMediaUtil.java", ">>>>>>>>>>>> scaleImageWithFilter() <<<<<<<<<<<", e);
			return bRet;
		}
		return bRet;
	}

	public static Bitmap rotateImage(File file){
		ExifInterface localExifInterface;
		try {
			localExifInterface = new ExifInterface(file.getAbsolutePath());
			int rotateInt = localExifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 
					ExifInterface.ORIENTATION_NORMAL);
			float rotate = getImageRotate(rotateInt);
			Matrix matrix = new Matrix();
			matrix.postRotate(rotate);	
			// 打开源文件
			Bitmap srcBitmap;
			{
				java.io.InputStream is;
				is = new FileInputStream(file);
				Options opts = getOptionsWithInSampleSize(file.getPath(), MAX_HEIGHT_WIDTH_IMAGE);
				srcBitmap = BitmapFactory.decodeStream(is, null, opts);
			}
			int width = srcBitmap.getWidth();
			int height = srcBitmap.getHeight();
			Bitmap dstBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, width, height, matrix, true);
			if (!srcBitmap.isRecycled()){
				srcBitmap.recycle();
			}
			return dstBitmap;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Bitmap decodeFile(File f) {
		try {
			// decode image size
			Options o = new Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			Options o2 = new Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	public static int computeSampleSize(Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);  
		int roundedSize;  
		if (initialSize <= 8) {  
			roundedSize = 1;  
			while (roundedSize < initialSize) {  
				roundedSize <<= 1;  
			}  
		} else {  
			roundedSize = (initialSize + 7) / 8 * 8;  
		}  
		return roundedSize;  
	}  

	private static int computeInitialSampleSize(Options options,int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;  
		double h = options.outHeight;  
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));  
		int upperBound = (minSideLength == -1) ? 128 :(int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));  
		if (upperBound < lowerBound) {  
			// return the larger one when there is no overlapping zone.  
			return lowerBound;  
		}  
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {  
			return 1;  
		} else if (minSideLength == -1) {  
			return lowerBound;  
		} else {  
			return upperBound;  
		}  
	}  

	public static Bitmap decodeFile(Context context ,Uri uri,int requiredSize) {
		try {
			//			// decode image size
			Options o = new Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, o);

			// Find the correct scale value. It should be the power of 2.
			//final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize || requiredSize == -1){
					break;
				}
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			Options o2 = new Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, o2);

			//			BitmapFactory.Options o2 = new BitmapFactory.Options();
			//			o2.inSampleSize = computeSampleSize(o2 , requiredSize ,-1);
			//			return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, o2);
		} catch (Exception e) {
			// TODO: 2015/10/25
			String dddddd;
		}
		return null;
	}

	/** 图片质量控制,让图片不超过200k */
	public static byte[] qualityCompless(Bitmap srcBitmap){
		return MultiMediaUtil.qualityCompless(srcBitmap , MAX_SIZE_IMAGE);
	}

	/** 图片质量控制 
	 * @param qualitySize 单位k*/
	public static byte[] qualityCompless(Bitmap srcBitmap , long qualitySize){
		int options = 100; 
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		srcBitmap.compress(CompressFormat.PNG, 100,baos);
		while (baos.toByteArray().length > qualitySize * 1024) {  //循环判断如果压缩后图片是否大于200kb,大于继续压缩        
			baos.reset();//重置baos即清空baos 
			if(options < 1){
				options = 1;
			}
			srcBitmap.compress(CompressFormat.PNG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
			if(options == 1){
				break;
			}else if(options < 10){
				options -= 1;
			}else{
				options -= baos.toByteArray().length / qualitySize * 1024;//每次都减少10 				
			}
		} 
		Log.v("MultiMediaUtil.java", "最后压缩的容量为：" + baos.toByteArray().length + "b" + " 压缩的质量因子为：" + options + "~" + (options + 10));
		return baos.toByteArray();
	}

	/**
	 * 获取长宽都不超过160dip的图片，基本思想是设置Options.inSampleSize按比例取得缩略图
	 */
	public static Options getOptionsWithInSampleSize(String filePath,
			int maxWidth) {
		Options bitmapOptions = new Options();
		bitmapOptions.inJustDecodeBounds = true;// 只取得outHeight(图片原始高度)和
		// outWidth(图片的原始宽度)而不加载图片
		BitmapFactory.decodeFile(filePath, bitmapOptions);
		bitmapOptions.inJustDecodeBounds = false;
		int inSampleSize = bitmapOptions.outWidth / (maxWidth / 10);// 应该直接除160的，但这里出16是为了增加一位数的精度
		if (inSampleSize % 10 != 0) {
			inSampleSize += 10;// 尽量取大点图片，否则会模糊
		}
		inSampleSize = inSampleSize / 10;
		if (inSampleSize <= 0) {// 判断200是否超过原始图片高度
			inSampleSize = 1;// 如果超过，则不进行缩放
		}
		bitmapOptions.inSampleSize = inSampleSize;
		return bitmapOptions;
	}

	/**
	 * 获得旋转角度
	 * @param rotate
	 * @return
	 */
	private static float getImageRotate(int rotate){
		float f;
		if (rotate == 6){
			f = 90.0F;
		} else if (rotate == 3){
			f = 180.0F;
		} else if (rotate == 8){
			f = 270.0F;
		} else {
			f = 0.0F;
		}

		return f;
	}

	/**
	 * 设置对比度矩阵
	 */
	private static void setContrast(ColorMatrix cm, float contrast) {
		float scale = contrast + 1.f;
		float translate = (-.5f * scale + .5f) * 255.f;
		cm.set(new float[] { scale, 0, 0, 0, translate, 0, scale, 0, 0,
				translate, 0, 0, scale, 0, translate, 0, 0, 0, 1, 0 });
	}

	/**
	 * 转换图片成圆形
	 * @param bitmap 传入Bitmap对象
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		if(bitmap == null){
			return null;
		}
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		int maxSize = 480;
		if(width > maxSize || height > maxSize ){//设置最大尺寸480，避免内存溢出
			if( width  >= height){
				height = height - (width - maxSize);
				width = maxSize ;
				dst_right = height;
				dst_bottom = height;
			}else{
				width = width - (width - maxSize);
				height = maxSize ;
				dst_right = width;
				dst_bottom = width;
			}
		}


		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect(( int)left, ( int )top, ( int)right, (int )bottom);
		final Rect dst = new Rect(( int)dst_left, ( int )dst_top, (int )dst_right, ( int)dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias( true );

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode( new PorterDuffXfermode(Mode. SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}
	
	/**
	 * 转换图片成正方形
	 * @param bitmap 传入Bitmap对象
	 * @return
	 */
	public static Bitmap toSquareBitmap(Bitmap bitmap) {
		if(bitmap == null){
			return null;
		}
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		int maxSize = 480;
		if(width > maxSize || height > maxSize ){//设置最大尺寸480，避免内存溢出
			if( width  >= height){
				height = height - (width - maxSize);
				width = maxSize ;
				dst_right = height;
				dst_bottom = height;
			}else{
				width = width - (width - maxSize);
				height = maxSize ;
				dst_right = width;
				dst_bottom = width;
			}
		}


		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect(( int)left, ( int )top, ( int)right, (int )bottom);
		final Rect dst = new Rect(( int)dst_left, ( int )dst_top, (int )dst_right, ( int)dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias( true );

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, 0, 0, paint);

		paint.setXfermode( new PorterDuffXfermode(Mode. SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

}
