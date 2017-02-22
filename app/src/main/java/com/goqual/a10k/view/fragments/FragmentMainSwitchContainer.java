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
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.util.event.EventSwitchEdit;
import com.goqual.a10k.util.event.EventToolbarClick;
import com.goqual.a10k.util.event.RxBus;
import com.goqual.a10k.view.adapters.AdapterSwitchContainer;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.interfaces.ISwitchOperationListener;
import com.goqual.a10k.view.interfaces.ISwitchRefreshListener;
import com.goqual.a10k.view.interfaces.IToolbarClickListener;

import rx.functions.Action1;

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

    private int mCurrentPage = 0;

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

        LogUtil.e(TAG, "CURRENT PAGE :: " + mCurrentPage);
        mBinding.viewPager.setCurrentItem(mCurrentPage);

        RxBus.getInstance().send(
                SwitchManager.getInstance().getCount() == 0 ?
                        new EventSwitchEdit(EventSwitchEdit.STATUS.HIDE) :
                        new EventSwitchEdit(EventSwitchEdit.STATUS.EDIT));
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
        mPagerAdapter.refresh();

        SwitchManager.getInstance().clear();
        getPresenter()
                .loadItems();
    }

    /**
     * list clear했는데도 불구하고 viewPager가 view를 다시 그려서
     * 항상 존재하는 switchListFragment로 고정
     * loadItems가 끝난 후 현제 focus되있는 frag로 다시 이동
     */
    @Override
    public void onStop() {
        super.onStop();
        mCurrentPage = mBinding.viewPager.getCurrentItem();
        mBinding.viewPager.setCurrentItem(0);
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
        subEvent();
    }

    private void subEvent() {
        RxBus.getInstance().toObserverable()
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object event) {
                        if(event instanceof EventToolbarClick) {
                            // TODO
                            LogUtil.e(TAG, "EVENT STATUS :: " + ((EventToolbarClick) event).getStatus());
                            switch (((EventToolbarClick) event).getStatus()) {
                                case DONE:
                                    passToolbarClickEvent(EventSwitchEdit.STATUS.DONE);

                                    RxBus.getInstance().send(new EventSwitchEdit(EventSwitchEdit.STATUS.EDIT));
                                    break;
                                case EDIT:
                                    passToolbarClickEvent(EventSwitchEdit.STATUS.EDIT);

                                    RxBus.getInstance().send(new EventSwitchEdit(EventSwitchEdit.STATUS.DONE));
                                    break;
                                case ADD:
                                    RxBus.getInstance().send(new EventSwitchEdit(EventSwitchEdit.STATUS.EDIT));
                                    break;
                            }
                        }
                    }
                });
    }

    private void passToolbarClickEvent(EventSwitchEdit.STATUS status) {
        ((IToolbarClickListener)mPagerAdapter.getItem(0)).onClickEdit(status);
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




    /**
     * switch operation
     */
    @Override
    public void onSwitchClicked(int position, int btnNumber) {
        Switch item = SwitchManager.getInstance().getItem(position);
        getSocketManager().operationOnOff(item, btnNumber);
    }

    @Override
    public void onSwitchDelete(int position) {
        getPresenter().deleteItem(position);
    }

    @Override
    public void onSwitchRename(int position, String title) {
        getPresenter().rename(position, title);
    }

    @Override
    public void onSuccessRenameSwitch(int position, String title) {
        ((ISwitchRefreshListener)mPagerAdapter.getItem(0)).changeSwitchTitle(position, title);
    }

    @Override
    public void onSuccessDeleteSwitch(int position) {
        // 등록된 스위치가 없으면 edit hide
        if (SwitchManager.getInstance().getCount() == 0) {
            RxBus.getInstance().send(new EventSwitchEdit(EventSwitchEdit.STATUS.HIDE));
        }
        mPagerAdapter.deleteItem(position);
        ((ISwitchRefreshListener)mPagerAdapter.getItem(0)).deleteSwitch(position);
    }
}
