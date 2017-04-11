package com.goqual.a10k.presenter.impl;

import android.content.Context;

import com.goqual.a10k.model.entity.Nfc;
import com.goqual.a10k.model.remote.ResultDTO;
import com.goqual.a10k.model.remote.service.NfcService;
import com.goqual.a10k.presenter.NfcDetectPresenter;
import com.goqual.a10k.util.LogUtil;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hanwool on 2017. 2. 28..
 */

public class NfcDetectPresenterImpl implements NfcDetectPresenter {
    public static final String TAG = NfcDetectPresenterImpl.class.getSimpleName();

    private View<Nfc> mView;
    private Context mContext;
    private NfcService mNfcService;

    public NfcDetectPresenterImpl(Context mContext, View mView) {
        this.mView = mView;
        this.mContext = mContext;
    }

    @Override
    public void checkExist(int bsId, String tag) {
        getNfcService().getrNfcApi().check(tag, bsId)
                .subscribeOn(Schedulers.newThread())
                .filter(result -> result.getResult() != null)
                .map(ResultDTO::getResult)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mView::refresh,
                        (e) -> {mView.refresh(new Nfc());},
                        ()-> {LogUtil.e(TAG, "complete");});
    }

    private NfcService getNfcService() {
        if(mNfcService == null) {
            mNfcService = new NfcService(mContext);
        }
        return mNfcService;
    }
}
