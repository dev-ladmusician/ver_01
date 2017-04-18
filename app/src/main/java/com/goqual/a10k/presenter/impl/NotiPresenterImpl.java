package com.goqual.a10k.presenter.impl;

import android.content.Context;

import com.goqual.a10k.model.entity.NotiWrap;
import com.goqual.a10k.model.remote.service.InviteService;
import com.goqual.a10k.model.remote.service.NotiService;
import com.goqual.a10k.presenter.NotiPresenter;
import com.goqual.a10k.view.adapters.model.AdapterDataModel;
import com.goqual.a10k.view.interfaces.IPaginationPage;

import org.apache.commons.lang3.NotImplementedException;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ladmusician on 2016. 12. 8..
 */

public class NotiPresenterImpl implements NotiPresenter {
    private static final String TAG = NotiPresenterImpl.class.getSimpleName();
    private View<NotiWrap> mView;
    private AdapterDataModel<NotiWrap> mAlarmAdapterDataModel;
    private NotiService mNotiService;
    private InviteService mInviteService;
    private Context mContext;

    public NotiPresenterImpl(Context ctx, View mView, AdapterDataModel<NotiWrap> dataModel) {
        this.mContext = ctx;
        this.mView = mView;
        this.mAlarmAdapterDataModel = dataModel;
    }

    @Override
    @Deprecated
    public void loadItems() {

        throw new NotImplementedException("Stub!");
    }

    @Override
    public void loadItems(int page) {
        getNotiService().getNotiApi().gets(page)
                .subscribeOn(Schedulers.newThread())
                .filter(result -> result.getResult() != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            ((IPaginationPage)mView).setPage(result.getPage());
                            ((IPaginationPage)mView).setLastPage(result.getLastPage());

                            for(NotiWrap each : result.getResult())
                                mView.addItem(each);

                        },
                        mView::onError,
                        mView::refresh
                );
    }

    @Override
    public void acceptInvite(int switchId) {
        mView.loadingStart();
        getInviteService().getInviteApi().acceptInvite(switchId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((x) -> {
                            mView.loadingStop();
                        },
                        e -> mView.onError(e),
                        mView::onSuccessInvite
                );
    }

    private NotiService getNotiService() {
        if (mNotiService == null) {
            mNotiService = new NotiService(mContext);
        }
        return mNotiService;
    }

    private InviteService getInviteService() {
        if (mInviteService == null)
            mInviteService = new InviteService(mContext);

        return mInviteService;
    }
}
