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
import com.goqual.a10k.helper.PreferenceHelper;
import com.goqual.a10k.util.BackPressUtil;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.util.event.EventToolbarClick;
import com.goqual.a10k.view.adapters.AdapterPager;
import com.goqual.a10k.view.base.BaseActivity;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.fragments.FragmentMainAlarm;
import com.goqual.a10k.view.fragments.FragmentMainNoti;
import com.goqual.a10k.view.fragments.FragmentMainSetting;
import com.goqual.a10k.view.fragments.FragmentMainSwitchContainer;
import com.goqual.a10k.view.interfaces.IActivityInteraction;
import com.goqual.a10k.view.interfaces.IToolbarClickListener;
import com.goqual.a10k.view.interfaces.IToolbarInteraction;

import static com.goqual.a10k.view.interfaces.IToolbarClickListener.STATE.DONE;
import static com.goqual.a10k.view.interfaces.IToolbarClickListener.STATE.EDIT;


public class ActivityMain extends BaseActivity<ActivityMainBinding>
        implements IActivityInteraction, IToolbarInteraction {
    public static final String TAG = ActivityMain.class.getSimpleName();

    private EventToolbarClick mEventToolbarClick;
    private BackPressUtil backPressUtil;

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
        backPressUtil = new BackPressUtil(this);
    }


    private void initView() {
        initToolbar();
        initViewPager();
        initMainTab();

        mEventToolbarClick = new EventToolbarClick(IToolbarClickListener.STATE.DONE);
        mBinding.toolbarEdit.setVisibility(View.GONE);
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

    public void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_edit:
                if (mEventToolbarClick.getState() == EDIT)
                    mEventToolbarClick.setState(IToolbarClickListener.STATE.DONE);
                else
                    mEventToolbarClick.setState(EDIT);
                passToolbarClickEvent(mEventToolbarClick.getState());
                break;
            case R.id.toolbar_add:
                handleAddClick();
                break;
        }
    }

    private void handleAddClick() {
        if (mBinding.mainPager.getCurrentItem() == getResources().getInteger(R.integer.frag_main_switch)) {
            startActivity(new Intent(this, ActivitySwitchConnection.class));
        } else {
            startActivity(new Intent(this, ActivityAlarmAddEdit.class));
        }
    }

    private void passToolbarClickEvent(IToolbarClickListener.STATE state) {
        if (mBinding.mainPager.getCurrentItem() == getResources().getInteger(R.integer.frag_main_switch) ||
                mBinding.mainPager.getCurrentItem() == getResources().getInteger(R.integer.frag_main_alarm)) {
            ((IToolbarClickListener)fragmentPagerAdapter.getItem(mBinding.mainPager.getCurrentItem())).onClickEdit(state);
        }
    }

    /**
     * set visible toolbar add
     */
    private void setToolbarAddVisibility() {
        if (mBinding.mainPager.getCurrentItem() == getResources().getInteger(R.integer.frag_main_noti) ||
                mBinding.mainPager.getCurrentItem() == getResources().getInteger(R.integer.frag_main_setting)) {
            mBinding.toolbarAdd.setVisibility(View.GONE);
        } else {
            mBinding.toolbarAdd.setVisibility(View.VISIBLE);
        }
    }

    /**
     * set visible toolbar edit
     * fragment단에서 호출출
    * @param state
     */
    @Override
    public void setToolbarEdit(IToolbarClickListener.STATE state) {
        mBinding.setEventSwitEditEnum(state);
    }

    private TabLayout.OnTabSelectedListener mainTapSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            mBinding.mainPager.setCurrentItem(tab.getPosition(), true);
        }
        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            LogUtil.e(TAG, "tab un selected");
        }
        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            LogUtil.e(TAG, "tab un reselected");
            if(tab.getPosition() == 0 && !isScrollUserInteraction) {
                ((FragmentMainSwitchContainer)fragmentPagerAdapter.getItem(0)).setCurrentPage(0);
            }
        }
    };

    private ViewPager.OnPageChangeListener mainPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        int lastState = 0;
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            LogUtil.e(TAG, "main page selection :: " + position);

            // main toolbar title
            setTitle(((BaseFragment)fragmentPagerAdapter.getItem(position)).getTitle());

            // add버튼 gone or visible
            setToolbarAddVisibility();

            // 페이지가 변경되면 toolbar 모두를 DONE모드로
            mEventToolbarClick.setState(DONE);
            passToolbarClickEvent(mEventToolbarClick.getState());

            try {
                invalidateFragmentMenus(position);
                mBinding.mainTaps.getTabAt(position).select();
            } catch (NullPointerException e){
                LogUtil.e(TAG, e.getMessage(), e);
            }

            if(((BaseFragment)fragmentPagerAdapter.getItem(position)).hasToolbarMenus()) {
                setToolbarEdit(IToolbarClickListener.STATE.DONE);
            }  else {
                setToolbarEdit(IToolbarClickListener.STATE.HIDE);
            }
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

    @Override
    public int getCurrentPage() {
        return mBinding.mainPager.getCurrentItem();
    }

    @Override
    public PreferenceHelper getPreferenceHelper() {
        return null;
    }

    @Override
    public void onBackPressed() {
        if (mEventToolbarClick.getState() == EDIT) {
            mEventToolbarClick.setState(IToolbarClickListener.STATE.DONE);
            passToolbarClickEvent(mEventToolbarClick.getState());
            return;
        } else {
            backPressUtil.onBackPressed();
        }
    }
}
