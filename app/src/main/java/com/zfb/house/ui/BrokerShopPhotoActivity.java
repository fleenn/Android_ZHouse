package com.zfb.house.ui;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.PictureSlidePagerAdapter;
import com.zfb.house.component.MyViewPager;

/**
 * 图片浏览页面
 * <p/>
 * Created by HourGlassRemember on 2016/8/31.
 */
@Layout(id = R.layout.activity_broker_shop_photo)
public class BrokerShopPhotoActivity extends LemonActivity {

    //返回
    @FieldView(id = R.id.img_back)
    private ImageView imgBack;
    //第几张/共几张
    @FieldView(id = R.id.txt_number)
    private TextView txtNumber;
    //自定义ViewPager
    @FieldView(id = R.id.mvp_photo)
    private MyViewPager mVpPhoto;
    //传过来的图片URL的数组
    private String[] photoArray;
    private String photo;

    @Override
    protected void initData() {
        photoArray = getIntent().getStringArrayExtra("photoArray");
        photo = getIntent().getStringExtra("photo");
        if (!ParamUtils.isEmpty(photoArray)) {//多张图片
            mVpPhoto.setAdapter(new PictureSlidePagerAdapter(getSupportFragmentManager(), photoArray));
        } else if (!ParamUtils.isEmpty(photo)) {//单张图片
            mVpPhoto.setAdapter(new PictureSlidePagerAdapter(getSupportFragmentManager(), photo));
        }
        mVpPhoto.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (!ParamUtils.isEmpty(photoArray)) {
                    txtNumber.setText(String.valueOf(position + 1) + "/" + photoArray.length);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //设置当前要呈现的图片
        mVpPhoto.setCurrentItem(ParamUtils.isNull(getIntentExtraInt("position")) ? 0 : getIntentExtraInt("position"));
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
