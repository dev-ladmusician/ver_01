package com.goqual.a10k.view.viewholders;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.wifi.ScanResult;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.goqual.a10k.databinding.ItemSwitchBinding;
import com.goqual.a10k.databinding.ItemWifiScanResultBinding;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.view.adapters.interfaces.OnRecyclerItemClickListener;

/**
 * Created by HanWool on 2017. 2. 20..
 */

public class WifiScanResultViewHolder extends RecyclerView.ViewHolder {
    private Context mContext;
    ItemWifiScanResultBinding binding;

    private int mPosition;
    private OnRecyclerItemClickListener mListener;

    public WifiScanResultViewHolder(View itemView, Context ctx) {
        super(itemView);
        mContext = ctx;
        binding = DataBindingUtil.bind(itemView);
        binding.setHolder(this);
    }

    public void bindView(int position, ScanResult item, OnRecyclerItemClickListener listener) {
        mListener = listener;
        mPosition = position;
        binding.setResult(item);
    }

    public void onBtnClick(View view) {
        mListener.onItemClick(view.getId(), mPosition);
    }
}
