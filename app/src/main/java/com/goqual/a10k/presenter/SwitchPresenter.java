package com.goqual.a10k.presenter;

/**
 * Created by ladmusician on 2017. 2. 21..
 */

public interface SwitchPresenter {
    void loadItems();
    void deleteItem(int position, int connectionId);
    void rename(int position, int connectionId, String title);

    interface View<T> extends BasePresenterView<T> {
    }
}
