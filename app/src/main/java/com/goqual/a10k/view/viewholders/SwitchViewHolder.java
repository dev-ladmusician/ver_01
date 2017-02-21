package com.goqual.a10k.view.viewholders;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.goqual.a10k.databinding.ItemSwitchBinding;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.view.adapters.interfaces.OnRecyclerItemClickListener;

/**
 * Created by HanWool on 2017. 2. 20..
 */

public class SwitchViewHolder extends RecyclerView.ViewHolder {
    private Context mContext;

    ItemSwitchBinding binding;

    public SwitchViewHolder(View itemView, Context ctx) {
        super(itemView);
        mContext = ctx;
        binding = DataBindingUtil.bind(itemView);
    }

    public void bindView(int position, Switch item, OnRecyclerItemClickListener listener) {
        binding.setItemSwitch(item);
    }
}
