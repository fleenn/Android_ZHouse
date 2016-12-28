package com.zfb.house.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zfb.house.R;

/**
 * 发布房源 编辑view
 * Created by linwenbing on 16/6/7.
 */
public class ReleaseHousingEditView extends LinearLayout {

    private String mTitle, mUnit, mHint;
    //左边文字
    private TextView tvTitle;
    //右边文字
    private EditText tvContent;
    //右边文字的单位
    private TextView tvUnit;

    public ReleaseHousingEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //在构造函数中将Xml中定义的布局解析出来。
        LayoutInflater.from(context).inflate(R.layout.item_release_housing_edit, this, true);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ReleaseHousingEditView);
        mTitle = attributes.getString(R.styleable.ReleaseHousingEditView_title_text);
        mUnit = attributes.getString(R.styleable.ReleaseHousingEditView_unit_text);
        mHint = attributes.getString(R.styleable.ReleaseHousingEditView_content_hint);
        tvTitle = (TextView) findViewById(R.id.txt_release_title);
        tvContent = (EditText) findViewById(R.id.txt_release_content);
        tvUnit = (TextView) findViewById(R.id.txt_release_unit);
        tvTitle.setText(mTitle);
        tvContent.setHint(mHint);
        if (!TextUtils.isEmpty(mUnit)) {
            tvUnit.setVisibility(View.VISIBLE);
            tvUnit.setText(mUnit);
        }
        //将光标移至文字末尾
        setCursor(tvContent);
        attributes.recycle();
    }

    /**
     * 将光标移至文字末尾
     *
     * @param editText
     */
    private void setCursor(final EditText editText) {
        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.i("linwb", "foucus = " + hasFocus);
                if (hasFocus) {
                    if (!TextUtils.isEmpty(editText.getText().toString())) {
                        Editable text = editText.getText();
                        Spannable spanText = text;
                        Selection.setSelection(spanText, text.length());
                    }
                }
            }
        });
    }

    public void setContent(String pContent) {
        tvContent.setText(pContent);
    }

    public String getContent() {
        return tvContent.getText().toString();
    }

    public void setUnit(String unit) {
        tvUnit.setText(unit);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public String getTitle() {
        return tvTitle.getText().toString();
    }

    /**
     * 设置右边值的输入类型
     *
     * @param type 输入类型
     */
    public void setInputType(int type) {
        tvContent.setInputType(type);
    }

}
