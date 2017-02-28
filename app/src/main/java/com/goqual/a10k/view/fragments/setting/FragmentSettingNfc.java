package com.goqual.a10k.view.fragments.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentSettingNfcBinding;
import com.goqual.a10k.view.base.BaseFragment;

/**
 * Created by hanwool on 2017. 2. 28..
 */

public class FragmentSettingNfc extends BaseFragment<FragmentSettingNfcBinding> {
    public static final String TAG = FragmentSettingNfc.class.getSimpleName();

    public static FragmentSettingNfc newInstance() {

        Bundle args = new Bundle();

        FragmentSettingNfc fragment = new FragmentSettingNfc();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting_nfc;
    }

    @Override
    public String getTitle() {
        return getString(R.string.tab_title_nfc);
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
