package com.lemon.util.sdCard;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * 多卡存储接口类
 * @author geolo
 */
public class MultiCard {
	private final long M = 1024 * 1024;
	private final long K = 1024;

	/**
	 * 外置存储卡默认预警临界值
	 */
	private final long DEF_EXTERNAL_SDCARD_WARNNING_LIMIT_SPACE_SIZE = 100 * M;

	/**
	 * 内置存储卡默认预警临界值
	 */
	private final long DEF_INTERNAL_SDCARD_WARNNING_LIMIT_SPACE_SIZE = 100 * M;

	/**
	 * 手机默认预警临界值
	 */
	private final long DEF_PHONE_WARNNING_LIMIT_SPACE_SIZE = 50 * M;	

	/**
	 * 默认视频大小最大值
	 */
	private final long DEF_VIDEO_MAX_SIZE = 50 * M;

	/**
	 * 默认图片大小最大值
	 */
	private final long DEF_IMAGE_MAX_SIZE = 500 * K;

	/**
	 * 默认文本大小最大值
	 */
	private final long DEF_TXT_MAX_SIZE = 300 * K;

	/**
	 * 默认日志大小最大值
	 */
	private final long DEF_LOG_MAX_SIZE = 300 * K;

	/**
	 * 默认音频大小最大值
	 */
	private final long DEF_AUDIO_MAX_SIZE = 10 * M;

	/**
	 * 默认自定义数据大小最大值
	 */
	private final long DEF_DATA_MAX_SIZE = 50 * M;

	/**
	 * 默认头像大小最大值
	 */
	private final long DEF_HEAD_MAX_SIZE = 500 * K;

	/**
	 * 默认APK大小最大值
	 */
	private final long DEF_APK_MAX_SIZE = 20 * M;


	/**
	 * 媒体类型
	 */
	public final static int TYPE_DATA = 0;
	public final static int TYPE_VIDEO = 1;
	public final static int TYPE_IMAGE = 2;
	public final static int TYPE_TXT = 3;
	public final static int TYPE_LOG = 4;
	public final static int TYPE_AUDIO = 5;
	public final static int TYPE_AVATAR = 6;
	public final static int TYPE_APK = 7;
	public final static int TYPE_OTHER = 8;

	/**
	 * 目标目录
	 */
	private final int DIRECTORY_EXTERNAL_SDCARD = 0;
	private final int DIRECTORY_INTERNAL_SDCARD = 1;
	private final int DIRECTORY_PHONE = 2;

	/**
	 * 目录名
	 */
	private final String ROOT_DIRECTORY_NAME = "ELN/";
	private final String AUDIO_DIRECTORY_NAME = "audio/";
	private final String VIDEO_DIRECTORY_NAME = "video/";
	private final String TXT_DIRECTORY_NAME = "txt/";
	private final String LOG_DIRECTORY_NAME = "log/";
	public static final String IMAGE_DIRECTORY_NAME = "image/";
	private final String DATA_DIRECTORY_NAME = "data/";
	private final String AVATAR_DIRECTORY_NAME = "avatar/";
	private final String APK_DIRECTORY_NAME = "apk/";
	private final String OTHER_DIRECTORY_NAME = "other/";


	/**
	 * 外置存储卡预警临界值
	 */
	private long mExternalSDCardWarnningLimitSpace = DEF_EXTERNAL_SDCARD_WARNNING_LIMIT_SPACE_SIZE;

	/**
	 * 内置存储卡预警临界值
	 */
	private long mInternalSDCardWarnningLimitSpace = DEF_INTERNAL_SDCARD_WARNNING_LIMIT_SPACE_SIZE;

	/**
	 * 手机预警临界值
	 */
	private long mPhoneWarnningLimitSpace = DEF_PHONE_WARNNING_LIMIT_SPACE_SIZE;

