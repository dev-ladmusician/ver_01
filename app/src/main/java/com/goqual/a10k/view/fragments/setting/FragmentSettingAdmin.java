package com.goqual.a10k.view.fragments.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentSettingAdminBinding;
import com.goqual.a10k.view.base.BaseFragment;

/**
 * Created by hanwool on 2017. 2. 28..
 */

public class FragmentSettingAdmin extends BaseFragment<FragmentSettingAdminBinding> {
    public static final String TAG = FragmentSettingAdmin.class.getSimpleName();

    public static FragmentSettingAdmin newInstance() {

        Bundle args = new Bundle();

        FragmentSettingAdmin fragment = new FragmentSettingAdmin();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting_admin;
    }

    @Override
    public String getTitle() {
        return getString(R.string.tab_title_admin);
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
