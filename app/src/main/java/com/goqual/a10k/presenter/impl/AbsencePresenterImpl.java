package com.goqual.a10k.presenter.impl;

import android.content.Context;

import com.goqual.a10k.model.entity.Absence;
import com.goqual.a10k.model.remote.ResultDTO;
import com.goqual.a10k.model.remote.service.AbsenceService;
import com.goqual.a10k.presenter.AbsencePresenter;

import org.apache.commons.lang3.NotImplementedException;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ladmusician on 2016. 12. 8..
 */

public class AbsencePresenterImpl implements AbsencePresenter {
    private static final String TAG = AbsencePresenterImpl.class.getSimpleName();
    private final Context mContext;
    private View<Absence> mView;
    private Absence mAbsence;
    private AbsenceService mAbsenceService;

    public AbsencePresenterImpl(Context ctx, View mView, Absence dataModel) {
        this.mContext = ctx;
        this.mView = mView;
        this.mAbsence = dataModel;
    }

    @Override
    public void getItem(int switchId) {
        getAbsenceService().getAbsenceApi().get(switchId)
                .subscribeOn(Schedulers.newThread())
                .filter(result -> result.getResult() != null)
                .map(ResultDTO::getResult)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mView::onSuccessGetItem,
                        mView::onError,
                        mView::refresh);
    }

    @Override
    public void add(Absence item) {
        getAbsenceService().getAbsenceApi().add(
                item.get_bsid(),
                item.getStart_hour(), item.getStart_min(),
                item.getEnd_hour(), item.getEnd_min(),
                item.isBtn1(), item.isBtn2(), item.isBtn3())
                .subscribeOn(Schedulers.newThread())
                .filter(result -> result.getResult() != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultDTO -> {
                            mView.loadingStop();
                        },
                        mView::onError,
                        () -> mView.refresh());
    }

    @Override
    public void update(Absence item) {
        getAbsenceService().getAbsenceApi().put(
                item.get_absenceid(),
                item.get_bsid(),
                item.getStart_hour(), item.getStart_min(),
                item.getEnd_hour(), item.getEnd_min(),
                item.isBtn1(), item.isBtn2(), item.isBtn3(), item.isState())
                .subscribeOn(Schedulers.newThread())
                .filter(result -> result.getResult() != null)
                .observeOn(AndroidSchedulers.mainThread())
                .map(ResultDTO::getResult)
                .subscribe(mView::onSuccessUpdate,
                        mView::onError,
                        mView::refresh);
    }

    @Override
    @Deprecated
    public void loadItems() {
        throw new NotImplementedException("Stub!");
    }

    @Override
    public void loadItems(int page) {
        int switchId = page;
    }

    public AbsenceService getAbsenceService() {
        if (mAbsenceService == null) {
            mAbsenceService = new AbsenceService(mContext);
        }
        return mAbsenceService;
    }
}
