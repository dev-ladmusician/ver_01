package com.goqual.a10k.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goqual.a10k.R;
import com.goqual.a10k.model.realm.Nfc;
import com.goqual.a10k.view.adapters.interfaces.OnRecyclerItemClickListener;
import com.goqual.a10k.view.adapters.model.AdapterDataModel;
import com.goqual.a10k.view.adapters.view.AdapterDataView;
import com.goqual.a10k.view.base.BaseActivity;
import com.goqual.a10k.view.viewholders.NfcViewHolder;

import java.util.ArrayList;
import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.RealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by HanWool on 2017. 2. 20..
 */

public class AdapterNfc extends RealmRecyclerViewAdapter<Nfc, NfcViewHolder>
        implements AdapterDataModel<Nfc>, AdapterDataView{

    private Context mContext = null;
    private OnRecyclerItemClickListener mItemClickListener = null;
    private OrderedRealmCollection<Nfc> mCollection;

    public AdapterNfc(Context mContext, OrderedRealmCollection<Nfc> collection) {
        super(collection, true);
        this.mContext = mContext;
        this.mCollection = collection;
    }

    @Override
    public void addItem(Nfc item) {

    }

    @Override
    public void deleteItem(int position) {

    }

    @Override
    public int getSize() {
        return mCollection.size();
    }

    @Override
    public void clear() {

    }

    @Override
    public void updateItem(int position, Nfc item) {

    }

    @Override
    public void updateItems(List<Nfc> items) {

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
        mCollection.get(position).setValues();
        holder.bindView(position, mCollection.get(position), mItemClickListener);
    }

    @Override
    public void onViewDetachedFromWindow(NfcViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    public void setItemState(boolean state) {
        for(Nfc item : mCollection) {
            item.setEditing(state);
        }
        refresh();
    }
}
