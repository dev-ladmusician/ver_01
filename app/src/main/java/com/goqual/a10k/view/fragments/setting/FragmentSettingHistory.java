package com.goqual.a10k.view.fragments.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentSettingAdminBinding;
import com.goqual.a10k.databinding.FragmentSettingHistoryBinding;
import com.goqual.a10k.view.base.BaseFragment;

/**
 * Created by hanwool on 2017. 2. 28..
 */

public class FragmentSettingHistory extends BaseFragment<FragmentSettingHistoryBinding> {
    public static final String TAG = FragmentSettingHistory.class.getSimpleName();

    public static FragmentSettingHistory newInstance() {

        Bundle args = new Bundle();

        FragmentSettingHistory fragment = new FragmentSettingHistory();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting_history;
    }

    @Override
    public String getTitle() {
        return getString(R.string.tab_title_history);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView(){
        mBinding.setFragment(this);
    }

}
