package com.goqual.a10k.view.customviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.goqual.a10k.databinding.ItemSwitchBtnStateSBinding;
import com.goqual.a10k.model.entity.Switch;

/**
 * Created by ladmusician on 2017. 2. 20..
 */

public class CustomViewBtnStates extends LinearLayout {
    private Switch mSwitch = null;
    private ItemSwitchBtnStateSBinding mBinding = null;

    public CustomViewBtnStates(Context context, Switch item) {
        super(context);
        mSwitch = item;

        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mBinding = ItemSwitchBtnStateSBinding.inflate(inflater);
    }

    public void setStateView(boolean isStateView) {
        mBinding.itemSwitchBtnStateContainer.setVisibility(isStateView ? View.VISIBLE : View.GONE);
    }
}
