package com.zfb.house.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.lemon.util.DisplayUtils;
import com.zfb.house.R;

/**
 * 自定义进度条（任务中心 -> 每月任务）
 * Created by Administrator on 2016/8/17.
 */
public class CustomerProgress extends View {

    private int grayPaint;
    private int orangePaint;
    private Paint paintBack;//背景色画笔
    private Paint paintProgress;//进度色画笔
    private Paint paintText;//字体颜色画笔
    private Rect mBound;//用于获取字体的大小
    private int totalValue;//总进度值
    private int progressValue;//当前进度值

    public CustomerProgress(Context context) {
        this(context, null);
    }

    public CustomerProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomerProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomerProgress, defStyleAttr, 0); //获取自定义的属性值组
        //获取自定义的属性值，设置默认颜色
        grayPaint = typedArray.getColor(R.styleable.CustomerProgress_backColor, getResources().getColor(R.color.underline_color_low));
        orangePaint = typedArray.getColor(R.styleable.CustomerProgress_progressColor, getResources().getColor(R.color.my_orange_two));
        typedArray.recycle(); //用完记得释放掉
        initPaint();//初始化画笔
        mBound = new Rect();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        //设置灰色背景框的画笔属性
        paintBack = new Paint();
        paintBack.setColor(grayPaint);
        paintBack.setDither(true);//设置防抖动

        //设置橘色进度条的画笔属性
        paintProgress = new Paint();
        paintProgress.setColor(orangePaint);
        paintProgress.setDither(true);

        //设置白色文字的画笔属性
        paintText = new Paint();
        paintText.setDither(true);
        paintText.setAntiAlias(true);
        paintText.setColor(Color.WHITE);//设置文字颜色
        paintText.setTextSize(DisplayUtils.sp2px(getContext(), 12f));//设置文字大小
    }

    /**
     * 重写onDraw来画进度条
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();//获取自定义View的宽度
        int height = getHeight();//获取自定义View的高度

        //画背景色
        canvas.drawRect(0, 0, width, height, paintBack);

        //画进度条颜色
        if (totalValue != 0) {
            canvas.drawRect(0, 0, progressValue * width / totalValue, height, paintProgress);
        }

        //画“进度”两个字
        String text = "进度";
        paintText.getTextBounds(text, 0, text.length(), mBound);
        canvas.drawText(text, width - paintText.measureText(text) - 4, -paintText.getFontMetrics().ascent + 4, paintText);

        float textBaselineY = height - paintText.getFontMetrics().bottom - 4;
        //画“总进度值”
        String totalValueText = String.valueOf(this.totalValue);
        float textCenterX = width - paintText.measureText(totalValueText) - 4;
        paintText.getTextBounds(totalValueText, 0, totalValueText.length(), mBound);
        canvas.drawText(totalValueText, textCenterX, textBaselineY, paintText);

        //画“/”（斜杠）
        String splitText = "/";
        paintText.getTextBounds(splitText, 0, splitText.length(), mBound);
        canvas.drawText(splitText, textCenterX - paintText.measureText(splitText), textBaselineY, paintText);

        //画“当前进度值”
        String progressValueText = String.valueOf(this.progressValue);
        paintText.getTextBounds(progressValueText, 0, progressValueText.length(), mBound);
        canvas.drawText(progressValueText, textCenterX - paintText.measureText(splitText) - paintText.measureText(progressValueText), textBaselineY, paintText);
    }

    /**
     * 设置总进度值
     *
     * @param totalValue 总进度值
     */
    public void setTotalValue(int totalValue) {
        this.totalValue = totalValue;
        postInvalidate();//刷新View
    }

    /**
     * 设置当前进度值
     *
     * @param progressValue 当前进度值
     */
    public void setProgressValue(int progressValue) {
        this.progressValue = progressValue;
        postInvalidate();//刷新View
    }

}
