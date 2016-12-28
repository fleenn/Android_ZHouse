package com.zfb.house.emchat.temp;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zfb.house.R;


/**
 * Created by JiangWB on 2015/7/14.
 */
public class TitleView extends LinearLayout{

    private View mLayout;
    private ImageView mLeftIcon;
    private ImageView mRightIcon;
    private ImageView mSearchVirtualLayout;
    private TextView mRightTV;
    private TextView mTitleTV;
    private EditText mSearchET;

    private void init(){
        inflate(getContext() , R.layout.title_top_layout , this);
        mLayout = findViewById(R.id.title_top_layout);
        mLeftIcon = (ImageView)findViewById(R.id.title_top_left_icon);
        mRightIcon = (ImageView)findViewById(R.id.title_top_right_icon);
        mRightTV = (TextView)findViewById(R.id.title_top_right_tv);
        mTitleTV = (TextView)findViewById(R.id.title_top_titleTV);
        mSearchET = (EditText)findViewById(R.id.title_top_searchET);
        mSearchVirtualLayout = (ImageView)findViewById(R.id.title_top_searchVirtualLayout);

    }

    public void setBackgroundColor(int color){
        mLayout.setBackgroundColor(color);
    }

    /** 获取输入框控件 */
    public EditText getCenterET(){
        return mSearchET;
    }

    public void setLeftIcon(int resId){
        mLeftIcon.setImageResource(resId);
    }

    public void setRightIcon(int resId){
        mRightIcon.setImageResource(resId);
        mRightIcon.setVisibility(View.VISIBLE);
        mRightTV.setVisibility(View.GONE);
    }

    /** 设置虚拟的搜索布局的切图 */
    public void setTitleCenterImage(int drawbleId){
        mSearchVirtualLayout.setVisibility(View.VISIBLE);
        mSearchVirtualLayout.setImageResource(drawbleId);
        hideSearchET();
        hideTitleTV();
    }

    public void setLeftClickListener(OnClickListener listener){
        mLeftIcon.setOnClickListener(listener);
    }

    public void setTitleTV(CharSequence text){
        hideSearchET();
        mTitleTV.setText(text);
        mTitleTV.setVisibility(View.VISIBLE);
    }

    public void setTitleColor(int color){
        mTitleTV.setTextColor(color);
    }

    public void hideTitleTV(){
        mTitleTV.setVisibility(View.GONE);
    }

    public void hideSearchET(){
        mSearchET.setVisibility(View.GONE);
    }

    public void setRightTV(CharSequence text){
        mRightTV.setText(text);
        mRightTV.setVisibility(View.VISIBLE);
        mRightIcon.setVisibility(View.GONE);
    }

    public void setRightColorTV(int color){
        mRightTV.setTextColor(color);
        mRightTV.setVisibility(View.VISIBLE);
    }

    public void hideRightTV(){
        mRightTV.setVisibility(View.INVISIBLE);
        mRightIcon.setVisibility(View.INVISIBLE);
    }

    public void setRightClickListener(OnClickListener listener){
        mRightTV.setOnClickListener(listener);
        mRightIcon.setOnClickListener(listener);
    }

    public void setCenterClickListener(OnClickListener listener){
        mSearchVirtualLayout.setOnClickListener(listener);
    }

    /***********************************************************************************/
    public TitleView(Context context) {
        super(context);
        init();
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TitleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


}
