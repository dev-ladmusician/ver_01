package com.goqual.a10k.view.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentMainSwitchEachBinding;
import com.goqual.a10k.databinding.FragmentMainSwitchListBinding;
import com.goqual.a10k.model.SwitchManager;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.interfaces.ISwitchOperationListener;

/**
 * Created by ladmusician on 2017. 2. 20..
 */

public class FragmentMainSwitchEach extends BaseFragment<FragmentMainSwitchListBinding>{
    public static final String TAG = FragmentMainSwitchEach.class.getSimpleName();

    private final static String ARG_POSITION = "arg_position";

    private FragmentMainSwitchEachBinding mBinding = null;
    private ISwitchOperationListener operationListener = null;
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
        int position = -1;
        if(getArguments() != null) {
            position = getArguments().getInt(ARG_POSITION, -1);
        }

        if (position != -1) {
            mSwitch = SwitchManager.getInstance().getItem(position);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        operationListener = (ISwitchOperationListener) getParentFragment();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void onBtnClick(View view) {
        LogUtil.d(TAG, ""+view.getId());
        /**
         * TODO: onSwitchClicked에서 스위치를 넘기는 방법을 고민할 필요가 있을 듯
         */
        switch (view.getId()) {
            case R.id.switch_btn_1:
                operationListener.onSwitchClicked(SwitchManager.getInstance().getPosition(mSwitch), 1);
                break;
            case R.id.switch_btn_2:
                operationListener.onSwitchClicked(SwitchManager.getInstance().getPosition(mSwitch), 2);
                break;
            case R.id.switch_btn_3:
                operationListener.onSwitchClicked(SwitchManager.getInstance().getPosition(mSwitch), 3);
                break;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding = DataBindingUtil.bind(view);
        if (mSwitch != null) {
            mBinding.setItemSwitch(mSwitch);
            mBinding.setFragment(this);
        }
    }


}
