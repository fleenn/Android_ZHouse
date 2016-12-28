package com.zfb.house.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lemon.util.ParamUtils;
import com.zfb.house.R;
import com.zfb.house.model.bean.PlotSearch;

import java.util.List;

/**
 * Created by Snekey on 2016/5/17.
 * 房友圈adpter
 */
public class PlotSearchAdapter extends RecyclerView.Adapter {

    private List<PlotSearch> mDatas;
    private Context mContext;
    private OnPlotClickListener onPlotClickListener;
    private String mInputValue;

    public void setOnPlotClickListener(OnPlotClickListener onPlotClickListener) {
        this.onPlotClickListener = onPlotClickListener;
    }

    public interface OnPlotClickListener {
        void toClickPlot(PlotSearch plotSearch);
    }

    public void setmInputValue(String mInputValue) {
        this.mInputValue = mInputValue;
    }

    public List<PlotSearch> getmDate() {
        return mDatas;
    }

    public PlotSearchAdapter(Context context, List<PlotSearch> datas) {
        this.mContext = context;
        mDatas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plot_search, null);
        return new PlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final PlotViewHolder viewHolder = (PlotViewHolder) holder;
        PlotSearch info = mDatas.get(position);
        String name = info.getName();
        if (name != null && name.contains(mInputValue)) {
            int index = name.indexOf(mInputValue);
            int len = mInputValue.length();
            Spanned temp = Html.fromHtml(name.substring(0, index)
                    + "<font color=#eb6800" +
                    ">"
                    + name.substring(index, index + len) + "</font>"
                    + name.substring(index + len, name.length()));

            viewHolder.tvName.setText(temp);
        } else {
            viewHolder.tvName.setText(name);
        }


    }

    @Override
    public int getItemCount() {
        return ParamUtils.isEmpty(mDatas) ? 0 : mDatas.size();
    }

    class PlotViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;


        public PlotViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tv_plot_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!ParamUtils.isNull(onPlotClickListener)) {
                        onPlotClickListener.toClickPlot(mDatas.get(getAdapterPosition()));
                    }
                }
            });
        }
    }
}
