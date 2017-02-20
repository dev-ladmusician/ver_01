package com.goqual.a10k.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentMainSwitchContainerBinding;
import com.goqual.a10k.view.adapters.SwitchContainerPagerAdapter;
import com.goqual.a10k.view.base.BaseFragment;

/**
 * Created by ladmusician on 2017. 2. 20..
 */

public class FragmentMainSwitchContainer extends BaseFragment<FragmentMainSwitchContainerBinding> {
    private static final String TAG = FragmentMainSwitchContainer.class.getSimpleName();

    private String mTitle = null;

    private SwitchContainerPagerAdapter mPagerAdapter;

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

    private void initView() {
        mPagerAdapter = new SwitchContainerPagerAdapter(getFragmentManager(), getActivity());
        mBinding.viewPager.setAdapter(mPagerAdapter);
        mBinding.viewPager.addOnPageChangeListener(onPageChangeListener);
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
