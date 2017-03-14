package com.goqual.a10k.view.viewholders;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.goqual.a10k.databinding.ItemAlarmBinding;
import com.goqual.a10k.model.entity.Alarm;
import com.goqual.a10k.view.adapters.interfaces.OnRecyclerItemClickListener;

/**
 * Created by HanWool on 2017. 2. 20..
 */

public class AlarmViewHolder extends RecyclerView.ViewHolder {
    private Context mContext;
    ItemAlarmBinding binding;

    private int mPosition;
    private OnRecyclerItemClickListener mListener;

    public AlarmViewHolder(View itemView, Context ctx) {
        super(itemView);
        mContext = ctx;
        binding = DataBindingUtil.bind(itemView);
        binding.setHolder(this);
        binding.setCtx(mContext);
    }

    public void bindView(int position, Alarm item, OnRecyclerItemClickListener listener) {
        mListener = listener;
        mPosition = position;
        binding.setItem(item);
    }

    public void onBtnClick(View view) {
        mListener.onItemClick(view.getId(), mPosition);
    }
}
