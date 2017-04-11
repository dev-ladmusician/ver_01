package com.goqual.a10k.presenter;

import com.goqual.a10k.model.entity.Nfc;

/**
 * Created by hanwool on 2017. 2. 28..
 */

public interface NfcTagPresenter {
    void loadItems(int switchId, int page);
    void getItem(String tadId);
    void delete(int position);
    void update(Nfc item, int position);

    interface View<T> extends BasePresenterView<T>{
        void onSuccess();
        void deleteItem(int position);
        void loadItems();
        void checkLoadMore();
    }
}
