package com.goqual.a10k.view.fragments.connect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentConnectSelectWifiBinding;
import com.goqual.a10k.view.base.BaseFragment;

/**
 * Created by hanwool on 2017. 2. 23..
 */

public class FragmentConnectSelectWifi extends BaseFragment<FragmentConnectSelectWifiBinding> {

    public static FragmentConnectSelectWifi newInstance() {

        Bundle args = new Bundle();

        FragmentConnectSelectWifi fragment = new FragmentConnectSelectWifi();
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentConnectSelectWifi() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_connect_select_wifi;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
