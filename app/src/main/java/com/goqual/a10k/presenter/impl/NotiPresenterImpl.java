package com.goqual.a10k.presenter.impl;

import android.content.Context;

import com.goqual.a10k.model.entity.NotiWrap;
import com.goqual.a10k.model.entity.PagenationWrapper;
import com.goqual.a10k.model.remote.ResultDTO;
import com.goqual.a10k.model.remote.service.NotiService;
import com.goqual.a10k.presenter.NotiPresenter;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.adapters.model.AdapterDataModel;

import org.apache.commons.lang3.NotImplementedException;

import rx.Observable;
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
                .map(PagenationWrapper::getResult)
                .flatMap(items -> Observable.from(items))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            LogUtil.d(TAG, result.toString());
                                mAlarmAdapterDataModel.addItem(result);
                                mView.addItem(result);
                        },
                        mView::onError,
                        mView::loadingStop
                );
    }

    private NotiService getNotiService() {
        if (mNotiService == null) {
            mNotiService = new NotiService(mContext);
        }
        return mNotiService;
    }
}
