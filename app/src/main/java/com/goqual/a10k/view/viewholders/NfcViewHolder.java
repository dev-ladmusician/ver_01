package com.goqual.a10k.view.viewholders;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.goqual.a10k.databinding.ItemNfcBinding;
import com.goqual.a10k.model.realm.Nfc;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.adapters.interfaces.OnRecyclerItemClickListener;

/**
 * Created by HanWool on 2017. 2. 20..
 */

public class NfcViewHolder extends RecyclerView.ViewHolder {
    private Context mContext;
    ItemNfcBinding binding;

    private int mPosition;
    private OnRecyclerItemClickListener mListener;

    public NfcViewHolder(View itemView, Context ctx) {
        super(itemView);
        mContext = ctx;
        binding = DataBindingUtil.bind(itemView);
        binding.setHolder(this);
    }

    public void bindView(int position, Nfc item, OnRecyclerItemClickListener listener) {
        LogUtil.e("NfcViewHolder", item.toString());
        mListener = listener;
        mPosition = position;
        binding.setItemNfc(item);
    }

    public void onBtnClick(View view) {
        mListener.onItemClick(view.getId(), mPosition);
    }
}
