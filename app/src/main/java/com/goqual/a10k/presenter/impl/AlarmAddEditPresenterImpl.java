package com.goqual.a10k.presenter.impl;

import android.content.Context;

import com.goqual.a10k.model.entity.Alarm;
import com.goqual.a10k.model.remote.service.AlarmService;
import com.goqual.a10k.presenter.AlarmAddEditPresenter;

import org.apache.commons.lang3.NotImplementedException;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ladmusician on 2016. 12. 8..
 */

public class AlarmAddEditPresenterImpl implements AlarmAddEditPresenter {
    private static final String TAG = AlarmAddEditPresenterImpl.class.getSimpleName();
    private View mView;
    private AlarmService mAlarmService;
    private Context mContext;

    public AlarmAddEditPresenterImpl(Context ctx, View mView) {
        this.mContext = ctx;
        this.mView = mView;
    }

    @Override
    public void delete(Alarm item) {
        mView.loadingStart();
        getAlarmService().getAlarmApi().delete(item.get_alarmid())
                .subscribeOn(Schedulers.newThread())
                .filter(result -> result.getResult() != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultDTO -> {
                            mView.onSuccessDelete();
                        },
                        (e) -> {
                            mView.onErrorDelete();
                        },
                        mView::loadingStop
                );
    }

    @Override
    public void add(Alarm item) {
        mView.loadingStart();
        getAlarmService().getAlarmApi().add(
                item.get_bsid(),
                item.getRingtone(),
                item.getRingtone_title(),
                item.getHour(),
                item.getMin(),
                item.isSun(),
                item.isMon(),
                item.isTue(),
                item.isWed(),
                item.isThur(),
                item.isFri(),
                item.isSat(),
                item.getBtn1(),
                item.getBtn2(),
                item.getBtn3())
                .subscribeOn(Schedulers.newThread())
                .filter(result -> result.getResult() != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultDTO -> {
                            mView.loadingStop();
                        },
                        mView::onError,
                        mView::onSuccess);
    }

    @Override
    public void update(Alarm item) {
        getAlarmService().getAlarmApi().put(
                item.get_alarmid(),
                item.get_bsid(),
                item.getRingtone(),
                item.getRingtone_title(),
                item.getHour(),
                item.getMin(),
                item.isSun(),
                item.isMon(),
                item.isTue(),
                item.isWed(),
                item.isThur(),
                item.isFri(),
                item.isSat(),
                item.getBtn1(),
                item.getBtn2(),
                item.getBtn3(),
                item.isState())
                .subscribeOn(Schedulers.newThread())
                .filter(result -> result.getResult() != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultDTO -> {
                            mView.loadingStop();
                        },
                        mView::onError,
                        mView::onSuccess);
    }

    @Override
    @Deprecated
    public void loadItems() {
        throw new NotImplementedException("Stub!");
    }

    @Override
    public void loadItems(int page) {
    }

    public AlarmService getAlarmService() {
        if (mAlarmService == null) {
            mAlarmService = new AlarmService(mContext);
        }
        return mAlarmService;
    }
}
