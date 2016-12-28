package com.zfb.house.component;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.zfb.house.R;
import com.zfb.house.util.ImageUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 自定义的画廊，用于对图片的预览的操作
 *
 * @author geolo
 */
public class GalleryDialog extends Dialog {

    private static final String TAG = "GalleryDialog.java";

    private Button mSaveButton;
    private View mBackBtn;
    private ImageView mDeleteBtn;
    private TextView mPagePointTV;
    private ViewPager mViewPager;
    private List<Uri> mImageUris = new ArrayList<Uri>();
    private ViewPagerAdapter mAdapter;
    private OnDeleteListener mDeleteListener;

    public interface OnDeleteListener {
        void onDeleteImage(Uri uri);
    }

    public GalleryDialog(Context context) {
        this(context, R.style.gallery_dialog);
    }

    public GalleryDialog(Context context, int theme) {
        super(context, theme);
        this.setOnDismissListener(mDismissListener);
        this.setOnCancelListener(mCancelListener);
        setContentView(R.layout.dialog_gallery);

        findViewById(R.id.title_layout_main).setBackgroundColor(0xb2000000);
        mBackBtn = findViewById(R.id.title_layout_left_button);
        mDeleteBtn = (ImageView) findViewById(R.id.title_layout_right_button);
        mPagePointTV = (TextView) findViewById(R.id.title_layout_name_TV);
        mSaveButton = (Button) findViewById(R.id.gallery_dialog_save);
        mDeleteBtn.setImageResource(R.drawable.selector_gallery_delete_btn);

        mViewPager = (ViewPager) findViewById(R.id.gallery_dialog_view_pager);
        mBackBtn.setOnClickListener(mClickListener);
        mDeleteBtn.setOnClickListener(mClickListener);
        mSaveButton.setOnClickListener(mClickListener);
        mSaveButton.setVisibility(View.GONE);
    }

    public void show() {
    }

    public void showGallery(Uri currentUri, Collection<Uri> imageUris, OnDeleteListener deleteListener) {
        try {
            int position = 0;
            int tempPosition = 0;
            mDeleteListener = deleteListener;
            if (mImageUris == null) {
                mImageUris = new ArrayList<Uri>();
            }
            mImageUris.clear();
            for (Uri uri : imageUris) {
                mImageUris.add(uri);
                if (uri == currentUri) {
                    position = tempPosition;
                }
                tempPosition++;
            }
            mAdapter = new ViewPagerAdapter(getContext(), mImageUris);
            mViewPager.addOnPageChangeListener(mOnPageChangeListener);
            mViewPager.setAdapter(mAdapter);
            mViewPager.setEnabled(false);
            mAdapter.notifyDataSetChanged();
            mViewPager.setCurrentItem(position > 0 ? position : 0);
            showPageNumber();
            super.show();
        } catch (Exception e) {
            Log.e(TAG, ">>>>>>>>>> showGallery() <<<<<<<<<<", e);
        }

    }

    public void hideRightButton() {
        if (mDeleteBtn != null) {
            mDeleteBtn.setVisibility(View.GONE);
            mSaveButton.setVisibility(View.GONE);
        }
    }

    public void showSaveButton() {
        mSaveButton.setVisibility(View.VISIBLE);
    }

