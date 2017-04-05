package com.goqual.a10k.presenter;

import com.goqual.a10k.model.entity.Nfc;

/**
 * Created by hanwool on 2017. 2. 28..
 */

public interface NfcTagCreatePresenter {
    void add(Nfc item);

    interface View<T> extends BasePresenterView<T> {
        void onSuccess();
        void onError();
        void onFinish();
    }
}
