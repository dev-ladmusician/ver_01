package com.goqual.a10k.view.fragments.alarm;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.goqual.a10k.R;
import com.goqual.a10k.model.SwitchManager;
import com.goqual.a10k.view.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ladmusician on 2016. 12. 27..
 */

public class FragmentAlarmSelectSwitchBtn extends BaseFragment {
    public static final String TAG = FragmentAlarmSelectSwitchBtn.class.getSimpleName();

    private static final String SWITCH_POSITION = "switch_position";
    private int mSwitchPosition;

    public static FragmentAlarmSelectSwitchBtn newInstance() {

        Bundle args = new Bundle();

        FragmentAlarmSelectSwitchBtn fragment = new FragmentAlarmSelectSwitchBtn();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_switch_each;
    }

    @Override
    public String getTitle() {
        return null;
    }
}
