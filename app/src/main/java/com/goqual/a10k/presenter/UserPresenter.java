package com.goqual.a10k.presenter;

/**
 * Created by ladmusician on 2016. 12. 8..
 */

public interface UserPresenter {
    void loadItems(int switchId);
    void delete(int position);
    void changeAdmin(int position);

    interface View<T> extends BasePresenterView<T>{
        void handleChangeAdmin(int position);
        void onErrorChangeAdmin();
    }
}
