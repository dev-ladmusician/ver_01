package com.goqual.a10k.view.fragments.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentSettingAbsenceBinding;
import com.goqual.a10k.model.SwitchManager;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.view.base.BaseFragment;

/**
 * Created by hanwool on 2017. 3. 10..
 */

public class FragmentSettingAbsence extends BaseFragment<FragmentSettingAbsenceBinding> {
    public static final String TAG = FragmentSettingAbsence.class.getSimpleName();


    public static final String EXTRA_SWITCH = "EXTRA_SWITCH";
    private Switch mSwitch;

    public static FragmentSettingAbsence newInstance(int item) {

        Bundle args = new Bundle();

        FragmentSettingAbsence fragment = new FragmentSettingAbsence();
        args.putInt(EXTRA_SWITCH, item);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting_absence;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            mSwitch = SwitchManager.getInstance().getItem(getArguments().getInt(EXTRA_SWITCH));
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.setFragment(this);
    }
}
