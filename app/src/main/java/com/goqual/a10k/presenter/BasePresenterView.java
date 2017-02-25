package com.goqual.a10k.presenter;

/**
 * Created by ladmusician on 2017. 2. 21..
 */

public interface BasePresenterView<T> {
    void loadingStart();
    void loadingStop();
    void refresh();
    void onError(Throwable e);
    void addItem(T item);
}
