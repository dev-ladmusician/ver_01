package com.goqual.a10k.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goqual.a10k.R;
import com.goqual.a10k.model.entity.Alarm;
import com.goqual.a10k.model.entity.NotiWrap;
import com.goqual.a10k.model.realm.Noti;
import com.goqual.a10k.view.adapters.interfaces.OnRecyclerItemClickListener;
import com.goqual.a10k.view.adapters.model.AdapterDataModel;
import com.goqual.a10k.view.adapters.view.AdapterDataView;
import com.goqual.a10k.view.viewholders.AlarmViewHolder;
import com.goqual.a10k.view.viewholders.NotiViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ladmusician on 2016. 12. 9..
 */

public class AdapterNoti extends RecyclerView.Adapter<NotiViewHolder>
        implements AdapterDataModel<NotiWrap>, AdapterDataView {

    private Context mContext = null;
    private List<NotiWrap> mItemList = new ArrayList<>();
    private OnRecyclerItemClickListener mItemClickListener = null;

    public AdapterNoti(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public NotiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_noti, parent, false);
        return new NotiViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(NotiViewHolder holder, int position) {
        holder.bindView(position, mItemList.get(position), mItemClickListener);
    }

    @Override
    public int getItemCount() {
        return getSize();
    }

    @Override
    public void addItem(NotiWrap item) {
        mItemList.add(item);
        refresh();
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
    public NotiWrap getItem(int position) {
        return mItemList.get(position);
    }

    @Override
    public void updateItem(int position, NotiWrap item) {
        mItemList.set(position, item);
    }


    @Override
    public void updateItems(List<NotiWrap> items) {
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
}
