package com.zfb.house.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lemon.util.ParamUtils;
import com.zfb.house.R;

import java.math.BigDecimal;

/**
 * 自定义RatingBar类，该类继承LinearLayout
 * Created by Administrator on 2016/4/29
 */
public class RatingBar extends LinearLayout {
    private String TAG = "RatingBar";

    private int starCount;//星星数量
    private float starImageSize;//星星大小
    private boolean startClickable;//是否可以被点击
    private Drawable starFillDrawable;//整颗星星
    private Drawable starHalfDrawable;//半颗星星
    private Drawable starEmptyDrawable;//星星背景

    private Changed changed;
    private int position;
//    private OnRatingChangeListener onRatingChangeListener;

    /**
     * 自定义RatingBar的构造函数
     *
     * @param context 上下文环境
     * @param attrs   自定义属性
     */
    public RatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);//设置排列方向为水平排列
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingBar);//关联自定义属性
        starCount = typedArray.getInt(R.styleable.RatingBar_starCount, 5);
        starImageSize = typedArray.getDimension(R.styleable.RatingBar_starImageSize, 20);
        startClickable = typedArray.getBoolean(R.styleable.RatingBar_startClickable, false);
        starFillDrawable = typedArray.getDrawable(R.styleable.RatingBar_starFill);
        starHalfDrawable = typedArray.getDrawable(R.styleable.RatingBar_starHalf);
        starEmptyDrawable = typedArray.getDrawable(R.styleable.RatingBar_starEmpty);

        for (int i = 0; i < starCount; i++) {
            ImageView imageView = getStarImageView(context);//获得一个ImageView
            //设置imageView的点击事件
//            imageView.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (startClickable) {//如果星星被点击了
//                        position += 0.5f;
//                        setStar(indexOfChild(v) + position);
//                        if (onRatingChangeListener != null) {
//                            onRatingChangeListener.onRatingChange(indexOfChild(v) + position);
//                        }
//                        if (position == 1) {
//                            position = 0;
//                        }
//                    }
//                }
//            });
            addView(imageView);//添加imageView
        }

    }

    /**
     * 获得星星的图像视图
     *
     * @param context 上下文环境
     * @return
     */
    private ImageView getStarImageView(Context context) {
        ImageView imageView = new ImageView(context);
        //创建一个宽高都为starImageSize的布局参数LayoutParams
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(Math.round(starImageSize), Math.round(starImageSize));
        imageView.setLayoutParams(params);//设置imageView关联的布局参数
        imageView.setPadding(0, 0, 5, 0);//设置imageView的内边距（顺序为：左、上、右、下）
        imageView.setMaxWidth(10);//设置imageView最大宽度
        imageView.setMaxHeight(10);//设置imageView最大高度
        return imageView;
    }

    /**
     * 设置星星的大小
     *
     * @param starImageSize
     */
    public void setStarImageSize(float starImageSize) {
        this.starImageSize = starImageSize;
    }

    /**
     * 设置星星是否可以被点击
     *
     * @param startClickable
     */
    public void setStartClickable(boolean startClickable) {
        this.startClickable = startClickable;
    }

    /**
     * 设置整颗星星的资源
     *
     * @param starFillDrawable
     */
    public void setStarFillDrawable(Drawable starFillDrawable) {
        this.starFillDrawable = starFillDrawable;
    }

    /**
     * 设置半颗星星的资源
     *
     * @param starHalfDrawable
     */
    public void setStarHalfDrawable(Drawable starHalfDrawable) {
        this.starHalfDrawable = starHalfDrawable;
    }

    /**
     * 设置星星背景的资源
     *
     * @param starEmptyDrawable
     */
    public void setStarEmptyDrawable(Drawable starEmptyDrawable) {
        this.starEmptyDrawable = starEmptyDrawable;
    }

    /**
     * 设置需要改变的数据
     *
     * @return
     */
    public Changed getChanged() {
        return changed;
    }

    /**
     * 获得需要改变的数据
     *
     * @param changed
     */
    public void setChanged(Changed changed) {
        this.changed = changed;
    }

    /**
     * 设置星星
     *
     * @param starNumber 星星数量
     */
    public void setStar(String starNumber) {
        float starCount = ParamUtils.isEmpty(starNumber) ? 1 : Float.parseFloat(starNumber) < 1 ? 1 : Float.parseFloat(starNumber);
        clearStar();
        int integral = (int) starCount;//整数部分
        BigDecimal b1 = new BigDecimal(Float.toString(starCount));
        BigDecimal b2 = new BigDecimal(Integer.toString(integral));
        float decimal = b1.subtract(b2).floatValue();//小数部分

        starCount = integral < this.starCount ? integral : this.starCount;
        starCount = starCount > 0 ? starCount : 0;

        if (changed != null && changed.getPosition() >= 0) {
            if (b1.floatValue() <= changed.getPosition() && changed.changeDrawable != null) {
                for (int i = 0; i < starCount; i++) {
                    ((ImageView) getChildAt(i)).setImageDrawable(changed.changeDrawable);
                }
            } else {
                for (int i = 0; i < starCount; i++) {
                    ((ImageView) getChildAt(i)).setImageDrawable(starFillDrawable);
                }
            }
        } else {
            for (int i = 0; i < starCount; i++) {
                ((ImageView) getChildAt(i)).setImageDrawable(starFillDrawable);
            }
        }

        if (decimal > 0) {
            if (changed != null && changed.getPosition() >= 0) {
                if (b1.floatValue() <= changed.getPosition() && changed.changeDrawable != null) {
                    ((ImageView) getChildAt(integral)).setImageDrawable(changed.changeHalfDrawable);
                } else {
                    ((ImageView) getChildAt(integral)).setImageDrawable(starHalfDrawable);
                }
            } else {
                ((ImageView) getChildAt(integral)).setImageDrawable(starHalfDrawable);
            }
        }

    }

    private void clearStar() {
        for (int i = 0; i < 5; i++) {
            ((ImageView) getChildAt(i)).setImageDrawable(null);
        }
    }

//    public interface OnRatingChangeListener {
//        void onRatingChange(float ratingCount);
//    }

    /**
     * 当星星被点击时候的回调
     *
     * @param onRatingChangeListener
     */
//    public void setOnRatingChangeListener(OnRatingChangeListener onRatingChangeListener) {
//        this.onRatingChangeListener = onRatingChangeListener;
//    }

    /**
     * 需要改变的图片和图片的下标
     */
    private class Changed {
        private int position = -1;//图片下标
        private Drawable changeDrawable;//整颗星星的资源
        private Drawable changeHalfDrawable;//半颗星星的资源

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public Drawable getChangeDrawable() {
            return changeDrawable;
        }

        public void setChangeDrawable(Drawable changeDrawable) {
            this.changeDrawable = changeDrawable;
        }

        public Drawable getChangeHalfDrawable() {
            return changeHalfDrawable;
        }

        public void setChangeHalfDrawable(Drawable changeHalfDrawable) {
            this.changeHalfDrawable = changeHalfDrawable;
        }
    }


}
