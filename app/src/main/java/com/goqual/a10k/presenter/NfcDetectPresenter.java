package com.goqual.a10k.presenter;

/**
 * Created by hanwool on 2017. 2. 28..
 */

public interface NfcDetectPresenter {
    void checkExist(int bsId, String tag);

    interface View<T>{
        void refresh(T item);
        void finishActivity();
    }
}
