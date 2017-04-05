package com.goqual.a10k.presenter;

import com.goqual.a10k.model.entity.Nfc;
import com.goqual.a10k.model.realm.NfcRealm;

/**
 * Created by hanwool on 2017. 2. 28..
 */

public interface NfcTagPresenter {
    void loadItems(int switchId, int page);
    void loadItemsInRealm(int page);

    void getItem(String tadId);
    void delete(int position);
    void detectedNfc(String tag);
    void add(NfcRealm item);
    void update(Nfc item);

    interface View<T> extends BasePresenterView<T>{
        void onSuccess();
        void deleteItem(int position);
        void loadItems();
        void checkLoadMore();
    }
}
