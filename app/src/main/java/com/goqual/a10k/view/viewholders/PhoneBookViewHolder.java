package com.goqual.a10k.view.viewholders;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.goqual.a10k.databinding.ItemPhoneBookBinding;
import com.goqual.a10k.model.entity.Phone;
import com.goqual.a10k.view.adapters.interfaces.OnRecyclerItemClickListener;

/**
 * Created by HanWool on 2017. 2. 20..
 */

public class PhoneBookViewHolder extends RecyclerView.ViewHolder {
    private Context mContext;
    ItemPhoneBookBinding binding;

    private int mPosition;
    private OnRecyclerItemClickListener mListener;

    public PhoneBookViewHolder(View itemView, Context ctx) {
        super(itemView);
        mContext = ctx;
        binding = DataBindingUtil.bind(itemView);
        binding.setHolder(this);
    }

    public void bindView(int position, Phone item, OnRecyclerItemClickListener listener) {
        mListener = listener;
        mPosition = position;
        binding.setPhone(item);
    }

    public void onBtnClick(View view) {
        mListener.onItemClick(view.getId(), mPosition);
    }

    public Context getContext() {
        return mContext;
    }
}
