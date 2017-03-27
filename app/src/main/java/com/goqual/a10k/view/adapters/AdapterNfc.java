package com.goqual.a10k.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goqual.a10k.R;
import com.goqual.a10k.model.entity.NfcWrap;
import com.goqual.a10k.model.realm.Nfc;
import com.goqual.a10k.view.adapters.interfaces.OnRecyclerItemClickListener;
import com.goqual.a10k.view.adapters.model.AdapterDataModel;
import com.goqual.a10k.view.adapters.view.AdapterDataView;
import com.goqual.a10k.view.viewholders.NfcViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HanWool on 2017. 2. 20..
 */

public class AdapterNfc extends RecyclerView.Adapter<NfcViewHolder>
        implements AdapterDataModel<NfcWrap>, AdapterDataView{

    private List<NfcWrap> mTagList = null;
    private Context mContext = null;
    private OnRecyclerItemClickListener mItemClickListener = null;

    public AdapterNfc(Context mContext) {
        this.mTagList = new ArrayList<>();
        this.mContext = mContext;
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
    public void addItem(NfcWrap item) {
        this.mTagList.add(item);
    }

    @Override
    public void deleteItem(int position) {
        this.mTagList.remove(position);
    }

    @Override
    public int getSize() {
        return this.mTagList.size();
    }

    @Override
    public void clear() {
        this.mTagList.clear();
    }

    @Override
    public NfcWrap getItem(int position) {
        return this.mTagList.get(position);
    }

    @Override
    public void updateItem(int position, NfcWrap item) {
        this.mTagList.set(position, item);
    }

    @Override
    public void updateItems(List<NfcWrap> items) {
        this.mTagList.addAll(items);
    }

    @Override
    public int getItemCount() {
        return getSize();
    }

    @Override
    public NfcViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_nfc, parent, false);
        return new NfcViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(NfcViewHolder holder, int position) {
        holder.bindView(position, mTagList.get(position), mItemClickListener);
    }

    public void setItemState(boolean state) {
        for(NfcWrap item : mTagList) {
            item.setEditing(state);
        }

        refresh();
    }
}
