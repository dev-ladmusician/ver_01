package com.goqual.a10k.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentMainSwitchListBinding;
import com.goqual.a10k.view.base.BaseFragment;

/**
 * Created by ladmusician on 2017. 2. 20..
 */

public class FragmentMainSetting extends BaseFragment<FragmentMainSwitchListBinding>{
    public static final String TAG = FragmentMainSetting.class.getSimpleName();

    public static FragmentMainSetting newInstance() {
        Bundle args = new Bundle();
        FragmentMainSetting fragment = new FragmentMainSetting();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_setting;
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_switch_each);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
