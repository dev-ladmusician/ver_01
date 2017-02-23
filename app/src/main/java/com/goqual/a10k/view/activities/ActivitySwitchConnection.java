package com.goqual.a10k.view.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.ActivitySwitchConnectionBinding;
import com.goqual.a10k.presenter.ConnectPresenter;
import com.goqual.a10k.presenter.SwitchPresenter;
import com.goqual.a10k.presenter.impl.ConnectPresenterImpl;
import com.goqual.a10k.presenter.impl.SwitchPresenterImpl;
import com.goqual.a10k.view.base.BaseActivity;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.fragments.connect.FragmentConnectInfo;
import com.goqual.a10k.view.interfaces.IConnectFragmentListener;

public class ActivitySwitchConnection extends BaseActivity<ActivitySwitchConnectionBinding>
        implements ConnectPresenter.View, IConnectFragmentListener {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_connection);
        getSupportFragmentManager()
                .beginTransaction()
                .add(mBinding.activityMain.getId(), FragmentConnectInfo.newInstance())
                .commit();
    }

    private ConnectPresenter getPresenter() {
        if(mPresenter == null) {
            mPresenter = new ConnectPresenterImpl(this, this);
        }
        return mPresenter;
    }

    @Override
    public void changePage(int page) {
        BaseFragment fragment;
        if(page == getResources().getInteger(R.integer.frag_select_wifi)) {

        }
        else if(page == getResources().getInteger(R.integer.frag_set_switch)) {

        }
        else if(page == getResources().getInteger(R.integer.frag_rename)) {

        }
    }
}
