package com.goqual.a10k.view.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentMainSettingBinding;
import com.goqual.a10k.databinding.FragmentMainSwitchListBinding;
import com.goqual.a10k.helper.PreferenceHelper;
import com.goqual.a10k.model.entity.User;
import com.goqual.a10k.presenter.UserPresenter;
import com.goqual.a10k.presenter.impl.UserPresenterImpl;
import com.goqual.a10k.util.Constraint;
import com.goqual.a10k.view.activities.ActivityMain;
import com.goqual.a10k.view.adapters.AdapterUser;
import com.goqual.a10k.view.base.BaseFragment;

/**
 * Created by ladmusician on 2017. 2. 20..
 */

public class FragmentMainSetting extends BaseFragment<FragmentMainSettingBinding>{
    public static final String TAG = FragmentMainSetting.class.getSimpleName();

    private UserPresenter mPresenter;

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
        mBinding.setFragment(this);
    }

    public void onBtnClick(View view) {
        String baseUrl = Constraint.BASE_URL;
        switch (view.getId()) {
            case R.id.setting_logout:
                PreferenceHelper.getInstance(getActivity()).deleteAllValues();
                ((ActivityMain)getActivity()).handleLogin();
                break;
            case R.id.setting_faq:
                baseUrl += "faq";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(baseUrl)));
                break;
            case R.id.setting_noti:
                baseUrl += "noti";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(baseUrl)));
                break;
            case R.id.setting_help:
                baseUrl += "help";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(baseUrl)));
                break;
            case R.id.setting_info:
                baseUrl += "info";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(baseUrl)));
                break;
        }
    }
}
