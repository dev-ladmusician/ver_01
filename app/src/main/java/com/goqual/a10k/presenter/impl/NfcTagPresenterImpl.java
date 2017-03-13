package com.goqual.a10k.presenter.impl;

import android.content.Context;

import com.goqual.a10k.model.realm.Nfc;
import com.goqual.a10k.model.remote.ResultDTO;
import com.goqual.a10k.model.remote.service.NfcService;
import com.goqual.a10k.presenter.NfcTagPresenter;
import com.goqual.a10k.util.LogUtil;

import java.util.ArrayList;

import io.realm.Realm;
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

    public NfcTagPresenterImpl(Context mContext, View mView) {
        this.mView = mView;
        this.mContext = mContext;
    }

    @Override
    public void loadItems(int switchId) {
        mView.loadingStart();
        LogUtil.d(TAG, "loadItems::switchId: " + switchId);
        getNfcService().getrNfcApi().gets(switchId)
                .subscribeOn(Schedulers.newThread())
                .filter(result -> result.getResult() != null)
                .map(ResultDTO::getResult)
                .filter(items -> items != null && !items.isEmpty())
                .flatMap(items -> Observable.from(items))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mView::addItem,
                        mView::onError,
                        mView::onSuccess);
    }

    @Override
    public void getItem(String tadId) {
        getNfcService().getrNfcApi().get(tadId)
                .subscribeOn(Schedulers.newThread())
                .filter(result -> result.getResult() != null)
                .map(ResultDTO::getResult)
                .filter(item -> item != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mView::addItem,
                        mView::onError,
                        mView::onSuccess);
    }

    @Override
    public void delete(int nfc_id) {
        mView.loadingStart();
        getNfcService().getrNfcApi().delete(nfc_id)
                .subscribeOn(Schedulers.newThread())
                .filter(result -> result.getResult() != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultDTO -> {
                            try (Realm realm = Realm.getDefaultInstance()) {
                                realm.executeTransaction(realm1 -> {
                                    realm1.where(Nfc.class).equalTo("_nfcid", nfc_id).findFirst().deleteFromRealm();
                                });
                            }
                            catch (Exception e) {
                                mView.onError(e);
                            }
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
                .subscribe(result -> {
                            mView.loadingStop();
                            try(Realm realm = Realm.getDefaultInstance()) {
                                realm.executeTransaction(realm1 -> {
                                    Nfc nfc = result.getResult();
                                    realm1.copyToRealm(nfc);
                                });
                            }
                            catch (Exception e) {
                                mView.onError(e);
                            }
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
