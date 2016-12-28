package com.zfb.house.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.model.StatusCode;
import com.zfb.house.R;
import com.zfb.house.adapter.PlotSearchAdapter;
import com.zfb.house.model.bean.PlotSearch;
import com.zfb.house.model.param.SearchPlotParam;
import com.zfb.house.model.result.SearchPlotResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linwenbing on 16/6/14.
 * 小区搜索
 */
@Layout(id = R.layout.activity_plot_search)
public class PlotSearchActivity extends LemonActivity {

    @FieldView(id = R.id.et_search)
    private EditText etSearch;
    @FieldView(id = R.id.rv_plot)
    private RecyclerView rvPlot;

    private PlotSearchAdapter mAdapter;
    private List<PlotSearch> mList;

    /**
     * 返回
     */
    @OnClick(id = R.id.img_title_lt)
    public void toBack() {
        hideKeyWord();
        finish();
    }

    @Override
    protected void initView() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                doSearch(s.toString());
            }
        });

        mList = new ArrayList<>();
        mAdapter = new PlotSearchAdapter(this, mList);
        mAdapter.setOnPlotClickListener(new PlotSearchAdapter.OnPlotClickListener() {
            @Override
            public void toClickPlot(PlotSearch plotSearch) {
                hideKeyWord();
                Intent intent = new Intent();
                intent.putExtra("plot_info", plotSearch);
                Activity activity = (Activity) mContext;
                activity.setResult(Activity.RESULT_OK, intent);
                activity.finish();
            }
        });
        rvPlot.setLayoutManager(new LinearLayoutManager(this));
        rvPlot.setAdapter(mAdapter);
    }

    private void hideKeyWord() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
    }

    private void doSearch(String key) {
        if (TextUtils.isEmpty(key)) {
            mList.clear();
            mAdapter.notifyDataSetChanged();
            return;
        }
        mAdapter.setmInputValue(key);
        SearchPlotParam param = new SearchPlotParam();
        param.setVillageName(key);
        apiManager.getVillageName(param);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onEventMainThread(SearchPlotResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            List<PlotSearch> releasePull = result.getData();
            mList.clear();
            mList.addAll(releasePull);
            mAdapter.notifyDataSetChanged();
        }
    }

}
