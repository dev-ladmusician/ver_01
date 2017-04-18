package com.goqual.a10k.presenter;

/**
 * Created by ladmusician on 2016. 12. 8..
 */

public interface NotiPresenter extends BasePresenter{
    void acceptInvite(int switchId);

    interface View<T> extends BasePresenterView<T>{
        void checkLoadMore();
        void onSuccessInvite();
    }
}
