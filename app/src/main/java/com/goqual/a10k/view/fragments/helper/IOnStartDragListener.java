package com.goqual.a10k.view.fragments.helper;

import android.support.v7.widget.RecyclerView;

/**
 * Created by ladmusician on 2017. 4. 19..
 */

public interface IOnStartDragListener {
    /**
     * Called when a view is requesting a start of a drag.
     *
     * @param viewHolder The holder of the view to drag.
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);
}
