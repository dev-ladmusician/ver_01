package com.goqual.a10k.presenter;

/**
 * Created by ladmusician on 2016. 12. 8..
 */

public interface NotiPresenter {
    void loadItems();

    interface View<T> extends BasePresenterView<T>{
    }
}
