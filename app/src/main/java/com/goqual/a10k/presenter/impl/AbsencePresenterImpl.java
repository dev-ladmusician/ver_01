package com.goqual.a10k.presenter.impl;

import android.content.Context;

import com.goqual.a10k.model.entity.Absence;
import com.goqual.a10k.model.remote.ResultDTO;
import com.goqual.a10k.model.remote.service.AbsenceService;
import com.goqual.a10k.presenter.AbsencePresenter;

import org.apache.commons.lang3.NotImplementedException;

import rx.Observable;
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
                .subscribe(resultDTO -> {
                            mView.loadingStop();
                        },
                        mView::onError,
                        () -> mView.refresh());
    }

    @Override
    @Deprecated
    public void loadItems() {
        getAbsenceService().getAbsenceApi().gets()
                .subscribeOn(Schedulers.newThread())
                .filter(result -> result.getResult() != null)
                .map(ResultDTO::getResult)
                .filter(items -> items != null && !items.isEmpty())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            Absence item = result.get(0);
                            mAbsence = item;
                            mView.addItem(item);
                            mView.loadingStop();
                        },
                        mView::onError,
                        mView::refresh);
    }

    @Override
    public void loadItems(int page) {
        throw new NotImplementedException("Stub!");
    }

    @Override
    public void delete(int position) {
        getAbsenceService().getAbsenceApi().delete(mAbsence.get_absenceid())
                .subscribeOn(Schedulers.newThread())
                .filter(result -> result.getResult() != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultDTO -> {
                            mView.onSuccessDelete();
                        },
                        (e) -> mView.onFailDelete(position),
                        () -> mView.refresh());
    }

    @Override
    public void updateState(int position) {
        getAbsenceService().getAbsenceApi().put(
                mAbsence.get_absenceid(),
                mAbsence.get_bsid(),
                mAbsence.getStart_hour(), mAbsence.getStart_min(),
                mAbsence.getEnd_hour(), mAbsence.getEnd_min(),
                mAbsence.isBtn1(), mAbsence.isBtn2(), mAbsence.isBtn3(),
                !mAbsence.isState())
                .subscribeOn(Schedulers.newThread())
                .filter(result -> result.getResult() != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultDTO -> {
                            mAbsence.setState(!mAbsence.isState());
                        },
                        mView::onError,
                        () -> mView.refresh());
    }


    public AbsenceService getAbsenceService() {
        if (mAbsenceService == null) {
            mAbsenceService = new AbsenceService(mContext);
        }
        return mAbsenceService;
    }
}
