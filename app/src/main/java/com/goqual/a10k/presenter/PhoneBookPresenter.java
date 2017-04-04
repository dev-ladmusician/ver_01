package com.goqual.a10k.presenter;

import android.support.annotation.Nullable;

/**
 * Created by ladmusician on 2016. 12. 8..
 */

public interface PhoneBookPresenter {
    void loadItems(@Nullable String query);
    void checkUser(int bsId, int position);

    interface View<T> extends BasePresenterView {
        void successInvite(int position);
        void errorInvite(int position);
    }
}
