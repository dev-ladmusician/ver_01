package com.goqual.a10k.presenter.impl;

import android.content.Context;

import com.goqual.a10k.model.entity.Nfc;
import com.goqual.a10k.model.realm.NfcRealm;
import com.goqual.a10k.model.remote.service.NfcService;
import com.goqual.a10k.presenter.NfcTagPresenter;
import com.goqual.a10k.view.adapters.model.AdapterDataModel;
import com.goqual.a10k.view.interfaces.IPaginationPage;

import io.realm.Realm;
import io.realm.RealmResults;
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
    private AdapterDataModel<Nfc> mAdapter;
    private Realm mRealm;

    public NfcTagPresenterImpl(Context mContext, View mView, AdapterDataModel model) {
        this.mView = mView;
        this.mContext = mContext;
        this.mAdapter = model;
    }

    public void loadItems(int switchId, int page) {
        mView.loadingStart();
        getNfcService().getrNfcApi().gets(switchId, page)
                .subscribeOn(Schedulers.newThread())
                .filter(result -> result.getResult() != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            ((IPaginationPage)mView).setPage(result.getPage());
                            ((IPaginationPage)mView).setLastPage(result.getLastPage());

                            for(Nfc each : result.getResult()) {
                                mView.addItem(each);

                                // insert realm
//                                mRealm.beginTransaction();
//                                mRealm.insert(new NfcRealm(each));
//                                mRealm.commitTransaction();
                            }
                        },
                        (e) -> {
                            mView.onError(e);
                            mView.loadingStop();
                        },
                        () -> {
                            mView.refresh();
                            mView.loadingStop();
                        });
    }

    @Override
    public void loadItemsInRealm(int page) {
        RealmResults<NfcRealm> realmResults = mRealm.where(NfcRealm.class).findAll();
        for(NfcRealm nfcRealm : realmResults) {
            mView.addItem(new Nfc(nfcRealm));
        }
        mView.loadingStop();
        mView.refresh();
    }

    @Override
    public void getItem(String tadId) {
//        getNfcService().getrNfcApi().get(tadId)
//                .subscribeOn(Schedulers.newThread())
//                .filter(result -> result.getResult() != null)
//                .map(ResultDTO::getResult)
//                .filter(item -> item != null)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(item -> mView.addItem(item.getRealmObject()),
//                        mView::onError,
//                        mView::onSuccess);
    }

    @Override
    public void delete(int position) {
        mView.loadingStart();
        getNfcService().getrNfcApi().delete(mAdapter.getItem(position).get_nfcid())
                .subscribeOn(Schedulers.newThread())
                .filter(result -> result.getResult() != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultDTO -> {
                            mView.deleteItem(position);
                        },
                        mView::onError,
                        () -> {
                            mView.loadingStop();
                            mView.refresh();
                        });
    }

    @Override
    public void detectedNfc(String tag) {
//        getNfcService().getrNfcApi().get(tag)
//                .subscribeOn(Schedulers.newThread())
//                .filter(result -> result.getResult() != null)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(rtv -> {
//                            mView.loadingStop();
////                            ((ActivityNfcDetected)mView).setNfc(rtv.getResult());
//                        },
//                        mView::onError,
//                        mView::onSuccess);
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
