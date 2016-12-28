package com.zfb.house.ui;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.lemon.annotation.OnClick;
import com.lemon.model.StatusCode;
import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.adapter.PlotSearchAdapter;
import com.zfb.house.adapter.SearchHistoryAdapter;
import com.zfb.house.adapter.SearchHotAdapter;
import com.zfb.house.model.SearchHistoryModel;
import com.zfb.house.model.bean.PlotSearch;
import com.zfb.house.model.param.SearchPlotParam;
import com.zfb.house.model.result.SearchPlotResult;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Snekey on 2016/6/8.
 */
@Layout(id = R.layout.activity_search)
public class SearchActivity extends LemonActivity {
    @FieldView(id = R.id.spin_home_search)
    private Spinner spinHomeSearch;
    @FieldView(id = R.id.grid_history_search)
    private GridView gridHistorySearch;
    @FieldView(id = R.id.list_hot_search)
    private GridView listHotSearch;
    @FieldView(id = R.id.et_search)
    private EditText etSearch;
    @FieldView(id = R.id.llayout_search_history)
    private LinearLayout llayoutSearchHistory;
    @FieldView(id = R.id.recycle_search_list)
    private RecyclerView recycleSearchList;

    private SearchHistoryAdapter searchHistoryAdapter;
    private SearchHotAdapter searchHotAdapter;
    private PlotSearchAdapter mPlotSearchAdapter;
    private List<PlotSearch> mList;
    private int brokerType = 1;//1售房专家 2租房专家 3租售专家

    @Override
    protected void initView() {
        spinHomeSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                brokerType = position + 1;
                searchHistoryAdapter.setBrokerType(brokerType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        searchHistoryAdapter = new SearchHistoryAdapter(mContext);
        List<SearchHistoryModel> list = daoManager.queryAllOrderBy(SearchHistoryModel.class, "id", false);
        if (!ParamUtils.isEmpty(list)) {
            searchHistoryAdapter.setData(list);
        }
        gridHistorySearch.setAdapter(searchHistoryAdapter);
        searchHotAdapter = new SearchHotAdapter(mContext);
        mList = new ArrayList<>();
        mPlotSearchAdapter = new PlotSearchAdapter(mContext, mList);
        mPlotSearchAdapter.setOnPlotClickListener(new PlotSearchAdapter.OnPlotClickListener() {
            @Override
            public void toClickPlot(PlotSearch plotSearch) {
                Intent intent = new Intent(mContext, SearchResultActivity.class);
                intent.putExtra("id", plotSearch.getId());
                intent.putExtra("brokerType", brokerType);
                List<SearchHistoryModel> searchHistoryModels = daoManager.queryAll(SearchHistoryModel.class);
                Iterator<SearchHistoryModel> iterator = searchHistoryModels.iterator();
                while (iterator.hasNext()) {
                    SearchHistoryModel model = iterator.next();
                    if (model.getHistoryName().equals(plotSearch.getName())) {
                        try {
                            daoManager.deleteById(SearchHistoryModel.class, "id", String.valueOf(model.getId()));
                            iterator.remove();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (searchHistoryModels.size() == 6) {
                    try {
                        daoManager.deleteById(SearchHistoryModel.class, "id", String.valueOf(searchHistoryModels.get(0).getId()));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                SearchHistoryModel searchHistoryModel = new SearchHistoryModel(plotSearch.getName(), plotSearch.getId());
                daoManager.create(SearchHistoryModel.class, searchHistoryModel);
                startActivity(intent);
            }
        });
        recycleSearchList.setLayoutManager(new LinearLayoutManager(this));
        recycleSearchList.setAdapter(mPlotSearchAdapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (recycleSearchList.getVisibility() == View.GONE) {
                    recycleSearchList.setVisibility(View.VISIBLE);
                    llayoutSearchHistory.setVisibility(View.GONE);
                }
                doSearch(s.toString());
            }
        });
    }

    @OnClick(id = R.id.tv_search_cancel)
    public void toCancel() {
        hideKeyboard();
        finish();
    }

    @OnClick(id = R.id.txt_search_clear)
    public void toDeleteHistory(){
        try {
            daoManager.deleteAll(SearchHistoryModel.class);
            searchHistoryAdapter.setData(null);
            searchHistoryAdapter.notifyDataSetChanged();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void doSearch(String key) {
        if (TextUtils.isEmpty(key)) {
            mList.clear();
            mPlotSearchAdapter.notifyDataSetChanged();
            return;
        }
        mPlotSearchAdapter.setmInputValue(key);
        SearchPlotParam param = new SearchPlotParam();
        param.setVillageName(key);
        apiManager.getVillageName(param);
    }

    public void onEventMainThread(SearchPlotResult result) {
        if (result.getResultCode().equals(StatusCode.SUCCESS.getCode())) {
            List<PlotSearch> releasePull = result.getData();
            mList.clear();
            mList.addAll(releasePull);
            mPlotSearchAdapter.notifyDataSetChanged();
        }
    }
}
