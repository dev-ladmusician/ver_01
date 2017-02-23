package com.goqual.a10k.presenter;

/**
 * Created by ladmusician on 2016. 12. 8..
 */

public interface ConnectPresenter {
    void register(String macaddr, String title, int count);

    interface View<T> extends BasePresenterView{
        void onSuccess();
        void keyPadUp();
        void keyPadDown();
    }
}
