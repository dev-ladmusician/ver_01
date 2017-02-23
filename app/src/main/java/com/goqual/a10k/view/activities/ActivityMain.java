package com.goqual.a10k.view.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.ActivityMainBinding;
import com.goqual.a10k.helper.PreferenceHelper;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.util.event.EventSwitchEdit;
import com.goqual.a10k.util.event.EventToolbarClick;
import com.goqual.a10k.util.event.RxBus;
import com.goqual.a10k.view.adapters.AdapterPager;
import com.goqual.a10k.view.base.BaseActivity;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.fragments.FragmentMainAlarm;
import com.goqual.a10k.view.fragments.FragmentMainNoti;
import com.goqual.a10k.view.fragments.FragmentMainSetting;
import com.goqual.a10k.view.fragments.FragmentMainSwitchContainer;
import com.goqual.a10k.view.interfaces.IActivityInteraction;

import rx.functions.Action1;


public class ActivityMain extends BaseActivity<ActivityMainBinding>
        implements IActivityInteraction{
    public static final String TAG = ActivityMain.class.getSimpleName();

    private EventSwitchEdit mEditBtnStatus;

    @Override
    protected int getLayoutId() { return R.layout.activity_main; }

    private Menu menu;
    private AdapterPager fragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding.setActivity(this);
        initView();
        PreferenceHelper.getInstance(this)
                .put(getString(R.string.arg_user_token),
                        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MywiaWF0IjoxNDg3NjUwOTc2LCJleHAiOjE0ODgxNzY1NzZ9.z4eJQRtAcGYJFyEWikVYYHrtmcfOl1R3c9bsFy5VNTI");

    }


    private void initView() {
        initToolbar();
        initViewPager();
        initMainTab();

        mBinding.toolbarEdit.setVisibility(View.GONE);

        subEvent();
    }

    private void subEvent() {
        RxBus.getInstance().toObserverable()
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object event) {
                        if(event instanceof EventSwitchEdit) {
                            mEditBtnStatus = (EventSwitchEdit)event;
                            mBinding.setEventSwitEditEnum(((EventSwitchEdit) event).getStatus());
                        }
                    }
                });
    }

    private void initToolbar() {
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initViewPager() {
        fragmentPagerAdapter = new AdapterPager(getSupportFragmentManager());
        fragmentPagerAdapter.addItem(FragmentMainSwitchContainer.newInstance());
        fragmentPagerAdapter.addItem(FragmentMainAlarm.newInstance());
        fragmentPagerAdapter.addItem(FragmentMainNoti.newInstance());
        fragmentPagerAdapter.addItem(FragmentMainSetting.newInstance());

        mBinding.mainPager.setOffscreenPageLimit(getResources().getInteger(R.integer.main_viewpager_count));
        mBinding.mainPager.setAdapter(fragmentPagerAdapter);
        mBinding.mainPager.addOnPageChangeListener(mainPagerPageChangeListener);
    }

    private void initMainTab() {
        if(Build.VERSION.SDK_INT>=23) {
            mBinding.mainTaps.setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
            mBinding.mainTaps.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimaryDark, null));
            mBinding.mainTaps.setTabTextColors(getResources().getColor(R.color.tabUnSelected, null), getResources().getColor(R.color.tabSelected, null));
            mBinding.mainTaps.setElevation(1);
        }
        else {
            mBinding.mainTaps.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            mBinding.mainTaps.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));
            mBinding.mainTaps.setTabTextColors(getResources().getColor(R.color.tabUnSelected), getResources().getColor(R.color.tabSelected));
        }
        mBinding.mainTaps.setTabMode(TabLayout.MODE_FIXED);
        mBinding.mainTaps.setTabGravity(TabLayout.GRAVITY_FILL);

        mBinding.mainTaps.addOnTabSelectedListener(mainTapSelectedListener);

        mBinding.mainTaps.addTab(mBinding.mainTaps.newTab().setText("1"));
        mBinding.mainTaps.addTab(mBinding.mainTaps.newTab().setText("2"));
        mBinding.mainTaps.addTab(mBinding.mainTaps.newTab().setText("3"));
        mBinding.mainTaps.addTab(mBinding.mainTaps.newTab().setText("4"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public AppBarLayout getAppbar() {
        return mBinding.appbar;
    }

    @Override
    public Toolbar getToolbar() {
        return mBinding.toolbar;
    }

    @Override
    public void setTitle(String title) {
        LogUtil.d(TAG, title);
        mBinding.toolbarTitle.setText(title);
    }

    public void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_edit:
                switch (mEditBtnStatus.getStatus()) {
                    case DONE:
                        RxBus.getInstance().send(new EventToolbarClick(EventToolbarClick.STATUS.DONE));
                        break;
                    case EDIT:
                        RxBus.getInstance().send(new EventToolbarClick(EventToolbarClick.STATUS.EDIT));
                        break;
                }
                break;
            case R.id.toolbar_add:
                RxBus.getInstance().send(new EventToolbarClick(EventToolbarClick.STATUS.ADD));
                break;
        }
    }

    private TabLayout.OnTabSelectedListener mainTapSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            mBinding.mainPager.setCurrentItem(tab.getPosition(), true);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    private ViewPager.OnPageChangeListener mainPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setTitle(((BaseFragment)fragmentPagerAdapter.getItem(position)).getTitle());
            try {
                invalidateFragmentMenus(position);
                mBinding.mainTaps.getTabAt(position).select();
            }
            catch (NullPointerException e){
                LogUtil.e(TAG, e.getMessage(), e);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

        private void invalidateFragmentMenus(int position){
            for(int i = 0; i < fragmentPagerAdapter.getCount(); i++){
                fragmentPagerAdapter.getItem(i).setHasOptionsMenu(i == position);
            }
            invalidateOptionsMenu(); //or respectively its support method.
        }
    };
}
