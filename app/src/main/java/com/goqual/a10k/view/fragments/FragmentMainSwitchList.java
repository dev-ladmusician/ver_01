package com.goqual.a10k.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentMainSwitchListBinding;
import com.goqual.a10k.view.base.BaseFragment;

/**
 * Created by ladmusician on 2017. 2. 20..
 */

public class FragmentMainSwitchList extends BaseFragment<FragmentMainSwitchListBinding>{
    public static final String TAG = FragmentMainSwitchList.class.getSimpleName();

    public static FragmentMainSwitchList newInstance() {
        Bundle args = new Bundle();
        FragmentMainSwitchList fragment = new FragmentMainSwitchList();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_switch_list;
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_switch_list);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {

    }
}
