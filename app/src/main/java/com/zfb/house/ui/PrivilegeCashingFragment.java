package com.zfb.house.ui;

import com.lemon.LemonFragment;
import com.lemon.annotation.Layout;
import com.zfb.house.R;
import com.zfb.house.adapter.PrivilegeCashingAdapter;

/**
 * 积分商城 -> 兑换中心 -> 特权兑换
 * Created by HourGlassRemember on 2016/8/5.
 */
@Layout(id = R.layout.fragment_cashing_privilege)
public class PrivilegeCashingFragment extends LemonFragment {

    //页数
    public int mPageNo = 1;
    private String token;

    //    //自定义RecyclerView
//    @FieldView(id = R.id.recycler_cashing_privilege)
//    protected LoadMoreRecyclerView recyclerPrivilege;
//    @FieldView(id = R.id.refresh_privilege_layout)
//    protected SwipeRefreshLayout refreshPrivilege;
    //实物兑换适配器
    private PrivilegeCashingAdapter adapter;

    @Override
    protected void initView() {
//        recyclerPrivilege.setHasFixedSize(true);
//        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
//        recyclerPrivilege.setLayoutManager(layoutManager);
//        adapter = new PrivilegeCashingAdapter(getActivity());
//        recyclerPrivilege.setAdapter(adapter);

    }

}
