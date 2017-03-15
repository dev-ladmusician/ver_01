package com.goqual.a10k.view.fragments.switches;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentMainSwitchEachBinding;
import com.goqual.a10k.model.SwitchManager;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.activities.ActivitySwitchSetting;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.interfaces.ISwitchOperationListener;

/**
 * Created by ladmusician on 2017. 2. 20..
 */

public class FragmentMainSwitchEach extends BaseFragment<FragmentMainSwitchEachBinding> {
    public static final String TAG = FragmentMainSwitchEach.class.getSimpleName();

    private final static String ARG_POSITION = "arg_position";

    private FragmentMainSwitchEachBinding mBinding = null;
    private ISwitchOperationListener operationListener = null;
    private Switch mSwitch = null;
    private int mSwitchItemPosition;

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
        return String.format("%s: %s", getString(R.string.app_name), mSwitch.getTitle());
    }

    @Override
    public boolean hasToolbarMenus() {
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSwitchItemPosition = -1;
        if(getArguments() != null) {
            mSwitchItemPosition = getArguments().getInt(ARG_POSITION, -1);
        }

        if (mSwitchItemPosition != -1) {
            mSwitch = SwitchManager.getInstance().getItem(mSwitchItemPosition);
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
        switch (view.getId()) {
            case R.id.switch_btn_1:
                operationListener.onSwitchClicked(mSwitchItemPosition, 1);
                break;
            case R.id.switch_btn_2:
                operationListener.onSwitchClicked(mSwitchItemPosition, 2);
                break;
            case R.id.switch_btn_3:
                operationListener.onSwitchClicked(mSwitchItemPosition, 3);
                break;
            case R.id.switch_setting:
                Intent intent = new Intent(getActivity(), ActivitySwitchSetting.class);
                intent.putExtra(ActivitySwitchSetting.ITEM_SWITCH, SwitchManager.getInstance().getPosition(mSwitch));
                startActivity(intent);
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
