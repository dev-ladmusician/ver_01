package com.goqual.a10k.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goqual.a10k.R;
import com.goqual.a10k.model.entity.Phone;
import com.goqual.a10k.model.entity.Phone;
import com.goqual.a10k.view.adapters.interfaces.OnRecyclerItemClickListener;
import com.goqual.a10k.view.adapters.model.AdapterDataModel;
import com.goqual.a10k.view.adapters.view.AdapterDataView;
import com.goqual.a10k.view.viewholders.NfcViewHolder;
import com.goqual.a10k.view.viewholders.PhoneBookViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HanWool on 2017. 2. 20..
 */

public class AdapterPhoneBook extends RecyclerView.Adapter<PhoneBookViewHolder>
        implements AdapterDataModel<Phone>, AdapterDataView{

    private List<Phone> mTagList = null;
    private Context mContext = null;
    private OnRecyclerItemClickListener mItemClickListener = null;

    public AdapterPhoneBook(Context mContext) {
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
    public void addItem(Phone item) {
        this.mTagList.add(item);
        refresh();
    }

    @Override
    public void deleteItem(int position) {
        this.mTagList.remove(position);
        refresh();
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
    public Phone getItem(int position) {
        return this.mTagList.get(position);
    }

    @Override
    public void updateItem(int position, Phone item) {
        this.mTagList.set(position, item);
    }

    @Override
    public void updateItems(List<Phone> items) {
        this.mTagList.addAll(items);
    }

    @Override
    public int getItemCount() {
        return getSize();
    }

    @Override
    public PhoneBookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_phone_book, parent, false);
        return new PhoneBookViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(PhoneBookViewHolder holder, int position) {
        holder.bindView(position, mTagList.get(position), mItemClickListener);
    }
}
