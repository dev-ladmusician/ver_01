package com.goqual.a10k.presenter;

/**
 * Created by ladmusician on 2017. 2. 21..
 */

public interface SwitchPresenter {
    void loadItems();
    void deleteItem(int position);
    void rename(int position, String title);

    interface View<T> extends BasePresenterView<T> {
        void onSuccessRenameSwitch(int position, String title);
    }
}
