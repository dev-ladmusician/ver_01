package com.goqual.a10k.presenter.impl;

import android.content.Context;

import com.goqual.a10k.model.entity.History;
import com.goqual.a10k.model.remote.ResultDTO;
import com.goqual.a10k.model.remote.service.HistoryService;
import com.goqual.a10k.presenter.HistoryPresenter;
import com.goqual.a10k.view.adapters.AdapterHistory;
import com.goqual.a10k.view.adapters.model.AdapterDataModel;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ladmusician on 7/29/16.
 */
public class HistoryPresenterImpl implements HistoryPresenter {
    private static final String TAG = HistoryPresenterImpl.class.getName();
    private View<History> mView;
    private AdapterHistory mHistoryAdapter;
    private Context mContext;
    private HistoryService mHistoryService;

    public HistoryPresenterImpl(Context ctx, View<History> view, AdapterHistory dataModel) {
        this.mContext = ctx;
        this.mView = view;
        this.mHistoryAdapter = dataModel;
    }

    @Override
    public void get(int switchId, int year, int month, int day, int page) {
        mHistoryAdapter.clear();
        mView.loadingStart();
        getHistoryService().getHistoryApi().gets(switchId, year, month, day, page)
                .subscribeOn(Schedulers.newThread())
                .filter(result -> result.getResult() != null)
                .map(ResultDTO::getResult)
                .filter(items -> items != null && !items.isEmpty())
                .flatMap(items -> Observable.from(items))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> {
                            mHistoryAdapter.addItem(item);
                            mView.addItem(item);
                        },
                        mView::onError,
                        mView::loadingStop
                );
    }



    private HistoryService getHistoryService() {
        if (mHistoryService == null)
            mHistoryService = new HistoryService(mContext);

        return mHistoryService;
    }
}
