package com.goqual.a10k.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.ActivityMainBinding;
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
import com.goqual.a10k.view.interfaces.IFragmentInteraction;
import com.goqual.a10k.view.interfaces.IToolbarClickListener;

import rx.functions.Action1;


public class ActivityMain extends BaseActivity<ActivityMainBinding>
        implements IActivityInteraction{
    public static final String TAG = ActivityMain.class.getSimpleName();

    private EventSwitchEdit mEditBtnStatus;

    @Override
    protected int getLayoutId() { return R.layout.activity_main; }

    private Menu menu;
    private AdapterPager fragmentPagerAdapter;

    private boolean isScrollUserInteraction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e(TAG, "ON_CREATE!!!");
        mBinding.setActivity(this);
        initView();
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
                            mBinding.setEventSwitEditEnum(((EventSwitchEdit) event).getSTATE());
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
        mBinding.mainTaps.addOnTabSelectedListener(mainTapSelectedListener);

        mBinding.mainTaps.addTab(mBinding.mainTaps.newTab().setIcon(R.drawable.selector_footer_switch));
        mBinding.mainTaps.addTab(mBinding.mainTaps.newTab().setIcon(R.drawable.selector_footer_timer));
        mBinding.mainTaps.addTab(mBinding.mainTaps.newTab().setIcon(R.drawable.selector_footer_noti));
        mBinding.mainTaps.addTab(mBinding.mainTaps.newTab().setIcon(R.drawable.selector_footer_setting));
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

    public void handleLogin() {
        startActivity(new Intent(this, ActivityPhoneAuth.class));
        finish();
    }

    @Override
    public void finishApp() {
        ActivityCompat.finishAffinity(this);
        System.exit(0);
    }

    private void setToolbarMenuVisibillity(boolean visibillity) {
        int visible = visibillity ? View.VISIBLE : View.GONE;
        mBinding.toolbarAdd.setVisibility(visible);
        mBinding.toolbarEdit.setVisibility(visible);
    }

    public void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_edit:
                switch (mEditBtnStatus.getSTATE()) {
                    case DONE:
                        RxBus.getInstance().send(new EventToolbarClick(IToolbarClickListener.STATE.DONE));
                        break;
                    case EDIT:
                        RxBus.getInstance().send(new EventToolbarClick(IToolbarClickListener.STATE.EDIT));
                        break;
                }
                break;
            case R.id.toolbar_add:
                if(mBinding.mainPager.getCurrentItem() == 0) {
                    RxBus.getInstance().send(new EventToolbarClick(IToolbarClickListener.STATE.ADD));
                }
                break;
        }
    }

    private TabLayout.OnTabSelectedListener mainTapSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            mBinding.mainPager.setCurrentItem(tab.getPosition(), true);
            if(tab.getPosition() == 0 && !isScrollUserInteraction) {
                ((FragmentMainSwitchContainer)fragmentPagerAdapter.getItem(0)).setCurrentPage(0);
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    private ViewPager.OnPageChangeListener mainPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        int currentPage = 0;
        int lastState = 0;
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            LogUtil.d(TAG, String.format("position:%d poOffset:%f poOffsetPixels:%d", position, positionOffset, positionOffsetPixels));
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

            ((IFragmentInteraction)fragmentPagerAdapter.getItem(position)).setFragmentVisible(IFragmentInteraction.VISIBLE);
            ((IFragmentInteraction)fragmentPagerAdapter.getItem(currentPage)).setFragmentVisible(IFragmentInteraction.INVISIBLE);
            currentPage = position;
            setToolbarMenuVisibillity((((BaseFragment) fragmentPagerAdapter.getItem(position)).hasToolbarMenus()));
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            LogUtil.d(TAG, String.format("state:%d", state));
            if(state == ViewPager.SCROLL_STATE_SETTLING) {
                isScrollUserInteraction = lastState == 1;
            }
            else {
                isScrollUserInteraction = false;
            }
            lastState = state;
        }

        private void invalidateFragmentMenus(int position){
            for(int i = 0; i < fragmentPagerAdapter.getCount(); i++){
                fragmentPagerAdapter.getItem(i).setHasOptionsMenu(i == position);
            }
            invalidateOptionsMenu(); //or respectively its support method.
        }
    };
}
