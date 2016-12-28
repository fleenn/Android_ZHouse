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
@Layout(id = R.layout.activity_money_tag)
public class MoneyTagActivity extends LemonActivity{
    @FieldView(id = R.id.tv_money_one)
    private TextView tvMoneyOne;
    @FieldView(id = R.id.tv_money_two)
    private TextView tvMoneyTwo;
    @FieldView(id = R.id.tv_money_three)
    private TextView tvMoneyThree;
    @FieldView(id = R.id.tv_money_four)
    private TextView tvMoneyFour;
    @FieldView(id = R.id.tv_money_five)
    private TextView tvMoneyFive;
    @FieldView(id = R.id.tv_money_six)
    private TextView tvMoneySix;
    @FieldView(id = R.id.tv_money_seven)
    private TextView tvMoneySeven;
    @FieldView(id = R.id.tv_money_eight)
    private TextView tvMoneyEight;
    @FieldView(id = R.id.tv_money_nine)
    private TextView tvMoneyNine;
    @FieldView(id = R.id.tv_money_ten)
    private TextView tvMoneyTen;
    @FieldView(id = R.id.tv_money_eleven)
    private TextView tvMoneyEleven;
    @FieldView(id = R.id.tv_money_twelve)
    private TextView tvMoneyTwelve;
    @FieldView(id = R.id.tv_money_thirteen)
    private TextView tvMoneyUThirteen;
    @FieldView(id = R.id.tv_money_fourteen)
    private TextView tvMoneyFourteen;

    @Override
    protected void initView() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = ((TextView) v).getText().toString();
                Intent intent = getIntent();
                intent.putExtra("money", s);
                setResult(RESULT_OK,intent);
                v.setSelected(true);
                finish();
            }
        };
        tvMoneyOne.setOnClickListener(onClickListener);
        tvMoneyTwo.setOnClickListener(onClickListener);
        tvMoneyThree.setOnClickListener(onClickListener);
        tvMoneyFour.setOnClickListener(onClickListener);
        tvMoneyFive.setOnClickListener(onClickListener);
        tvMoneySix.setOnClickListener(onClickListener);
        tvMoneySeven.setOnClickListener(onClickListener);
        tvMoneyEight.setOnClickListener(onClickListener);
        tvMoneyNine.setOnClickListener(onClickListener);
        tvMoneyTen.setOnClickListener(onClickListener);
        tvMoneyEleven.setOnClickListener(onClickListener);
        tvMoneyTwelve.setOnClickListener(onClickListener);
        tvMoneyUThirteen.setOnClickListener(onClickListener);
        tvMoneyFourteen.setOnClickListener(onClickListener);
    }

    /**
     * 返回
     */
    @OnClick(id = R.id.rlayout_tittle_lt_img)
    public void toBask() {
        finish();
    }
}
