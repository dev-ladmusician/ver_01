package com.goqual.a10k.presenter;

import android.net.wifi.ScanResult;

/**
 * Created by ladmusician on 2016. 12. 8..
 */

public interface WifiPresenter {
    void startScan();
    void onClick(int position);
    void connectToWifi(String pass);
    void connect10K();
    void setName(String name);
    void destroy();

    interface View<T>{
        void onConnectError();
        void onConnectSuccess();

        void onRegisterSuccess();

        void addAP(ScanResult bs);
        void noSwitchFound();
        void onScanEnd();
        void onScanStart();

        void openPassDialog(String ssid);
    }
}
