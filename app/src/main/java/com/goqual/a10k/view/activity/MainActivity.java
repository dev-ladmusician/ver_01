package com.goqual.a10k.view.activity;

import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.goqual.a10k.R;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.BlankFragment;
import com.goqual.a10k.view.adapters.FragmentPagerAdapter;
import com.goqual.a10k.view.base.FragmentBase;
import com.goqual.a10k.view.interfaces.IActivityInteraction;


import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements IActivityInteraction{

    public static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;
    @BindView(R.id.main_pager)
    ViewPager viewPager;
    @BindView(R.id.main_taps)
    TabLayout tabLayout;
    @BindView(R.id.toolbar_title)
    TextView mTitleTextView;

    private Menu menu;

    private FragmentPagerAdapter fragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        initToolbar();
        initViewPager();
        initMainTab();
    }

    private void initToolbar() {

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initViewPager() {
        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager());
        fragmentPagerAdapter.addItem(BlankFragment.newInstance("1"));
        fragmentPagerAdapter.addItem(BlankFragment.newInstance("2"));
        fragmentPagerAdapter.addItem(BlankFragment.newInstance("3"));
        fragmentPagerAdapter.addItem(BlankFragment.newInstance("4"));
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.addOnPageChangeListener(mainPagerPageChangeListener);
    }

    private void initMainTab() {
        if(Build.VERSION.SDK_INT>=23) {
            tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
            tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimaryDark, null));
            tabLayout.setTabTextColors(getResources().getColor(R.color.tabUnSelected, null), getResources().getColor(R.color.tabSelected, null));
            tabLayout.setElevation(1);
        }
        else {
            tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));
            tabLayout.setTabTextColors(getResources().getColor(R.color.tabUnSelected), getResources().getColor(R.color.tabSelected));
        }
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.addOnTabSelectedListener(mainTapSelectedListener);

        tabLayout.addTab(tabLayout.newTab().setText("1"));
        tabLayout.addTab(tabLayout.newTab().setText("2"));
        tabLayout.addTab(tabLayout.newTab().setText("3"));
        tabLayout.addTab(tabLayout.newTab().setText("4"));
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
        return appBarLayout;
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public void setTitle(String title) {
        LogUtil.d(TAG, title);
        mTitleTextView.setText(title);
    }

    private TabLayout.OnTabSelectedListener mainTapSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            viewPager.setCurrentItem(tab.getPosition(), true);
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
            setTitle(((FragmentBase)fragmentPagerAdapter.getItem(position)).getTitle());
            try {
                invalidateFragmentMenus(position);
                tabLayout.getTabAt(position).select();
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
