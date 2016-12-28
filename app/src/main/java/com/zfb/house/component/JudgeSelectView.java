package com.zfb.house.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.zfb.house.R;

/**
 * Created by linwenbing on 16/6/7.
 * 发布房源 选择view
 */
public class JudgeSelectView extends LinearLayout implements View.OnClickListener{
    public interface OnSlectClick{
        void getResult(int selectType);
    }

    public void setOnSlectClick(OnSlectClick onSlectClick) {
        this.onSlectClick = onSlectClick;
    }

    private OnSlectClick onSlectClick;
    private Button btnOne,btnTwo,btnThree;
    public JudgeSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //在构造函数中将Xml中定义的布局解析出来。
        LayoutInflater.from(context).inflate(R.layout.view_judge_select, this, true);
        btnOne = (Button) findViewById(R.id.select_one);
        btnTwo = (Button) findViewById(R.id.select_two);
        btnThree = (Button) findViewById(R.id.select_three);
        btnOne.setOnClickListener(this);
        btnTwo.setOnClickListener(this);
        btnThree.setOnClickListener(this);

        btnOne.setTextColor(0xffffffff);
        btnOne.setBackgroundResource(R.drawable.shape_release_orange);
        if (onSlectClick != null)
            onSlectClick.getResult(1);
    }

    @Override
    public void onClick(View v) {
        btnOne.setBackgroundResource(R.drawable.shape_release_write);
        btnTwo.setBackgroundResource(R.drawable.shape_release_write);
        btnThree.setBackgroundResource(R.drawable.shape_release_write);
        btnThree.setTextColor(0xff000000);
        btnTwo.setTextColor(0xff000000);
        btnOne.setTextColor(0xff000000);
        btnThree.setTextColor(0xff000000);
        switch (v.getId()){
            case R.id.select_one:
                btnOne.setTextColor(0xffffffff);
                btnOne.setBackgroundResource(R.drawable.shape_release_orange);
                if (onSlectClick != null)
                    onSlectClick.getResult(1);
                break;
            case R.id.select_two:
                btnTwo.setTextColor(0xffffffff);
                btnTwo.setBackgroundResource(R.drawable.shape_release_orange);
                if (onSlectClick != null)
                    onSlectClick.getResult(2);
                break;
            case R.id.select_three:
                btnThree.setTextColor(0xffffffff);
                btnThree.setBackgroundResource(R.drawable.shape_release_orange);
                if (onSlectClick != null)
                    onSlectClick.getResult(3);
                break;
        }
    }
}
