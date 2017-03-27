package com.goqual.a10k.view.viewholders;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.goqual.a10k.databinding.ItemAlarmBinding;
import com.goqual.a10k.databinding.ItemAlarmSwitchSelectBinding;
import com.goqual.a10k.model.entity.Alarm;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.view.adapters.interfaces.OnRecyclerItemClickListener;

/**
 * Created by HanWool on 2017. 2. 20..
 */

public class AlarmSwitchSelectViewHolder extends RecyclerView.ViewHolder {
    private Context mContext;
    ItemAlarmSwitchSelectBinding binding;

    private int mPosition;
    private OnRecyclerItemClickListener mListener;

    public AlarmSwitchSelectViewHolder(View itemView, Context ctx) {
        super(itemView);
        mContext = ctx;
        binding = DataBindingUtil.bind(itemView);
        binding.setHolder(this);
    }

    public void bindView(int position, Switch item, OnRecyclerItemClickListener listener) {
        mListener = listener;
        mPosition = position;
        binding.setItemSwitch(item);
    }

    public void onBtnClick(View view) {
        mListener.onItemClick(view.getId(), mPosition);
    }
}
