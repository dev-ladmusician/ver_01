package com.goqual.a10k.presenter.impl;

import android.content.Context;

import com.google.common.base.Throwables;
import com.goqual.a10k.model.remote.SwitchService;
import com.goqual.a10k.presenter.ConnectPresenter;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ladmusician on 2017. 1. 17..
 */

public class ConnectPresenterImpl implements ConnectPresenter {
    private static final String TAG = ConnectPresenterImpl.class.getName();
    private View mView;
    private Context mContext;
    private SwitchService mSwitchService = null;

    public ConnectPresenterImpl(Context ctx, View view) {
        this.mView = view;
        this.mContext = ctx;
    }

    @Override
    public void register(String macaddr, String title, int count) {
        getSwitchService().getSwitchApi().add(macaddr, title, count)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((x) -> {
                            mView.loadingStop();
                            mView.keyPadDown();
                        },
                        e -> mView.onError(e),
                        mView::onSuccess
                );
    }

    public SwitchService getSwitchService() {
        if (mSwitchService == null)
            mSwitchService = new SwitchService(mContext);

        return mSwitchService;
    }
}
