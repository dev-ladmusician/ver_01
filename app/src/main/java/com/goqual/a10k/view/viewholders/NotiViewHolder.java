package com.goqual.a10k.view.viewholders;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.goqual.a10k.databinding.ItemAlarmBinding;
import com.goqual.a10k.databinding.ItemNotiBinding;
import com.goqual.a10k.model.entity.Alarm;
import com.goqual.a10k.model.entity.NotiWrap;
import com.goqual.a10k.view.adapters.interfaces.OnRecyclerItemClickListener;

/**
 * Created by HanWool on 2017. 2. 20..
 */

public class NotiViewHolder extends RecyclerView.ViewHolder {
    private Context mContext;
    ItemNotiBinding binding;

    private int mPosition;
    private OnRecyclerItemClickListener mListener;

    public NotiViewHolder(View itemView, Context ctx) {
        super(itemView);
        mContext = ctx;
        binding = DataBindingUtil.bind(itemView);
        binding.setHolder(this);
    }

    public void bindView(int position, NotiWrap item, OnRecyclerItemClickListener listener) {
        mListener = listener;
        mPosition = position;
        binding.setItem(item);
    }

    public void onBtnClick(View view) {
        mListener.onItemClick(view.getId(), mPosition);
    }
}