	//	/**
	//	 * 外置存储卡拒写临界值
	//	 */
	//	private long mExternalSDCardUnwriteLimitSpace = DEF_EXTERNAL_SDCARD_UNWRITE_LIMIT_SPACE_SIZE;
	//	
	//	/**
	//	 * 内置存储卡拒写临界值
	//	 */
	//	private long mInternalSDCardUnwriteLimitSpace = DEF_INTERNAL_SDCARD_UNWRITE_LIMIT_SPACE_SIZE;
	//	
	//	/**
	//	 * 手机拒写临界值
	//	 */
	//	private long mPhoneUnwriteLimitSpace = DEF_PHONE_UNWRITE_LIMIT_SPACE_SIZE;

	/**
	 * 视频大小最大值
	 */
	private long mVideoMaxSize = DEF_VIDEO_MAX_SIZE;

	/**
	 * 图片大小最大值
	 */
	private long mImageMaxSize = DEF_IMAGE_MAX_SIZE;

	/**
	 * 文本大小最大值
	 */
	private long mTxtMaxSize = DEF_TXT_MAX_SIZE;

	/**
	 * 日志大小最大值
	 */
	private long mLogMaxSize = DEF_LOG_MAX_SIZE;

	/**
	 * 音频大小最大值
	 */
	private long mAudioMaxSize = DEF_AUDIO_MAX_SIZE;

	/**
	 * 自定义数据大小最大值
	 */
	private long mDataMaxSize = DEF_DATA_MAX_SIZE;

	/**
	 * 头像大小最大值
	 */
	private long mHeadMaxSize = DEF_HEAD_MAX_SIZE;

	/**
	 * APK大小最大值
	 */
	private long mApkMaxSize = DEF_APK_MAX_SIZE;

	/**
	 * 外置SD卡路径
	 */
	private String mExternalSDCardPath = "";

	/**
	 * 内置SD卡路径
	 */
	private List<String> mInternalSDCardPath = new ArrayList<String>();

	/**
	 * 手机本身存储空间路径
	 */
	private String mPhoneDataPath = "";

	/**
	 * 存储空间相应目录是否已经创建
	 */
	private boolean isMakeExternalSDCardDirectory;
	private boolean isMakeInternalSDCardDirectory;
	private boolean isMakePhoneDataDirectroy;


	private static MultiCard instance;


	/**
	 * 获得单实例接口
	 */
	public static MultiCard getInstance(Context context) {
		if (instance == null) {
			instance = new MultiCard();
			instance.setInternalSDCard(getInternalSDCardPath(context));
			instance.mExternalSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
			//            Util.cjxLog("外置SD卡路径：", instance.mExternalSDCardPath);
			//    instance.mPhoneDataPath = Environment.getDataDirectory().getAbsolutePath();	//没权限
			instance.mPhoneDataPath = context.getFilesDir().getAbsolutePath();
			//            Util.cjxLog("手机DATA路径：", instance.mPhoneDataPath);
		}
		instance.makeAllDirectory();
		return instance;
	}

	public static void init(Context context) {
		getInstance(context);
	}

	/**
	 * 文件全名转绝对路径（读）
	 * @param fileName 文件全名（文件名.扩展名）
	 * @return 返回绝对路径
	 */
	public String getReadPath(String fileName){
		if(TextUtils.isEmpty(fileName)) {
			return "";
		}

		int fileType = getFileType(fileName);
		List<StorageDirectory> directories = getDirectorysByPriority(fileType);
		for(StorageDirectory directory : directories) {
			String filePath = directory.path + fileName;
			File file = new File(filePath);
			if(file.exists()) {
				//    			Util.cjxLog("read path:", filePath);
				return filePath;
			}
		}
		return "";
	}

