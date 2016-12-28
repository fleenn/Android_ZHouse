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
 * Created by Snekey on 2016/10/19.
 */
@Layout(id = R.layout.activity_area_tag)
public class AreaTagActivity extends LemonActivity{
    @FieldView(id = R.id.tv_area_one)
    private TextView tvAreaOne;
    @FieldView(id = R.id.tv_area_two)
    private TextView tvAreaTwo;
    @FieldView(id = R.id.tv_area_three)
    private TextView tvAreaThree;
    @FieldView(id = R.id.tv_area_four)
    private TextView tvAreaFour;
    @FieldView(id = R.id.tv_area_five)
    private TextView tvAreaFive;
    @FieldView(id = R.id.tv_area_six)
    private TextView tvAreaSix;
    @FieldView(id = R.id.tv_area_seven)
    private TextView tvAreaSeven;
    @FieldView(id = R.id.tv_area_eight)
    private TextView tvAreaEight;
    @FieldView(id = R.id.tv_area_unlimited)
    private TextView tvAreaUnlimited;

    @Override
    protected void initView() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = ((TextView) v).getText().toString();
                Intent intent = getIntent();
                intent.putExtra("area", s);
                setResult(RESULT_OK,intent);
                v.setSelected(true);
                finish();
            }
        };
        tvAreaOne.setOnClickListener(onClickListener);
        tvAreaTwo.setOnClickListener(onClickListener);
        tvAreaThree.setOnClickListener(onClickListener);
        tvAreaFour.setOnClickListener(onClickListener);
        tvAreaFive.setOnClickListener(onClickListener);
        tvAreaSix.setOnClickListener(onClickListener);
        tvAreaSeven.setOnClickListener(onClickListener);
        tvAreaEight.setOnClickListener(onClickListener);
        tvAreaUnlimited.setOnClickListener(onClickListener);
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBask() {
        finish();
    }
}
