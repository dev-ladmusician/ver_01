package com.goqual.a10k.presenter;

import com.goqual.a10k.view.activities.ActivityPhoneAuth;

/**
 * Created by ladmusician on 2017. 2. 21..
 */

public interface PhoneAuthPresenter {
    void requestSmsToken(String phoneNumber);
    void join(String phoneNumber, String smsToken);
    String getPhoneNumber();
    String getPhoneNumberCountryCode();



    interface View<T> extends BasePresenterView<T> {
        void onSuccessPhoneNumberAuth(String phoneNumber, String auth);
        void requestStartAppAuth();
        void onSuccessAuthProcess();
        void onErrorAuthProcess(ActivityPhoneAuth.ERROR_REASON reason, String reson);
        void onEndAuthProcess();
    }
}