	/**
	 * 文件全名转绝对路径（写）
	 * @param fileName 文件全名（文件名.扩展名）
	 * @return 返回绝对路径信息
	 * @throws LimitSpaceUnwriteException 内存不足
	 * @throws IllegalArgumentException 文件名不合法
	 */
	public MultiCardFilePath getWritePath(String fileName) throws LimitSpaceUnwriteException, IllegalArgumentException {
		if(TextUtils.isEmpty(fileName)) {
			throw new IllegalArgumentException();
		}

		int fileType = getFileType(fileName);//获取文件媒体类型
		StorageDirectory directory = limitSpaceUnwrite(fileType, 0);
		if(!new File(directory.path).exists()){
			new File(directory.path).mkdirs();
		}
		if(directory == null) {
			throw new LimitSpaceUnwriteException();
		} else {
			MultiCardFilePath multiCardFilePath = new MultiCardFilePath();
			multiCardFilePath.setFilePath(directory.path + fileName);
			if(limitSpaceWarnning(directory, fileType)) {
				multiCardFilePath.setCode(MultiCardFilePath.RET_LIMIT_SPACE_WARNNING);
			} else {
				multiCardFilePath.setCode(MultiCardFilePath.RET_OK);
			}
			return multiCardFilePath;
		}
	}

	/**
	 * 设置内置SD卡目录
	 * @param directories
	 */
	public void setInternalSDCard(List<String> directories) {
		mInternalSDCardPath = directories;
	}

	/**
	 * 删除所有头像
	 * @param fileSuffix 指定需要删除的文件类型{@link FileSuffix}
	 * @param noDel 不删除的文件名
	 */
	public void deleteAllByType(String fileSuffix ,String noDel){
		int fileType = getFileType(fileSuffix);
		List<StorageDirectory> directories = getDirectorysByPriority(fileType);
		for(StorageDirectory directory : directories) {
			File file = new File(directory.path);
			if (file.exists() && file.isDirectory()) {
				String[] strings = file.list();
				for (String string : strings) {
					if (string.equals(noDel)) {
						continue;
					}
					File avatarFile = new File(directory.path + string);
					if (avatarFile.exists()) {
						avatarFile.delete();
					}
				}
			}
		}
	}

	/**
	 * 外置和内置SD卡是否存在
	 * @return
	 */
	public boolean isSDCardExist() {
		return isExternalSDCardExist() || isInternalSDCardExist();
	}

	/**
	 * 获取外置存储卡剩余空间
	 * @return
	 */
	public long getExternalSDCardSpace() {
		return getResidualSpace(mExternalSDCardPath);
	}

	/**
	 * 获取内置存储卡剩余空间
	 * @return
	 */
	public long getInternalSDCardSpace() {
		if(mInternalSDCardPath != null && mInternalSDCardPath.size() > 0) {
			long size = 0;
			for(String path:mInternalSDCardPath) {
				long space = getResidualSpace(path);
				if(space > size) {
					size = space;
				}
			}

			return size;
		} else {
			return 0;
		}
	}

	/**
	 * 根据文件类型，检查是否有可写空间
	 * fileType 文件类型
	 * type 检查的范围,0表示所有,1表示只检查外置
	 * @return
	 */
	public boolean checkSDCardSpace(int fileType, int type) {
		StorageDirectory directory = limitSpaceUnwrite(fileType, type);
		if(directory != null) {
			return true;
		} else {
			return false;
		}
	}

	private MultiCard() {

	}

