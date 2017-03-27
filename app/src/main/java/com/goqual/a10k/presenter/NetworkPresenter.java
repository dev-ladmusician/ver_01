package com.goqual.a10k.presenter;

/**
 * Created by hanwool on 2017. 3. 16..
 */

public interface NetworkPresenter {
    void onResume();
    void onStop();

    interface View<T> extends BasePresenterView<T> {
        void onConnected();
        void onDisconnected();
    }
}
