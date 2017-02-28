package com.goqual.a10k.presenter.impl;

import android.content.Context;

import com.goqual.a10k.model.entity.Nfc;
import com.goqual.a10k.model.remote.ResultDTO;
import com.goqual.a10k.model.remote.service.NfcService;
import com.goqual.a10k.presenter.NfcTagPresenter;
import com.goqual.a10k.util.LogUtil;

import java.util.ArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hanwool on 2017. 2. 28..
 */

public class NfcTagPresenterImpl implements NfcTagPresenter {
    public static final String TAG = NfcTagPresenterImpl.class.getSimpleName();

    private View<Nfc> mView;
    private Context mContext;
    private NfcService mNfcService;
    private ArrayList<Nfc> mTagList;

    public NfcTagPresenterImpl(Context mContext, View mView) {
        this.mView = mView;
        this.mContext = mContext;
        this.mTagList = new ArrayList<>();
    }

    @Override
    public void loadItems(int switchId) {
        LogUtil.d(TAG, "loadItems::switchId: " + switchId);
        getNfcService().getrNfcApi().gets(switchId)
                .subscribeOn(Schedulers.newThread())
                .filter(result -> result.getResult() != null)
                .map(ResultDTO::getResult)
                .filter(items -> items != null && !items.isEmpty())
                .flatMap(items -> Observable.from(items))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((item) -> {
                            mView.addItem(item);
                            mTagList.add(item);
                        },
                        mView::onError,
                        () -> {
                            mView.loadingStop();
                        });
    }

    @Override
    public void delete(int position) {
        Nfc item = (Nfc) mTagList.get(position);
        getNfcService().getrNfcApi().delete(item.get_nfcid())
                .subscribeOn(Schedulers.newThread())
                .filter(result -> result.getResult() != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultDTO -> {
                            mTagList.remove(position);
                        },
                        mView::onError,
                        mView::onSuccess);
    }

    @Override
    public void detectedNfc(String tag) {
        getNfcService().getrNfcApi().get(tag)
                .subscribeOn(Schedulers.newThread())
                .filter(result -> result.getResult() != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rtv -> {
                            mView.loadingStop();
//                            ((ActivityNfcDetected)mView).setNfc(rtv.getResult());
                        },
                        mView::onError,
                        mView::onSuccess);
    }

    @Override
    public void add(Nfc item) {
        getNfcService().getrNfcApi().add(
                item.get_bsid(),
                item.getTag(),
                item.getBtn1(),
                item.getBtn2(),
                item.getBtn3(),
                item.getTitle()
        )
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
    public void update(Nfc item) {
        getNfcService().getrNfcApi().put(
                item.get_nfcid(),
                item.get_bsid(),
                item.getTag(),
                item.getBtn1(),
                item.getBtn2(),
                item.getBtn3(),
                item.getTitle()
        )
                .subscribeOn(Schedulers.newThread())
                .filter(result -> result.getResult() != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultDTO -> {
                            mView.loadingStop();
                        },
                        mView::onError,
                        mView::onSuccess);
    }

    private NfcService getNfcService() {
        if(mNfcService == null) {
            mNfcService = new NfcService(mContext);
        }
        return mNfcService;
    }
}
