package com.goqual.a10k.view.fragments.alarm;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goqual.a10k.R;
import com.goqual.a10k.model.SwitchManager;
import com.goqual.a10k.view.base.BaseFragment;

/**
 * Created by ladmusician on 2016. 12. 29..
 */

public class FragmentAlarmSelectSwitch extends BaseFragment {
    public static final String TAG = FragmentAlarmSelectSwitch.class.getSimpleName();

    public static FragmentAlarmSelectSwitch newInstance() {
        
        Bundle args = new Bundle();
        
        FragmentAlarmSelectSwitch fragment = new FragmentAlarmSelectSwitch();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_alarm_select_switch;
    }

    @Override
    public String getTitle() {
        return null;
    }
}
