package com.goqual.a10k.presenter.impl;

import android.content.Context;

import com.goqual.a10k.model.entity.Alarm;
import com.goqual.a10k.model.remote.service.AlarmService;
import com.goqual.a10k.presenter.AlarmPresenter;
import com.goqual.a10k.view.adapters.model.AdapterDataModel;
import com.goqual.a10k.view.interfaces.IPaginationPage;

import org.apache.commons.lang3.NotImplementedException;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ladmusician on 2016. 12. 8..
 */

public class AlarmPresenterImpl implements AlarmPresenter {
    private static final String TAG = AlarmPresenterImpl.class.getSimpleName();
    private View mView;
    private AdapterDataModel<Alarm> mAlarmAdapterDataModel;
    private AlarmService mAlarmService;
    private Context mContext;

    public AlarmPresenterImpl(Context ctx, View mView, AdapterDataModel<Alarm> dataModel) {
        this.mContext = ctx;
        this.mView = mView;
        this.mAlarmAdapterDataModel = dataModel;
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
                        () -> mView.refresh());
    }

    @Override
    @Deprecated
    public void loadItems() {
        throw new NotImplementedException("Stub!");
    }

    @Override
    public void loadItems(int page) {
        mAlarmAdapterDataModel.clear();
        getAlarmService().getAlarmApi().gets(page)
                .subscribeOn(Schedulers.newThread())
                .filter(result -> result.getResult() != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            ((IPaginationPage)mView).setPage(result.getPage());
                            ((IPaginationPage)mView).setLastPage(result.getLastPage());

                            for(Alarm each : result.getResult()) {
                                mView.addItem(each);
                            }
                        },
                        mView::onError,
                        mView::refresh);
    }

    @Override
    public void delete(int position) {
        Alarm item = (Alarm)mAlarmAdapterDataModel.getItem(position);
        getAlarmService().getAlarmApi().delete(item.get_alarmid())
                .subscribeOn(Schedulers.newThread())
                .filter(result -> result.getResult() != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultDTO -> {
                            mAlarmAdapterDataModel.deleteItem(position);
                            mView.onSuccessDelete();
                        },
                        (e) -> {
                            mView.onFailDelete(position);
                            mView.onError(e);
                        },
                        mView::refresh
                );
    }

    @Override
    public void updateState(int position) {
        Alarm item = ((Alarm) mAlarmAdapterDataModel.getItem(position));
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
                !item.isState()
        )
                .subscribeOn(Schedulers.newThread())
                .filter(result -> result.getResult() != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultDTO -> {
                            item.setState(!item.isState());
                            mAlarmAdapterDataModel.updateItem(position, item);
                        },
                        mView::onError,
                        () -> mView.refresh());
    }



    public AlarmService getAlarmService() {
        if (mAlarmService == null) {
            mAlarmService = new AlarmService(mContext);
        }
        return mAlarmService;
    }
}
