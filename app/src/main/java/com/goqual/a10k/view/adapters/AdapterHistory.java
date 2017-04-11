package com.goqual.a10k.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goqual.a10k.R;
import com.goqual.a10k.model.entity.History;
import com.goqual.a10k.presenter.HistoryPresenter;
import com.goqual.a10k.view.adapters.interfaces.OnRecyclerItemClickListener;
import com.goqual.a10k.view.adapters.model.AdapterDataModel;
import com.goqual.a10k.view.adapters.view.AdapterDataView;
import com.goqual.a10k.view.viewholders.HistoryViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HanWool on 2017. 2. 20..
 */

public class AdapterHistory extends RecyclerView.Adapter<HistoryViewHolder>
implements AdapterDataModel<History>, AdapterDataView {

    private static final String TAG = AdapterHistory.class.getSimpleName();
    private List<History> mHistoryList = null;
    private Context mContext = null;
    private OnRecyclerItemClickListener mItemClickListener = null;
    private HistoryPresenter.View<History> mView;

    public AdapterHistory(Context mContext, HistoryPresenter.View<History> view) {
        this.mHistoryList = new ArrayList<>();
        this.mContext = mContext;
        this.mView = view;
    }

    @Override
    public void refresh() {
        notifyDataSetChanged();
    }

    @Override
    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener) {
        this.mItemClickListener = onRecyclerItemClickListener;
    }

    @Override
    public void addItem(History item) {
        this.mHistoryList.add(item);
    }

    @Override
    public void deleteItem(int position) {
        this.mHistoryList.remove(position);
        refresh();
    }

    @Override
    public int getSize() {
        return this.mHistoryList.size();
    }

    @Override
    public void clear() {
        this.mHistoryList.clear();
        refresh();
    }

    @Override
    public History getItem(int position) {
        return this.mHistoryList.get(position);
    }

    @Override
    public List<History> getItems() {
        return this.mHistoryList;
    }

    @Override
    public void updateItem(int position, History item) {
        this.mHistoryList.set(position, item);
        refresh();
    }

    @Override
    public void updateItems(List<History> items) {
        this.mHistoryList.addAll(items);
        refresh();
    }


    @Override
    public int getItemCount() {
        return getSize();
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        holder.bindView(position, mHistoryList.get(position), mItemClickListener);

        if (position == getItemCount() - 1) {
            mView.checkLoadMore();
        }
    }
}
