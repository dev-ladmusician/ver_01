package com.goqual.a10k.view.adapters.view;

import com.goqual.a10k.view.adapters.interfaces.OnRecyclerItemClickListener;

/**
 * Created by ladmusician on 4/5/16.
 */
public interface AdapterDataView {
    void refresh();
    void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener);
}