    private void showPageNumber() {
        int amount = mImageUris.size();
        mPagePointTV.setText(mViewPager.getCurrentItem() + 1 + "/" + amount);
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_layout_left_button:
                    GalleryDialog.this.dismiss();
                    break;
                case R.id.title_layout_right_button://对某个图片进行删除的调用操作
                    try {
                        int mCurrentPosition = mViewPager.getCurrentItem();
                        int amount = mImageUris.size();
                        Uri uri = mImageUris.get(mCurrentPosition);
                        if (amount == 1) {
                            GalleryDialog.this.dismiss();
                        } else {
                            mImageUris.remove(uri);
                            mAdapter.notifyDataSetChanged();
                            if (mCurrentPosition == 0) {
                                mViewPager.setCurrentItem(0);
                                showPageNumber();
                            } else {
                                mViewPager.setCurrentItem(mCurrentPosition - 1);
                            }
                        }
                        mDeleteListener.onDeleteImage(uri);
                    } catch (Exception e) {
                        Log.e(TAG, ">>>>>>>>>> mClickListener -- delete -- btn() <<<<<<<<<<", e);
                    }
                    break;
                case R.id.gallery_dialog_save: {
                    //// TODO: 2015/10/13 没有图片保存功能

                    if (!Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {
                        Toast.makeText(getContext(), "无sd卡", Toast.LENGTH_SHORT).show();
                    } else {
                        final Uri uri = mImageUris.get(mViewPager.getCurrentItem());
                        final String scheme = uri.toString();//getScheme();
                        String savePathString = Environment.getExternalStorageDirectory() + "/ELN/image/";
                        String path = savePathString + uri.getPath().hashCode() + ".jpg";
                        if (new File(path).exists()) {
                            Toast.makeText(getContext(), "图片已保存", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!TextUtils.isEmpty(scheme)) {
                            File file = null;
                            if (scheme.startsWith("http")) {
                                //file = com.nostra13.universalimageloader.core.ImageLoader
                                // .getInstance().getDiscCache().get(uri.toString()+".jpg");
                                new AsyncTask<String, Integer, String>() {
                                    @Override
                                    protected String doInBackground(String... strings) {
                                        FutureTarget<File> future = Glide.with(getContext())
                                                .load(scheme)
                                                .downloadOnly(500, 500);
                                        try {
                                            File file = future.get();
                                            return file.toString();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        } catch (ExecutionException e) {
                                            e.printStackTrace();
                                        }
                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(String s) {
                                        super.onPostExecute(s);
                                        if (!TextUtils.isEmpty(s)) {
                                            String savePathString = Environment.getExternalStorageDirectory() + "/ELN/image/";
                                            CopySdcardFile(s, savePathString + uri.getPath().hashCode() + ".jpg");
//												notifyImageLibraryUpdate(getContext(), savePathString);
//												MLog.e("图片保存位置",savePathString+uri.getPath().hashCode() + ".jpg");
                                            //2016-2-18 图片保存后通知图库更新
                                            notifyImageLibraryUpdate(getContext(), savePathString + uri.getPath().hashCode() + ".jpg");
                                        }
                                    }
                                }.execute("");

                            } else {
                                String pathString = uri.toString().replace("file://", "");
                                file = new File(pathString);
                                CopySdcardFile(file.toString(), savePathString + uri.getPath().hashCode() + ".jpg");
//								notifyImageLibraryUpdate(getContext(), savePathString);
                                //2016-2-18 图片保存后通知图库更新
                                notifyImageLibraryUpdate(getContext(), savePathString + uri.getPath().hashCode() + ".jpg");
                            }

                        }
                    }
                }
                break;
                default:
                    break;
            }
        }
    };

    public static void notifyImageLibraryUpdate(Context context, String path) {
        try {
            Uri data = Uri.parse("file://" + path);
            context.sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, data));
        } catch (Exception e) {
            Log.e("star", "notify image library failed");
        }

    }

    private OnDismissListener mDismissListener = new OnDismissListener() {

        @Override
        public void onDismiss(DialogInterface dialog) {
            try {
                Log.d(TAG, ">>>>>>>>> mDismissListener <<<<<<<<<<<<");
                if (mImageUris != null) {
                    mImageUris.clear();
                    mAdapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                Log.e(TAG, ">>>>>>>>>> mDismissListener -- onDismiss() <<<<<<<<<<", e);
            }

        }
    };

    private OnCancelListener mCancelListener = new OnCancelListener() {

        @Override
        public void onCancel(DialogInterface dialog) {
            mImageUris.clear();
            mImageUris = null;
        }
    };

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            showPageNumber();
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    class ViewPagerAdapter extends PagerAdapter {

        private Context mContext;
        private LayoutInflater mInflater;
        private List<Uri> mImageUris = new ArrayList<Uri>();

        ViewPagerAdapter(Context context, List<Uri> imageUris) {
            mContext = context;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (imageUris != null) {
                mImageUris = imageUris;
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            RelativeLayout relativeLayout = new RelativeLayout(mContext);
//			ZoomImageView zoomImageView = new ZoomImageView(mContext);
//			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
//			relativeLayout.addView(zoomImageView,params);

            RelativeLayout.LayoutParams progressparams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            progressparams.addRule(RelativeLayout.CENTER_IN_PARENT);
            final ImageView loadingImageView = new ImageView(mContext);
            relativeLayout.addView(loadingImageView, progressparams);
            container.addView(relativeLayout);


            try {
                Uri uri = mImageUris.get(position);

//				String path = uri.getPath();
//				String scheme = uri.getScheme();
//				String host = uri.getHost();
//
//				if(!TextUtils.isEmpty(scheme) && scheme.startsWith("http")){
//					new ImageUtil().loadImageByVolley(zoomImageView,
//							scheme +"://"+ host+"/"+path,
//							getContext(),
//							R.drawable.empty_photo,
//							R.drawable.empty_photo);
//				}else{
//					scheme = "file://"+scheme;
//					Bitmap bitmap = MultiMediaUtil.decodeFile(getContext(), uri, 300);
//					if (bitmap == null) {
//						bitmap = BitmapFactory.decodeResource(mContext.getResources() , R.drawable.empty_photo);
//					}
//					zoomImageView.setImageBitmap(bitmap);
//
//				}

                new ImageUtil().loadImageByVolley(loadingImageView,
                        uri.toString(),
                        getContext(),
                        R.drawable.loading_picture,
                        R.drawable.empty_photo);

            } catch (Exception e) {
                Log.e(TAG, ">>>>>>>>>> ViewPagerAdapter.java -- instantiateItem() <<<<<<<<<<", e);
            }

            return relativeLayout;
        }

        @Override
        public int getCount() {
            return mImageUris.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

        @Override
        public int getItemPosition(Object object) {
            /* 1. 即使调用了galleryAdapter.notifyDataSetChanged();但是ViewPager还是不会更新原来的数据。
             * 2. 注意：POSITION_NONE 是一个PagerAdapter的内部常量，值是-2，
			 * 3. 可以更新数据了
			 * */
            return POSITION_NONE;
        }

    }


    //文件拷贝
    //要复制的目录下的所有非子目录(文件夹)文件拷贝
    public int CopySdcardFile(String fromFile, String toFile) {

        try {
            File filetoFile = new File(toFile);
            if (!filetoFile.getParentFile().exists()) {
                filetoFile.getParentFile().mkdirs();
            }
            if (filetoFile.exists()) {
                Toast.makeText(getContext(), "已保存图片到:" + toFile, Toast.LENGTH_SHORT).show();
                return 0;
            } else {
                filetoFile.createNewFile();
            }


            InputStream fosfrom = new FileInputStream(fromFile);
            FileOutputStream fosto = new FileOutputStream(filetoFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            fosto.flush();
            fosfrom.close();
            fosto.close();
            Toast.makeText(getContext(), "保存图片到:" + toFile, Toast.LENGTH_SHORT).show();
            return 0;
        } catch (Exception ex) {
            Log.e(TAG, "xxxxxxxx:" + ex.toString());
            return -1;
        }
    }

}
