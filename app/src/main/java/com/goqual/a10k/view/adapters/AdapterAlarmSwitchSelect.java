package com.goqual.a10k.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goqual.a10k.R;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.view.adapters.interfaces.OnRecyclerItemClickListener;
import com.goqual.a10k.view.adapters.model.AdapterDataModel;
import com.goqual.a10k.view.adapters.view.AdapterDataView;
import com.goqual.a10k.view.viewholders.AlarmSwitchSelectViewHolder;
import com.goqual.a10k.view.viewholders.SwitchViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HanWool on 2017. 2. 20..
 */

public class AdapterAlarmSwitchSelect extends RecyclerView.Adapter<AlarmSwitchSelectViewHolder>
implements AdapterDataModel<Switch>, AdapterDataView{

    private List<Switch> mSwitchList = null;
    private Context mContext = null;
    private OnRecyclerItemClickListener mItemClickListener = null;

    public AdapterAlarmSwitchSelect(Context mContext) {
        this.mSwitchList = new ArrayList<>();
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
    public void addItem(Switch item) {
        this.mSwitchList.add(item);
    }

    @Override
    public void deleteItem(int position) {
        this.mSwitchList.remove(position);
    }

    @Override
    public int getSize() {
        return this.mSwitchList.size();
    }

    @Override
    public void clear() {
        this.mSwitchList.clear();
    }

    @Override
    public Switch getItem(int position) {
        return this.mSwitchList.get(position);
    }

    @Override
    public void updateItem(int position, Switch item) {
        this.mSwitchList.set(position, item);
    }

    @Override
    public void updateItems(List<Switch> items) {
        this.mSwitchList.addAll(items);
    }

    @Override
    public int getItemCount() {
        return getSize();
    }

    @Override
    public AlarmSwitchSelectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_alarm_switch_select, parent, false);
        return new AlarmSwitchSelectViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(AlarmSwitchSelectViewHolder holder, int position) {
        holder.bindView(position, mSwitchList.get(position), mItemClickListener);
    }
}
