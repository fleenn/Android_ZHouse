package com.zfb.house.component;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.zfb.house.model.bean.PhotoModel;

import java.util.ArrayList;

public class PhotoClassify {
	private ArrayList<PhotoModel> photoModels = new ArrayList<PhotoModel>();
	private ContentResolver cr;

	public PhotoClassify(ContentResolver cr) {
		this.cr = cr;
	}

	public boolean getImageFromPhone() {
		Cursor cursor = null;
		try {
			cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					null, null, null, MediaStore.Images.Media.DATE_MODIFIED
							+ " desc");
			int count = cursor.getCount();
			// cursor.moveToFirst();
			while (cursor.moveToNext()) {
				String path = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
				processSamePath(path);
				Log.i("path", path);

			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		return false;
	}

	private void processSamePath(String path) {
		String rootPath = getRootPath(path);
		if (photoModels.size() == 0) {
			PhotoModel model = getPhotoModel(rootPath, path);
			photoModels.add(model);
		} else {
			boolean isAdd = false;
			for (int i = 0; i < photoModels.size(); i++) {
				if (photoModels.get(i).getRootPath().equals(rootPath)) {

					isAdd = true;
					photoModels.get(i).getList().add(path);
				}
			}
			if (!isAdd) {
				PhotoModel model = getPhotoModel(rootPath, path);
				photoModels.add(model);

			}

		}
	}

	private boolean isSdCardExist() {
		if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment
				.getExternalStorageState())) {
			return true;
		} else
			return false;
	}

	private PhotoModel getPhotoModel(String rootPath, String path) {
		ArrayList<String> list = new ArrayList<String>();
		list.add(path);
		PhotoModel model = new PhotoModel(rootPath, list);
		return model;
	}

	private String getRootPath(String path) {
		String rootPath = "";
		try {
			if (path.contains("/")) {
				int maxlength = path.split("/").length - 1;
				rootPath = path.split("/")[maxlength - 1];

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return rootPath;
	}

	public ArrayList<PhotoModel> getPhotoModels() {
		return photoModels;
	}

	public void setPhotoModels(ArrayList<PhotoModel> photoModels) {
		this.photoModels = photoModels;
	}

}