	/**
	 * 是否因空间不足报警
	 * @param directory 存储目录
	 * @param fileType 请求写操作的文件类型
	 * @return 返回true表示内存预警，否则反之
	 */
	private boolean limitSpaceWarnning(StorageDirectory directory, int fileType) {
		if(getDirectoryWarnningLimitSpace(directory.type) > directory.residualSpace) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 是否因空间不足拒写
	 * @param fileType 请求写操作的文件类型
	 * @return 返回满足写条件的最优目录；如果返回null表示没有目录满足条件
	 */
	private StorageDirectory limitSpaceUnwrite(int fileType, int type) {
		List<StorageDirectory> directories = getDirectorysByPriority(fileType);
		for(StorageDirectory directory : directories) {
			//如果只检查外置存储卡则过滤下
			if(type == 1 && directory.type != DIRECTORY_EXTERNAL_SDCARD) {
				continue;
			}
			long residualSpace = getResidualSpace(directory.rootDirectory);
			if(getFileMaxSize(fileType) <= residualSpace) {
				directory.residualSpace = residualSpace;
				return directory;
			}
		}
		return null;
	}

	/**
	 * 根据文件类型及检查范围，判断是否需要空间预警
	 * @param fileType 文件类型
	 * @param type 检查范围,0表示全部,1表示外置
	 * @return 返回绝对路径信息
	 */
	public boolean islimitSpaceWarning(int fileType, int type) {
		StorageDirectory directory = limitSpaceUnwrite(fileType, type);
		if(directory == null) {
			return false;
		} else {
			return !limitSpaceWarnning(directory, fileType);
		}
	}

	/**
	 * 获取目录剩余空间
	 * @param directoryPath
	 * @return
	 */
	private long getResidualSpace(String directoryPath) {
		try {
			StatFs sf = new StatFs(directoryPath);
			long blockSize = sf.getBlockSize();
			long availCount = sf.getAvailableBlocks();
			long availCountByte = availCount * blockSize;
			return availCountByte;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取文件最大大小
	 * @param type
	 * @return
	 */
	private long getFileMaxSize(int type) {
		switch (type) {
		case TYPE_AUDIO:
			return mAudioMaxSize;

		case TYPE_VIDEO:
			return mVideoMaxSize;

		case TYPE_TXT:
			return mTxtMaxSize;

		case TYPE_LOG:
			return mLogMaxSize;

		case TYPE_IMAGE:
			return mImageMaxSize;

		case TYPE_DATA:
			return mDataMaxSize;

		case TYPE_AVATAR:
			return mHeadMaxSize;

		case TYPE_APK:
			return mApkMaxSize;

		default:
			return mDataMaxSize;
		}
	}

	/**
	 * 按优先级取出所有目录
	 * @param fileType
	 * @return
	 */
	private List<StorageDirectory> getDirectorysByPriority(int fileType) {
		String endDirectoryName = String.format("/%s%s", ROOT_DIRECTORY_NAME, getDirectoryName(fileType));
		List<StorageDirectory> directories = new ArrayList<StorageDirectory>();
		if(fileType == TYPE_AVATAR) {
			directories.add(new StorageDirectory(mPhoneDataPath + endDirectoryName, DIRECTORY_PHONE, mPhoneDataPath));
			if(isInternalSDCardExist()) {
				for(String directory : mInternalSDCardPath) {
					directories.add(new StorageDirectory(directory + endDirectoryName, DIRECTORY_INTERNAL_SDCARD, directory));
				}
			}
			if(isExternalSDCardExist()) {
				directories.add(new StorageDirectory(mExternalSDCardPath + endDirectoryName, DIRECTORY_EXTERNAL_SDCARD, mExternalSDCardPath));
			}
		} else {
			if(isExternalSDCardExist()) {
				directories.add(new StorageDirectory(mExternalSDCardPath + endDirectoryName, DIRECTORY_EXTERNAL_SDCARD, mExternalSDCardPath));
			}
			if(isInternalSDCardExist()) {
				for(String directory : mInternalSDCardPath) {
					directories.add(new StorageDirectory(directory + endDirectoryName, DIRECTORY_INTERNAL_SDCARD, directory));
				}
			}

			//图片,音频，视频需要外部程序使用,外部程序无法访问沙盒，不支持
			if(fileType == TYPE_DATA || fileType == TYPE_TXT || fileType == TYPE_LOG || fileType == TYPE_AVATAR || fileType == TYPE_APK|| fileType == TYPE_OTHER) {
				directories.add(new StorageDirectory(mPhoneDataPath + endDirectoryName, DIRECTORY_PHONE, mPhoneDataPath));
			}
		}
		return directories;
	}

	/**
	 * 获取目录报警临界空间
	 * @param directory
	 * @return
	 */
	private long getDirectoryWarnningLimitSpace(int directory) {
		switch (directory) {
		case DIRECTORY_EXTERNAL_SDCARD:
			return mExternalSDCardWarnningLimitSpace;

		case DIRECTORY_INTERNAL_SDCARD:
			return mInternalSDCardWarnningLimitSpace;

		case DIRECTORY_PHONE:
			return mPhoneWarnningLimitSpace;

		default:
			return mPhoneWarnningLimitSpace;
		}
	}

	/**
	 * 获取目录拒写临界空间
	 * @param directory
	 * @return
	 */
	private long getDirectoryUnwriteLimitSpace(int directory) {
		switch (directory) {
		case DIRECTORY_EXTERNAL_SDCARD:
			return 0;

		case DIRECTORY_INTERNAL_SDCARD:
			return 0;

		case DIRECTORY_PHONE:
			return 0;

		default:
			return 0;
		}
	}

	/**
	 * 获取文件媒体类型
	 * @param fileName
	 * @return
	 */
	private int getFileType(String fileName) {
		if(fileName.endsWith(FileSuffix.VIDEO)) {
			return TYPE_VIDEO;
		} else if(fileName.endsWith(FileSuffix.MP4)) {
			return TYPE_AUDIO;
		} else if(fileName.endsWith(FileSuffix.MP3)) {
			return TYPE_AUDIO;
		}else if(fileName.endsWith(FileSuffix.THREE_GPP)) {
			return TYPE_AUDIO;
		}else if(fileName.endsWith(FileSuffix.M4A)) {
			return TYPE_AUDIO;
		}else if(fileName.endsWith(FileSuffix.JPG)) {
			return TYPE_IMAGE;
		} else if(fileName.endsWith(FileSuffix.JPEG)) {
			return TYPE_IMAGE;
		} else if(fileName.endsWith(FileSuffix.PNG)) {
			return TYPE_IMAGE;
		} else if(fileName.endsWith(FileSuffix.BMP)) {
			return TYPE_IMAGE;
		} else if(fileName.endsWith(FileSuffix.LOG)) {
			return TYPE_LOG;
		} else if(fileName.endsWith(FileSuffix.TXT)) {
			return TYPE_TXT;
		} else if(fileName.endsWith(FileSuffix.PDF)){
			return TYPE_TXT;
		} else if(fileName.endsWith(FileSuffix.DOC)){
			return TYPE_TXT;
		} else if(fileName.endsWith(FileSuffix.DATA)){
			return TYPE_DATA;
		} else if(fileName.endsWith(FileSuffix.APK)) {
			return TYPE_APK;
		} else if(fileName.endsWith(FileSuffix.AVATOR)) {
			return TYPE_AVATAR;
		} else {
			return TYPE_OTHER;
		}
	}

	/**
	 * 获取文件对应的目录名
	 * @param fileType
	 * @return
	 */
	private String getDirectoryName(int fileType) {
		switch (fileType) {
		case TYPE_AUDIO:
			return AUDIO_DIRECTORY_NAME;

		case TYPE_VIDEO:
			return VIDEO_DIRECTORY_NAME;

		case TYPE_TXT:
			return TXT_DIRECTORY_NAME;

		case TYPE_LOG:
			return LOG_DIRECTORY_NAME;

		case TYPE_IMAGE:
			return IMAGE_DIRECTORY_NAME;

		case TYPE_DATA:
			return DATA_DIRECTORY_NAME;

		case TYPE_AVATAR:
			return AVATAR_DIRECTORY_NAME;

		case TYPE_APK:
			return APK_DIRECTORY_NAME;

		default:
			return OTHER_DIRECTORY_NAME;
		}
	}

	/**
	 * 创建存储空间下的所有目录
	 */
	private void makeAllDirectory() {
		makeDirectory(DIRECTORY_EXTERNAL_SDCARD);
		makeDirectory(DIRECTORY_INTERNAL_SDCARD);
		makeDirectory(DIRECTORY_PHONE);
	}

	/**
	 * 创建必要的目录
	 * @param directoryType
	 */
	private void makeDirectory(int directoryType) {
		List<String> rootDirectorys = new ArrayList<String>();
		switch (directoryType) {
		case DIRECTORY_EXTERNAL_SDCARD:
			rootDirectorys.add(mExternalSDCardPath);
			if(isMakeExternalSDCardDirectory || !isExternalSDCardExist()) {
				return;
			}
			isMakeExternalSDCardDirectory = true;
			break;

		case DIRECTORY_INTERNAL_SDCARD:
			for(String path : mInternalSDCardPath) {
				rootDirectorys.add(path);
			}
			if(isMakeInternalSDCardDirectory || !isInternalSDCardExist()) {
				return;
			}
			isMakeInternalSDCardDirectory = true;
			break;

		case DIRECTORY_PHONE:
			rootDirectorys.add(mPhoneDataPath);
			if(isMakePhoneDataDirectroy) {
				return;
			}
			isMakePhoneDataDirectroy = true;
			break;

		default:
			return;
		}

		for(String rootDirectory : rootDirectorys) {
			String appDirectory = rootDirectory + "/" + ROOT_DIRECTORY_NAME;
			makeDirectoryCheck(directoryType, mkdir(appDirectory));
			makeDirectoryCheck(directoryType, mkdir(appDirectory + AUDIO_DIRECTORY_NAME));
			makeDirectoryCheck(directoryType, mkdir(appDirectory + VIDEO_DIRECTORY_NAME));
			makeDirectoryCheck(directoryType, mkdir(appDirectory + TXT_DIRECTORY_NAME));
			makeDirectoryCheck(directoryType, mkdir(appDirectory + LOG_DIRECTORY_NAME));
			makeDirectoryCheck(directoryType, mkdir(appDirectory + IMAGE_DIRECTORY_NAME));
			makeDirectoryCheck(directoryType, mkdir(appDirectory + DATA_DIRECTORY_NAME));
			makeDirectoryCheck(directoryType, mkdir(appDirectory + AVATAR_DIRECTORY_NAME));
			makeDirectoryCheck(directoryType, mkdir(appDirectory + APK_DIRECTORY_NAME));
			makeDirectoryCheck(directoryType, mkdir(appDirectory + OTHER_DIRECTORY_NAME));
		}
	}

	/**
	 * 创建目录检查
	 * @param directoryType
	 * @param mk
	 */
	private void makeDirectoryCheck(int directoryType, boolean mk) {
		if(!mk) {
			switch (directoryType) {
			case DIRECTORY_EXTERNAL_SDCARD:
				isMakeExternalSDCardDirectory = false;
				break;

			case DIRECTORY_INTERNAL_SDCARD:
				isMakeInternalSDCardDirectory = false;
				break;

			case DIRECTORY_PHONE:
				isMakePhoneDataDirectroy = false;
				break;
			}
		}
	}

	/**
	 * 创建目录
	 * @param path
	 * @return
	 */
	private boolean mkdir(String path) {
		File file = new File(path);
		if(!file.exists()) {
			boolean mk = file.mkdir();
			if(mk) {
				//创建成功
			} else {
				//创建失败
			}
			return mk;
		}
		//已经存在
		return true;
	}

	/**
	 * 外置存储卡是否存在
	 * @return
	 */
	public boolean isExternalSDCardExist() {
		boolean bExist = false;
		bExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		if(bExist && getExternalSDCardSpace() > 0) {	//存在,并且空间大于0
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 内置存储卡是否存在
	 * @return
	 */
	private boolean isInternalSDCardExist() {
		boolean bExist = false;
		bExist = mInternalSDCardPath != null && mInternalSDCardPath.size() > 0;
		if(bExist && getInternalSDCardSpace() > 0) {	//存在,并且空间大于0
			return true;
		} else {
			return false;
		}
	}


	/**
	 * 存储目录
	 * @author geolo
	 *
	 */
	private class StorageDirectory {
		public String path;
		public int type;
		public long residualSpace;
		public String rootDirectory;

		public StorageDirectory(String path, int type) {
			this.path = path;
			this.type = type;
		}

		public StorageDirectory(String path, int type, String rootDirectory) {
			this.path = path;
			this.type = type;
			this.rootDirectory = rootDirectory;
		}
	}
	/**
	 * 获取参数类型，返回值保存在Class[]中
	 * @param cls 类
	 * @param mName 方法名字
	 * @return 返回产生类型列表
	 */
	private static Class[] _getParamTypes(Class cls, String mName) {
		Class[] cs = null;

        /*
         * Note: 由于我们一般通过反射机制调用的方法，是非public方法
         * 所以在此处使用了getDeclaredMethods()方法
         */
		Method[] mtd = cls.getDeclaredMethods();
		for (int i = 0; i < mtd.length; i++) {
			if (!mtd[i].getName().equals(mName)) {    // 不是我们需要的参数，则进入下一次循环
				continue;
			}

			cs = mtd[i].getParameterTypes();
		}
		return cs;
	}

	/**
	 * 通过类对象，运行指定方法
	 * @param obj 类对象
	 * @param methodName 方法名
	 * @param params 参数值
	 * @return 失败返回null
	 */
	public static Object invokeByObject(Object obj, String methodName, Object[] params) {
		if(obj == null || TextUtils.isEmpty(methodName)){
			return null;
		}
		Class cls = obj.getClass();
		Object retObject = null;
		try {
			// 根据方法名获取指定方法的参数类型列表
			Class paramTypes[] = _getParamTypes(cls, methodName);
			// 获取指定方法
			Method meth = cls.getMethod(methodName, paramTypes);
			meth.setAccessible(true);

			// 调用指定的方法并获取返回值为Object类型
			if(params == null){
				retObject = meth.invoke(obj);
			} else {
				retObject = meth.invoke(obj, params);
			}
		} catch (Exception e) {
			Log.e("ReflectionUtil.java", "********* invokeByObject() ********", e);
		}

		return retObject;
	}
	
	 /**
     * 获取内置SD卡路径
     * @param context
     * @return
     */
    public static List<String> getInternalSDCardPath(Context context) {
    	List<String> pathes = new ArrayList<String>();
    	StorageManager sm =(StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
    	Object[] ob = (Object[]) invokeByObject(sm, "getVolumeList", null);
    	if(ob != null) {
    		for(Object o : ob) {
        		final String PATH = "mPath=";
        		final String REMOVABLE = "mRemovable=";
        		String path = null;
        		String removable = null;
        		try {
               		path = o.toString();
            		path = path.substring(path.indexOf(PATH));
            		
            		if(path.contains(","))
            		{
            			path = path.substring(PATH.length(), path.indexOf(","));
            		} else if (path.contains(" ")) {
            			path = path.substring(PATH.length(), path.indexOf(" "));
					}
            		
            		
            		removable = o.toString();
            		removable = removable.substring(removable.indexOf(REMOVABLE));
            		
            		if(removable.contains(","))
            		{
                		removable = removable.substring(REMOVABLE.length(), removable.indexOf(","));
            		} else if (removable.contains(" ")) {
                		removable = removable.substring(REMOVABLE.length(), removable.indexOf(" "));
					}
				} catch (Exception e) {
					e.printStackTrace();
					path = null;

				}
				
				if(!TextUtils.isEmpty(path) && !TextUtils.isEmpty(removable) && removable.equalsIgnoreCase("false")){
	        		pathes.add(path);
				}
   
        	}
    	}
    	return pathes;
    }
    
}
