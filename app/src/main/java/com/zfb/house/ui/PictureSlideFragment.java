package com.zfb.house.ui;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.lemon.LemonFragment;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.zfb.house.R;
import com.zfb.house.component.PhotoView.PhotoViewAttacher;
import com.zfb.house.model.bean.UserBean;

/**
 * 图片浏览里面的Fragment
 * Created by HourGlassRemember on 2016/9/1.
 */
@Layout(id = R.layout.fragment_picture_slide)
public class PictureSlideFragment extends LemonFragment {

    //图片
    @FieldView(id = R.id.img_picture)
    private ImageView imageView;
    //图片路径的URL
    private String url;
    private PhotoViewAttacher mAttacher;

    @Override
    protected void initData() {
        url = getArguments() != null ? getArguments().getString("url") : "";
        mAttacher = new PhotoViewAttacher(imageView);
        Glide.with(getActivity()).load(url.equals("isDefault") ? R.drawable.default_avatar : url.equals("isMine") ? UserBean.getInstance(getActivity()).photo : url)
                .crossFade().into(new GlideDrawableImageViewTarget(imageView) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                super.onResourceReady(resource, animation);
                mAttacher.update();
            }
        });
    }

    /**
     * 单例模式新建一个Fragment
     *
     * @param url 图片的URL
     * @return
     */
    public static PictureSlideFragment newInstance(String url) {
        PictureSlideFragment psf = new PictureSlideFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        psf.setArguments(bundle);
        return psf;
    }

}
