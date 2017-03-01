package com.goqual.a10k.presenter;

import com.goqual.a10k.model.realm.Nfc;

/**
 * Created by hanwool on 2017. 2. 28..
 */

public interface NfcTagPresenter {
    void loadItems(int switchId);
    void delete(int position);
    void detectedNfc(String tag);
    void add(Nfc item);
    void update(Nfc item);

    interface View<T> extends BasePresenterView<T>{
        void onSuccess();
        void deleteItem(int position);
    }
}
