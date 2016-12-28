package com.zfb.house.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.lemon.util.DisplayUtils;
import com.zfb.house.R;

/**
 * 自定义右侧滑动条
 */
public class SideBar extends View {
    // 触摸事件
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    // 26个字母，外加一个#号表示特殊字符，放在最后面
    public static String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
    private int choose = -1;// 选中
    //绘制字母的画笔
    private Paint paint = new Paint();
    private TextView mTextDialog;

    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    public SideBar(Context context) {
        this(context, null);
    }

    public SideBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 重写这个方法
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 获取焦点改变背景颜色.
        int height = getHeight();// 获取对应高度
        int width = getWidth(); // 获取对应宽度
        int singleHeight = height / letters.length;// 获取每一个字母的高度

        for (int i = 0; i < letters.length; i++) {
            if (!isInEditMode()) {
                paint.setColor(getResources().getColor(R.color.my_black_two));//设置字体颜色
            }
            paint.setAntiAlias(true);//设置抗锯齿
            //设置侧滑条文字的大小
            paint.setTextSize(DisplayUtils.sp2px(getContext(), 18f));//设置字母字体大小
            // 选中的状态
            if (i == choose) {
                paint.setColor(getResources().getColor(R.color.my_black_two));
                paint.setFakeBoldText(true);
            }
            // x坐标等于中间-字符串宽度的一半.
            float xPos = width / 2 - paint.measureText(letters[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(letters[i], xPos, yPos, paint);//绘制所有的字母
            paint.reset();// 重置画笔
        }

    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();// 点击y坐标
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * letters.length);// 点击y坐标所占总高度的比例 * b数组的长度就等于点击b中的个数.

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //点击时设置半透明的背景色
                setBackgroundColor(Color.parseColor("#33000000"));
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //松开手指时取消背景色
                setBackgroundResource(android.R.color.transparent);
                //重绘SideBar
                invalidate();
                choose = -1;
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(View.INVISIBLE);
                }
                break;
            default:
                if (oldChoose != c) {
                    if (c >= 0 && c < letters.length) {
                        if (listener != null) {
                            listener.onTouchingLetterChanged(letters[c]);
                        }
                        if (mTextDialog != null) {
                            mTextDialog.setText(letters[c]);
                            mTextDialog.setVisibility(View.VISIBLE);
                        }
                        choose = c;
                        //重绘SideBar
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }

    /**
     * 向外公开的方法
     *
     * @param onTouchingLetterChangedListener
     */
    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    /**
     * 接口
     *
     * @author coder
     */
    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s);
    }

}