package com.goqual.a10k.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.ActivitySettingBinding;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.util.event.EventSwitchEdit;
import com.goqual.a10k.util.event.EventToolbarClick;
import com.goqual.a10k.util.event.RxBus;
import com.goqual.a10k.view.adapters.AdapterPager;
import com.goqual.a10k.view.base.BaseActivity;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.fragments.setting.FragmentSettingAdmin;
import com.goqual.a10k.view.fragments.setting.FragmentSettingHistory;
import com.goqual.a10k.view.fragments.setting.FragmentSettingNfc;
import com.goqual.a10k.view.interfaces.IActivityInteraction;
import com.goqual.a10k.view.interfaces.IToolbarClickListener;

import rx.functions.Action1;

/**
 * Created by hanwool on 2017. 2. 28..
 */

public class ActivitySetting extends BaseActivity<ActivitySettingBinding>
implements IActivityInteraction{

    public static final String TAG = ActivitySetting.class.getSimpleName();

    public static final String ITEM_SWITCH = "item_switch";

    private AdapterPager mAdapterPage;
    private Switch mSwitch;

    private EventToolbarClick mEventToolbarClick;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent() != null && getIntent().getExtras() != null) {
            mSwitch = getIntent().getExtras().getParcelable(ITEM_SWITCH);
            if(mSwitch == null) {
                throw new IllegalStateException("Empty INTENT!");
            }
        }
        initViewPager();
        initTab();
        subEvent();
    }



    private void subEvent() {
        RxBus.getInstance().toObserverable()
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object event) {
                        if(event instanceof EventToolbarClick) {
                            mEventToolbarClick = (EventToolbarClick)event;
                            mBinding.setEditSwitchStatus(mEventToolbarClick.getStatus());
                            passToolbarClickEvent(mEventToolbarClick.getStatus());
                        }
                    }
                });
    }

    private void initViewPager() {
        mEventToolbarClick = new EventToolbarClick(IToolbarClickListener.STATUS.EDIT);
        mBinding.setEditSwitchStatus(mEventToolbarClick.getStatus());
        mBinding.setActivity(this);
        mAdapterPage = new AdapterPager(getSupportFragmentManager());
        mAdapterPage.addItem(FragmentSettingAdmin.newInstance(mSwitch));
        mAdapterPage.addItem(FragmentSettingNfc.newInstance(mSwitch));
        mAdapterPage.addItem(FragmentSettingHistory.newInstance(mSwitch));
        mBinding.settingContainer.setAdapter(mAdapterPage);
        mBinding.settingContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                setTitle(((BaseFragment) mAdapterPage.getItem(position)).getTitle());
                try {
                    invalidateFragmentMenus(position);
                    mBinding.settingTabs.getTabAt(position).select();

                }
                catch (NullPointerException e){
                    LogUtil.e(TAG, e.getMessage(), e);
                }

                if(position == 2) {
                    mBinding.toolbarEditContainer.setVisibility(View.GONE);
                }
                else {
                    mBinding.toolbarEditContainer.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

            private void invalidateFragmentMenus(int position){
                for(int i = 0; i < mAdapterPage.getCount(); i++){
                    mAdapterPage.getItem(i).setHasOptionsMenu(i == position);
                }
                invalidateOptionsMenu(); //or respectively its support method.
            }
        });
        mBinding.settingContainer.setOffscreenPageLimit(3);
    }

    private void initTab() {
        mBinding.settingTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mBinding.settingContainer.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mBinding.settingTabs.addTab(mBinding.settingTabs.newTab().setText(R.string.tab_title_admin));
        mBinding.settingTabs.addTab(mBinding.settingTabs.newTab().setText(R.string.tab_title_nfc));
        mBinding.settingTabs.addTab(mBinding.settingTabs.newTab().setText(R.string.tab_title_history));
    }

    @Override
    public void setTitle(CharSequence title) {
        mBinding.toolbarTitle.setText(title);
    }

    public void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.toolbar_edit_container:
                if(mEventToolbarClick.getStatus() == IToolbarClickListener.STATUS.EDIT) {
                    RxBus.getInstance().send(new EventToolbarClick(IToolbarClickListener.STATUS.DONE));
                }
                else {
                    RxBus.getInstance().send(new EventToolbarClick(IToolbarClickListener.STATUS.EDIT));
                }
                break;
        }
    }

    private void passToolbarClickEvent(IToolbarClickListener.STATUS status) {
        ((IToolbarClickListener)mAdapterPage.getItem(0)).onClickEdit(status);
    }

    @Override
    public AppBarLayout getAppbar() {
        return null;
    }

    @Override
    public Toolbar getToolbar() {
        return null;
    }

    @Override
    public void setTitle(String title) {
//        mBinding.toolbarTitle.setText(title);
    }

    @Override
    public void finishApp() {

    }
}
