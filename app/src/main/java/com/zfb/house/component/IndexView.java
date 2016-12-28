package com.zfb.house.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zfb.house.R;

/**
 * 自定义右侧索引View
 * Created by Administrator on 2016/5/18 0018.
 */
public class IndexView extends View {
    //绘制索引字母所需要的画笔
    private Paint mPaint;

    //窗口，浮动View的容器，比Activity的显示更高一层
    private WindowManager mWindowManager;
    //悬浮的View，显示浮动的字体，类似toast
    private View mFloatView;
    //悬浮View的宽度
    private int mFloatWidth;
    //悬浮View的高度
    private int mFloHeight;

    //索引的字母，可以添加自己想要的
    private String mIndex[] = {"?", "#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    //判断是否已经被触摸，默认没有被触摸
    private boolean onTouched = false;
    //字体的大小
    private float mTextSize;
    //测量字体的大小
    private Rect mTextBound;
    //获取View的宽度和高度以平分索引字母
    private int mViewWidth, mViewHeight;

    //声明回调接口
    private OnIndexSelectListener listener;

    public IndexView(Context context) {
        this(context, null);
    }

    public IndexView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();//初始化View
    }

    /**
     * 初始化View
     */
    private void initView() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//Paint.ANTI_ALIAS_FLAG是使位图抗锯齿的标志
        mTextBound = new Rect();

        //设置悬浮选中的索引
        //获取WindowManager
        mWindowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        //通过LayoutInflater获取悬浮的View
        mFloatView = LayoutInflater.from(getContext()).inflate(R.layout.overlay_indexview, null);
        //默认隐藏悬浮的View
        mFloatView.setVisibility(GONE);
        //转换高度和宽度为sp
        mFloatWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, getResources().getDisplayMetrics());
        mFloHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, getResources().getDisplayMetrics());
//        post(new Runnable() {
//            @Override
//            public void run() {
//                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(mFloatWidth, mFloHeight,//高度，宽度
//                        WindowManager.LayoutParams.TYPE_APPLICATION,//应用程序窗口
//                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE//不接受触摸屏事件
//                                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,//不允许获得焦点
//                        PixelFormat.TRANSLUCENT);//窗口半透明显示
//                mWindowManager.addView(mFloatView, layoutParams);//在父窗口中添加悬浮子窗口
//            }
//        });

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (onTouched) {//如果该View被触摸了
            canvas.drawColor(0x30000000);
        }
        for (int i = 0; i < mIndex.length; i++) {//遍历所有的选项
            mPaint.setColor(0xff000000);//设置画笔颜色（字体颜色）
            mPaint.setTextSize(mTextSize * 3.0f / 4.0f);//设置画笔大小（字体大小）
            mPaint.setTypeface(Typeface.DEFAULT);//设置字体样式为默认样式
            mPaint.getTextBounds(mIndex[i], 0, mIndex[i].length(), mTextBound);
            float formX = mViewWidth / 2.0f - mTextBound.width() / 2.0f;
            float formY = mTextSize * i + mTextSize / 2.0f + mTextBound.height() / 2.0f;
            canvas.drawText(mIndex[i], formX, formY, mPaint);
            mPaint.reset();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    /**
     * 测量本身的大小，这里只是测量宽度
     *
     * @param widthMeasureSpec 宽度
     * @return
     */
    private int measureWidth(int widthMeasureSpec) {
        //定义iew的宽度
        int width;
        //获取当前View的测量模式
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        //获取当前View的测量值，这里的得到的只是初步的值，我们需根据测量模式来确定我们期望的大小
        int size = MeasureSpec.getSize(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            width = size;
        } else {//否则，就需要结合Padding的值来确定
            int desire = size + getLeft() + getRight();
            //如果模式为最大模式
            if (mode == MeasureSpec.AT_MOST) {
                width = Math.min(desire, size);
            } else {
                width = desire;
            }
        }
        mViewWidth = width;
        return width;
    }

    /**
     * 测量本身的大小，这里只是测量高度
     *
     * @param heightMeasureSpec 高度
     * @return
     */
    private int measureHeight(int heightMeasureSpec) {
        //定义View的高度
        int height;
        //获取当前View的测量模式
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        //获取当前View的测量值，这里的得到的只是初步的值，我们需根据测量模式来确定我们期望的大小
        int size = MeasureSpec.getSize(heightMeasureSpec);
        //如果模式为精确模式，当前View的高度就是之前得到的size
        if (mode == MeasureSpec.EXACTLY) {
            height = size;
        } else {//否则，就需要结合Padding的值来确定
            int desire = size + getTop() + getBottom();
            //如果模式为最大模式
            if (mode == MeasureSpec.AT_MOST) {
                height = Math.min(desire, size);
            } else {
                height = desire;
            }
        }
        mViewHeight = height;
        mTextSize = mViewHeight * 1.0f / mIndex.length;
        return height;
    }

    //定义一个回调接口
    public interface OnIndexSelectListener {
        //返回选中的位置和对应的索引名
        void OnItemSelect(int position, String value);
    }

    /**
     * 重写触摸事件
     *
     * @param event 事件
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //当前选中位置的纵坐标
        float y = event.getY();
        int index = (int) (y / mTextSize);
        if (index > 0 && index < mIndex.length) {
            mFloatView.setVisibility(VISIBLE);//显示悬浮窗口
            ((TextView) mFloatView).setText(mIndex[index]);//设置悬浮窗口显示的文字
            if (listener != null) {
                listener.OnItemSelect(index, mIndex[index]);
            }
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            onTouched = true;
        } else {
            onTouched = false;
            mFloatView.setVisibility(GONE);
        }
        invalidate();//过滤器他触摸事件
        return true;
    }
}
