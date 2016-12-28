package com.zfb.house.component;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.zfb.house.R;

/**
 * 加载对话框
 */
public class LoadDialog extends Dialog {

    private TextView mTipTextView;

    private String tip = "";

    public void setText(String s) {
        mTipTextView.setText(s);
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public LoadDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public LoadDialog(Context context, int theme) {
        super(context, theme);
    }

    public LoadDialog(Context context) {
        super(context, R.style.loading_dialog_themes);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_loading);
        mTipTextView = (TextView) findViewById(R.id.loading_tip);
    }

    @Override
    public void onBackPressed() {

    }

}
