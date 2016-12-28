package com.zfb.house.ui;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.zfb.house.R;

/**
 * Created by Snekey on 2016/10/18.
 */
@Layout(id = R.layout.activity_layout_tag)
public class LayoutTagActivity extends LemonActivity{

    @FieldView(id = R.id.tv_layout_one)
    private TextView tvLayoutOne;
    @FieldView(id = R.id.tv_layout_two)
    private TextView tvLayoutTwo;
    @FieldView(id = R.id.tv_layout_three)
    private TextView tvLayoutThree;
    @FieldView(id = R.id.tv_layout_four)
    private TextView tvLayoutFour;
    @FieldView(id = R.id.tv_layout_five)
    private TextView tvLayoutFive;
    @FieldView(id = R.id.tv_layout_six)
    private TextView tvLayoutSix;
    @FieldView(id = R.id.tv_layout_other)
    private TextView tvLayoutOther;
    @FieldView(id = R.id.tv_layout_unlimited)
    private TextView tvLayoutUnlimited;

    @Override
    protected void initView() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = ((TextView) v).getText().toString();
                Intent intent = getIntent();
                intent.putExtra("layout", s);
                setResult(RESULT_OK,intent);
                v.setSelected(true);
                finish();
            }
        };
        tvLayoutOne.setOnClickListener(onClickListener);
        tvLayoutTwo.setOnClickListener(onClickListener);
        tvLayoutThree.setOnClickListener(onClickListener);
        tvLayoutFour.setOnClickListener(onClickListener);
        tvLayoutFive.setOnClickListener(onClickListener);
        tvLayoutSix.setOnClickListener(onClickListener);
        tvLayoutOther.setOnClickListener(onClickListener);
        tvLayoutUnlimited.setOnClickListener(onClickListener);
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBask() {
        finish();
    }
}