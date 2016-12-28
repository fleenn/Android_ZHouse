package com.zfb.house.component;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zfb.house.R;
import com.zfb.house.model.bean.ReleaseHousingMultiBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linwenbing on 16/6/7.
 */
public class ReleaseHousingMultiSelectView extends LinearLayout{
    private Context context;
    private List<ReleaseHousingMultiBean> evaList = new ArrayList<>();
    private  FlowLayout flayout;

    public int getSelectNumber() {
        return selectNumber;
    }

    public void setSelectNumber(int selectNumber) {
        this.selectNumber = selectNumber;
    }

    private int selectNumber;
    private int maxSelect = 3;
    public void setMaxSelect(int maxSelect) {
        this.maxSelect = maxSelect;
    }

    public ReleaseHousingMultiSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        //在构造函数中将Xml中定义的布局解析出来。
        LayoutInflater.from(context).inflate(R.layout.item_release_housing_multi_select, this, true);
        flayout = (FlowLayout) findViewById(R.id.fl_select);

    }

    public void setViewData(List<ReleaseHousingMultiBean> p_evaList){
        evaList = p_evaList;
        int size = evaList.size();
        for (int i = 0; i < size; i++) {
            flayout.addView(getTextView(i));
        }

    }


    private View getTextView(final int position){
        final ReleaseHousingMultiBean info = evaList.get(position);

        View itemView = LayoutInflater.from(context).inflate(R.layout.item_release_multi_select_layout, null);
        final TextView markTv = (TextView) itemView.findViewById(R.id.eva_content);
        markTv.setText(info.getTitle());
        if (info.isSelect()) {
            markTv.setBackgroundResource(R.drawable.shape_release_orange);
            markTv.setTextColor(0xffffffff);

        }

        markTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (info.isSelect()) {
                    info.setIsSelect(false);
                    evaList.get(position).setIsSelect(false);
                    selectNumber--;
                } else {
                    if (selectNumber > maxSelect || selectNumber == maxSelect) {
                        Toast.makeText(context, "最多选择" + maxSelect + "项", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    selectNumber++;
                    info.setIsSelect(true);
                    evaList.get(position).setIsSelect(true);
                }

                if (info.isSelect()) {
                    markTv.setBackgroundResource(R.drawable.shape_release_orange);
                    markTv.setTextColor(0xffffffff);

                } else {
                    markTv.setBackgroundResource(R.drawable.shape_release_write);
                    markTv.setTextColor(0xff000000);
                }
            }
        });

        return itemView;
    }

    public String getSelectValue(){
        int size = evaList.size();
        String value = "";
        for (int i = 0;i < size;i++){
            if (evaList.get(i).isSelect()){
                if (TextUtils.isEmpty(value)){
                    value = evaList.get(i).getValue();
                }else{
                    value = value + "," + evaList.get(i).getValue();
                }
            }
        }

        return value;
    }

}
