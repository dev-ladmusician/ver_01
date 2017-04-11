package com.goqual.a10k.view.base;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.goqual.a10k.R;
import com.goqual.a10k.presenter.NetworkPresenter;
import com.goqual.a10k.presenter.impl.NetworkPresenterImpl;
import com.goqual.a10k.util.ErrorHandler;
import com.goqual.a10k.view.activities.ActivityPhoneAuth;
import com.goqual.a10k.view.dialog.CustomDialog;

/**
 * Created by ladmusician on 2017. 2. 20..
 */

public abstract class BaseActivity<B extends ViewDataBinding> extends AppCompatActivity
        implements NetworkPresenter.View, BaseErrorHandler{
    protected B mBinding;

    private CustomDialog mNetworkErrorDialog;

    private boolean isDetectNetworkError = true;
    private NetworkPresenterImpl mNetworkPresenter;

    protected abstract int getLayoutId();
    public abstract void onBtnClick(View view);

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
        new CustomDialog(this)
                .setTitleText(R.string.error_dialog_title)
                .setMessageText(ErrorHandler.makeErrorString(this, e))
                .setPositiveButton(getString(R.string.common_retry), (dialog, which) -> {
                    refresh();
                })
                .setNegativeButton(getString(R.string.common_quit), (dialog, which) -> {
                    finish();
                })
                .setPositiveButton(false)
                .show();
    }

    @Override
    public void restartAuth() {
        startActivity(new Intent(this, ActivityPhoneAuth.class));
        finish();
    }

    @Override
    public void onDisconnected() {
        if(!mNetworkErrorDialog.isShowing() && isDetectNetworkError) {
            mNetworkErrorDialog.show();
        }
    }

    @Override
    public void onConnected() {
        if(mNetworkErrorDialog.isShowing() && isDetectNetworkError)  {
            mNetworkErrorDialog.dismiss();
        }
    }

    @Override
    public void addItem(Object item) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPresenter();
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        mNetworkErrorDialog = new CustomDialog(this)
                .setTitleText(R.string.internet_error_title)
                .setMessageText(R.string.internet_error_content)
                .setPositiveButton(false)
                .setNegativeButton(getString(R.string.common_quit), (dialog, which) -> {
                    ActivityCompat.finishAffinity(this);
                    System.exit(0);
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPresenter().onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getPresenter().onStop();
    }

    public void setDetectNetworkError(boolean detectNetworkError) {
        isDetectNetworkError = detectNetworkError;
    }

    private NetworkPresenter getPresenter() {
        if(mNetworkPresenter == null) {
            mNetworkPresenter = new NetworkPresenterImpl(this, this);
        }
        return mNetworkPresenter;
    }
}
