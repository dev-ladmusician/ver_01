package com.goqual.a10k.presenter.impl;

import android.content.Context;

import com.goqual.a10k.model.entity.Nfc;
import com.goqual.a10k.model.remote.service.NfcService;
import com.goqual.a10k.presenter.NfcTagCreatePresenter;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hanwool on 2017. 2. 28..
 */

public class NfcTagCreatePresenterImpl implements NfcTagCreatePresenter {
    public static final String TAG = NfcTagCreatePresenterImpl.class.getSimpleName();

    private View<Nfc> mView;
    private Context mContext;
    private NfcService mNfcService;

    public NfcTagCreatePresenterImpl(Context mContext, View mView) {
        this.mView = mView;
        this.mContext = mContext;
    }

    @Override
    public void add(Nfc item) {
        getNfcService().getrNfcApi().add(
                item.get_bsid(),
                item.getTag(),
                item.getBtn1(),
                item.getBtn2(),
                item.getBtn3(),
                item.getTitle())
                .subscribeOn(Schedulers.newThread())
                .filter(result -> result.getResult() != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            if (result.getResult().get_nfcid() > 0)
                                mView.onSuccess();
                            else
                                mView.onError();
                        },
                        mView::onError,
                        mView::onFinish);
    }

    private NfcService getNfcService() {
        if(mNfcService == null) {
            mNfcService = new NfcService(mContext);
        }
        return mNfcService;
    }
}
