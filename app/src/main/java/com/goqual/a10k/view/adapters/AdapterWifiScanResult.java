package com.goqual.a10k.view.adapters;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goqual.a10k.R;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.adapters.interfaces.OnRecyclerItemClickListener;
import com.goqual.a10k.view.adapters.model.AdapterDataModel;
import com.goqual.a10k.view.adapters.view.AdapterDataView;
import com.goqual.a10k.view.viewholders.SwitchViewHolder;
import com.goqual.a10k.view.viewholders.WifiScanResultViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HanWool on 2017. 2. 20..
 */

public class AdapterWifiScanResult extends RecyclerView.Adapter<WifiScanResultViewHolder>
implements AdapterDataModel<ScanResult>, AdapterDataView{

    private List<ScanResult> mScanResult = null;
    private Context mContext = null;
    private OnRecyclerItemClickListener mItemClickListener = null;

    public AdapterWifiScanResult(Context mContext) {
        this.mScanResult = new ArrayList<>();
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
    public void addItem(ScanResult item) {
        this.mScanResult.add(item);
    }

    @Override
    public void deleteItem(int position) {
        this.mScanResult.remove(position);
    }

    @Override
    public int getSize() {
        return this.mScanResult.size();
    }

    @Override
    public void clear() {
        this.mScanResult.clear();
    }

    @Override
    public ScanResult getItem(int position) {
        return this.mScanResult.get(position);
    }

    @Override
    public void updateItem(int position, ScanResult item) {
        this.mScanResult.set(position, item);
    }

    @Override
    public void updateItems(List<ScanResult> items) {
        this.mScanResult.addAll(items);
    }

    @Override
    public int getItemCount() {
        return getSize();
    }

    @Override
    public WifiScanResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_wifi_scan_result, parent, false);
        return new WifiScanResultViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(WifiScanResultViewHolder holder, int position) {
        holder.bindView(position, mScanResult.get(position), mItemClickListener);
    }
}
