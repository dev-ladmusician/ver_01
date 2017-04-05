package com.goqual.a10k.view.activities;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.ActivitySwitchConnectionBinding;
import com.goqual.a10k.helper.PreferenceHelper;
import com.goqual.a10k.presenter.ConnectPresenter;
import com.goqual.a10k.presenter.impl.ConnectPresenterImpl;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.base.BaseActivity;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.fragments.connect.FragmentConnectInfo;
import com.goqual.a10k.view.fragments.connect.FragmentConnectSelectWifi;
import com.goqual.a10k.view.interfaces.IActivityInteraction;
import com.goqual.a10k.view.interfaces.IActivityFragmentPageChangeListener;

public class ActivitySwitchConnection extends BaseActivity<ActivitySwitchConnectionBinding>
        implements ConnectPresenter.View, IActivityFragmentPageChangeListener, IActivityInteraction {

    public static final String TAG = ActivitySwitchConnection.class.getSimpleName();

    private ConnectPresenter mPresenter;

    @Override
    public void onSuccess() {

    }

    @Override
    public void keyPadUp() {

    }

    @Override
    public void keyPadDown() {

    }

    @Override
    public void loadingStart() {

    }

    @Override
    public void loadingStop() {

    }

    @Override
    public void refresh() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void addItem(Object item) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_switch_connection;
    }

    @Override
    public void onBtnClick(View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_connection);
        getSupportFragmentManager()
                .beginTransaction()
                .add(mBinding.activityMain.getId(), FragmentConnectInfo.newInstance())
                .commit();
        setDetectNetworkError(false);
    }

    private ConnectPresenter getPresenter() {
        if(mPresenter == null) {
            mPresenter = new ConnectPresenterImpl(this, this);
        }
        return mPresenter;
    }

    @Override
    public void changePage(int page) {
        LogUtil.d(TAG, "changePage::"+page);
        BaseFragment fragment = null;
        if(page == getResources().getInteger(R.integer.frag_info)) {
            fragment = FragmentConnectInfo.newInstance();
        }
        else if(page == getResources().getInteger(R.integer.frag_select_wifi)) {
            fragment = FragmentConnectSelectWifi.newInstance();
        }
        else if(page == getResources().getInteger(R.integer.frag_set_switch)) {

        }
        else if(page == getResources().getInteger(R.integer.frag_rename)) {

        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(mBinding.activityMain.getId(), fragment)
                .commit();
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

    }

    @Override
    public void finishApp() {

    }

    @Override
    public PreferenceHelper getPreferenceHelper() {
        return null;
    }
}
