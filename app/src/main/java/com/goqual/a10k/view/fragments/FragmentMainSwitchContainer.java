package com.goqual.a10k.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentMainSwitchContainerBinding;
import com.goqual.a10k.model.SwitchManager;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.presenter.SocketManager;
import com.goqual.a10k.presenter.SwitchPresenter;
import com.goqual.a10k.presenter.impl.SocketManagerImpl;
import com.goqual.a10k.presenter.impl.SwitchPresenterImpl;
import com.goqual.a10k.view.adapters.AdapterSwitchContainer;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.interfaces.ISwitchOperationListener;
import com.goqual.a10k.view.interfaces.ISwitchRefreshListener;

/**
 * Created by ladmusician on 2017. 2. 20..
 */

public class FragmentMainSwitchContainer extends BaseFragment<FragmentMainSwitchContainerBinding>
implements SwitchPresenter.View<Switch>, ISwitchOperationListener, SocketManager.View {
    private static final String TAG = FragmentMainSwitchContainer.class.getSimpleName();

    private String mTitle = null;

    private AdapterSwitchContainer mPagerAdapter;

    private SwitchPresenterImpl mPresenter;
    private SocketManagerImpl mSocketManager;

    public static FragmentMainSwitchContainer newInstance() {
        
        Bundle args = new Bundle();
        FragmentMainSwitchContainer fragment = new FragmentMainSwitchContainer();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void loadingStart() {

    }

    @Override
    public void loadingStop() {

    }

    @Override
    public void refresh() {
        /**
         * TODO Switch가 많아지면 memory 문제가 발생함
         */
        mBinding.viewPager.setOffscreenPageLimit(SwitchManager.getInstance().getCount());
        mPagerAdapter.refresh();

        // update switch list
        ((ISwitchRefreshListener)mPagerAdapter.getItem(0)).updateSwitches();
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void addItem(Switch item) {
        mPagerAdapter.addItem(item);
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_switch_container;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPagerAdapter.clear();
        SwitchManager.getInstance().clear();
        getPresenter()
                .loadItems();
    }

    private SwitchPresenter getPresenter() {
        if(mPresenter == null) {
            mPresenter = new SwitchPresenterImpl(getActivity(), this, mPagerAdapter);
        }
        return mPresenter;
    }

    private SocketManager getSocketManager() {
        if(mSocketManager == null) {
            mSocketManager = new SocketManagerImpl(this, getActivity());
        }
        return mSocketManager;
    }

    private void initView() {
        mPagerAdapter = new AdapterSwitchContainer(getChildFragmentManager(), getActivity());
        mBinding.viewPager.setAdapter(mPagerAdapter);
        mBinding.viewPager.addOnPageChangeListener(onPageChangeListener);
    }

    @Override
    public void onSwitchClicked(int position, int btnNumber) {
        Switch item = SwitchManager.getInstance().getItem(position);
        getSocketManager().operationOnOff(item, btnNumber);
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            mTitle = ((BaseFragment)mPagerAdapter.getItem(position)).getTitle();
            mActivityInteraction.setTitle(mTitle);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
