package com.goqual.a10k.view.viewholders;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.goqual.a10k.databinding.ItemHistoryBinding;
import com.goqual.a10k.model.entity.History;
import com.goqual.a10k.view.adapters.interfaces.OnRecyclerItemClickListener;

/**
 * Created by HanWool on 2017. 2. 20..
 */

public class HistoryViewHolder extends RecyclerView.ViewHolder {
    private Context mContext;
    ItemHistoryBinding binding;

    private int mPosition;
    private OnRecyclerItemClickListener mListener;

    public HistoryViewHolder(View itemView, Context ctx) {
        super(itemView);
        mContext = ctx;
        binding = DataBindingUtil.bind(itemView);
        binding.setHolder(this);
    }

    public void bindView(int position, History item, OnRecyclerItemClickListener listener) {
        mListener = listener;
        mPosition = position;
        binding.setItem(item);
    }

    public void onBtnClick(View view) {
        mListener.onItemClick(view.getId(), mPosition);
    }
}
