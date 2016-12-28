package com.zfb.house.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zfb.house.R;

/**
 * 发布房源 选择view
 * Created by linwenbing on 16/6/7.
 */
public class ReleaseHousingSelectView extends LinearLayout {

    //左边文字的值
    private String mTitle;
    //左右两边的文字
    private TextView tvTitle, tvContent;

    public ReleaseHousingSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //在构造函数中将Xml中定义的布局解析出来。
        LayoutInflater.from(context).inflate(R.layout.item_release_housing_select, this, true);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ReleaseHousingSelectView);
        mTitle = attributes.getString(R.styleable.ReleaseHousingSelectView_text);
        tvTitle = (TextView) findViewById(R.id.txt_release_title);
        tvTitle.setText(mTitle);
        tvContent = (TextView) findViewById(R.id.txt_release_content);
        attributes.recycle();
    }

    public void setContent(String pContent) {
        tvContent.setText(pContent);
    }

    public String getContent() {
        if (tvContent != null) {
            return tvContent.getText().toString();
        }
        return "";
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public String getTitle() {
        if (tvTitle != null) {
            return tvTitle.getText().toString();
        }
        return "";
    }

}
