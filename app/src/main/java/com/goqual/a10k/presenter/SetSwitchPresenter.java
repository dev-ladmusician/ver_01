package com.goqual.a10k.presenter;

import android.net.wifi.ScanResult;

/**
 * Created by ladmusician on 2016. 12. 8..
 */

public interface SetSwitchPresenter {
    void setName(String name);
    void startSetting10K();

    void checkSwitchConnected();
    void destroy();

    void setSelectedWifi(ScanResult wifi, String passwd);
    void set10KAP(ScanResult wifi);

    interface View<T>{
        void onConnectError();
        void onConnectSuccess();

        void onRegisterSuccess();
        void onRegisterError();

        void onSwitchConnected();
        void switchNotConnected();

        void startLoading();
        void stopLoading();
    }
}
