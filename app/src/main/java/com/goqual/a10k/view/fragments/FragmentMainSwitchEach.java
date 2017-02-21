package com.goqual.a10k.view.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentMainSwitchEachBinding;
import com.goqual.a10k.databinding.FragmentMainSwitchListBinding;
import com.goqual.a10k.model.SwitchManager;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.view.base.BaseFragment;

/**
 * Created by ladmusician on 2017. 2. 20..
 */

public class FragmentMainSwitchEach extends BaseFragment<FragmentMainSwitchListBinding>{
    public static final String TAG = FragmentMainSwitchEach.class.getSimpleName();
    private final static String ARG_POSITION = "arg_position";
    FragmentMainSwitchEachBinding mBinding;
    private Switch mSwitch = null;

    public static FragmentMainSwitchEach newInstance(int position) {
        Bundle args = new Bundle();
        FragmentMainSwitchEach fragment = new FragmentMainSwitchEach();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_switch_each;
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_switch_each);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int position = getArguments() != null ?
                getArguments().getInt(ARG_POSITION, -1) : -1;

        if (position != -1) {
            mSwitch = SwitchManager.getInstance().getItem(position);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding = DataBindingUtil.bind(view);
        if (mSwitch != null) {
            mBinding.setItemSwitch(mSwitch);
        }
    }


}
