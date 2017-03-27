package com.goqual.a10k.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goqual.a10k.R;
import com.goqual.a10k.model.entity.Alarm;
import com.goqual.a10k.view.adapters.interfaces.OnRecyclerItemClickListener;
import com.goqual.a10k.view.adapters.model.AdapterDataModel;
import com.goqual.a10k.view.adapters.view.AdapterDataView;
import com.goqual.a10k.view.viewholders.AlarmViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ladmusician on 2016. 12. 9..
 */

public class AdapterAlarm extends RecyclerView.Adapter<AlarmViewHolder>
        implements AdapterDataModel<Alarm>, AdapterDataView {

    private Context mContext = null;
    private List<Alarm> mItemList = new ArrayList<>();
    private OnRecyclerItemClickListener mItemClickListener = null;

    public AdapterAlarm(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_alarm, parent, false);
        return new AlarmViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(AlarmViewHolder holder, int position) {
        holder.bindView(position, mItemList.get(position), mItemClickListener);
    }

    @Override
    public int getItemCount() {
        return getSize();
    }

    @Override
    public void addItem(Alarm item) {
        mItemList.add(item);
    }

    @Override
    public void deleteItem(int position) {
        mItemList.remove(position);
    }

    @Override
    public int getSize() {
        return mItemList.size();
    }

    @Override
    public void clear() {
        mItemList.clear();
    }

    @Override
    public Alarm getItem(int position) {
        return mItemList.get(position);
    }

    @Override
    public void updateItem(int position, Alarm item) {
        mItemList.set(position, item);
    }


    @Override
    public void updateItems(List<Alarm> items) {
        clear();
        mItemList.addAll(items);
    }

    @Override
    public void refresh() {
        notifyDataSetChanged();
    }

    @Override
    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener) {
        this.mItemClickListener = onRecyclerItemClickListener;
    }

    public void setDeletable(boolean state) {
        for (Alarm each : mItemList) {
            each.setmIsDeletable(state);
        }
        refresh();
    }
}
