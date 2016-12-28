package com.zfb.house.component;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Display;
import android.widget.RelativeLayout;

/**
 * Created by geolo on 2015/11/23.
 */
public class InputMethodRelativeLayout extends RelativeLayout {

    private int width;
    private int height;
    private int screenHeight;
    private boolean sizeChanged = false;
    private OnSizeChangedListener onSizeChangedListener;

    private void init(){
        Display localDisplay = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        //screenHeight = localDisplay.getHeight();
        Point point = new Point();
        localDisplay.getSize(point);
        screenHeight = point.y;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.width = widthMeasureSpec;
        this.height = heightMeasureSpec;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //监听不为空、宽度不变、当前高度与历史高度不为0
        if(this.onSizeChangedListener != null && w == oldw && h != 0 && oldh != 0){
            if(h >= oldh || (Math.abs(h - oldh) <= this.screenHeight / 4)){
                sizeChanged = false;
            }else if(h <= oldh || (Math.abs(h - oldh) <= this.screenHeight / 4)){
                sizeChanged = true;
            }
            this.onSizeChangedListener.onSizeChange(sizeChanged);
            measure(this.width - w + getWidth(), this.height - h + getHeight());
        }
    }

    /**
     * @Title: setOnSizeChangedListener
     * @Description: 为当前布局设置onSizeChanged监听器
     * @param sizeChangedListener
     * @return void
     */
    public void setOnSizeChangedListener(OnSizeChangedListener sizeChangedListener) {
        this.onSizeChangedListener = sizeChangedListener;
    }


    public abstract interface OnSizeChangedListener{
        public abstract void onSizeChange(boolean flag);
    }

    /********************************************************/
    public InputMethodRelativeLayout(Context context) {
        super(context);
        init();
    }

    public InputMethodRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InputMethodRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public InputMethodRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
}
