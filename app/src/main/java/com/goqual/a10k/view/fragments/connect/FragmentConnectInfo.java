package com.goqual.a10k.view.fragments.connect;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentConnectInfoBinding;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.interfaces.IConnectFragmentListener;

/**
 * Created by hanwool on 2017. 2. 23..
 */

public class FragmentConnectInfo extends BaseFragment<FragmentConnectInfoBinding> {

    public static FragmentConnectInfo newInstance() {

        Bundle args = new Bundle();

        FragmentConnectInfo fragment = new FragmentConnectInfo();
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentConnectInfo() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_connect_info;
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
        mBinding.setFragment(this);
    }

    public void onBtnClick(View v) {
        switch (v.getId()) {
            case R.id.info_next:
                ((IConnectFragmentListener)getActivity()).changePage(getResources().getInteger(R.integer.frag_select_wifi));
        }
    }
}
